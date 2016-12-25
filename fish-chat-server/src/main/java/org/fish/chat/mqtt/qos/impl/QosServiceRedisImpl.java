package org.fish.chat.mqtt.qos.impl;


import org.fish.chat.common.cache.redis.FishCacheRedis;
import org.fish.chat.common.log.LoggerManager;
import org.apache.commons.codec.binary.Base64;
import org.fish.chat.mqtt.protocol.wire.MqttPublish;
import org.fish.chat.mqtt.protocol.wire.MqttWireMessage;
import org.fish.chat.mqtt.qos.QosService;
import org.springframework.beans.factory.InitializingBean;


/**
 * Comments for QosServiceRedisImpl.java
 * **/

public class QosServiceRedisImpl extends UserSessionLinstenerAdapter implements QosService,
        InitializingBean {

    private static final int DEFAULT_EXPIRE_TIME = 60;

    private static final String CLIENT_FLIGHT_QUEUE_KEY_PREFIX = "client_flight_queue_";

    private static final String EXACTLY_ONCE_KEY_PREFIX = "exactly_once_";

    private static final String FLIGHT_QUEUE_KEY_PREFIX = "flight_queue_";

    private static final String FLIGHT_PUBLISH_KEY_PREFIX = "flight_publish_";

    private FishCacheRedis fishCacheRedis;

    private String getClientFlightQueueKey(long userId) {
        return CLIENT_FLIGHT_QUEUE_KEY_PREFIX + userId;
    }

    private String getExactlyOnceKey(long userId) {
        return EXACTLY_ONCE_KEY_PREFIX + userId;
    }

    private String getFlightQueueKey(long userId) {
        return FLIGHT_QUEUE_KEY_PREFIX + userId;
    }

    private String getFlightPublishKey(long userId) {
        return FLIGHT_PUBLISH_KEY_PREFIX + userId;
    }


/* (non-Javadoc)
     * @see cn.techwolf.boss.mqtt.qos.QosService#setExactlyOnceMessage(long, cn.techwolf.boss.mqtt.protocol.wire.MqttPublish)
     */

    @Override
    public boolean setExactlyOnceMessage(long userId, MqttPublish mqttPublish) {
        try {
            cacheService.set(getExactlyOnceKey(userId),
                    Base64.encodeBase64String(mqttPublish.getBytes()), DEFAULT_EXPIRE_TIME);
            return true;
        } catch (MqttException e) {
            LoggerManager.error("", e);
            return false;
        }
    }

    @Override
    public int setClientFlightQueueMessage(long userId, MqttPublish message) {
        String key = getClientFlightQueueKey(userId);
        try {
            int pos = cacheService.rpush(key, Base64.encodeBase64String(message.getBytes()));
            cacheService.expire(key, DEFAULT_EXPIRE_TIME);
            System.out.println("push client publish" + message + " to queue " + key + " pos = " + pos);
            return pos;
        } catch (MqttException e) {
            LoggerManager.error("", e);
            return -1;
        }
    }

    @Override
    public MqttPublish getClientFlightQueueMessage(long userId, boolean pop) {
        String key = getClientFlightQueueKey(userId);
        String value;
        if (pop) {
            value = cacheService.lpop(key);
        } else {
            value = cacheService.lpeek(key);
        }
        if (value != null) {
            try {
                MqttWireMessage wireMessage = MqttWireMessage.createWireMessage(Base64
                        .decodeBase64(value));
                if (wireMessage instanceof MqttPublish) {
                    return (MqttPublish) wireMessage;
                }
            } catch (MqttException e) {
                LoggerManager.error("", e);
            }
        }
        return null;
    }

    @Override
    public Long getClinetFlightQueueLen(long userId) {
        Long len = 0L;
        try {
            len = cacheService.llen(getClientFlightQueueKey(userId));
        } catch (Exception e) {
            LoggerManager.error("the key:" + getClientFlightQueueKey(userId) + " value maybe not a collection", e);
        }
        return len;
    }

    @Override
    public MqttPublish getExactlyOnceMessage(long userId, boolean clean) {
        String key = getExactlyOnceKey(userId);
        String value = cacheService.get(key);
        if (value != null) {
            try {
                MqttWireMessage wireMessage = MqttWireMessage.createWireMessage(Base64
                        .decodeBase64(value));
                if (wireMessage instanceof MqttPublish) {
                    if (clean) {
                        cacheService.del(key);
                    }
                    return (MqttPublish) wireMessage;
                }
            } catch (MqttException e) {
                LoggerManager.error("", e);
            }
        }
        return null;
    }


    @Override
    public MqttPersistableWireMessage getInFlightMessage(long userId) {
        String key = getFlightQueueKey(userId);
        String value = cacheService.lpeek(key);
        if (value != null) {
            try {
                MqttWireMessage wireMessage = MqttWireMessage.createWireMessage(Base64
                        .decodeBase64(value));
                if (wireMessage instanceof MqttPersistableWireMessage) {
                    cacheService.expire(key, DEFAULT_EXPIRE_TIME);
                    return (MqttPersistableWireMessage) wireMessage;
                } else {
                    LoggerManager.warn(wireMessage
                            + " is not a instanceof MqttPersistableWireMessage");
                }
            } catch (MqttException e) {
                LoggerManager.error("", e);
            }
        }
        return null;
    }


    @Override
    public MqttPersistableWireMessage getNextMessage(long userId) {
        String key = getFlightQueueKey(userId);
        String value = cacheService.lpop(key);
        if (value != null) {
            return getInFlightMessage(userId);
        }
        return null;
    }


    @Override
    public int addMessage(long userId, MqttPersistableWireMessage message) {
        String key = getFlightQueueKey(userId);
        try {
            int pos = cacheService.rpush(key, Base64.encodeBase64String(message.getBytes()));
            cacheService.expire(key, DEFAULT_EXPIRE_TIME);
            System.out.println("push" + message + " to queue " + key + " pos = " + pos);
            return pos;
        } catch (MqttException e) {
            LoggerManager.error("", e);
            return -1;
        }
    }


    @Override
    public boolean updateMessage(long userId, MqttPubRel message) {
        String key = getFlightQueueKey(userId);
        String flightPublishKey = getFlightPublishKey(userId);
        MqttPersistableWireMessage publishMessage = getInFlightMessage(userId);
        if (publishMessage instanceof MqttPublish) {
            String flightPublish = cacheService.lpop(key);
            try {
                cacheService.set(flightPublishKey, flightPublish, DEFAULT_EXPIRE_TIME);
                cacheService.lpush(key, Base64.encodeBase64String(message.getBytes()));
                cacheService.expire(key, DEFAULT_EXPIRE_TIME);
                return true;
            } catch (MqttException e) {
                LoggerManager.error("", e);
            }
        } else {
            LoggerManager.info("the message int queue is not publish message");
        }
        return false;
    }


    @Override
    public MqttPublish getInflightPublish(long userId, boolean clean) {

        String key = getFlightPublishKey(userId);

        String value = cacheService.get(key);
        if (value != null) {
            try {
                MqttWireMessage wireMessage = MqttWireMessage.createWireMessage(Base64
                        .decodeBase64(value));
                if (wireMessage instanceof MqttPublish) {
                    if (clean) {
                        cacheService.del(key);
                    } else {
                        cacheService.expire(key, DEFAULT_EXPIRE_TIME);
                    }
                    return (MqttPublish) wireMessage;
                } else {
                    LoggerManager.warn(wireMessage
                            + " is not a instanceof MqttPersistableWireMessage");
                }
            } catch (MqttException e) {
                LoggerManager.error("", e);
            }
        }
        return null;

    }



    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cacheService, "cacheService must not null!");
    }


    public void setCacheService(ICacheService cacheService) {
        this.cacheService = cacheService;
    }

    private void cleanup(long userId) {
        cacheService.del(getFlightQueueKey(userId));
        cacheService.del(getExactlyOnceKey(userId));
        cacheService.del(getFlightPublishKey(userId));
    }



    @Override
    public void onSessionDestory(UserSession userSession) {
        cleanup(userSession.getUserId());
    }

    @Override
    public void onSessionDisconnect(UserSession userSession) {
        cleanup(userSession.getUserId());
    }

}

