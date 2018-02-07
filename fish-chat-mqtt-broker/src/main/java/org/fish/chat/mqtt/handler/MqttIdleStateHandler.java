package org.fish.chat.mqtt.handler;

import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.fish.chat.mqtt.session.ChannelSession;
import org.fish.chat.mqtt.session.manager.ChannelSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * channel 处于idle状态触发IdleStateEvent
 *
 * 心跳检测 当读写时间超过设定的超时时间
 *
 * @author adre
 */
@Component
public class MqttIdleStateHandler extends IdleStateHandler {

    private ChannelSessionManager channelSessionManager;

    /**
     * @param readerIdleTimeSeconds
     * @param writerIdleTimeSeconds
     * @param allIdleTimeSeconds
     */
    public MqttIdleStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds,
            int allIdleTimeSeconds, ChannelSessionManager channelSessionManager) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
        this.channelSessionManager = channelSessionManager;
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        // channel 没有从peer读操作 关闭ctx
        if (evt.state() == IdleState.READER_IDLE) {
            if (channelSessionManager != null) {
                ChannelSession channelSession = channelSessionManager.getChannelSession(ctx
                        .channel());
                if (channelSession != null) {
                    RequestIdUtil.setRequestId(channelSession.getUserId());
                    LoggerManager.warn("MqttIdleStateHandler idle:" + evt.state() + ", uid="
                            + channelSession.getUserId() + ", cid=" + channelSession.getCid());
                } else {
                    RequestIdUtil.setRequestId(0);
                    LoggerManager.warn("MqttIdleStateHandler idle:" + evt.state());
                }
            }

            // 关闭连接
            ctx.close();
        }
        // WRITER_IDLE 暂时不发送消息 由客户端发送ping 把持心跳
    }

}
