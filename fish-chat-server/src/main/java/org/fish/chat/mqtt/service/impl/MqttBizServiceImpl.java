/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.service.impl;


import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.fish.chat.chat.ChatProtocol;
import org.fish.chat.chat.callback.MessageAckCallback;
import org.fish.chat.chat.handler.IqHandler;
import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.UserSession;
import org.fish.chat.chat.service.MessageService;
import org.fish.chat.chat.service.UserChatService;
import org.fish.chat.chat.service.UserSessionService;
import org.fish.chat.chat.utils.ProtocolUtil;
import org.fish.chat.common.constants.ChatConstant;
import org.fish.chat.mqtt.protocol.MqttException;
import org.fish.chat.mqtt.protocol.wire.*;
import org.fish.chat.mqtt.service.MqttBizService;
import org.fish.chat.mqtt.service.MqttService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Comments for MqttBizServiceImpl.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月10日 下午3:13:54
 */
public class MqttBizServiceImpl implements MqttBizService, InitializingBean {

    private UserSessionService userSessionService;
    private UserChatService userChatService;
    private MessageAckCallback messageAckCallback;
    private IqHandler iqHandler;
    private MessageService messageService;
    private MqttService mqttService;

    private ExecutorService executorService = new ThreadPoolExecutor(4, 8, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(10000));

    @Override
    public MqttConnack connect(long userId, long cid, MqttConnect mqttConnect, String ip, int type) {
        String password = mqttConnect.getPassword();
//        if (StringUtils.length(password) > 16) {
            //TODO check user
//            String secretKey = passportResponse.getResData().getUserSecretKey();
//            String random = StringUtils.substring(password, 16);
//            String checksum = DigestUtils.md5Hex(secretKey + random).substring(8, 24) + random;
//            if (!StringUtils.equalsIgnoreCase(password, checksum)) {
//                LoggerManager.warn("MqttBizService.connect check failed. secretKey="
//                        + secretKey + ", random" + random + ", checksum = " + checksum + ", password=" + password);
//                return new MqttConnack(MqttConnack.REFUSED_BAD_USERNAME_PASSWORD);
//            }
//        } else {
//            LoggerManager.warn("MqttBizService.connect password is too short . password = " + password);
//            return new MqttConnack(MqttConnack.REFUSED_BAD_USERNAME_PASSWORD);
//        }

        float protocolVersion = 1.0f;

        String[] params = mqttConnect.getUserName().split("-");
        if (params.length >= 3) {
            protocolVersion = NumberUtils.toFloat(params[2]);
        }

        UserSession userSession = userSessionService.connect(userId, cid, ip, type,
                mqttConnect.getClientId(), UserSession.USER_SESSION_TYPE_CLIENT, protocolVersion);
        if (userSession != null) {
            return new MqttConnack(MqttConnack.ACCEPTED);
        } else {
            LoggerManager.warn("MqttBizService.connect userSessionService.connect return null!");
            return new MqttConnack(MqttConnack.REFUSED_SERVER_UNAVAILABLE);
        }
    }

