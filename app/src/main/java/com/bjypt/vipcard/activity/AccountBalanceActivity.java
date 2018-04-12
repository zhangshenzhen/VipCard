package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.view.LoadingFragDialog;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/5.
 */
public class AccountBalanceActivity extends BaseActivity implements VolleyCallBack {

    private RelativeLayout relative_balanceOutMoney;
    private RelativeLayout relative_back;
    private RelativeLayout relative_Pay;
    private RelativeLayout relative_balance;
    private RelativeLayout relative_hint;
    private RelativeLayout rl_huiyuan_bi;

    private String pkmuser, isBindingBank, balance;
    private TextView system_balance;
    private TextView tv_balanceIncome;
    private TextView tv_yesterdayIncome;
    private TextView tv_hint;
    private TextView tv_huiyuan_money;

    private ImageView iv_hint;
    private boolean isShow = true;

    //控制图片旋转
    private final int ROTARE_GO = 0;
    private final int ROTATE_BACK = 1;

    private String yesterdayIncome;
    private String grade;
    private String balanceIncome;

    private SystemInfomationBean systemInfomationBean;
//    private LoadingFragDialog mFragDialog;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_account_balance);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(this,1, Config.web.system_infomatiob, params, this);
//        mFragDialog.showDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

    @Override
    public void initView() {

        relative_balanceOutMoney = (RelativeLayout) findViewById(R.id.relative_balanceOutMoney);//账户余额
        relative_back = (RelativeLayout) findViewById(R.id.layout_back);
        relative_Pay = (RelativeLayout) findViewById(R.id.relative_Pay);//充值
        system_balance = (TextView) findViewById(R.id.system_balance);//提现
        tv_huiyuan_money = (TextView) findViewById(R.id.tv_huiyuan_money);//提现

        relative_balance = (RelativeLayout) findViewById(R.id.relative_balance);
        rl_huiyuan_bi = (RelativeLayout) findViewById(R.id.rl_huiyuan_bi);

        relative_hint = (RelativeLayout) findViewById(R.id.relative_hint);

        tv_balanceIncome = (TextView) findViewById(R.id.tv_balanceIncome);//余额累计收入
        tv_yesterdayIncome = (TextView) findViewById(R.id.tv_yesterdayIncome);//昨日累计收入
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        iv_hint = (ImageView) findViewById(R.id.iv_hint);

//        mFragDialog=new LoadingFragDialog(this,R.anim.loadingpage,R.style.MyDialogStyle);

    }

    @Override
    public void afterInitView() {

        setAllTextColor();
        setRotateImg(ROTATE_BACK);
    }

    @Override
    public void bindListener() {


        relative_back.setOnClickListener(this);
        relative_hint.setOnClickListener(this);

    }

