package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.QqCurrencyBeen;

import java.util.ArrayList;

/**
 * Created by 涂有泽 .
 * Date by 2016/8/5
 * Use by 腾讯充值
 */
public class TencentPayActivity extends BaseActivity{

    private RelativeLayout back;
    private TextView qBi,qVip,qRed,qYellow,qBlue,qPurple,qGreen,qType,payOne,payTwo,payThree,payFour,payFive,paySix;
    private EditText qNum;
    private TextView qTotalMoney;
    private TextView qPay;
    private ArrayList<QqCurrencyBeen> beenlist;
    private int payMoney = 5;
    private int totalMoney = 5;
    private String cardid = "220614";
    private int TYPE_FLAG = 1;
    private int NUM_FLAG = 1;
    public static int FLAG_LIFE_QQ = 1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what){
                case 1:
                    qTotalMoney.setText(totalMoney+"元");
                    break;
            }
        }
    };

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_tencent_pay);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        beenlist=new ArrayList<QqCurrencyBeen>();
        FLAG_LIFE_QQ = 1;
        back = (RelativeLayout) findViewById(R.id.tencent_back);
        qBi = (TextView) findViewById(R.id.q_bi_txt);
        qVip = (TextView) findViewById(R.id.q_vip_txt);
        qRed = (TextView) findViewById(R.id.q_red_txt);
        qYellow = (TextView) findViewById(R.id.q_yellow_txt);
        qBlue = (TextView) findViewById(R.id.q_blue_txt);
        qPurple = (TextView) findViewById(R.id.q_purple_txt);
        qGreen = (TextView) findViewById(R.id.q_green_txt);

        qType = (TextView) findViewById(R.id.q_type_txt);

        payOne = (TextView) findViewById(R.id.pay_one);
        payTwo = (TextView) findViewById(R.id.pay_two);
        payThree = (TextView) findViewById(R.id.pay_three);
        payFour = (TextView) findViewById(R.id.pay_four);
        payFive = (TextView) findViewById(R.id.pay_five);
        paySix = (TextView) findViewById(R.id.pay_six);

        qNum = (EditText) findViewById(R.id.q_num_et);
        qTotalMoney = (TextView) findViewById(R.id.q_total_money);
        qPay = (TextView) findViewById(R.id.q_pay_txt);
        getMoneyAndCardId();
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        back.setOnClickListener(this);
        qBi.setOnClickListener(this);
        qVip.setOnClickListener(this);
        qRed.setOnClickListener(this);
        qYellow.setOnClickListener(this);
        qBlue.setOnClickListener(this);
        qPurple.setOnClickListener(this);
        qGreen.setOnClickListener(this);
        payOne.setOnClickListener(this);
        payTwo.setOnClickListener(this);
        payThree.setOnClickListener(this);
        payFour.setOnClickListener(this);
        payFive.setOnClickListener(this);
        paySix.setOnClickListener(this);
        qPay.setOnClickListener(this);

    }

    /*判断获取当前支付的金额和cardid*/
    private void getMoneyAndCardId(){
        if(TYPE_FLAG ==1){
            beenlist= Wethod.setList(beenlist, 5);//Q币
            if(NUM_FLAG == 1){
                cardid = beenlist.get(0).getCardid();
                totalMoney = beenlist.get(0).getCardmoney();
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 2){
                cardid = beenlist.get(1).getCardid();
                totalMoney = beenlist.get(1).getCardmoney();
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 3){
                cardid = beenlist.get(2).getCardid();
                totalMoney = beenlist.get(2).getCardmoney();
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 4){
                cardid = beenlist.get(3).getCardid();
                totalMoney = beenlist.get(3).getCardmoney();
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 5){
                cardid = beenlist.get(4).getCardid();
                totalMoney = beenlist.get(4).getCardmoney();
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 6){
                cardid = beenlist.get(5).getCardid();
                totalMoney = beenlist.get(5).getCardmoney();
                mHandler.sendEmptyMessage(1);
            }
        }else if(TYPE_FLAG == 2){
            beenlist= Wethod.setList(beenlist, 9);//QQ会员
            if(NUM_FLAG == 1){
                cardid = "222309";
                totalMoney = 10*1;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 2){
                cardid = "222309";
                totalMoney = 10*2;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 3){
                cardid = "222309";
                totalMoney = 10*3;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 4){
                cardid = "222309";
                totalMoney = 10*6;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 5){
                cardid = "222309";
                totalMoney = 10*9;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 6){
                cardid = "222309";
                totalMoney = 10*12;
                mHandler.sendEmptyMessage(1);
            }

        }else if(TYPE_FLAG == 3){
            if(NUM_FLAG == 1){
                cardid = "222301";
                totalMoney = 10*1;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 2){
                cardid = "222301";
                totalMoney = 10*2;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 3){
                cardid = "222301";
                totalMoney = 10*3;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 4){
                cardid = "222301";
                totalMoney = 10*6;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 5){
                cardid = "222301";
                totalMoney = 10*9;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 6){
                cardid = "222301";
                totalMoney = 10*12;
                mHandler.sendEmptyMessage(1);
            }

        }else if(TYPE_FLAG == 4){
            if(NUM_FLAG == 1){
                cardid = "222302";
                totalMoney = 10*1;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 2){
                cardid = "222302";
                totalMoney = 10*2;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 3){
                cardid = "222302";
                totalMoney = 10*3;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 4){
                cardid = "222302";
                totalMoney = 10*6;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 5){
                cardid = "222302";
                totalMoney = 10*9;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 6){
                cardid = "222302";
                totalMoney = 10*12;
                mHandler.sendEmptyMessage(1);
            }

        }else if(TYPE_FLAG == 5){
            if(NUM_FLAG == 1){
                cardid = "222308";
                totalMoney = 10*1;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 2){
                cardid = "222308";
                totalMoney = 10*2;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 3){
                cardid = "222308";
                totalMoney = 10*3;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 4){
                cardid = "222308";
                totalMoney = 10*6;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 5){
                cardid = "222308";
                totalMoney = 10*9;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 6){
                cardid = "222308";
                totalMoney = 10*12;
                mHandler.sendEmptyMessage(1);
            }

        }else if(TYPE_FLAG == 6){
            if(NUM_FLAG == 1){
                cardid = "222304";
                totalMoney = 10*1;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 2){
                cardid = "222304";
                totalMoney = 10*2;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 3){
                cardid = "222304";
                totalMoney = 10*3;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 4){
                cardid = "222304";
                totalMoney = 10*6;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 5){
                cardid = "222304";
                totalMoney = 10*9;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 6){
                cardid = "222304";
                totalMoney = 10*12;
                mHandler.sendEmptyMessage(1);
            }

        }else if(TYPE_FLAG == 7){
            if(NUM_FLAG == 1){
                cardid = "222306";
                totalMoney = 10*1;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 2){
                cardid = "222306";
                totalMoney = 10*2;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 3){
                cardid = "222306";
                totalMoney = 10*3;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 4){
                cardid = "222306";
                totalMoney = 10*6;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 5){
                cardid = "222306";
                totalMoney = 10*9;
                mHandler.sendEmptyMessage(1);
            }else if(NUM_FLAG == 6){
                cardid = "222306";
                totalMoney = 10*12;
                mHandler.sendEmptyMessage(1);
            }

        }
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.q_bi_txt:
                /*点击了Q币充值，其他选项为未点击状态*/

                qBi.setBackgroundResource(R.mipmap.qq_back_click);
                qBi.setTextColor(Color.parseColor("#FFFFFF"));
                qVip.setBackgroundResource(R.mipmap.qq_back_noclick);
                qVip.setTextColor(Color.parseColor("#59CDF0"));
                qRed.setBackgroundResource(R.mipmap.qq_back_noclick);
                qRed.setTextColor(Color.parseColor("#59CDF0"));
                qYellow.setBackgroundResource(R.mipmap.qq_back_noclick);
                qYellow.setTextColor(Color.parseColor("#59CDF0"));
                qBlue.setBackgroundResource(R.mipmap.qq_back_noclick);
                qBlue.setTextColor(Color.parseColor("#59CDF0"));
                qPurple.setBackgroundResource(R.mipmap.qq_back_noclick);
                qPurple.setTextColor(Color.parseColor("#59CDF0"));
                qGreen.setBackgroundResource(R.mipmap.qq_back_noclick);
                qGreen.setTextColor(Color.parseColor("#59CDF0"));

                payOne.setText("5元");
                payTwo.setText("10元");
                payThree.setText("20元");
                payFour.setText("30元");
                payFive.setText("50元");
                paySix.setText("100元");
                TYPE_FLAG = 1;
                NUM_FLAG = 1;
                getMoneyAndCardId();
                break;
            case R.id.q_vip_txt:

                qBi.setBackgroundResource(R.mipmap.qq_back_noclick);
                qBi.setTextColor(Color.parseColor("#59CDF0"));
                qVip.setBackgroundResource(R.mipmap.qq_back_click);
                qVip.setTextColor(Color.parseColor("#FFFFFF"));
                qRed.setBackgroundResource(R.mipmap.qq_back_noclick);
                qRed.setTextColor(Color.parseColor("#59CDF0"));
                qYellow.setBackgroundResource(R.mipmap.qq_back_noclick);
                qYellow.setTextColor(Color.parseColor("#59CDF0"));
                qBlue.setBackgroundResource(R.mipmap.qq_back_noclick);
                qBlue.setTextColor(Color.parseColor("#59CDF0"));
                qPurple.setBackgroundResource(R.mipmap.qq_back_noclick);
                qPurple.setTextColor(Color.parseColor("#59CDF0"));
                qGreen.setBackgroundResource(R.mipmap.qq_back_noclick);
                qGreen.setTextColor(Color.parseColor("#59CDF0"));

                payOne.setText("1个月");
                payTwo.setText("2个月");
                payThree.setText("3个月");
                payFour.setText("6个月");
                payFive.setText("9个月");
                paySix.setText("12个月");

                payOne.setBackgroundResource(R.mipmap.qq_back_click);
                payOne.setTextColor(Color.parseColor("#FFFFFF"));
                payTwo.setBackgroundResource(R.mipmap.qq_back_noclick);
                payTwo.setTextColor(Color.parseColor("#59CDF0"));
                payThree.setBackgroundResource(R.mipmap.qq_back_noclick);
                payThree.setTextColor(Color.parseColor("#59CDF0"));
                payFour.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFour.setTextColor(Color.parseColor("#59CDF0"));
                payFive.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFive.setTextColor(Color.parseColor("#59CDF0"));
                paySix.setBackgroundResource(R.mipmap.qq_back_noclick);
                paySix.setTextColor(Color.parseColor("#59CDF0"));

                TYPE_FLAG =2;
                NUM_FLAG = 1;
                getMoneyAndCardId();
                break;
            case R.id.q_red_txt:

                qBi.setBackgroundResource(R.mipmap.qq_back_noclick);
                qBi.setTextColor(Color.parseColor("#59CDF0"));
                qVip.setBackgroundResource(R.mipmap.qq_back_noclick);
                qVip.setTextColor(Color.parseColor("#59CDF0"));
                qRed.setBackgroundResource(R.mipmap.qq_back_click);
                qRed.setTextColor(Color.parseColor("#FFFFFF"));
                qYellow.setBackgroundResource(R.mipmap.qq_back_noclick);
                qYellow.setTextColor(Color.parseColor("#59CDF0"));
                qBlue.setBackgroundResource(R.mipmap.qq_back_noclick);
                qBlue.setTextColor(Color.parseColor("#59CDF0"));
                qPurple.setBackgroundResource(R.mipmap.qq_back_noclick);
                qPurple.setTextColor(Color.parseColor("#59CDF0"));
                qGreen.setBackgroundResource(R.mipmap.qq_back_noclick);
                qGreen.setTextColor(Color.parseColor("#59CDF0"));

                payOne.setText("1个月");
                payTwo.setText("2个月");
                payThree.setText("3个月");
                payFour.setText("6个月");
                payFive.setText("9个月");
                paySix.setText("12个月");

                payOne.setBackgroundResource(R.mipmap.qq_back_click);
                payOne.setTextColor(Color.parseColor("#FFFFFF"));
                payTwo.setBackgroundResource(R.mipmap.qq_back_noclick);
                payTwo.setTextColor(Color.parseColor("#59CDF0"));
                payThree.setBackgroundResource(R.mipmap.qq_back_noclick);
                payThree.setTextColor(Color.parseColor("#59CDF0"));
                payFour.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFour.setTextColor(Color.parseColor("#59CDF0"));
                payFive.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFive.setTextColor(Color.parseColor("#59CDF0"));
                paySix.setBackgroundResource(R.mipmap.qq_back_noclick);
                paySix.setTextColor(Color.parseColor("#59CDF0"));

                TYPE_FLAG = 3;
                NUM_FLAG = 1;
                getMoneyAndCardId();
                break;
            case R.id.q_yellow_txt:

                qBi.setBackgroundResource(R.mipmap.qq_back_noclick);
                qBi.setTextColor(Color.parseColor("#59CDF0"));
                qVip.setBackgroundResource(R.mipmap.qq_back_noclick);
                qVip.setTextColor(Color.parseColor("#59CDF0"));
                qRed.setBackgroundResource(R.mipmap.qq_back_noclick);
                qRed.setTextColor(Color.parseColor("#59CDF0"));
                qYellow.setBackgroundResource(R.mipmap.qq_back_click);
                qYellow.setTextColor(Color.parseColor("#FFFFFF"));
                qBlue.setBackgroundResource(R.mipmap.qq_back_noclick);
                qBlue.setTextColor(Color.parseColor("#59CDF0"));
                qPurple.setBackgroundResource(R.mipmap.qq_back_noclick);
                qPurple.setTextColor(Color.parseColor("#59CDF0"));
                qGreen.setBackgroundResource(R.mipmap.qq_back_noclick);
                qGreen.setTextColor(Color.parseColor("#59CDF0"));

                payOne.setText("1个月");
                payTwo.setText("2个月");
                payThree.setText("3个月");
                payFour.setText("6个月");
                payFive.setText("9个月");
                paySix.setText("12个月");

                payOne.setBackgroundResource(R.mipmap.qq_back_click);
                payOne.setTextColor(Color.parseColor("#FFFFFF"));
                payTwo.setBackgroundResource(R.mipmap.qq_back_noclick);
                payTwo.setTextColor(Color.parseColor("#59CDF0"));
                payThree.setBackgroundResource(R.mipmap.qq_back_noclick);
                payThree.setTextColor(Color.parseColor("#59CDF0"));
                payFour.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFour.setTextColor(Color.parseColor("#59CDF0"));
                payFive.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFive.setTextColor(Color.parseColor("#59CDF0"));
                paySix.setBackgroundResource(R.mipmap.qq_back_noclick);
                paySix.setTextColor(Color.parseColor("#59CDF0"));

                TYPE_FLAG = 4;
                NUM_FLAG = 1;
                getMoneyAndCardId();
                break;
            case R.id.q_blue_txt:


                qBi.setBackgroundResource(R.mipmap.qq_back_noclick);
                qBi.setTextColor(Color.parseColor("#59CDF0"));
                qVip.setBackgroundResource(R.mipmap.qq_back_noclick);
                qVip.setTextColor(Color.parseColor("#59CDF0"));
                qRed.setBackgroundResource(R.mipmap.qq_back_noclick);
                qRed.setTextColor(Color.parseColor("#59CDF0"));
                qYellow.setBackgroundResource(R.mipmap.qq_back_noclick);
                qYellow.setTextColor(Color.parseColor("#59CDF0"));
                qBlue.setBackgroundResource(R.mipmap.qq_back_click);
                qBlue.setTextColor(Color.parseColor("#FFFFFF"));
                qPurple.setBackgroundResource(R.mipmap.qq_back_noclick);
                qPurple.setTextColor(Color.parseColor("#59CDF0"));
                qGreen.setBackgroundResource(R.mipmap.qq_back_noclick);
                qGreen.setTextColor(Color.parseColor("#59CDF0"));

                payOne.setText("1个月");
                payTwo.setText("2个月");
                payThree.setText("3个月");
                payFour.setText("6个月");
                payFive.setText("9个月");
                paySix.setText("12个月");

                payOne.setBackgroundResource(R.mipmap.qq_back_click);
                payOne.setTextColor(Color.parseColor("#FFFFFF"));
                payTwo.setBackgroundResource(R.mipmap.qq_back_noclick);
                payTwo.setTextColor(Color.parseColor("#59CDF0"));
                payThree.setBackgroundResource(R.mipmap.qq_back_noclick);
                payThree.setTextColor(Color.parseColor("#59CDF0"));
                payFour.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFour.setTextColor(Color.parseColor("#59CDF0"));
                payFive.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFive.setTextColor(Color.parseColor("#59CDF0"));
                paySix.setBackgroundResource(R.mipmap.qq_back_noclick);
                paySix.setTextColor(Color.parseColor("#59CDF0"));

                TYPE_FLAG = 5;
                NUM_FLAG = 1;
                getMoneyAndCardId();
                break;
            case R.id.q_purple_txt:


                qBi.setBackgroundResource(R.mipmap.qq_back_noclick);
                qBi.setTextColor(Color.parseColor("#59CDF0"));
                qVip.setBackgroundResource(R.mipmap.qq_back_noclick);
                qVip.setTextColor(Color.parseColor("#59CDF0"));
                qRed.setBackgroundResource(R.mipmap.qq_back_noclick);
                qRed.setTextColor(Color.parseColor("#59CDF0"));
                qYellow.setBackgroundResource(R.mipmap.qq_back_noclick);
                qYellow.setTextColor(Color.parseColor("#59CDF0"));
                qBlue.setBackgroundResource(R.mipmap.qq_back_noclick);
                qBlue.setTextColor(Color.parseColor("#59CDF0"));
                qPurple.setBackgroundResource(R.mipmap.qq_back_click);
                qPurple.setTextColor(Color.parseColor("#FFFFFF"));
                qGreen.setBackgroundResource(R.mipmap.qq_back_noclick);
                qGreen.setTextColor(Color.parseColor("#59CDF0"));

                payOne.setText("1个月");
                payTwo.setText("2个月");
                payThree.setText("3个月");
                payFour.setText("6个月");
                payFive.setText("9个月");
                paySix.setText("12个月");

                payOne.setBackgroundResource(R.mipmap.qq_back_click);
                payOne.setTextColor(Color.parseColor("#FFFFFF"));
                payTwo.setBackgroundResource(R.mipmap.qq_back_noclick);
                payTwo.setTextColor(Color.parseColor("#59CDF0"));
                payThree.setBackgroundResource(R.mipmap.qq_back_noclick);
                payThree.setTextColor(Color.parseColor("#59CDF0"));
                payFour.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFour.setTextColor(Color.parseColor("#59CDF0"));
                payFive.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFive.setTextColor(Color.parseColor("#59CDF0"));
                paySix.setBackgroundResource(R.mipmap.qq_back_noclick);
                paySix.setTextColor(Color.parseColor("#59CDF0"));

                TYPE_FLAG = 6;
                NUM_FLAG = 1;
                getMoneyAndCardId();
                break;
            case R.id.q_green_txt:

                qBi.setBackgroundResource(R.mipmap.qq_back_noclick);
                qBi.setTextColor(Color.parseColor("#59CDF0"));
                qVip.setBackgroundResource(R.mipmap.qq_back_noclick);
                qVip.setTextColor(Color.parseColor("#59CDF0"));
                qRed.setBackgroundResource(R.mipmap.qq_back_noclick);
                qRed.setTextColor(Color.parseColor("#59CDF0"));
                qYellow.setBackgroundResource(R.mipmap.qq_back_noclick);
                qYellow.setTextColor(Color.parseColor("#59CDF0"));
                qBlue.setBackgroundResource(R.mipmap.qq_back_noclick);
                qBlue.setTextColor(Color.parseColor("#59CDF0"));
                qPurple.setBackgroundResource(R.mipmap.qq_back_noclick);
                qPurple.setTextColor(Color.parseColor("#59CDF0"));
                qGreen.setBackgroundResource(R.mipmap.qq_back_click);
                qGreen.setTextColor(Color.parseColor("#FFFFFF"));

                payOne.setText("1个月");
                payTwo.setText("2个月");
                payThree.setText("3个月");
                payFour.setText("6个月");
                payFive.setText("9个月");
                paySix.setText("12个月");

                payOne.setBackgroundResource(R.mipmap.qq_back_click);
                payOne.setTextColor(Color.parseColor("#FFFFFF"));
                payTwo.setBackgroundResource(R.mipmap.qq_back_noclick);
                payTwo.setTextColor(Color.parseColor("#59CDF0"));
                payThree.setBackgroundResource(R.mipmap.qq_back_noclick);
                payThree.setTextColor(Color.parseColor("#59CDF0"));
                payFour.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFour.setTextColor(Color.parseColor("#59CDF0"));
                payFive.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFive.setTextColor(Color.parseColor("#59CDF0"));
                paySix.setBackgroundResource(R.mipmap.qq_back_noclick);
                paySix.setTextColor(Color.parseColor("#59CDF0"));

                TYPE_FLAG = 7;
                NUM_FLAG = 1;
                getMoneyAndCardId();
                break;
            case R.id.pay_one:

                payOne.setBackgroundResource(R.mipmap.qq_back_click);
                payOne.setTextColor(Color.parseColor("#FFFFFF"));
                payTwo.setBackgroundResource(R.mipmap.qq_back_noclick);
                payTwo.setTextColor(Color.parseColor("#59CDF0"));
                payThree.setBackgroundResource(R.mipmap.qq_back_noclick);
                payThree.setTextColor(Color.parseColor("#59CDF0"));
                payFour.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFour.setTextColor(Color.parseColor("#59CDF0"));
                payFive.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFive.setTextColor(Color.parseColor("#59CDF0"));
                paySix.setBackgroundResource(R.mipmap.qq_back_noclick);
                paySix.setTextColor(Color.parseColor("#59CDF0"));

                NUM_FLAG = 1;
                getMoneyAndCardId();
                break;
            case R.id.pay_two:

                payOne.setBackgroundResource(R.mipmap.qq_back_noclick);
                payOne.setTextColor(Color.parseColor("#59CDF0"));
                payTwo.setBackgroundResource(R.mipmap.qq_back_click);
                payTwo.setTextColor(Color.parseColor("#FFFFFF"));
                payThree.setBackgroundResource(R.mipmap.qq_back_noclick);
                payThree.setTextColor(Color.parseColor("#59CDF0"));
                payFour.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFour.setTextColor(Color.parseColor("#59CDF0"));
                payFive.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFive.setTextColor(Color.parseColor("#59CDF0"));
                paySix.setBackgroundResource(R.mipmap.qq_back_noclick);
                paySix.setTextColor(Color.parseColor("#59CDF0"));

                NUM_FLAG = 2;
                getMoneyAndCardId();
                break;
            case R.id.pay_three:

                payOne.setBackgroundResource(R.mipmap.qq_back_noclick);
                payOne.setTextColor(Color.parseColor("#59CDF0"));
                payTwo.setBackgroundResource(R.mipmap.qq_back_noclick);
                payTwo.setTextColor(Color.parseColor("#59CDF0"));
                payThree.setBackgroundResource(R.mipmap.qq_back_click);
                payThree.setTextColor(Color.parseColor("#FFFFFF"));
                payFour.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFour.setTextColor(Color.parseColor("#59CDF0"));
                payFive.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFive.setTextColor(Color.parseColor("#59CDF0"));
                paySix.setBackgroundResource(R.mipmap.qq_back_noclick);
                paySix.setTextColor(Color.parseColor("#59CDF0"));

                NUM_FLAG = 3;
                getMoneyAndCardId();
                break;
            case R.id.pay_four:

                payOne.setBackgroundResource(R.mipmap.qq_back_noclick);
                payOne.setTextColor(Color.parseColor("#59CDF0"));
                payTwo.setBackgroundResource(R.mipmap.qq_back_noclick);
                payTwo.setTextColor(Color.parseColor("#59CDF0"));
                payThree.setBackgroundResource(R.mipmap.qq_back_noclick);
                payThree.setTextColor(Color.parseColor("#59CDF0"));
                payFour.setBackgroundResource(R.mipmap.qq_back_click);
                payFour.setTextColor(Color.parseColor("#FFFFFF"));
                payFive.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFive.setTextColor(Color.parseColor("#59CDF0"));
                paySix.setBackgroundResource(R.mipmap.qq_back_noclick);
                paySix.setTextColor(Color.parseColor("#59CDF0"));

                NUM_FLAG = 4;
                getMoneyAndCardId();
                break;
            case R.id.pay_five:

                payOne.setBackgroundResource(R.mipmap.qq_back_noclick);
                payOne.setTextColor(Color.parseColor("#59CDF0"));
                payTwo.setBackgroundResource(R.mipmap.qq_back_noclick);
                payTwo.setTextColor(Color.parseColor("#59CDF0"));
                payThree.setBackgroundResource(R.mipmap.qq_back_noclick);
                payThree.setTextColor(Color.parseColor("#59CDF0"));
                payFour.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFour.setTextColor(Color.parseColor("#59CDF0"));
                payFive.setBackgroundResource(R.mipmap.qq_back_click);
                payFive.setTextColor(Color.parseColor("#FFFFFF"));
                paySix.setBackgroundResource(R.mipmap.qq_back_noclick);
                paySix.setTextColor(Color.parseColor("#59CDF0"));

                NUM_FLAG = 5;
                getMoneyAndCardId();
                break;
            case R.id.pay_six:

                payOne.setBackgroundResource(R.mipmap.qq_back_noclick);
                payOne.setTextColor(Color.parseColor("#59CDF0"));
                payTwo.setBackgroundResource(R.mipmap.qq_back_noclick);
                payTwo.setTextColor(Color.parseColor("#59CDF0"));
                payThree.setBackgroundResource(R.mipmap.qq_back_noclick);
                payThree.setTextColor(Color.parseColor("#59CDF0"));
                payFour.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFour.setTextColor(Color.parseColor("#59CDF0"));
                payFive.setBackgroundResource(R.mipmap.qq_back_noclick);
                payFive.setTextColor(Color.parseColor("#59CDF0"));
                paySix.setBackgroundResource(R.mipmap.qq_back_click);
                paySix.setTextColor(Color.parseColor("#FFFFFF"));

                NUM_FLAG = 6;
                getMoneyAndCardId();
                break;
            case R.id.q_pay_txt:
                if(qNum.getText().toString()==null||qNum.getText().toString().equals("")){
                    Toast.makeText(this,"请输入QQ号码",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(this,QqSurePayActivity.class);
                    intent.putExtra("FLAG",1);
                    intent.putExtra("qqNum",qNum.getText().toString());
                    if(TYPE_FLAG == 1){
                        intent.putExtra("qqType","Q币充值");
                        intent.putExtra("consumeChildType","5");
                    }else if(TYPE_FLAG == 2){
                        intent.putExtra("qqType","QQ会员");
                        intent.putExtra("consumeChildType","9");
                    }else if(TYPE_FLAG == 3){
                        intent.putExtra("qqType","红钻");
                        intent.putExtra("consumeChildType","6");
                    }else if(TYPE_FLAG == 4){
                        intent.putExtra("qqType","黄钻");
                        intent.putExtra("consumeChildType","12");
                    }else if(TYPE_FLAG == 5){
                        intent.putExtra("qqType","蓝钻");
                        intent.putExtra("consumeChildType","11");
                    }else if(TYPE_FLAG == 6){
                        intent.putExtra("qqType","紫钻");
                        intent.putExtra("consumeChildType","7");
                    }else if(TYPE_FLAG == 7){
                        intent.putExtra("qqType","绿钻");
                        intent.putExtra("consumeChildType","8");
                    }
                    intent.putExtra("mobilePhone","123");
                    intent.putExtra("cardid",cardid);
                    intent.putExtra("totalMoney",totalMoney);
                    startActivity(intent);

                }
                break;

            case R.id.tencent_back:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FLAG_LIFE_QQ == 2){
            finish();
            FLAG_LIFE_QQ = 1;
        }
    }
}
