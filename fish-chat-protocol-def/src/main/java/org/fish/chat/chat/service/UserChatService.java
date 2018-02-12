package org.fish.chat.chat.service;


import org.fish.chat.chat.ChatProtocol;
import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.UserSession;

import java.util.List;

/**
 * 用户聊天服务
 *
 * @author adre
 */
public interface UserChatService {

    /**
     * 下发未读消息
     * @param userSession
     */
    void sendUnreadMessage(UserSession userSession);


    /**
     * 上行
     * @param userId
     * @param message
     * @param sessionType
     */
    void dispatchMessage(long userId, Message message, int sessionType);

    /**
     * 上行
     * @param userId
     * @param identity
     * @param message
     */
    void dispatchSystemMessage(long userId, int identity, Message message);

    /**
     * 下行
     * @param userId
     * @param message
     * @param sessionType
     */
    void sendMessage(long userId, Message message, int sessionType);

    /**
     * 下行
     * @param userId
     * @param messageList
     * @param sessionType
     */
    void sendMessage(long userId, List<Message> messageList, int sessionType);

    /**
     * 下行报文
     * @param userSession
     * @param protocol
     * @param msgId
     */
    void sendProtocol(UserSession userSession, ChatProtocol.FishChatProtocol protocol, int msgId);

    /**
     * 下行
     * @param userSession
     * @param clientMsgId
     * @param msgId
     */
    void syncMessageId(UserSession userSession, long clientMsgId, long msgId);

}
