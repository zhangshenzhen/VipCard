package com.bjypt.vipcard.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bjypt.vipcard.R;


/**
 * Created by duanyj on 2016/3/3.
 */
public class NormalDialog {
    private static NormalDialog dialog = null;

    private Context context;
    private TextView descText;
    private TextView tv_dialog_title;
    private TextView positiveButton;
    private TextView negativeButton;
    private android.app.Dialog ad;

    /* 1:懒汉式，静态工程方法，创建实例 */
    public static NormalDialog getInstance(Context context) {
        if (dialog  == null) {
            dialog  = new NormalDialog(context);
        }
        return dialog ;
    }

    private NormalDialog(Context context) {
        this.context = context;
        alertDialog();
    }

    public void setDesc(String desc) {
        descText.setText(desc);
    }

    public void setTitle(String title) {
        tv_dialog_title.setText(title);
    }

    private void alertDialog() {
        ad = new android.app.Dialog(context);
        ad.setCanceledOnTouchOutside(false);
        ad.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ad.show();
        Window window = ad.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        window.setContentView(R.layout.dialog_normal_layout);
        descText = (TextView) window.findViewById(R.id.dialog_normal_desc);
        tv_dialog_title = (TextView) window.findViewById(R.id.tv_dialog_title);
        positiveButton = (TextView) window.findViewById(R.id.positiveButton);
        negativeButton = (TextView) window.findViewById(R.id.negativeButton);
    }


    /**
     * 关闭对话框
     */
    public void dismiss() {
        ad.dismiss();
        dialog = null;
    }

    /**
     * 确定键监听器
     *
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener) {
        positiveButton.setOnClickListener(listener);
    }

    /**
     * 取消键监听器
     *
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener) {
        negativeButton.setOnClickListener(listener);
    }
}
