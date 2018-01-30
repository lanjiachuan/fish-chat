package org.fish.chat.chat.server;

import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Comments for ChatBootstrap.java
 *
 */
public class ChatServerBootstrap {

    private static volatile boolean running = true;

    private static final String[] CONFIG_FILES = new String[] { "applicationContext*.xml", "classpath*:spring/*.xml" };

    private static ClassPathXmlApplicationContext context = null;

    public static void main(String[] args) {
        final long start = System.currentTimeMillis();

        context = new ClassPathXmlApplicationContext(CONFIG_FILES);
        context.start();
        RequestIdUtil.setRequestId();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                context.stop();
                LoggerManager.info(" App [ChatServerBootstrap] stopped!");
            } catch (Throwable t) {
                LoggerManager.error(t.getMessage(), t);
            } finally {
                LoggerManager.info("shutdown [ ChatServerBootstrap] success, running time:"
                        + ((System.currentTimeMillis() - start) / 60000) + " m");
            }
            synchronized (ChatServerBootstrap.class) {
                running = false;
                ChatServerBootstrap.class.notify();
            }
        }));

        LoggerManager.info("start up [ChatServerBootstrap] success, time used:" + (System.currentTimeMillis() - start) + " ms");

        synchronized (ChatServerBootstrap.class) {
            while (running) {
                try {
                    ChatServerBootstrap.class.wait();
                } catch (Throwable e) {}
            }
        }

        LoggerManager.info("shutdown[ChatServerBootstrap] success!");
    }

}
