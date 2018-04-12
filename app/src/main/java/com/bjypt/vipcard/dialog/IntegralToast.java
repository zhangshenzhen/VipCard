package com.bjypt.vipcard.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;

/**
 * Created by 崔龙 on 2017/12/12.
 */

public class IntegralToast {
    public static IntegralToast mToastEmail;
    private Toast toast;

    private IntegralToast() {
    }

    public static IntegralToast getIntegralToast() {
        if (mToastEmail == null) {
            mToastEmail = new IntegralToast();
        }
        return mToastEmail;
    }

    /**
     * 显示
     */
    public void ToastShow(Context context, ViewGroup root, String str) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_integral_layout, root);
        TextView text = (TextView) view.findViewById(R.id.tv_dialog_title);
        text.setText(str); // 设置显示文字
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0); // Toast显示的位置
        toast.setDuration(1000); // Toast显示的时间
        toast.setView(view);
        toast.show();
    }

    public void ToastCancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
