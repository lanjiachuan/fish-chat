package org.fish.chat.mqtt.handler;

import org.fish.chat.common.log.LoggerManager;
import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.MqttMessage;
import org.fish.chat.mqtt.protocol.wire.MqttPubAck;
import org.fish.chat.mqtt.protocol.wire.MqttPubRec;
import org.fish.chat.mqtt.protocol.wire.MqttPublish;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * 发布消息
 * Qos=0,1,2
 *
 * @author adre
 */
public class MqttPublishHandler extends AbstractMqttHandler<MqttPublish> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttPublish msg) throws Exception {

        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());

        MqttPublish mqttPublish = msg;
        int qos = mqttPublish.getMessage().getQos();
        switch (qos) {
            case MqttMessage.QOS_AT_LEAST_ONCE:
                MqttPubAck mqttPubAck = new MqttPubAck(mqttPublish);
                LoggerManager.info(channelSession == null ? "" : channelSession.toString()
                        + "send " + mqttPubAck);
                ctx.writeAndFlush(mqttPubAck);
                break;
            case MqttMessage.QOS_EXACTLY_ONCE:
                MqttPubRec mqttPubRec = new MqttPubRec(mqttPublish);
                LoggerManager.info(channelSession == null ? "" : channelSession.toString()
                        + "send " + mqttPubRec);
                ctx.writeAndFlush(mqttPubRec);
                break;
            default:
                break;
        }
        if (channelSession != null) {
            boolean result = mqttBizService.publish(channelSession.getUserId(),
                    channelSession.getCid(), mqttPublish);
            if (!result) {
                ctx.close();
            }
        } else {
            LoggerManager.error("==>receive publish =" + mqttPublish
                    + ", but channelSession was null!");
            ctx.close();
        }

    }

}
