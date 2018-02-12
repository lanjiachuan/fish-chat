package org.fish.chat.chat.service.impl;

import org.fish.chat.chat.callback.MessageFinishCallback;
import org.fish.chat.chat.service.MessageService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 聊天实现类
 *
 * @author adre
 */
@Service
public class UserChatServiceImpl implements UserChatService, DeliverService, MessageFinishCallback, InitializingBean {

    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private ChatFilter chatFilter;
    @Autowired
    private DeliverService deliverService;
    @Autowired
    private MqttService mqttService;
    @Autowired
    private MessageService messageService;

    private JsonFormat pbJsonFormat = new JsonFormat();

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userSessionService, "userSessionService is required!");
        Assert.notNull(chatFilter, "chatFilter is required!");
        Assert.notNull(deliverService, "deliverService is required!");
        Assert.notNull(mqttService, "mqttService is required!");
    }

    @Override
    public void sendUnreadMessage(UserSession userSession) {
        RequestIdUtil.setRequestId(userSession.getUserId());
        List<Message> messages = messageService.getUnreadMessage(userSession.getUserId(), userSession.getType());
        if (CollectionUtils.isEmpty(messages)) {
            LoggerManager.error("UserChatServiceImpl.sendUnreadMessage error userId = " + userSession.getUserId() + " identity = " + userSession.getType());
            return;
        }
        int count = messages != null ? messages.size() : 0;
        LoggerManager.info("UserChatServiceImpl.sendUnreadMessage find " + count + " message wait to send! userId = " + userSession.getUserId());
        LoggerManager.info("UserChatServiceImpl.sendUnreadMessage userId = " + userSession.getUserId());
        sendMessage(userSession, messages);
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
        //本地或网络投递
        deliverService.deliverMessage(message.getTo().getUid(), message);
        chatFilter.afterDeliver(fromUserSession, message);
    }

    @Override
    public void dispatchSystemMessage(long userId, int type, Message message) {
        if (message.getFrom().getUid() == message.getTo().getUid()) {
            LoggerManager.warn("UserChatServiceImpl.dispatchSystemMessage not allow send message to yourself, userId = " + userId + "message = " + message);
            return;
        }
        UserSession tempUserSession = new UserSession();
        tempUserSession.setUserId(userId);
        tempUserSession.setType(type);
        tempUserSession.setTemp(true);
        dispatchMessage(tempUserSession, message);
    }

    private void sendMessage(UserSession userSession, Message message) {
        sendMessage(userSession, message, ChatConstant.QOS_TYPE_EXACTLY_ONCE);
    }

    private void sendMessage(UserSession userSession, Message message, int qos) {
        List<Message> messageList = new ArrayList<Message>();
        messageList.add(message);
        sendMessage(userSession, messageList, qos);
    }

    private void sendMessage(UserSession userSession, List<Message> messageList, int qos) {
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

                    String jsonResponse = pbJsonFormat.printToString(protocol);
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
                    String jsonResponse = pbJsonFormat.printToString(protocol);
                    LoggerManager.debug("sendMessage2web result:" + jsonResponse);
                    //cometService.sendResponse(userSession.getUserId(), userSession.getCid(), "application/json;charset=UTF-8", jsonResponse);
                    // TODO
                }
            }
        }
    }

    @Override
    public void sendMessage(long userId, Message message, int sessionType) {
        UserSession userSession = userSessionService.getUserSession(userId, sessionType);
        if (userSession != null) {
            sendMessage(userSession, message);
        }
    }

    /**
     * 下行
     *
     * @param userId
     */
    @Override
    public void sendMessage(long userId, List<Message> messageList, int sessionType) {
        UserSession userSession = userSessionService.getUserSession(userId, sessionType);
        if (userSession != null) {
            sendMessage(userSession, messageList);
        }
    }

    private void sendMessage(UserSession userSession, List<Message> messageList) {
        sendMessage(userSession, messageList, ChatConstant.QOS_TYPE_EXACTLY_ONCE);
    }

    /**
     * 下行报文
     */
    @Override
    public void sendProtocol(UserSession userSession, ChatProtocol.FishChatProtocol protocol, int msgId) {
        if (userSession != null && protocol != null) {
            LoggerManager.info("userId=" + userSession.getUserId() + "sendProtocol:" + pbJsonFormat.printToString(protocol));
            MqttMessage message = new MqttMessage(protocol.toByteArray());
            message.setQos(ChatConstant.QOS_TYPE_EXACTLY_ONCE);
            MqttPublish publish = new MqttPublish(ChatConstant.DEFAULT_TOPIC_NAME, message);
            publish.setMessageId(msgId);
            mqttService.publish(userSession.getUserId(), userSession.getCid(), publish);
        }
    }

    /**
     * 下行
     */
    @Override
    public void syncMessageId(UserSession userSession, long clientMsgId, long msgId) {
        if (userSession != null && userSession.getType() == UserSession.USER_SESSION_TYPE_CLIENT) {
            ChatProtocol.FishChatProtocol protocol = ProtocolUtil.getMessageSyncProtocol(clientMsgId, msgId);
            LoggerManager.info("userId=" + userSession.getUserId() + " ,syncMessageId:" + pbJsonFormat.printToString(protocol));
            MqttMessage message = new MqttMessage(protocol.toByteArray());
            message.setQos(ChatConstant.QOS_TYPE_EXACTLY_ONCE);
            MqttPublish publish = new MqttPublish(ChatConstant.DEFAULT_TOPIC_NAME, message);
            publish.setMessageId((int) clientMsgId);
            mqttService.publish(userSession.getUserId(), userSession.getCid(), publish);
        }
    }

    private ExecutorService executorService = new ThreadPoolExecutor(8, 8, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100000));

    @Override
    public void deliverMessage(final long toUserId, final Message message) {
        executorService.submit(() -> {
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
        });
    }

    /**
     * set message status received
     * @param userId
     * @param messageList
     */
    @Override
    public void messageFinish(long userId, List<Message> messageList) {
        UserSession userSession = userSessionService.getUserSession(userId, UserSession.USER_SESSION_TYPE_CLIENT);
        if (userSession != null) {
            Set<Long> idSet = new HashSet<>();
            for (Message message : messageList) {
                if (message != null && message.getTo().getUid() == userId && !message.isReceived()) {
                    idSet.add(message.getId());
                }
            }
            if (idSet.size() > 0) {
                // TODO: 2017/1/7 set message status 
//                messageApi.receiveMessage(userId, userSession.getIdentity(), new ArrayList<Long>(idSet));
            }
        }
    }
    
}
