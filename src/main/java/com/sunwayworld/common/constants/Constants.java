package com.sunwayworld.common.constants;

public class Constants {
    public static final int EXPIRY = 3;
    public static final int LOGIN = 7;

    public static final int SUCCESS_ONLINE = 9;
    public static final int SUCCESS_OFFLINE = 11;
    public static final int FAILURE_OFFLINE = 12;

    public static final int MAXVERSION = 14;
    public static final int UNFOUNDVERSION = 15;

    public static final int SUCCESS = 99;
    public static final String SUCCESS_STRING = "success";

    public static final int FAILURE = 100;
    public static final String FAILURE_STRING = "failure";

    public static final int DOWNLOAD_AGAIN = 33;//重新下载
    public static final int DOWNLOAD_NEXT = 44;//跳过

    public static final int UPLOAD_AGAIN = 55;//重新上传
    public static final int UPLOAD_NEXT = 66;//跳过

    //ListView一页限定条数
    public static final int PAGE_LIMIT_COUNT = 100;
    public static final int PAGECOUNT = 100;


    //登录状态
    public static int loginState = -1;

    //设备的Imei和mac地址
    public static String deviceImei = null;
    public static String deviceMac = null;


    //数据库常见操作CRUD
    /**
     * 修改
     */
    public static int UPDATE = 1;
    /**
     * 删除
     */
    public static int DELETE = 2;
    /**
     * 退回
     */
    public static int BACK = 3;
    /**
     * 上传
     */
    public static int UPLOAD = 4;

}
