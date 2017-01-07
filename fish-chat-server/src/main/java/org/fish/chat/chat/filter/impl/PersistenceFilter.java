package org.fish.chat.chat.filter.impl;


import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.UserSession;
import org.fish.chat.chat.service.MessageService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * Comments for PersistenceFilter.java
 *
 */
public class PersistenceFilter extends ChatFilterAdapter implements InitializingBean {


    @Override
    public boolean beforeDeliver(UserSession fromUserSession, Message message) {
        if (!message.isOffline()) {
            // TODO
        }
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

}
