package org.fish.chat.mqtt.service;


import org.fish.chat.mqtt.protocol.wire.MqttPubRel;
import org.fish.chat.mqtt.protocol.wire.MqttPublish;

/**
 * 只实现了Qos=2的方法
 *
 * @author adre
 */
public interface MqttService {

    /**
     * publish
     * @param userId
     * @param cid
     * @param publish
     * @return
     */
    boolean publish(long userId, long cid, MqttPublish publish);

    /**
     * pubRel
     * @param userId
     * @param cid
     * @param mqttPubRel
     * @return
     */
    boolean pubRel(long userId, long cid, MqttPubRel mqttPubRel);

    /**
     * publish close
     * @param userId
     * @param cid
     * @param publish
     * @return
     */
    boolean publishAndClose(long userId, long cid, MqttPublish publish);

    /**
     * close channel
     * @param userId
     * @param cid
     * @return
     */
    boolean close(long userId, long cid);
}
