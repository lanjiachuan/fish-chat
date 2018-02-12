package org.fish.chat.mqtt.service;


import org.fish.chat.mqtt.protocol.wire.*;

/**
 * mqtt协议消息类型
 *
 * @author adre
 */
public interface MqttBizService {

    /**
     * 连接
     * @param userId
     * @param cid
     * @param mqttConnect
     * @param ip
     * @param type
     * @return
     */
    MqttConnack connect(long userId, long cid, MqttConnect mqttConnect, String ip, int type);

    /**
     * 断开
     * @param userId
     * @param cid
     * @param mqttDisconnect
     */
    void disconnect(long userId, long cid, MqttDisconnect mqttDisconnect);

    /**
     * 发布消息
     * @param userId
     * @param cid
     * @param mqttPublish
     * @return
     */
    boolean publish(long userId, long cid, MqttPublish mqttPublish);

    /**
     * 心跳检测
     * @param userId
     * @param cid
     */
    void ping(long userId, long cid);

    /**
     * 发布确认
     * Qos=1
     * @param userId
     * @param cid
     * @param mqttPuback
     * @return
     */
    boolean pubAck(long userId, long cid, MqttPubAck mqttPuback);

    /**
     * 发布信息收到
     * Qos=2
     * @param userId
     * @param cid
     * @param mqttPubRec
     * @return
     */
    boolean pubRec(long userId, long cid, MqttPubRec mqttPubRec);

    /**
     * 发布信息分发
     * Qos=2
     * @param userId
     * @param cid
     * @param mqttPubRel
     * @return
     */
    boolean pubRel(long userId, long cid, MqttPubRel mqttPubRel);

    /**
     * 发布完成消息
     * Qos=2
     * @param userId
     * @param cid
     * @param mqttPubComp
     * @return
     */
    boolean pubComp(long userId, long cid, MqttPubComp mqttPubComp);

    /**
     * 订阅
     * @param userId
     * @param cid
     * @param mqttSubscribe
     * @return
     */
    boolean subscribe(long userId, long cid, MqttSubscribe mqttSubscribe);

    /**
     * 取消订阅
     * @param userId
     * @param cid
     * @param mqttUnubscribe
     * @return
     */
    boolean unsubscribe(long userId, long cid, MqttUnsubscribe mqttUnubscribe);

    /**
     * channel 失效
     * @param userId
     * @param cid
     */
    void channelInactive(long userId, long cid);
}
