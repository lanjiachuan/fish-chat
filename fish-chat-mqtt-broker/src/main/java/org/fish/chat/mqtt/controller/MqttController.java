package org.fish.chat.mqtt.controller;

import org.fish.chat.mqtt.protocol.wire.MqttPubRel;
import org.fish.chat.mqtt.protocol.wire.MqttPublish;
import org.fish.chat.mqtt.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author adre
 * @version 创建时间：2018/2/25 下午7:30
 */
@RestController
@EnableDiscoveryClient
public class MqttController {

    @Autowired
    private MqttService mqttService;

    @RequestMapping("publish")
    public boolean publish(@RequestParam long userId,
                           @RequestParam long cid,
                           @RequestParam MqttPublish publish) {
        return mqttService.publish(userId, cid, publish);
    }

    @RequestMapping("pubRel")
    public boolean pubRel(@RequestParam long userId,
                          @RequestParam long cid,
                          @RequestParam MqttPubRel pubRel) {
        return mqttService.pubRel(userId, cid, pubRel);
    }

    @RequestMapping("publishAndClose")
    public boolean publishAndClose(@RequestParam long userId,
                                   @RequestParam long cid,
                                   @RequestParam MqttPublish publish) {
        return mqttService.publishAndClose(userId, cid, publish);
    }

    @RequestMapping("close")
    public boolean close(@RequestParam long userId,
                         @RequestParam long cid) {
        return mqttService.close(userId, cid);
    }

    @RequestMapping("hello")
    public String echo() {
        return "hello boot";
    }
}
