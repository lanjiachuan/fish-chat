
package org.fish.chat.mqtt.session.manager.impl;

import org.fish.chat.common.log.LoggerManager;
import io.netty.channel.Channel;
import org.apache.commons.lang3.math.NumberUtils;
import org.fish.chat.mqtt.protocol.wire.MqttConnect;
import org.fish.chat.mqtt.session.ChannelSession;
import org.fish.chat.mqtt.session.manager.ChannelSessionManager;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Comments for ChannelSessionManagerImpl.java
 *
 */
public class ChannelSessionManagerImpl implements ChannelSessionManager, InitializingBean {

    private Map<Long, ChannelSession> channelSessionMap = new ConcurrentHashMap<Long, ChannelSession>();

    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    public ChannelSession getChannelSession(long id) {
        return channelSessionMap.get(id);
    }

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.session.manager.ChannelSessionManager#createChannelSession(cn.techwolf.mqtt.protocol.wire.MqttConnect, io.netty.channel.Channel)
     */
    @Override
    public ChannelSession createChannelSession(long userId, int userType, MqttConnect connect,
            Channel channel) {
        if (connect != null && channel != null) {
            ChannelSession channelSession = new ChannelSession(userId, channel);
            channelSessionMap.put(channelSession.getCid(), channelSession);
            channel.attr(ATTR_KEY_CONNECTION_ID).set(String.valueOf(channelSession.getCid()));
            channel.attr(ATTR_KEY_CLIENT_ID).set(connect.getClientId());
            channel.attr(ATTR_KEY_USER_ID).set(String.valueOf(userId));
            channel.attr(ATTR_KEY_USER_TYPE).set(String.valueOf(userType));
            return channelSession;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.session.manager.ChannelSessionManager#getChannelSession(io.netty.channel.Channel)
     */
    @Override
    public ChannelSession getChannelSession(Channel channel) {
        long cid = NumberUtils.toLong(channel.attr(ATTR_KEY_CONNECTION_ID).get());
        if (cid > 0) {
            return getChannelSession(cid);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                LoggerManager.info("current ChannelSessionMap size = " + channelSessionMap.size());
                for (ChannelSession channelSession : channelSessionMap.values()) {
                    if (!channelSession.getChannel().isActive()) {
                        try {
                            LoggerManager.warn("channelSession is not active , uid ="
                                    + channelSession.getUserId() + ", cid="
                                    + channelSession.getCid());
                            destroyChannelSession(channelSession);
                        } catch (Exception e) {
                            LoggerManager.error("", e);
                        }
                    }
                }
            }
        }, 20, 60, TimeUnit.SECONDS);
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.mqtt.session.manager.ChannelSessionManager#destroyChannelSession(io.netty.channel.Channel)
     */
    @Override
    public void destroyChannelSession(ChannelSession channelSession) {
        if (channelSession != null) {
            channelSessionMap.remove(channelSession.getCid());
        }
    }

}
