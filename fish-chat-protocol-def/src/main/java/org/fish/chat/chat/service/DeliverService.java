package org.fish.chat.chat.service;


import org.fish.chat.chat.model.Message;

/**
 * 发布消息到mqtt server
 *
 * @author adre
 */
public interface DeliverService {

    /**
     * 发布消息
     * @param toUserId
     * @param message
     */
    void deliverMessage(long toUserId, Message message);

}