package org.fish.chat.chat.service;


import org.fish.chat.chat.model.Message;

import java.util.List;

/**
 * chat server 发送消息
 * 不通过mqtt调用的发送
 *
 * @author adre
 */
public interface SystemChatService {

    /**
     * 发送消息
     * @param fromUid
     * @param message
     */
    void sendMessage(long fromUid, Message message);

    /**
     * 系统用户发送消息 不是有uidLoadBalance，而是使用轮询，使压力负载到多台机器
     * @param message
     */
    void sendSystemMessage(Message message);

    /**
     * 保存消息并返回messageId
     * @param fromUid
     * @param message
     * @return
     */
    long saveMessage(long fromUid, Message message);

    /**
     * 发布文本消息
     * @param fromId
     * @param identity
     * @param toId
     * @param toIdentity
     * @param text
     */
    void sendTextMessage(long fromId, int identity, long toId, int toIdentity, String text);

    /**
     * 发布文本消息
     * @param fromId
     * @param identity
     * @param toId
     * @param toIdentity
     * @param text
     * @param messageType
     */
    void sendTextMessage(long fromId, int identity, long toId, int toIdentity, String text, int messageType);

    /**
     * 批量发布文本消息
     * @param fromUid
     * @param identity
     * @param toUidList
     * @param toIdentity
     * @param text
     * @param taskId
     */
    void multiSendTextMessage(long fromUid, int identity, List<Long> toUidList, int toIdentity, String text, long taskId);

    /**
     * 批量发布消息
     * @param fromUid
     * @param identity
     * @param toUidList
     * @param message
     */
    void multiSendMessage(long fromUid, int identity, List<Long> toUidList, Message message);

    /**
     * 批量发布系统消息
     * @param toUidList
     * @param message
     */
    void multiSendSystemMessage(List<Long> toUidList, Message message);

    /**
     * 将某人踢下线
     * @param userId
     */
    void kickoff(long userId);

    /**
     * 冻结某人某身份
     * @param userId
     * @param identity
     * @param canExplain
     */
    void identityFreeze(long userId, int identity, boolean canExplain);

}
