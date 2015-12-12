/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.filter.impl;


import org.fish.chat.chat.filter.ChatFilter;
import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.UserSession;

/**
 * Comments for ChatFilterAdapter.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月24日 下午1:23:45
 */
public class ChatFilterAdapter implements ChatFilter {

    @Override
    public boolean beforeDeliver(UserSession fromUserSession, Message message) {
        return true;
    }

    @Override
    public void afterDeliver(UserSession fromUserSession, Message message) {
    }

    @Override
    public boolean beforeSendMessage(UserSession toUserSession, Message message) {
        return true;
    }

    @Override
    public void afterSendMessage(UserSession toUserSession, Message message) {

    }

    @Override
    public void onSendMessageNotOnline(long toUserId, Message message) {

    }
}