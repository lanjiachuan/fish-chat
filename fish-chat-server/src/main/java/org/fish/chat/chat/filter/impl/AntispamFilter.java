package org.fish.chat.chat.filter.impl;


import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.TextMessage;
import org.fish.chat.chat.model.UserSession;
import org.fish.chat.common.constants.ChatConstant;

/**
 * Comments for AntispamFilter.java
 *
 */
public class AntispamFilter extends ChatFilterAdapter {

    @Override
    public boolean beforeDeliver(UserSession fromUserSession, Message message) {

        if (message instanceof TextMessage && message.getFrom().getUid() != ChatConstant.SYSTEM_USER_ID && message.getType() == Message.MESSAGE_TYPE_SINGLE) {


        }

        return true;
    }

}
