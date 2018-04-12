package com.bjypt.vipcard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.bjypt.vipcard.R;


/**
 * Created by 崔龙 on 2018/1/17.
 * <p>
 * 商家理财购成功dialog
 */

public class PurchaseFinancingSuccessDialog extends Dialog implements View.OnClickListener {

    protected PurchaseFinancingSuccessDialog(Context context) {
        //使用自定义Dialog样式
        super(context, R.style.custom_dialog);
        //指定布局
        setContentView(R.layout.financing_pay_ok);
        //点击外部不可消失
        setCancelable(false);

        //设置标题
        View cancel_1 = findViewById(R.id.cancel_1);
        View back_home = findViewById(R.id.back_home);
        View take_notes = findViewById(R.id.take_notes);
        cancel_1.setOnClickListener(this);
        back_home.setOnClickListener(this);
        take_notes.setOnClickListener(this);
    }

    //去理财记录
    public void goTakeNotes() {
    }

    //去理财记录
    public void backHome() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_1:
                cancel();
                break;
            case R.id.back_home:
                backHome();
                break;
            case R.id.take_notes:
                goTakeNotes();
                break;
        }
    }
}
