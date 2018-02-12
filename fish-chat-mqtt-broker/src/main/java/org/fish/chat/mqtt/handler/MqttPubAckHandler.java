package org.fish.chat.mqtt.handler;

import org.fish.chat.common.log.LoggerManager;
import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttPubAck;
import org.fish.chat.mqtt.session.ChannelSession;
import org.springframework.stereotype.Component;

/**
 * 发布确认
 * Qos=1
 *
 * @author adre
 */
@Component
public class MqttPubAckHandler extends AbstractMqttHandler<MqttPubAck> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttPubAck msg) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        if (channelSession != null) {
            MqttPubAck mqttPuback = msg;
            mqttBizService.pubAck(channelSession.getUserId(), channelSession.getCid(), mqttPuback);
        } else {
            LoggerManager.error("channelSession was null, but receive " + msg);
            ctx.close();
        }
    }

}
