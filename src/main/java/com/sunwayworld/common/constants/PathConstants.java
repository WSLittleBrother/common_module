package com.sunwayworld.common.constants;


import com.sunwayworld.utils.util.SDCardUtils;

/**
 * @author zhaolp ${2016/5/13}
 * @version V1.0
 * @Description 数据存放路径的url
 */
public class PathConstants {

    public static final String PATH = "ws/demo/";
    //所有文件所放目录
    public static final String APP_SDCARD_PATH = SDCardUtils.getSDCardPath() + PATH;
    //所有错误日志所放目录
    public static final String SDCARD_CRASH_PATH = APP_SDCARD_PATH + "crash/";
    //所有用户数据目录
    public static final String SDCARD_DATA_PATH = APP_SDCARD_PATH + "data/";
    //更新包目录
    public static final String SDCARD_UPDATE_PATH = APP_SDCARD_PATH + "update/";
    //当前用户目录
    public static String SDCARD_USER_DATA_PATH;
    //当前用户常用文件目录
    public static String SDCARD_USER_COMMENT_PATH;
    //当前用户导出PD的目录
    public static String SDCARD_USER_PDF_PATH;


}
