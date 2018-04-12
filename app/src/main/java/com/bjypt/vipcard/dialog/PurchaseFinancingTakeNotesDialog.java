package com.bjypt.vipcard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.Interface.SelectSuccessListener;
import com.bjypt.vipcard.widget.GoToSelectView;


/**
 * Created by 崔龙 on 2018/1/17.
 * <p>
 * 商家理财购成功dialog
 */

public class PurchaseFinancingTakeNotesDialog extends Dialog implements View.OnClickListener {

    private final GoToSelectView gt_view;

    public PurchaseFinancingTakeNotesDialog(Context context, String pkmuser, String investid, String categoryid) {
        //使用自定义Dialog样式
        super(context, R.style.custom_dialog);
        //指定布局
        setContentView(R.layout.dialog_financing_take_notes);
        //点击外部不可消失
        setCancelable(false);
        final View cancel = findViewById(R.id.cancel);
        View cancel2 = findViewById(R.id.cancel2);
        cancel.setOnClickListener(this);
        cancel2.setOnClickListener(this);
        gt_view = (GoToSelectView) findViewById(R.id.gt_view);
        gt_view.requestGoToSelect(context, pkmuser, investid, categoryid);
        gt_view.setSelectSuccessListener(new SelectSuccessListener() {
            @Override
            public void selectOk() {
                cancel();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                cancel();
                break;
            case R.id.cancel2:
                gt_view.pleaseInputPassword();
                break;
        }
    }
}
