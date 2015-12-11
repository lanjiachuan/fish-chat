/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.model;

import java.util.Date;

/**
 * Comments for MessageSummary.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月18日 下午8:08:37
 */
public class MessageSummary {

    private long id;
    private long ownerId;
    private int ownerIdentity;
    private long friendId;
    private int friendIdentity;
    private long fromId;
    private int fromIdentity;
    private long toId;
    private int toIdentity;
    private long messageId;
    private int type;
    private int mediaType;
    private int status;
    private int chatType;
    private long taskId;
    private Date createTime;

    public static final int STATUS_OUT_BOX = 1;
    public static final int STATUS_IN_BOX = 0;

    public static final int CHAT_TYPE_B2C = 1;
    public static final int CHAT_TYPE_C2B = 2;
    public static final int CHAT_TYPE_C2C = 3;

    public MessageSummary() {
    }

    public MessageSummary(Message message) {
        this.fromId = message.getFrom().getUid();
        this.fromIdentity = message.getFrom().getIdentity();
        this.toId = message.getTo().getUid();
        this.toIdentity = message.getTo().getIdentity();
        this.messageId = message.getId();
        this.type = message.getType();
        this.mediaType = message.getMediaType();
        this.createTime = message.getCreateTime();
        this.taskId = message.getTaskId();
    }

    public static MessageSummary getOutboxMessageSummary(Message message) {
        MessageSummary summary = new MessageSummary(message);
        summary.setOwnerId(summary.getFromId());
        summary.setOwnerIdentity(summary.getFromIdentity());
        summary.setFriendId(summary.getToId());
        summary.setFriendIdentity(summary.getToIdentity());
        summary.setStatus(STATUS_OUT_BOX);
        return summary;
    }

    public static MessageSummary getInboxMessageSummary(Message message) {
        MessageSummary summary = new MessageSummary(message);
        summary.setOwnerId(summary.getToId());
        summary.setOwnerIdentity(summary.getToIdentity());
        summary.setFriendId(summary.getFromId());
        summary.setFriendIdentity(summary.getFromIdentity());
        summary.setStatus(STATUS_IN_BOX);
        return summary;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public int getOwnerIdentity() {
        return ownerIdentity;
    }

    public void setOwnerIdentity(int ownerIdentity) {
        this.ownerIdentity = ownerIdentity;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public int getFriendIdentity() {
        return friendIdentity;
    }

    public void setFriendIdentity(int friendIdentity) {
        this.friendIdentity = friendIdentity;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public int getFromIdentity() {
        return fromIdentity;
    }

    public void setFromIdentity(int fromIdentity) {
        this.fromIdentity = fromIdentity;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public int getToIdentity() {
        return toIdentity;
    }

    public void setToIdentity(int toIdentity) {
        this.toIdentity = toIdentity;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
