package com.bjypt.vipcard.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bjypt.vipcard.R;

/**
 * Created by Administrator on 2017/8/25.
 */

public class ReminderDialog {

    private Context context;

    private TextView titleText;

    private TextView negativeButton;

    private android.app.Dialog ad;

    public ReminderDialog(Context context) {
        this.context = context;
        alertDialog();
    }

    public void setTitle(String title) {
        titleText.setText(title);
    }

    public void setButtonName(String name){
        negativeButton.setText(name);
    }

    private void alertDialog() {
        ad=new android.app.Dialog(context);
        ad.setCanceledOnTouchOutside(false);
        ad.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ad.show();
        Window window = ad.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        window.setContentView(R.layout.dialog_reminder);

        titleText = (TextView) window.findViewById(R.id.dailog_info);
        negativeButton = (TextView) window.findViewById(R.id.dialog_cancle);
    }



    /**
     * 关闭对话框
     */
    public void dismiss() {
        ad.dismiss();
    }

    /**
     * 取消键监听器
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener){
        negativeButton.setOnClickListener(listener);
    }
}
