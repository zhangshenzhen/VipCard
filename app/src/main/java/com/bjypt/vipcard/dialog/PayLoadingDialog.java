package com.bjypt.vipcard.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bjypt.vipcard.R;

/**
 * Created by 崔龙 on 2017/5/15.
 * 所有支付页面dialog样式
 */

public class PayLoadingDialog extends ProgressDialog {

    private Context mContext;
    private static ProgressDialog payLoadingDialog;
    private ImageView iv_pay_dialog;
    private AnimationDrawable mAnimation;

    public PayLoadingDialog(Context context) {
        super(context);
        this.mContext = context;
        setCanceledOnTouchOutside(true);

    }

    public static synchronized ProgressDialog getInstance(Context context) {
        if (payLoadingDialog == null) {
            payLoadingDialog = new PayLoadingDialog(context);
        }
        return payLoadingDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.dialog_pay_loading);
        iv_pay_dialog = (ImageView) findViewById(R.id.iv_pay_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    private void initData() {
        iv_pay_dialog.setBackgroundResource(R.drawable.animation_pay);
        // 通过ImageView对象拿到背景显示的AnimationDrawablel
        mAnimation = (AnimationDrawable) iv_pay_dialog.getBackground();
        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
        iv_pay_dialog.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();

            }
        });
    }

    @Override
    public void show() {
        super.show();
        this.getWindow().setBackgroundDrawable(new ColorDrawable());
    }
}
