package com.bjypt.vipcard.activity;

import android.view.View;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.umeng.UmengCountContext;

/**
 * Created by 涂有泽 .
 * Date by 2016/5/11
 * Use by 消息(商家优惠信息，商家活动，系统提醒)
 */
public class TotalNewsActivity extends BaseActivity{
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_totalnews);
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

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }
}
