package org.fish.chat.chat.server;

import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.common.utils.RequestIdUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 聊天业务服务入口
 *
 * @author adre
 */
@EnableAutoConfiguration
public class ChatServerBootstrap {

    private static volatile boolean running = true;

    public static void main(String[] args) {
        final long start = System.currentTimeMillis();

        // 启动spring boot服务
        SpringApplication.run(ChatServerBootstrap.class, args);

        RequestIdUtil.setRequestId();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
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
