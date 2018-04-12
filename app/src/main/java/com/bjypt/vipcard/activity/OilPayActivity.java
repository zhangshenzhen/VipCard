package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.utils.AES;

import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/8/5
 * Use by 中石化加油卡充值
 */
public class OilPayActivity extends BaseActivity implements VolleyCallBack{

    private RelativeLayout mBack;
    private TextView mCardNum,mCardPhoneNum,mCardNews,mWatchNews,mOne,mTwo,mThree,mCardName;
    private LinearLayout mCardNewsLinear;
    private TextView mPay;
    private View first;
    private int TYPE_FLAG = 1;
    public static int FLAG_LIFE_OIL = 1;
    private int FLAG ;//1---中石油   2--中石化
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_oli);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        intent.getIntExtra("FLAG",0);
    }

    @Override
    public void initView() {
        FLAG_LIFE_OIL = 1;
        mBack = (RelativeLayout) findViewById(R.id.rl_printer_back_oli);
        mCardNum = (TextView) findViewById(R.id.oli_card_num);//加油卡卡号
        mCardPhoneNum = (TextView) findViewById(R.id.oli_phone_num);//手机号码
        mCardNews = (TextView) findViewById(R.id.oli_card_news);//油卡信息
        mWatchNews = (TextView) findViewById(R.id.watch_news);//立即查看
        mOne = (TextView) findViewById(R.id.pay_first_oli);//100元充值
        mTwo = (TextView) findViewById(R.id.pay_second_oli);//500元充值
        mThree = (TextView) findViewById(R.id.pay_third_oli);//1000元充值

        mCardNewsLinear = (LinearLayout) findViewById(R.id.card_man_news);//点击查看信息弹出的
        mCardName = (TextView) findViewById(R.id.card_name);//持卡人姓名
        first = findViewById(R.id.first);//点击查看信息时弹出的

        mPay = (TextView) findViewById(R.id.oli_pay);

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
        mPay.setOnClickListener(this);
        mWatchNews.setOnClickListener(this);
        mOne.setOnClickListener(this);
        mTwo.setOnClickListener(this);
        mThree.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.rl_printer_back_oli:
                finish();
                break;
            case R.id.watch_news:
                Map<String,String> params = new HashMap<>();
                params.put("phoneno",AES.encrypt(getFromSharePreference(Config.userConfig.phoneno),AES.key));
                params.put("userId", AES.encrypt(getFromSharePreference(Config.userConfig.pkregister),AES.key));
                params.put("gameUserid", AES.encrypt(mCardNum.getText().toString(), AES.key));
                params.put("mobile", AES.encrypt(mCardPhoneNum.getText().toString(), AES.key));
                Wethod.httpPost(OilPayActivity.this,1111, Config.web.life_base + "of/recharge/queryCardInfo", params, this);

                break;
            case R.id.pay_first_oli:
                mOne.setBackgroundResource(R.mipmap.qq_back_click);
                mOne.setTextColor(Color.parseColor("#FFFFFF"));
                mTwo.setBackgroundResource(R.mipmap.qq_back_noclick);
                mTwo.setTextColor(Color.parseColor("#59CDF0"));
                mThree.setBackgroundResource(R.mipmap.qq_back_noclick);
                mThree.setTextColor(Color.parseColor("#59CDF0"));
                TYPE_FLAG = 1;
                break;
            case R.id.pay_second_oli:
                mOne.setBackgroundResource(R.mipmap.qq_back_noclick);
                mOne.setTextColor(Color.parseColor("#59CDF0"));
                mTwo.setBackgroundResource(R.mipmap.qq_back_click);
                mTwo.setTextColor(Color.parseColor("#FFFFFF"));
                mThree.setBackgroundResource(R.mipmap.qq_back_noclick);
                mThree.setTextColor(Color.parseColor("#59CDF0"));
                TYPE_FLAG = 2;
                break;
            case R.id.pay_third_oli:
                mOne.setBackgroundResource(R.mipmap.qq_back_noclick);
                mOne.setTextColor(Color.parseColor("#59CDF0"));
                mTwo.setBackgroundResource(R.mipmap.qq_back_noclick);
                mTwo.setTextColor(Color.parseColor("#59CDF0"));
                mThree.setBackgroundResource(R.mipmap.qq_back_click);
                mThree.setTextColor(Color.parseColor("#FFFFFF"));
                TYPE_FLAG = 3;
                break;
            case R.id.oli_pay:
                Intent intent = new Intent(this,QqSurePayActivity.class);
                intent.putExtra("FLAG",2);//1--QQ  2--中石化  3--中石油
                intent.putExtra("qqNum",mCardNum.getText().toString());
                intent.putExtra("qqType","中石化油卡充值");
                if(FLAG == 1){
                    intent.putExtra("consumeChildType","1");
                }else {
                    intent.putExtra("consumeChildType","2");
                }

                if(TYPE_FLAG == 1){
                    if(FLAG == 1){
                        intent.putExtra("cardid","64349102");
                    }else{
                        intent.putExtra("cardid","64157004");
                    }
                    intent.putExtra("totalMoney",100);
                }else if(TYPE_FLAG == 2){
                    if(FLAG == 1){
                        intent.putExtra("cardid","64349102");
                    }else{
                        intent.putExtra("cardid","64157002");
                    }
                    intent.putExtra("cardid","64157002");
                    intent.putExtra("totalMoney",500);
                }else if(TYPE_FLAG == 3){
                    if(FLAG == 1){
                        intent.putExtra("cardid","64349102");
                    }else {
                        intent.putExtra("cardid","64157001");
                    }
                    intent.putExtra("totalMoney",1000);
                }
                intent.putExtra("mobilePhone",mCardPhoneNum.getText().toString());
                startActivity(intent);

                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if(reqcode == 1111){
            mCardNewsLinear.setVisibility(View.VISIBLE);
            first.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FLAG_LIFE_OIL == 2){
            finish();
            FLAG_LIFE_OIL =1;
        }
    }
}
