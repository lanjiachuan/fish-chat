package org.fish.chat.common.cache.redis;

import cn.techwolf.common.log.LoggerManager;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.fish.chat.common.utils.ThreadUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by adre on 2016/12/25.
 */
public class FishCacheRedis implements InitializingBean {

    private String redisAddress; // 连接地址
    private int dbSize;
    private List<JedisPool> dupRedis;


    private final int CACHE_DB = 1;
    private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();


    public final ThreadPoolExecutor dbpool = new ThreadPoolExecutor(12, 12, 1, TimeUnit.MINUTES,
            new ArrayBlockingQueue<Runnable>(20),
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


    public <T> T getFromRedis(final String key, final Class<T> classOfT) {
        try {
            try (Jedis j = dupRedis.get(0).getResource()) {
                j.select(CACHE_DB);
                String v = j.get(key);
                if (v != null) {
                    return gson.fromJson(v, classOfT);
                }
            }
        } catch (Exception e) {
            LoggerManager.error(key, e);
        }
        return null;
    }

    public void saveToRedis(final String key, final Object obj, final int seconds) {
        if (key != null && obj != null) {
            dbpool.submit(new Runnable() {
                @Override
                public void run() {
                    try (Jedis j = dupRedis.get(0).getResource()) {
                        String value = gson.toJson(obj);
                        j.select(CACHE_DB);
                        j.setex(key, seconds, value); // 保存在db1 里面,cache6分钟
                    }
                }
            });
        }
    }

    /**
     * 默认存储6分钟
     * @param key
     * @param obj
     */
    public void saveToRedis(final String key, final Object obj) {
        saveToRedis(key, obj, 60 * 60);
    }

    public long rpush(final String key, final String s) {
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

    public long lpush(final String key, final String s) {
        final Long[] position = new Long[1];
        if (key != null && StringUtils.isNotEmpty(s)) {
            dbpool.submit(new Runnable() {
                @Override
                public void run() {
                    try (Jedis j = dupRedis.get(0).getResource()) {
                        j.select(CACHE_DB);
                        position[0] = j.lpush(key, s);
                    }
                }
            });
        }
        return position[0];
    }

    public String lpop(final String key) {
        final String[] value = {""};
        if (key != null) {
            dbpool.submit(new Runnable() {
                @Override
                public void run() {
                    try (Jedis j = dupRedis.get(0).getResource()) {
                        j.select(CACHE_DB);
                        value[0] = j.lpop(key);
                    }
                }
            });
        }
        return value[0];
    }

    public String lpeek(final String key) {
        final String[] value = {""};
        if (key != null) {
            dbpool.submit(new Runnable() {
                @Override
                public void run() {
                    try (Jedis j = dupRedis.get(0).getResource()) {
                        j.select(CACHE_DB);
                        value[0] = j.lindex(key, 0);
                    }
                }
            });
        }
        return value[0];
    }

    public Long llen(final String key) {
        final Long[] value = new Long[1];
        if (key != null) {
            dbpool.submit(new Runnable() {
                @Override
                public void run() {
                    try (Jedis j = dupRedis.get(0).getResource()) {
                        j.select(CACHE_DB);
                        value[0] = j.llen(key);
                    }
                }
            });
        }
        return value[0];
    }

    public void del(final String key) {
        if (key != null) {
            dbpool.submit(new Runnable() {
                @Override
                public void run() {
                    try (Jedis j = dupRedis.get(0).getResource()) {
                        j.select(CACHE_DB);
                        j.del(key);
                    }
                }
            });
        }
    }

    public void expire(final String key, final int seconds) {
        final Long[] position = new Long[1];
        if (key != null) {
            dbpool.submit(new Runnable() {
                @Override
                public void run() {
                    try (Jedis j = dupRedis.get(0).getResource()) {
                        j.select(CACHE_DB);
                        j.expire(key, seconds);
                    }
                }
            });
        }
    }

    public void setRedisAddress(String redisAddress) {
        this.redisAddress = redisAddress;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(redisAddress, "redisAddress is required!");
        Assert.notNull(dbSize, "dbSize is required!");
        initJedisPools();
    }
}
