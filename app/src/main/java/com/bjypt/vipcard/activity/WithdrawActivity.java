package com.bjypt.vipcard.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.NewBindBankCardActivity;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.RespBase;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.dialog.NormalDialog;
import com.bjypt.vipcard.model.LimitBean;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.model.WithdrawBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.MD5;
import com.bjypt.vipcard.view.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/20
 * Use by 提现
 */
public class WithdrawActivity extends BaseActivity implements VolleyCallBack {
    //123
    private LinearLayout mBack;
    private ImageView mBankLogo;
    private TextView mBankName;
    private TextView mBankCardNum;
    private TextView tv_tishi_width;
    private EditText mWithDrawBalance;
    private TextView mTrue;
    private WithdrawBean withdrawBean;
    private PsdDialog psdDialog;
    private Boolean PSD_TYPE = false;
    private TextView goYuEBAo;                    //购买理财
    private TextView tv_withdraw;
    private TextView recharge;                    //立即充值

    private SystemInfomationBean systemInfomationBean;
    private String isBindingBank;
    private String mRate;
    private String noratebalance;
    private String balance;
    private static final String PRICE_FRACTION_TWO = "0.00"; // 保留2位小数
    private static final int LIMIT = 1110; // 保留2位小数
    private LimitBean limitBean;
    private String startMoney;
    private String endMoney;
    private String tipEnd;
    private String tipStart;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_withdraw);
    }

    @Override
    public void beforeInitView() {
        //验证是否绑定银行卡
        Map<String, String> params1 = new HashMap<>();
        params1.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(WithdrawActivity.this, 12, Config.web.system_infomatiob, params1, this);

        //充值、提现需调用的接口获取上下限
        requestLimit();
    }

    @Override
    public void initView() {
        recharge = (TextView) findViewById(R.id.withdraw_recharge);                    //立即充值
        mBack = (LinearLayout) findViewById(R.id.withdraw_back);                        //返回键
        mBankLogo = (ImageView) findViewById(R.id.bank_logo);                           //银行Logo
        mBankName = (TextView) findViewById(R.id.bank_name_tv);                        //银行名称
        tv_tishi_width = (TextView) findViewById(R.id.tv_tishi_width);
        mBankCardNum = (TextView) findViewById(R.id.bank_card_num);                   //银行卡号码和类型
        mWithDrawBalance = (EditText) findViewById(R.id.withdraw_balance);           //可提现余额和输入提现金额
        mTrue = (TextView) findViewById(R.id.issue_withdraw);                         //确定
        goYuEBAo = (TextView) findViewById(R.id.go_yu_e_bao_withdraw);              //跳转余额界面
        tv_withdraw = (TextView) findViewById(R.id.tv_withdraw);

        SpannableString ss = new SpannableString("用户余额可购买任意一款理财产品，理财产品到期后提现可免手续费！");
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")),
                0, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#3475b7")),
                25, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_withdraw.setText(ss);


    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
        mTrue.setOnClickListener(this);
        goYuEBAo.setOnClickListener(this);
        recharge.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.withdraw_recharge:

                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent = new Intent(WithdrawActivity.this, NewTopupWayActivity.class);
                    intent.putExtra("FLAG", 2);
                    intent.putExtra("pkmuser", systemInfomationBean.getResultData().getPkmuser());
                    startActivity(intent);
                } else {
                    startActivity(new Intent(WithdrawActivity.this, LoginActivity.class));
                }
                break;
            case R.id.go_yu_e_bao_withdraw:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(WithdrawActivity.this, YuEBaoActivity.class));
                } else {
                    startActivity(new Intent(WithdrawActivity.this, LoginActivity.class));
                }
                break;
            case R.id.withdraw_back:
                finish();
                break;
            case R.id.issue_withdraw:
                if (noratebalance != null) {
                    if (mWithDrawBalance.getText().toString().trim().length() != 0) {
                        if (Double.parseDouble(noratebalance) > Double.parseDouble(mWithDrawBalance.getText().toString())) {
                            withdrawCash();
                        } else {
                            if (Double.parseDouble(mWithDrawBalance.getText().toString()) > Double.parseDouble(withdrawBean.getResultData().getBalance())) {
                                Toast.makeText(this, "对不起,您的余额不足,请充值！", Toast.LENGTH_LONG).show();
                            } else {
                                showAlertDialog();
                            }
                        }
                    } else {
                        Toast.makeText(this, "请输入提现金额", Toast.LENGTH_LONG).show();
                    }
                } else {
                    withdrawCash();
                }

                break;
        }
    }


    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == LIMIT) {
            try {
                limitBean = getConfiguration().readValue(result.toString(), LimitBean.class);
                startMoney = limitBean.getResultData().getStart();
                endMoney = limitBean.getResultData().getEnd();
                tipEnd = limitBean.getResultData().getTip_end();
                tipStart = limitBean.getResultData().getTip_start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 12) {
            try {
                systemInfomationBean = getConfiguration().readValue(result.toString(), SystemInfomationBean.class);
                isBindingBank = systemInfomationBean.getResultData().getIsBindingBank();
                if (isBindingBank.equals("N")) {
                    Intent intent = new Intent(WithdrawActivity.this, NewBindBankCardActivity.class);
                    startActivity(intent);
                } else {
                    queryWithdraw();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (reqcode == 1) {
            try {
                withdrawBean = getConfiguration().readValue(result.toString(), WithdrawBean.class);
                mRate = withdrawBean.getResultData().getRate(); //费率
                noratebalance = withdrawBean.getResultData().getNoratebalance(); //免费率金额
                balance = withdrawBean.getResultData().getBalance();
                mBankCardNum.setText(withdrawBean.getResultData().getBankcardno());
                mBankName.setText(withdrawBean.getResultData().getBankusername());
                mWithDrawBalance.setHint("可提现金额:" + balance + "元");
                tv_tishi_width.setText(withdrawBean.getResultData().getWdmsg());
                ImageLoader.getInstance().displayImage(withdrawBean.getResultData().getBanklogo(), mBankLogo);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (reqcode == 2) {
            Toast.makeText(this, "提现申请成功", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, RechargeRecordActivity.class);
            intent.putExtra("type", 3);
            startActivity(intent);
            finish();
        } else if (reqcode == 1314) {
            psdDialog.dismiss();
            Toast.makeText(this, "密码校验通过", Toast.LENGTH_LONG).show();
            Map<String, String> params = new HashMap<>();
            params.put("pkmuser", getFromSharePreference(Config.userConfig.pkregister));
            params.put("type", "2");
            params.put("amount", AES.encrypt(mWithDrawBalance.getText().toString(), AES.key));
            params.put("bankcardno", mBankCardNum.getText().toString());
            Wethod.httpPost(WithdrawActivity.this, 2, Config.web.withdraw_balance, params, this);
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 1) {              //提现查询接口
            isOrNotIntentBandBank(result);
        } else if (reqcode == 2) {
            isOrNotIntentBandBank(result);
        } else if (reqcode == 1314) {
            try {
                RespBase respBase = getConfiguration().readValue(result.toString(), RespBase.class);
                if (respBase.getResultData().toString().contains("未查询到用户支付密码信息")) {
                    psdDialog.dismiss();
                    Intent intent = new Intent(this, ChangePasswordActivity.class);
                    intent.putExtra("psdType", PSD_TYPE);
                    startActivity(intent);
                } else {
                    ToastUtil.showToast(this, respBase.getResultData().toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //判断未添加银行支行信息的用户，跳转完善绑卡信息
    private void isOrNotIntentBandBank(Object result) {
        try {
            RespBase respBase = getConfiguration().readValue(result.toString(), RespBase.class);
            if (respBase.getResultData().equals("BIND_LNAME")) {
                final NormalDialog mNormalDialog = NormalDialog.getInstance(WithdrawActivity.this);
                mNormalDialog.setDesc("银行卡信息未完善，请先完善银行卡信息");
                mNormalDialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNormalDialog.dismiss();
                        Intent intent = new Intent(WithdrawActivity.this, NewBindBankCardActivity.class);
                        startActivity(intent);
                    }
                });
                mNormalDialog.setOnNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNormalDialog.dismiss();
                        WithdrawActivity.this.finish();
                    }
                });
            } else {
                ToastUtil.showToast(this, respBase.getResultData() + "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    class PsdDialog extends Dialog {

        private Context context;
        private String messageOne, MessageTwo, title;
        private TextView mProductName;
        private TextView mProductMoney;
        private RelativeLayout mBack;
        private TextView mPsdTv;
        TextView t1, t2, t3, t4, t5, t6, et;

        String key = "";

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
                /*if (i == 0) {
                    t1.setText(String.valueOf(arr[0]));
                } else if (i == 1) {
                    t2.setText(String.valueOf(arr[1]));
                } else if (i == 2) {
                    t3.setText(String.valueOf(arr[2]));
                } else if (i == 3) {
                    t4.setText(String.valueOf(arr[3]));
                } else if(i == 4){
                    t5.setText(String.valueOf(arr[4]));
                }else if(i == 5){
                    t6.setText(String.valueOf(arr[5]));
                }*/

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
                    Wethod.httpPost(WithdrawActivity.this, 1314, Config.web.checkout_pay_psd, payPsdParams, WithdrawActivity.this);
                }
                setKey();
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();
        queryWithdraw();
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawActivity.this, R.style.MyDialogStyle);
        View view = View.inflate(WithdrawActivity.this, R.layout.item_dialog_info, null);
        builder.setView(view);
        builder.setCancelable(false);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_detailed);

        BigDecimal a = new BigDecimal(mWithDrawBalance.getText().toString());
        BigDecimal b = new BigDecimal(noratebalance);//免手续费的金额
        BigDecimal beyond = a.subtract(b);
        BigDecimal c = new BigDecimal(mRate);
        BigDecimal serviceCharge = beyond.multiply(c.multiply(BigDecimal.valueOf(0.01)));

        DecimalFormat df = new DecimalFormat(PRICE_FRACTION_TWO);
        df.setRoundingMode(RoundingMode.HALF_UP);
        BigDecimal result = new BigDecimal(df.format(serviceCharge));

        String str = "您的提现已超出免手续费提现金额，超出金额为" + "<font color='#f66262'>" + beyond + "</font>" + "。将收取" + "<font color='#f66262'>" + result + "</font>" + "手续费";
        tv_title.setText(Html.fromHtml(str));
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_no = (TextView) view.findViewById(R.id.tv_no);
        //取消或确定按钮监听事件处理
        final AlertDialog dialog = builder.create();
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawCash();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 提现的逻辑
     */
    private void withdrawCash() {
        if (mWithDrawBalance.getText().toString() == null || mWithDrawBalance.getText().toString().equals("")) {
            Toast.makeText(this, "请输入提现金额", Toast.LENGTH_LONG).show();
        } else if (Integer.parseInt(mWithDrawBalance.getText().toString()) == 0) {
            Toast.makeText(this, "提现金额不能为0", Toast.LENGTH_LONG).show();
        } else {
            if (withdrawBean == null) {
                ToastUtil.showToast(this, "请升级到最新版本");
                return;
            }
            if (Double.parseDouble(mWithDrawBalance.getText().toString()) > Double.parseDouble(withdrawBean.getResultData().getBalance())) {
                Toast.makeText(this, "对不起,您的余额不足,请充值！", Toast.LENGTH_LONG).show();
            } else if (Double.parseDouble(mWithDrawBalance.getText().toString()) > Double.parseDouble(endMoney)) {
                Toast.makeText(this, tipEnd, Toast.LENGTH_LONG).show();
            } else if (Double.parseDouble(mWithDrawBalance.getText().toString()) < Double.parseDouble(startMoney)) {
                Toast.makeText(this, tipStart, Toast.LENGTH_LONG).show();
            } else {
                psdDialog = new PsdDialog(this, "提现", "", mWithDrawBalance.getText().toString());
                psdDialog.show();
            }
        }
    }

    //充值、提现需调用的接口获取上下限
    private void requestLimit() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "withdraw");
        Wethod.httpPost(WithdrawActivity.this, LIMIT, Config.web.Limit, params, this);
    }

    //提现查询
    private void queryWithdraw() {
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", getFromSharePreference(Config.userConfig.pkregister));
        params.put("type", "2");
        Wethod.httpPost(WithdrawActivity.this, 1, Config.web.query_withdraw, params, this);
    }
}
