/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.handler;

import org.fish.chat.common.log.LoggerManager;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.fish.chat.common.utils.RequestIdUtil;
import org.fish.chat.mqtt.protocol.wire.*;
import org.fish.chat.mqtt.service.MqttBizService;
import org.fish.chat.mqtt.session.ChannelSession;
import org.fish.chat.mqtt.session.manager.ChannelSessionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Comments for MqttHandler.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月9日 上午9:52:41
 */
@Sharable
public class MqttDispatcherHandler extends ChannelInboundHandlerAdapter implements InitializingBean {

    private MqttConnectHandler mqttConnectHandler;

    private MqttPublishHandler mqttPublishHandler;

    private MqttPingReqHandler mqttPingReqHandler;

    private MqttPubAckHandler mqttPubAckHandler;

    private MqttSubscribeHandler mqttSubscribeHandler;

    private MqttUnsubscribeHandler mqttUnsubscribeHandler;

    private MqttPubRecHandler mqttPubRecHandler;

    private MqttPubRelHandler mqttPubRelHandler;

    private MqttPubCompHandler mqttPubCompHandler;

    private MqttDisconnectHandler mqttDisconnectHandler;

    private ChannelSessionManager channelSessionManager;

    private MqttBizService mqttBizService;

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        RequestIdUtil.setRequestId(channelSession != null ? channelSession.getUserId() : 0);

