package org.fish.chat.mqtt.feign;

import org.fish.chat.mqtt.protocol.wire.MqttPubRel;
import org.fish.chat.mqtt.protocol.wire.MqttPublish;
import org.fish.chat.mqtt.service.MqttService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * feign客户端
 *
 * @author adre
 * @version 创建时间：2018/2/28 下午2:58
 */
@FeignClient(name = "mqtt-provider")
public interface MqttFeignClient extends MqttService {

    /**
     * publish
     * @param userId
     * @param cid
     * @param publish
     * @return
     */
    @RequestMapping("publish")
    @Override
    boolean publish(@RequestParam long userId,
                    @RequestParam long cid,
                    @RequestParam MqttPublish publish);

    /**
     * pubRel
     * @param userId
     * @param cid
     * @param pubRel
     * @return
     */
    @RequestMapping("pubRel")
    @Override
    boolean pubRel(@RequestParam long userId,
                   @RequestParam long cid,
                   @RequestParam MqttPubRel pubRel);

    /**
     * publish close
     * @param userId
     * @param cid
     * @param publish
     * @return
     */
    @RequestMapping("publishAndClose")
    @Override
    boolean publishAndClose(@RequestParam long userId,
                            @RequestParam long cid,
                            @RequestParam MqttPublish publish);

    /**
     * close channel
     * @param userId
     * @param cid
     * @return
     */
    @RequestMapping("close")
    @Override
    boolean close(@RequestParam long userId,
                  @RequestParam long cid);
}
