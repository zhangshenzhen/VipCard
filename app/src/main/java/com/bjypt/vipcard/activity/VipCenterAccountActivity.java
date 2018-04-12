package com.bjypt.vipcard.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.NewBindBankCardActivity;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.umeng.UmengCountContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/20.
 */
public class VipCenterAccountActivity extends BaseActivity implements VolleyCallBack {

    private TextView vipgrade, vipbanlance, tv_interest;
    private Intent in;
    private TextView mfundlist;
    private RelativeLayout layout_back;
    private LinearLayout linear_vc_myaccount;
    private LinearLayout linear_vc_keyong_account;
    private LinearLayout linear_vc_binding_bankcard;
    private LinearLayout linear_vc_tonghuaInfo;
    private LinearLayout linear_vc_shouji_recharge;
    private LinearLayout linear_vc_liuliang_charge;
    private LinearLayout linear_vc_tenxun_recharge;
    private LinearLayout linear_vc_zhognshiyou_recharge;
    private LinearLayout linear_vc_zhongshihua_recharge;
    private LinearLayout linear_vc_lifeservice_recharge;
    private SystemInfomationBean systemInfomationBean;
    public static int FLAG_MY_FRAGMENT = 1;

    private String pkmuser, isBindingBank, balance;
    private LinearLayout total_vip_linear;

    private String Historytotalinterest;             //余额总收益
    private String interest;

