package org.fish.chat.mqtt.handler;

import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttUnsubAck;
import org.fish.chat.mqtt.protocol.wire.MqttUnsubscribe;
import org.fish.chat.mqtt.session.ChannelSession;
import org.springframework.stereotype.Component;

/**
 * Comments for MqttUnsubscribeHandler.java
 *
 * @author adre
 */
@Component
public class MqttUnsubscribeHandler extends AbstractMqttHandler<MqttUnsubscribe> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttUnsubscribe msg) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        if (channelSession != null) {
            ctx.writeAndFlush(new MqttUnsubAck(msg));
            chatFeignClient.unsubscribe(channelSession.getUserId(), channelSession.getCid(), msg);
        } else {
            ctx.close();
        }
    }

}
