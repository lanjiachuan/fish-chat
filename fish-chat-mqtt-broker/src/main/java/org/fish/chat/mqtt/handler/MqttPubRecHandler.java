/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.handler;

import org.fish.chat.common.log.LoggerManager;
import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttPubRec;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * Comments for MqttPubRecHandler.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月11日 上午11:19:37
 */
public class MqttPubRecHandler extends AbstractMqttHandler<MqttPubRec> {

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.handler.AbstractMqttHandler#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttPubRec msg) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        if (channelSession != null) {
            mqttBizService.pubRec(channelSession.getUserId(), channelSession.getCid(), msg);
        } else {
            LoggerManager.error("channelSession was null, but receive " + msg);
            ctx.close();
        }
    }

}
