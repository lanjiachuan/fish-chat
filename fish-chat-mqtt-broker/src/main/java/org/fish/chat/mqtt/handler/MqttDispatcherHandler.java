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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * dispatch message
 * @author adre
 */
@Component
@Sharable
public class MqttDispatcherHandler extends ChannelInboundHandlerAdapter implements InitializingBean {

    @Autowired
    private MqttConnectHandler mqttConnectHandler;
    @Autowired
    private MqttPublishHandler mqttPublishHandler;
    @Autowired
    private MqttPingReqHandler mqttPingReqHandler;
    @Autowired
    private MqttPubAckHandler mqttPubAckHandler;
    @Autowired
    private MqttPubRecHandler mqttPubRecHandler;
    @Autowired
    private MqttPubRelHandler mqttPubRelHandler;
    @Autowired
    private MqttPubCompHandler mqttPubCompHandler;
    @Autowired
    private MqttDisconnectHandler mqttDisconnectHandler;
    @Autowired
    private ChannelSessionManager channelSessionManager;
    @Autowired
    private MqttBizService mqttBizService;

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
            } else if (msg instanceof MqttDisconnect) {
                mqttDisconnectHandler.channelRead(ctx, (MqttDisconnect) msg);
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        if (channelSession != null) {
            RequestIdUtil.setRequestId(channelSession.getUserId());
            LoggerManager.info("channelInactive, userId=" + channelSession.getUserId() + ",cid="
                    + channelSession.getCid());
            channelSessionManager.destroyChannelSession(channelSession);
            mqttBizService.channelInactive(channelSession.getUserId(), channelSession.getCid());
        }
        LoggerManager.info(ctx.channel().remoteAddress() + " was closed!");
    }

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
        f.addListener((ChannelFutureListener) future -> {
            assert f == future;
            ctx.close();
        });

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(mqttConnectHandler, "mqttConnectHandler must not null!");
        Assert.notNull(mqttPublishHandler, "mqttPublishHandler must not null!");
        Assert.notNull(mqttPubAckHandler, "mqttPubAckHandler must not null!");
        Assert.notNull(mqttPingReqHandler, "mqttPingReqHandler must not null!");
        Assert.notNull(mqttPubRecHandler, "mqttPubRecHandler must not null!");
        Assert.notNull(mqttPubRelHandler, "mqttPubRecHandler must not null!");
        Assert.notNull(mqttPubCompHandler, "mqttPubCompHandler must not null!");
        Assert.notNull(channelSessionManager, "channelSessionManager must not null!");
        Assert.notNull(mqttBizService, "mqttBizService must not null!");
        Assert.notNull(mqttDisconnectHandler, "mqttDisconnectHandler must not null!");
    }

}
