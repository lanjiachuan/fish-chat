package org.fish.chat.common.constants;

/**
 * Comments for ChatConstant.java
 *
 */
public interface ChatConstant {

    long SYSTEM_USER_ID = 1000;// 小秘书
    long NOTIFY_USER_ID = 999;
    long NOTIFY_COUNT_USER_ID = 998;
    long KUAIBAO_USER_ID = 997;
    long SYSTEM_PUSH_USER_ID = 899; // 推送小秘书

    String DEFAULT_TOPIC_NAME = "chat";
    //心跳间隔时间
    int HEART_BEAT_INTERVAL = 60;
    int PROTOCOL_TYPE_MESSAGE = 1;
    int PROTOCOL_TYPE_PRESENCE = 2;
    int PROTOCOL_TYPE_IQ = 3;
    int PROTOCOL_TYPE_IQ_RESPONSE = 4;
    int PROTOCOL_TYPE_IQ_MESSAGE_SYNC = 5;
    int PROTOCOL_TYPE_MESSAGE_READ = 6;
    int QOS_TYPE_AT_MOST_ONCE = 0;
    int QOS_TYPE_AT_LEAST_ONCE = 1;
    int QOS_TYPE_EXACTLY_ONCE = 2;

    int MAX_SYSTEM_USER_ID = 10000;

}