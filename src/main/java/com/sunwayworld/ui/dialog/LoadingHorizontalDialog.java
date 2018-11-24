package com.sunwayworld.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.SpriteFactory;
import com.github.ybq.android.spinkit.Style;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.sunwayworld.common.R;


/**
 * Created by 99351 on 2018/7/10.
 */

public class LoadingHorizontalDialog extends AlertDialog {
    private Sprite sprite;
    private int color;
    private Context context;
    private RelativeLayout layoutBg;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_style1);
        setCanceledOnTouchOutside(false);
        initView();
        setSprite();
    }

    /**
     * 构造方法，默认是圆形的
     *
     * @param context 上下文对象
     */
    public LoadingHorizontalDialog(@NonNull Context context) {
        super(context, R.style.CommonDialog);
        this.context = context;
    }

    /**
     * 构造方法
     *
     * @param context 上下文对象
     * @param color   颜色，为十六进制且必须是八位的，不然设置无效
     */
    public LoadingHorizontalDialog(@NonNull Context context, int color) {
        super(context, R.style.CommonDialog);
        this.color = color;
        this.context = context;
    }

    /**
     * 构造方法
     *
     * @param context 上下文对象
     * @param style   加载动画的样式，一共有14种
     */
    public LoadingHorizontalDialog(@NonNull Context context, Style style) {
        super(context, R.style.CommonDialog);
        this.context = context;
        sprite = SpriteFactory.create(style);
    }

    /**
     * 构造方法
     *
     * @param context 上下文对象
     * @param color   颜色，为十六进制且必须是八位的，不然设置无效
     * @param style   加载动画的样式，一共有14种
     */
    public LoadingHorizontalDialog(@NonNull Context context, int color, Style style) {
        super(context, R.style.CommonDialog);
        this.context = context;
        this.color = color;
        sprite = SpriteFactory.create(style);
    }

    protected LoadingHorizontalDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initView() {
        layoutBg = findViewById(R.id.layout_bg);
        textView = findViewById(R.id.textview);
    }

    public void setSprite() {
        sprite.setBounds(0, 0, 100, 100);
        sprite.setColor(Color.BLACK);
        textView.setCompoundDrawables(sprite, null, null, null);
        if (color > 0) {
            sprite.setColor(color);
        }
        sprite.start();
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(String text) {
        textView.setText(text);
    }

    public void setDrawablePadding(int padding) {
        textView.setCompoundDrawablePadding(padding);
    }

    /**
     * 此对象能直接用在布局里面去
     *
     * @return
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * 改变颜色
     *
     * @param color 这的颜色要用十六进制，并且必须是八位的，eg:0xFFFF0000 红色
     */
    public void setColor(int color) {
        sprite.setColor(color);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        sprite.setColorFilter(colorFilter);
    }

    public void setBackgroundResource(int rid) {
        layoutBg.setBackgroundResource(rid);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        sprite.stop();
    }
}
