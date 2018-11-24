package com.sunwayworld.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.sunwayworld.common.R;


/**
 * Created by 99351 on 2018/7/10.
 */

public class LoadingWhorlDialog extends AlertDialog {
    private TextView tvTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_whorl);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        tvTip = findViewById(R.id.tv_tip);
    }

    public void setTvTip(TextView tvTip) {
        this.tvTip = tvTip;
    }

    public LoadingWhorlDialog(@NonNull Context context) {
        super(context, R.style.CommonDialog);
    }

    protected LoadingWhorlDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingWhorlDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
