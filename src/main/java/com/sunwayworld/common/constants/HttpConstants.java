package com.sunwayworld.common.constants;

/**
 * @Description 网络请求的url
 * @author zhaolp ${2016/5/13}
 * @version V1.0
 */
public class HttpConstants {
    public static final String  ACTIVATIONCODE = "shyjphone";

    /***
     * 激活服务器根地址
     ***/
    public static final String SYSCONFIG_URL = "http://mdm.sunwayworld.com/pm/activate";
    /**
     * 激活码获取
     */
    public static final String ACTIVATION_ACTION = "/activate/activate!activateApp.action";
    /**
     * 检查更新
     */
    public static final String UPDATE_ACTION = "/update/update!checkAppUpdate.action";

}