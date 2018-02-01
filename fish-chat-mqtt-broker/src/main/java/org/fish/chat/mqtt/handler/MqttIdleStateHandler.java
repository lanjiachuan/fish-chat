package org.fish.chat.mqtt.handler;

import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.fish.chat.mqtt.session.ChannelSession;
import org.fish.chat.mqtt.session.manager.ChannelSessionManager;

/**
 * channel 处于idle状态触发
 *
 * 待测试 是否需要关闭上下文？
 *
 * @author adre
 */
public class MqttIdleStateHandler extends IdleStateHandler {

    private ChannelSessionManager channelSessionManager;

    /**
     * @param readerIdleTimeSeconds
     * @param writerIdleTimeSeconds
     * @param allIdleTimeSeconds
     */
    public MqttIdleStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds,
            int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        if (evt.state() == IdleState.READER_IDLE || evt.state() == IdleState.ALL_IDLE) {
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

            //todo 测试是否需要关闭上下文

            ctx.close();
        }
    }

    /**
     * @param channelSessionManager the channelSessionManager to set
     */
    public void setChannelSessionManager(ChannelSessionManager channelSessionManager) {
        this.channelSessionManager = channelSessionManager;
    }

}
