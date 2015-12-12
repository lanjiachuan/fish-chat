/**
 * techwolf.cn All rights reserved.
 */
package org.fish.chat.chat.model;


import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Comments for UserSession.java
 *
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月11日 上午11:51:35
 */
public class UserSession implements Serializable {

    private static final long serialVersionUID = 4764222363064128558L;

    public static int USER_SESSION_OFFLINE = -1; // 不在线
    public static int USER_SESSION_STATUS_INIT = 0; // 初始化，等待上传客户端信息
    public static int USER_SESSION_STATUS_NORMAL = 1; // 普通状态
    public static int USER_SESSION_STATUS_WAIT = 2; // 连接断掉，等待重连
    public static int USER_SESSION_STATUS_DIE = 3; // 已发送下线，或者等待重连超时
    public static int USER_SESSION_TYPE_CLIENT = 0;
    public static int USER_SESSION_TYPE_WEB = 1;
    public static int USER_SESSION_CLIENT_STATUS_FRONT = 1;
    public static int USER_SESSION_CLIENT_STATUS_BACK = 2;

    private long userId;
    private long cid;
    private int status;
    private int type;
    private int identity;
    private String ip;
    private Date createTime;
    private long lastHeartBeat;
    private float clientVersion;
    private float protocolVersion;
    private String clientSystem;//手机操作系统
    private String clientSystemVersion;//手机操作系统版本
    private String clientModel;//机型
    private String uniqId;
    private int appId;
    private String platform;
    private String network;
    private String channel;
    private boolean temp;
    private long lastSyncTime;
    private String clientId;
    private int clientStatus = 1;//客户端前台还是后台，1为前台，2为后台
    private long clientStartTime;
    private long clientResumeTime;

    public UserSession() {
        createTime = new Date();
    }

    public UserSession(UserSession userSession) {
        this.userId = userSession.getUserId();
        this.cid = userSession.getCid();
        this.status = userSession.getStatus();
        this.ip = userSession.getIp();
        this.createTime = userSession.getCreateTime();
        this.clientVersion = userSession.getClientVersion();
        this.identity = userSession.getIdentity();
        this.clientId = userSession.getClientId();
        this.lastHeartBeat = userSession.getLastHeartBeat();
        this.type = userSession.getType();
    }

    public UserSession(long userId, long cid, String ip, int identity, String clientId) {
        createTime = new Date();
        this.userId = userId;
        this.cid = cid;
        this.ip = ip;
        this.identity = identity;
        this.clientId = clientId;
        lastHeartBeat = System.currentTimeMillis();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIp() {
        int index = StringUtils.indexOf(ip, ":");
        if (StringUtils.isNotBlank(ip) && StringUtils.startsWith(ip, "/") && index > 0) {
            return StringUtils.substring(ip, 1, index);
        }
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getLastHeartBeat() {
        return lastHeartBeat;
    }

    public void setLastHeartBeat(long lastHeartBeat) {
        this.lastHeartBeat = lastHeartBeat;
    }

    public float getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(float clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getClientSystem() {
        return clientSystem;
    }

    public void setClientSystem(String clientSystem) {
        this.clientSystem = clientSystem;
    }

    public String getClientSystemVersion() {
        return clientSystemVersion;
    }

    public void setClientSystemVersion(String clientSystemVersion) {
        this.clientSystemVersion = clientSystemVersion;
    }

    public String getClientModel() {
        return clientModel;
    }

    public void setClientModel(String clientModel) {
        this.clientModel = clientModel;
    }

    @Override
    public String toString() {
        return "[userId:" + userId + ",identity:" + identity + ",cid:" + cid + ",ip:" + ip
                + ",status:" + status + ",clientId:" + clientId + ",type:" + type + ",clientVersion:" + clientVersion + "]";
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    public long getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(long lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(float protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Map<String, String> getClientInfoMap() {
        Map<String, String> data = new HashMap<String, String>();
        // TODO
        return data;
    }

    public String getUniqId() {
        return uniqId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setUniqId(String uniqId) {
        this.uniqId = uniqId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public int getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(int clientStatus) {
        this.clientStatus = clientStatus;
    }

    public long getClientStartTime() {
        return clientStartTime;
    }

    public long getClientResumeTime() {
        return clientResumeTime;
    }

    public void setClientStartTime(long clientStartTime) {
        this.clientStartTime = clientStartTime;
    }

    public void setClientResumeTime(long clientResumeTime) {
        this.clientResumeTime = clientResumeTime;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

}