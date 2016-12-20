package org.fish.chat.mqtt.handler;

import org.fish.chat.common.log.LoggerManager;
import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttDisconnect;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * Comments for MqttDisConnectHandler.java
 *
 * mqtt disconnect destroy channel session
 */
public class MqttDisconnectHandler extends AbstractMqttHandler<MqttDisconnect> {

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttDisconnect msg) throws Exception {
        MqttDisconnect mqttDisconnect = msg;
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        if (channelSession != null) {
            mqttBizService.disconnect(channelSession.getUserId(), channelSession.getCid(),
                    mqttDisconnect);
            channelSessionManager.destroyChannelSession(channelSession);
            LoggerManager.info("destory channelSession successful,uid="
                    + channelSession.getUserId() + ", cid=" + channelSession.getCid());
        } else {
            LoggerManager.warn("not found channelSession!");
        }
        ctx.close();
    }

}
