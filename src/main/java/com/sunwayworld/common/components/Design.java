package com.sunwayworld.common.components;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Wang on 2016/8/18.
 */
public class Design extends View{

    private OnTouchLetterChangedListener onTouchLetterChangedListener;
    public static String[] zimu = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private int choose = -1;
    private Paint paint = new Paint();

    private TextView mTextDialog;

    public void setTextDialog(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public Design(Context context) {
        super(context);
    }

    public Design(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Design(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();//获取高度
        int width = getWidth();//获取宽度
        int singleHeight = height / zimu.length;//获取每一个字母的高度

        for (int i = 0; i < zimu.length; i++) {
            paint.setColor(Color.parseColor("#25C6DA"));
            paint.setAntiAlias(true);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextSize(20);
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }
            //x坐标等于(中间-字符串宽度)的一半
            float xPos = width / 2 - paint.measureText(zimu[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(zimu[i], xPos, yPos, paint);
            paint.reset();//重置画笔
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();//点击y坐标
        final int oldChoose = choose;
        final OnTouchLetterChangedListener listener = onTouchLetterChangedListener;
        //点击y坐标所占总高度的比例*zimu数组的长度就等于点击zimu中的下标
        final int c = (int) (y / getHeight() * zimu.length);

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackground(new ColorDrawable(0x00000000));
                choose = -1;
                //invalidate()是用来刷新View的
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(INVISIBLE);
                }
                break;
            default:
                setBackgroundColor(Color.parseColor("#c9c9c9"));
                if (oldChoose != c) {
                    if (c >= 0 && c < zimu.length) {
                        if (listener != null) {
                            listener.onTouchLetterChanged(zimu[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(zimu[c]);
//                            mTextDialog.setTextColor(DataUtils.currentcolor);
//                            mTextDialog.setBackgroundColor(Color.parseColor("#25C6DA"));
                            mTextDialog.setVisibility(VISIBLE);
                        }
                        choose = c;
                        invalidate();
                    }
                }
                break;

        }

        return true;
    }

    /**
     * 向外公开
     * @param onTouchLetterChangedListener
     */
    public void setOnTouchLetterChangedListener(OnTouchLetterChangedListener onTouchLetterChangedListener) {
        this.onTouchLetterChangedListener = onTouchLetterChangedListener;
    }

    public interface OnTouchLetterChangedListener {
        public void onTouchLetterChanged(String s);
    }
}
