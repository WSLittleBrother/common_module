package com.sunwayworld.common.components.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sunwayworld.common.R;
import com.sunwayworld.common.adapter.BaseSelectBeen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 99351 on 2018/6/6.
 */

public class BaseRecyclerViewAdapter<T> extends BaseQuickAdapter<BaseSelectBeen<T>, BaseViewHolder> {
    //这是标记选中的item的背景颜色，默认是不标记
    protected boolean isMarkClickItem = false;
    /**
     * 是否进入选择模式
     */
    protected boolean isSelectMode = false;

    public BaseRecyclerViewAdapter(int layoutid) {
        super(layoutid, null);
    }

    @Override
    protected void convert(final BaseViewHolder helper, BaseSelectBeen<T> item) {
        CheckBox checkBox = helper.getView(R.id.checkbox);
        /**
         * 只有当进入选择模式的时候才能进行选择
         */
        if (checkBox != null) {
            if (isSelectMode) {
                helper.getView(R.id.itemView).setBackgroundColor(Color.TRANSPARENT);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(item.isSelect());
            } else {
                if (isMarkClickItem) {
                    if (item.isCurrentPositon()) {
                        helper.getView(R.id.itemView).setBackgroundColor(0x5513579B);
                    } else {
                        helper.getView(R.id.itemView).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
                checkBox.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 全选和全不选
     *
     * @param b
     */
    public void selectAll(boolean b) {
        if (b) {
            for (BaseSelectBeen item : mData) {
                item.setSelect(true);
            }
        } else {
            for (BaseSelectBeen item : mData) {
                item.setSelect(false);
            }
        }

        notifyDataSetChanged();

    }

    /**
     * 得到最终选择的项
     *
     * @return 选择项
     */
    public List getSelectedItems() {
        List<T> selectedItems = new ArrayList<>();
        for (BaseSelectBeen item : mData) {
            if (item.isSelect()) {
                selectedItems.add((T) item.getData());
            }
        }
        return selectedItems;
    }

    /**
     * 反选
     */
    public void selectConverse() {
        for (BaseSelectBeen item : mData) {
            item.setSelect(!item.isSelect());
        }
        notifyDataSetChanged();
    }

    /**
     * 控制是否去标记当前点击的item的背景色
     *
     * @return
     */
    public boolean isMarkClickItem() {
        return isMarkClickItem;
    }

    public void setMarkClickItem(boolean markClickItem) {
        isMarkClickItem = markClickItem;
    }

    /**
     * 是否是可选模式
     *
     * @return 当前的可选模式状态，为false说明不是可选模式
     */
    public boolean isSelectMode() {
        return isSelectMode;
    }

    public void setSelectMode(boolean selectMode) {
        isSelectMode = selectMode;
    }

    /**
     * 此方法在进入和退出选择模式的时候被调用
     */
    public void toggleMode() {
        isSelectMode = !isSelectMode;
        for (BaseSelectBeen item : mData) {
            item.setSelect(false);
        }
        this.notifyDataSetChanged();
    }


}
