/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.handler;

import cn.techwolf.common.log.LoggerManager;
import cn.techwolf.common.utils.RequestIdUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.fish.chat.mqtt.session.ChannelSession;
import org.fish.chat.mqtt.session.manager.ChannelSessionManager;

/**
 * Comments for IdleStateHandler.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月9日 下午2:40:59
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

    /* (non-Javadoc)
     * @see io.netty.handler.timeout.IdleStateHandler#channelIdle(io.netty.channel.ChannelHandlerContext, io.netty.handler.timeout.IdleStateEvent)
     */
    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
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
