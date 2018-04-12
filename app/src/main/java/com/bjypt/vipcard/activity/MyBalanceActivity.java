package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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
import com.bjypt.vipcard.model.VipcenterResultDataBean;
import com.bjypt.vipcard.umeng.UmengCountContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/5/3
 * Use by 我的余额
 */
public class MyBalanceActivity extends BaseActivity implements VolleyCallBack<String> {

    private RelativeLayout mBack;
    private TextView mMyBalance;//我的余额
    private TextView mTotalEarnings;
    private TextView mYestodayEarnings;
    private TextView mUsableBalance;
    private TextView mIssueWithdraw;
    private Intent mIntent;
    private String isBindCard,balance,pkmuser;
    private TextView mPayBalance;
    private String sysBalance;
    private String Historytotalinterest;
    private String interest;
    private SystemInfomationBean systemInfomationBean;
    private VipcenterResultDataBean mVipcenterResultDataBean;
    private Map<String,String> mParams;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what){
                case 1:
                    mMyBalance.setText(sub(stringToDouble(systemInfomationBean.getResultData().getWholebalance()),stringToDouble(systemInfomationBean.getResultData().getBalance()))+
                            "(积分)"+"+"+stringToDouble(systemInfomationBean.getResultData().getBalance())+"元");
                    mTotalEarnings.setText("您的余额累计收益已达"+stringToDouble(systemInfomationBean.getResultData().getHistorytotalinterest())+"元");
                    mYestodayEarnings.setText("昨日收益"+stringToDouble(systemInfomationBean.getResultData().getInterest())+"元");
                    mUsableBalance.setText(stringToDouble(systemInfomationBean.getResultData().getBalance())+"元");
                    break;
            }
        }
    };

    private Handler mTopupHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what){
                case  1:
                    mMyBalance.setText(sub(stringToDouble(mVipcenterResultDataBean.getResultData().getWholebalance()),stringToDouble(mVipcenterResultDataBean.getResultData().getBalance()))+
                            "(积分)"+"+"+stringToDouble(mVipcenterResultDataBean.getResultData().getBalance())+"元");
                   /* double a = Double.parseDouble(mVipcenterResultDataBean.getResultData().getWholebalance())-Double.parseDouble(mVipcenterResultDataBean.getResultData().getBalance());
                    DecimalFormat df = new DecimalFormat("#.00");
                    mMyBalance.setText(df.format(a)+ "(积分)"+"+"+stringToDouble(mVipcenterResultDataBean.getResultData().getBalance())+"元");*/
                    mTotalEarnings.setText("您的余额累计收益已达"+stringToDouble(mVipcenterResultDataBean.getResultData().getHistorytotalinterest()+"")+"元");
                    mYestodayEarnings.setText("昨日收益"+stringToDouble(mVipcenterResultDataBean.getResultData().getInterest())+"元");
                    mUsableBalance.setText(stringToDouble(mVipcenterResultDataBean.getResultData().getBalance())+"元");
                    break;
            }
        }
    };

    public  Double roundDouble(double dou, int i)
    {
        Double d= null;
        try
        {
            double factor = Math.pow(10, i);
            d= Math.floor(dou* factor + 0.5) / factor;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return d;
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_mybalance);
    }

    @Override
    public void beforeInitView() {

        mIntent=getIntent();
        isBindCard = mIntent.getStringExtra("isBindCard");
        balance = mIntent.getStringExtra("balance");
        pkmuser = mIntent.getStringExtra("pkmuser");
    }

    @Override
    public void initView() {
        mBack = (RelativeLayout) findViewById(R.id.balance_back);
        mMyBalance = (TextView) findViewById(R.id.my_balance);//我的余额
        mTotalEarnings = (TextView) findViewById(R.id.total_earnings);//累计收益
        mYestodayEarnings = (TextView) findViewById(R.id.yestoday_earnings);//昨日收益
        mUsableBalance = (TextView) findViewById(R.id.usable_balance);//提现金额
        mIssueWithdraw = (TextView) findViewById(R.id.whithdraw_balance);
        mPayBalance = (TextView) findViewById(R.id.pay_balance);
    }

    @Override
    public void afterInitView() {

        if(mIntent.getStringExtra("wholebalance")==null){
            mMyBalance.setText("0.00元");
        }else {
            mMyBalance.setText("("+sub(stringToDouble(mIntent.getStringExtra("wholebalance")),stringToDouble(mIntent.getStringExtra("balance")))+")"+
                    "积分"+"+"+stringToDouble(mIntent.getStringExtra("balance"))+"元");
          /*  Double b = Double.parseDouble(mIntent.getStringExtra("wholebalance"))-Double.parseDouble(mIntent.getStringExtra("balance"));
            DecimalFormat dft = new DecimalFormat("#.00");
            mMyBalance.setText(dft.format(b)+ "(积分)"+"+"+mIntent.getStringExtra("balance")+"元");*/
        }

        if(mIntent.getStringExtra("totalinterest")==null){
            mTotalEarnings.setText("您的余额累计收益已达0.00元");
        }else {
            mTotalEarnings.setText("您的余额累计收益已达"+stringToDouble(mIntent.getStringExtra("totalinterest"))+"元");
        }

        if(mIntent.getStringExtra("interest")==null){
            mYestodayEarnings.setText("昨日收益0.00元");
        }else {
            mYestodayEarnings.setText("昨日收益"+stringToDouble(mIntent.getStringExtra("interest"))+"元");
        }

        if(mIntent.getStringExtra("balance")==null){
            mUsableBalance.setText("0.00元");
        }else {
            mUsableBalance.setText(stringToDouble(mIntent.getStringExtra("balance"))+"元");
        }

    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
        mIssueWithdraw.setOnClickListener(this);
        mPayBalance.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.balance_back:
                finish();
                break;
            case R.id.whithdraw_balance:
                if(isBindCard.equals("Y")){
                    startActivity(new Intent(this,WithdrawActivity.class));
                }else{
                    Toast.makeText(this,"您尚未绑定银行卡,请先绑定银行卡",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this,BindBankCardActivity.class));
                }

                break;
            case R.id.pay_balance:
//                if(isBindCard.equals("Y")){
                    Intent intent = new Intent(this,TopupAmountActivity.class);
                    intent.putExtra("FLAG",2);
                    intent.putExtra("pkmuser",pkmuser);
                    //此时还需要在此页面调用一个获取余额的接口，并且在此时会返回一个系统主键，传递到下一个页面
                    startActivity(intent);
//                }else{
//                    Toast.makeText(this,"您还没有绑定银行卡,请先绑定银行卡",Toast.LENGTH_LONG).show();
//                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);

        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(MyBalanceActivity.this,1, Config.web.system_infomatiob, params, this);

        mParams = new HashMap<>();
        mParams.put("userId", getFromSharePreference(Config.userConfig.pkregister));

        Wethod.httpPost(MyBalanceActivity.this,1991, Config.web.get_platBalance, mParams, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 1) {
            try {
                systemInfomationBean = getConfiguration().readValue(result.toString(), SystemInfomationBean.class);

                 sysBalance = systemInfomationBean.getResultData().getBalance();
                 Historytotalinterest=systemInfomationBean.getResultData().getHistorytotalinterest();
                 interest=systemInfomationBean.getResultData().getInterest();

                if(Double.parseDouble(sysBalance)!=Double.parseDouble(balance)){
                    mHandler.sendEmptyMessage(1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(reqcode == 1991){
            try {
                mVipcenterResultDataBean = getConfiguration().readValue(result.toString(), VipcenterResultDataBean.class);
                mTopupHandler.sendEmptyMessage(1);
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
}
