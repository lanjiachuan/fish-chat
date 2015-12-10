/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.handler;

import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttUnsubAck;
import org.fish.chat.mqtt.protocol.wire.MqttUnsubscribe;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * Comments for MqttUnsubscribeHandler.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月10日 下午8:58:24
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
