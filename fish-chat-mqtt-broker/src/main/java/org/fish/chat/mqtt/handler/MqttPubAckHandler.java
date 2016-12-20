package org.fish.chat.mqtt.handler;

import org.fish.chat.common.log.LoggerManager;
import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttPubAck;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * Comments for MqttPubackHandler.java
 *
 */
public class MqttPubAckHandler extends AbstractMqttHandler<MqttPubAck> {

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */
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
