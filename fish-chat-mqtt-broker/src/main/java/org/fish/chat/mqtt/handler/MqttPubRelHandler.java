package org.fish.chat.mqtt.handler;

import org.fish.chat.common.log.LoggerManager;
import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttPubComp;
import org.fish.chat.mqtt.protocol.wire.MqttPubRel;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * Comments for MqttPubRelHandler.java
 *
 */
public class MqttPubRelHandler extends AbstractMqttHandler<MqttPubRel> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttPubRel msg) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        MqttPubRel mqttPubRel = msg;

        MqttPubComp mqttPubComp = new MqttPubComp(mqttPubRel.getMessageId());
        LoggerManager.info(channelSession == null ? "" : channelSession.toString() + "send "
                + mqttPubComp);
        ctx.writeAndFlush(mqttPubComp);
        
        if (channelSession != null) {
            mqttBizService.pubRel(channelSession.getUserId(), channelSession.getCid(), msg);
        } else {
            LoggerManager.error("channelSession was null, but receive " + msg);
            ctx.close();
        }
        

    }

}
