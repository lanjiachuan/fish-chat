package org.fish.chat.common.cache.redis;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import org.fish.chat.common.utils.ThreadUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 缓存
 *
 * @author adre
 * @date 2016/12/25
 */
public class FishCacheRedis {

    private static final Logger logger = Logger.getLogger(FishCacheRedis.class);

    /**
     * 连接地址
     */
    private String redisAddress = "127.0.0.1:63790";
    private int dbSize = 10;
    private static List<JedisPool> dupRedis;


    private static final int CACHE_DB = 1;
    private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();


    private static final ThreadPoolExecutor dbpool = new ThreadPoolExecutor(12, 12, 1, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(20),
            ThreadUtils.newThreadFactory("db"), new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 初始化redis pool
     */
    private void initJedisPools() {
        for (String r : redisAddress.split(";")) {
            String[] parts = r.split(":");
            if (parts.length == 2) {
                JedisPool t = new JedisPool(
                        new GenericObjectPoolConfig(),
                        parts[0],
                        Integer.parseInt(parts[1]),
                        Protocol.DEFAULT_TIMEOUT, null, dbSize);
                dupRedis.add(t);
            } else {
                throw new IllegalArgumentException("redis server: " + r);
            }
        }
    }


    public static <T> T getFromRedis(final String key, final Class<T> classOfT) {
        try {
            try (Jedis j = dupRedis.get(0).getResource()) {
                j.select(CACHE_DB);
                String v = j.get(key);
                if (v != null) {
                    return gson.fromJson(v, classOfT);
                }
            }
        } catch (Exception e) {
            logger.error(key, e);
        }
        return null;
    }

    public static void saveToRedis(final String key, final Object obj, final int seconds) {
        if (key != null && obj != null) {
            dbpool.submit(() -> {
                try (Jedis j = dupRedis.get(0).getResource()) {
                    String value = gson.toJson(obj);
                    j.select(CACHE_DB);
                    j.setex(key, seconds, value); // 保存在db1 里面,cache6分钟
                }
            });
        }
    }

    /**
     * 默认存储6分钟
     * @param key
     * @param obj
     */
    public static void saveToRedis(final String key, final Object obj) {
        saveToRedis(key, obj, 60 * 60);
    }

    public static long rpush(final String key, final String s) {
        final Long[] position = new Long[1];
        if (key != null && StringUtils.isNotEmpty(s)) {
            dbpool.submit(new Runnable() {
                @Override
                public void run() {
                    try (Jedis j = dupRedis.get(0).getResource()) {
                        j.select(CACHE_DB);
                        position[0] = j.rpush(key, s);
                    }
                }
            });
        }
        return position[0];
    }

    public static long lpush(final String key, final String s) {
        final Long[] position = new Long[1];
        if (key != null && StringUtils.isNotEmpty(s)) {
            dbpool.submit(() -> {
                try (Jedis j = dupRedis.get(0).getResource()) {
                    j.select(CACHE_DB);
                    position[0] = j.lpush(key, s);
                }
            });
        }
        return position[0];
    }

    public static String lpop(final String key) {
        final String[] value = {""};
        if (key != null) {
            dbpool.submit(() -> {
                try (Jedis j = dupRedis.get(0).getResource()) {
                    j.select(CACHE_DB);
                    value[0] = j.lpop(key);
                }
            });
        }
        return value[0];
    }

    public static String lpeek(final String key) {
        final String[] value = {""};
        if (key != null) {
            dbpool.submit(() -> {
                try (Jedis j = dupRedis.get(0).getResource()) {
                    j.select(CACHE_DB);
                    value[0] = j.lindex(key, 0);
                }
            });
        }
        return value[0];
    }

    public static Long llen(final String key) {
        final Long[] value = new Long[1];
        if (key != null) {
            dbpool.submit(() -> {
                try (Jedis j = dupRedis.get(0).getResource()) {
                    j.select(CACHE_DB);
                    value[0] = j.llen(key);
                }
            });
        }
        return value[0];
    }

    public static void del(final String key) {
        if (key != null) {
            dbpool.submit(() -> {
                try (Jedis j = dupRedis.get(0).getResource()) {
                    j.select(CACHE_DB);
                    j.del(key);
                }
            });
        }
    }

    public static void expire(final String key, final int seconds) {
        final Long[] position = new Long[1];
        if (key != null) {
            dbpool.submit(() -> {
                try (Jedis j = dupRedis.get(0).getResource()) {
                    j.select(CACHE_DB);
                    j.expire(key, seconds);
                }
            });
        }
    }

}
