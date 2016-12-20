
package org.fish.chat.chat.model;

import java.util.Date;

/**
 * Comments for MessageStatistics.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年6月26日 下午5:19:41
 */
public class UserMessageStatistics {

    long userId;

    int messageCount;

    Date lastTime;

    /**
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * @return the messageCount
     */
    public int getMessageCount() {
        return messageCount;
    }

    /**
     * @param messageCount the messageCount to set
     */
    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    /**
     * @return the lastTime
     */
    public Date getLastTime() {
        return lastTime;
    }

    /**
     * @param lastTime the lastTime to set
     */
    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

}
