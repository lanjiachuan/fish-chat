/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.handler;

import cn.techwolf.common.log.LoggerManager;
import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttPubComp;
import org.fish.chat.mqtt.protocol.wire.MqttPubRel;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * Comments for MqttPubRelHandler.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月11日 上午10:59:49
 */
public class MqttPubRelHandler extends AbstractMqttHandler<MqttPubRel> {

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.handler.AbstractMqttHandler#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttPubRel msg) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        MqttPubRel mqttPubRel = (MqttPubRel) msg;

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
