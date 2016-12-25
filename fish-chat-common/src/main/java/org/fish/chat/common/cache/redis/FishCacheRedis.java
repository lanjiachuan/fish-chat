package org.fish.chat.common.cache.redis;

import cn.techwolf.common.log.LoggerManager;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    public void saveToRedis(final String key, final Object obj) {
        if (key != null && obj != null) {
            dbpool.submit(new Runnable() {
                @Override
                public void run() {
                    try (Jedis j = dupRedis.get(0).getResource()) {
                        String value = gson.toJson(obj);
                        j.select(CACHE_DB);
                        j.setex(key, 6 * 60, value); // 保存在db1 里面,cache6分钟
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
