package org.fish.chat.common.utils;

/**
 * Created by guo on 15-10-27.
 * 星座工具类
 */
public class ConstellationUtils {

    public static String getAstro(int month, int day) {
        String[] starArr = {"魔羯座","水瓶座", "双鱼座", "牡羊座",
                "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座" };
        int[] DayArr = {22, 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22};  // 两个星座分割日
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < DayArr[month - 1]) {
            index = index - 1;
        }
        // 取模
        index = index % 11;
        // 返回索引指向的星座string
        return starArr[index];
    }

}
