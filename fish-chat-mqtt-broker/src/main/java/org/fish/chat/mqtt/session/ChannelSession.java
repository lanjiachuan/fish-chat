package org.fish.chat.mqtt.session;

import io.netty.channel.Channel;
import org.fish.chat.common.utils.ChannelSessionUtil;
import org.fish.chat.common.utils.IpUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * mqtt 层缓存对象
 *
 * 缓存channel uid
 *
 * @author adre
 */
public class ChannelSession {

    private static AtomicInteger sender = new AtomicInteger(1);

    private static int MAX_SENDER_NUM = 1 << 28;

    private long cid;

    private Channel channel;

    private long userId;

    public ChannelSession(long userId, Channel channel) {
        cid = generateCid();
        this.channel = channel;
        this.userId = userId;
    }

    private long generateCid() {
        int id = sender.getAndIncrement();
        if (id < 0 || id >= MAX_SENDER_NUM) {
            sender.set(1);
            id = 1;
        }
        long cid = (IpUtil.getLocalInnerIp() + (1L << 32)) << 28 | id;
        return cid;
    }

    /**
     * @return the cid
     */
    public long getCid() {
        return cid;
    }

    /**
     * @param cid the cid to set
     */
    public void setCid(long cid) {
        this.cid = cid;
    }

    /**
     * @return the channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "[cid=" + cid + ", userId=" + userId + "]";
    }

}
