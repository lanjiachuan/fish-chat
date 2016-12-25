package org.fish.chat.common.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工具类
 */
public class ThreadUtils {

    public static ThreadFactory newThreadFactory(final String name) {
        return new ThreadFactory() {
            AtomicInteger atomicInteger = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, name + atomicInteger.getAndIncrement());
            }
        };
    }

    public static ThreadPoolExecutor newUnthrowThreadPool(int corePoolSize,
                                                      int maxPoolSize,
                                                      long keepAliveTime,
                                                      TimeUnit unit,
                                                      int workQueueSize,
                                                      String threadFactoryName) {
        return new ThreadPoolExecutor(corePoolSize,
                maxPoolSize,
                keepAliveTime,
                unit,
                new LinkedBlockingQueue<Runnable>(workQueueSize),
                ThreadUtils.newThreadFactory(threadFactoryName),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
