package org.fish.chat.common.utils;

import cn.techwolf.common.log.LoggerManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by liujun on 15/6/19.
 */
public class IdSecurityUtils {

    public static String encryptString(String plainText) {
        try {
            String encryptString = SecurityUtils.rc4Encrypt(plainText);
            encryptString = StringUtils.substring(DigestUtils.md5Hex(encryptString), 8, 24) + encryptString;
            return encryptString;
        }
        catch (Throwable e) {
            LoggerManager.error("encryptString", e);
        }
        return null;
    }

    public static String decryptString(String encryptString) {
        try {
            String sum = StringUtils.substring(encryptString, 0, 16);
            encryptString = StringUtils.substring(encryptString, 16);

            String plainText = SecurityUtils.rc4Decrypt(encryptString);

            if (StringUtils.isNotBlank(plainText) &&
                    StringUtils.equalsIgnoreCase(StringUtils.substring(DigestUtils.md5Hex(encryptString), 8, 24), sum)) {
                return plainText;
            }
        } catch (Throwable e) {
            LoggerManager.error("decryptString", e);
        }
        return null;
    }

    public static String encryptId(long id) {
        return encryptString(String.valueOf(id));
    }

    public static long decryptId(String encryptId) {
        return NumberUtils.toLong(decryptString(encryptId));
    }

    public static void main(String[] args) {
        String encode = IdSecurityUtils.encryptString("1003");

        System.out.println("encode : " + encode);
        System.out.println("decode : " + IdSecurityUtils.decryptString(encode));

    }

}

