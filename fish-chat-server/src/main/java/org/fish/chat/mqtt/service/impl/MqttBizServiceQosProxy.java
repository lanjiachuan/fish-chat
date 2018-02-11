package org.fish.chat.mqtt.service.impl;

import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import org.fish.chat.mqtt.protocol.MqttMessage;
import org.fish.chat.mqtt.protocol.wire.*;
import org.fish.chat.mqtt.qos.QosService;
import org.fish.chat.mqtt.service.MqttBizService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 代理模式
 *
 * @author adre
 */
@Service
public class MqttBizServiceQosProxy implements MqttBizService, InitializingBean {

    @Autowired
    private MqttBizService mqttBizService;
    @Autowired
    private QosService qosService;

    @Override
    public MqttConnack connect(long userId, long cid, MqttConnect mqttConnect, String ip, int type) {
        RequestIdUtil.setRequestId(userId);
        try {
            return mqttBizService.connect(userId, cid, mqttConnect, ip, type);
        } catch (Exception e) {
            LoggerManager.error("", e);
            return new MqttConnack(MqttConnack.REFUSED_SERVER_UNAVAILABLE);
        }
    }

    @Override
    public void disconnect(long userId, long cid, MqttDisconnect mqttDisconnect) {
        RequestIdUtil.setRequestId(userId);
        try {
            mqttBizService.disconnect(userId, cid, mqttDisconnect);
        } catch (Exception e) {

        }
    }

    @Override
    public boolean publish(long userId, long cid, MqttPublish mqttPublish) {
        RequestIdUtil.setRequestId(userId);
        try {
            int qos = mqttPublish.getMessage().getQos();
            if (qos == MqttMessage.QOS_EXACTLY_ONCE) {
                LoggerManager
                        .info("==>MqttBizServiceQosProxy : message not send , wait pubrel to send msgId="
                                + mqttPublish.getMessageId());
                int index = qosService.setClientFlightQueueMessage(userId, mqttPublish);
                return index > -1;
            } else {
                mqttBizService.publish(userId, cid, mqttPublish);
            }
            return true;

        } catch (Exception e) {
            LoggerManager.error("", e);
            return false;
        }

    }

    @Override
    public boolean pubAck(long userId, long cid, MqttPubAck mqttPuback) {
        RequestIdUtil.setRequestId(userId);
        try {
            return mqttBizService.pubAck(userId, cid, mqttPuback);
        } catch (Exception e) {
            LoggerManager.error("", e);
        }
        return false;
    }

    @Override
    public boolean pubRec(long userId, long cid, MqttPubRec mqttPubRec) {
        RequestIdUtil.setRequestId(userId);
        try {
            return mqttBizService.pubRec(userId, cid, mqttPubRec);
        } catch (Exception e) {
            LoggerManager.error("", e);
        }
        return false;
    }

    @Override
    public boolean pubRel(long userId, long cid, MqttPubRel mqttPubRel) {
        RequestIdUtil.setRequestId(userId);
        try {
            int messageId = mqttPubRel.getMessageId();
            //取list直到满足否则抛弃
            while (qosService.getClientFlightQueueLen(userId) > 0) {
                MqttPublish mqttPublish = qosService.getClientFlightQueueMessage(userId,true);
                LoggerManager.info("==>loop mqttPubRel = " + mqttPubRel + ", publish = " + mqttPublish
                        + ", userId " + userId);
                if (mqttPublish != null && messageId == mqttPublish.getMessageId()) {
                    mqttBizService.publish(userId, cid, mqttPublish);
                    return true;
                }
            }
//            MqttPublish mqttPublish = qosService.getExactlyOnceMessage(userId, true);
//            LoggerManager.info("==>MqttBizServiceQosProxy : receive mqttPubRel=" + mqttPubRel
//                    + " , find message publish=" + mqttPublish + ", userId " + userId);
//            if (mqttPublish != null && messageId == mqttPublish.getMessageId()) {
//                mqttBizService.publish(userId, cid, mqttPublish);
//                return true;
//            } else {
//                LoggerManager.error("mqttPubRel = " + mqttPubRel + ", publish = " + mqttPublish
//                        + ", userId " + userId);
//                return false;
//            }
        } catch (Exception e) {
            LoggerManager.error("", e);
        }
        return false;
    }

    @Override
    public boolean pubComp(long userId, long cid, MqttPubComp mqttPubComp) {
        RequestIdUtil.setRequestId(userId);
        try {
            return mqttBizService.pubComp(userId, cid, mqttPubComp);
        } catch (Exception e) {
            LoggerManager.error("", e);
        }
        return false;
    }

    @Override
    public boolean subscribe(long userId, long cid, MqttSubscribe mqttSubscribe) {
        RequestIdUtil.setRequestId(userId);
        try {
            return mqttBizService.subscribe(userId, cid, mqttSubscribe);
        } catch (Exception e) {
            LoggerManager.error("", e);
        }
        return false;
    }

    @Override
    public boolean unsubscribe(long userId, long cid, MqttUnsubscribe mqttUnubscribe) {
        RequestIdUtil.setRequestId(userId);
        try {
            return mqttBizService.unsubscribe(userId, cid, mqttUnubscribe);
        } catch (Exception e) {
            LoggerManager.error("", e);
        }
        return false;
    }

    @Override
    public void channelInactive(long userId, long cid) {
        mqttBizService.channelInactive(userId, cid);
    }

    @Override
    public void ping(long userId, long cid) {
        mqttBizService.ping(userId, cid);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(mqttBizService, "mqttBizService must not null!");
        Assert.notNull(qosService, "qosService must not null!");
    }

}
