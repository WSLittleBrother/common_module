package com.sunwayworld.utils.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/04/16
 *     desc  : 权限相关工具类
 * </pre>
 */
public final class PermissionUtils {

    private static int mRequestCode = -1;

    private static OnPermissionListener mOnPermissionListener;

    public interface OnPermissionListener {

        void onPermissionGranted();

        void onPermissionDenied(String[] deniedPermissions);
    }

    public abstract static class RationaleHandler {
        private Context context;
        private int requestCode;
        private String[] permissions;

        protected abstract void showRationale();

        void showRationale(Context context, int requestCode, String[] permissions) {
            this.context = context;
            this.requestCode = requestCode;
            this.permissions = permissions;
            showRationale();
        }

        @TargetApi(Build.VERSION_CODES.M)
        public void requestPermissionsAgain() {
            ((Activity) context).requestPermissions(permissions, requestCode);
        }
    }

    @Deprecated
    @TargetApi(Build.VERSION_CODES.M)
    public static void checkPermission(Context context, int requestCode
            , String permission) {
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissions(Context context, int requestCode
            , String[] permissions, OnPermissionListener listener) {
        requestPermissions(context, requestCode, permissions, listener, null);
    }

    /**
     * 请求权限
     *
     * @param context     上下文
     * @param requestCode 请求码
     * @param listener    请求监听
     * @param handler     权限被拒绝后，则走该方法，可不定义，则不断请求权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissions(Context context, int requestCode
            , String[] permissions, OnPermissionListener listener, RationaleHandler handler) {
        if (context instanceof Activity) {
            mRequestCode = requestCode;
            mOnPermissionListener = listener;
            String[] deniedPermissions = getDeniedPermissions(context, permissions);
            if (deniedPermissions.length > 0) {
                boolean rationale = shouldShowRequestPermissionRationale(context, deniedPermissions);
                if (rationale && handler != null) {
                    handler.showRationale(context, requestCode, deniedPermissions);
                } else {
                    ((Activity) context).requestPermissions(deniedPermissions, requestCode);
                }
            } else {
                if (mOnPermissionListener != null)
                    mOnPermissionListener.onPermissionGranted();
            }
        } else {
            throw new RuntimeException("Context must be an Activity");
        }
    }

    /**
     * 请求权限结果，对应Activity中onRequestPermissionsResult()方法。
     * <p>
     * 在Activity中调用PermissionUtils.onRequestPermissionsResult(this,requestCode,permissions);
     */
    public static void onRequestPermissionsResult(Activity context, int requestCode, String[] permissions, int[] grantResults) {
        if (mRequestCode != -1 && requestCode == mRequestCode) {
            if (mOnPermissionListener != null) {
                String[] deniedPermissions = getDeniedPermissions(context, permissions);
                if (deniedPermissions.length > 0) {
                    mOnPermissionListener.onPermissionDenied(deniedPermissions);
                } else {
                    mOnPermissionListener.onPermissionGranted();
                }
            }
        }
    }

    /**
     * 获取请求权限中需要授权的权限
     */
    private static String[] getDeniedPermissions(Context context, String[] permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions.toArray(new String[deniedPermissions.size()]);
    }

    /**
     * 是否彻底拒绝了某项权限；彻底拒绝返回为true，否则为false
     *
     * @param context           上下文
     * @param deniedPermissions 被拒绝的权限
     */
    public static boolean hasAlwaysDeniedPermission(Context context, String... deniedPermissions) {
        for (String deniedPermission : deniedPermissions) {
            if (!shouldShowRequestPermissionRationale(context, deniedPermission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否有权限需要说明提示
     */
    private static boolean shouldShowRequestPermissionRationale(Context context, String... deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
        boolean rationale;
        for (String permission : deniedPermissions) {
            rationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission);
            if (rationale) return true;
        }
        return false;
    }

    /**
     * 打开权限设置界面，手动开启权限
     *
     * @param context     上下文
     * @param requestCode 请求码
     */
    public static void openSetting(Context context, int requestCode) {
        String model = DeviceUtils.getManufacturer();
        if (TextUtils.isEmpty(model)) {
            ((Activity) context).finish();
        } else if ("MIUI".equals(model)) {
            Intent intent = new Intent(
                    "miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            ((Activity) context).startActivityForResult(intent, requestCode);
        }
    }
}