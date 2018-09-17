package com.bjypt.vipcard.activity.crowdfunding;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.pay.CrowdfundingPayActivity;
import com.bjypt.vipcard.base.BaseActivity;

import java.math.BigDecimal;

public class SupportAgreementActivity extends BaseActivity {

    private Button btn_cancel, btn_sure;

    private int pkprogressitemid;
    private int pkmerchantid;
    private String amount;
    private int paytype;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_agreement);
    }

    @Override
    public void beforeInitView() {
        pkprogressitemid = getIntent().getIntExtra("pkprogressitemid", 0);
        pkmerchantid = getIntent().getIntExtra("pkmerchantid", 0);
        amount = getIntent().getStringExtra("amount");
        paytype = getIntent().getIntExtra("paytype", 0);
        if (pkprogressitemid == 0) {
            finish();
        }
    }

    @Override
    public void initView() {
        btn_cancel = (Button) findViewById(R.id.btn_cancel_agreement);
        btn_sure = (Button) findViewById(R.id.btn_sure_agreement);
        btn_cancel.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_agreement:
                finish();
                break;
            case R.id.btn_sure_agreement:
                Intent intent = new Intent(this, CrowdfundingPayActivity.class);
                intent.putExtra("pkprogressitemid", pkprogressitemid);
                intent.putExtra("paytype", paytype);
                intent.putExtra("amount", amount);
                intent.putExtra("pkmerchantid", pkmerchantid);
                startActivity(intent);
                finish();
                //打开支付界面
                break;

        }

    }
}
