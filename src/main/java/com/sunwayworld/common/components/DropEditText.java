package com.sunwayworld.common.components;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.sunwayworld.common.R;

import java.util.Calendar;
import java.util.List;

/**
 * 可下拉可输入的EditText
 * Created by yanghan on 2017/6/2.
 */

public class DropEditText extends LinearLayout implements View.OnClickListener, TextWatcher {
    private EditText mEditText;
    private TextView mTextView;
    private TextView mTextViewRight;
    private ImageView mImageView;
    private ListPopupWindow mPopView;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private OnTextChangeListener onTextChangeListener;
    private OnDropClickListener onDropClickListener;
    private OnDropDateOrTimePickerListener onDropDateOrTimePickerListener;

    public final static int DatePicker = 1;
    public final static int DatePicker_NoDay = 2;
    public final static int DatePicker_CLEAR = 3;
    public final static int DatePicker_NoDay_CLEAR = 4;
    public final static int DatePicker_NoDay_NOCLEAR = 5;

    public final static int TimePicker = 6;
    private int picker = 0;
    private int year, month, day, hour, minute;

    private String et_hint;
    private String tv_text;
    private int tv_color;
    private float tv_textSize;
    private float et_textSize;
    private float textSize;
    private int iv_src;
    private int dropWidth;
    private boolean dropMode;
    private boolean et_enable;
    private boolean et_focusable;
    private boolean tv_visible;
    private int et_background;
    private List objectList;
    private int position = -1;

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener;
    }

    public void setOnDropClickListener(OnDropClickListener onDropClickListener) {
        this.onDropClickListener = onDropClickListener;
    }

    public void setOnDropDateOrTimePickerListener(OnDropDateOrTimePickerListener onDropDateOrTimePickerListener, int picker) {
        this.onDropDateOrTimePickerListener = onDropDateOrTimePickerListener;
        this.picker = picker;
    }

    public void setOnDropDateOrTimePickerListener(OnDropDateOrTimePickerListener onDropDateOrTimePickerListener, int picker, int year, int month, int day) {
        this.onDropDateOrTimePickerListener = onDropDateOrTimePickerListener;
        this.picker = picker;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setOnDropDateOrTimePickerListener(OnDropDateOrTimePickerListener onDropDateOrTimePickerListener, int picker, int hour, int minute) {
        this.onDropDateOrTimePickerListener = onDropDateOrTimePickerListener;
        this.picker = picker;
        this.hour = hour;
        this.minute = minute;
    }

    public DropEditText(Context context) {
        this(context, null, 0);
    }

    public DropEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public DropEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DropEditText, defStyleAttr, 0);
        iv_src = typedArray.getResourceId(R.styleable.DropEditText_dropSrc, R.drawable.arrow_down);
        dropMode = typedArray.getBoolean(R.styleable.DropEditText_dropMode, false);//设置是否为下拉选择
        et_enable = typedArray.getBoolean(R.styleable.DropEditText_et_enable, true);
        et_focusable = typedArray.getBoolean(R.styleable.DropEditText_et_focusable, true);
        et_background = typedArray.getResourceId(R.styleable.DropEditText_et_background, 0);//设置edittext的背景
        et_hint = typedArray.getString(R.styleable.DropEditText_et_hint);
        et_textSize = typedArray.getDimension(R.styleable.DropEditText_et_textSize, 0);
        tv_text = typedArray.getString(R.styleable.DropEditText_tv_text);
        tv_visible = typedArray.getBoolean(R.styleable.DropEditText_tv_visible, true);
        tv_color = typedArray.getColor(R.styleable.DropEditText_tv_color, getResources().getColor(android.R.color.black));//设置textview的字体颜色
        tv_textSize = typedArray.getDimension(R.styleable.DropEditText_tv_textSize, 0);
        textSize = typedArray.getDimension(R.styleable.DropEditText_textSize, getResources().getDimension(R.dimen.xfont));
        dropWidth = typedArray.getInteger(R.styleable.DropEditText_dropWidth, 0);//设置弹出的宽度，默认为edittext控件的宽度
        typedArray.recycle();
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DropEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.drop_edittext, this, true);
        setOrientation(LinearLayout.HORIZONTAL);
        setBaselineAligned(false);

