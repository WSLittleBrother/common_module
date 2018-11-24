package com.sunwayworld.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间戳的工具类
 * Created by 99351 on 2018/3/7.
 */

public class TimeStampUtils {
    /**
     * 根据毫秒时间戳来格式化字符串
     * 今天显示今天、昨天显示昨天、前天显示前天.
     * 早于前天的显示当前的天
     * @param timeStamp 毫秒值
     * @return 今天 昨天 前天 或者 yyyy-MM-dd HH:mm:ss类型字符串
     */
    public static String getDayStamp(long timeStamp) {
        String result ="";
        long curTimeMillis = System.currentTimeMillis();

        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(new Date(curTimeMillis));
        int todayYear=todayCalendar.get(Calendar.YEAR);
        int todayMonth=todayCalendar.get(Calendar.MONTH);
        int todayDay=todayCalendar.get(Calendar.DAY_OF_MONTH);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timeStamp));
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        if (todayYear==year&todayMonth==month){
            switch (todayDay-day){
                case 0:
                    result =  "今";
                    break;
                case 1:
                    result =  "昨";
                    break;
                case 2:
                    result =  "前";
                    break;
                    default:
                        result =  day+"日";
                        break;
            }
        }
        return  result;
    }
//——————————分割线—————————————————–

    /**
     * 根据时间戳来判断当前的时间是几天前,几分钟,刚刚
     * @param timeStamp
     * @return
     */
    public static String getTimeStateNew(long timeStamp){
        String result = "";
        long curTimeMillis = System.currentTimeMillis();
        long stamp = (curTimeMillis - timeStamp)/1000;
        if (stamp<60){
            result = "刚刚";
        }else if (stamp<60*60){
            result =  stamp/60 +"分钟前";
        }else if (stamp<60*60*24){
            result = stamp/(60*60) +"小时前";
        }else if (stamp<60*60*24*10){
            result = stamp/(60*60*24) +"天前";
        }else {
            result = new SimpleDateFormat("yy-MM-dd").format(new Date(timeStamp));
        }

        return result;
    }
}
