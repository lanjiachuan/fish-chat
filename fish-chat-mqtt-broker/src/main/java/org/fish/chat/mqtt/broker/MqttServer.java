package org.fish.chat.mqtt.broker;

import org.fish.chat.common.log.LoggerManager;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * mqtt服务器 netty初始化类
 *
 * @author adre
 */
@Component
public class MqttServer implements InitializingBean {

    private int port;
    @Autowired
    private MqttDispatcherHandler mqttDispatcherHandler;
    @Autowired
    private ChannelSessionManager channelSessionManager;

    public boolean start() {
        new Thread(this::run).start();

        return true;
    }

    private void run() {
        LoggerManager.info("MqttServer start ");
        // boss group 只暴露一个端口 所以只配置一个线程
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch) {
                            MqttIdleStateHandler idleStateHandler = new MqttIdleStateHandler(
                                    180, 200, 200);
                            idleStateHandler
                                    .setChannelSessionManager(channelSessionManager);
                            ch.pipeline().addLast(idleStateHandler);
                            ch.pipeline().addLast(new MqttProtocolDecoder());
                            ch.pipeline().addLast(new MqttProtocolEncoder());
                            ch.pipeline().addLast(mqttDispatcherHandler);
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LoggerManager.error("", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public MqttServer(int port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(mqttDispatcherHandler, "mqttDispatcherHandler must not null!");
        Assert.notNull(channelSessionManager, "channelSessionManager must not null!");
    }

}
