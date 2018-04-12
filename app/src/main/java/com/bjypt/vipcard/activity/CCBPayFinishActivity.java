package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.bjypt.vipcard.base.BaseActivity;
import com.sinia.orderlang.utils.StringUtil;

/**
 * Created by admin on 2017/5/19.
 */

public class CCBPayFinishActivity extends BaseActivity {
    @Override
    public void setContentLayout() {

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
        Log.i("wanglei", "onResume:" + getIntent().getStringExtra("CCBPARAM"));
        String ccbparam = getIntent().getStringExtra("CCBPARAM");
//        TopupWayActivity.WXPAY_FLAG = 2;//平台充值
        if (getIntent().hasExtra("CCBPARAM"))
            ccbparam = getIntent().getStringExtra("CCBPARAM");
        if (StringUtil.isNotEmpty(ccbparam)) {
            Log.i("wanglei", "ccb pay finish:");
            Intent intent  = new Intent();
            intent.setAction("ccb_pay_notify");
//            intent.setClass(this, PayCenterActivity.class);
//            intent.putExtra("payResult", true);
//            setResult(RESULT_OK, intent);
            sendBroadcast(intent);
            finish();
        }
    }
}
