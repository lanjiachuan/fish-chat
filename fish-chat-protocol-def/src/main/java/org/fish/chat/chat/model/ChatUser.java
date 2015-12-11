/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.model;


import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.fish.chat.common.utils.IdSecurityUtils;

import java.io.Serializable;

/**
 * Comments for ChatUser.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月18日 下午5:40:16
 */
public class ChatUser implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 28354246548422973L;

    private static final String JSON_PROPERTIES_UID = "uid";
    private static final String JSON_PROPERTIES_NAME = "name";
    private static final String JSON_PROPERTIES_AVATAR = "avatar";
    private static final String JSON_PROPERTIES_HEAD_IMG = "head_img";
    private static final String JSON_PROPERTIES_IDENTITY = "identity";
    private static final String JSON_PROPERTIES_VERIFY = "verify";
    private static final String JSON_PROPERTIES_GENDER = "gender";
    private static final String JSON_PROPERTIES_BOSS = "boss";
    private static final String JSON_PROPERTIES_DISTANCE = "distance";
    private static final String JSON_PROPERTIES_DISTANCE_DESC = "distanceDesc";

    private long uid;
    private String name = "";
    private String avatar = "";
    private int identity;
    private int age;
    private String content = "";
    private long distance;
    private String distanceDesc = "";
    private ChatUserBoss chatUserBoss;
    private int gender;

    public ChatUser(long uid) {
        this.uid = uid;
    }

    public ChatUser(long uid, int identity) {
        this.uid = uid;
        this.identity = identity;
    }

    public ChatUser(long uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public long getUid() {
        return uid;
    }

    public String getEncryptId() {
        return IdSecurityUtils.encryptId(getUid());
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_PROPERTIES_UID, uid);
        jsonObject.put(JSON_PROPERTIES_NAME, StringUtils.defaultString(name));
        jsonObject.put(JSON_PROPERTIES_AVATAR, StringUtils.defaultString(avatar));
        jsonObject.put(JSON_PROPERTIES_IDENTITY, identity);
        jsonObject.put(JSON_PROPERTIES_GENDER, gender);
        jsonObject.put(JSON_PROPERTIES_BOSS, chatUserBoss.toJsonObject());
        jsonObject.put(JSON_PROPERTIES_DISTANCE, distance);
        jsonObject.put(JSON_PROPERTIES_DISTANCE_DESC, distanceDesc);
        return jsonObject;
    }

    public static ChatUser fromJson(JSONObject jsonObject) {
        if (jsonObject != null) {
            ChatUser chatUser = new ChatUser(0);
            chatUser.setUid(jsonObject.optLong(JSON_PROPERTIES_UID));
            chatUser.setName(jsonObject.optString(JSON_PROPERTIES_NAME));
            chatUser.setAvatar(jsonObject.optString(JSON_PROPERTIES_AVATAR));
            chatUser.setIdentity(jsonObject.optInt(JSON_PROPERTIES_IDENTITY));
            chatUser.setGender(jsonObject.optInt(JSON_PROPERTIES_GENDER));
            JSONObject bossObject = jsonObject.optJSONObject(JSON_PROPERTIES_BOSS);
            if (bossObject != null) {
                ChatUserBoss boss = ChatUserBoss.fromJson(bossObject);
                chatUser.setChatUserBoss(boss);
            }
            chatUser.setDistance(jsonObject.optLong(JSON_PROPERTIES_DISTANCE));
            chatUser.setDistanceDesc(jsonObject.optString(JSON_PROPERTIES_DISTANCE_DESC));
            return chatUser;
        }
        return null;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String getDistanceDesc() {
        return distanceDesc;
    }

    public void setDistanceDesc(String distanceDesc) {
        this.distanceDesc = distanceDesc;
    }

    public ChatUserBoss getChatUserBoss() {
        return chatUserBoss;
    }

    public void setChatUserBoss(ChatUserBoss chatUserBoss) {
        this.chatUserBoss = chatUserBoss;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
