package com.sunwayworld.utils.util;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;

public class ContextUtils {

    /**
     * 判断当前上下文context是否存在
     */
    public static boolean isContextExisted(Context context) {
        if (context != null) {
            if (context instanceof Activity) {
                return !((Activity) context).isFinishing();
            } else if (context instanceof Service) {
                return ServiceUtils.isServiceRunning(context, context.getClass().getName());
            } else if (context instanceof Application) {
                return true;
            }
        }
        return false;
    }

}
