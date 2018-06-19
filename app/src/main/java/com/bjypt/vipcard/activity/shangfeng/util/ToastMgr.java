package com.bjypt.vipcard.activity.shangfeng.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;

/**
 * toast管理类
 */
public class ToastMgr {
    static Handler toastHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            Object[] obj = (Object[]) msg.obj;
            CharSequence content = (CharSequence) obj[0];
            int duration = (Integer) obj[1];
            ToastEnum.builder.display(content, duration);
            return false;
        }
    });

    public enum ToastEnum {
        builder;
        private View view;
        private TextView tv;
        private Toast toast;

        /**
         * @param appContext
         */
        public void init(Context appContext) {
            view = RelativeLayout.inflate(appContext, R.layout.toast_layout, null);
            tv = (TextView) view.findViewById(R.id.toast_name);
            toast = new Toast(appContext);
            toast.setView(view);
        }

        /**
         * 显示在中间Toast
         *
         * @param content
         * @param duration Toast持续时间
         */
        public void display(CharSequence content, int duration) {
            if (content.length() != 0) {
                if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                    tv.setText(content);
                    toast.setDuration(duration);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    // 不是主线程
                    Message message = new Message();
                    Object[] obj = {content, duration};
                    message.obj = obj;
                    toastHandler.sendMessage(message);
                }

            }
        }

        /**
         * 显示在底部Toast
         *
         * @param content
         * @param duration Toast持续时间
         */
        public void displayBottom(CharSequence content, int duration) {
            if (content.length() != 0) {
                tv.setText(content);
                toast.setDuration(duration);
                toast.setGravity(Gravity.BOTTOM, 0, DisplayUtil.dip2px(50));
                toast.show();
            }
        }
    }
}