//        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        FrameLayout.LayoutParams ivparams = new FrameLayout.LayoutParams(this.getMeasuredHeight(), this.getMeasuredHeight());
//        mTextView = new TextView(context);
//        textParams.gravity = Gravity.CENTER_VERTICAL;
//        FrameLayout frameLayout = new FrameLayout(context);
//        mEditText = new EditText(context);
//        ivparams.gravity = Gravity.CENTER_VERTICAL|Gravity.END;
//        mImageView = new ImageView(context);
//        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        mImageView.setLayoutParams(ivparams);
//        frameLayout.addView(mEditText, params);
//        frameLayout.addView(mImageView);
//        addView(mTextView, textParams);
//        addView(frameLayout, params);

        mPopView = new ListPopupWindow(context);
        adapter = new ArrayAdapter<>(context, R.layout.item_spinner);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditText = (EditText) findViewById(R.id.et);
        mImageView = (ImageView) findViewById(R.id.iv);
        mTextView = (TextView) findViewById(R.id.tv);
        mTextViewRight = (TextView) findViewById(R.id.tv_right);
        mImageView.setOnClickListener(this);
        mEditText.addTextChangedListener(this);
        mTextView.setText(tv_text);
        mTextView.setTextColor(tv_color);
        if (tv_visible) {
            mTextView.setVisibility(VISIBLE);
        } else {
            mTextView.setVisibility(GONE);
        }
        if (dropMode && et_enable) {
            mImageView.setVisibility(VISIBLE);
        } else {
            mImageView.setVisibility(GONE);
        }
        mEditText.setHint(et_hint);
        if (et_background != 0) {
            mEditText.setBackgroundResource(et_background);
        }
        mEditText.setEnabled(et_enable);
        mEditText.setFocusable(et_focusable);
        mImageView.setImageResource(iv_src);
        if (tv_textSize != 0) {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tv_textSize);
            mTextViewRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, tv_textSize);
        } else {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            mTextViewRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        if (et_textSize != 0) {
            mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, et_textSize);
        } else {
            mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        mPopView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                position = pos;
                needChange = true;
                mEditText.setText(list.get(position));
                mPopView.dismiss();
            }
        });
    }

    public <T> void setStringList(final List<String> list, final List<T> objectList) {
        this.objectList = objectList;
        setStringList(list);
    }

    public void setStringList(final List<String> list) {
        this.list = list;
        if (list != null && !list.isEmpty()) {
            if (adapter != null) {
                adapter.clear();
                adapter.addAll(list);
            }
            mPopView.setAdapter(adapter);
            if (dropWidth == 0) {
                mPopView.setWidth(mEditText.getMeasuredWidth());
            } else {
                mPopView.setWidth(dropWidth);
            }
            mPopView.setHeight(LayoutParams.WRAP_CONTENT);
            mPopView.setModal(true);
            mPopView.setAnchorView(mEditText);
            mPopView.show();
        }
    }

    public String getText() {
        return mEditText.getText().toString();
    }

    public boolean isNullStringList() {
        return !(list != null && !list.isEmpty());
    }

    @Override
    public void onClick(View v) {
        if (onDropClickListener != null) {
            onDropClickListener.onClick(v);
        }
        if (onDropDateOrTimePickerListener != null && picker != 0) {
            if (picker == DatePicker) {
                setDatePicker();
            } else if (picker == TimePicker) {
                setTimePicker();
            } else {
                Toast.makeText(getContext(), "无效选择器！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setDatePicker() {
        final Calendar c = Calendar.getInstance();
        if (year == 0 || month == 0 || day == 0) {
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        final DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            }
        }, year, month, day);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "保存年月日", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialo, int which) {
                int monthOfYear = dialog.getDatePicker().getMonth();
                monthOfYear++;
                needChange = true;
                mEditText.setText(dialog.getDatePicker().getYear() + "-" + monthOfYear + "-" + dialog.getDatePicker().getDayOfMonth());
                if (onDropDateOrTimePickerListener != null) {
                    onDropDateOrTimePickerListener.onSelected(dialog.getDatePicker().getYear() + "-" + monthOfYear + "-" + dialog.getDatePicker().getDayOfMonth(), dialog.getDatePicker().getYear(), monthOfYear, dialog.getDatePicker().getDayOfMonth());
                }
            }
        });
        if (picker == DatePicker_NoDay) {
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "保存年月", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int which) {
                    int monthOfYear = dialog.getDatePicker().getMonth();
                    monthOfYear++;
                    needChange = true;
                    mEditText.setText(dialog.getDatePicker().getYear() + "-" + monthOfYear);
                    if (onDropDateOrTimePickerListener != null) {
                        onDropDateOrTimePickerListener.onSelected(dialog.getDatePicker().getYear() + "-" + monthOfYear, dialog.getDatePicker().getYear(), monthOfYear, dialog.getDatePicker().getDayOfMonth());
                    }
                }
            });
        }
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialo, int which) {
                int monthOfYear = dialog.getDatePicker().getMonth();
                monthOfYear++;
                needChange = true;
                mEditText.setText("");
                if (onDropDateOrTimePickerListener != null) {
                    onDropDateOrTimePickerListener.onSelected("", dialog.getDatePicker().getYear(), monthOfYear, dialog.getDatePicker().getDayOfMonth());
                }
            }
        });
        dialog.show();
    }

    private void setTimePicker() {
        final Calendar c = Calendar.getInstance();
        if (hour == 0 || minute == 0) {
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }
        final TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                needChange = true;
                mEditText.setText(hourOfDay + ":" + minute);
                if (onDropDateOrTimePickerListener != null) {
                    onDropDateOrTimePickerListener.onSelected(hourOfDay + ":" + minute, hourOfDay, minute);
                }
            }
        }, hour, minute, true);
        dialog.show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString())) {
            mTextView.setTextColor(tv_color);
        } else {
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
        }
        if (onTextChangeListener != null) {
            if (objectList == null || objectList.isEmpty() || position < 0) {
                onTextChangeListener.afterTextChanged(s.toString(), null);
            } else {
                onTextChangeListener.afterTextChanged(s.toString(), objectList.get(position));
            }
            position = -1;
        }
    }

    private boolean needChange = false;

    public boolean isChanged() {
        if (mEditText.hasFocus() || needChange) {
            needChange = false;
            return true;
        } else {
            return false;
        }
    }

    public interface OnTextChangeListener {
        void afterTextChanged(String text, Object object);
    }

    public interface OnDropClickListener {
        void onClick(View v);
    }

    public abstract static class OnDropDateOrTimePickerListener {
        public void onSelected(String select, int year, int month, int day) {

        }

        public void onSelected(String select, int hour, int minute) {

        }
    }

    public void setTv_text(String tv_text) {
        if (mTextView != null)
            mTextView.setText(tv_text);
    }


    public void setTvRight_text(String tv_text) {
        if (mTextViewRight != null) {
            mTextViewRight.setVisibility(View.VISIBLE);
            mTextViewRight.setText(tv_text);
        }
    }

    public void setEt_text(String et_text) {
        if (mEditText != null)
            mEditText.setText(et_text);
    }

    public void setEt_text(String et_text, boolean needChange) {
        this.needChange = needChange;
        if (mEditText != null)
            mEditText.setText(et_text);
    }

    public void setTv_color(int tv_color) {
        this.tv_color = tv_color;
        if (mTextView != null && TextUtils.isEmpty(mEditText.getText())) {
            mTextView.setTextColor(tv_color);
        }
    }

    public void setEt_enable(boolean enable) {
        if (mEditText != null) {
            mEditText.setEnabled(enable);
        }
        if (!enable) {
            mImageView.setVisibility(GONE);
        } else if (dropMode) {
            mImageView.setVisibility(VISIBLE);
        }
    }

    public void setEt_focusable(boolean focusable) {
        if (mEditText != null) {
            mEditText.setFocusable(focusable);
            mEditText.setFocusableInTouchMode(focusable);
        }
        if (!focusable && mEditText != null) {
//            mEditText.setBackgroundResource(R.drawable.bg_edittext_no_focused);
        }
//        else if (et_background != 0 && mEditText != null) {
//            mEditText.setBackgroundResource(et_background);
//        }
    }

    public boolean isEt_focusable() {
        return et_focusable;
    }

    public void setDropMode(boolean dropMode) {
        if (dropMode && mEditText.isEnabled()) {
            mImageView.setVisibility(VISIBLE);
        } else {
            mImageView.setVisibility(GONE);
        }
    }

    public EditText getEditText() {
        return mEditText;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }

}
