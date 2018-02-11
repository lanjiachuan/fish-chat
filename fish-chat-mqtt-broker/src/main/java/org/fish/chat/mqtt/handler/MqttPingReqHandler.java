package org.fish.chat.mqtt.handler;

import org.fish.chat.common.log.LoggerManager;
import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttPingReq;
import org.fish.chat.mqtt.protocol.wire.MqttPingResp;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * ping pong heart beat
 * @author adre
 */
public class MqttPingReqHandler extends AbstractMqttHandler<MqttPingReq> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttPingReq msg) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        if (channelSession == null) {
            ctx.close();
        } else {
            LoggerManager.debug(channelSession + " ping");
            mqttBizService.ping(channelSession.getUserId(), channelSession.getCid());
            ctx.writeAndFlush(new MqttPingResp());
        }
    }

}
