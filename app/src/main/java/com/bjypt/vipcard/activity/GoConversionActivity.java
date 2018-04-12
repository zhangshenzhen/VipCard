package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.GoConversionBean;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GoConversionActivity extends BaseActivity implements VolleyCallBack<String> {

    private LinearLayout back;                //返回键
    private LinearLayout linear_hf;           //话费
    private LinearLayout linear_sd;           //水电
    private LinearLayout linear_ljmd;         //商家立即买单
    private TextView hf_tv;
    private TextView sd_tv;
    private GoConversionBean goConversionBean;
    private static final int request_go_conversion = 1001;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_go_conversion);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        back = (LinearLayout) findViewById(R.id.go_conversion_back);
        linear_hf = (LinearLayout) findViewById(R.id.linear_hf);
        linear_sd = (LinearLayout) findViewById(R.id.linear_sd);
        linear_ljmd = (LinearLayout) findViewById(R.id.linear_ljmd);
        hf_tv = (TextView) findViewById(R.id.hf_tv);
        sd_tv = (TextView) findViewById(R.id.sd_tv);
        Wethod.httpPost(GoConversionActivity.this,request_go_conversion, Config.web.go_conversion, null, this);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        back.setOnClickListener(this);
        linear_hf.setOnClickListener(this);
        linear_ljmd.setOnClickListener(this);
        linear_sd.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.go_conversion_back:          //返回
                Intent intent1 = new Intent();
                this.setResult(11,intent1);          //返回码为11回到我的页面
                finish();
                break;
            case R.id.linear_hf:                    //话费充值
                Intent intent = new Intent(this, LifeServireH5Activity.class);
                intent.putExtra("serviceName", "话费充值");
                intent.putExtra("life_url", goConversionBean.getResultData().getPhoneUrl());
                intent.putExtra("isLogin", "Y");
                startActivity(intent);
                break;
            case R.id.linear_sd:                   //水电缴费
                Intent intent3 = new Intent(this, LifeServireH5Activity.class);
                intent3.putExtra("life_url", goConversionBean.getResultData().getWaterUrl());
                intent3.putExtra("isLogin", "Y");
                intent3.putExtra("serviceName", "水电缴费");
                startActivity(intent3);
                break;
            case R.id.linear_ljmd:                 //商家立即买单
                Intent intent4 = new Intent();
                this.setResult(12,intent4);           //返回码为12回到店铺街页面
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == request_go_conversion) {
            try {
                goConversionBean = getConfiguration().readValue(result.toString(),GoConversionBean.class);
//                sd_tv.setText("每单最高可抵扣缴费金额的"+goConversionBean.getResultData().getWaterRate());
//                hf_tv.setText("每日首单最高可抵扣缴费金额的"+goConversionBean.getResultData().getPhoneRate());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
