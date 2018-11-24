package com.sunwayworld.common.utils;

import java.math.BigDecimal;

/**
 * Created by 99351 on 2018/1/26.
 */

public class DoubleUtils {
    public DoubleUtils() {
            throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static double getDecimalPointDouble(double d,int count){
        BigDecimal b=new BigDecimal(d);
        double  result=b.setScale(count,BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }


    public static double getPointCount(double d,int count){
        return  (double)(Math.round(d*count))/count;
    }

    public static String getFileSize(long d){
        double l = d;
        if (l/(1024)<1){
            return l+"字节";
        }else if (l/(1024*1024)<1){
            return getPointCount(l/1024,1000) +" kb";
        }else if (l/(1024*1024*1024)<1){
            return getPointCount(l/(1024*1024),1000)+" M";
        }else{
            return getPointCount(l/(1024*1024*1024),1000)+" G";
        }
    }

}
