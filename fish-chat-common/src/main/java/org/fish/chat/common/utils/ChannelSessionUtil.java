
package org.fish.chat.common.utils;



/**
 * 根据cid 获得 ip
 *
 * @author adre
 */
public class ChannelSessionUtil {

    public static String getIpByChannelId(long cid) {
        long ip = cid >> 28;
        return IpUtil.longToIP(ip);
    }
    
    public static void main(String[] args) {
        long cid = 867646728990360058L;
        System.out.println(cid);
        System.out.println(ChannelSessionUtil.getIpByChannelId(cid));
    }

}
