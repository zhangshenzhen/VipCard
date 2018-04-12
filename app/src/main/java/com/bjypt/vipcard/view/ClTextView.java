package com.bjypt.vipcard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 崔龙 on 2017/4/20.
 * 自定义TextView
 */

public class ClTextView extends TextView {

    public ClTextView(Context context) {
        super(context);
    }

    public ClTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override

    public boolean isFocused() {
        return true;
    }
}
