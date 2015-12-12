/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.filter;


import cn.techwolf.common.log.LoggerManager;
import org.apache.commons.collections.CollectionUtils;
import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.UserSession;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Comments for ChatFilterChain.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月24日 下午12:34:37
 */
public class ChatFilterChain implements ChatFilter, InitializingBean {

    private List<ChatFilter> chain;

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
        Assert.notNull(chain, "filter chain must not null!");
    }

    public void setChain(List<ChatFilter> chain) {
        this.chain = chain;
    }

}