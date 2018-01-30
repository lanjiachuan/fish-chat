package org.fish.chat.chat.impl;

import org.apache.commons.lang.math.NumberUtils;
import org.fish.chat.chat.ChatProtocol;
import org.fish.chat.chat.handler.IqHandler;
import org.fish.chat.chat.model.UserSession;
import org.fish.chat.chat.service.UserSessionService;
import org.fish.chat.common.constants.ChatConstant;
import org.fish.chat.common.log.LoggerManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 历史消息 undone
 *
 * Comments for MessageHistoryIqHandler.java
 *
 */
public class MessageHistoryIqHandler implements IqHandler, InitializingBean {

    private static final String RESULT = "result";
    private static final String HAS_NEXT = "has_next";
    private static final String UID = "uid";
    private static final String MSG_ID = "msg_id";
    private static int LIMIT_COUNT = 20;

    private UserSessionService userSessionService;

    @Override
    public ChatProtocol.FishChatProtocol handle(long userId, long qid, String query, Map<String, String> params) {

        ChatProtocol.FishChatProtocol.Builder protocol = ChatProtocol.FishChatProtocol.newBuilder();
        protocol.setType(ChatConstant.PROTOCOL_TYPE_IQ_RESPONSE);

        ChatProtocol.FishIqResponse.Builder responseBuilder = ChatProtocol.FishIqResponse.newBuilder();
        responseBuilder.setQid(qid);
        responseBuilder.setQuery(query);

        int result = 0;

        long friendId = NumberUtils.toLong(params.get(UID));
        long msgId = NumberUtils.toLong(params.get(MSG_ID), Long.MAX_VALUE);
        msgId = msgId == 0 ? Long.MAX_VALUE : msgId;

        UserSession userSession = userSessionService.getUserSession(userId, UserSession.USER_SESSION_TYPE_CLIENT);

        try {
            // TODO: 2017/1/7 get history message
        } catch (Exception e) {
            LoggerManager.error("", e);
        }
        ChatProtocol.FishKVEntry.Builder uidEntryBuilder = ChatProtocol.FishKVEntry.newBuilder();
        uidEntryBuilder.setKey(UID);
        uidEntryBuilder.setValue(String.valueOf(friendId));
        responseBuilder.addResults(uidEntryBuilder);

        ChatProtocol.FishKVEntry.Builder entryBuilder = ChatProtocol.FishKVEntry.newBuilder();
        entryBuilder.setKey(RESULT);
        entryBuilder.setValue(String.valueOf(result));
        responseBuilder.addResults(entryBuilder);

        protocol.setIqResponse(responseBuilder);

        return protocol.build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userSessionService, "userSessionService must not null!");
    }

    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

}