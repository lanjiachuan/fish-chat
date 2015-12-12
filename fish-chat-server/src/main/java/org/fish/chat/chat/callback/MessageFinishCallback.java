/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.callback;


import org.fish.chat.chat.model.Message;

import java.util.List;

/**
 * Comments for MessageFinishCallback.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月21日 下午4:37:50
 */
public interface MessageFinishCallback {

    /**
     * 消息
     * 
     * @param userId
     * @param message
     */
    public void messageFinish(long userId, List<Message> message);
}
