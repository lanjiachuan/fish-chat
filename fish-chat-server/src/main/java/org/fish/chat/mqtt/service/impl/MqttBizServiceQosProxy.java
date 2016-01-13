package org.fish.chat.mqtt.service.impl;

/**
 * techwolf.cn All rights reserved.
 */


import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import org.fish.chat.mqtt.protocol.MqttMessage;
import org.fish.chat.mqtt.protocol.wire.*;
import org.fish.chat.mqtt.qos.QosService;
import org.fish.chat.mqtt.service.MqttBizService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Comments for MqttBizServiceQosProxy.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月11日 上午11:23:46
 */
public class MqttBizServiceQosProxy implements MqttBizService, InitializingBean {

    private MqttBizService mqttBizService;

    private QosService qosService;

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.service.MqttBizService#connect(long, long, cn.techwolf.mqtt.protocol.wire.MqttConnect)
     */
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

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.service.MqttBizService#disconnect(long, cn.techwolf.mqtt.protocol.wire.MqttDisconnect)
     */
    @Override
    public void disconnect(long userId, long cid, MqttDisconnect mqttDisconnect) {
        RequestIdUtil.setRequestId(userId);
        try {
            mqttBizService.disconnect(userId, cid, mqttDisconnect);
        } catch (Exception e) {

        }
    }

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.service.MqttBizService#publish(long, cn.techwolf.mqtt.protocol.wire.MqttPublish)
     */
    @Override
    public boolean publish(long userId, long cid, MqttPublish mqttPublish) {
        RequestIdUtil.setRequestId(userId);
        try {
            int qos = mqttPublish.getMessage().getQos();
            if (qos == MqttMessage.QOS_EXACTLY_ONCE) {
                LoggerManager
                        .info("==>MqttBizServiceQosProxy : message not send , wait pubrel to send msgId="
                                + mqttPublish.getMessageId());
                // zyl modify 2015 06 09
//                return qosService.setExactlyOnceMessage(userId, mqttPublish);
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

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.service.MqttBizService#pubAck(long, cn.techwolf.mqtt.protocol.wire.MqttPubAck)
     */
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

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.service.MqttBizService#pubRec(long, cn.techwolf.mqtt.protocol.wire.MqttPubRec)
     */
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

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.service.MqttBizService#pubRel(long, cn.techwolf.mqtt.protocol.wire.MqttPubRel)
     */
    @Override
    public boolean pubRel(long userId, long cid, MqttPubRel mqttPubRel) {
        RequestIdUtil.setRequestId(userId);
        try {
            int messageId = mqttPubRel.getMessageId();
            //取list直到满足否则抛弃
            while (qosService.getClinetFlightQueueLen(userId) > 0) {
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

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.service.MqttBizService#pubComp(long, cn.techwolf.mqtt.protocol.wire.MqttPubComp)
     */
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

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.service.MqttBizService#subscribe(long, cn.techwolf.mqtt.protocol.wire.MqttSubscribe)
     */
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

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.service.MqttBizService#unsubscribe(long, cn.techwolf.mqtt.protocol.wire.MqttUnsubscribe)
     */
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

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(mqttBizService, "mqttBizService must not null!");
        Assert.notNull(qosService, "qosService must not null!");
    }

    /**
     * @param mqttBizService the mqttBizService to set
     */
    public void setMqttBizService(MqttBizService mqttBizService) {
        this.mqttBizService = mqttBizService;
    }

    /**
     * @param qosService the qosService to set
     */
    public void setQosService(QosService qosService) {
        this.qosService = qosService;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.service.MqttBizService#channelInactive(long)
     */
    @Override
    public void channelInactive(long userId, long cid) {
        mqttBizService.channelInactive(userId, cid);
    }

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.service.MqttBizService#ping(long)
     */
    @Override
    public void ping(long userId, long cid) {
        mqttBizService.ping(userId, cid);
    }

}
