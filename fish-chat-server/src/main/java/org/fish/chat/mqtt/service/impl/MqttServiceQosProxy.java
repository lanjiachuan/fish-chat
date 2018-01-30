package org.fish.chat.mqtt.service.impl;

import com.google.common.cache.*;
import org.fish.chat.chat.ChatProtocol;
import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import org.fish.chat.chat.callback.MessageAckCallback;
import org.fish.chat.chat.callback.MessageFinishCallback;
import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.UserSession;
import org.fish.chat.chat.service.UserSessionService;
import org.fish.chat.chat.utils.ProtocolUtil;
import org.fish.chat.common.constants.ChatConstant;
import org.fish.chat.mqtt.protocol.MqttException;
import org.fish.chat.mqtt.protocol.wire.*;
import org.fish.chat.mqtt.qos.QosService;
import org.fish.chat.mqtt.service.MqttService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Comments for MqttServiceQosProxy.java
 *
 */
public class MqttServiceQosProxy implements MqttService, MessageAckCallback, InitializingBean,
        RemovalListener<Long, MqttPersistableWireMessage> {

    private static final int TRY_TIMES = 3;

    private static final int EXPIRE_TIME = 10;

    private UserSessionService userSessionService;

    private MessageFinishCallback messageFinishCallback;

    private MqttService mqttService;

    private QosService qosService;

    private final Cache<Long, MqttPersistableWireMessage> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(EXPIRE_TIME, TimeUnit.SECONDS).removalListener(this).build();

    private final static ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(5);

    @Override
    public boolean publish(long userId, long cid, MqttPublish publish) {
        RequestIdUtil.setRequestId(userId);
        int qos = publish.getMessage().getQos();
        if (qos > 0) {
            int pos = qosService.addMessage(userId, publish);
            if (pos == 1) {
                LoggerManager.info("<==puslish: qos queue is empty, immediately send 。 messageId = "
                                + publish.getMessageId());
                //当前队列没有消息， 可以直接发送消息
                writeAndRetry(userId, publish);
            } else {
                LoggerManager.info("<==puslish: qos queue is not empty, pos = " + pos
                        + ", messageId = " + publish.getMessageId());
            }
        } else {
            LoggerManager.info("<==qos = 0 ,  immediately send");
            mqttService.publish(userId, cid, publish);
            messageFinish(userId, publish);
        }

        return true;
    }

    @Override
    public boolean pubRel(long userId, long cid, MqttPubRel mqttPubRel) {
        RequestIdUtil.setRequestId(userId);
        MqttPersistableWireMessage message = qosService.getInFlightMessage(userId);
        if (message != null && message.getMessageId() == mqttPubRel.getMessageId()) {
            LoggerManager.info("<==pubRel: pub release to client success, msgId = "
                    + mqttPubRel.getMessageId());
            qosService.updateMessage(userId, mqttPubRel);
        } else {
            LoggerManager.debug("pub release to client faild . publish = " + message + " , pubrel="
                    + mqttPubRel);
        }
        writeAndRetry(userId, mqttPubRel);//将消息替换为状态
        return true;
    }

    @Override
    public boolean notifyPubRec(long userId, long cid, MqttPubRec mqttPubRec) {
        RequestIdUtil.setRequestId(userId);
        return pubRel(userId, cid, new MqttPubRel(mqttPubRec));
    }

    @Override
    public boolean notifyPubAck(long userId, long cid, MqttPubAck mqttPubAck) {
        RequestIdUtil.setRequestId(userId);
        MqttPersistableWireMessage message = qosService.getInFlightMessage(userId);
        if (message instanceof MqttPublish) {
            if (message != null && message.getMessageId() == mqttPubAck.getMessageId()) {
                sendNext(userId);
                messageFinish(userId, (MqttPublish) message);
                return true;
            } else {
                LoggerManager.warn("MqttPubAck = " + mqttPubAck + " , publish =" + message);
            }
        }
        return false;

    }

    @Override
    public boolean notifyPubComp(long userId, long cid, MqttPubComp mqttPubComp) {
        RequestIdUtil.setRequestId(userId);
        MqttPersistableWireMessage message = qosService.getInFlightMessage(userId);
        if (message instanceof MqttPubRel) {
            if (message != null && message.getMessageId() == mqttPubComp.getMessageId()) {
                sendNext(userId);
                MqttPublish publish = qosService.getInflightPublish(userId, true);
                messageFinish(userId, publish);
                return true;
            } else {
                LoggerManager.warn("PubComp = " + mqttPubComp + " , publish =" + message);
            }
        }
        return false;

    }

    /**
     * 消息客户端已经成功接收
     *
     * @param publish
     * @return
     */
    public boolean messageFinish(long userId, MqttPublish publish) {
        RequestIdUtil.setRequestId(userId);
        try {
            if(publish!=null) {
                ChatProtocol.FishChatProtocol protocol = ChatProtocol.FishChatProtocol.parseFrom(publish.getPayload());
                //处理已读消息
                int count = protocol.getMessagesCount();
                List<Message> messageList = new ArrayList<Message>();
                for (int i = 0; i < count; i++) {
                    Message message = ProtocolUtil.convertMessage(protocol.getMessages(i));
                    messageList.add(message);
                }
                if (messageList.size() > 0) {
                    messageFinishCallback.messageFinish(userId, messageList);
                }

                switch (protocol.getType()) {
                    case ChatConstant.PROTOCOL_TYPE_MESSAGE:
                        break;
                    case ChatConstant.PROTOCOL_TYPE_PRESENCE:
                        break;
                    case ChatConstant.PROTOCOL_TYPE_IQ:
                        break;
                    case ChatConstant.PROTOCOL_TYPE_IQ_RESPONSE:
                        break;
                    case ChatConstant.PROTOCOL_TYPE_IQ_MESSAGE_SYNC:
                        break;
                    default:
                        LoggerManager.warn("undefined protocol = " + protocol);

                }
            }
            else {
                LoggerManager.warn(String.format("userId= %s, messageFinish publish is null", userId));
            }
        } catch (InvalidProtocolBufferException e) {
            LoggerManager.error("", e);
        } catch (MqttException e) {
            LoggerManager.error("", e);
        }
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(mqttService, "mqttService must not null!");
        Assert.notNull(qosService, "qosService must not null!");
        scheduled.scheduleWithFixedDelay(() -> {
            try {
                cache.cleanUp();
                LoggerManager.debug("clean up guava Cache , current guava size: " + cache.size());
            } catch (Exception e) {
                LoggerManager.error("clean up guava Cache error! ", e);
            }

        }, 2, 1, TimeUnit.SECONDS);
    }

    /**
     * @param mqttService the mqttService to set
     */
    public void setMqttService(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    /**
     * @param qosService the qosService to set
     */
    public void setQosService(QosService qosService) {
        this.qosService = qosService;
    }

    private void writeAndRetry(long userId, MqttPersistableWireMessage message) {
        RequestIdUtil.setRequestId(userId);
        cache.put(userId, message);
        UserSession userSession = userSessionService.getUserSession(userId,
                UserSession.USER_SESSION_TYPE_CLIENT);
        if (userSession == null) {
            LoggerManager.warn("MqttServiceQosProxy.writeAndRetry userSession is null, userId ="
                    + userId);
            return;
        }
        if (message instanceof MqttPublish) {
            mqttService.publish(userSession.getUserId(), userSession.getCid(),
                    (MqttPublish) message);
        } else {
            mqttService.pubRel(userSession.getUserId(), userSession.getCid(), (MqttPubRel) message);
        }

    }

    private void sendNext(long userId) {
        cache.invalidate(userId);
        MqttPersistableWireMessage message = qosService.getNextMessage(userId);
        if (message != null) {
            writeAndRetry(userId, message);
        }
    }

    @Override
    public void onRemoval(RemovalNotification<Long, MqttPersistableWireMessage> notification) {
        if (notification.getCause() == RemovalCause.EXPIRED) {
            MqttPersistableWireMessage message = notification.getValue();
            int sendTimes = message.getSendTimes();
            if (sendTimes <= TRY_TIMES) {
                message.setDuplicate(true);
                message.setSendTimes(sendTimes + 1);
                LoggerManager.warn("retry send message = " + message);
                writeAndRetry(notification.getKey(), message);
            } else {
                LoggerManager.warn(" try" + TRY_TIMES + " times (message = " + message
                        + "), throw it and send nextMessage");
                //超过次数,发送下一条
                sendNext(notification.getKey());
            }
        }
    }

    /**
     * @param userSessionService the userSessionService to set
     */
    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    /**
     * @param messageFinishCallback the messageFinishCallback to set
     */
    public void setMessageFinishCallback(MessageFinishCallback messageFinishCallback) {
        this.messageFinishCallback = messageFinishCallback;
    }

    @Override
    public boolean publishAndClose(long userId, long cid, MqttPublish publish) {
        publish.getMessage().setQos(0);
        return mqttService.publishAndClose(userId, cid, publish);
    }

    @Override
    public boolean close(long userId, long cid) {
        return mqttService.close(userId, cid);
    }

}
