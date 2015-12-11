package org.fish.chat.common.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * AES加密 
 * @ClassName: AES 
 * @author wwj <wangweijie@techwolf.cn>
 * @date 2014年2月10日 下午6:15:27 
 *
 */
public class AES {  
      
    static final String algorithmStr="AES/ECB/PKCS5Padding";  
      
    static private KeyGenerator keyGen;  
      
    static private Cipher cipher;  
      
    static boolean isInited=false;  
      
    //初始化  
    static private void init()  
    {  
          
        //初始化keyGen  
        try {  
            keyGen=KeyGenerator.getInstance("AES");  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        keyGen.init(128);  
          
        //初始化cipher  
        try {  
            cipher=Cipher.getInstance(algorithmStr);  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
        }  
          
        isInited=true;  
    }  
      
	public static byte[] genKey() {
		//如果没有初始化过,则初始化 
		if (!isInited) {
            init();  
        }  
        return keyGen.generateKey().getEncoded();  
    }  
      
	public static byte[] encrypt(byte[] content, byte[] keyBytes) {
        byte[] encryptedText=null;  
        // 为初始化
		if (!isInited) {
            init();  
        }  
          
        Key key=new SecretKeySpec(keyBytes,"AES");  
          
        try {  
            cipher.init(Cipher.ENCRYPT_MODE, key);  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  
        }  
          
        try {  
            encryptedText=cipher.doFinal(content);  
        } catch (IllegalBlockSizeException e) {  
            e.printStackTrace();  
        } catch (BadPaddingException e) {  
            e.printStackTrace();  
        }  
          
        return encryptedText;  
    }  
      
    //解密为byte[]  
	public static byte[] decryptToBytes(byte[] content, byte[] keyBytes) {
        byte[] originBytes=null;  
		
        if (!isInited) {  
            init();  
        }  
          
        Key key=new SecretKeySpec(keyBytes,"AES");  
          
        try {  
            cipher.init(Cipher.DECRYPT_MODE, key);  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  
        }  
          
        //解密  
        try {  
            originBytes=cipher.doFinal(content);  
        } catch (IllegalBlockSizeException e) {  
            e.printStackTrace();  
        } catch (BadPaddingException e) {  
            e.printStackTrace();  
        }  
          
        return originBytes;  
    }
}