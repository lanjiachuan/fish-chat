package org.fish.chat.mqtt.handler;

import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttPubComp;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * 发布完成消息
 * Qos=2
 *
 * @author adre
 */
public class MqttPubCompHandler extends AbstractMqttHandler<MqttPubComp> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttPubComp msg) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        if (channelSession != null) {
            mqttBizService.pubComp(channelSession.getUserId(), channelSession.getCid(), msg);
        } else {
            ctx.close();
        }
    }

}
