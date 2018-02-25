package org.fish.chat.mqtt.controller;

import org.fish.chat.mqtt.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author adre
 * @version 创建时间：2018/2/25 下午7:30
 */
@RestController
public class MqttController {

    @Autowired
    private MqttService mqttService;


}
