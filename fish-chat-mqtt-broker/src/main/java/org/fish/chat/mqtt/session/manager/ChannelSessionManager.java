package org.fish.chat.mqtt.session.manager;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.fish.chat.mqtt.protocol.wire.MqttConnect;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * channel缓存
 *
 *
 * @author adre
 */
public interface ChannelSessionManager {

    AttributeKey<String> ATTR_KEY_CONNECTION_ID = AttributeKey.valueOf("cid");

    AttributeKey<String> ATTR_KEY_CLIENT_ID = AttributeKey.valueOf("clientId");

    AttributeKey<String> ATTR_KEY_USER_ID = AttributeKey.valueOf("userId");

    AttributeKey<String> ATTR_KEY_USER_TYPE = AttributeKey.valueOf("userType");

    /**
     * 为channel创建session
     * @param userId
     * @param userType
     * @param connect
     * @param channel
     * @return
     */
    ChannelSession createChannelSession(long userId, int userType, MqttConnect connect, Channel channel);

    /**
     * 根据cid 获取channelSession
     * @param id
     * @return
     */
    ChannelSession getChannelSession(long id);

    /**
     * 根据channel 获取channelSession
     * 
     * @param channel
     * @return
     */
    ChannelSession getChannelSession(Channel channel);

    /**
     * 清理channel
     *
     * @param session
     */
    void destroyChannelSession(ChannelSession session);
}
