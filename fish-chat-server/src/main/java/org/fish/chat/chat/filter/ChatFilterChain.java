package org.fish.chat.chat.filter;


import org.fish.chat.common.log.LoggerManager;
import org.apache.commons.collections.CollectionUtils;
import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.UserSession;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 责任链
 *
 * @author adre
 */
@Service
public class ChatFilterChain implements ChatFilter, InitializingBean {

    private List<ChatFilter> chain;

    @Autowired
    private ChatFilter persistenceFilter;
    @Autowired
    private ChatFilter syncMessageFilter;

    @Override
    public boolean beforeDeliver(UserSession fromUserSession, Message message) {
        if (CollectionUtils.isNotEmpty(chain)) {
            for (int i = 0; i < chain.size(); i++) {
                try {
                    ChatFilter filter = chain.get(i);
                    if (!filter.beforeDeliver(fromUserSession, message)) {
                        LoggerManager.info("ChatFilterChain" + filter.getClass() + "beforeDeliver return --->false");
                        return false;
                    }
                } catch (Throwable t) {
                    LoggerManager.error("ChatFilterChain.beforeDispatch has some wrong", t);
                }
            }
        }
        return true;
    }

    @Override
    public boolean beforeSendMessage(UserSession toUserSession, Message message) {
        if (CollectionUtils.isNotEmpty(chain)) {
            for (int i = 0; i < chain.size(); i++) {
                try {
                    ChatFilter filter = chain.get(i);
                    if (!filter.beforeSendMessage(toUserSession, message)) {
                        return false;
                    }
                } catch (Throwable t) {
                    LoggerManager.error("ChatFilterChain.beforeDispatch has some wrong", t);
                }
            }
        }
        return true;
    }

    @Override
    public void afterSendMessage(UserSession toUserSession, Message message) {
        if (CollectionUtils.isNotEmpty(chain)) {
            for (int i = 0; i < chain.size(); i++) {
                try {
                    ChatFilter filter = chain.get(i);
                    filter.afterSendMessage(toUserSession, message);
                } catch (Throwable t) {
                    LoggerManager.error("ChatFilterChain.beforeDispatch has some wrong", t);
                }
            }
        }
    }

    @Override
    public void afterDeliver(UserSession fromUserSession, Message message) {
        if (CollectionUtils.isNotEmpty(chain)) {
            for (int i = 0; i < chain.size(); i++) {
                try {
                    ChatFilter filter = chain.get(i);
                    filter.afterDeliver(fromUserSession, message);
                } catch (Throwable t) {
                    LoggerManager.error("ChatFilterChain.beforeDispatch has some wrong", t);
                }
            }
        }
    }

    @Override
    public void onSendMessageNotOnline(long toUserId, Message message) {
        if (CollectionUtils.isNotEmpty(chain)) {
            for (int i = 0; i < chain.size(); i++) {
                try {
                    ChatFilter filter = chain.get(i);
                    filter.onSendMessageNotOnline(toUserId, message);
                } catch (Throwable t) {
                    LoggerManager.error("ChatFilterChain.beforeDispatch has some wrong", t);
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        chain = new ArrayList<>();
        if (persistenceFilter != null) {
            chain.add(persistenceFilter);
        }
        if (syncMessageFilter != null) {
            chain.add(syncMessageFilter);
        }
    }

}