        if (msg instanceof MqttPingReq) {
            mqttPingReqHandler.channelRead(ctx, (MqttPingReq) msg);
        } else {
            LoggerManager.info("==>" + (channelSession == null ? "" : channelSession.toString())
                    + "receive " + msg);
            if (msg instanceof MqttConnect) {
                mqttConnectHandler.channelRead(ctx, (MqttConnect) msg);
            } else if (msg instanceof MqttPublish) {
                mqttPublishHandler.channelRead(ctx, (MqttPublish) msg);
            } else if (msg instanceof MqttPubAck) {
                mqttPubAckHandler.channelRead(ctx, (MqttPubAck) msg);
            } else if (msg instanceof MqttPubRec) {
                mqttPubRecHandler.channelRead(ctx, (MqttPubRec) msg);
            } else if (msg instanceof MqttPubRel) {
                mqttPubRelHandler.channelRead(ctx, (MqttPubRel) msg);
            } else if (msg instanceof MqttPubComp) {
                mqttPubCompHandler.channelRead(ctx, (MqttPubComp) msg);
            } else if (msg instanceof MqttSubscribe) {
                mqttSubscribeHandler.channelRead(ctx, (MqttSubscribe) msg);
            } else if (msg instanceof MqttUnsubscribe) {
                mqttUnsubscribeHandler.channelRead(ctx, (MqttUnsubscribe) msg);
            } else if (msg instanceof MqttDisconnect) {
                mqttDisconnectHandler.channelRead(ctx, (MqttDisconnect) msg);
            }
        }
    }

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        if (channelSession != null) {
            RequestIdUtil.setRequestId(channelSession.getUserId());
            LoggerManager.info("channelInactive, userId=" + channelSession.getUserId() + ",cid="
                    + channelSession.getCid());
            channelSessionManager.destoryChannelSession(channelSession);
            mqttBizService.channelInactive(channelSession.getUserId(), channelSession.getCid());
        }
        LoggerManager.info(ctx.channel().remoteAddress() + " was closed!");
    }

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
     */
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        long userId = 0;
        long cid = 0;
        if (channelSession != null) {
            userId = channelSession.getUserId();
            cid = channelSession.getCid();
            RequestIdUtil.setRequestId(channelSession.getUserId());
        }

        LoggerManager.error("userId=" + userId + ", cid=" + cid + " caught an exception ", cause);

        final ChannelFuture f = ctx.writeAndFlush("close");
        f.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;
                ctx.close();
            }
        });

    }

    /**
     * @param mqttConnectHandler the mqttConnectHandler to set
     */
    public void setMqttConnectHandler(MqttConnectHandler mqttConnectHandler) {
        this.mqttConnectHandler = mqttConnectHandler;
    }

    /**
     * @param mqttPublishHandler the mqttPublishHandler to set
     */
    public void setMqttPublishHandler(MqttPublishHandler mqttPublishHandler) {
        this.mqttPublishHandler = mqttPublishHandler;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(mqttConnectHandler, "mqttConnectHandler must not null!");
        Assert.notNull(mqttPublishHandler, "mqttPublishHandler must not null!");
        Assert.notNull(mqttPubAckHandler, "mqttPubAckHandler must not null!");
        Assert.notNull(mqttPingReqHandler, "mqttPingReqHandler must not null!");
        Assert.notNull(mqttSubscribeHandler, "mqttSubscribeHandler must not null!");
        Assert.notNull(mqttPubRecHandler, "mqttPubRecHandler must not null!");
        Assert.notNull(mqttPubRelHandler, "mqttPubRecHandler must not null!");
        Assert.notNull(mqttPubCompHandler, "mqttPubCompHandler must not null!");
        Assert.notNull(channelSessionManager, "channelSessionManager must not null!");
        Assert.notNull(mqttBizService, "mqttBizService must not null!");
        Assert.notNull(mqttDisconnectHandler, "mqttDisconnectHandler must not null!");
    }

    /**
     * @param mqttPingReqHandler the mqttPingReqHandler to set
     */
    public void setMqttPingReqHandler(MqttPingReqHandler mqttPingReqHandler) {
        this.mqttPingReqHandler = mqttPingReqHandler;
    }

    /**
     * @param mqttPubAckHandler the mqttPubAckHandler to set
     */
    public void setMqttPubAckHandler(MqttPubAckHandler mqttPubAckHandler) {
        this.mqttPubAckHandler = mqttPubAckHandler;
    }

    /**
     * @param mqttSubscribeHandler the mqttSubscribeHandler to set
     */
    public void setMqttSubscribeHandler(MqttSubscribeHandler mqttSubscribeHandler) {
        this.mqttSubscribeHandler = mqttSubscribeHandler;
    }

    /**
     * @param mqttUnsubscribeHandler the mqttUnsubscribeHandler to set
     */
    public void setMqttUnsubscribeHandler(MqttUnsubscribeHandler mqttUnsubscribeHandler) {
        this.mqttUnsubscribeHandler = mqttUnsubscribeHandler;
    }

    /**
     * @param mqttPubRecHandler the mqttPubRecHandler to set
     */
    public void setMqttPubRecHandler(MqttPubRecHandler mqttPubRecHandler) {
        this.mqttPubRecHandler = mqttPubRecHandler;
    }

    /**
     * @param mqttPubRelHandler the mqttPubRelHandler to set
     */
    public void setMqttPubRelHandler(MqttPubRelHandler mqttPubRelHandler) {
        this.mqttPubRelHandler = mqttPubRelHandler;
    }

    /**
     * @param mqttPubCompHandler the mqttPubCompHandler to set
     */
    public void setMqttPubCompHandler(MqttPubCompHandler mqttPubCompHandler) {
        this.mqttPubCompHandler = mqttPubCompHandler;
    }

    /**
     * @param channelSessionManager the channelSessionManager to set
     */
    public void setChannelSessionManager(ChannelSessionManager channelSessionManager) {
        this.channelSessionManager = channelSessionManager;
    }

    /**
     * @param mqttBizService the mqttBizService to set
     */
    public void setMqttBizService(MqttBizService mqttBizService) {
        this.mqttBizService = mqttBizService;
    }

    /**
     * @param mqttDisconnectHandler the mqttDisconnectHandler to set
     */
    public void setMqttDisconnectHandler(MqttDisconnectHandler mqttDisconnectHandler) {
        this.mqttDisconnectHandler = mqttDisconnectHandler;
    }

}
