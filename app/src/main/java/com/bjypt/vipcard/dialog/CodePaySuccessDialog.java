package com.bjypt.vipcard.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;


/**
 * Created by 崔龙 on 2017/10/23.
 * <p>
 * 二维码提示框
 */
public class CodePaySuccessDialog {

    private Context context;

    private TextView titleText;

    private TextView positiveButton;

    private TextView negativeButton;

    private ImageView iv_line;

    private android.app.Dialog ad;

    public CodePaySuccessDialog(Context context) {
        this.context = context;
        alertDialog();
    }

    public void setTitle(String title) {
        titleText.setText(title);
    }

    public void setPositiveButtonVisible(int visableProgress) {
        positiveButton.setVisibility(visableProgress);
        iv_line.setVisibility(visableProgress);
    }

    public void setPositiveButton(String s) {
        positiveButton.setText(s);
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
        window.setContentView(R.layout.dialog_code_pay_layout2);
        titleText = (TextView) window.findViewById(R.id.dialog_normal_title);
        positiveButton = (TextView) window.findViewById(R.id.positiveButton);
        negativeButton = (TextView) window.findViewById(R.id.negativeButton);
        iv_line = (ImageView) window.findViewById(R.id.iv_line);
    }


    /**
     * 关闭对话框
     */
    public void dismiss() {
        ad.dismiss();
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
