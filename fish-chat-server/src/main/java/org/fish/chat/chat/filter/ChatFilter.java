/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.filter;


import org.fish.chat.chat.model.Message;
import org.fish.chat.chat.model.UserSession;

/**
 * Comments for CharFilter.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月24日 下午12:34:48
 */
public interface ChatFilter {

    /**
     * 消息投递前
     *
     * @return
     */
    public boolean beforeDeliver(UserSession fromUserSession, Message message);

    /**
     * 消息投递后
     *
     * @return
     */
    public void afterDeliver(UserSession fromUserSession, Message message);

    /**
     * 消息发送前
     *
     * @return
     */
    public boolean beforeSendMessage(UserSession toUserSession, Message message);

    /**
     * 消息发送后
     *
     * @param toUserSession
     * @param message
     */
    public void afterSendMessage(UserSession toUserSession, Message message);

    /**
     * 用户不在线
     *
     * @param message
     */
    public void onSendMessageNotOnline(long toUserId, Message message);

}
