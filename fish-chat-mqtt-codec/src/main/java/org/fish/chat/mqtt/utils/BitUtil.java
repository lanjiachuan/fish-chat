package org.fish.chat.mqtt.utils;

/**
 * bit计算
 *
 * @author adre
 */
public class BitUtil {

    /**
     * 从右到左,0--7
     * 
     * @param b
     * @param index
     * @return
     */
    public static boolean getBit(byte b, int index) {
        int i = b & (1 << index);
        return i != 0;

    }

    /**
     * 从左到右,0--7
     * 
     * @param b
     * @param index
     * @return
     */
    public static int getBitNum(byte b, int index) {
        int i = b & (1 << index);
        int n = 0;
        if (i != 0) {
            n = 1;

        }
        return n;

    }

    public static byte getHalfHight(byte b) {
        return (byte) ((b >> 4) & 0x0f);
    }

    public static byte getHalfLow(byte b) {
        return (byte) (b & 0x0f);
    }

    public static String byte2String(byte b) {
//        String s = "";
        StringBuilder builder = new StringBuilder(8);
        for (int i = 7; i >= 0; i--) {
//            s = s + getBitNum(b, i);
            builder.append(getBitNum(b, i));
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        short i = 1000;
        short j = 2000;
        int left = i << 16;
        int userId = left | j;
        System.out.println(left);
        System.out.println(userId);

    }

    public static int byteToInt(byte[] b) {

        int mask = 0xff;
        int temp = 0;
        int n = 0;
        for (int i = 0; i < 4; i++) {
            n <<= 8;
            temp = b[i] & mask;
            n |= temp;
        }
        return n;
    }

    public static int byteToShort(byte[] b) {

        int mask = 0xff;
        int temp = 0;
        int n = 0;
        for (int i = 0; i < 2; i++) {
            n <<= 8;
            temp = b[i] & mask;
            n |= temp;
        }
        return n;
    }

    public static byte[] intToBytes(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        // for (int j = 0; j < 4; j++) {
        // result[j] = (byte) (j >> (24 - j * 8) & 0xff);
        //
        // }

        return result;
    }

    /**
     * 
     * @param i
     * @return
     */
    public static byte[] shortToBytes(short i) {
        byte[] result = new byte[2];
        result[0] = (byte) ((i >> 8) & 0xFF);
        result[1] = (byte) (i & 0xFF);
        return result;
    }

}
