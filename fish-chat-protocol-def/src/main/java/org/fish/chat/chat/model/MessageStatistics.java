/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.model;

import java.util.Date;

/**
 * Comments for MessageStatistics.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年6月26日 下午5:19:41
 */
public class MessageStatistics {

    long fromId;

    long toId;

    int chatType;

    int num;

    Date time;

    String mediaBody;

    /**
     * @return the fromId
     */
    public long getFromId() {
        return fromId;
    }

    /**
     * @param fromId the fromId to set
     */
    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    /**
     * @return the toId
     */
    public long getToId() {
        return toId;
    }

    /**
     * @param toId the toId to set
     */
    public void setToId(long toId) {
        this.toId = toId;
    }

    /**
     * @return the chatType
     */
    public int getChatType() {
        return chatType;
    }

    /**
     * @param chatType the chatType to set
     */
    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    /**
     * @return the num
     */
    public int getNum() {
        return num;
    }

    /**
     * @param num the num to set
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * @return the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * @param mediaBody the mediaBody to set
     */
    public void setMediaBody(String mediaBody) {
        this.mediaBody = mediaBody;
    }

    /**
     * @return the mediaBody
     */
    public String getMediaBody() {
        return mediaBody;
    }

}
