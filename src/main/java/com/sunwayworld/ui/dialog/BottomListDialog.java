package com.sunwayworld.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sunwayworld.common.R;
import com.sunwayworld.common.base.abstracts.CallBack;
import com.sunwayworld.ui.adapter.SimpleListAdapter;
import com.sunwayworld.ui.widget.MyDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 99351 on 2018/6/11.
 */

public class BottomListDialog extends AppCompatDialog {
    private TextView mTitle;
    private RecyclerView mRecyclerView;
    private SimpleListAdapter adapter;

    private CallBack callBack;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom_list);
        Window window=getWindow();
        WindowManager.LayoutParams lp=window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.BOTTOM;
        window.setAttributes(lp);

        iniView();
        initListener();
    }

    private void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                callBack.success(position);
                dismiss();
            }
        });
    }

    private void iniView() {
        mTitle = findViewById(R.id.title);
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new MyDecoration(mContext,MyDecoration.VERTICAL_LIST));
        adapter = new SimpleListAdapter();
        mRecyclerView.setAdapter(adapter);
    }

    public void setList(List<String> list) {
            adapter.setNewData(list);

    }

    public BottomListDialog(Context context, CallBack callBack) {
        super(context,R.style.AnimDialog);
        mContext = context;
        this.callBack  =callBack;
    }

    public BottomListDialog(Context context, int theme) {
        super(context, R.style.AnimDialog);
    }

    public void setButtonText(String tip,String first,String second){
        mTitle.setText(tip);
    }

}
