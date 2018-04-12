package com.bjypt.vipcard.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.RespBase;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.MD5;
import com.bjypt.vipcard.utils.MyDialogUtil;
import com.bjypt.vipcard.view.MyDialog;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/8/5
 * Use by QQ充值确认订单
 */
public class QqSurePayActivity extends BaseActivity implements VolleyCallBack{


    private TextView mPhoneNum;
    private TextView mCellCore;
    private TextView mChargeMoney;
    private TextView mNeedMore;
    private TextView mPlatformAlmost;
    private RelativeLayout mGoPay;
    private RelativeLayout mBack;
    private Button mSureCharge;
    private int FLAG ;//1--qq  2--中石化  3--中石油 4--流量充值
    private String qqNum;
    private String qqType;
    private String cardid;
    private int totalMoney;
    private String consumeChildType;
    private String key = "";
    private EditText et;
    private PsdDialog psdDialog;
    private Boolean PSD_TYPE = false;
    private Double MyBalance;
    private SystemInfomationBean systemInfomationBean;
    private TextView mOrderNum;
    private String mobilePhone;

    private String systemPkmuser;//系统主键

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    mPlatformAlmost.setText("平台余额:" + MyBalance + "元");
                    break;
            }
        }
    };

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_qq_sure);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        mobilePhone = intent.getStringExtra("mobilePhone");
        FLAG = intent.getIntExtra("FLAG",0);
        qqNum = intent.getStringExtra("qqNum");
        qqType = intent.getStringExtra("qqType");
        cardid = intent.getStringExtra("cardid");
        totalMoney = intent.getIntExtra("totalMoney", 0);
        consumeChildType = intent.getStringExtra("consumeChildType");
    }

    @Override
    public void initView() {
        mPhoneNum = (TextView) findViewById(R.id.tv_PhoneNum_qq);//qq号码
        mCellCore = (TextView) findViewById(R.id.tv_QCellCore_qq);//充值类型
        mChargeMoney = (TextView) findViewById(R.id.tv_chargeMoney_qq);//充值金额
        mNeedMore = (TextView) findViewById(R.id.tv_needMore_qq);//还需支付
        mPlatformAlmost = (TextView) findViewById(R.id.tv_platformAlmost_qq);//平台余额
        mGoPay = (RelativeLayout) findViewById(R.id.relative_goPay_qq);//去支付
        mSureCharge = (Button) findViewById(R.id.btn_sureCharge_qq);//确认充值
        mBack = (RelativeLayout) findViewById(R.id.rl_printer_back_qq);
        mOrderNum = (TextView) findViewById(R.id.order_num);

        if(FLAG ==1){
            mOrderNum.setText("手机号码");
        }else if(FLAG == 2){
            mOrderNum.setText("加油卡卡号");
        }else if(FLAG == 3){
            mOrderNum.setText("加油卡卡号");
        }else if(FLAG == 4){
            mOrderNum.setText("手机号码");
        }
    }

    @Override
    public void afterInitView() {
        mPhoneNum.setText(qqNum);
        mCellCore.setText(qqType);
        mChargeMoney.setText(totalMoney + "元");
        mNeedMore.setText(totalMoney + "元");
        mPlatformAlmost.setText("平台余额:" + MyBalance + "元");
    }

    @Override
    public void bindListener() {
        mGoPay.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mSureCharge.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.relative_goPay_qq:
                //跳转到余额充值页面
                goCharged();
                break;
            case R.id.btn_sureCharge_qq:
                //支付
                psdDialog = new PsdDialog(this, "充值", "", "余额:" + totalMoney + "元");
                psdDialog.show();
                break;
            case R.id.rl_printer_back_qq:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(QqSurePayActivity.this,1234, Config.web.system_infomatiob, params, this);
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if(reqcode == 1314){
            psdDialog.dismiss();
            psdDialog.hide();
            psdDialog.cancel();
            if(FLAG == 1){
                //QQ充值
                Map<String,String> params = new HashMap<>();
                params.put("mobile", AES.encrypt(qqNum,AES.key));
                params.put("consumeType", AES.encrypt(4+"",AES.key));
                params.put("cardnum",AES.encrypt(1+"",AES.key));
                params.put("cardid",AES.encrypt(cardid,AES.key));
                params.put("totalMoney",AES.encrypt(totalMoney+"",AES.key));
                params.put("consumeChildType",AES.encrypt(consumeChildType,AES.key));
                params.put("phoneno", AES.encrypt(getFromSharePreference(Config.userConfig.phoneno), AES.key));
                params.put("userId", AES.encrypt(getFromSharePreference(Config.userConfig.pkregister), AES.key));
//            Wethod.httpPost(1122, "http://192.168.1.112:7778/hyb/S01/of/recharge/onlineorderQb", params, this);
                Wethod.httpPost(QqSurePayActivity.this,1122,Config.web.life_base+"of/recharge/onlineorderQb",params,this);
            }else if(FLAG == 2){
                //中石化油卡充值
                Map<String,String> params = new HashMap<>();
                params.put("phoneno", AES.encrypt(getFromSharePreference(Config.userConfig.phoneno), AES.key));
                params.put("userId", AES.encrypt(getFromSharePreference(Config.userConfig.pkregister), AES.key));
                params.put("gameUserid",AES.encrypt(qqNum,AES.key));
                params.put("mobile",mobilePhone);
                params.put("cardType","0");//手机只有直冲
                params.put("totalMoney",totalMoney+"");
                params.put("cardid",cardid);
                Wethod.httpPost(QqSurePayActivity.this,2222,Config.web.life_base+"of/recharge/cardOnlineorder",params,this);

            }else if(FLAG == 3){
                //中石油油卡充值
                Map<String,String> params = new HashMap<>();
                params.put("phoneno", AES.encrypt(getFromSharePreference(Config.userConfig.phoneno), AES.key));
                params.put("userId", AES.encrypt(getFromSharePreference(Config.userConfig.pkregister), AES.key));
                params.put("gameUserid",AES.encrypt(qqNum,AES.key));
                params.put("mobile",mobilePhone);
                params.put("cardType","1");//手机只有直冲
                params.put("totalMoney",totalMoney+"");
                params.put("cardid",cardid);
                Wethod.httpPost(QqSurePayActivity.this,2222,Config.web.life_base+"of/recharge/cardOnlineorder",params,this);
            }else if(FLAG == 4){
                //手机流量充值
                Map<String,String> params = new HashMap<>();
                params.put("phoneno", AES.encrypt(getFromSharePreference(Config.userConfig.phoneno), AES.key));
                params.put("userId", AES.encrypt(getFromSharePreference(Config.userConfig.pkregister), AES.key));
                params.put("mobile",AES.encrypt(mobilePhone,AES.key));
                params.put("cardType","0");
                params.put("errMsg",cardid);
                params.put("totalMoney",totalMoney+"");
                params.put("totalMoney","5");
                Wethod.httpPost(QqSurePayActivity.this,3333,Config.web.life_base+"of/recharge/onlineFlowOrder",params,this);
            }
        }else if(reqcode == 1234){
            try {
                systemInfomationBean = getConfiguration().readValue(result.toString(), SystemInfomationBean.class);
                MyBalance = Double.parseDouble(systemInfomationBean.getResultData().getBalance());
                mHandler.sendEmptyMessage(1);
                systemPkmuser = systemInfomationBean.getResultData().getPkmuser();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(reqcode == 2222){
            Wethod.ToFailMsg(this,result);
            finish();
            OilPayActivity.FLAG_LIFE_OIL = 2;
        }else if(reqcode == 3333){
            Wethod.ToFailMsg(this,result);
            finish();
            FlowActivity.FLAG_LIFE_FLOW = 2;
        }else if(reqcode == 1122){
            Wethod.ToFailMsg(this,result);
            finish();
            TencentPayActivity.FLAG_LIFE_QQ = 2;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if(reqcode == 1314){
            try {
                RespBase respBase = getConfiguration().readValue(result.toString(), RespBase.class);
                if (respBase.getResultData().toString().contains("未查询到用户支付密码信息")) {
                    psdDialog.dismiss();
                    Intent intent = new Intent(this, ChangePasswordActivity.class);
                    intent.putExtra("psdType", PSD_TYPE);
                    startActivity(intent);
                } else if (respBase.getResultData().toString().contains("支付密码输入有误")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提示");
                    builder.setMessage("支付密码有误,请重新输入");

                    builder.setNegativeButton("重试",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    key = "";
                                    et.setText("");
                                    dialog.dismiss();
                                }
                            });

                    builder.setPositiveButton("修改支付密码",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    psdDialog.dismiss();
                                    dialog.dismiss();
                                    Intent intent = new Intent(QqSurePayActivity.this, ForgetPayPsdActivity.class);
                                    startActivity(intent);
                                }
                            });
                    builder.create().show();

                } else {
                    Toast.makeText(this, respBase.getResultData().toString(), Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(reqcode == 1122){
            Wethod.ToFailMsg(this,result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }


    /*********************************
     * 支付密码校验
     ********************************************/
    class PsdDialog extends Dialog {


        private Context context;
        private String messageOne, MessageTwo, title;
        private TextView mProductName;
        private TextView mProductMoney;
        private RelativeLayout mBack;
        private TextView mPsdTv;

        TextView t1, t2, t3, t4, t5, t6;


        public PsdDialog(Context context, String title, String messageOne, String MessageTwo) {
            super(context, R.style.MyDialogStyle);
            this.context = context;
            this.title = title;
            this.messageOne = messageOne;
            this.MessageTwo = MessageTwo;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            init();
        }

        public void init() {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.psd_input_dialog, null);
            setContentView(view);

            mProductName = (TextView) findViewById(R.id.product_name);
            mProductMoney = (TextView) findViewById(R.id.product_money);
            mPsdTv = (TextView) findViewById(R.id.psd_tv);

            mProductName.setText(messageOne);
            mProductMoney.setText(MessageTwo);
            mPsdTv.setText(title);


            mBack = (RelativeLayout) findViewById(R.id.iv_paysuccess_back);

            t1 = (TextView) findViewById(R.id.t1);
            t2 = (TextView) findViewById(R.id.t2);
            t3 = (TextView) findViewById(R.id.t3);
            t4 = (TextView) findViewById(R.id.t4);
            t5 = (TextView) findViewById(R.id.t5);
            t6 = (TextView) findViewById(R.id.t6);
            et = (EditText) findViewById(R.id.editHide);

            et.addTextChangedListener(tw);

            Window dialogWindow = getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
            lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
            dialogWindow.setAttributes(lp);


            //隐藏
            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PsdDialog.this.dismiss();
                }
            });
        }

        void setKey() {
            char[] arr = key.toCharArray();
            t1.setText("");
            t2.setText("");
            t3.setText("");
            t4.setText("");
            t5.setText("");
            t6.setText("");
            for (int i = 0; i < arr.length; i++) {

                if (i == 0) {
                    t1.setText("*");
                } else if (i == 1) {
                    t2.setText("*");
                } else if (i == 2) {
                    t3.setText("*");
                } else if (i == 3) {
                    t4.setText("*");
                } else if (i == 4) {
                    t5.setText("*");
                } else if (i == 5) {
                    t6.setText("*");
                }

            }
        }


        TextWatcher tw = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                key = s.toString();
                if (key.length() == 6) {
                    Map<String, String> payPsdParams = new HashMap<>();
                    payPsdParams.put("phoneno", getFromSharePreference(Config.userConfig.phoneno));
                    try {
                        payPsdParams.put("payPassword", MD5.getMd5(key, MD5.key));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    Wethod.httpPost(QqSurePayActivity.this,1314, Config.web.checkout_pay_psd, payPsdParams, QqSurePayActivity.this);
                }
                setKey();
            }
        };

    }

    protected void dialogShow() {

        MyDialogUtil.setDialog(this, "提示", "平台余额不足", "请先去加款后充值", "去加款", "取消", new MyDialog.ClickListenerInterface() {
            @Override
            public void doButtomOne() {

                goCharged();
                MyDialogUtil.hideDialog();
            }

            @Override
            public void doButtomTwo() {

                MyDialogUtil.hideDialog();
            }
        });
    }

    public void goCharged() {
        Intent intent = new Intent(this, TopupAmountActivity.class);
        intent.putExtra("FLAG", 3);

        intent.putExtra("pkmuser", systemPkmuser);
        startActivity(intent);
    }
}
