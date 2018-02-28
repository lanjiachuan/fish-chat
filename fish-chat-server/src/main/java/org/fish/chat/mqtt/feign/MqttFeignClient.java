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
    boolean publish(@RequestParam(name = "userId") long userId,
                    @RequestParam(name = "cid") long cid,
                    @RequestParam(name = "publish") MqttPublish publish);

    /**
     * pubRel
     * @param userId
     * @param cid
     * @param pubRel
     * @return
     */
    @RequestMapping("pubRel")
    @Override
    boolean pubRel(@RequestParam(name = "userId") long userId,
                   @RequestParam(name = "cid") long cid,
                   @RequestParam(name = "pubRel") MqttPubRel pubRel);

    /**
     * publish close
     * @param userId
     * @param cid
     * @param publish
     * @return
     */
    @RequestMapping("publishAndClose")
    @Override
    boolean publishAndClose(@RequestParam(name = "userId") long userId,
                            @RequestParam(name = "cid") long cid,
                            @RequestParam(name = "publish") MqttPublish publish);

    /**
     * close channel
     * @param userId
     * @param cid
     * @return
     */
    @RequestMapping("close")
    @Override
    boolean close(@RequestParam(name = "userId") long userId,
                  @RequestParam(name = "cid") long cid);
}