    @Override
    public void disconnect(final long userId, final long cid, MqttDisconnect mqttDisconnect) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    userSessionService.destroyUserSession(userId, cid, UserSession.USER_SESSION_TYPE_CLIENT);
                } catch (Exception e) {
                    LoggerManager.error("MqttBizServiceImpl.disconnect", e);
                }
            }
        });
    }

    @Override
    public boolean publish(final long userId, long cid, MqttPublish mqttPublish) {
        try {
            UserSession userSession = userSessionService.getUserSession(userId, UserSession.USER_SESSION_TYPE_CLIENT);
            if (userSession == null) {
                mqttService.close(userId, cid);
            } else {
                final ChatProtocol.FishChatProtocol protocol = ChatProtocol.FishChatProtocol.parseFrom(mqttPublish.getPayload());
                LoggerManager.info("==>publish:" + JsonFormat.printToString(protocol));
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        RequestIdUtil.setRequestId(userId);
                        try {
                            switch (protocol.getType()) {
                                case ChatConstant.PROTOCOL_TYPE_MESSAGE:
                                    int count = protocol.getMessagesCount();
                                    for (int i = 0; i < count; i++) {
                                        ChatProtocol.FishMessage techwolfMessage = protocol.getMessages(i);
                                        if (techwolfMessage != null) {
                                            Message message = ProtocolUtil.convertMessage(techwolfMessage);
                                            userChatService.dispatchMessage(userId, message, UserSession.USER_SESSION_TYPE_CLIENT);
                                        }
                                    }
                                    break;
                                case ChatConstant.PROTOCOL_TYPE_PRESENCE:
                                    userSessionService.presence(userId, protocol.getPresence(), UserSession.USER_SESSION_TYPE_CLIENT);
                                    break;
                                case ChatConstant.PROTOCOL_TYPE_IQ:
                                    ChatProtocol.FishIq fishIq = protocol.getIq();
                                    if (fishIq != null) {
                                        UserSession userSession = userSessionService.getUserSession(userId, UserSession.USER_SESSION_TYPE_CLIENT);
                                        if (userSession != null) {
                                            Map<String, String> params = new HashMap<String, String>();
                                            List<ChatProtocol.FishKVEntry> entryList = fishIq.getParamsList();
                                            if (entryList != null && entryList.size() > 0) {
                                                for (ChatProtocol.FishKVEntry entry : entryList) {
                                                    params.put(entry.getKey(), entry.getValue());
                                                }
                                            }
                                            ChatProtocol.FishChatProtocol response = iqHandler.handle(userId, userSession.getIdentity(), fishIq.getQid(),
                                                    fishIq.getQuery(), params);
                                            if (response != null) {
                                                userChatService.sendProtocol(userSession, response, (int) fishIq.getQid());
                                            } else {
                                                LoggerManager.warn("qid =" + fishIq.getQid() + ",query=" + fishIq.getQuery() + ", return null");
                                            }
                                        } else {
                                            LoggerManager.warn("userSession was null!");
                                        }
                                    }
                                    break;
                                case ChatConstant.PROTOCOL_TYPE_IQ_RESPONSE:
                                    break;
                                case ChatConstant.PROTOCOL_TYPE_IQ_MESSAGE_SYNC:
                                    break;
                                case ChatConstant.PROTOCOL_TYPE_MESSAGE_READ:
                                    List<ChatProtocol.FishMessageRead> messageReadList = protocol.getMessageReadList();
                                    if (CollectionUtils.isNotEmpty(messageReadList)) {
                                        UserSession userSession = userSessionService.getUserSession(userId, UserSession.USER_SESSION_TYPE_CLIENT);
                                        if (userSession != null) {
                                            for (ChatProtocol.FishMessageRead techwolfMessageRead : messageReadList) {
                                                if (techwolfMessageRead != null) {
                                                    messageService.readUserMessage(userId, userSession.getIdentity(), techwolfMessageRead.getUserId(), techwolfMessageRead.getMessageId());
                                                } else {
                                                    LoggerManager.warn("MqttBizServiceImpl.publish techwolfMessageRead is null!!!, " + JsonFormat.printToString(protocol));
                                                }
                                            }
                                        } else {
                                            LoggerManager.warn("MqttBizServiceImpl.publish userSession is null when received message read!");
                                        }
                                    }
                                    break;
                                default:
                                    LoggerManager.warn("undefined protocol type :" + protocol.getType());
                            }
                        } catch (Exception e) {
                            LoggerManager.error("", e);
                        }
                    }
                });

            }
        } catch (MqttException e) {
            LoggerManager.error("", e);
            return false;
        } catch (InvalidProtocolBufferException e) {
            LoggerManager.error("", e);
            return false;
        }
        return true;
    }

    @Override
    public void ping(long userId, long cid) {
        UserSession userSession = userSessionService.getUserSession(userId, UserSession.USER_SESSION_TYPE_CLIENT);
        if (userSession != null) {
            userSessionService.heartBeat(userId, UserSession.USER_SESSION_TYPE_CLIENT);
        } else {
            mqttService.close(userId, cid);
        }
    }

    @Override
    public boolean pubAck(final long userId, final long cid, final MqttPubAck mqttPuback) {
        UserSession userSession = userSessionService.getUserSession(userId, UserSession.USER_SESSION_TYPE_CLIENT);
        if (userSession != null) {
            executorService.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        messageAckCallback.notifyPubAck(userId, cid, mqttPuback);
                    } catch (Exception e) {
                        LoggerManager.error("MqttBizServiceImpl.disconnect", e);
                    }
                }

            });
            return true;
        } else {
            mqttService.close(userId, cid);
        }

        return false;
    }

    @Override
    public boolean pubRec(final long userId, final long cid, final MqttPubRec mqttPubRec) {
        UserSession userSession = userSessionService.getUserSession(userId, UserSession.USER_SESSION_TYPE_CLIENT);
        if (userSession != null) {
            executorService.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        messageAckCallback.notifyPubRec(userId, cid, mqttPubRec);
                    } catch (Exception e) {
                        LoggerManager.error("MqttBizServiceImpl.disconnect", e);
                    }
                }

            });
        } else {
            mqttService.close(userId, cid);
        }

        return true;
    }

    @Override
    public boolean pubRel(long userId, long cid, MqttPubRel mqttPubRel) {
        return true;
    }

    @Override
    public boolean pubComp(final long userId, final long cid, final MqttPubComp mqttPubComp) {
        UserSession userSession = userSessionService.getUserSession(userId, UserSession.USER_SESSION_TYPE_CLIENT);
        if (userSession != null) {
            executorService.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        messageAckCallback.notifyPubComp(userId, cid, mqttPubComp);
                    } catch (Exception e) {
                        LoggerManager.error("MqttBizServiceImpl.disconnect", e);
                    }
                }

            });
            return true;
        } else {
            mqttService.close(userId, cid);
        }
        return false;
    }

    @Override
    public boolean subscribe(long userId, long cid, MqttSubscribe mqttSubscribe) {
        return true;
    }

    @Override
    public boolean unsubscribe(long userId, long cid, MqttUnsubscribe mqttUnubscribe) {
        return true;
    }

    @Override
    public void channelInactive(long userId, long cid) {
        userSessionService.disconnect(userId, cid, UserSession.USER_SESSION_TYPE_CLIENT);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userSessionService, "userSessionService must not null!");
        Assert.notNull(userChatService, "userChatService must not null!");
        Assert.notNull(messageAckCallback, "messageAckCallback must not null!");
        Assert.notNull(mqttService, "mqttService must not null!");
    }

    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    public void setUserChatService(UserChatService userChatService) {
        this.userChatService = userChatService;
    }

    public void setMessageAckCallback(MessageAckCallback messageAckCallback) {
        this.messageAckCallback = messageAckCallback;
    }


    public void setIqHandler(IqHandler iqHandler) {
        this.iqHandler = iqHandler;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setMqttService(MqttService mqttService) {
        this.mqttService = mqttService;
    }
}
