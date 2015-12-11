/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.service;


import org.fish.chat.chat.model.Message;

/**
 * Comments for DeliverService.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年7月10日 下午3:48:58
 */
public interface DeliverService {

    public void deliverMessage(long toUserId, Message message);

}