package com.sunwayworld.common.base;

import android.app.Application;

import com.sunwayworld.common.constants.PathConstants;
import com.sunwayworld.utils.util.CrashUtils;
import com.sunwayworld.utils.util.L;
import com.sunwayworld.utils.util.SPUtils;
import com.sunwayworld.utils.util.Utils;


/**
 * Author：CHENHAO
 * date：2018/5/3
 * desc：组件开发中我们的application是放在debug包下的，进行集成合并时是需要移除掉的，
 * 所以组件module中不能使用debug包下的application的context，
 * 因此组件中必须通过Utils.getContext()方法来获取全局 Context
 */

public class BaseApplication extends Application {
    private static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        init();
    }

    public static BaseApplication getApplication(){
        return application;
    }

    private void init(){
//        InitAppUtils.init(this);
        //工具类初始化
        Utils.init(this);
        //工具类： 偏好设置初始化
        SPUtils.init();
        //工具类： 异常处理初始化
        CrashUtils.getInstance().init(PathConstants.PATH);
        //工具类： Log处理初始化
        L.Builder builder = new L.Builder(PathConstants.PATH);
        builder.setBorderSwitch(false);
    }
}
