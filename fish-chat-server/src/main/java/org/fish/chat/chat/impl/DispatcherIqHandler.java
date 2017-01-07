package org.fish.chat.chat.impl;


import org.fish.chat.chat.ChatProtocol;
import org.fish.chat.chat.handler.IqHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 选择器
 *
 * Comments for IqDispatchHandler.java
 *
 */
public class DispatcherIqHandler implements IqHandler, InitializingBean {

    private Map<String, IqHandler> handlerMap;

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

    public void setHandlerMap(Map<String, IqHandler> handlerMap) {
        this.handlerMap = handlerMap;
    }

}