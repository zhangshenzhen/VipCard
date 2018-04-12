package com.bjypt.vipcard.view;

import android.content.Context;
import android.widget.Toast;

import com.sinia.orderlang.utils.StringUtil;

/**
 * Created by Administrator on 2016/11/15.
 */

public class ToastUtil {
    private static Toast mToast = null;

    public static void showToast(Context context, String text) {
        if (StringUtil.isNotEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.show();
        }
    }
}
