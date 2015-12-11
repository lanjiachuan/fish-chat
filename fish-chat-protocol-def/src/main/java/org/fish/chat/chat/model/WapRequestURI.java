package org.fish.chat.chat.model;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * 需要返回客户端的wap或者web路径
 *
 * @author xiangan
 */
public class WapRequestURI implements InitializingBean {

    public static String wapHhost;

    public static String RECOOMEND_INVITE;

    public static String SHARE_RECOOMEND_INVITE;

    public static String SHARE_BOSS_DETAIL;

    public static String SHARE_NIUREN_DETAIL;

    public static String SHARE_NIUREN_INVITE;

    public static String SHARE_INVITE_BOSS;

    public static String SHARE_INVITE_GEEK;

    public static String SHARE_JOB_DETAIL;

    public static String SHARE_INVITE_USER;

    public static String SHARE_CASH_GITF;

    public static String SHARE_BOSS_HOME;

    public static String WAP_HOST;

    public void setWapHhost(String wapHhost) {
        WapRequestURI.wapHhost = wapHhost;
        RECOOMEND_INVITE = "http://" + wapHhost + "/";
        SHARE_RECOOMEND_INVITE = "http://" + wapHhost + "/sr/";
        SHARE_BOSS_DETAIL = "http://" + wapHhost + "/boss/";
        SHARE_NIUREN_DETAIL = "http://" + wapHhost + "/niu/";
        SHARE_NIUREN_INVITE = "http://" + wapHhost + "/invite/geekhome/";
        SHARE_INVITE_BOSS = "http://" + wapHhost + "/invite/boss";
        SHARE_INVITE_GEEK = "http://" + wapHhost + "/invite/geek";
        SHARE_JOB_DETAIL = "http://" + wapHhost + "/job/";
        SHARE_INVITE_USER = "http://" + wapHhost + "/iu";
        SHARE_CASH_GITF = "http://" + wapHhost + "/activity/cashgift/self";
        SHARE_BOSS_HOME = "http://" + wapHhost;
        WAP_HOST = "http://" + wapHhost;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(wapHhost, "wapHhost is required");
    }

}
