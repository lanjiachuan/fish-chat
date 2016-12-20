package org.fish.chat.chat.model;

/**
 * Comments for TaskReceiveCount.java
 *
 */
public class TaskReceiveCount {

    private long taskId;

    private int sendCount;

    private int receiveCount;

    private int readCount;

    /**
     * @return the taskId
     */
    public long getTaskId() {
        return taskId;
    }

    /**
     * @return the receiveCount
     */
    public int getReceiveCount() {
        return receiveCount;
    }

    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    /**
     * @param receiveCount the receiveCount to set
     */
    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
}
