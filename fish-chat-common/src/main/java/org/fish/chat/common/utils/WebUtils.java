package org.fish.chat.common.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * WEB工具类，主要是读写Cookie和Web request、response对象一些常用操作
 * 
 * @ClassName: WebUtils
 * @author wwj <wangweijie@techwolf.cn>
 * @date 2014年2月11日 上午11:24:49
 * 
 */
public abstract class WebUtils {

    public static final String JSON_SUFFIX = ".json";

    public static final String REQUEST_METHOD_POST = "post";

    public static final String REQUEST_METHOD_GET = "get";

    public static final String METHOD_HEAD = "HEAD";

    public static final String METHOD_GET = "GET";

    public static final String METHOD_POST = "POST";

    public static final String HEADER_PRAGMA = "Pragma";

    public static final String HEADER_EXPIRES = "Expires";

    public static final String HEADER_CACHE_CONTROL = "Cache-Control";

    public static final int SESSION_COOKIE = -1;

    public static final int FOREVER_COOKIE = 0x12cc0300;

    public static final String INTRANET_PREFIX_10 = "10.";
    public static final String INTRANET_PREFIX_192 = "192.168.";
    public static final String INTRANET_PREFIX_100_97 = "100.97.";
    public static final String INTRANET_PREFIX_180_97_88 = "180.97.88.";
    public static final String INTRANET_PREFIX_180_97_165 = "180.97.165.";
    public static final String INTRANET_PREFIX_180_97_166 = "180.97.166.";

    /* 全局唯一ID */
    public static final String GLOBAL_UUID_COOKIE = "__a";
	
    protected WebUtils() {
    }

    public static Cookie cookie(String key, String value, int validy) {
        return cookie(key, value, validy, null);
    }

    public static Cookie cookie(String key, String value, int validy, String domainName) {
        if (key != null && value != null) {
            Cookie cookie = new Cookie(key, value);
            cookie.setMaxAge(validy);
            cookie.setPath("/");
            if (domainName != null) cookie.setDomain(domainName);
            return cookie;
        } else {
            return null;
        }
    }

    public static Cookie removeableCookie(String key, String domainName) {
        if (key != null) {
            Cookie cookie = new Cookie(key, "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            if (domainName != null) cookie.setDomain(domainName);
            return cookie;
        } else {
            return null;
        }
    }

    public static boolean areEquals(Cookie cookie1, Cookie cookie2) {
        return cookie1.getName().equals(cookie2.getName())
                && (cookie1.getValue() != null ? cookie1.getValue().equals(cookie2.getValue())
                        : cookie2.getValue() == null)
                && (cookie1.getMaxAge() == cookie2.getMaxAge()
                        && cookie1.getSecure() == cookie2.getSecure() && cookie1.getVersion() == cookie2
                        .getVersion())
                && (cookie1.getComment() != null ? cookie1.getComment()
                        .equals(cookie2.getComment()) : cookie2.getComment() == null)
                && (cookie1.getDomain() != null ? cookie1.getDomain().equals(cookie2.getDomain())
                        : cookie2.getDomain() == null)
                && (cookie1.getPath() != null ? cookie1.getPath().equals(cookie2.getPath())
                        : cookie2.getPath() == null);
    }

    public static final String getCookieValue(String key, HttpServletRequest req) {
        String str = null;
        Cookie cookies[] = req.getCookies();
        if (null == cookies) return null;
        for (int i = 0; i < cookies.length && str == null; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equals(key)) return cookie.getValue();
        }
        return null;
    }

    public static void applyCacheSeconds(HttpServletResponse response, int pageCacheSecond,
            boolean mustRevalidate) {
        if (pageCacheSecond > 0) {
            // HTTP 1.0 header
            response.setDateHeader(HEADER_EXPIRES, System.currentTimeMillis() + pageCacheSecond
                    * 1000L);
            // HTTP 1.1 header
            String headerValue = "max-age=" + pageCacheSecond;
            if (mustRevalidate) {
                headerValue += ", must-revalidate";
            }
            response.setHeader(HEADER_CACHE_CONTROL, headerValue);
        } else if (pageCacheSecond == 0) {
            response.setHeader(HEADER_PRAGMA, "No-cache");
            // HTTP 1.0 header
            response.setDateHeader(HEADER_EXPIRES, 1L);
            // HTTP 1.1 header: "no-cache" is the standard value,
            // "no-store" is necessary to prevent caching on FireFox.
            response.setHeader(HEADER_CACHE_CONTROL, "no-cache");
            response.addHeader(HEADER_CACHE_CONTROL, "no-store");
        }
    }

