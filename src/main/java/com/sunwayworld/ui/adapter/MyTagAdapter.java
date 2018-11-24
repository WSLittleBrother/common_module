package com.sunwayworld.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sunwayworld.common.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Created by 99351 on 2018/7/25.
 */

public class MyTagAdapter extends TagAdapter<String> {
    private Context context;
    private List<String> datas;
    private TagFlowLayout mFlowLayout;

    public MyTagAdapter(Context context, TagFlowLayout mFlowLayout, List<String> datas) {
        super(datas);
        this.context = context;
        this.datas = datas;
        this.mFlowLayout = mFlowLayout;
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.textview_flow,mFlowLayout, false);
        tv.setText(s);
        return tv;
    }
}
