package com.bjypt.vipcard.activity.shangfeng.util;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.bjypt.vipcard.base.MyApplication;

import static com.bjypt.vipcard.activity.shangfeng.util.ToastMgr.ToastEnum.builder;


public class ToastUtils {

    public static void showToast(int resID) {
        showToast(Toast.LENGTH_SHORT, resID);
    }

    public static void showToast(int time, int resID) {
        showToast(time, MyApplication.getResourceString(resID));
    }

    public static void showToast(String text) {

        showToast(Toast.LENGTH_SHORT, text);
    }

    public static void showToast(int duration, String text) {
        if (!TextUtils.isEmpty(text)) {
            builder.display(text, duration);
        }
    }

    public static void showLongToast(int resID) {
        showToast(Toast.LENGTH_LONG, resID);
    }

    public static void showLongToast(String text) {
        showToast(Toast.LENGTH_LONG, text);
    }


    /**
     * 带bottom表示在底部显示
     */
    public static void showToastBottom(int resID) {
        showToastBottom(Toast.LENGTH_SHORT, resID);
    }

    public static void showToastBottom(String text) {
        showToastBottom(Toast.LENGTH_SHORT, text);
    }


    public static void showLongToastBottom(int resID) {
        showToastBottom(Toast.LENGTH_LONG, resID);
    }

    public static void showLongToastBottom(String text) {
        showToastBottom(Toast.LENGTH_LONG, text);
    }

    public static void showToastBottom(int duration, int resID) {
        showToastBottom(duration, MyApplication.getResourceString(resID));
    }

    /**
     * Toast一个图片
     */
    private static Toast showToastImage(int resID) {
        final Toast toast = Toast.makeText(MyApplication.getContext(), "", Toast.LENGTH_SHORT);
        View mNextView = toast.getView();
        if (mNextView != null) mNextView.setBackgroundResource(resID);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }

    public static void showToastBottom(int duration, String text) {
        if (!TextUtils.isEmpty(text)) {
            builder.displayBottom(text, duration);
        }
    }
}
