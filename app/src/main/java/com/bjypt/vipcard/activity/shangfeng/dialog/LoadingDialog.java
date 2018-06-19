package com.bjypt.vipcard.activity.shangfeng.dialog;

import android.app.Activity;
import android.content.Context;

import com.bjypt.vipcard.R;
import com.orhanobut.logger.Logger;

/**
 * 加载 Dialog
 */
public class LoadingDialog extends DialogNoTitle {

    private static String TAG = LoadingDialog.class.getSimpleName();
    private Context context;

    public LoadingDialog(Context context, String title) {
        super(context, R.style.LoadingDialogStyle, R.layout.dialog_loading, title);
        this.context = context;

    }

    /**
     * 显示loading
     */
    public void shows() {
        if (context != null) {
            try {
                if (context instanceof Activity && !((Activity) context).isFinishing()) {
                    this.show();
                    this.setCancelable(true);
                    this.setCanceledOnTouchOutside(true);
                }
            } catch (Exception e) {
                Logger.e(TAG, "e   " + e.toString());
            }
        }
    }

    /**
     * 关掉loading
     */
    public void dismisss() {
        if (context != null) {
            try {
                if (context instanceof Activity && !((Activity) context).isFinishing()) {
                    this.dismiss();
                }
            } catch (Exception e) {
                Logger.e(TAG, "e   " + e.toString());
            }
        }
    }

}
