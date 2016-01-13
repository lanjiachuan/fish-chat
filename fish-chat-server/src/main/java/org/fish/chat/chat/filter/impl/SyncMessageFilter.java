/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.filter.impl;

import org.fish.chat.common.log.LoggerManager;
import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.UserSession;
import org.fish.chat.chat.service.UserChatService;
import org.fish.chat.chat.service.UserSessionService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Comments for SyncMessageFilter.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年8月18日 下午7:01:21
 */
public class SyncMessageFilter extends ChatFilterAdapter implements InitializingBean {

    private UserSessionService userSessionService;
    private UserChatService userChatService;

    @Override
    public boolean beforeDeliver(UserSession fromUserSession, Message message) {
        int anotherSessionType = fromUserSession.getType() == UserSession.USER_SESSION_TYPE_CLIENT
                ? UserSession.USER_SESSION_TYPE_WEB : UserSession.USER_SESSION_TYPE_CLIENT;
        UserSession anotherSession = userSessionService.getUserSession(fromUserSession.getUserId(), anotherSessionType);
        if (anotherSession != null) {
            //同步发送消息
            userChatService.sendMessage(anotherSession.getUserId(), message, anotherSession.getType());
        }
        //系统消息同步给本地
        if (message.isSync() && message.getType() == Message.MESSAGE_TYPE_SYSTEM) {
            //同步发送消息
            userChatService.sendMessage(fromUserSession.getUserId(), message, fromUserSession.getType());
            LoggerManager.info("sync message:" + message);
        }

        if (fromUserSession.getType() == UserSession.USER_SESSION_TYPE_CLIENT
                && message.getType() != Message.MESSAGE_TYPE_SYSTEM && message.getClientMsgId() != 0) {
            //同步消息id
            userChatService.syncMessageId(fromUserSession, message.getClientMsgId(), message.getId());
        }

        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userChatService, "userChatService must not null!");
        Assert.notNull(userSessionService, "userSessionService must not null!");
    }

    public void setUserChatService(UserChatService userChatService) {
        this.userChatService = userChatService;
    }

    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

}