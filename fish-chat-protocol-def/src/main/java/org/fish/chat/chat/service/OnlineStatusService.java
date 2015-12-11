package org.fish.chat.chat.service;

import java.util.List;
import java.util.Map;

/**
 * Created by liujun on 15/3/5.
 */
public interface OnlineStatusService {

    public Map<Long, Integer> getOnlineStatus(List<Long> userIdList);
}
