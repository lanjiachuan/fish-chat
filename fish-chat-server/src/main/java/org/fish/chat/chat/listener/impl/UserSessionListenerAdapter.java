package org.fish.chat.chat.listener.impl;

import org.fish.chat.chat.ChatProtocol;
import org.fish.chat.chat.listener.UserSessionListener;
import org.fish.chat.chat.model.UserSession;

/**
 * Comments for UserSessionListenerAdapter.java
 *
 */
public class UserSessionListenerAdapter implements UserSessionListener {

    @Override
    public void onSessionDestroy(UserSession userSession) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSessionCreate(UserSession userSession) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSessionOwnerChange(UserSession oldUserSession, UserSession newUserSession) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSessionReconnect(UserSession userSession) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSessionDisconnect(UserSession userSession) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterPresence(ChatProtocol.FishPresence presence, UserSession userSession) {

    }
}
