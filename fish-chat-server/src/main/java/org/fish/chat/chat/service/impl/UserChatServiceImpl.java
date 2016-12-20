package org.fish.chat.chat.service.impl;

import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import com.googlecode.protobuf.format.JsonFormat;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.fish.chat.chat.ChatProtocol;
import org.fish.chat.chat.filter.ChatFilter;
import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.TextMessage;
import org.fish.chat.chat.model.UserSession;
import org.fish.chat.chat.service.DeliverService;
import org.fish.chat.chat.service.UserChatService;
import org.fish.chat.chat.service.UserSessionService;
import org.fish.chat.chat.utils.ProtocolUtil;
import org.fish.chat.common.constants.ChatConstant;
import org.fish.chat.mqtt.protocol.MqttMessage;
import org.fish.chat.mqtt.protocol.wire.MqttPublish;
import org.fish.chat.mqtt.service.MqttService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * User: bdq
 * Date: 11/12/15
 * Time: 16:34
 */
public class UserChatServiceImpl implements UserChatService, DeliverService, InitializingBean {

    private UserSessionService userSessionService;
    private ChatFilter chatFilter;
    private DeliverService deliverService;
    private MqttService mqttService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userSessionService, "userSessionService is required!");
        Assert.notNull(chatFilter, "chatFilter is required!");
        Assert.notNull(deliverService, "deliverService is required!");
        Assert.notNull(mqttService, "mqttService is required!");
    }

    @Override
    public void sendUnreadMessage(UserSession userSession) {

    }

    @Override
    public void dispatchMessage(long userId, Message message, int sessionType) {
        RequestIdUtil.setRequestId(userId);
        LoggerManager.info("dispatchMessage message = " + message);
        if (message.getFrom().getUid() != userId) {
            LoggerManager.warn(userId + " send a message from id is " + message.getFrom().getUid() + ", drop it!");
            return;
        }
        if (message.getFrom().getUid() == message.getTo().getUid()) {
            LoggerManager.warn("UserChatServiceImpl.dispatchMessage not allow send message to yourself, userId = " + userId + "message = " + message);
            return;
        }
        UserSession fromUserSession = userSessionService.getUserSession(userId, sessionType);
        dispatchMessage(fromUserSession, message);
    }

    private void dispatchMessage(UserSession fromUserSession, Message message) {
        if (fromUserSession == null) {
            LoggerManager.warn("UserChatServiceImpl.dispatchMessage found fromUserSession is null!! why?");
            return;
        }
        RequestIdUtil.setRequestId(fromUserSession.getUserId());
        LoggerManager.info("userId=" + fromUserSession.getUserId() + ", dispatch message=" + message);
        if (!chatFilter.beforeDeliver(fromUserSession, message)) {
            LoggerManager.info("filter return false, shop dispatch message = " + message);
            return;
        }
        deliverService.deliverMessage(message.getTo().getUid(), message);//本地或网络投递
        chatFilter.afterDeliver(fromUserSession, message);
    }

    @Override
    public void dispatchSystemMessage(long userId, int identity, Message message) {

    }

    public void sendMessage(UserSession userSession, Message message) {
        sendMessage(userSession, message, ChatConstant.QOS_TYPE_EXACTLY_ONCE);
    }

    public void sendMessage(UserSession userSession, Message message, int qos) {
        List<Message> messageList = new ArrayList<Message>();
        messageList.add(message);
        sendMessage(userSession, messageList, qos);
    }

    public void sendMessage(UserSession userSession, List<Message> messageList, int qos) {
        if (messageList != null && messageList.size() > 0) {
            if (CollectionUtils.isNotEmpty(messageList)) {

                if (userSession.getType() == UserSession.USER_SESSION_TYPE_CLIENT) {
                    ChatProtocol.FishChatProtocol.Builder builder = ChatProtocol.FishChatProtocol.newBuilder();
                    builder.setType(ChatConstant.PROTOCOL_TYPE_MESSAGE);
                    for (Message message : messageList) {
                        builder.addMessages(ProtocolUtil.convertFishMessage(message));
                    }
                    ChatProtocol.FishChatProtocol protocol = builder.build();
                    MqttMessage message = new MqttMessage(protocol.toByteArray());
                    message.setQos(qos);
                    MqttPublish publish = new MqttPublish(ChatConstant.DEFAULT_TOPIC_NAME, message);

                    short mqttMsgId = (short) ((messageList.get(0).getId() % (Short.MAX_VALUE - 1)) + 1);
                    mqttMsgId = mqttMsgId == 0 ? 1 : mqttMsgId;
                    publish.setMessageId(mqttMsgId);

                    String jsonResponse = JsonFormat.printToString(protocol);
                    LoggerManager.info("Send to userSession=" + userSession + " : " + jsonResponse);
                    mqttService.publish(userSession.getUserId(), userSession.getCid(), publish);
                } else if (userSession.getType() == UserSession.USER_SESSION_TYPE_WEB) {
                    //发送给webSession

                    ChatProtocol.FishChatProtocol.Builder builder = ChatProtocol.FishChatProtocol.newBuilder();
                    builder.setType(ChatConstant.PROTOCOL_TYPE_MESSAGE);
                    for (Message message : messageList) {
                        if (message instanceof TextMessage) {
                            TextMessage textMessage = (TextMessage) message;
                            textMessage.setText(StringEscapeUtils.escapeHtml3(textMessage.getText()));//过滤xss
                        }
                        builder.addMessages(ProtocolUtil.convertFishMessage(message));
                    }
                    ChatProtocol.FishChatProtocol protocol = builder.build();
                    String jsonResponse = JsonFormat.printToString(protocol);
                    LoggerManager.debug("sendMessage2web result:" + jsonResponse);
                    //cometService.sendResponse(userSession.getUserId(), userSession.getCid(), "application/json;charset=UTF-8", jsonResponse);
                    // TODO
                }
            }
        }
    }

    @Override
    public void sendMessage(long userId, Message message, int sessionType) {

    }

    /**
     * 下行
     *
     * @param userId
     */
    @Override
    public void sendMessage(long userId, List<Message> messageList, int sessionType) {

    }

    /**
     * 下行报文
     */
    @Override
    public void sendProtocol(UserSession userSession, ChatProtocol.FishChatProtocol protocol, int msgId) {

    }

    /**
     * 下行
     */
    @Override
    public void syncMessageId(UserSession userSession, long clientMsgId, long msgId) {

    }

    private ExecutorService executorService = new ThreadPoolExecutor(8, 8, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100000));

    @Override
    public void deliverMessage(final long toUserId, final Message message) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestIdUtil.setRequestId(toUserId);
                    List<UserSession> toUserSessionList = userSessionService.getAllUserSession(toUserId);

                    if (toUserSessionList != null && toUserSessionList.size() > 0) {
                        for (UserSession toUserSession : toUserSessionList) {
                            if (toUserSession != null) {
                                if (toUserSession.getStatus() != UserSession.USER_SESSION_STATUS_WAIT) {
                                    LoggerManager.info("find userSession=" + toUserSession + " to send message = " + message);
                                    //发送消息前过滤
                                    if (!chatFilter.beforeSendMessage(toUserSession, message)) {
                                        LoggerManager.info("filter beforeSendMessage return false, message will not send 。message = " + message);
                                        return;
                                    }
                                    try {
                                        sendMessage(toUserSession, message);
                                    } catch (Exception e) {
                                        LoggerManager.error("deliverMessage sendMessage has exception", e);
                                    }
                                    LoggerManager.info("from=" + message.getFrom().getUid() + ", to=" + toUserSession + " send Message " + message + " finish!");
                                    //发送消息后
                                    chatFilter.afterSendMessage(toUserSession, message);
                                } else {
                                    //等待状态或者死亡状态
                                    //用户类型一致
                                    LoggerManager.warn("message=" + message + " will not send , the resson is one of below, to = " + toUserSession);
                                    LoggerManager.warn("1、user was unavailable status = " + toUserSession.getStatus());
                                    LoggerManager.warn("2、user type is same, fromUserType=" + message.getFrom().getIdentity());
                                }
                            }
                        }
                    } else {
                        chatFilter.onSendMessageNotOnline(toUserId, message);
                        LoggerManager.info("user [" + toUserId + "] was not online!" + message.getTo().getIdentity() + "|" + message.getFrom().getIdentity());
                    }
                } catch (Exception e) {
                    LoggerManager.error("UserChatServiceImpl.deliverMessage has exception", e);
                }
            }
        });
    }

    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    public void setChatFilter(ChatFilter chatFilter) {
        this.chatFilter = chatFilter;
    }

    public void setDeliverService(DeliverService deliverService) {
        this.deliverService = deliverService;
    }

    public void setMqttService(MqttService mqttService) {
        this.mqttService = mqttService;
    }
}
