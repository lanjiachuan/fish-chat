/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.mqtt.exception;


/**
 * Comments for MqttProtocolException.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月9日 上午9:58:20
 */
public class MqttProtocolException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -4507182148334159175L;

    public MqttProtocolException(String message) {
        super(message);
    }
}
