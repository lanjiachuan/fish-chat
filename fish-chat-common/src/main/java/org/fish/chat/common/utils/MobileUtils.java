package org.fish.chat.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by liujun on 15/7/28.
 */
public class MobileUtils {

    private static final String[] CHINA_MOBILE_PREFIX = new String[]{"134", "135", "136", "137", "138", "139", "147", "150", "151", "152", "157", "158", "159", "178", "182", "183", "184", "187", "188"};

    public static boolean isChinaMobilePhone(String phone) {
        if (StringUtils.isNotBlank(phone)) {
            for (String prefix : CHINA_MOBILE_PREFIX) {
                if (StringUtils.startsWithIgnoreCase(StringUtils.trim(phone), StringUtils.trim(prefix))) {
                    return true;
                }
            }
        }
        return false;
    }
}
