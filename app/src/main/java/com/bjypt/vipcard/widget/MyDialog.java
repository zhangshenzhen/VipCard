package com.bjypt.vipcard.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bjypt.vipcard.R;

/**
 * Created by Dell on 2018/4/3.
 */

public class MyDialog extends Dialog implements View.OnClickListener {

    private final LayoutInflater inflater;
    private View view;

    public MyDialog(Context context) {
        super(context, R.style.MyDialog);
        inflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        view = inflater.inflate(R.layout.dialog_view,null);
        Window window = this.getWindow();
        if (window != null) {
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
