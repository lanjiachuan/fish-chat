/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.handler;

import cn.techwolf.common.log.LoggerManager;
import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.MqttMessage;
import org.fish.chat.mqtt.protocol.wire.MqttPubAck;
import org.fish.chat.mqtt.protocol.wire.MqttPubRec;
import org.fish.chat.mqtt.protocol.wire.MqttPublish;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * Comments for MqttPublishHandler.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月10日 下午4:07:24
 */
public class MqttPublishHandler extends AbstractMqttHandler<MqttPublish> {

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttPublish msg) throws Exception {

        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());

        MqttPublish mqttPublish = (MqttPublish) msg;
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
