package org.fish.chat.chat.callback;


import org.fish.chat.chat.model.Message;

import java.util.List;

/**
 * Comments for MessageFinishCallback.java
 *
 */
public interface MessageFinishCallback {

    /**
     * 消息
     * 
     * @param userId
     * @param message
     */
    public void messageFinish(long userId, List<Message> message);
}
