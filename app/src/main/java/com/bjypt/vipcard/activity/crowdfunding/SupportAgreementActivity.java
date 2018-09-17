package com.bjypt.vipcard.activity.crowdfunding;

import android.view.View;
import android.widget.Button;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;

public class SupportAgreementActivity extends BaseActivity {

    private Button btn_cancel ,btn_sure;

    @Override
    public void setContentLayout() {
     setContentView(R.layout.activity_agreement);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        btn_cancel = (Button)findViewById(R.id.btn_cancel_agreement);
        btn_sure = (Button)findViewById(R.id.btn_sure_agreement);
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
        switch (v.getId()){
            case R.id.btn_cancel_agreement:
                finish();
                break;
            case R.id.btn_sure_agreement:
                //打开支付界面
                break;

        }

    }
}
