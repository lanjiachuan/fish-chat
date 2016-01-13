/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.service.impl;


import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.chat.model.ActionMessage;
import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.UserSession;
import org.fish.chat.chat.service.ChatSystemService;
import org.fish.chat.chat.service.MessageService;
import org.fish.chat.chat.service.UserChatService;
import org.fish.chat.chat.utils.MessageUtil;
import org.fish.chat.common.constants.ChatConstant;
import org.fish.chat.common.utils.JsonReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 对外提供的发消息接口
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年5月15日 上午10:43:34
 */
public class ChatSystemServiceImpl implements ChatSystemService, InitializingBean {

    private MessageService messageService;
    private UserChatService userChatService;

    private ExecutorService executorService = new ThreadPoolExecutor(16, 16, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000000));

    @Override
    public void sendTextMessage(final long fromId, final int identity, final long toId, final int toIdentity, final String text) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Message message = MessageUtil.createTextMessage(0, fromId, identity, toId, toIdentity,
                        Message.MESSAGE_TEMPLATE_NORMAL, Message.MESSAGE_TYPE_SYSTEM, text);
                userChatService.dispatchSystemMessage(fromId, identity, message);
            }
        });
    }

    @Override
    public void sendTextMessage(final long fromId, final int identity, final long toId, final int toIdentity, final String text, final int messageType) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Message message = MessageUtil.createTextMessage(0, fromId, identity, toId, toIdentity,
                        Message.MESSAGE_TEMPLATE_NORMAL, messageType, text);
                userChatService.dispatchSystemMessage(fromId, identity, message);
            }
        });
    }

    @Override
    public void multiSendTextMessage(final long fromUid, final int identity, final List<Long> toUidList, int toIdentity, String text, long taskId) {
        final Message message = MessageUtil.createTextMessage(0, fromUid, identity, 0, toIdentity, Message.MESSAGE_TEMPLATE_NORMAL, Message.MESSAGE_TYPE_SYSTEM, text);
        message.setTaskId(taskId);
        long messageId = saveMessage(message.getFrom().getUid(), message);
        if (messageId > 0) {
            message.setEntrySaved(true);
            message.setId(messageId);
        }

        executorService.submit(new Runnable() {

            @Override
            public void run() {
                for (long toUid : toUidList) {
                    message.setTo(toUid);
                    userChatService.dispatchSystemMessage(fromUid, identity, message);
                }
            }
        });
    }

    @Override
    public void kickoff(final long userId) {
        ActionMessage message = new ActionMessage();
        message.setTo(userId);
        message.setFrom(ChatConstant.SYSTEM_USER_ID);
        message.setActionType(ActionMessage.ActionType.KICK_OFF);
        message.setPersistence(false);
        message.setTemplateId(Message.MESSAGE_TEMPLATE_NORMAL);
        message.setId(System.currentTimeMillis());
        message.setCreateTime(new Date());

        userChatService.dispatchMessage(ChatConstant.SYSTEM_USER_ID, message, UserSession.USER_SESSION_TYPE_CLIENT);
    }

    @Override
    public void identityFreeze(long userId, int identity, boolean canExplain) {
        ActionMessage message = new ActionMessage();
        message.setTo(userId, identity); // TODO
        message.setFrom(ChatConstant.SYSTEM_USER_ID);
        message.setActionType(ActionMessage.ActionType.IDENTITY_FREEZE);
        message.setPersistence(false);
        message.setTemplateId(Message.MESSAGE_TEMPLATE_NORMAL);
        message.setId(System.currentTimeMillis());
        message.setCreateTime(new Date());
        Map<String, Object> map = new HashMap<String, Object>();

        message.setExtend(JsonReader.fetchString(map));
        sendMessage(message.getFrom().getUid(), message);
    }

    @Override
    public void sendMessage(long fromUid, Message message) {
        userChatService.dispatchSystemMessage(fromUid, message.getFrom().getIdentity(), message);
    }

    @Override
    public void sendSystemMessage(Message message) {
        userChatService.dispatchSystemMessage(message.getFrom().getUid(), message.getFrom().getIdentity(), message);
    }

    @Override
    public long saveMessage(long fromUid, Message message) {
        messageService.saveMessage(message, false, false);
        return message.getId();
    }

    @Override
    public void multiSendSystemMessage(List<Long> toUidList, Message message) {
        multiSendMessage(message.getFrom().getUid(), message.getFrom().getIdentity(), toUidList, message);
    }

    @Override
    public void multiSendMessage(final long fromUid, final int identity,
                                 final List<Long> toUidList, final Message message) {
        if (!message.isEntrySaved()) {
            message.setTo(0);
            long messageId = saveMessage(message.getFrom().getUid(), message);
            if (messageId > 0) {
                message.setEntrySaved(true);
                message.setId(messageId);
            }
        }

        executorService.submit(new Runnable() {

            @Override
            public void run() {
//                message.setChatType(identity); // TODO
                for (long toUid : toUidList) {
                    message.setTo(toUid);
                    message.setCreateTime(new Date());
                    try {
                        userChatService.dispatchSystemMessage(fromUid, identity, message);
                    } catch (Exception e) {
                        LoggerManager.error("", e);
                    }
                }
            }
        });

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userChatService, "userChatService must not null!");
        Assert.notNull(messageService, "messageService must not null!");
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setUserChatService(UserChatService userChatService) {
        this.userChatService = userChatService;
    }
}
