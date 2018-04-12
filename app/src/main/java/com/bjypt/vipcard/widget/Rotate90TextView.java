package com.bjypt.vipcard.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by 崔龙 on 2017/10/23.
 * <p>
 * 旋转90度的TextView
 */

public class Rotate90TextView extends android.support.v7.widget.AppCompatTextView {
    public Rotate90TextView(Context context) {
        super(context);
    }

    public Rotate90TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Rotate90TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
        canvas.rotate(90, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        super.onDraw(canvas);
    }
}
