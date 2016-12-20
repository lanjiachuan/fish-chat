package org.fish.chat.chat.handler;


import org.fish.chat.chat.ChatProtocol;

import java.util.Map;

/**
 * Comments for IqDispatchService.java
 *
 */
public interface IqHandler {

    public ChatProtocol.FishChatProtocol handle(long userId, int identity, long qid, String query,
                                                    Map<String, String> params);
}
