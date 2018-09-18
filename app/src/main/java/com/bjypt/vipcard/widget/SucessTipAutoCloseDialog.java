package com.bjypt.vipcard.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bjypt.vipcard.R;

/**
 * Created by Dell on 2018/4/11.
 */

public class SucessTipAutoCloseDialog extends Dialog {

    private LayoutInflater inflater;
    private View view;

    private TextView tv_content;

    public SucessTipAutoCloseDialog(@NonNull Context context) {
        super(context, R.style.MyDialogStyle);
        inflater = LayoutInflater.from(context);
        initViews();
    }


    private void initViews() {
        view = inflater.inflate(R.layout.dialog_sucess_tip, null);
        Window window = this.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setWindowAnimations(R.style.dialogWindowAnim);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setAttributes(lp);
        }

        tv_content = (TextView) view.findViewById(R.id.tv_content);
        setContentView(view);

    }

    public void setTextContent(String msg) {
        tv_content.setText(msg);
    }


    @Override
    public void show() {

        super.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SucessTipAutoCloseDialog.this.cancel();
            }
        }, 3000);
    }
}
