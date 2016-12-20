
package org.fish.chat.mqtt.session.manager;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.fish.chat.mqtt.protocol.wire.MqttConnect;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * Comments for ChannelSessionManager.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月10日 下午1:46:25
 */
public interface ChannelSessionManager {

    AttributeKey<String> ATTR_KEY_CONNECTION_ID = AttributeKey.valueOf("cid");

    AttributeKey<String> ATTR_KEY_CLIENT_ID = AttributeKey.valueOf("clientId");

    AttributeKey<String> ATTR_KEY_USER_ID = AttributeKey.valueOf("userId");

    AttributeKey<String> ATTR_KEY_USER_TYPE = AttributeKey.valueOf("userType");

    /**
     * 为channel创建session
     * 
     * @param channel
     * @return
     */
    public ChannelSession createChannelSession(long userId, int userType, MqttConnect connect,
                                               Channel channel);

    /**
     * 根据cid 获取channelSession
     * 
     * @param id
     * @return
     */
    public ChannelSession getChannelSession(long id);

    /**
     * 根据channel 获取channelSession
     * 
     * @param channel
     * @return
     */
    public ChannelSession getChannelSession(Channel channel);

    /**
     * @param session
     */
    public void destoryChannelSession(ChannelSession session);
}
