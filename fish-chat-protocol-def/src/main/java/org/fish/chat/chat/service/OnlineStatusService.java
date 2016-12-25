package org.fish.chat.chat.service;

import java.util.List;
import java.util.Map;

public interface OnlineStatusService {

    public Map<Long, Integer> getOnlineStatus(List<Long> userIdList);
}
