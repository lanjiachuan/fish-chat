package org.fish.chat.mqtt.broker;

import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Comments for MqttBootstrap.java
 *
 * @author adre
 */
public class MqttBootstrap {

    private static volatile boolean running = true;

    private static final String[] CONFIG_FILES = new String[] { "classpath*:spring/*.xml" };

    private static ClassPathXmlApplicationContext context = null;

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        final long start = System.currentTimeMillis();

        context = new ClassPathXmlApplicationContext(CONFIG_FILES);
        MqttServer mqttServer = context.getBean(MqttServer.class);
        mqttServer.start();
        RequestIdUtil.setRequestId();
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                try {
                    context.stop();
                    LoggerManager.info(" App [MqttBootstrap] stopped!");
                } catch (Throwable t) {
                    LoggerManager.error(t.getMessage(), t);
                } finally {
                    LoggerManager.info("shutdown [ MqttBootstrap] sucess, running time:"
                            + ((System.currentTimeMillis() - start) / 60000) + " m");
                }
                synchronized (MqttBootstrap.class) {
                    running = false;
                    MqttBootstrap.class.notify();
                }
            }
        });

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
