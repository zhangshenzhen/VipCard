package com.bjypt.vipcard.utils;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.bjypt.vipcard.view.MyDialog;

/**
 * Created by Administrator on 2016/5/9.
 */
public class MyDialogUtil {

    private static MyDialog myDialog;

    /*Context context
     String title      标题
     String messageOne   第一行的消息
      String messageTwo   第二行的消息
      String ButtomOneText   第一个按钮
       String ButtomTwoText   第二个按钮
       MyDialog.ClickListenerInterface v   点击处理
        doButtomOne 为点击第一个  doButtomTwo 点击第二个
            */
    public static void setDialog(Context context, String title, String messageOne, String messageTwo, String ButtomOneText, String ButtomTwoText, MyDialog.ClickListenerInterface v) {

        myDialog = new MyDialog(context, title, messageOne, messageTwo, ButtomOneText, ButtomTwoText, v);
        Window window = myDialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setAttributes(lp);
        }
        myDialog.show();
    }

    /**
     * 隐藏dialge
     */
    public static void hideDialog() {
        myDialog.dismiss();


        Log.e("", "");
    }
}
