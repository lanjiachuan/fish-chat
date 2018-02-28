package org.fish.chat.mqtt;

import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 入口
 *
 * 加载spring 唤起MqttServer
 *
 * @author adre
 */
@EnableAutoConfiguration
@ComponentScan
@EnableFeignClients
public class MqttApplication {

    private static volatile boolean running = true;

    /**
     * @param args
     */
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();

        // 启动spring boot服务
        SpringApplication.run(MqttApplication.class, args);

        RequestIdUtil.setRequestId();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                LoggerManager.info(" App [MqttApplication] stopped!");
            } catch (Throwable t) {
                LoggerManager.error(t.getMessage(), t);
            } finally {
                LoggerManager.info("shutdown [ MqttApplication] success, running time:"
                        + ((System.currentTimeMillis() - start) / 60000) + " m");
            }
            synchronized (MqttApplication.class) {
                running = false;
                MqttApplication.class.notify();
            }
        }));

        LoggerManager.info("start up [MqttApplication] sucess, time used:"
                + (System.currentTimeMillis() - start) + " ms");

        synchronized (MqttApplication.class) {
            while (running) {
                try {
                    MqttApplication.class.wait();
                } catch (Throwable e) {}
            }
        }

        LoggerManager.info("shutdown[MqttApplication] success!");
    }

}
