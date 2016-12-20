
package org.fish.chat.mqtt.session;

import cn.techwolf.common.location.utils.IpUtil;
import io.netty.channel.Channel;
import org.fish.chat.common.utils.ChannelSessionUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Comments for ChannelSession.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月9日 下午7:49:01
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
        long cid = (IpUtil.getLocalInnerIp() + (1l << 32)) << 28 | id;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[cid=" + cid + ", userId=" + userId + "]";
    }

    public static void main(String[] args) {
        int a = (1 << 28);
        System.out.println(a);
        sender.set(Integer.MAX_VALUE-10);
        for (int i = 0; i < 100; i++) {
            int id = sender.getAndIncrement();
            if(id <=0) {
                sender.set(1);
                id = 1;
            }
//            System.out.println((1l << 32));
//            System.out.println(IpUtil.getLocalInnerIp() + (1l << 32));
            long cid = (IpUtil.getLocalInnerIp() + (1l << 32)) << 28 | id;
            System.out.println(cid);
            String ip = ChannelSessionUtil.getIpByChannelId(cid);
            System.out.println(ip);

        }

    }
}
