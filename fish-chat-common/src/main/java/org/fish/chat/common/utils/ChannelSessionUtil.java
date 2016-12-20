
package org.fish.chat.common.utils;

import cn.techwolf.common.location.utils.IpUtil;

/**
 * Comments for ChannelSessionUtil.java
 * 
 * @author <a href="mailto:liujun@techwolf.cn">刘军</a>
 * @createTime 2014年4月12日 下午7:47:01
 */
public class ChannelSessionUtil {

    public static String getIpByChannelId(long cid) {
        long ip = cid >> 28;
        return IpUtil.getIpStringFromInt((int) ip);
    }
    
    public static void main(String[] args) {
        long cid =867646728990360058l;
        System.out.println(cid);
        System.out.println(ChannelSessionUtil.getIpByChannelId(cid));
    }

}
