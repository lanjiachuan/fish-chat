package org.fish.chat.chat.listener.impl;

import org.fish.chat.chat.ChatProtocol;
import org.fish.chat.chat.listener.UserSessionListener;
import org.fish.chat.chat.model.UserSession;

/**
 * Comments for UserSessionListenerAdapter.java
 *
 */
public class UserSessionListenerAdapter implements UserSessionListener {

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.listener.UserSessionListener#onSessionDestroy(cn.techwolf.boss.chat.model.UserSession)
     */
    @Override
    public void onSessionDestroy(UserSession userSession) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.listener.UserSessionListener#onSessionCreate(cn.techwolf.boss.chat.model.UserSession)
     */
    @Override
    public void onSessionCreate(UserSession userSession) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.listener.UserSessionListener#onSessionOwnerChange(cn.techwolf.boss.chat.model.UserSession)
     */
    @Override
    public void onSessionOwnerChange(UserSession oldUserSession, UserSession newUserSession) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.listener.UserSessionListener#onSessionReconnect(cn.techwolf.boss.chat.model.UserSession)
     */
    @Override
    public void onSessionReconnect(UserSession userSession) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see cn.techwolf.boss.chat.listener.UserSessionListener#onSessionDisconnect(cn.techwolf.boss.chat.model.UserSession)
     */
    @Override
    public void onSessionDisconnect(UserSession userSession) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterPresence(ChatProtocol.FishPresence presence, UserSession userSession) {

    }
}
