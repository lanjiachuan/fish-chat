/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.broker;

import cn.techwolf.common.log.LoggerManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.fish.chat.mqtt.decoder.MqttProtocolDecoder;
import org.fish.chat.mqtt.encoder.MqttProtocolEncoder;
import org.fish.chat.mqtt.handler.MqttDispatcherHandler;
import org.fish.chat.mqtt.handler.MqttIdleStateHandler;
import org.fish.chat.mqtt.session.manager.ChannelSessionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Comments for MqttServer.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月8日 下午8:15:13
 */
public class MqttServer implements InitializingBean {

    private int port;

    private MqttDispatcherHandler mqttDispatcherHandler;

    private ChannelSessionManager channelSessionManager;

    public MqttServer(int port) {
        this.port = port;
    }

    public boolean start() throws InterruptedException {
        new Thread(new Runnable() {

            @Override
            public void run() {
                LoggerManager.info("MqttServer start ");
                EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap b = new ServerBootstrap(); // (2)
                    b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
                            .childHandler(new ChannelInitializer<SocketChannel>() { // (4)

                                        @Override
                                        public void initChannel(SocketChannel ch) throws Exception {
                                            MqttIdleStateHandler idleStateHandler = new MqttIdleStateHandler(
                                                    180, 200, 200);
                                            idleStateHandler
                                                    .setChannelSessionManager(channelSessionManager);
                                            ch.pipeline().addLast(idleStateHandler);
                                            ch.pipeline().addLast(new MqttProtocolDecoder());
                                            ch.pipeline().addLast(new MqttProtocolEncoder());
                                            ch.pipeline().addLast(mqttDispatcherHandler);
                                        }
                                    }).option(ChannelOption.SO_BACKLOG, 128) // (5)
                            .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

                    // Bind and start to accept incoming connections.
                    ChannelFuture f = b.bind(port).sync(); // (7)

                    f.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    LoggerManager.error("", e);
                } finally {
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                }
            }
        }).start();

        return true;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @param mqttDispatcherHandler the mqttDispatcherHandler to set
     */
    public void setMqttDispatcherHandler(MqttDispatcherHandler mqttDispatcherHandler) {
        this.mqttDispatcherHandler = mqttDispatcherHandler;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(mqttDispatcherHandler, "mqttDispatcherHandler must not null!");
        Assert.notNull(channelSessionManager, "channelSessionManager must not null!");
    }

    /**
     * @param channelSessionManager the channelSessionManager to set
     */
    public void setChannelSessionManager(ChannelSessionManager channelSessionManager) {
        this.channelSessionManager = channelSessionManager;
    }

}
