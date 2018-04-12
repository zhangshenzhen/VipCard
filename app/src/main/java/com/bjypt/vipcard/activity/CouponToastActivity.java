package com.bjypt.vipcard.activity;

import android.view.View;
import android.widget.LinearLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;

public class CouponToastActivity extends BaseActivity {

    private LinearLayout ly_coupon_taost_back;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_coupon_toast);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        ly_coupon_taost_back = (LinearLayout) findViewById(R.id.ly_coupon_taost_back);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        ly_coupon_taost_back.setOnClickListener(this);

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ly_coupon_taost_back:
                finish();
                break;
        }

    }
}
