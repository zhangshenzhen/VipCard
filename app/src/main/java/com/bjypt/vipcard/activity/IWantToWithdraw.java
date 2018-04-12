package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.NewBindBankCardActivity;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.model.WithdrawBean;
import com.bjypt.vipcard.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IWantToWithdraw extends BaseActivity implements VolleyCallBack<String> {

    private TextView mBankName;         //持卡人姓名
    private LinearLayout mBack;         //返回键
    private RoundImageView mBankLogo;  //银行logo
    private TextView mBankCardNum;     //银行卡号
    private TextView mTrue;             //我要充值
    private TextView goToWithdraw;     //直接提现

    private SystemInfomationBean systemInfomationBean;
    private String isBindingBank;
    private WithdrawBean withdrawBean;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_iwant_to_withdraw);
    }

    @Override
    public void beforeInitView() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(IWantToWithdraw.this, 1, Config.web.system_infomatiob, params, this);
    }

    @Override
    public void initView() {
        mBack = (LinearLayout) findViewById(R.id.withdraw_back);                        //返回键
        mBankLogo = (RoundImageView) findViewById(R.id.bank_logo);                           //银行Logo
        mBankName = (TextView) findViewById(R.id.bank_name_tv);                        //持卡人姓名
        mBankCardNum = (TextView) findViewById(R.id.bank_card_num);                    //银行卡号码和类型
        mTrue = (TextView) findViewById(R.id.issue_recharge);                           //我要充值
        goToWithdraw = (TextView) findViewById(R.id.go_to_withdraw);                   //直接提现
    }

    @Override
    public void afterInitView() {
        Map<String, String> params2 = new HashMap<>();
        params2.put("pkmuser", getFromSharePreference(Config.userConfig.pkregister));
        params2.put("type", "2");
        Wethod.httpPost(IWantToWithdraw.this, 12, Config.web.query_withdraw, params2, this);
    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
        mTrue.setOnClickListener(this);
        goToWithdraw.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.withdraw_back:             //返回
                finish();
                break;
            case R.id.issue_recharge:           //我要充值
                Intent intent = new Intent(IWantToWithdraw.this, NewTopupWayActivity.class);
                intent.putExtra("pkmuser", systemInfomationBean.getResultData().getPkmuser());
                intent.putExtra("ChongZhi_Flag","90");
                startActivity(intent);
                break;
            case R.id.go_to_withdraw:           //直接提现
                Intent intent1 = new Intent(IWantToWithdraw.this, WithdrawActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 1) {
            try {
                systemInfomationBean = getConfiguration().readValue(result.toString(), SystemInfomationBean.class);
                isBindingBank = systemInfomationBean.getResultData().getIsBindingBank();
                if (isBindingBank.equals("N")) {
                    Intent intent = new Intent(IWantToWithdraw.this, NewBindBankCardActivity.class);
                    intent.putExtra("WithDrawFlag", "22");
                    startActivity(intent);
                    finish();
                } 
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 12) {
            try {
                withdrawBean = getConfiguration().readValue(result.toString(), WithdrawBean.class);
                mBankCardNum.setText(withdrawBean.getResultData().getBankcardno());
                mBankName.setText(withdrawBean.getResultData().getBankusername());
                ImageLoader.getInstance().displayImage(withdrawBean.getResultData().getBanklogo(), mBankLogo, AppConfig.DEFAULT_IMG_OPTIONS_PERSON);
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
