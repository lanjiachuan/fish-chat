
package org.fish.chat.chat.callback;


import org.fish.chat.mqtt.protocol.wire.MqttPubAck;
import org.fish.chat.mqtt.protocol.wire.MqttPubComp;
import org.fish.chat.mqtt.protocol.wire.MqttPubRec;

/**
 * Comments for MessageAckCallback.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月21日 下午4:50:52
 */
public interface MessageAckCallback {

    public boolean notifyPubAck(long userId, long cid, MqttPubAck mqttPubAck);

    public boolean notifyPubComp(long userId, long cid, MqttPubComp mqttPubComp);

    public boolean notifyPubRec(long userId, long cid, MqttPubRec mqttPubRec);
}
