
package org.fish.chat.mqtt.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.fish.chat.mqtt.protocol.wire.MqttWireMessage;

import java.util.List;

/**
 * Comments for MqttProtocolDecoderDispatcher.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月8日 下午5:37:02
 */
public class MqttProtocolDecoder extends ReplayingDecoder<Void> {

    /* (non-Javadoc)
     * @see io.netty.handler.codec.ByteToMessageDecoder#decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List)
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MqttWireMessage mwm = MqttWireMessage.createWireMessage(new ByteBufInputStream(in));
        out.add(mwm);
    }

    /* (non-Javadoc)
     * @see io.netty.handler.codec.ReplayingDecoder#channelInactive(io.netty.channel.ChannelHandlerContext)
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

}
