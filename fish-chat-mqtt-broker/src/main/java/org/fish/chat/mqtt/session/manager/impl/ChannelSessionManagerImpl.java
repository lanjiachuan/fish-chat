
package org.fish.chat.mqtt.session.manager.impl;

import org.fish.chat.common.log.LoggerManager;
import io.netty.channel.Channel;
import org.apache.commons.lang3.math.NumberUtils;
import org.fish.chat.mqtt.protocol.wire.MqttConnect;
import org.fish.chat.mqtt.session.ChannelSession;
import org.fish.chat.mqtt.session.manager.ChannelSessionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 对新创建的channel缓存相关信息
 *
 * 定时清理处于非active状态的缓存信息
 *
 * @author adre
 */
@Service
public class ChannelSessionManagerImpl implements ChannelSessionManager, InitializingBean {

    /**
     * 单点上所有session 都存在这里 分布式负载均衡必须保证同一个channel始终落在同一个节点上
     */
    private Map<Long, ChannelSession> channelSessionMap = new ConcurrentHashMap<>();

    /**
     * 起一个线程 定时检查channel是否active  清理channelSessionMap
     */
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    @Override
    public ChannelSession getChannelSession(long id) {
        return channelSessionMap.get(id);
    }

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

    @Override
    public ChannelSession getChannelSession(Channel channel) {
        long cid = NumberUtils.toLong(channel.attr(ATTR_KEY_CONNECTION_ID).get());
        if (cid > 0) {
            return getChannelSession(cid);
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
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
        }, 20, 60, TimeUnit.SECONDS);
    }

    @Override
    public void destroyChannelSession(ChannelSession channelSession) {
        if (channelSession != null) {
            channelSessionMap.remove(channelSession.getCid());
        }
    }

}
