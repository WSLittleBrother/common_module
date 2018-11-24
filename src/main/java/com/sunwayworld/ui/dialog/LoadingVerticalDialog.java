package com.sunwayworld.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.SpriteFactory;
import com.github.ybq.android.spinkit.Style;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.Pulse;
import com.github.ybq.android.spinkit.style.RotatingCircle;
import com.github.ybq.android.spinkit.style.RotatingPlane;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.github.ybq.android.spinkit.style.Wave;
import com.sunwayworld.common.R;

/**
 * Created by 99351 on 2018/7/10.
 */

public class LoadingVerticalDialog extends AlertDialog {
    private TextView tvTip;
    private SpinKitView progressBar;
    private Sprite sprite;
    private int color;
    private Context context;
    private RelativeLayout layoutBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_style);
        setCanceledOnTouchOutside(false);
        initView();
        setSprite();
    }

    /**
     * 构造方法，默认是圆形的
     *
     * @param context 上下文对象
     */
    public LoadingVerticalDialog(@NonNull Context context) {
        super(context, R.style.CommonDialog);
        this.context = context;
    }

    /**
     * 构造方法
     *
     * @param context 上下文对象
     * @param color   颜色，为十六进制且必须是八位的，不然设置无效
     */
    public LoadingVerticalDialog(@NonNull Context context, int color) {
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
    public LoadingVerticalDialog(@NonNull Context context, Style style) {
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
    public LoadingVerticalDialog(@NonNull Context context, int color, Style style) {
        super(context, R.style.CommonDialog);
        this.context = context;
        this.color = color;
        sprite = SpriteFactory.create(style);
    }

    protected LoadingVerticalDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initView() {
        progressBar = findViewById(R.id.spin_kit);
        tvTip = findViewById(R.id.tv_tip);
        if (sprite == null) {
            sprite = progressBar.getIndeterminateDrawable();
        }
        layoutBg = findViewById(R.id.layout_bg);
    }

    private void setSprite() {
        sprite.setColor(color);
        progressBar.setIndeterminateDrawable(sprite);
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

    /**
     * 设置提示信息
     *
     * @param tvTip
     */
    public void setTvTip(TextView tvTip) {
        this.tvTip = tvTip;
    }

    /**
     * 这里传递的是资源文件加的id，之所以不直接传颜色，是因为这样会显示
     * 背景是方形的，所以需要自己写一个shape给背景画个圆角
     *
     * @param rid
     */
    public void setBackgroundResource(int rid) {
        layoutBg.setBackgroundResource(rid);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        sprite.stop();
    }
}
