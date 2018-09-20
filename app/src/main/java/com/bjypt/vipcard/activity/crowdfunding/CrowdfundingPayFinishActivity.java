package com.bjypt.vipcard.activity.crowdfunding;

import android.view.View;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.githang.statusbar.StatusBarCompat;

public class CrowdfundingPayFinishActivity extends BaseActivity{
    @Override
    public void setContentLayout() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.cf_account_detail_head));
        setContentView(R.layout.activity_crowdfunding_payfinish);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }
}