    @SuppressWarnings("unchecked")
    public static String parseQueryString(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        String result = "";
        for (Object o : map.keySet()) {
            String key = o + "";
            String[] values = (String[]) map.get(key);
            for (String value : values) {
                result += key + "=" + value + "&";
            }
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = "";
        Set<String> ipSet = getIpAddrSet(request);
        if(!ipSet.isEmpty()){
            ip = ipSet.iterator().next();
        }
        return ip;
    }

    //获取非内网的真实和代理的所有ip
    public static Set<String> getIpAddrSet(HttpServletRequest request) {
        Set<String> ipSet = new HashSet<String>();
        String ip = request.getHeader("RemoteIp");
        if (StringUtils.isNotBlank(ip) && !ip.startsWith(INTRANET_PREFIX_10)
                && !ip.startsWith(INTRANET_PREFIX_192) && !ip.startsWith(INTRANET_PREFIX_100_97)
                && !ip.startsWith(INTRANET_PREFIX_180_97_88) && !ip.startsWith(INTRANET_PREFIX_180_97_165)
                && !ip.startsWith(INTRANET_PREFIX_180_97_166)) {
            ipSet.add(ip);
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(ip) && !ip.startsWith(INTRANET_PREFIX_10)
                && !ip.startsWith(INTRANET_PREFIX_192) && !ip.startsWith(INTRANET_PREFIX_100_97)
                && !ip.startsWith(INTRANET_PREFIX_180_97_88) && !ip.startsWith(INTRANET_PREFIX_180_97_165)
                && !ip.startsWith(INTRANET_PREFIX_180_97_166)) {
            ipSet.add(ip);
        }

        ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isNotEmpty(ip)) {
            String[] ipArr = StringUtils.split(ip, ",");
            if (ipArr != null && ipArr.length > 0) {
                for (int i = 0; i < ipArr.length; i++) {
                    ip = StringUtils.trim(ipArr[i]);
                    if (StringUtils.isNotBlank(ip) && !ip.startsWith(INTRANET_PREFIX_10)
                            && !ip.startsWith(INTRANET_PREFIX_192) && !ip.startsWith(INTRANET_PREFIX_100_97)
                            && !ip.startsWith(INTRANET_PREFIX_180_97_88) && !ip.startsWith(INTRANET_PREFIX_180_97_165)
                            && !ip.startsWith(INTRANET_PREFIX_180_97_166)) {
                        ipSet.add(ip);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(ip) && !ip.startsWith(INTRANET_PREFIX_10)
                && !ip.startsWith(INTRANET_PREFIX_192) && !ip.startsWith(INTRANET_PREFIX_100_97)
                && !ip.startsWith(INTRANET_PREFIX_180_97_88) && !ip.startsWith(INTRANET_PREFIX_180_97_165)
                && !ip.startsWith(INTRANET_PREFIX_180_97_166)) {
            ipSet.add(ip);
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.isNotBlank(ip) && !ip.startsWith(INTRANET_PREFIX_10)
                && !ip.startsWith(INTRANET_PREFIX_192) && !ip.startsWith(INTRANET_PREFIX_100_97)
                && !ip.startsWith(INTRANET_PREFIX_180_97_88) && !ip.startsWith(INTRANET_PREFIX_180_97_165)
                && !ip.startsWith(INTRANET_PREFIX_180_97_166)) {
            ipSet.add(ip);
        }
        ip = request.getHeader("WL-Proxy-Client-IP");

        if (StringUtils.isNotBlank(ip) && !ip.startsWith(INTRANET_PREFIX_10)
                && !ip.startsWith(INTRANET_PREFIX_192) && !ip.startsWith(INTRANET_PREFIX_100_97)
                && !ip.startsWith(INTRANET_PREFIX_180_97_88) && !ip.startsWith(INTRANET_PREFIX_180_97_165)
                && !ip.startsWith(INTRANET_PREFIX_180_97_166)) {
            ipSet.add(ip);
        }
        ip = request.getRemoteAddr();
        if (StringUtils.isNotBlank(ip) && !ip.startsWith(INTRANET_PREFIX_10)
                && !ip.startsWith(INTRANET_PREFIX_192) && !ip.startsWith(INTRANET_PREFIX_100_97)
                && !ip.startsWith(INTRANET_PREFIX_180_97_88) && !ip.startsWith(INTRANET_PREFIX_180_97_165)
                && !ip.startsWith(INTRANET_PREFIX_180_97_166)) {
            ipSet.add(ip);
        }
        return ipSet;
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    @SuppressWarnings("unchecked")
    public static String getRequestPath(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder(request.getRequestURI());
        Enumeration<String> enumeration = request.getParameterNames();
        if (enumeration.hasMoreElements()) {
            sb.append("?");
        }
        while (enumeration.hasMoreElements()) {
            Object object = enumeration.nextElement();
            sb.append(object);
            sb.append("=");
            sb.append(request.getParameter(object.toString()));
            sb.append("&");
        }
        String requesturi = "";
        String contextPath = request.getContextPath();
        if (sb.indexOf("&") != -1) {
            requesturi = sb.substring(0, sb.lastIndexOf("&"));
        } else {
            requesturi = sb.toString();
        }
        requesturi = requesturi.substring(requesturi.indexOf(contextPath) + contextPath.length());
        return requesturi;
    }

    /**
     * json数据返回
     * 
     * @param response
     * @param str
     */
    public static void printWrite(HttpServletResponse response, String str) {
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("return str is null");
        }
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(str);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * 是否为post请求
     * 
     * @param request
     * @return
     */
    public static boolean isPost(HttpServletRequest request) {
        return StringUtils.equalsIgnoreCase(REQUEST_METHOD_POST, request.getMethod());
    }

    /**
     * 是否为get请求
     * 
     * @param request
     * @return
     */
    public static boolean isGet(HttpServletRequest request) {
        return StringUtils.equalsIgnoreCase(REQUEST_METHOD_GET, request.getMethod());
    }

    /**
     * 是否为json请求
     * 
     * @param request
     * @return
     */
    public static boolean isJsonRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return StringUtils.endsWith(uri, JSON_SUFFIX);
    }

    public static void saveCookie(HttpServletResponse response, String key, String value,
            String domain) {
        saveCookie(response, key, value, -1, "/", domain);
    }

    public static void saveCookie(HttpServletResponse response, String key, String value,
            String path, String domain) {
        saveCookie(response, key, value, -1, path, domain);
    }

    public static void saveCookie(HttpServletResponse response, String key, String value,
            int second, String path, String domain) {
        value = StringUtils.remove(value, '\n');
        value = StringUtils.remove(value, '\r');
        Cookie cookie = new Cookie(key, value);
        if (StringUtils.isNotBlank(path)) {
            cookie.setPath(path);
        }
        cookie.setMaxAge(second);
        if (StringUtils.isNotBlank(domain)) {
            if (!StringUtils.startsWith(domain, ".")) {
                domain = "." + domain;
            }
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }

    public static void clearCookie(HttpServletResponse response, String key, int second,
            String path, String domain) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath(path);
        cookie.setMaxAge(second);
        if (StringUtils.isNotBlank(domain)) {
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }
    
    /**
     * 返回32位唯一uuid
     * @param request
     * @return
     */
    public static String getGlobalUidString(HttpServletRequest request){
        String globalUid = "";
        String cookieStr = WebUtils.getCookieValue(GLOBAL_UUID_COOKIE, request);
        
        String[] aryCookie = StringUtils.split(cookieStr, ".");
        if(aryCookie!=null && aryCookie.length>1){
            globalUid = aryCookie[1] + "-" + aryCookie[0];
        }
        if(StringUtils.isNotEmpty(globalUid) && globalUid.length()<32){
            globalUid = globalUid + StringUtils.repeat("a", 32-globalUid.length());
        }
        return globalUid;
    }
    
    /**
     * 返回long类型唯一uuid
     * @param request
     * @return
     */
    public static long getGlobalUidLong(HttpServletRequest request){
        long globalUid = 0;
        String cookieStr = WebUtils.getCookieValue(GLOBAL_UUID_COOKIE, request);
        String[] aryCookie = StringUtils.split(cookieStr, ".");
        if(aryCookie!=null && aryCookie.length>1){
            globalUid =  NumberUtils.toLong(aryCookie[1] + aryCookie[0], 0);
        }
        return globalUid;
    }

    /**
     * 拼字符串 如|1|2|3|...|
     * @param cookieIds
     * @param newId
     * @return
     */
    public static String concatCookieIds(String cookieIds, String newId) {
        if (StringUtils.isBlank(newId)) {
            return cookieIds;
        }

        if (StringUtils.isBlank(cookieIds)) {
            cookieIds = "|" + newId + "|";
        } else {
            cookieIds = cookieIds + "|" + newId + "|";
        }
        return cookieIds;
    }

    public static Boolean cookieIdsContains(String cookieIds, String newId) {
        if(StringUtils.isBlank(cookieIds)) {
            return false;
        }

        if (StringUtils.isBlank(newId)) {
            return true;
        }
        return cookieIds.contains("|" + newId + "|");
    }
}
