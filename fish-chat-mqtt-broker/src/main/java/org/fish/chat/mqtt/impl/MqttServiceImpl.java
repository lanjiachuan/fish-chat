package org.fish.chat.mqtt.impl;


import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.fish.chat.mqtt.protocol.wire.MqttPubRel;
import org.fish.chat.mqtt.protocol.wire.MqttPublish;
import org.fish.chat.mqtt.service.MqttService;
import org.fish.chat.mqtt.session.ChannelSession;
import org.fish.chat.mqtt.session.manager.ChannelSessionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Comments for MqttServiceImpl.java
 *
 */
public class MqttServiceImpl implements MqttService, InitializingBean {

    private ChannelSessionManager channelSessionManager;

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.service.MqttService#publish(long, cn.techwolf.mqtt.protocol.wire.MqttPublish)
     */
    @Override
    public boolean publish(long userId, long cid, MqttPublish publish) {
        RequestIdUtil.setRequestId(userId);
        LoggerManager.info("<==MqttServiceImpl.publish userId=" + userId + ", cid=" + cid
                + ", message=" + publish.getMessageId());
        ChannelSession channelSession = channelSessionManager.getChannelSession(cid);
        if (channelSession != null) {
            channelSession.getChannel().writeAndFlush(publish);
            return true;
        } else {
            LoggerManager.warn("<==MqttServiceImpl.publish channelSession is null ! userId=" + userId
                    + ", cid=" + cid);
        }
        return false;
    }

    /**
     * @param channelSessionManager the channelSessionManager to set
     */
    public void setChannelSessionManager(ChannelSessionManager channelSessionManager) {
        this.channelSessionManager = channelSessionManager;
    }

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.service.MqttService#pubRel(long, cn.techwolf.mqtt.protocol.wire.MqttPubRel)
     */
    @Override
    public boolean pubRel(long userId, long cid, MqttPubRel mqttPubRel) {
        RequestIdUtil.setRequestId(userId);
        LoggerManager.info("<==MqttServiceImpl.pubRel userId=" + userId + ", cid=" + cid
                + ", message=" + mqttPubRel.getMessageId());
        ChannelSession channelSession = channelSessionManager.getChannelSession(cid);
        if (channelSession != null) {
            channelSession.getChannel().writeAndFlush(mqttPubRel);
            return true;
        } else {
            LoggerManager.warn("<==MqttServiceImpl.pubRel channelSession is null ! userId=" + userId
                    + ", cid=" + cid);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(channelSessionManager, "channelSessionManager must not null!");
    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.mqtt.service.MqttService#publishAndClose(long, long, cn.techwolf.boss.mqtt.protocol.wire.MqttPublish)
     */
    @Override
    public boolean publishAndClose(final long userId, final long cid, final MqttPublish publish) {
        RequestIdUtil.setRequestId(userId);
        LoggerManager.info("<==MqttServiceImpl.publishAndClose userId=" + userId + ", cid=" + cid
                + ", message=" + publish.getMessageId());
        ChannelSession channelSession = channelSessionManager.getChannelSession(cid);
        if (channelSession != null) {
            final Channel channel = channelSession.getChannel();

            ChannelFuture f = channelSession.getChannel().writeAndFlush(publish);
            f.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    RequestIdUtil.setRequestId(userId);
                    LoggerManager.info("channel will close after publish, uid" + userId + ",cid="
                            + cid);
                    channel.close();
                }
            });
            return true;
        } else {
            LoggerManager.warn("<==MqttServiceImpl.publishAndClose channelSession is null ! userId="
                    + userId + ", cid=" + cid);
        }
        return false;
    }
    
    
    @Override
    public boolean close( long userId,  long cid) {
        RequestIdUtil.setRequestId(userId);
        LoggerManager.info("<==MqttServiceImpl.close userId=" + userId + ", cid=" + cid);
        ChannelSession channelSession = channelSessionManager.getChannelSession(cid);
        if (channelSession != null) {
            final Channel channel = channelSession.getChannel();
            channel.close();
            return true;
        } else {
            LoggerManager.warn("<==MqttServiceImpl.close channelSession is null ! userId="
                    + userId + ", cid=" + cid);
        }
        return false;
    }

}
