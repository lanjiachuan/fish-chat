package org.fish.chat.chat.service;


import org.fish.chat.chat.ChatProtocol;
import org.fish.chat.chat.model.UserSession;

import java.util.List;

/**
 * chat server session
 *
 * @author adre
 */
public interface UserSessionService {

    /**
     * 连接创建
     * @param userId
     * @param cid
     * @param ip
     * @param identity
     * @param clientId
     * @param type
     * @param protocolVersion
     * @return
     */
    UserSession connect(long userId, long cid, String ip, int identity, String clientId, int type, float protocolVersion);

    /**
     * 断开
     * @param userId
     * @param cid
     * @param type
     * @return
     */
    boolean disconnect(long userId, long cid, int type);

    /**
     * 获取所有在线session
     * @param userId
     * @return
     */
    List<UserSession> getAllUserSession(long userId);

    /**
     * 获取session
     * @param userId
     * @param type
     * @return
     */
    UserSession getUserSession(long userId, int type);

    /**
     * 清理session
     * @param userId
     * @param cid
     * @param type
     * @return
     */
    boolean destroyUserSession(long userId, long cid, int type);

    /**
     * 心跳
     * @param userId
     * @param type
     * @return
     */
    boolean heartBeat(long userId, int type);

    /**
     * 出席消息
     * @param userId
     * @param presence
     * @param type
     */
    void presence(long userId, ChatProtocol.FishPresence presence, int type);

}