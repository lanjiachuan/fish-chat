package org.fish.chat.chat.impl;


import com.google.common.collect.ImmutableMap;
import org.fish.chat.chat.ChatProtocol;
import org.fish.chat.chat.handler.IqHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 选择器
 *
 * @author adre
 */
@Service
public class DispatcherIqHandler implements IqHandler, InitializingBean {

    @Autowired
    private MessageHistoryIqHandler messageHistoryIqHandler;
    private Map<String, IqHandler> handlerMap = ImmutableMap.of("/message/history", messageHistoryIqHandler);

    @Override
    public ChatProtocol.FishChatProtocol handle(long userId, long qid, String query, Map<String, String> params) {
        IqHandler iqHandler = handlerMap.get(query);
        if (iqHandler != null) {
            return iqHandler.handle(userId, qid, query, params);
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(handlerMap, "handlerMap must not null!");
    }
}