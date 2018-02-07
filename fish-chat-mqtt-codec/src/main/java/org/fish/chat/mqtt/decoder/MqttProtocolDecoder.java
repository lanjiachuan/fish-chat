package org.fish.chat.mqtt.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.mqtt.protocol.MqttException;
import org.fish.chat.mqtt.protocol.wire.MqttWireMessage;

import java.util.List;

/**
 * mqtt消息解码
 *
 * 自定义decode方法 MqttWireMessage解析mqtt消息 ReplayingDecoder解决粘包等问题
 *
 * @author adre
 */
public class MqttProtocolDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        MqttWireMessage mwm = null;
        try {
            mwm = MqttWireMessage.createWireMessage(new ByteBufInputStream(in));
            out.add(mwm);
        } catch (MqttException e) {
            LoggerManager.warn("fail to create a mqtt message!");
        }
    }

    /**
     * 其实没有存在的意义
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

}
