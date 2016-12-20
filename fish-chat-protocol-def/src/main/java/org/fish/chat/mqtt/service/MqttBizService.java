
package org.fish.chat.mqtt.service;


import org.fish.chat.mqtt.protocol.wire.*;

/**
 * Comments for MqttBizService.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月10日 下午2:56:00
 */
public interface MqttBizService {

    public MqttConnack connect(long userId, long cid, MqttConnect mqttConnect, String ip, int type);

    public void disconnect(long userId, long cid, MqttDisconnect mqttDisconnect);

    public boolean publish(long userId, long cid, MqttPublish mqttPublish);

    public void ping(long userId, long cid);

    public boolean pubAck(long userId, long cid, MqttPubAck mqttPuback);

    public boolean pubRec(long userId, long cid, MqttPubRec mqttPubRec);

    public boolean pubRel(long userId, long cid, MqttPubRel mqttPubRel);

    public boolean pubComp(long userId, long cid, MqttPubComp mqttPubComp);

    public boolean subscribe(long userId, long cid, MqttSubscribe mqttSubscribe);

    public boolean unsubscribe(long userId, long cid, MqttUnsubscribe mqttUnubscribe);

    public void channelInactive(long userId, long cid);
}
