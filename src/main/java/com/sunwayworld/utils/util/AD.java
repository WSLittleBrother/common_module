package com.sunwayworld.utils.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * 功能说明：
 * 作者：书杰
 * 日期：2017/2/14
 */
public class AD {

    public AD() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static AlertDialog.Builder builder;
    private static OnClickEventListener mOnClickEventListener;

    public static AlertDialog.Builder getADBuilder(Context context, String msg) {
        return getADBuilder(context, null, msg, null, null, null);
    }

    public static AlertDialog.Builder getADBuilder(Context context, OnClickEventListener mOnClickEventListener) {
        return getADBuilder(context, null, mOnClickEventListener);
    }

    public static AlertDialog.Builder getADBuilder(Context context, String title, String msg) {
        return getADBuilder(context, title, msg, null, null, null, null);
    }

    /**
     * 该方法与getADBuilder(Context context, String title, OnClickEventListener mOnClickEventListener)方法中有冲突
     * 可直接调用getADBuilder(Context context, OnClickEventListener mOnClickEventListener) 方法，然后在setTitle()设置标题
     *
     * @param msg 显示信息
     */
    public static AlertDialog.Builder getADBuilder(Context context, String msg, OnClickEventListener mOnClickEventListener) {
        if (mOnClickEventListener != null) {
            return getADBuilder(context, null, msg, "确认", null, "取消", mOnClickEventListener);
        } else {
            return getADBuilder(context, null, msg, null, null, null);
        }
    }

    public static AlertDialog.Builder getADBuilder(Context context, String title, String msg, OnClickEventListener mOnClickEventListener) {
        if (mOnClickEventListener != null) {
            return getADBuilder(context, title, msg, "确认", null, "取消", mOnClickEventListener);
        } else {
            return getADBuilder(context, title, msg, null, null, null);
        }
    }

    /**
     * 该方法与getADBuilder(Context context, String title, String positive, String negative, OnClickEventListener listener)方法中有冲突
     * 可直接调用getADBuilder(Context context, null, String positive, String negative, OnClickEventListener listener) 方法，然后在setTitle()设置标题
     *
     * @param msg 显示信息
     */
    public static AlertDialog.Builder getADBuilder(Context context, String msg, String positive, String negative, OnClickEventListener listener) {
        return getADBuilder(context, null, msg, positive, null, negative, listener);
    }

    /**
     * 该方法与getADBuilder(Context context, String title, String positive,String neutral,  String negative, OnClickEventListener listener)方法中有冲突
     * 可直接调用getADBuilder(Context context, null, String positive,String neutral,  String negative, OnClickEventListener listener) 方法，然后在setTitle()设置标题
     *
     * @param msg 显示信息
     */
    public static AlertDialog.Builder getADBuilder(Context context, String msg, String positive, String neutral, String negative, OnClickEventListener listener) {
        return getADBuilder(context, null, msg, positive, neutral, negative, listener);
    }

