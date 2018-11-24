package com.sunwayworld.common.dao.model;


import com.sunwayworld.common.entity.SysConfig;
import com.sunwayworld.common.entity.User;
import com.sunwayworld.utils.util.SPUtils;

public class AppModel {
    private static User user;
    private static SysConfig sysConfig;

    public static User getUser() {
        if (user == null) {
            user = (User) SPUtils.getBase64Str("user", null);
        }
        return user;
    }

    public static void setUser(User usr) {
        SPUtils.putBase64Str("user", usr);
        user = usr;
    }


    public static SysConfig getSysConfig() {
        if (sysConfig == null) {
            sysConfig = (SysConfig) SPUtils.getBase64Str("sysConfig", null);
        }
        return sysConfig;
    }

    public static void setSysConfig(SysConfig sysconfig) {
        SPUtils.putBase64Str("sysConfig", sysconfig);
        sysConfig = sysconfig;
    }


    static int screenDp;

    public static int getScreenDp() {
        return screenDp;
    }

    public static void setScreenDp(int screenDp) {
        AppModel.screenDp = screenDp;
    }

}
