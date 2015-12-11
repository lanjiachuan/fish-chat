package org.fish.chat.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 职位的发布时间
 * 
 * @author xiangan
 * 
 */
public class TimeUtils {

    private static final long HOUR = 60 * 60 * 1000l;
    private static final int DAYS_OF_MONTH = 30;
    private static final int HOURS_OF_DAY = 24;

    public static String jobPubTime(long time) {

    	Calendar current = Calendar.getInstance();
    	Calendar before = Calendar.getInstance();
    	before.setTimeInMillis(time);
    	int beforeDayOfYear = before.get(Calendar.DAY_OF_YEAR);
    	int currentDayOfYear = current.get(Calendar.DAY_OF_YEAR);
    	
    	String timeString;
    	
    	int hour = (int) ((current.getTimeInMillis() - time) / HOUR);
    	
    	if (hour == 0) {//刚刚 
    		timeString = "刚刚";
    	} else if (hour < HOURS_OF_DAY) {//一天内 那么显示几小时
    		timeString = hour + "小时前";
    	} else {//大于一天  那么
    		int days = currentDayOfYear - beforeDayOfYear;
    		if (days == 1) {
    			timeString = "昨天";
    		} else if (days < DAYS_OF_MONTH) {
    			timeString = days + "天前";
    		}  else {
    			timeString = (days / DAYS_OF_MONTH) + "月前";
    		}
    	}
    	
        return timeString;
    }

    public static String activeTimeString(long time) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -3);

        long threeDayBefore = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_MONTH, -4);
        long sevenDayBefore = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_MONTH, -23);
        long oneMonthbefore = calendar.getTimeInMillis();
        String timeString = "";

        if (time > threeDayBefore) {
            timeString = "今日";
        } else if (time > sevenDayBefore) {
            timeString = "三天内";
        } else if (time > oneMonthbefore) {
            timeString = "本周";
        } else {
            timeString = "两周内";
        }
        return "最近回复：" + timeString;

    }
    
    public static String jobPubTimeString(long time) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -3);

        long threeDayBefore = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_MONTH, -4);
        long sevenDayBefore = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_MONTH, -23);
        long oneMonthbefore = calendar.getTimeInMillis();
        String timeString = "";

        if (time > threeDayBefore) {
            timeString = "今日";
        } else if (time > sevenDayBefore) {
            timeString = "三天内";
        } else if (time > oneMonthbefore) {
            timeString = "本周";
        } else {
            timeString = "两周内";
        }
        return timeString;

    }
    
    public static String tagTimeString(long time) {
        return jobPubTime(time);
    }

    public static long getTodayTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    public static int getTodayYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static long getLastMonthTime(int lastN) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - lastN, 0, 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    public static int exipireTime() {
        Calendar curDate = Calendar.getInstance();
        Calendar tommorowDate = new GregorianCalendar(curDate.get(Calendar.YEAR),
                curDate.get(Calendar.MONTH), curDate.get(Calendar.DATE) + 1, 0, 0, 0);
        long timeCap = tommorowDate.getTimeInMillis() - System.currentTimeMillis();
        return (int) timeCap / 1000;
    }

    public static Date getDatefromStr8(String date8) {
        if (StringUtils.isNotBlank(date8)) {
            int length = date8.length();
            if (length >= 8) {
                int year = NumberUtils.toInt(date8.substring(0, 4));
                int month = NumberUtils.toInt(date8.substring(4, 6));
                int day = NumberUtils.toInt(date8.substring(6, 8));
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month - 1, day, 0, 0, 0);

                return calendar.getTime();
            }

        }
        return null;
    }

    public static Date getDatefromStr4(String date) {
        if (StringUtils.isNotBlank(date)) {
            int length = date.length();
            int year =0;
            if (length >4) {
                year  = NumberUtils.toInt(date.substring(0, 4));
               
            }else{
                year=NumberUtils.toInt(date);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(year+1, 0, 0, 0, 0, 0);
            return calendar.getTime();
        }
        return null;
    }
    
    public static String getDate8(String date) {
        String resultDate="";
        if (StringUtils.isNotBlank(date)) {
            if(StringUtils.length(date)==4){
                resultDate=date+"0101";
            }else if(StringUtils.length(date)==6){
                resultDate=date+"01";
            }else{
                resultDate=date;
            }
        }
        return resultDate;
    }
    
    
    

    public static Date getNdaysOffToday(int n){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -n);
        return calendar.getTime();
    }
    
    public static void main(String[] args) {
         Date d=getNdaysOffToday(7);
        System.out.println(d);
    }
}
