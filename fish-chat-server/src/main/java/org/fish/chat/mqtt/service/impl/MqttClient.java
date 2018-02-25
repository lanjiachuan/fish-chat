package org.fish.chat.mqtt.service.impl;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.fish.chat.common.utils.OkHttpClientUtil;
import org.fish.chat.mqtt.protocol.MqttMessage;
import org.fish.chat.mqtt.protocol.wire.MqttPersistableWireMessage;
import org.fish.chat.mqtt.protocol.wire.MqttPubRel;
import org.fish.chat.mqtt.protocol.wire.MqttPublish;
import org.fish.chat.mqtt.service.MqttService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author adre E-mail:baiduqing@birdwork.com
 * @version 创建时间：2018/2/25 下午8:39
 */
@Service
public class MqttClient implements MqttService {

    private static final String MQTT_ADDR = "127.0.0.1:8001";
    private static Gson gson = new Gson();

    @Override
    public boolean publish(long userId, long cid, MqttPublish publish) {
        try {
            OkHttpClientUtil.post(MQTT_ADDR, generateJson(userId, cid, publish));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean pubRel(long userId, long cid, MqttPubRel pubRel) {
        return false;
    }

    @Override
    public boolean publishAndClose(long userId, long cid, MqttPublish publish) {
        return false;
    }

    @Override
    public boolean close(long userId, long cid) {
        return false;
    }

    private String generateJson(long userId, long cid, MqttPersistableWireMessage message) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", userId);
        params.put("cid", cid);
        if (message != null) {
            params.put("message", message);
        }
        return gson.toJson(params);
    }

}
