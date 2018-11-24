package com.sunwayworld.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sunwayworld.common.base.inter.IPresenter;

import java.lang.ref.WeakReference;

/**
 * Created by YY007 on 2017/12/28.
 */

public abstract class BasePresenter<V> implements IPresenter<V> {
    public final String TAG = BasePresenter.class.getSimpleName();

    protected V mIView;

    protected Context context;
    //定义弱引用接口
    private WeakReference<V> weakReference;


    @Override
    public void attachView(V view) {
        weakReference = new WeakReference<V>(view);
        mIView = weakReference.get();
        init();
    }

    //初始化部分项方法
    public abstract void init();

    @Override
    public void detachView() {
        if (weakReference != null) {
            weakReference.clear();
            mIView = null;
        }
    }

    protected void handlerIntent(Intent intent) {

    }

    protected void handlerBundle(Bundle bundle) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
