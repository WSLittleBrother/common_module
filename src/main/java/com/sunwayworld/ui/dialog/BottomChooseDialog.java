package com.sunwayworld.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sunwayworld.common.R;

/**
 * Created by 99351 on 2018/6/11.
 */

public class BottomChooseDialog extends AppCompatDialog {
    public static final int CAMERA = 0;
    public static final int ALBUM = 1;


    private TextView mTip;
    private TextView mCancel;
    private TextView mCamera;
    private TextView mAlbum;
    private Context mContext;
    private View.OnClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom_choose);
        Window window=getWindow();
        WindowManager.LayoutParams lp=window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.BOTTOM;
        window.setAttributes(lp);

        iniView();
    }

    private void iniView() {
        mCamera = findViewById(R.id.tv_up);
        mAlbum = findViewById(R.id.tv_down);
        mCancel = findViewById(R.id.cancel);
        mTip = findViewById(R.id.tip);

        mCamera.setOnClickListener(listener);
        mAlbum.setOnClickListener(listener);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public BottomChooseDialog(Context context,View.OnClickListener listener) {
        super(context,R.style.AnimDialog);
        mContext = context;
        this.listener = listener;
    }

    public BottomChooseDialog(Context context, int theme) {
        super(context, R.style.AnimDialog);
    }

    public void setButtonText(String tip,String first,String second){
        mTip.setText(tip);
        mCamera.setText(first);
        mAlbum.setText(second);
    }


    public void setButtonText(String first,String second){
        mCamera.setText(first);
        mAlbum.setText(second);
    }

}
