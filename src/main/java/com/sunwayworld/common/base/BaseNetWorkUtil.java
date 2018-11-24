package com.sunwayworld.common.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;

import com.sunwayworld.common.base.abstracts.CallBack;
import com.sunwayworld.common.components.MultiProgressDialog;
import com.sunwayworld.common.utils.retrofit2.http.HttpApiBase;
import com.sunwayworld.utils.util.T;

/**
 * Created by YY007 on 2018/1/5.
 */

public class BaseNetWorkUtil {
    public final String TAG = BaseNetWorkUtil.class.getSimpleName();

    protected ProgressDialog pd;
    protected MultiProgressDialog mPd;
    protected HttpApiBase httpApiBase;
    /**
     * 接口
     */
    protected CallBack callBack;
    /**
     * 意图
     */
    protected Context context;

    public BaseNetWorkUtil(Context context) {
        this.context = context;
        init();
    }

    public BaseNetWorkUtil(CallBack callBack) {
        this.callBack = callBack;
        init();
    }

    public BaseNetWorkUtil(Context context, CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        init();
    }

    protected void init() {
        httpApiBase = HttpApiBase.getInstance();
    }


    /**
     * 显示ProgerssDialog，带进度条
     */
    public void showProgerssDialog() {
        showProgerssDialog(ProgressDialog.STYLE_HORIZONTAL);
    }

    /**
     * 显示ProgerssDialog
     */
    public void showProgerssDialog(int style) {
        if (null == pd) {
            pd = new ProgressDialog(context);
        }
        pd.setProgressStyle(style);
        // 设置ProgressDialog 是否可以按退回按键取消
        pd.setCancelable(false);
        pd.setMax(100);
        pd.setProgress(0);
        pd.setTitle("正在请求网络...");
        pd.setButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                httpApiBase.cancelDownload();
                dialog.dismiss();
                T.showShort("已取消请求！");
            }
        });
//        pd.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                dismissProgerssDialog();
//            }
//        });
        pd.show();
    }

    public void setPdTitle(final String text) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            pd.setTitle(text);
        } else {
            if (pd.getWindow() != null) {
                pd.getWindow().getDecorView().post(new Runnable() {
                    @Override
                    public void run() {
                        pd.setTitle(text);
                    }
                });
            }
        }
    }

    /**
     * 显示复杂ProgerssDialog
     */
    public void showMultiProgressDialog() {
        if (null == mPd) {
            mPd = new MultiProgressDialog(context);
        }
        // 设置ProgressDialog 是否可以按退回按键取消
        mPd.setCancelable(false);
        mPd.setMainPbMax(100);
        mPd.setSecondPbMax(100);
        mPd.setTitle("正在请求网络...");
        mPd.setButton(DialogInterface.BUTTON_POSITIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                httpApiBase.cancelDownload();
                dialog.dismiss();
                T.showShort("已取消请求！");
            }
        });
        mPd.show();
    }

    /**
     * 隐藏ProgerssDialog
     */
    public void dismissProgerssDialog() {
        dismissProgerssDialog(false);
    }


    /**
     * 隐藏ProgerssDialog
     *
     * @param needRefersh 是否需要回调刷新Activity中数据，默认为false
     */
    public void dismissMultiProgerssDialog(boolean needRefersh) {
        if (mPd != null) {
            mPd.dismiss();
            mPd = null;
        }
        if (needRefersh && callBack != null) {
            callBack.notifyHttpDataSetChanged();
        }
    }

    /**
     * 隐藏ProgerssDialog
     */
    public void dismissMultiProgerssDialog() {
        dismissMultiProgerssDialog(false);
    }


    /**
     * 隐藏ProgerssDialog
     *
     * @param needRefersh 是否需要回调刷新Activity中数据，默认为false
     */
    public void dismissProgerssDialog(boolean needRefersh) {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
        if (needRefersh && callBack != null) {
            callBack.notifyHttpDataSetChanged();
        }
    }
}
