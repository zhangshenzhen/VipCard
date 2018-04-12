package com.bjypt.vipcard.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bjypt.vipcard.R;

/**
 * Created by Dell on 2018/4/3.
 */

public class MyDialogAD extends Dialog implements View.OnClickListener {
    private final LayoutInflater inflater;
    private View view;

    public MyDialogAD(Context context) {
        super(context, R.style.MyDialog);
        inflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        view = inflater.inflate(R.layout.dialog_view,null);
        Window window = this.getWindow();
        if (window != null) { //设置dialog的布局样式 让其位于底部
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setAttributes(lp);
        }
        view.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        dismiss();
    }
}
