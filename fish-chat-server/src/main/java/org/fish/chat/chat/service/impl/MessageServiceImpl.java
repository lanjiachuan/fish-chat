package org.fish.chat.chat.service.impl;

import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.MessageChatStatistics;
import org.fish.chat.chat.model.TaskReceiveCount;
import org.fish.chat.chat.model.UserMessageStatistics;
import org.fish.chat.chat.service.MessageService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by adre on 2017/1/7.
 */
public class MessageServiceImpl implements MessageService {

    @Override
    public boolean saveMessage(Message message, boolean saveInbox, boolean saveOutbox) {
        return false;
    }

    @Override
    public int getUnreadMessageCount(long userId, int identity) {
        return 0;
    }

    @Override
    public List<Message> getUnreadMessage(long userId, int userType) {
        return null;
    }

    @Override
    public List<Message> getMessageAfterMaxId(long userId, int userType, long maxId) {
        return null;
    }

    @Override
    public boolean readUserMessage(long userId, long friendId, long lastMessageId) {
        return false;
    }

    @Override
    public boolean deleteMessage(long userId, List<Long> idList) {
        return false;
    }

    @Override
    public long getNextMessageId() {
        return 0;
    }

    @Override
    public List<Message> getMessageHistory(long userId1, int identity, long userId2) {
        return null;
    }

    @Override
    public List<Message> getMessageHistoryByPage(long userId1, int identity, long userId2, int maxCursor, int chatType, int pageSize) {
        return null;
    }

    @Override
    public List<MessageChatStatistics> getMessageChatStatistics(List<Long> uids) {
        return null;
    }

    @Override
    public List<UserMessageStatistics> getUserMessageStatistics(List<Long> uids) {
        return null;
    }

    @Override
    public List<Message> syncMessage(long uid, int identity, Collection<Long> friendIds, int friendIdentity, long lastMessageId) {
        return null;
    }

    @Override
    public List<Message> getFriendMessageHistoryBeforeLast(long userId, int identity, long friendId, long lastMessageId, int limit) {
        return null;
    }

    @Override
    public Message getMessageById(long msgId) {
        return null;
    }

    @Override
    public boolean updateMessage(Message message) {
        return false;
    }

    @Override
    public Map<Long, TaskReceiveCount> getReceivedCountByTaskId(List<Long> taskIdList) {
        return null;
    }

    @Override
    public List<Message> getUserLastMessage(List<Long> userIds, int page, int pageSize) {
        return null;
    }
}
