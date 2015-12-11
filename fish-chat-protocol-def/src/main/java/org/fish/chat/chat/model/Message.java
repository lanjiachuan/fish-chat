/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.model;

import cn.techwolf.common.log.LoggerManager;
import com.google.gson.annotations.Expose;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Comments for Message.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月14日 下午7:22:08
 */
public abstract class Message implements Serializable {

    private static final long serialVersionUID = -3714721708175778717L;
    public static final int MESSAGE_CHAT_TYPE_NIU = 1;//牛人发的消息
    public static final int MESSAGE_CHAT_TYPE_BOSS = 2;//BOSS发的消息
    public static final int MESSAGE_CHAT_TYPE_C2C = 3;//牛人对牛人的消息
    public static final int MESSAGE_TYPE_SINGLE = 1;
    public static final int MESSAGE_TYPE_GROUP = 2;
    public static final int MESSAGE_TYPE_SYSTEM = 3;
    public static final int MESSAGE_MEDIA_TYPE_TEXT = 1;
    public static final int MESSAGE_MEDIA_TYPE_SOUND = 2;
    public static final int MESSAGE_MEDIA_TYPE_IMAGE = 3;
    public static final int MESSAGE_MEDIA_TYPE_ACTION = 4;
    public static final int MESSAGE_MEDIA_TYPE_ARTICLE = 5;
    public static final int MESSAGE_MEDIA_TYPE_NOTIFY = 6;
    public static final int MESSAGE_MEDIA_TYPE_DIALOG = 7;
    public static final int MESSAGE_MEDIA_TYPE_JD = 8;
    public static final int MESSAGE_MEDIA_TYPE_RESUME = 9;
    public static final int MESSAGE_TEMPLATE_NORMAL = 1;
    public static final int MESSAGE_TEMPLATE_CENTER = 2;
    public static final int MESSAGE_TEMPLATE_CENTER_GRAY = 3;
    public static final int MESSAGE_TEMPLATE_SKILL = 4;
    public static final int MESSAGE_TEMPLATE_MARK = 5;//解析标签
    public static final int MESSAGE_TEMPLATE_CENTER_GRAY_NEW = 6;
    public static final int MESSAGE_TEMPLATE_CENTER_GRAY_NEW_2 = 7;// 两行
    protected static final String JSON_PROPERTY_SINCE_VERSION = "since_version";
    public static final String JSON_KEY_CLIENT_INFO = "client_info";

    @Expose
    protected ChatUser from;
    @Expose
    protected ChatUser to;
    @Expose
    protected int templateId = 1;
    @Expose
    protected long id;
    @Expose
    protected long clientMsgId;
    @Expose
    protected Date createTime = new Date();
    @Expose
    protected int type = MESSAGE_TYPE_SINGLE;
    protected String headTitle;
    protected boolean persistence = true;//是否持久化
    protected boolean entrySaved = false;//该标记主要给小秘书群发使用，小秘书群发先保存实体
    protected boolean offline = false;
    protected boolean received = false;
    protected long taskId;//辅助信息
    protected boolean saveInbox = true;
    protected boolean saveOutbox = true;
    protected boolean sync = false;//是否将系统消息同步到发送方
    protected boolean convert = false;//转化消息
    protected float sinceVersion = 0;//消息最低版本
    protected Map<String, String> clientInfoMap;

    public abstract int getMediaType();

    public abstract String getMediaBody();

    public abstract boolean setMediaBody(String json);

    public abstract String getPushMessage();

    public abstract String getPushUrl();

    public ChatUser getFrom() {
        return from;
    }

    public void setFrom(ChatUser from) {
        this.from = from;
    }

    public void setFrom(long userId) {
        setFrom(new ChatUser(userId));
    }

    public void setFrom(long userId, int identity) {
        setFrom(new ChatUser(userId, identity));
    }

    public ChatUser getTo() {
        return to;
    }

    public void setTo(ChatUser to) {
        this.to = to;
    }

    public void setTo(long userId) {
        setTo(new ChatUser(userId));
    }

    public void setTo(long userId, int identity) {
        setTo(new ChatUser(userId, identity));
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isPersistence() {
        return persistence;
    }

    public void setPersistence(boolean persistence) {
        this.persistence = persistence;
    }

    @Override
    public String toString() {
        return "[messageId=" + id + ", type=" + type + ",mediaType=" + getMediaType()
                + ",templateId=" + templateId + ",persistence=" + persistence + "]";
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public long getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        this.clientMsgId = clientMsgId;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public boolean isSaveInbox() {
        return saveInbox;
    }

    public void setSaveInbox(boolean saveInbox) {
        this.saveInbox = saveInbox;
    }

    public boolean isSaveOutbox() {
        return saveOutbox;
    }

    public void setSaveOutbox(boolean saveOutbox) {
        this.saveOutbox = saveOutbox;
    }

    public boolean isConvert() {
        return convert;
    }

    public void setConvert(boolean convert) {
        this.convert = convert;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getHeadTitle() {
        return headTitle;
    }

    public void setHeadTitle(String headTitle) {
        this.headTitle = headTitle;
    }

    public Map<String, String> getClientInfoMap() {
        return clientInfoMap;
    }

    public void setClientInfoMap(Map<String, String> clientInfoMap) {
        this.clientInfoMap = clientInfoMap;
    }


    public float getSinceVersion() {
        return sinceVersion;
    }

    public void setSinceVersion(float sinceVersion) {
        this.sinceVersion = sinceVersion;
    }

    public void writeClientInfoJson(JSONObject jsonObject) {
        if (clientInfoMap != null && clientInfoMap.size() > 0) {
            jsonObject.put(JSON_KEY_CLIENT_INFO, JSONObject.fromObject(clientInfoMap));
        }
    }

    public String getClientInfoJson() {
        if(clientInfoMap != null && clientInfoMap.size() > 0) {
            JSONObject jsonObject = JSONObject.fromObject(clientInfoMap);
            return jsonObject.toString();
        }
        return StringUtils.EMPTY;
    }


    public void readClientInfoJson(JSONObject jsonObject) {
        if (jsonObject != null && jsonObject.containsKey(JSON_KEY_CLIENT_INFO)) {
            Map<String, String> clientInfoMap = new HashMap<String, String>();
            JSONObject clientInfo = jsonObject.optJSONObject(JSON_KEY_CLIENT_INFO);
            Iterator iterator = clientInfo.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = clientInfo.optString(key);
                clientInfoMap.put(key, value);
            }
            this.clientInfoMap = clientInfoMap;
        }
    }

    public Message clone() {
        try {
            return (Message) super.clone();
        } catch (CloneNotSupportedException e) {
            LoggerManager.error("Message.clone error", e);
        }
        return null;
    }

    public boolean isEntrySaved() {
        return entrySaved;
    }

    public void setEntrySaved(boolean entrySaved) {
        this.entrySaved = entrySaved;
    }
}
