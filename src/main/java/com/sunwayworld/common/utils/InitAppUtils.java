package com.sunwayworld.common.utils;

import android.content.Context;

import com.sunwayworld.common.constants.PathConstants;
import com.sunwayworld.common.dao.model.AppModel;
import com.sunwayworld.common.entity.SysConfig;
import com.sunwayworld.utils.util.L;
import com.sunwayworld.utils.util.SDCardUtils;

import java.io.File;
import java.security.MessageDigest;


public class InitAppUtils {

    private static final String TAG = "InitAppUtils";
    static Context context;

    private InitAppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void init(Context c) {
        context = c;
    }


    // 利用MD5算法得到真正的密钥
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private final static String MD5(String s) {
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取密钥
     *
     * @return
     */
    public static String getDesKey() {
        SysConfig sysConfig = AppModel.getSysConfig();
        if (sysConfig == null || sysConfig.getDeskey() == null
                || "".equals(sysConfig.getDeskey())) {
            return null;
        } else {
            return MD5(sysConfig.getDeskey()).substring(0, 24);
        }
    }

    // 释放数据库连接
    public static void releaseDB() {
//        BusOpenHelperManager.releaseDb();
//        SysOpenHelperManager.releaseDb();
    }

    /***
     * 初始化用户文件夹
     ***/
    public static void initUserFileAndFolder() {
        L.v("init", "初始化用户文件夹");
        if (SDCardUtils.isSDCardEnable()) {
            PathConstants.SDCARD_USER_DATA_PATH = PathConstants.SDCARD_DATA_PATH + AppModel.getUser().getUsrnam() + "/";
            PathConstants.SDCARD_USER_PDF_PATH = PathConstants.SDCARD_DATA_PATH + AppModel.getUser().getUsrnam() + "/pdf/";
            PathConstants.SDCARD_USER_COMMENT_PATH = PathConstants.SDCARD_DATA_PATH + AppModel.getUser().getUsrnam() + "/comment/";
            File destDir = new File(PathConstants.SDCARD_USER_COMMENT_PATH);
            File pdfDir = new File(PathConstants.SDCARD_USER_PDF_PATH);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            if (!pdfDir.exists()){
                pdfDir.mkdirs();
            }
        } else {
            throw new UnsupportedOperationException("You don't have a SD card...");
        }
    }


}
