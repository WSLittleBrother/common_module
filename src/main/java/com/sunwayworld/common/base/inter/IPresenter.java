package com.sunwayworld.common.base.inter;

/**
 * 用于装载接口的装载器
 * Created by yanghan on 2016/4/13.
 */
public interface IPresenter<V> {

    //绑定
    void attachView(V view);

    //解绑
    void detachView();

}
