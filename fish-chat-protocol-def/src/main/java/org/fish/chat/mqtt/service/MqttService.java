
package org.fish.chat.mqtt.service;


import org.fish.chat.mqtt.protocol.wire.MqttPubRel;
import org.fish.chat.mqtt.protocol.wire.MqttPublish;

/**
 * Comments for MqttService.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月10日 下午2:49:40
 */
public interface MqttService {

    public boolean publish(long userId, long cid, MqttPublish publish);

    public boolean pubRel(long userId, long cid, MqttPubRel mqttPubRel);

    public boolean publishAndClose(long userId, long cid, MqttPublish publish);

    boolean close(long userId, long cid);
}
