package org.fish.chat.chat.callback;


import org.fish.chat.mqtt.protocol.wire.MqttPubAck;
import org.fish.chat.mqtt.protocol.wire.MqttPubComp;
import org.fish.chat.mqtt.protocol.wire.MqttPubRec;

/**
 * Comments for MessageAckCallback.java
 *
 */
public interface MessageAckCallback {

    public boolean notifyPubAck(long userId, long cid, MqttPubAck mqttPubAck);

    public boolean notifyPubComp(long userId, long cid, MqttPubComp mqttPubComp);

    public boolean notifyPubRec(long userId, long cid, MqttPubRec mqttPubRec);
}