    @Override
    public void onClickEvent(View v) {

        switch (v.getId()) {
            case R.id.relative_balanceOutMoney://余额提现
                if (isBindingBank.equals("Y")) {
                    startActivity(new Intent(AccountBalanceActivity.this, WithdrawActivity.class));
                } else {
                    startActivity(new Intent(AccountBalanceActivity.this, BindBankCardActivity.class));
                    Toast.makeText(this, "您还没有绑定银行卡,请先绑定银行卡", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.layout_back://返回

                this.finish();
                break;
            case R.id.relative_Pay://充值
//                if(isBindingBank.equals("Y")){
                Intent intent = new Intent(this, TopupAmountActivity.class);
                intent.putExtra("FLAG", 2);
                intent.putExtra("pkmuser", pkmuser);
                //此时还需要在此页面调用一个获取余额的接口，并且在此时会返回一个系统主键，传递到下一个页面1
                startActivity(intent);
//                }else{
//                    Toast.makeText(this,"您还没有绑定银行卡,请先绑定银行卡",Toast.LENGTH_LONG).show();
//                }

                break;
            case R.id.relative_hint:

                if (isShow) {
                    setRotateImg(ROTATE_BACK);
                } else {
                    setRotateImg(ROTARE_GO);
                }

                break;
            case R.id.relative_balance:

                Intent in = new Intent(this, VipCenterAccountActivity.class);
                in.putExtra("grade", grade);
                in.putExtra("wholebalance", systemInfomationBean.getResultData().getWholebalance());
                startActivity(in);
                break;
        }
    }


    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
            try {
                systemInfomationBean = getConfiguration().readValue(result.toString(), SystemInfomationBean.class);
                balance = systemInfomationBean.getResultData().getBalance();
                isBindingBank = systemInfomationBean.getResultData().getIsBindingBank();
                yesterdayIncome = systemInfomationBean.getResultData().getInterest();//昨日收益
                balanceIncome = systemInfomationBean.getResultData().getHistorytotalinterest();//总收益
                tv_huiyuan_money.setText(sub(stringToDouble(systemInfomationBean.getResultData().getWholebalance()),stringToDouble(systemInfomationBean.getResultData().getBalance()))+"");
                Log.i("aaa", "" + balanceIncome);
                if (systemInfomationBean.getResultData().getGrade() == null) grade = "普通会员";
                else grade = systemInfomationBean.getResultData().getGrade();

                pkmuser = systemInfomationBean.getResultData().getPkmuser();
                system_balance.setText(balance + "元");
                tv_yesterdayIncome.setText(yesterdayIncome + "元");
                tv_balanceIncome.setText(balanceIncome + "元");

                setAllTextColor();

                relative_Pay.setOnClickListener(this);
                relative_balanceOutMoney.setOnClickListener(this);
                relative_balance.setOnClickListener(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        mFragDialog.cancelDialog();
    }

    @Override
    public void onFailed(int reqcode, Object result) {
//        mFragDialog.cancelDialog();
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    /**
     * 更改所有字体色
     */
    public void setAllTextColor() {
        setTextColor(tv_balanceIncome);
        setTextColor(tv_yesterdayIncome);
        setTextColor(system_balance);
    }

    /**
     * 设置字体颜色
     */
    public void setTextColor(TextView tv) {
        String str = tv.getText().toString();

        SpannableString msp = new SpannableString(str);
        msp.setSpan(new ForegroundColorSpan(Color.GRAY), str.length() - 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(msp);
    }

    /**
     * *设置旋转动画
     */
    public void setRotateImg(int isStart) {

        RotateAnimation animation = null;
        switch (isStart) {
            case ROTARE_GO:
                animation = new RotateAnimation(-90f, 90f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                tv_hint.setVisibility(View.GONE);
                break;
            case ROTATE_BACK:
                animation = new RotateAnimation(90f, -90f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                tv_hint.setVisibility(View.VISIBLE);
                break;
        }

        animation.setDuration(500);
        animation.setFillAfter(true);
//        animation.setRepeatCount(1);
//        iv_hint.setAnimation();

        iv_hint.startAnimation(animation);
//        animation.startNow();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isShow = !isShow;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public void isConntectedAndRefreshData() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(this,1, Config.web.system_infomatiob, params, this);
    }

    public double sub(double v1,double v2){
        if (v1==v2){
            return 0.00;
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        String value = b1.subtract(b2).doubleValue()+"";
        if (value.contains(".")){
            if (value.substring(value.indexOf("."),value.length()).length()>3){
                return Double.parseDouble(value.substring(0,value.indexOf(".")+3));
            }
        }
        return Double.parseDouble(value);
    }
    public double stringToDouble(String value){
        String valusePiont = value.substring(value.indexOf("."),value.length());
        if (value!=null&&!value.equals("")){
            double val;
            if (value.contains(".")&&valusePiont.length()>=3){
                val = Double.parseDouble(value.substring(0,value.indexOf(".")+3));
            }else {
                val = Double.parseDouble(value);
            }
            return val;
        }

        return 0.00;

    }
}
