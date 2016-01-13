package org.fish.chat.common.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Comments for RequestIdUtil.java
 *
 */
public class RequestIdUtil {

    private static AtomicLong sender = new AtomicLong(1);

    private static ThreadLocal<Long> requestId = new ThreadLocal<Long>();

    public static long getRequestId() {
        Long value = requestId.get();
        return value == null ? 0 : value;
    }

    public static void setRequestId() {
        long currentRequestId = sender.getAndIncrement();
        requestId.set(currentRequestId);
    }

    public static void setRequestId(long id) {
        requestId.set(id);
    }

    public static void cleanRequestId() {
        requestId.remove();
    }
}
