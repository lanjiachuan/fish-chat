package org.fish.chat.chat.service;


import org.fish.chat.chat.model.Message;

/**
 * Comments for DeliverService.java
 *
 */
public interface DeliverService {

    public void deliverMessage(long toUserId, Message message);

}