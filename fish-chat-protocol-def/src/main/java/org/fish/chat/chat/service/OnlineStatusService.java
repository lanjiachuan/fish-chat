package org.fish.chat.chat.service;

import java.util.List;
import java.util.Map;

/**
 * 在线状态查询
 *
 * @author adre
 */
public interface OnlineStatusService {

    /**
     * 获取在线状态
     * @param userIdList
     * @return
     */
    Map<Long, Integer> getOnlineStatus(List<Long> userIdList);
}
