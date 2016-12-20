package org.fish.chat.chat.model;

import java.util.Date;

/**
 * Comments for MessageChatStatistics.java
 *
 */
public class MessageChatStatistics {

    private long bossUid;

    private String bossName;

    private long niurenUid;

    private String niurenName;

    private Date lastChatDate;

    private int bossSendCount;

    private int niurenSendCount;

    private String lastMessage;

    /**
     * @return the bossUid
     */
    public long getBossUid() {
        return bossUid;
    }

    /**
     * @param bossUid the bossUid to set
     */
    public void setBossUid(long bossUid) {
        this.bossUid = bossUid;
    }

    /**
     * @return the niurenUid
     */
    public long getNiurenUid() {
        return niurenUid;
    }

    /**
     * @param niurenUid the niurenUid to set
     */
    public void setNiurenUid(long niurenUid) {
        this.niurenUid = niurenUid;
    }

    /**
     * @return the lastChatDate
     */
    public Date getLastChatDate() {
        return lastChatDate;
    }

    /**
     * @param lastChatDate the lastChatDate to set
     */
    public void setLastChatDate(Date lastChatDate) {
        this.lastChatDate = lastChatDate;
    }

    /**
     * @return the bossSendCount
     */
    public int getBossSendCount() {
        return bossSendCount;
    }

    /**
     * @param bossSendCount the bossSendCount to set
     */
    public void setBossSendCount(int bossSendCount) {
        this.bossSendCount = bossSendCount;
    }

    /**
     * @return the niurenSendCount
     */
    public int getNiurenSendCount() {
        return niurenSendCount;
    }

    /**
     * @param niurenSendCount the niurenSendCount to set
     */
    public void setNiurenSendCount(int niurenSendCount) {
        this.niurenSendCount = niurenSendCount;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MessageChatStatistics) {
            MessageChatStatistics mcs = (MessageChatStatistics) obj;
            if (mcs.getBossUid() == this.getBossUid() && mcs.getNiurenUid() == this.getNiurenUid()) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getBossUid() + "-" + this.getNiurenUid();
    }

    /**
     * @return the lastMessage
     */
    public String getLastMessage() {
        return lastMessage;
    }

    /**
     * @param lastMessage the lastMessage to set
     */
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    /**
     * @return the bossName
     */
    public String getBossName() {
        return bossName;
    }

    /**
     * @param bossName the bossName to set
     */
    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    /**
     * @return the niurenName
     */
    public String getNiurenName() {
        return niurenName;
    }

    /**
     * @param niurenName the niurenName to set
     */
    public void setNiurenName(String niurenName) {
        this.niurenName = niurenName;
    }

}
