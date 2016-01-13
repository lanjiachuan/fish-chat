package org.fish.chat.common.log;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.fish.chat.common.utils.RequestIdUtil;

/**
 * Created by duqing on 15-3-5.
 */
public class LoggerManager {

    private static final Logger debugLogger = Logger.getLogger("debug");

    private static final Logger infoLogger = Logger.getLogger("info");

    private static final Logger warnLogger = Logger.getLogger("warn");

    private static final Logger errorLogger = Logger.getLogger("error");

    private static final Logger accessLogger = Logger.getLogger("access");

    public static void debug(Object msg) {
        if (debugLogger.isDebugEnabled()) {
            debugLogger.debug(getRequetIdString(msg));
        }
    }

    public static void debug(Object msg, Throwable e) {
        if (debugLogger.isDebugEnabled()) {
            debugLogger.debug(getRequetIdString(msg), e);
        }
    }

    public static void info(Object msg) {
        if (infoLogger.isInfoEnabled()) {
            infoLogger.info(getRequetIdString(msg));
        }
    }

    public static void info(Object msg, Throwable e) {
        if (infoLogger.isInfoEnabled()) {
            infoLogger.info(getRequetIdString(msg), e);
        }
    }

    public static void warn(Object msg) {
        warnLogger.warn(getRequetIdString(msg));
    }

    public static void warn(Object msg, Throwable e) {
        warnLogger.warn(getRequetIdString(msg), e);
    }

    public static void error(Object msg) {
        errorLogger.error(getRequetIdString(msg));
    }

    public static void error(Object msg, Throwable e) {
        errorLogger.error(getRequetIdString(msg), e);
    }

    private static String getRequetIdString(Object msg) {
        long requestId = RequestIdUtil.getRequestId();
        String finalMessage = requestId == 0 ? StringUtils.EMPTY : "[" + requestId + "]";
        if (msg != null) {
            finalMessage += msg.toString();
        }
        return finalMessage;
    }
}
