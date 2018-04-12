package com.bjypt.vipcard.utils;

import android.content.Context;
import android.widget.EditText;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.view.MyDialog;
import com.bjypt.vipcard.view.PsdDialog;

/**
 * Created by 涂有泽 .
 * Date by 2016/5/11
 * Use by
 */
public class PsdDialogUtils {

    private static PsdDialog psdDialog;

    public static void setDialog(Context context, String title, String messageOne, String messageTwo) {
        psdDialog = new PsdDialog(context, title,messageOne, messageTwo);
        psdDialog.show();
    }

}
