package com.sunwayworld.common.base.abstracts;

/**
 * Created by YY007 on 2018/1/5.
 * 网络请求接口回调部分代码
 */

public abstract class CallBack<T> {

    public void notifyHttpDataSetChanged() {

    }

    public void notifyHttpDataSetChanged(T t) {

    }


    public void success() {

    }

    public void success(T t) {

    }

    public void fail() {

    }

    public void fail(T t) {

    }

}
