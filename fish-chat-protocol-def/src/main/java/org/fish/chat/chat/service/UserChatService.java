package org.fish.chat.chat.service;


import org.fish.chat.chat.ChatProtocol;
import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.UserSession;

import java.util.List;

/**
 * 用户聊天服务
 *
 */
public interface UserChatService {

    /**
     * 下发未读消息
     */
    public void sendUnreadMessage(UserSession userSession);


    /**
     * 上行
     * 
     * @param userId
     * @param message
     */
    public void dispatchMessage(long userId, Message message, int sessionType);

    /**
     * 上行
     * 
     * @param userId
     * @param message
     */
    public void dispatchSystemMessage(long userId, int identity, Message message);

    /**
     * 下行
     * 
     * @param userId
     * @param message
     */
    public void sendMessage(long userId, Message message, int sessionType);

    /**
     * 下行
     * 
     * @param userId
     */
    public void sendMessage(long userId, List<Message> messageList, int sessionType);

    /**
     * 下行报文
     */
    public void sendProtocol(UserSession userSession, ChatProtocol.FishChatProtocol protocol, int msgId);

    /**
     * 下行
     */
    public void syncMessageId(UserSession userSession, long clientMsgId, long msgId);

}
