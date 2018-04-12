package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.dialog.CodePaySuccessDialog;
import com.bjypt.vipcard.model.BalanceOutSuccessBean;
import com.bjypt.vipcard.view.ToastUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BalanceOutActivity extends BaseActivity implements VolleyCallBack<String> {

    private RelativeLayout balance_out_back;                         //返回
    private EditText et_money;                                        //输入框
    private TextView confirm_tv;                                      //确认
    private String pkmuser;
    private static final int request_toPlatformBalance = 1110;
    private String amount;
    private TextView tv_message;
    private String cardNum;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_balance_out);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        pkmuser = intent.getStringExtra("pkmuser");
        amount = intent.getStringExtra("amount");
        cardNum = intent.getStringExtra("cardnum");

    }

    @Override
    public void initView() {
        balance_out_back = (RelativeLayout) findViewById(R.id.balance_out_back);
        et_money = (EditText) findViewById(R.id.et_money);
        confirm_tv = (TextView) findViewById(R.id.confirm_tv);
        tv_message = (TextView) findViewById(R.id.tv_message);
    }

    @Override
    public void afterInitView() {
        String s1 = amount;
        String ss = new String("此商家电子卡余额为%s元，直接转出到平台余额中，实时到账，可用于提现");
        String sss = String.format(ss, s1);
        int s1_start = sss.indexOf(s1);
        int s1_end = s1_start + s1.length() + 1;
        SpannableString spannableString = new SpannableString(sss);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#EB584E")),
                s1_start, s1_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_message.setText(spannableString);
    }

    @Override
    public void bindListener() {
        confirm_tv.setOnClickListener(this);
        balance_out_back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.balance_out_back:
                finish();
                break;
            case R.id.confirm_tv:
                if (!et_money.getText().toString().isEmpty()){
                    if (Double.parseDouble(et_money.getText().toString()) > Double.parseDouble(amount)) {
                        ToastUtil.showToast(this, "您的账户余额不足");
                    } else if (et_money.getText().toString().equals("0")) {
                        ToastUtil.showToast(this,"输入的金额不能为零");
                    } else {
                        requestData();
                    }
                }else {
                    ToastUtil.showToast(this, "请输入金额");
                }

                break;
        }
    }

    public void requestData() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        params.put("pkmuser", pkmuser);
        params.put("cardnum", cardNum);
        params.put("convertAmount", et_money.getText().toString());
        Wethod.httpPost(BalanceOutActivity.this, request_toPlatformBalance, Config.web.card_balance_toPlatformBalance, params, this);

    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == request_toPlatformBalance) {
            try {
                BalanceOutSuccessBean balanceOutSuccessBean = getConfiguration().readValue(result.toString(), BalanceOutSuccessBean.class);

                final CodePaySuccessDialog dialog = new CodePaySuccessDialog(this);
                dialog.setTitle("余额转出成功");
                dialog.setOnNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        BalanceOutActivity.this.finish();
                    }
                });
                dialog.setPositiveButtonVisible(View.GONE);

//                ToastUtil.showToast(this, balanceOutSuccessBean.getResultData());
//                finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        if (reqcode == request_toPlatformBalance) {
            try {
                BalanceOutSuccessBean balanceOutSuccessBean = getConfiguration().readValue(result.toString(), BalanceOutSuccessBean.class);

                ToastUtil.showToast(this, balanceOutSuccessBean.getResultData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
