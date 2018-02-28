package org.fish.chat.mqtt.handler;


import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.fish.chat.chat.model.UserSession;
import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import org.fish.chat.mqtt.protocol.wire.MqttConnack;
import org.fish.chat.mqtt.protocol.wire.MqttConnect;
import org.fish.chat.mqtt.session.ChannelSession;
import org.springframework.stereotype.Component;

/**
 * user connect mqtt check uid and pwd
 * @author adre
 */
@Component
public class MqttConnectHandler extends AbstractMqttHandler<MqttConnect> {

    @Override
    public void channelRead(final ChannelHandlerContext ctx, MqttConnect msg) throws Exception {
        MqttConnect mqttConnect = msg;
        final String mqttClientId = mqttConnect.getClientId();
        final String userName = mqttConnect.getUserName();
        final String password = mqttConnect.getPassword();
        boolean cleanSession = mqttConnect.isCleanSession();
        //clientId
        if (StringUtils.isEmpty(mqttClientId) || mqttClientId.length() > 23) {
            MqttConnack connack = new MqttConnack(MqttConnack.REFUSED_IDENTIFIER_REJECTED);
            final ChannelFuture future = ctx.writeAndFlush(connack);
            future.addListener((ChannelFutureListener) future1 -> {
                ctx.close();
                LoggerManager.info("MqttConnectHandler - REFUSED_IDENTIFIER_REJECTED:"
                        + mqttClientId);

            });
            return;
        }

        //userName and Password
        int userId = 0;
        if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
            userId = NumberUtils.toInt(userName, 0);
        }

        RequestIdUtil.setRequestId(userId);

        if (userId == 0) {
            MqttConnack connack = new MqttConnack(MqttConnack.REFUSED_BAD_USERNAME_PASSWORD);
            final ChannelFuture future = ctx.writeAndFlush(connack);
            future.addListener((ChannelFutureListener) future12 -> {
                ctx.close();
                LoggerManager.info("<==MqttConnectHandler - REFUSED_BAD_USERNAME_PASSWORD:"
                        + userName + " " + password);

            });
            return;
        }

        ChannelSession channelSession = channelSessionManager.createChannelSession(userId, UserSession.USER_TYPE_DEFAULT,
                mqttConnect, ctx.channel());

        LoggerManager.info("==>connect params--mqttClientId:" + mqttClientId + " uname:" + userName
                + " password:" + password + " cleanSession:" + cleanSession + ",cid=" + channelSession.getCid());
        
        MqttConnack mqttConnack = chatFeignClient.connect(channelSession.getUserId(),
                channelSession.getCid(), mqttConnect, ctx.channel().remoteAddress().toString(),
                UserSession.USER_TYPE_DEFAULT);
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
