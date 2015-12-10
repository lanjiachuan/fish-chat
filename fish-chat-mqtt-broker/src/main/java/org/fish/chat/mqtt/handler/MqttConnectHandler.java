/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.handler;


import cn.techwolf.common.log.LoggerManager;
import cn.techwolf.common.utils.RequestIdUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.fish.chat.mqtt.protocol.wire.MqttConnack;
import org.fish.chat.mqtt.protocol.wire.MqttConnect;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * Comments for MqttConnectHandler.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月10日 上午10:19:05
 */
public class MqttConnectHandler extends AbstractMqttHandler<MqttConnect> {

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, MqttConnect msg) throws Exception {
        MqttConnect mqttConnect = (MqttConnect) msg;
        final String mqttClientId = mqttConnect.getClientId();
        final String userName = mqttConnect.getUserName();
        final String password = mqttConnect.getPassword();
        boolean cleanSession = mqttConnect.isCleanSession();
        //clientId
        if (StringUtils.isEmpty(mqttClientId) || mqttClientId.length() > 23) {
            MqttConnack connack = new MqttConnack(MqttConnack.REFUSED_IDENTIFIER_REJECTED);
            final ChannelFuture future = ctx.writeAndFlush(connack);
            future.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    ctx.close();
                    LoggerManager.info("MqttConnectHandler - REFUSED_IDENTIFIER_REJECTED:"
                            + mqttClientId);

                }
            });
            return;
        }

        //userName and Password
        int userId = 0;
        int type = 0;
        boolean rightful = false;
        if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
            int index = StringUtils.indexOf(userName, "-");
            if (index > 0) {
                String[] arr = userName.split("-");
                userId = NumberUtils.toInt(arr[0]);
                type = NumberUtils.toInt(arr[1], -1);
                rightful = true;
            }
        }

        RequestIdUtil.setRequestId(userId);

        if (!rightful && userId == 0 && (type != 0 || type != 1)) {
            MqttConnack connack = new MqttConnack(MqttConnack.REFUSED_BAD_USERNAME_PASSWORD);
            final ChannelFuture future = ctx.writeAndFlush(connack);
            future.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    ctx.close();
                    LoggerManager.info("<==MqttConnectHandler - REFUSED_BAD_USERNAME_PASSWORD:"
                            + userName + " " + password);

                }
            });
            return;
        }

        ChannelSession channelSession = channelSessionManager.createChannelSession(userId, type,
                mqttConnect, ctx.channel());

        LoggerManager.info("==>connect params--mqttClientId:" + mqttClientId + " uname:" + userName
                + " password:" + password + " cleanSession:" + cleanSession + ",cid=" + channelSession.getCid());
        
        MqttConnack mqttConnack = mqttBizService.connect(channelSession.getUserId(),
                channelSession.getCid(), mqttConnect, ctx.channel().remoteAddress().toString(),
                type);
        if (mqttConnack == null) {
            LoggerManager.error("MqttConnectHandler connect return null");
            mqttConnack = new MqttConnack(MqttConnack.REFUSED_SERVER_UNAVAILABLE);
        }
        ChannelFuture future = ctx.writeAndFlush(mqttConnack);
        //非接受需要关闭
        if (mqttConnack.getReturnCode() != MqttConnack.ACCEPTED) {
            LoggerManager.warn("server refused wait to close. mqttConnack=" + mqttConnack);
            future.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    ctx.close();
                }
            });
        }

    }
}