    public static AlertDialog.Builder getADBuilder(final Context context, String title, String msg, String positive, String neutral, String negative, final OnClickEventListener listener) {
        setOnClickEventListener(listener);

        builder = new AlertDialog.Builder(context).setTitle(TextUtils.isEmpty(title) ? "提示" : title);
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        if (listener != null) {
            if (!TextUtils.isEmpty(positive) && !TextUtils.isEmpty(negative)) {
                setDoubleDialog(positive, negative);
            }
            if (!TextUtils.isEmpty(neutral)) {
                builder.setNeutralButton(neutral, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onClickNeutral(dialog, which);
                    }
                });
            }
        }
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                KeyboardUtils.hideSoftInput((Activity) context);
            }
        });
        return builder;
    }

    private static void setDoubleDialog(String positive, String negative) {
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mOnClickEventListener != null)
                    mOnClickEventListener.onClickPositive(dialog, which);
            }
        });

        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mOnClickEventListener != null)
                    mOnClickEventListener.onClickNegative(dialog, which);
            }
        });
    }

    private static void setOnClickEventListener(OnClickEventListener listener) {
        mOnClickEventListener = listener;
    }

    /**
     * 点击事件接口
     */
    public static abstract class OnClickEventListener {

        /**
         * 一般确定按钮
         */
        public void onClickPositive(DialogInterface dialog, int which) {
            dialog.dismiss();
        }

        /**
         * 可选择性重写方法，一般取消按钮
         *
         * @param dialog
         * @param which
         */
        public void onClickNegative(DialogInterface dialog, int which) {
            dialog.dismiss();
        }

        /**
         * 可选择性重写方法
         *
         * @param dialog
         * @param which
         */
        public void onClickNeutral(DialogInterface dialog, int which) {
            dialog.dismiss();
        }

        /**
         * 一般确定按钮
         */
        public void onClickPositive(DialogInterface dialog, boolean[] whichs) {
            dialog.dismiss();
        }
    }


    public static AlertDialog.Builder getADMultiChooseBuilder(Context context, String title, String[] items, String positive, String negative, final OnClickEventListener listener) {
        setOnClickEventListener(listener);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final boolean[] choiceArr = new boolean[items.length];
        for (int i = 0; i < items.length; i++) {
            choiceArr[i] = false;
        }
        builder.setMultiChoiceItems(items, choiceArr, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                choiceArr[which] = isChecked;
            }
        });
        builder.setTitle(title).setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.onClickPositive(dialog, choiceArr);
            }
        });
        builder.setTitle(title).setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.onClickNegative(dialog, which);
            }
        });
        return builder;
    }

    public static void showMultiChooseDialog(Context context, String title, String[] items, String positive, String negative, final OnClickEventListener listener) {
        getADMultiChooseBuilder(context, title, items, positive, negative, listener).create().show();
    }

    public static void showMultiChooseDialog(Context context, String title, String[] items, final OnClickEventListener listener) {
        showMultiChooseDialog(context, title, items, "确定", "取消", listener);
    }


    public static void showSingleChooseDialog(Context context, String title, String[] items, final OnClickEventListener listener) {
        showSingleChooseDialog(context, title, items, true, listener);
    }

    public static AlertDialog.Builder getADSingleChooseBuilder(Context context, String title, String[] items, String positive, String negative, boolean showCheckBox, final OnClickEventListener listener) {
        setOnClickEventListener(listener);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final int[] selectItem = {-1};
        if (showCheckBox) {
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectItem[0] = which;
                }
            });
        } else {
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectItem[0] = which;
                    if (selectItem[0] != -1 && listener != null) {
                        listener.onClickPositive(dialog, selectItem[0]);
                    }
                }
            });
        }
        builder.setTitle(title).setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectItem[0] != -1 && listener != null) {
                    listener.onClickPositive(dialog, selectItem[0]);
                }
            }
        }).setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.onClickNegative(dialog, which);
            }
        });
        return builder;
    }


    public static void showSingleChooseDialog(Context context, String title, String[] items, final boolean showCheckBox, final OnClickEventListener listener) {
        getADSingleChooseBuilder(context, title, items, "确定", "取消", showCheckBox, listener).create().show();
    }

    /**
     * 获取日期的选择器
     */
    public static void showDatePickerDialog(Context context, int themeResId, final OnDateOrTimePickerListener listener) {
        Calendar calendar = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(context, themeResId, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int monthofyear = monthOfYear + 1;
                // 此处得到选择的时间
                listener.onSelected(year + "-" + monthofyear + "-" + dayOfMonth, year, monthofyear, dayOfMonth);
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * 获取时间的选择器
     */
    public static void showTimePickerDialog(Context context, final OnDateOrTimePickerListener listener) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (listener != null)
                    listener.onSelected(hourOfDay + ":" + minute, hourOfDay, minute);
            }
        }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
    }

    public abstract static class OnDateOrTimePickerListener {
        public void onSelected(String select, int year, int month, int day) {

        }

        public void onSelected(String select, int hour, int minute) {

        }
    }
}
