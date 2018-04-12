package com.bjypt.vipcard.dialog;

import android.app.Dialog;
import android.content.Context;

import com.bjypt.vipcard.R;


/**
 * Created by 崔龙 on 2017/10/9.
 */

public class UploadDialog {
    private Dialog dialog;
    private Context context;

    public UploadDialog(Context context) {
        this.context = context;
    }

    public void showLoading(){
        if(dialog==null){
            dialog=new Dialog(context, R.style.Translucent_NoTitle);
            dialog.setContentView(R.layout.loading);
        }
        if(!dialog.isShowing()){
            dialog.show();
        }
    }
    public void hideLoading(){
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
