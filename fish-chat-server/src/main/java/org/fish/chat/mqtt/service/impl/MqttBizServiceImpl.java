package org.fish.chat.mqtt.service.impl;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.fish.chat.common.utils.ThreadUtils;
import org.fish.chat.mqtt.protocol.MqttException;
import org.fish.chat.mqtt.protocol.wire.*;
import org.fish.chat.mqtt.service.MqttBizService;
import org.fish.chat.mqtt.service.MqttService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * mqtt业务处理
 *
 * @author adre
 */
@Service
public class MqttBizServiceImpl implements MqttBizService, InitializingBean {

    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private UserChatService userChatService;
    @Autowired
    private MessageAckCallback messageAckCallback;
    @Autowired
    private IqHandler iqHandler;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MqttService mqttService;

    private JsonFormat pbJsonFormat;

    private ExecutorService executorService = ThreadUtils.newUnthrowThreadPool(4, 8, 10, TimeUnit.MINUTES, 10000, "mqtt-biz-service");

    /**
     * 建立连接 创建session
     * @param userId
     * @param cid
     * @param mqttConnect
     * @param ip
     * @param type
     * @return
     */
    @Override
    public MqttConnack connect(long userId, long cid, MqttConnect mqttConnect, String ip, int type) {
        String password = mqttConnect.getPassword();
        if (StringUtils.length(password) > 16) {
            //TODO check user
            String secretKey = "test";
            String random = StringUtils.substring(password, 16);
            String checksum = DigestUtils.md5Hex(secretKey + random).substring(8, 24) + random;
            if (!StringUtils.equalsIgnoreCase(password, checksum)) {
                LoggerManager.warn("MqttBizService.connect check failed. secretKey="
                        + secretKey + ", random" + random + ", checksum = " + checksum + ", password=" + password);
                return new MqttConnack(MqttConnack.REFUSED_BAD_USERNAME_PASSWORD);
            }
        } else {
            LoggerManager.warn("MqttBizService.connect password is too short . password = " + password);
            return new MqttConnack(MqttConnack.REFUSED_BAD_USERNAME_PASSWORD);
        }

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
        executorService.submit(() -> {
            try {
                userSessionService.destroyUserSession(userId, cid, UserSession.USER_SESSION_TYPE_CLIENT);
            } catch (Exception e) {
                LoggerManager.error("MqttBizServiceImpl.disconnect", e);
            }
        });
    }

    /**
     * 发布消息
     * @param userId
     * @param cid
     * @param mqttPublish
     * @return
     */
    @Override
    public boolean publish(final long userId, long cid, MqttPublish mqttPublish) {
        try {
            UserSession userSession = userSessionService.getUserSession(userId, UserSession.USER_SESSION_TYPE_CLIENT);
            if (userSession == null) {
                mqttService.close(userId, cid);
            } else {
                final ChatProtocol.FishChatProtocol protocol = ChatProtocol.FishChatProtocol.parseFrom(mqttPublish.getPayload());

                LoggerManager.info("==>publish:" + pbJsonFormat.printToString(protocol));
                executorService.submit(() -> {
                    RequestIdUtil.setRequestId(userId);
                    try {
                        switch (protocol.getType()) {
                            case ChatConstant.PROTOCOL_TYPE_MESSAGE:
                                int count = protocol.getMessagesCount();
                                for (int i = 0; i < count; i++) {
                                    ChatProtocol.FishMessage fishMessage = protocol.getMessages(i);
                                    if (fishMessage != null) {
                                        Message message = ProtocolUtil.convertMessage(fishMessage);
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
                                    UserSession userSession1 = userSessionService.getUserSession(userId, UserSession.USER_SESSION_TYPE_CLIENT);
                                    if (userSession1 != null) {
                                        Map<String, String> params = new HashMap<String, String>();
                                        List<ChatProtocol.FishKVEntry> entryList = fishIq.getParamsList();
                                        if (entryList != null && entryList.size() > 0) {
                                            for (ChatProtocol.FishKVEntry entry : entryList) {
                                                params.put(entry.getKey(), entry.getValue());
                                            }
                                        }
                                        ChatProtocol.FishChatProtocol response = iqHandler.handle(userId, fishIq.getQid(),
                                                fishIq.getQuery(), params);
                                        if (response != null) {
                                            userChatService.sendProtocol(userSession1, response, (int) fishIq.getQid());
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
                                    UserSession userSession1 = userSessionService.getUserSession(userId, UserSession.USER_SESSION_TYPE_CLIENT);
                                    if (userSession1 != null) {
                                        for (ChatProtocol.FishMessageRead fishMessageRead : messageReadList) {
                                            if (fishMessageRead != null) {
                                                messageService.readUserMessage(userId, fishMessageRead.getUserId(), fishMessageRead.getMessageId());
                                            } else {
                                                LoggerManager.warn("MqttBizServiceImpl.publish fishMessageRead is null!!!, " + pbJsonFormat.printToString(protocol));
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
            executorService.submit(() -> {
                try {
                    messageAckCallback.notifyPubAck(userId, cid, mqttPuback);
                } catch (Exception e) {
                    LoggerManager.error("MqttBizServiceImpl.disconnect", e);
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
            executorService.submit(() -> {
                try {
                    messageAckCallback.notifyPubRec(userId, cid, mqttPubRec);
                } catch (Exception e) {
                    LoggerManager.error("MqttBizServiceImpl.disconnect", e);
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
            executorService.submit(() -> {
                try {
                    messageAckCallback.notifyPubComp(userId, cid, mqttPubComp);
                } catch (Exception e) {
                    LoggerManager.error("MqttBizServiceImpl.disconnect", e);
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
}
