///**
// * techwolf.cn All rights reserved.
// */
//package org.fish.chat.chat.service.impl;
//
//
//
//import cn.techwolf.common.log.LoggerManager;
//import cn.techwolf.common.utils.RequestIdUtil;
//import com.google.common.cache.*;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.math.NumberUtils;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.util.Assert;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.*;
//
///**
// * Comments for UserSessionServiceSyncImpl.java
// *
// * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
// * @createTime 2014年7月17日 上午9:51:25
// */
//public class UserSessionServiceSyncImpl implements UserSessionService, UserSessionListener, InitializingBean
//        , RemovalListener<Long, UserSession>, OnlineStatusService {
//
//    private static final int ANDROID_APPID = 1001;
//    private static final int IOS_APPID = 1002;
//    private static final int WAIT_RECONNECT_EXPIRE_TIME = ChatConstant.HEART_BEAT_INTERVAL * 3;
//    private ICacheService cacheService;
//    private Map<Long, UserSession> userSessionMap = new ConcurrentHashMap<Long, UserSession>();
//    private Map<Long, UserSession> userWebSessionMap = new ConcurrentHashMap<Long, UserSession>();
//
//    private List<UserSessionListener> listeners;
//    private final Cache<Long, UserSession> cache = CacheBuilder.newBuilder()
//            .expireAfterWrite(WAIT_RECONNECT_EXPIRE_TIME, TimeUnit.SECONDS).removalListener(this).build();
//
//    private static final ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(5);
//    private ExecutorService executorService = new ThreadPoolExecutor(2, 8, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10000));
//
//    @Override
//    public UserSession connect(long userId, long cid, String ip, int identity, String clientId, int type, float protocolVersion) {
//        RequestIdUtil.setRequestId(userId);
//        UserSession userSession = getUserSession(userId, type);
//        if (userSession == null) {
//            userSession = new UserSession(userId, cid, ip, identity, clientId);
//            userSession.setType(type);
//            userSession.setProtocolVersion(protocolVersion);
//            userSession.setStatus(UserSession.USER_SESSION_STATUS_NORMAL);
//            onSessionCreate(userSession);
//            LoggerManager.info("new user connect " + userSession);
//        } else {
//            UserSession oldSession = new UserSession(userSession);
//
//            LoggerManager.info(userSession + " reconnect");
//            userSession.setCid(cid);
//            userSession.setIp(ip);
//            userSession.setIdentity(identity);
//            userSession.setClientId(clientId);
//            userSession.setLastHeartBeat(System.currentTimeMillis());
//            userSession.setClientModel("");
//            userSession.setClientSystem("");
//            userSession.setClientSystemVersion("");
//            userSession.setClientVersion(0);
//            userSession.setProtocolVersion(protocolVersion);
//            //已登录重新连接
//            userSession.setStatus(UserSession.USER_SESSION_STATUS_NORMAL);
//            LoggerManager.info("after reconnect " + userSession);
//            if (oldSession.getStatus() == UserSession.USER_SESSION_STATUS_NORMAL) {
//                onSessionOwnerChange(oldSession, userSession);
//            }
//            onSessionReconnect(userSession);
//        }
//        saveUserSession(userSession);
//        return userSession;
//    }
//
//    @Override
//    public boolean disconnect(long userId, long cid, int type) {
//        RequestIdUtil.setRequestId(userId);
//        UserSession userSession = getUserSession(userId, type);
//        if (userSession != null && userSession.getCid() == cid) {
//            userSession.setStatus(UserSession.USER_SESSION_STATUS_WAIT);
//            LoggerManager.info(userSession + " was disconnected , change status to wait Reconnect");
//            saveUserSession(userSession);
//            onSessionDisconnect(userSession);
//        } else {
//            LoggerManager.warn("UserSession.disconnect fail session =" + userSession + ",userId=" + userId + ", cid = " + cid);
//        }
//        return false;
//    }
//
//    @Override
//    public void onSessionDestroy(final UserSession userSession) {
//        try {
//            executorService.submit(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (listeners != null && listeners.size() > 0) {
//                        for (UserSessionListener listener : listeners) {
//                            try {
//                                listener.onSessionDestroy(userSession);
//                            } catch (Exception e) {
//                                LoggerManager.error("", e);
//                            }
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//            LoggerManager.error("", e);
//        }
//    }
//
//    @Override
//    public void onSessionCreate(final UserSession userSession) {
//        try {
//            executorService.submit(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (listeners != null && listeners.size() > 0) {
//                        try {
//                            //睡10ms 保证客户端已经收到connack
//                            Thread.sleep(10);
//                        } catch (InterruptedException e1) {
//                        }
//                        for (UserSessionListener listener : listeners) {
//                            try {
//                                listener.onSessionCreate(userSession);
//                            } catch (Exception e) {
//                                LoggerManager.error("", e);
//                            }
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//            LoggerManager.error("", e);
//        }
//
//    }
//
//    @Override
//    public void onSessionOwnerChange(final UserSession oldUserSession, final UserSession newUserSession) {
//        try {
//            executorService.submit(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (listeners != null && listeners.size() > 0) {
//
//                        for (UserSessionListener listener : listeners) {
//                            try {
//                                listener.onSessionOwnerChange(oldUserSession, newUserSession);
//                            } catch (Exception e) {
//                                LoggerManager.error("", e);
//                            }
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//            LoggerManager.error("", e);
//        }
//    }
//
//    @Override
//    public void onSessionReconnect(final UserSession userSession) {
//        try {
//            cache.invalidate(userSession.getUserId());
//            executorService.submit(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (listeners != null && listeners.size() > 0) {
//
//                        for (UserSessionListener listener : listeners) {
//                            try {
//                                listener.onSessionReconnect(userSession);
//                            } catch (Exception e) {
//                                LoggerManager.error("", e);
//                            }
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//            LoggerManager.error("", e);
//        }
//    }
//
//    @Override
//    public void onSessionDisconnect(final UserSession userSession) {
//        try {
//            cache.put(userSession.getUserId(), userSession);
//            executorService.submit(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (listeners != null && listeners.size() > 0) {
//
//                        for (UserSessionListener listener : listeners) {
//                            try {
//                                listener.onSessionDisconnect(userSession);
//                            } catch (Exception e) {
//                                LoggerManager.error("", e);
//                            }
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//            LoggerManager.error("", e);
//        }
//    }
//
//    @Override
//    public void onRemoval(RemovalNotification<Long, UserSession> notification) {
//        if (notification.getCause() == RemovalCause.EXPIRED) {
//            destroyUserSession(notification.getKey(), notification.getValue());
//        }
//    }
//
//    private void saveUserSession(UserSession userSession) {
//        if (userSession.getType() == UserSession.USER_SESSION_TYPE_CLIENT) {
//            userSessionMap.put(userSession.getUserId(), userSession);
//            userSession.setLastSyncTime(System.currentTimeMillis());
//            cacheService.set(getKey(userSession.getUserId()), userSession, UserSession.class, ChatConstant.HEART_BEAT_INTERVAL * 3);
//            cacheService.set(getOnlineKey(userSession.getUserId(), userSession.getIdentity()), "", ChatConstant.HEART_BEAT_INTERVAL * 3);
//        } else if (userSession.getType() == UserSession.USER_SESSION_TYPE_WEB) {
//            userWebSessionMap.put(userSession.getUserId(), userSession);
//            userSession.setLastSyncTime(System.currentTimeMillis());
//            cacheService.set(getWebKey(userSession.getUserId()), userSession, UserSession.class, ChatConstant.HEART_BEAT_INTERVAL * 3);
//        }
//    }
//
//    private void deleteUserSession(long userId, int type) {
//        if (type == UserSession.USER_SESSION_TYPE_CLIENT) {
//            UserSession userSession = userSessionMap.remove(userId);
//            cacheService.del(getKey(userId));
//            if (userSession != null) {
//                cacheService.del(getOnlineKey(userSession.getUserId(), userSession.getIdentity()));
//            }
//        } else if (type == UserSession.USER_SESSION_TYPE_WEB) {
//            userWebSessionMap.remove(userId);
//            cacheService.del(getWebKey(userId));
//        }
//    }
//
//    private void checkSession() {
//        LoggerManager.info("====== checkSession start ======");
//        long startTime = System.currentTimeMillis();
//
//        LoggerManager.info("before clean current client session size = " + userSessionMap.size());
//        LoggerManager.info("before clean current web session size = " + userWebSessionMap.size());
//
//        List<Map<Long, UserSession>> userSessionMapList = new ArrayList<Map<Long, UserSession>>();
//        userSessionMapList.add(userSessionMap);
//        userSessionMapList.add(userWebSessionMap);
//
//        for (Map<Long, UserSession> tempUserSessionMap : userSessionMapList) {
//            int geekCount = 0, bossCount = 0, cleanCount = 0, cleanBossCount = 0, cleanGeekCount = 0;
//
//            int androidUserCount = 0, androidForegroundCount = 0, androidBackgroundCount = 0, androidWaitReconnectCount = 0, androidGeekCount = 0, androidBossCount = 0;
//
//            int iosUserCount = 0, iosWaitReconnectCount = 0, iosGeekCount = 0, iosBossCount = 0;
//
//            int otherUserCount = 0, otherWaitReconnectCount = 0;
//
//
//            Map<String, Integer> networkCountMap = new HashMap<String, Integer>();
////            Map<String, Integer> modelCountMap = new HashMap<String, Integer>();
//            Map<String, Integer> versionCountMap = new HashMap<String, Integer>();
//            Map<String, Integer> channelCountMap = new HashMap<String, Integer>();
//            Map<String, Integer> systemCountMap = new HashMap<String, Integer>();
//
//
//            for (UserSession userSession : tempUserSessionMap.values()) {
//                long passedTime = System.currentTimeMillis() - userSession.getLastHeartBeat();
//                if (passedTime > ChatConstant.HEART_BEAT_INTERVAL * 6000) {
//                    LoggerManager.warn("userId=" + userSession.getUserId() + " lastHeartBeat= "
//                            + userSession.getLastHeartBeat() + "(" + passedTime + "ms) need to clean,");
//                    //只清理本地数据,可能为服务器down掉导致用户hash到另外一台机器去,远程数据通过失效时间清理
//                    UserSession removeUserSession = tempUserSessionMap.remove(userSession.getUserId());
//                    if (removeUserSession != null) {
//                        cleanCount++;
//                        if (removeUserSession.getIdentity() == User.IDENTITY_GEEK) {
//                            cleanGeekCount++;
//                        } else {
//                            cleanBossCount++;
//                        }
//                    }
//
//                    continue;
//                }
//                //检查是否需要同步
//                if (System.currentTimeMillis() - userSession.getLastSyncTime() > ChatConstant.HEART_BEAT_INTERVAL * 2000) {
//                    syncSession(userSession);
//                }
//                //统计用户信息
//
//                if (userSession.getIdentity() == User.IDENTITY_GEEK) {
//                    geekCount++;
//                } else {
//                    bossCount++;
//                }
//
//                switch (userSession.getAppId()) {
//                    case ANDROID_APPID:
//                        androidUserCount++;
//                        if (userSession.getClientStatus() == UserSession.USER_SESSION_CLIENT_STATUS_FRONT) {
//                            androidForegroundCount++;
//                        } else if (userSession.getClientStatus() == UserSession.USER_SESSION_CLIENT_STATUS_BACK) {
//                            androidBackgroundCount++;
//                        }
//
//                        if (userSession.getStatus() == UserSession.USER_SESSION_STATUS_WAIT) {
//                            androidWaitReconnectCount++;
//                        }
//
//                        if (userSession.getIdentity() == User.IDENTITY_GEEK) {
//                            androidGeekCount++;
//                        } else {
//                            androidBossCount++;
//                        }
//                        break;
//                    case IOS_APPID:
//                        iosUserCount++;
//                        if (userSession.getStatus() == UserSession.USER_SESSION_STATUS_WAIT) {
//                            iosWaitReconnectCount++;
//                        }
//
//                        if (userSession.getIdentity() == User.IDENTITY_GEEK) {
//                            iosGeekCount++;
//                        } else {
//                            iosBossCount++;
//                        }
//                        break;
//                    default:
//                        if (userSession.getStatus() == UserSession.USER_SESSION_STATUS_WAIT) {
//                            otherWaitReconnectCount++;
//                        }
//                        otherUserCount++;
//                }
//                //统计网络信息
//                addMapCount(networkCountMap, StringUtils.defaultString(userSession.getNetwork(), "unknown"));
//                //统计机型信息
////                addMapCount(modelCountMap, StringUtils.defaultString(userSession.getClientModel(), "unknown"));
//                //统计版本信息
//                addMapCount(versionCountMap, String.valueOf(userSession.getClientVersion()));
//                //渠道统计信息
//                addMapCount(channelCountMap, String.valueOf(userSession.getChannel()));
//                //系统统计信息
//                addMapCount(systemCountMap, StringUtils.defaultIfEmpty(userSession.getClientSystem(), "unknownSystem") + "(" + StringUtils.defaultIfEmpty(userSession.getClientSystemVersion(), "unkonwSystemVersion") + ")");
//
//            }
//
//            LoggerManager.info(String.format("geekCount = %s, bossCount= %s", geekCount, bossCount));
//            LoggerManager.info(String.format("androidGeekCount = %s, iosGeekCount= %s", androidGeekCount, iosGeekCount));
//            LoggerManager.info(String.format("androidBossCount = %s, iosBossCount= %s", androidBossCount, iosBossCount));
//            LoggerManager.info(String.format("cleanCount = %s, cleanBossCount= %s, cleanGeekCount = %s", cleanCount, cleanBossCount, cleanGeekCount));
//
//            LoggerManager.info(String.format("androidUserCount = %s, androidForegroundCount = %s, androidBackgroundCount = %s, androidWaitReconnectCount = %s", androidUserCount, androidForegroundCount, androidBackgroundCount, androidWaitReconnectCount));
//            LoggerManager.info(String.format("iosUserCount = %s, iosWaitReconnectCount = %s", iosUserCount, iosWaitReconnectCount));
//            LoggerManager.info(String.format("otherUserCount = %s, otherWaitReconnectCount = %s", otherUserCount, otherWaitReconnectCount));
//
//
//            StringBuffer networkBuffer = new StringBuffer();
//            networkBuffer.append("\n==================网络分布==================\n");
//            for (Map.Entry<String, Integer> entry : networkCountMap.entrySet()) {
//                networkBuffer.append(String.format("%s = %s\n", entry.getKey(), entry.getValue()));
//            }
//            LoggerManager.info(networkBuffer.toString());
////            LoggerManager.info("==================机型分布==================");
////            for(Map.Entry<String,Integer> entry: modelCountMap.entrySet()) {
////                LoggerManager.info(String.format("%s = %s", entry.getKey(), entry.getValue()));
////            }
//
//            StringBuffer versionBuffer = new StringBuffer();
//            versionBuffer.append("\n==================版本分布==================\n");
//            for (Map.Entry<String, Integer> entry : versionCountMap.entrySet()) {
//                versionBuffer.append(String.format("%s = %s\n", entry.getKey(), entry.getValue()));
//            }
//            LoggerManager.info(versionBuffer.toString());
//
//            StringBuffer channelBuffer = new StringBuffer();
//            channelBuffer.append("\n==================渠道分布==================\n");
//            for (Map.Entry<String, Integer> entry : channelCountMap.entrySet()) {
//                channelBuffer.append(String.format("%s = %s\n", entry.getKey(), entry.getValue()));
//            }
//            LoggerManager.info(channelBuffer.toString());
//
//            StringBuffer systemBuffer = new StringBuffer();
//            systemBuffer.append("\n==================系统分布==================\n");
//            for (Map.Entry<String, Integer> entry : systemCountMap.entrySet()) {
//                systemBuffer.append(String.format("%s = %s\n", entry.getKey(), entry.getValue()));
//            }
//            LoggerManager.info(systemBuffer.toString());
//
//        }
//
//        LoggerManager.info("after clean current client session size = " + userSessionMap.size());
//        LoggerManager.info("after clean current web session size = " + userWebSessionMap.size());
//
//        LoggerManager.info("====== checkSession end  (use " + (System.currentTimeMillis() - startTime) + "ms)======");
//    }
//
//    private void addMapCount(Map<String, Integer> countMap, String key) {
//        if (countMap != null) {
//            Integer count = countMap.get(key);
//            if (count == null) {
//                count = 1;
//            } else {
//                count++;
//            }
//            countMap.put(key, count);
//        }
//    }
//
//    private void syncSession(UserSession userSession) {
//        userSession.setLastSyncTime(System.currentTimeMillis());
//        String key = userSession.getType() == UserSession.USER_SESSION_TYPE_CLIENT ? getKey(userSession.getUserId()) : getWebKey(userSession.getUserId());
//        cacheService.set(key, userSession, UserSession.class, ChatConstant.HEART_BEAT_INTERVAL * 3);
//        cacheService.set(getOnlineKey(userSession.getUserId(), userSession.getIdentity()), "", ChatConstant.HEART_BEAT_INTERVAL * 3);
//    }
//
//    public void presence(long userId, ChatProtocol.TechwolfPresence presence, int type) {
//        UserSession userSession = getUserSession(userId, type);
//        if (userSession != null) {
//            if (presence.getClientInfo() != null) {
//                userSession.setClientVersion(NumberUtils.toFloat(presence.getClientInfo().getVersion()));
//                userSession.setClientSystem(presence.getClientInfo().getSystem());
//                userSession.setClientSystemVersion(presence.getClientInfo().getSystemVersion());
//                userSession.setClientModel(presence.getClientInfo().getModel());
//                userSession.setAppId(presence.getClientInfo().getAppid());
//                userSession.setUniqId(presence.getClientInfo().getUniqid());
//                userSession.setPlatform(presence.getClientInfo().getPlatform());
//                userSession.setNetwork(presence.getClientInfo().getNetwork());
//                userSession.setChannel(presence.getClientInfo().getChannel());
//            }
//            if (presence.getClientTime() != null) {
//                userSession.setClientStartTime(presence.getClientTime().getStartTime());
//                userSession.setClientResumeTime(presence.getClientTime().getResumeTime());
//            }
//            if (presence.getType() == 4) {
//                //后台（Android）
//                userSession.setClientStatus(UserSession.USER_SESSION_CLIENT_STATUS_BACK);
//            }
//            if (presence.getType() == 5) {
//                //恢复（Android）
//                userSession.setClientStatus(UserSession.USER_SESSION_CLIENT_STATUS_FRONT);
//            }
//            this.afterPresence(presence, userSession);
//        }
//    }
//
//    @Override
//    public List<UserSession> getAllUserSession(long userId) {
//        List<UserSession> sessionList = new ArrayList<UserSession>();
//        UserSession userSession = getUserSession(userId, UserSession.USER_SESSION_TYPE_CLIENT);
//        if (userSession != null) {
//            sessionList.add(userSession);
//        }
//        userSession = getUserSession(userId, UserSession.USER_SESSION_TYPE_WEB);
//        if (userSession != null) {
//            sessionList.add(userSession);
//        }
//        return sessionList;
//    }
//
//    @Override
//    public UserSession getUserSession(long userId, int type) {
//        if (userId <= 1000) {
//            return new UserSession(userId, 0, "127.0.0.1", 2, null);
//        }
//        if (type == UserSession.USER_SESSION_TYPE_CLIENT) {
//            UserSession userSession = userSessionMap.get(userId);
//            if (userSession == null) {
//                //这里可以加反向内存cache
//                userSession = cacheService.get(getKey(userId), UserSession.class);
//                if (userSession != null) {
//                    userSessionMap.put(userSession.getUserId(), userSession);
//                }
//            }
//            return userSession;
//        } else if (type == UserSession.USER_SESSION_TYPE_WEB) {
//            UserSession userSession = userWebSessionMap.get(userId);
//            if (userSession == null) {
//                //这里可以加反向内存cache
//                userSession = cacheService.get(getWebKey(userId), UserSession.class);
//                if (userSession != null) {
//                    userWebSessionMap.put(userSession.getUserId(), userSession);
//                }
//            }
//            return userSession;
//        }
//        return null;
//    }
//
//    @Override
//    public boolean destroyUserSession(long userId, long cid, int type) {
//        RequestIdUtil.setRequestId(userId);
//        UserSession userSession = getUserSession(userId, type);
//        if (userSession != null && userSession.getCid() == cid) {
//            destroyUserSession(userId, userSession);
//            return true;
//        } else {
//            LoggerManager.warn("UserSession.destroyUserSession fail session =" + userSession + ",userId=" + userId + ", cid = " + cid);
//        }
//        return false;
//    }
//
//    private boolean destroyUserSession(long userId, UserSession userSession) {
//        deleteUserSession(userId, userSession.getType());
//        LoggerManager.info(userSession + " was destroyed");
//        onSessionDestroy(userSession);
//        return true;
//    }
//
//    @Override
//    public void afterPresence(final TechwolfPresence presence, final UserSession userSession) {
//        try {
//            LoggerManager.warn("UserSession.afterPresence session =" + userSession);
//            executorService.submit(new Runnable() {
//                @Override
//                public void run() {
//                    if (CollectionUtils.isNotEmpty(listeners)) {
//                        for (UserSessionListener listener : listeners) {
//                            try {
//                                listener.afterPresence(presence, userSession);
//                            } catch (Exception e) {
//                                LoggerManager.error("", e);
//                            }
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//            LoggerManager.error("", e);
//        }
//    }
//
//    @Override
//    public boolean heartBeat(long userId, int type) {
//        RequestIdUtil.setRequestId(userId);
//        UserSession userSession = getUserSession(userId, type);
//        if (userSession == null) {
//            LoggerManager.warn("userSession was not exist when ping! userId=" + userId);
//            return false;
//        }
//        userSession.setLastHeartBeat(System.currentTimeMillis());
//        if (System.currentTimeMillis() - userSession.getLastSyncTime() > ChatConstant.HEART_BEAT_INTERVAL * 2000) {
//            syncSession(userSession);
//        }
//        return true;
//    }
//
//    @Override
//    public Map<Long, Integer> getOnlineStatus(List<Long> userIdList) {
//        Map<Long, Integer> onlineMap = new HashMap<Long, Integer>();
//        for (Long userId : userIdList) {
//            UserSession userSession = userSessionMap.get(userId);
//            if (userSession != null) {
//                onlineMap.put(userId, userSession.getStatus());
//            }
//        }
//        return onlineMap;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        Assert.notNull(cacheService, "cacheService must not null!");
//        schedule.scheduleAtFixedRate(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    cache.cleanUp();
//                } catch (Exception e) {
//                    LoggerManager.error("clean up guava Cache error! ", e);
//                }
//            }
//        }, 2, 1, TimeUnit.SECONDS);
//        schedule.scheduleAtFixedRate(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    checkSession();
//                } catch (Exception e) {
//                    LoggerManager.error("checkSession error! ", e);
//                }
//            }
//        }, ChatConstant.HEART_BEAT_INTERVAL, ChatConstant.HEART_BEAT_INTERVAL * 3, TimeUnit.SECONDS);
//    }
//
//    private String getOnlineKey(long userId, int identity) {
//        return "chat_online_" + userId + "_" + identity; //供排序使用的key
//    }
//
//    private String getKey(long userId) {
//        return "user_chat_session_" + userId;
//    }
//
//    private String getWebKey(long userId) {
//        return "user_chat_web_session_" + userId;
//    }
//
//    public void setListeners(List<UserSessionListener> listeners) {
//        this.listeners = listeners;
//    }
//
//    public void setCacheService(ICacheService cacheService) {
//        this.cacheService = cacheService;
//    }
//
//}