    private Handler showPopHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 1:
                    Log.e("HHHH", "handleMessage");
                    setShowBackLogin(linear_vc_binding_bankcard);
                    break;
            }
        }
    };

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_vc_myaccount);
    }

    @Override
    public void beforeInitView() {

        in = getIntent();
        request();
//        mBundle.putString("balance",mBean.getResultData().getBalance());//可用余额
//        mBundle.putString("totalinterest",mBean.getResultData().getTotalinterest());//总收益
//        mBundle.putString("interest", mBean.getResultData().getInterest());//昨日收益
//        mBundle.putString("wholebalance", mBean.getResultData().getWholebalance());//余额
//        mBundle.putString("grade", grade);//等级

    }

    public void request() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(VipCenterAccountActivity.this, 1, Config.web.system_infomatiob, params, this);
    }

    @Override
    public void initView() {
        total_vip_linear = (LinearLayout) findViewById(R.id.total_vip_linear);

        vipgrade = (TextView) findViewById(R.id.vipgrade);//vip等级
        vipbanlance = (TextView) findViewById(R.id.vipbanlance);//收益信息

        layout_back = (RelativeLayout) findViewById(R.id.layout_back);
        tv_interest = (TextView) findViewById(R.id.tv_interest);
        mfundlist = (TextView) findViewById(R.id.tv_fundlist);
        linear_vc_myaccount = (LinearLayout) findViewById(R.id.linear_vc_myaccount);
        linear_vc_keyong_account = (LinearLayout) findViewById(R.id.linear_vc_keyong_account);
        linear_vc_binding_bankcard = (LinearLayout) findViewById(R.id.linear_vc_binding_bankcard);
        linear_vc_tonghuaInfo = (LinearLayout) findViewById(R.id.linear_vc_tonghuaInfo);
        linear_vc_shouji_recharge = (LinearLayout) findViewById(R.id.linear_vc_shouji_recharge);
        linear_vc_liuliang_charge = (LinearLayout) findViewById(R.id.linear_vc_liuliang_charge);
        linear_vc_tenxun_recharge = (LinearLayout) findViewById(R.id.linear_vc_tenxun_recharge);
        linear_vc_zhognshiyou_recharge = (LinearLayout) findViewById(R.id.linear_vc_zhognshiyou_recharge);
        linear_vc_zhongshihua_recharge = (LinearLayout) findViewById(R.id.linear_vc_zhongshihua_recharge);
        linear_vc_lifeservice_recharge = (LinearLayout) findViewById(R.id.linear_vc_lifeservice_recharge);

    }

    @Override
    public void afterInitView() {
//        new MyThread().start();
//        final CustomDialog customDialog = new CustomDialog(this,R.style.customDialog,R.layout.layout_weixin_tishi);
//        customDialog.show();
    }

    @Override
    public void bindListener() {

        layout_back.setOnClickListener(this);
        mfundlist.setOnClickListener(this);
        linear_vc_myaccount.setOnClickListener(this);
        linear_vc_keyong_account.setOnClickListener(this);
        linear_vc_binding_bankcard.setOnClickListener(this);
        linear_vc_tonghuaInfo.setOnClickListener(this);
        linear_vc_shouji_recharge.setOnClickListener(this);
        linear_vc_liuliang_charge.setOnClickListener(this);
        linear_vc_tenxun_recharge.setOnClickListener(this);
        linear_vc_zhognshiyou_recharge.setOnClickListener(this);
        linear_vc_zhongshihua_recharge.setOnClickListener(this);
        linear_vc_lifeservice_recharge.setOnClickListener(this);


    }

    @Override
    public void onClickEvent(View v) {

        Intent mIntent = new Intent();
        switch (v.getId()) {
            case R.id.tv_fundlist:                                      //查询明细
                Intent inte = new Intent(VipCenterAccountActivity.this, RechargeRecordActivity.class);
                inte.putExtra("type", 1);
                startActivity(inte);

                break;
            case R.id.linear_vc_keyong_account:                       //可用余额（我要充值）
                Intent intents = new Intent(this, NewTopupWayActivity.class);
                intents.putExtra("FLAG", 2);
                intents.putExtra("pkmuser", systemInfomationBean.getResultData().getPkmuser());
                startActivity(intents);
                /*Log.i("aaa", "可用余额");
                mIntent.setClass(this, MyBalanceActivity.class);
                mIntent.putExtras(in.getExtras());
                mIntent.putExtra("isBindCard", isBindingBank);
                mIntent.putExtra("balance", balance);
                mIntent.putExtra("totalinterest", Historytotalinterest);
                mIntent.putExtra("interest", interest);
                mIntent.putExtra("pkmuser", pkmuser);
                startActivity(mIntent);*/
                break;
            case R.id.linear_vc_myaccount:                               //余额宝
                Intent intent2 = new Intent(this, YuEBaoActivity.class);
                startActivity(intent2);
               /* Log.i("aaa", "我的余额");
                mIntent.setClass(this, MyBalanceActivity.class);
                mIntent.putExtras(in.getExtras());
                mIntent.putExtra("totalinterest", Historytotalinterest);
                mIntent.putExtra("interest", interest);
                mIntent.putExtra("isBindCard", isBindingBank);
                mIntent.putExtra("balance", balance);
                mIntent.putExtra("pkmuser", pkmuser);
                startActivity(mIntent);*/
                break;
            case R.id.linear_vc_binding_bankcard:                      //绑定银行卡
                mIntent.setClass(this, NewBindBankCardActivity.class);
                startActivity(mIntent);
                break;
            case R.id.linear_vc_tonghuaInfo:                           //我要提现
                Intent intent = new Intent(VipCenterAccountActivity.this, WithdrawActivity.class);
                startActivity(intent);
                break;
            case R.id.linear_vc_shouji_recharge:                       //手机充值（现改成充值记录）
                Log.i("aaa", "手机充值");
                /*Intent mobileChargeIntent = new Intent(this, PhoneChargeActivity.class);
                mobileChargeIntent.putExtra("mybalance", in.getStringExtra("totalinterest"));
                startActivity(mobileChargeIntent);*/
                startActivity(new Intent(VipCenterAccountActivity.this, RechargeAccountActivity.class));
                break;
            case R.id.linear_vc_liuliang_charge:                       //流量充值
                startActivity(new Intent(this, FlowActivity.class));
                break;
            case R.id.linear_vc_tenxun_recharge:                       //腾讯充值(现改成积分记录)
//                startActivity(new Intent(this, TencentPayActivity.class));
                startActivity(new Intent(VipCenterAccountActivity.this, HuiyuanbiRecordActivity.class));
                break;
            case R.id.linear_vc_zhognshiyou_recharge:                   //中石油充值（现改为消费分红）
               /* Intent intent1 = new Intent(this, OilPayActivity.class);
                intent1.putExtra("FLAG", 1);
                startActivity(intent1);*/
                startActivity(new Intent(VipCenterAccountActivity.this, ConsumeDividendActivity.class));
                break;
            case R.id.linear_vc_zhongshihua_recharge:                   //余额宝
                /*Intent intent2 = new Intent(this, YuEBaoActivity.class);
                startActivity(intent2);*/
                break;
            case R.id.layout_back:
                this.finish();
                break;
            case R.id.linear_vc_lifeservice_recharge:                   //现改成余额增益
//                startActivity(new Intent(this, LifeServiceActivity.class));
                startActivity(new Intent(VipCenterAccountActivity.this, BalanceEarningsActivity.class));
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
                pkmuser = systemInfomationBean.getResultData().getPkmuser();
                Historytotalinterest = systemInfomationBean.getResultData().getHistorytotalinterest();
                interest = systemInfomationBean.getResultData().getInterest();

                vipgrade.setText("当前平台用户等级为" + in.getStringExtra("grade"));
                vipbanlance.setText("您的余额收益已达" + Historytotalinterest + "元");
                tv_interest.setText("昨日收益" + interest + "元");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    protected void onResume() {

        Log.e("xyz", "FLAG_MY_FRAGMENT:" + FLAG_MY_FRAGMENT);
        if (FLAG_MY_FRAGMENT == 2) {
            finish();
            FLAG_MY_FRAGMENT = 1;
        }

        super.onResume();
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

    public void setShowBackLogin(View view) {
        View contentView = View.inflate(this, R.layout.layout_weixin_tishi, null);
        TextView tv_wei = (TextView) contentView.findViewById(R.id.tv_weixin);
        ImageView iv_back = (ImageView) contentView.findViewById(R.id.iv_delete_weixin);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        tv_wei.setText("会员余额资金可在惠缘包平台内的所有商家及民生类项目使用，并可在工作日内随时提现（9:00-15:00），当日到账（节假日顺延）");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // popupWindow.showAsDropDown(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            Log.e("HHHH", "linear_vc_binding_bankcard.getMeasuredHeight():" + linear_vc_binding_bankcard.getMeasuredHeight());
            while (true) {
                Log.e("HHHH", "linear_vc_binding_bankcard.getMeasuredHeight() 1:" + linear_vc_binding_bankcard.getMeasuredHeight());
                if (linear_vc_binding_bankcard.getMeasuredHeight() > 0) {
                    Log.e("HHHH", "linear_vc_binding_bankcard.getMeasuredHeight() 2:" + linear_vc_binding_bankcard.getMeasuredHeight());
                    showPopHandler.sendEmptyMessage(1);
                    return;
                }
            }
        }
    }


    @Override
    public void isConntectedAndRefreshData() {
        request();
    }
}
