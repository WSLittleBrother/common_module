package com.sunwayworld.common.adapter;

import java.io.Serializable;

/**
 * Created by WS on 2018/1/20.
 */

public class BaseSelectBeen<T> implements Serializable {
    /**
     * 数据
     */
    private T data;
    /**
     * 标记CHeckBox的选中与否
     */
    private boolean isSelect;
    /**
     * 标记当前点中的位置
     */
    private boolean isCurrentPositon;


    public BaseSelectBeen() {

    }

    public BaseSelectBeen(T data, boolean isSelect) {
        this.data = data;
        this.isSelect = isSelect;
    }

    public BaseSelectBeen(T data) {
        this.data = data;
    }

    public boolean isCurrentPositon() {
        return isCurrentPositon;
    }

    public void setCurrentPositon(boolean currentPositon) {
        isCurrentPositon = currentPositon;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
