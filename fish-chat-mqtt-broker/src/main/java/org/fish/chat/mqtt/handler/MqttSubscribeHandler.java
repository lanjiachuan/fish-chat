/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.handler;

import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttSuback;
import org.fish.chat.mqtt.protocol.wire.MqttSubscribe;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * Comments for MqttSubscribeHandler.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月11日 上午10:13:23
 */
public class MqttSubscribeHandler extends AbstractMqttHandler<MqttSubscribe> {

    /* (non-Javadoc)
     * @see cn.techwolf.mqtt.handler.AbstracMqttHandler#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttSubscribe msg) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        if (channelSession != null) {
            ctx.writeAndFlush(new MqttSuback(msg));
            mqttBizService.subscribe(channelSession.getUserId(), channelSession.getCid(), msg);
        } else {
            ctx.close();
        }
    }

}
