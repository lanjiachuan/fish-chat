package org.fish.chat.mqtt.handler;


import io.netty.channel.ChannelHandlerContext;
import org.fish.chat.mqtt.service.MqttBizService;
import org.fish.chat.mqtt.session.manager.ChannelSessionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author adre
 */
@Service
public abstract class AbstractMqttHandler<T> implements InitializingBean {

    @Autowired
    protected ChannelSessionManager channelSessionManager;
    @Autowired
    protected MqttBizService mqttBizService;

    /**
     * channel read
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public abstract void channelRead(ChannelHandlerContext ctx, T msg) throws Exception;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(channelSessionManager, "channelSessionManager must not null!");
        Assert.notNull(mqttBizService, "mqttBizService must not null!");
    }
}
