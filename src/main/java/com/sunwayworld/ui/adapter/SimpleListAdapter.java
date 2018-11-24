package com.sunwayworld.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sunwayworld.common.R;

/**
 * Created by 99351 on 2018/9/16.
 */

public class SimpleListAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public SimpleListAdapter() {
        super(R.layout.item_simple_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.text,item);
    }
}
