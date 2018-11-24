package com.sunwayworld.common.components;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sunwayworld.common.R;

/**
 * Created by YY007 on 2018/1/16.
 */

public class MultiProgressDialog extends AlertDialog {

    private View view;

    public MultiProgressDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public MultiProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public MultiProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private TextView mainTitle;
    private TextView mainTv;
    private TextView mainCntTv;
    private ProgressBar mainPb;

    private TextView secondTitle;
    private TextView secondTv;
    private TextView secondCntTv;
    private ProgressBar secondPb;

    private void init() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.layout_progress, null);
        setView(view);
        mainTitle = view.findViewById(R.id.mainTitle);
        mainTv = view.findViewById(R.id.mainTv);
        mainCntTv = view.findViewById(R.id.mainCntTv);
        mainPb = view.findViewById(R.id.mainPb);

        secondTitle = view.findViewById(R.id.secondTitle);
        secondTv = view.findViewById(R.id.secondTv);
        secondCntTv = view.findViewById(R.id.secondCntTv);
        secondPb = view.findViewById(R.id.secondPb);
    }


    public void setMainProgress(String mainTv, String mainCntTv) {
        setMainTv(mainTv);
        setMainCntTv(mainCntTv);
    }

    public void setSecondProgress(String secondTv, String secondCntTv) {
        setSecondTv(secondTv);
        setSecondCntTv(secondCntTv);
    }

    public void setMainTitle(final String text) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            mainTitle.setText(text);
        } else {
            view.post(new Runnable() {
                @Override
                public void run() {
                    mainTitle.setText(text);
                }
            });
        }
    }

    public void setMainTv(final String text) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            mainTv.setText(text);
        } else {
            view.post(new Runnable() {
                @Override
                public void run() {
                    mainTv.setText(text);
                }
            });
        }
    }

    public void setMainCntTv(final String text) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            mainCntTv.setText(text);
        } else {
            view.post(new Runnable() {
                @Override
                public void run() {
                    mainCntTv.setText(text);
                }
            });
        }
    }

    public void setMainPbProgress(int progress) {
        this.mainPb.setProgress(progress);
    }

    public void setMainPbMax(int max) {
        mainPb.setMax(max);
    }

    public void setMainPb(int max, int progress) {
        mainPb.setMax(max);
        mainPb.setProgress(progress);
    }

    public void setSecondTitle(final String text) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            secondTitle.setText(text);
        } else {
            view.post(new Runnable() {
                @Override
                public void run() {
                    secondTitle.setText(text);
                }
            });
        }
    }

    public void setSecondTv(final String text) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            secondTv.setText(text);
        } else {
            view.post(new Runnable() {
                @Override
                public void run() {
                    secondTv.setText(text);
                }
            });
        }
    }

    public void setSecondCntTv(final String text) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            secondCntTv.setText(text);
        } else {
            view.post(new Runnable() {
                @Override
                public void run() {
                    secondCntTv.setText(text);
                }
            });
        }
    }

    public void setSecondPbProgress(int progress) {
        this.secondPb.setProgress(progress);
    }

    public void setSecondPbMax(int max) {
        this.secondPb.setMax(max);
    }

    public void setSecondPb(int max, int progress) {
        this.secondPb.setMax(max);
        this.secondPb.setProgress(progress);
    }

}
