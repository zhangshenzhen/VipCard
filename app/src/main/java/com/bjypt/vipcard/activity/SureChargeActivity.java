package com.bjypt.vipcard.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
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
import com.bjypt.vipcard.model.PhoneCallerlocBean;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.MD5;
import com.bjypt.vipcard.utils.MyDialogUtil;
import com.bjypt.vipcard.utils.StringUtils;
import com.bjypt.vipcard.view.MyDialog;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016/6/7.
 */
public class SureChargeActivity extends BaseActivity implements VolleyCallBack {

    private TextView mPhoneNum;
    private TextView mCellCore;
    private TextView mOperators;
    private TextView mChargeMoney;
    private TextView mNeedMore;
    private TextView mPlatformAlmost;
    private RelativeLayout mGoPay;
    private RelativeLayout mBack;
    private Button mSureCharge;

    private Intent mIntent;
    private Double MyBalance;
    private int PayMoney;
    private PsdDialog psdDialog;
    private String systemPkmuser;//平台主键
    private SystemInfomationBean systemInfomationBean;
    private Boolean PSD_TYPE = false;
    private String key = "";
    private EditText et;

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
        setContentView(R.layout.activity_surecharge);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {

        mPhoneNum = (TextView) findViewById(R.id.tv_PhoneNum);//手机号码
        mCellCore = (TextView) findViewById(R.id.tv_QCellCore);//归属地
        mOperators = (TextView) findViewById(R.id.tv_Operators);//运营商
        mChargeMoney = (TextView) findViewById(R.id.tv_chargeMoney);//充值金额
        mNeedMore = (TextView) findViewById(R.id.tv_needMore);//还需支付
        mPlatformAlmost = (TextView) findViewById(R.id.tv_platformAlmost);//平台余额
        mGoPay = (RelativeLayout) findViewById(R.id.relative_goPay);//去支付
        mSureCharge = (Button) findViewById(R.id.btn_sureCharge);//确认充值
        mBack = (RelativeLayout) findViewById(R.id.rl_printer_back);
    }

    @Override
    public void afterInitView() {

        mIntent = getIntent();
        systemPkmuser = mIntent.getStringExtra("systemPkmuser");
//        MyBalance = mIntent.getDoubleExtra("MyBalance", 0);
        PayMoney = mIntent.getIntExtra("ChargeMoneyNum", 0);

//        if (MyBalance > 0 && PayMoney > 0) {
//            Log.i("aaa", "取到的为正常值>>>>>" + MyBalance);
//        }
        mPhoneNum.setText(mIntent.getStringExtra("PhoneNum"));
        mCellCore.setText(mIntent.getStringExtra("PhoneCallerloc"));
        mOperators.setText(mIntent.getStringExtra("PhoneCallerlocLocation"));
        mChargeMoney.setText(StringUtils.setScale(PayMoney + "") + "(充值不提供发票)");
        mNeedMore.setText(StringUtils.setScale(PayMoney + "") + "元");
        mPlatformAlmost.setText("平台余额:" + MyBalance + "元");
    }

    @Override
    public void bindListener() {

        mGoPay.setOnClickListener(this);
        mSureCharge.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {

        switch (v.getId()) {
            case R.id.relative_goPay://去付款

                goCharged();
                break;
            case R.id.btn_sureCharge://确认充值
                Log.i("aaa", "PayMoney" + PayMoney + ">>>>>" + MyBalance);
                if (PayMoney <= MyBalance) {
                    psdDialog = new PsdDialog(this, "充值", "", "余额:" + MyBalance + "元");
                    psdDialog.show();
                } else {
//                    Toast.makeText(this, "平台余额不足,请充值", Toast.LENGTH_SHORT).show();
                    dialogShow();
                }
                break;
            case R.id.rl_printer_back:
                finish();

                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {

        Log.i("aaa", "" + result.toString());
        if (reqcode == 1) {

            try {
                PhoneCallerlocBean phoneCallerlocBean = getConfiguration().readValue(result.toString(), PhoneCallerlocBean.class);
                //判断没有中文
                if (phoneCallerlocBean.getResultData().contains("已使用")) {
                    Toast.makeText(this, phoneCallerlocBean.getResultData(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, phoneCallerlocBean.getResultData(), Toast.LENGTH_LONG).show();
                    setPopup();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 1314) {
            psdDialog.dismiss();
            Map<String, String> params = new HashMap<>();

            params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
            params.put("mobile", mIntent.getStringExtra("PhoneNum"));
            params.put("totalMoney", PayMoney + "");
            params.put("consumeType", "1");//充值
            params.put("cardType", "0");//消费
            Log.i("aaa", "pkregister===" + getFromSharePreference(Config.userConfig.pkregister));
            Log.i("aaa", "mobile===" + mIntent.getStringExtra("PhoneNum"));
            Log.i("aaa", "totalMoney===" + PayMoney);

            Wethod.httpPost(SureChargeActivity.this,1, Config.web.on_line_order, params, this);
        } else if (reqcode == 1234) {
            try {
                systemInfomationBean = getConfiguration().readValue(result.toString(), SystemInfomationBean.class);
                MyBalance = Double.parseDouble(systemInfomationBean.getResultData().getBalance());
                mHandler.sendEmptyMessage(1);
                systemPkmuser = systemInfomationBean.getResultData().getPkmuser();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 1314) {
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
                                    Intent intent = new Intent(SureChargeActivity.this, ForgetPayPsdActivity.class);
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
                    Wethod.httpPost(SureChargeActivity.this,1314, Config.web.checkout_pay_psd, payPsdParams, SureChargeActivity.this);
                }
                setKey();
            }
        };

    }


    @Override
    protected void onResume() {
        super.onResume();
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(SureChargeActivity.this,1234, Config.web.system_infomatiob, params, this);
    }

    public void goCharged() {
        Intent intent = new Intent(this, TopupAmountActivity.class);
        intent.putExtra("FLAG", 3);
        intent.putExtra("pkmuser", systemPkmuser);
        FirstActivity.WXPAY_PHONE = 2;
        startActivity(intent);
    }

    protected void dialogShow() {

        MyDialogUtil.setDialog(this,"提示","平台余额不足","请先去加款后充值","去加款","取消", new MyDialog.ClickListenerInterface() {
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

    private PopupWindow popupWindow;
    private int screenWidth;
    private int screenHeigh;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setPopup() {

        DisplayMetrics dm=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;

        View view = View.inflate(this, R.layout.layout_phonecharge_ok_pop, null);
        popupWindow = new PopupWindow(view, screenWidth, screenHeigh);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        Button mBackMain = (Button) view.findViewById(R.id.btn_backMain);
        Button mBackOrder = (Button) view.findViewById(R.id.btn_backOrder);

        popupWindow.showAtLocation(mBack, 0, 0, Gravity.CENTER);
        //返回主页
        mBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BroadCastReceiverUtils utils = new BroadCastReceiverUtils();
                utils.sendBroadCastReceiver(SureChargeActivity.this,"close");
                popupWindow.dismiss();
                finish();
            }
        });

        //订单界面
        mBackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SureChargeActivity.this, UnPayOrderActivity.class);
                intent.putExtra("flag", 1);
                startActivity(intent);
                popupWindow.dismiss();
                finish();
            }
        });
    }
}
