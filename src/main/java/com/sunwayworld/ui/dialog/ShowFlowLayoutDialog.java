package com.sunwayworld.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.sunwayworld.common.R;
import com.sunwayworld.ui.adapter.MyTagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 99351 on 2018/7/10.
 */

public class ShowFlowLayoutDialog {

    private AlertDialog.Builder builder;
    private Context context;
    private List<String> list;
    private TagFlowLayout flowlayout;
    private MyTagAdapter adapter;

    private onClickListener onClickListener;

    public ShowFlowLayoutDialog(Context context, List<String> list, onClickListener onClickListener) {
        this.context = context;
        this.list = list;
        this.onClickListener = onClickListener;
        createDialog();
    }

    private void createDialog() {
        if (builder == null) {
            builder = new AlertDialog.Builder(context);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_show_flow_layout, null);
        flowlayout = view.findViewById(R.id.flowlayout);
        adapter = new MyTagAdapter(context, flowlayout, list);
        flowlayout.setAdapter(adapter);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> listString = new ArrayList<>();
                List<Integer> listInt = new ArrayList<>();
                for (Integer i : flowlayout.getSelectedList()) {
                    listString.add(list.get(i));
                    listInt.add(i);
                }
                onClickListener.onClickPositive(listString);
                onClickListener.onClickPositivePosition(listInt);
            }
        });

    }

    public void setTitle(String title) {
        builder.setTitle(title);
    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public void show() {
        builder.create().show();

    }

    public abstract static class onClickListener {

        public void onClickPositive(List<String> list) {

        }

        public void onClickPositivePosition(List<Integer> list) {

        }

    }

}
