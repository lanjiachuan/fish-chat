package org.fish.chat.mqtt.feign;

import org.fish.chat.mqtt.protocol.wire.*;
import org.fish.chat.mqtt.service.MqttBizService;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * https://stackoverflow.com/questions/44313482/fiegn-client-with-spring-boot-requestparam-value-was-empty-on-parameter-0
 *
 * @author adre
 * @version 创建时间：2018/2/28 下午4:34
 */
@FeignClient(name = "chat-provider")
public interface ChatFeignClient extends MqttBizService {

    /**
     * 连接
     * @param userId
     * @param cid
     * @param mqttConnect
     * @param ip
     * @param type
     * @return
     */
    @RequestMapping("connect")
    @Override
    MqttConnack connect(@RequestParam(name = "userId") long userId,
                        @RequestParam(name = "cid") long cid,
                        @RequestParam(name = "mqttConnect") MqttConnect mqttConnect,
                        @RequestParam(name = "ip") String ip,
                        @RequestParam(name = "type") int type);

    /**
     * 断开
     * @param userId
     * @param cid
     * @param mqttDisconnect
     */
    @RequestMapping("disconnect")
    @Override
    void disconnect(@RequestParam(name = "userId") long userId,
                    @RequestParam(name = "cid") long cid,
                    @RequestParam(name = "mqttDisconnect") MqttDisconnect mqttDisconnect);

    /**
     * 发布消息
     * @param userId
     * @param cid
     * @param mqttPublish
     * @return
     */
    @RequestMapping("publish")
    @Override
    boolean publish(@RequestParam(name = "userId") long userId,
                    @RequestParam(name = "cid") long cid,
                    @RequestParam(name = "mqttPublish") MqttPublish mqttPublish);

    /**
     * 心跳检测
     * @param userId
     * @param cid
     */
    @RequestMapping("ping")
    @Override
    void ping(@RequestParam(name = "userId") long userId,
              @RequestParam(name = "cid") long cid);

    /**
     * 发布确认
     * Qos=1
     * @param userId
     * @param cid
     * @param mqttPuback
     * @return
     */
    @RequestMapping("pubAck")
    @Override
    boolean pubAck(@RequestParam(name = "userId") long userId,
                   @RequestParam(name = "cid") long cid,
                   @RequestParam(name = "mqttPuback") MqttPubAck mqttPuback);

    /**
     * 发布信息收到
     * Qos=2
     * @param userId
     * @param cid
     * @param mqttPubRec
     * @return
     */
    @RequestMapping("pubRec")
    @Override
    boolean pubRec(@RequestParam(name = "userId") long userId,
                   @RequestParam(name = "cid") long cid,
                   @RequestParam(name = "mqttPubRec") MqttPubRec mqttPubRec);

    /**
     * 发布信息分发
     * Qos=2
     * @param userId
     * @param cid
     * @param mqttPubRel
     * @return
     */
    @RequestMapping("pubRel")
    @Override
    boolean pubRel(@RequestParam(name = "userId") long userId,
                   @RequestParam(name = "cid") long cid,
                   @RequestParam(name = "mqttPubRel") MqttPubRel mqttPubRel);

    /**
     * 发布完成消息
     * Qos=2
     * @param userId
     * @param cid
     * @param mqttPubComp
     * @return
     */
    @RequestMapping("pubComp")
    @Override
    boolean pubComp(@RequestParam(name = "userId") long userId,
                    @RequestParam(name = "cid") long cid,
                    @RequestParam(name = "mqttPubComp") MqttPubComp mqttPubComp);

    /**
     * 订阅
     * @param userId
     * @param cid
     * @param mqttSubscribe
     * @return
     */
    @RequestMapping("subscribe")
    @Override
    boolean subscribe(@RequestParam(name = "userId") long userId,
                      @RequestParam(name = "cid") long cid,
                      @RequestParam(name = "mqttSubscribe") MqttSubscribe mqttSubscribe);

    /**
     * 取消订阅
     * @param userId
     * @param cid
     * @param mqttUnubscribe
     * @return
     */
    @RequestMapping("unsubscribe")
    @Override
    boolean unsubscribe(@RequestParam(name = "userId") long userId,
                        @RequestParam(name = "cid") long cid,
                        @RequestParam(name = "mqttUnubscribe") MqttUnsubscribe mqttUnubscribe);

    /**
     * channel 失效
     * @param userId
     * @param cid
     */
    @RequestMapping("channelInactive")
    @Override
    void channelInactive(@RequestParam(name = "userId") long userId,
                         @RequestParam(name = "cid") long cid);
}
