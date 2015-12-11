package org.fish.chat.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by guo on 15-10-27.
 */
public class DateUtils {

    public static Date parseDate(String dateStr, String format) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(format)) {
            return null;
        }
        try {
            return new SimpleDateFormat(format, Locale.CHINA).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateStr(Date date, String format) {
        if (date == null || StringUtils.isBlank(format)) {
            return StringUtils.EMPTY;
        }
        return new SimpleDateFormat(format, Locale.CHINA).format(date);
    }

}
