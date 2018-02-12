package org.fish.chat.chat.handler;


import org.fish.chat.chat.ChatProtocol;

import java.util.Map;

/**
 * Iq 消息 模拟http请求
 * 目前只有历史消息
 *
 * @author adre
 */
public interface IqHandler {

    /**
     * Iq消息
     * @param userId
     * @param qid 唯一id
     * @param query http uri
     * @param params http params
     * @return
     */
    ChatProtocol.FishChatProtocol handle(long userId, long qid, String query, Map<String, String> params);
}
