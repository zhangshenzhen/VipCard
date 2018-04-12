package com.bjypt.vipcard.view;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.utils.LogUtil;

/**
 * Created by wanglei .
 * Date by 2018/04/03
 * Use by 自定义Dialog
 */
public class ServiceUpgradeDialog extends Dialog {


    Context context;

    ImageView iv_cancel;
    private WindowManager windowManager;

    public ServiceUpgradeDialog(Context context) {
        super(context, R.style.MyDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.service_upgrade_dialog);

        Window window = this.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setAttributes(lp);
        }
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                LogUtil.debugPrint("isUpgrading : " + "onCancel");
                MyApplication.getInstance().setUpgrading(false);
            }
        });
        iv_cancel = (ImageView)findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().setUpgrading(false);
                dismiss();
            }
        });
    }


}
