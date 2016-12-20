package org.fish.chat.mqtt.handler;

import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttUnsubAck;
import org.fish.chat.mqtt.protocol.wire.MqttUnsubscribe;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * Comments for MqttUnsubscribeHandler.java
 *
 */
public class MqttUnsubscribeHandler extends AbstractMqttHandler<MqttUnsubscribe> {

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.handler.AbstracMqttHandler#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttUnsubscribe msg) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        if (channelSession != null) {
            ctx.writeAndFlush(new MqttUnsubAck(msg));
            mqttBizService.unsubscribe(channelSession.getUserId(), channelSession.getCid(), msg);
        } else {
            ctx.close();
        }
    }

}
