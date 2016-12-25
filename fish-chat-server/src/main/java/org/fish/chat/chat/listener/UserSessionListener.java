package org.fish.chat.chat.listener;


import org.fish.chat.chat.ChatProtocol;
import org.fish.chat.chat.model.UserSession;

/**
 * Comments for UserSessionListener.java
 */
public interface UserSessionListener {

    /**
     * session销毁时
     *
     * @param userSession
     */
    public void onSessionDestroy(UserSession userSession);

    /**
     * 创建session时
     *
     * @param userSession
     */
    public void onSessionCreate(UserSession userSession);

    /**
     * 用户重新连接
     *
     * @param userSession
     */
    public void onSessionReconnect(UserSession userSession);

    /**
     * 用户连接断开
     *
     * @param userSession
     */
    public void onSessionDisconnect(UserSession userSession);

    /**
     * 单点登录，session拥有者被替换
     *
     */
    public void onSessionOwnerChange(UserSession oldUserSession, UserSession newUserSession);

    /**
     * 用户发送presence后
     * */
    public void afterPresence(ChatProtocol.FishPresence presence, UserSession userSession);

}
