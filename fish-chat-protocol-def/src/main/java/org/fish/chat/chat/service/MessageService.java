package org.fish.chat.chat.service;


import org.fish.chat.chat.model.Message;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 消息处理
 *
 * @author adre
 */
public interface MessageService {

    /**
     * 保存消息
     * @param message
     * @param saveInbox
     * @param saveOutbox
     * @return
     */
    boolean saveMessage(Message message, boolean saveInbox, boolean saveOutbox);

    /**
     * 获取未读消息
     * 
     * @param userId
     * @return
     */
    int getUnreadMessageCount(long userId, int identity);

    /**
     * 获取未读消息
     * @param userId
     * @param userType
     * @return
     */
    List<Message> getUnreadMessage(long userId, int userType);

    /**
     * 获取未读消息
     * @param userId
     * @param userType
     * @param maxId
     * @return
     */
    List<Message> getMessageAfterMaxId(long userId, int userType, long maxId);

    /**
     * 已读消息
     * @param userId
     * @param friendId
     * @param lastMessageId
     * @return
     */
    boolean readUserMessage(long userId, long friendId, long lastMessageId);

    /**
     * 删除消息
     * @param userId
     * @param idList
     * @return
     */
    boolean deleteMessage(long userId, List<Long> idList);

    /**
     * 获取消息唯一id
     * 
     * @return
     */
    long getNextMessageId();

    /**
     * 获取两个人的聊天记录
     * 
     * @param userId1
     * @param identity
     * @param userId2
     * @return
     */
    List<Message> getMessageHistory(long userId1, int identity, long userId2);

    /**
     * 获取两个人的聊天记录
     * 
     * @param userId1
     * @param identity
     * @param userId2
     * @return
     */
    List<Message> getMessageHistoryByPage(long userId1, int identity, long userId2, int maxCursor, int chatType, int pageSize);

    /**
     * 获取lastMessage后的所有内容
     * @param uid
     * @param identity
     * @param friendIds
     * @param friendIdentity
     * @param lastMessageId
     * @return
     */
    List<Message> syncMessage(long uid, int identity, Collection<Long> friendIds, int friendIdentity, long lastMessageId);

    /**
     * 获取lastMessage后的所有内容
     * @param userId
     * @param identity
     * @param friendId
     * @param lastMessageId
     * @param limit
     * @return
     */
    List<Message> getFriendMessageHistoryBeforeLast(long userId, int identity, long friendId, long lastMessageId, int limit);

    /**
     * 根据id获取消息
     * 
     * @param msgId
     * @return
     */
    Message getMessageById(long msgId);

    /**
     * 更新消息
     * 
     * @param message
     * @return
     */
    boolean updateMessage(Message message);

    /**
     * 获取指定用户的最后几条消息
     * 
     * @param userIds
     * @param page
     * @param pageSize
     * @return
     */
    List<Message> getUserLastMessage(List<Long> userIds, int page, int pageSize);

}
