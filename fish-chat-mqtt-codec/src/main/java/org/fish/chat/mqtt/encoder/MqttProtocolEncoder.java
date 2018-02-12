package org.fish.chat.mqtt.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.fish.chat.mqtt.protocol.wire.MqttWireMessage;

/**
 * mqtt消息编码
 *
 * @author adre
 */
public class MqttProtocolEncoder extends MessageToByteEncoder<MqttWireMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MqttWireMessage msg, ByteBuf out)
            throws Exception {
        out.writeBytes(msg.getBytes());
    }

}
