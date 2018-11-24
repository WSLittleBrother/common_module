package com.sunwayworld.common.utils.webservice;

/**
 * Created by 99351 on 2018/6/14.
 */

public class WebserviceTimeUtils {

    public static String XX2HX(String timeString) {
        return timeString.replace("/", "-");
    }
//    public static String XX2HX(String timeString) {
//        String timestr = timeString.replace("/", "-");
//        long timeMill = TimeUtils.string2Millis(timestr);
//        return TimeUtils.millis2String(timeMill, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//    }
}
