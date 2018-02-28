package org.fish.chat.chat.filter.impl;


import org.fish.chat.chat.filter.ChatFilter;
import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.UserSession;

/**
 * 适配器
 *
 * @author adre
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