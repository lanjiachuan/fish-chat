/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.handler;


import org.fish.chat.chat.ChatProtocol;

import java.util.Map;

/**
 * Comments for IqDispatchService.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年8月19日 上午10:31:23
 */
public interface IqHandler {

    public ChatProtocol.FishChatProtocol handle(long userId, int identity, long qid, String query,
                                                    Map<String, String> params);
}
