/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.service;


import org.fish.chat.chat.model.Message;

import java.util.List;

/**
 * Comments for ChatSystemService.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年5月15日 上午10:40:25
 */
public interface ChatSystemService {

    public void sendMessage(long fromUid, Message message);

    // 系统用户发送消息 不是有uidLoadBalance，而是使用轮询，使压力负载到多台机器
    public void sendSystemMessage(Message message);

    // 保存消息并返回messageId
    public long saveMessage(long fromUid, Message message);

    public void sendTextMessage(long fromId, int identity, long toId, int toIdentity, String text);

    public void sendTextMessage(long fromId, int identity, long toId, int toIdentity, String text, int messageType);

    public void multiSendTextMessage(long fromUid, int identity, List<Long> toUidList, int toIdentity, String text, long taskId);

    public void multiSendMessage(long fromUid, int identity, List<Long> toUidList, Message message);

    public void multiSendSystemMessage(List<Long> toUidList, Message message);

    // 将某人踢下线
    public void kickoff(long userId);

    // 冻结某人某身份
    public void identityFreeze(long userId, int identity, boolean canExplain);

}
