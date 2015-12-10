package org.fish.chat.mqtt.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.fish.chat.mqtt.protocol.wire.MqttWireMessage;

public class MqttProtocolEncoder extends MessageToByteEncoder<MqttWireMessage> {

    /* (non-Javadoc)
     * @see io.netty.handler.codec.MessageToByteEncoder#encode(io.netty.channel.ChannelHandlerContext, java.lang.Object, io.netty.buffer.ByteBuf)
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, MqttWireMessage msg, ByteBuf out)
            throws Exception {
        out.writeBytes(msg.getBytes());
    }

}
