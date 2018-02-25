package org.fish.chat.mqtt.broker;

import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * 入口
 *
 * 加载spring 唤起MqttServer
 *
 * @author adre
 */
@EnableAutoConfiguration
public class MqttBootstrap {

    private static volatile boolean running = true;

    /**
     * @param args
     */
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();

        // 启动spring boot服务
        SpringApplication.run(MqttBootstrap.class, args);

        RequestIdUtil.setRequestId();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                LoggerManager.info(" App [MqttBootstrap] stopped!");
            } catch (Throwable t) {
                LoggerManager.error(t.getMessage(), t);
            } finally {
                LoggerManager.info("shutdown [ MqttBootstrap] success, running time:"
                        + ((System.currentTimeMillis() - start) / 60000) + " m");
            }
            synchronized (MqttBootstrap.class) {
                running = false;
                MqttBootstrap.class.notify();
            }
        }));

        LoggerManager.info("start up [MqttBootstrap] sucess, time used:"
                + (System.currentTimeMillis() - start) + " ms");

        synchronized (MqttBootstrap.class) {
            while (running) {
                try {
                    MqttBootstrap.class.wait();
                } catch (Throwable e) {}
            }
        }

        LoggerManager.info("shutdown[MqttBootstrap] success!");
    }

}
