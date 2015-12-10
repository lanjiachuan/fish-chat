/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.handler;

import cn.techwolf.common.log.LoggerManager;
import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttPingReq;
import org.fish.chat.mqtt.protocol.wire.MqttPingResp;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * Comments for MqttPingReqHandler.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月10日 下午4:43:09
 */
public class MqttPingReqHandler extends AbstractMqttHandler<MqttPingReq> {

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttPingReq msg) throws Exception {
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        if (channelSession == null) {
            ctx.close();
        } else {
            LoggerManager.debug(channelSession + " ping");
            mqttBizService.ping(channelSession.getUserId(), channelSession.getCid());
            ctx.writeAndFlush(new MqttPingResp());
        }
    }

}
