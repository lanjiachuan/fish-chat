/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.handler;

import org.fish.chat.common.log.LoggerManager;
import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.protocol.wire.MqttDisconnect;
import org.fish.chat.mqtt.session.ChannelSession;

/**
 * Comments for MqttDisConnectHandler.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月10日 下午4:34:02
 */
public class MqttDisconnectHandler extends AbstractMqttHandler<MqttDisconnect> {

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, MqttDisconnect msg) throws Exception {
        MqttDisconnect mqttDisconnect = (MqttDisconnect) msg;
        ChannelSession channelSession = channelSessionManager.getChannelSession(ctx.channel());
        if (channelSession != null) {
            mqttBizService.disconnect(channelSession.getUserId(), channelSession.getCid(),
                    mqttDisconnect);
            channelSessionManager.destoryChannelSession(channelSession);
            LoggerManager.info("destory channelSession successful,uid="
                    + channelSession.getUserId() + ", cid=" + channelSession.getCid());
        } else {
            LoggerManager.warn("not found channelSession!");
        }
        ctx.close();
    }

}
