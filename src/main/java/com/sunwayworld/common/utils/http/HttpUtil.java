package com.sunwayworld.common.utils.http;


import com.sunwayworld.common.constants.HttpConstants;
import com.sunwayworld.common.dao.model.AppModel;
import com.sunwayworld.common.utils.CompressUtils;
import com.sunwayworld.common.utils.EncoderUtils;
import com.sunwayworld.common.utils.InitAppUtils;
import com.sunwayworld.utils.util.AppUtils;
import com.sunwayworld.utils.util.DeviceUtils;
import com.sunwayworld.utils.util.PhoneUtils;

/**
 * @author yanghan ${2016/5/13}
 * @version V1.0
 * @Description 网络请求、下载数据进行加密、解密处理工具类
 */
public class HttpUtil {
    /***
     * 获取网络请求URL
     ***/
    public static String getAbsoluteUrl(String action) {
        if (action.equals(HttpConstants.ACTIVATION_ACTION)) {
            return HttpConstants.SYSCONFIG_URL + action;
        }
        if (action.equals(HttpConstants.UPDATE_ACTION)) {
            return HttpConstants.SYSCONFIG_URL + action;
        }
        int length = AppModel.getSysConfig().getBaseurl().length();
        String baseurl = AppModel.getSysConfig().getBaseurl();
        if (baseurl.substring(length - 1, length).equals("/")) {
            return AppModel.getSysConfig().getBaseurl() + "limspad" + action;
        } else
            return AppModel.getSysConfig().getBaseurl() + "/limspad" + action;
    }

    /***
     * 获取网络请求参数
     ***/
    public static HttpBuilder getParams(HttpBuilder builder) {
        if (builder != null) {
            builder.add("cond.systemvision", "Android " + android.os.Build.VERSION.RELEASE);
            builder.add("app_version", AppUtils.getAppVersionCode() + "");
            builder.add("cond.system", "Android");
            String uniquecode = PhoneUtils.getIMEI();
            String codetype;
            if (uniquecode == null) {
                uniquecode = DeviceUtils.getMacAddress();
                codetype = "MAC";
            } else {
                codetype = "IMEI";
            }
            builder.add("cond.uniquecode", uniquecode);
            builder.add("cond.codetype", codetype);
            if (AppModel.getSysConfig() != null) {
                builder.add("app_name", AppModel.getSysConfig().getTitle());
            }
        }
        return builder;
    }

    public static HttpBuilder getParamsMulti(HttpBuilder builder) {
        if (builder != null) {
            builder.addMulti("cond.systemvision", "Android " + android.os.Build.VERSION.RELEASE);
            builder.addMulti("app_version", AppUtils.getAppVersionCode() + "");
            builder.add("cond.system", "Android");
            String uniquecode = PhoneUtils.getIMEI();
            String codetype;
            if (uniquecode == null) {
                uniquecode = DeviceUtils.getMacAddress();
                codetype = "MAC";
            } else {
                codetype = "IMEI";
            }
            builder.add("cond.uniquecode", uniquecode);
            builder.add("cond.codetype", codetype);
            if (AppModel.getSysConfig() != null) {
                builder.addMulti("app_name", AppModel.getSysConfig().getTitle());
            }
        }
        return builder;
    }

    /***
     * 对返回数据进行解密
     ***/
    public static byte[] getEncbyte(byte[] bytes) {
        byte[] body = null;
        String deskey = InitAppUtils.getDesKey();
        try {
            if (AppModel.getSysConfig().isEncrypt()) {
                body = EncoderUtils.getDecString((CompressUtils.decompressStr(bytes)), deskey).getBytes();
            } else {
                body = bytes;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }
}
