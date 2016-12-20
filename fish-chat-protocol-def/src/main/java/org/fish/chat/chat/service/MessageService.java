package org.fish.chat.chat.service;


import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.MessageChatStatistics;
import org.fish.chat.chat.model.TaskReceiveCount;
import org.fish.chat.chat.model.UserMessageStatistics;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Comments for MessageService.java
 *
 */
public interface MessageService {

    public boolean saveMessage(Message message, boolean saveInbox, boolean saveOutbox);

    /**
     * 获取未读消息
     * 
     * @param userId
     * @return
     */
    public int getUnreadMessageCount(long userId, int identity);

    /**
     * 获取未读消息
     * 
     * @param userId
     * @return
     */
    public List<Message> getUnreadMessage(long userId, int userType);

    /**
     * 获取未读消息
     * 
     * @param userId
     * @return
     */
    public List<Message> getMessageAfterMaxId(long userId, int userType, long maxId);

    /**
     * @param userId
     * @param idList
     * @return
     */
    public boolean receiveMessage(long userId, int identity, List<Long> idList);

    /**
     * 已读消息
     * */
    public boolean readUserMessage(long userId, int identity, long friendId, long lastMessageId);

    /**
     * @param userId
     * @param idList
     * @return
     */
    public boolean deleteMessage(long userId, List<Long> idList);

    /**
     * 获取唯一id
     * 
     * @return
     */
    public long getNextMessageId();

    /**
     * 获取两个人的聊天记录
     * 
     * @param userId1
     * @param identity
     * @param userId2
     * @return
     */
    public List<Message> getMessageHistory(long userId1, int identity, long userId2);

    /**
     * 获取两个人的聊天记录
     * 
     * @param userId1
     * @param identity
     * @param userId2
     * @return
     */
    public List<Message> getMessageHistoryByPage(long userId1, int identity, long userId2, int maxCursor, int chatType, int pageSize);

    /**
     * 获取制定uid的所有聊天统计
     * 
     * @param uids
     * @return
     */
    public List<MessageChatStatistics> getMessageChatStatistics(List<Long> uids);

    /**
     * 获取制定uid的聊天个数统计
     * 
     * @param uids
     * @return
     */
    public List<UserMessageStatistics> getUserMessageStatistics(List<Long> uids);

    /**
     * 获取lastMessage后的所有内容
     * 
     * @param lastMessageId
     * @return
     */
    public List<Message> syncMessage(long uid, int identity, Collection<Long> friendIds, int friendIdentity, long lastMessageId);

    /**
     * 获取lastMessage后的所有内容
     * 
     * @param lastMessageId
     * @return
     */
    public List<Message> getFriendMessageHistoryBeforeLast(long userId, int identity, long friendId, long lastMessageId, int limit);

    /**
     * 根据id获取消息
     * 
     * @param msgId
     * @return
     */
    public Message getMessageById(long msgId);

    /**
     * 根据id获取消息
     * 
     * @param message
     * @return
     */
    public boolean updateMessage(Message message);

    /**
     * 根据taskId获取送达个数
     * 
     * @param taskIdList
     * @return
     */
    public Map<Long, TaskReceiveCount> getReceivedCountByTaskId(List<Long> taskIdList);

    /**
     * 获取指定用户的最后几条消息
     * 
     * @param userIds
     * @param page
     * @param pageSize
     * @return
     */
    public List<Message> getUserLastMessage(List<Long> userIds, int page, int pageSize);

}
