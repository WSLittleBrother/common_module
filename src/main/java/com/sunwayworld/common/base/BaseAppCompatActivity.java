package com.sunwayworld.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.alibaba.android.arouter.launcher.ARouter;


/**
 * Created by Yanghan on 2017/12/28.
 * 所有Activity的基类
 * 生命周期：onCreate->onStart->onResume->onPause->onStop->onDestory
 * T：需要实现的Presenter
 * V：接口
 */

public abstract class BaseAppCompatActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
    public final String TAG = BaseAppCompatActivity.class.getSimpleName();

    protected T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("TAG", this.getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        presenter = onCreatePresenter();
        if (presenter != null) {
            presenter.attachView((V) this);
        }
        handlerIntent(getIntent());

        //把Activity添加到集合，统一管理
        ActivityManager.getActivityMar().addActivity(this);
    }

    protected void handlerIntent(Intent intent) {
        if (presenter != null) {
            presenter.handlerIntent(getIntent());
        }
    }

    /*
    * 如若使用MVP模式，则必须实现presenter层
    * */
    protected abstract T onCreatePresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
        //Activity销毁时，从集合移除
        ActivityManager.getActivityMar().finishActivity(this);
    }

    /**
     * @Description: 响应按键事件
     * @parameters @param keyCode
     * @parameters @param event
     * @returns
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            onRunBack();
        }
        return false;
    }

    /**
     * @Description: 编写返回事件
     */
    public abstract void onRunBack();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (presenter != null) {
            presenter.onActivityResult(requestCode, resultCode, data);
        }
    }
}
