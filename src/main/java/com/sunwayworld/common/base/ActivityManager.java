package com.sunwayworld.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sunwayworld.utils.util.T;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lan on 2017/6/22.
 * 管理Activity
 */
public class ActivityManager {
    //存储Activity的Stack
    private static Stack<Activity> activityStack = new Stack<Activity>();
    //单例模式
    private static ActivityManager instance;
    private static Boolean isExit = false;

    private ActivityManager() {

    }

    /**
     * @return 通过单例获取
     */
    public static ActivityManager getActivityMar() {
        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                }
            }
        }
        return instance;
    }

    /**
     * @param activity 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * @param activity 移除指定的Activity对象
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束掉所有的Activity
     */
    public void finishAllActivity() {
        if (null != activityStack && activityStack.size() > 0) {
            for (int i = 0; i < activityStack.size(); i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * @return 获得当前的Activity
     */
    public Activity currentActivity() {
        if (!activityStack.empty()) {
            return activityStack.lastElement();
        }
        return null;
    }

    /**
     * 退出栈中所有Activity
     *
     * @param cls
     * @return void
     */
    public void exitApp(Class<?> cls) {
        while (true) {
            Activity activity = currentActivity();
            if (null == activity) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            finishActivity(activity);
        }
        System.gc();
        System.exit(0);
    }

    /**
     * 退出App程序应用
     *
     * @param context 上下文
     * @return boolean True退出|False提示
     */
    public void exitApp(Context context) {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;
            //信息提示
            T.showShort("再按一次后退键退出应用程序");
            //创建定时器
            tExit = new Timer();
            //如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    //取消退出
                    isExit = false;
                }
            }, 2000);
        } else {
            finishAllActivity();
            //创建ACTION_MAIN
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Context content = ((Activity) context);
            //启动ACTION_MAIN
            content.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public void exitAppMethod(Context context) {
        finishAllActivity();
        //创建ACTION_MAIN
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Context content = ((Activity) context);
        //启动ACTION_MAIN
        content.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
