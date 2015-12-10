/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.qos;


import org.fish.chat.mqtt.protocol.wire.MqttPersistableWireMessage;
import org.fish.chat.mqtt.protocol.wire.MqttPubRel;
import org.fish.chat.mqtt.protocol.wire.MqttPublish;

/**
 * Comments for QosService.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月11日 上午11:58:32
 */
public interface QosService {

    /**
     * 设置qos=2的消息,等待返回MqttPubRel时再可见(up)
     * 
     * @param userId
     * @param mqttPublish
     * @return
     */
    public boolean setExactlyOnceMessage(long userId, MqttPublish mqttPublish);

    /**
     * 获取qos=2等待PubRel的消息(up)
     * 
     * @param userId
     * @param messageId
     * @param clean
     * @return
     */
    public MqttPublish getExactlyOnceMessage(long userId, boolean clean);

    /**
     * 获取正在飞行的消息
     * 
     * @param cid
     * @return
     */
    public MqttPersistableWireMessage getInFlightMessage(long userId);

    /**
     * 获取下条待发送消息
     * 
     * @param cid
     * @return
     */
    public MqttPersistableWireMessage getNextMessage(long userId);

    /**
     * 添加消息
     * 
     * @param message
     * @return
     */
    public int addMessage(long userId, MqttPersistableWireMessage message);

    /**
     * 替换飞行消息 用release 消息替换 publish消息
     */
    public boolean updateMessage(long userId, MqttPubRel message);

    /**
     * 获取当前传输完成的publish消息
     * 
     * @param userId
     * @param clean
     * @return
     */
    public MqttPublish getInflightPublish(long userId, boolean clean);

    /**
     * 设置客户端的飞行信息队列
     * @param userId
     * @param message
     * @return
     */
    public int setClientFlightQueueMessage(long userId, MqttPublish message);

    /**
     * 获取缓存中的客户端飞行publish
     * @param userId
     * @return
     */
    public MqttPublish getClientFlightQueueMessage(long userId, boolean pop);

    /**
     * 获取长度
     * @param userId
     * @return
     */
    public Long getClinetFlightQueueLen(long userId);

}
