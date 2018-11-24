package com.sunwayworld.common.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.sunwayworld.common.R;
import com.sunwayworld.common.constants.PathConstants;
import com.sunwayworld.common.dao.model.UpdateInfo;
import com.sunwayworld.common.utils.retrofit2.http.HttpApiBase;
import com.sunwayworld.utils.util.AppUtils;
import com.sunwayworld.utils.util.IntentUtils;
import com.sunwayworld.utils.util.L;
import com.sunwayworld.utils.util.Utils;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.ResponseBody;


/**
 * Created by 99351 on 2018/6/10.
 */

public class InstallApkService extends Service {
    private NotificationManager manager;

    //安装APK
    public static final int INSTALL_APK_CODE = 0;
    //下载Apk失败
    public static final int DOWNLOAD_APK_FAIL_CODE = 1;
    //开始下载APK
    public static final int START_DOWNLOAD_APK = 2;
    //取消下载
    public static final int CANCEL_DOWNLOAD_APK = 3;

    private InstallBinder binder = new InstallBinder();
    //网络请求对象
    private HttpApiBase httpApiBase;
    //更新Apk的信息
    private UpdateInfo updateInfo;
    //Progress进度旧的
    private int oldProgress = 0;

    private NotificationCompat.Builder builder;

    @Override
    public void onCreate() {
        super.onCreate();
        httpApiBase = HttpApiBase.getInstance();
        initNotification();
    }

    private void initNotification() {
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = AppUtils.getAppPackageName(Utils.getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name="应用更新";
            NotificationChannel mChannel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(mChannel);
        }
        builder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.v2_logo_light))
                /**设置通知右边的小图标**/
                .setSmallIcon(R.drawable.v2_logo_light)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("即将下载新版本")
                .setContentText("请稍后...")
                .setChannelId(channelId);
        manager.notify(1, builder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * 如果使用startService的方式开始Service，那么首先会调用这里的方法，我们将在这里进行Apk的下载
     * @param intent  传递过来的intent,可以通过intent进行值的传递
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra("bundle");
        int code = -1;
        if (bundle != null && bundle.containsKey("code")) {
            code = bundle.getInt("code");
            switch (code) {
                case INSTALL_APK_CODE:
                    Intent installIntent = IntentUtils.openFile(getUpdateApkPath(updateInfo));
                    startActivity(installIntent);
                    break;
                case DOWNLOAD_APK_FAIL_CODE:
                    downloadApk(updateInfo);
                    break;

                case START_DOWNLOAD_APK:
                    updateInfo = (UpdateInfo) bundle.getSerializable("updateInfo");
                    downloadApk(updateInfo);
                    break;
                case CANCEL_DOWNLOAD_APK:
                    httpApiBase.cancelDownload();
                    manager.cancelAll();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 销毁服务
     */
    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();
    }

    /**
     * 通过绑定模式进行交互，不过下载APK不需要交互，这里没有使用
     */
    class InstallBinder extends Binder {
        public InstallApkService getInstallApkService() {
            return InstallApkService.this;
        }

    }

    /**
     * 下载Apk
     * @param updateInfo
     */
    private void downloadApk(final UpdateInfo updateInfo) {

        httpApiBase.ApiSubscrbe(httpApiBase.getData(updateInfo.getUpdateurl()), new HttpApiBase.ProgressObserver() {
            @Override
            public Object handlerData(ResponseBody responseBody) throws Exception {
                String path = getUpdateApkPath(updateInfo);
                File file = new File(path);
                if (file.exists()) {
                    file.deleteOnExit();
                }
                FileOutputStream fos = null;
                fos = new FileOutputStream(file);
                fos.write(responseBody.bytes());
                fos.flush();
                fos.close();
                return path;
            }

            @Override
            public void onNext(Object o) {
                if (o instanceof String) {
                    final String path = (String) o;
                    if (TextUtils.isEmpty(path)) {
                        builder.setContentTitle("没有找到安装包，请重试或手动下载.  ")
                                .setContentText("重试").setContentIntent(sendMessage(DOWNLOAD_APK_FAIL_CODE));
                        manager.notify(1, builder.build());
                    } else {
                        Intent i = IntentUtils.openFile(path);
                        if (null == i) {
                            builder.setContentTitle("没有找到安装包，请重试或手动下载.  ")
                                    .setContentText("重试").setContentIntent(sendMessage(DOWNLOAD_APK_FAIL_CODE));
                            manager.notify(1, builder.build());
                        } else {
                            builder.setContentTitle("下载完成")
                                    .setContentText("点击安装")
                                    .setProgress(100, 100, false).setContentIntent(sendMessage(INSTALL_APK_CODE));
                            manager.notify(1, builder.build());
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                builder.setContentTitle("下载失败，请重试或手动下载.  ")
                        .setContentText("重试").setContentIntent(sendMessage(DOWNLOAD_APK_FAIL_CODE));
                manager.notify(1, builder.build());
            }

            @Override
            public void downloadProgress(long bytesRead, long contentLength, boolean done, long networkSpeed) {
                L.d("bytesRead：" + bytesRead + "-------contentLength：" + contentLength + "------progress：" + bytesRead * 100 / contentLength);
                int progress = (int) (bytesRead * 100 / contentLength);
                if (progress > oldProgress) {
                    if (progress < 100) {
                        builder.setContentTitle("当前进度  " + progress + "%")
                                .setContentText("正在下载,点击取消下载").setContentIntent(sendMessage(CANCEL_DOWNLOAD_APK))
                                .setProgress(100, progress, false);
                        manager.notify(1, builder.build());
                    } else {
                        builder.setContentTitle("下载完成")
                                .setContentText("点击安装")
                                .setProgress(100, progress, false);
                        manager.notify(1, builder.build());
                    }
                }
            }
        });
    }

    /**
     * 获得APK路径
     * @param ui
     * @return
     */
    private String getUpdateApkPath(UpdateInfo ui) {
        return PathConstants.SDCARD_UPDATE_PATH + ui.getVersion() + ".apk";
    }

    /**
     * Notification的点击事件处理
     * @param code 请求代码
     * @return PendingIntent对象
     */
    private PendingIntent sendMessage(int code) {
        Intent intent = new Intent(InstallApkService.this, InstallApkService.class);
        Bundle bundle = new Bundle();
        bundle.putInt("code", code);
        intent.putExtras(bundle);
        intent.putExtra("bundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

}
