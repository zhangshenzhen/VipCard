package com.bjypt.vipcard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.PayDealTypeEnum;
import com.bjypt.vipcard.common.PayTypeEnum;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.NewAlipay;
import com.bjypt.vipcard.model.NewPayData;
import com.bjypt.vipcard.model.NewPayWxData;
import com.bjypt.vipcard.model.NewYinLianPayData;
import com.bjypt.vipcard.model.ObjectModelBean;
import com.bjypt.vipcard.model.PayTypeBean;
import com.bjypt.vipcard.model.PrePayOrderId;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.CheckPwdDialog;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.wxapi.WXHelper;
import com.sinia.orderlang.utils.StringUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.unionpay.UPPayAssistEx;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 五折卡充值
 */

public class RightAwayOnLineNewActivity extends BaseActivity implements VolleyCallBack<String> {
    private LinearLayout layout_bank_pay;     //银行卡支付
    private View view_bank_pay;
    private LinearLayout layout_zhifubao_pay; //支付宝支付
    private View view_zhifubao_pay;
    private LinearLayout layout_weixin_pay;   //微信支付
    private View view_weixin_pay;
    private LinearLayout layout_balance_pay;  //惠员包余额支付
    private View view_balance_pay;
    private LinearLayout layout_back_right;   //返回键
    private EditText right_away_edit;         //数据框

    private ImageView iv_bank_pay;      //银行卡radiobutton
    private ImageView iv_zhifubao_pay;  //支付宝radiobutton
    private ImageView iv_weixin_pay;    //微信 radiobutton
    private ImageView iv_balance_pay;   //惠员包余额 radiobutton

    private TextView tv_balance; //惠员包余额显示
    private TextView tv_ok_pay;  //确认支付
    private TextView close_tv;   //提示字眼
    // RadioGroup 价格选择
    private RadioGroup radioGroup;
    private RadioButton btn1;
    private RadioButton btn2;
    private RadioButton btn3;


    private String result;
    private int PAY_TYPE = PayTypeEnum.Pingtai.getCode();

    private static final int request_code_paytype = 9; //支付方式返回标识
    private static final int request_code_create_order = 10; //支付预下单
    private static final int request_code_to_pay = 11; //下单
    private String substring;
    private boolean isHalfOff;
    private boolean isRightNew = true;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_new_rightaway_online);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        isHalfOff = intent.getBooleanExtra("isHalfOff", false);
    }

    @Override
    public void initView() {
        // LinearLayout
        layout_bank_pay = (LinearLayout) findViewById(R.id.layout_bank_pay);
        view_bank_pay = findViewById(R.id.view_bank_pay);
        layout_zhifubao_pay = (LinearLayout) findViewById(R.id.layout_zhifubao_pay);
        view_zhifubao_pay = findViewById(R.id.view_zhifubao_pay);
        layout_weixin_pay = (LinearLayout) findViewById(R.id.layout_weixin_pay);
        view_weixin_pay = findViewById(R.id.view_weixin_pay);
        layout_balance_pay = (LinearLayout) findViewById(R.id.layout_balance_pay);
        view_balance_pay = findViewById(R.id.view_balance_pay);
        layout_back_right = (LinearLayout) findViewById(R.id.layout_back_right);
        // EditText
        right_away_edit = (EditText) findViewById(R.id.right_away_edit); // 输入内容
        // RadioButton
        iv_bank_pay = (ImageView) findViewById(R.id.iv_bank_pay);
        iv_zhifubao_pay = (ImageView) findViewById(R.id.iv_zhifubao_pay);
        iv_weixin_pay = (ImageView) findViewById(R.id.iv_weixin_pay);
        iv_balance_pay = (ImageView) findViewById(R.id.iv_balance_pay);
        // TextView
        tv_balance = (TextView) findViewById(R.id.tv_balance);        // 惠员余额Text
        iv_balance_pay.setBackgroundResource(R.mipmap.select_true); // 默认以惠员包余额为首选支付方式
        tv_ok_pay = (TextView) findViewById(R.id.tv_ok_pay);          // 确定按钮
        close_tv = (TextView) findViewById(R.id.close_tv);
        right_away_edit.setVisibility(View.GONE);
        right_away_edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        radioGroup = (RadioGroup) findViewById(R.id.rg_rightaway);
        btn1 = (RadioButton) findViewById(R.id.btn1);
        btn2 = (RadioButton) findViewById(R.id.btn2);
        btn3 = (RadioButton) findViewById(R.id.btn3);

    }

    @Override
    public void afterInitView() {
        right_away_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //获取焦点
                    close_tv.setVisibility(View.GONE);
                } else if (!hasFocus && right_away_edit.getText().toString().equals("")) {
                    //失去焦点
                    close_tv.setVisibility(View.VISIBLE);
                }
            }
        });

        right_away_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        right_away_edit.setText(s);
                        right_away_edit.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    right_away_edit.setText(s);
                    right_away_edit.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        right_away_edit.setText(s.subSequence(0, 1));
                        right_away_edit.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    if (!s.toString().contains(".") && s.toString().substring(0, 1).equals("0") && !(s.toString().substring(1, 2).equals("0"))) {
                        right_away_edit.setText(s.toString().substring(s.length() - 1) + ".00");
                        right_away_edit.setSelection((s.toString().substring(s.length() - 1) + ".00").length());
                    } else if (s.toString().substring(0, 1).equals("0") && s.toString().substring(1, 2).equals("0")) {
                        right_away_edit.setText(s.toString().substring(s.length() - 1) + ".00");
                        right_away_edit.setSelection((s.toString().substring(s.length() - 1) + ".00").length());
                    }
                }

                if (right_away_edit.getText().toString().length() > 0) {

                    if (right_away_edit.getText().toString().substring(0, 1).equals(".")) {
                        if (right_away_edit.getText().toString().length() > 1 && !(right_away_edit
                                .getText().toString().substring(right_away_edit.getText().toString()
                                        .length() - 1).equals("0"))) {

                        } else {
                        }
                    } else if (Double.parseDouble(right_away_edit.getText().toString()) > 0) {
                    }
                }
            }
        });
        getHttpPayType();
    }

    /**
     * 获取五折店的支付方式
     */
    private void getHttpPayType() {
        Wethod.httpGet(RightAwayOnLineNewActivity.this, request_code_paytype, Config.web.fine_discount_paytype, this);
    }

    /**
     * 预下单
     *
     * @param total_amount
     */
    private void getHttpCreateOrder(String total_amount) {
        Map<String, String> maps = new HashMap<>();
        maps.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        maps.put("total_amount_aes", AES.encrypt(total_amount, AES.key));
        Wethod.httpPost(RightAwayOnLineNewActivity.this, request_code_create_order, Config.web.fine_discount_recharge, maps, this);
    }

    private void toHttpPay(String pre_orderid) {
        Map<String, String> maps = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        if (PAY_TYPE == PayTypeEnum.Pingtai.getCode()) {
            param.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));//当使用平台余额支付时候需要pkregister.pkmuser  param.put("pkmuser",pkmuser);
        }
        param.put("orderid", pre_orderid);//订单主键
        param.put("dealtype", PayDealTypeEnum.WuzhedianChongzhi.getCode() + "");//业务类型 1.充值 2立即买单 3优惠券  后续待定
        param.put("amount", substring);//金额
//                param.put("amount","0.01");//金额
        // 1支付宝 2财付通 3百度钱包 4 网银 5余额支付 6微信支付 7现金支付 8其他支付 9平台支付 10建行在线
        param.put("paytype", "" + PAY_TYPE);
        param.put("paySubject", "五折店充值");
        param.put("payBody", "五折店充值");
        Wethod.httpPost(this, request_code_to_pay, Config.web.pay_new_before, param, this);
    }

    @Override
    public void bindListener() {
        layout_bank_pay.setOnClickListener(this);
        layout_zhifubao_pay.setOnClickListener(this);
        layout_weixin_pay.setOnClickListener(this);
        layout_balance_pay.setOnClickListener(this);
        tv_ok_pay.setOnClickListener(this);
        layout_back_right.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_back_right:
                finish();
                break;
            /**
             * 选择银联支付
             */
            case R.id.layout_bank_pay:
                PAY_TYPE = PayTypeEnum.Wangyin.getCode();
                iv_bank_pay.setBackgroundResource(R.mipmap.select_true);
                iv_zhifubao_pay.setBackgroundResource(R.mipmap.select_false);
                iv_weixin_pay.setBackgroundResource(R.mipmap.select_false);
                iv_balance_pay.setBackgroundResource(R.mipmap.select_false);
                break;
            /**
             * 选择支付宝支付
             */
            case R.id.layout_zhifubao_pay:
                PAY_TYPE = PayTypeEnum.Zhifubao.getCode();
                iv_bank_pay.setBackgroundResource(R.mipmap.select_false);
                iv_zhifubao_pay.setBackgroundResource(R.mipmap.select_true);
                iv_weixin_pay.setBackgroundResource(R.mipmap.select_false);
                iv_balance_pay.setBackgroundResource(R.mipmap.select_false);
                break;
            /**
             *选择微信支付
             */
            case R.id.layout_weixin_pay:
                PAY_TYPE = PayTypeEnum.Weixin.getCode();
                iv_bank_pay.setBackgroundResource(R.mipmap.select_false);
                iv_zhifubao_pay.setBackgroundResource(R.mipmap.select_false);
                iv_weixin_pay.setBackgroundResource(R.mipmap.select_true);
                iv_balance_pay.setBackgroundResource(R.mipmap.select_false);
                break;
            /**
             * 选择余额支付
             */
            case R.id.layout_balance_pay:
                PAY_TYPE = PayTypeEnum.Pingtai.getCode();
                iv_bank_pay.setBackgroundResource(R.mipmap.select_false);
                iv_zhifubao_pay.setBackgroundResource(R.mipmap.select_false);
                iv_weixin_pay.setBackgroundResource(R.mipmap.select_false);
                iv_balance_pay.setBackgroundResource(R.mipmap.select_true);
                break;
            case R.id.tv_ok_pay:
                String many = "";
                if(btn1.getId()==radioGroup.getCheckedRadioButtonId()){
                    many = btn1.getText().toString();
                }else if(btn2.getId()==radioGroup.getCheckedRadioButtonId()){
                    many = btn2.getText().toString();
                }else if(btn3.getId()==radioGroup.getCheckedRadioButtonId()){
                    many = btn3.getText().toString();
                }
                substring = many.substring(0, many.length() - 1);
//                ToastUtil.showToast(RightAwayOnLineNewActivity.this, substring);

//                String many = right_away_edit.getText().toString();
//                if(result.equals("")){
//                    ToastUtil.showToast(this, "请输入正确金额");
//                    break;
//                }
//                if (many.equals("") || many.startsWith("-") || (right_away_edit
//                        .getText().toString().equals("0")) || (right_away_edit
//                        .getText().toString().equals("0.0")) || (right_away_edit
//                        .getText().toString().equals("0.00"))) {
//                    break;
//                }
                if (isPostPreOrder(substring)) {
                    getHttpCreateOrder(substring);
                }
                break;
        }
    }

    /**
     * 判断是否需要支付密码
     *
     * @return
     */
    private boolean isPostPreOrder(final String total_money) {
        if (PAY_TYPE == PayTypeEnum.Pingtai.getCode()) {
            if (getFromSharePreference(Config.userConfig.paypassword).equals("") || getFromSharePreference(Config.userConfig.paypassword) == null) {
                //未设置支付密码
                Toast.makeText(this, "请设置支付密码", Toast.LENGTH_LONG).show();
                Intent mLogPsdIntent = new Intent(this, ChangePasswordActivity.class);
                mLogPsdIntent.putExtra("psdType", false);
                startActivityForResult(mLogPsdIntent, 0);
                return false;
            } else {
                    if (Double.parseDouble(total_money) <= Double.parseDouble(tv_balance.getText().toString())) {
                        CheckPwdDialog psdDialog = new CheckPwdDialog(this, "金额", "五折卡充值", "¥" + total_money, new CheckPwdDialog.CheckPayPasswordListener() {
                            @Override
                            public void onPass() {
                                getHttpCreateOrder(total_money);
                            }
                        });
                        psdDialog.show();
                        return false;
                    } else {
                        ToastUtil.showToast(RightAwayOnLineNewActivity.this, "平台余额不足，请使用其他支付方式");
                    }
            }
        }
        return true;
    }


    @Override
    public void onSuccess(int reqcode, String result) {
        switch (reqcode) {
            case request_code_paytype:
                if (StringUtil.isNotEmpty(result)) {
                    ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                    try {
                        ObjectModelBean<PayTypeBean> pay_type_model = objectMapper.readValue(result, new TypeReference<ObjectModelBean<PayTypeBean>>() {
                        });
                        display_paytype_view(pay_type_model.getResultData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtil.showToast(this, "网络连接错误， 请检查网络");
                    finish();
                }
                break;
            case request_code_create_order:
                if (StringUtil.isNotEmpty(result)) {
                    ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                    try {
                        ObjectModelBean<PrePayOrderId> pay_type_model = objectMapper.readValue(result, new TypeReference<ObjectModelBean<PrePayOrderId>>() {
                        });
                        toHttpPay(pay_type_model.getResultData().getPkorderid());
                        if(isHalfOff){
                            Intent intent = new Intent(this,RightAwayNewActivity.class);
                            intent.putExtra("isRightNew",isRightNew);
                            startActivity(intent);
                            finish();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case request_code_to_pay:
                if (StringUtil.isNotEmpty(result)) {
                    if (PAY_TYPE == PayTypeEnum.Pingtai.getCode()) {
                        //平台余额
                        Toast.makeText(this, "购买成功", Toast.LENGTH_LONG).show();
                        finish();
                    } else if (PAY_TYPE == PayTypeEnum.Zhifubao.getCode()) {
                        //支付宝
                        try {
                            NewPayData newPayData = getConfiguration().readValue(result.toString(), NewPayData.class);
                            NewAlipay newAlipay = new NewAlipay(this, Alipay_handlers);
                            newAlipay.setProduct(newPayData.getResultData().getContent());
                            newAlipay.startAlipay();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (PAY_TYPE == PayTypeEnum.Weixin.getCode()) {
                        //微信
                        try {
                            NewPayWxData newPayWxData = getConfiguration().readValue(result.toString(), NewPayWxData.class);
                            try {
                                JSONObject json = new JSONObject(newPayWxData.getResultData().getContent());
                                WXHelper wxHelper = new WXHelper(this, json.getString("appid"));
                                PayReq req = new PayReq();
                                req.appId = json.getString("appid");
                                req.partnerId = json.getString("partnerid");
                                req.prepayId = json.getString("prepayid");
                                req.nonceStr = json.getString("noncestr");
                                req.timeStamp = json.getString("timestamp");
                                req.packageValue = json.getString("package");
                                req.sign = json.getString("sign");
                                req.extData = "success";
                                wxHelper.pay(req);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (PAY_TYPE == PayTypeEnum.Wangyin.getCode()) {
                        //银联 NewYinLianPayData
                        try {
                            NewYinLianPayData newYinLianPayData = getConfiguration().readValue(result.toString(), NewYinLianPayData.class);
                            Log.e("ppp", "newYinLianPayData:" + newYinLianPayData.getResultData().getTn());
                            goUnionpay(newYinLianPayData.getResultData().getTn());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    private Handler Alipay_handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ALIPAY_SUCCESS://支付宝支付成功
                    Toast.makeText(RightAwayOnLineNewActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case ALIPAY_FAIL://支付宝支付失败
                    Toast.makeText(RightAwayOnLineNewActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    break;
                case ALIPAY_TO_BE_CONFIRMED://支付宝支付待确认

                    break;
                case ALIPAY_CANCEL://支付宝支付取消
                    Toast.makeText(RightAwayOnLineNewActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
        int ret = UPPayAssistEx.startPay(this, null, null, tn, mode);
        Log.e("TYZ", "ret:" + ret);
        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
            // 需要重新安装控件

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UPPayAssistEx.installUPPayPlugin(RightAwayOnLineNewActivity.this);
                            dialog.dismiss();
                        }
                    });

            builder.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
    }


    /**
     * 接收银联回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 22) {
            String result = data.getExtras().getString("result");
            Toast.makeText(this, "result:" + result, Toast.LENGTH_LONG).show();
        } else {
            String str = data.getExtras().getString("pay_result");
            if (str.equalsIgnoreCase("success")) {
                /**********支付成功************/
                Toast.makeText(this, "支付成功", Toast.LENGTH_LONG);
                finish();
            } else if (str.equalsIgnoreCase("fail")) {
                /***********支付失败************/
                Toast.makeText(this, "支付失败", Toast.LENGTH_LONG);
            } else if (str.equalsIgnoreCase("cancel")) {
                Toast.makeText(this, "支付取消", Toast.LENGTH_LONG);
            }
        }
    }

    /**
     *   显示支付方式
     * @param pay_type_bean
     */
    private void display_paytype_view(PayTypeBean pay_type_bean) {
        String[] pay_type_split = pay_type_bean.getPaytype().split("\\|");
        for (int i = 0; i < pay_type_split.length; i++) {
            if (PayTypeEnum.Zhifubao.getCode() == StringUtil.getInt(pay_type_split[i], -1)) {
                layout_zhifubao_pay.setVisibility(View.VISIBLE);
                view_zhifubao_pay.setVisibility(View.VISIBLE);
            } else if (PayTypeEnum.Wangyin.getCode() == StringUtil.getInt(pay_type_split[i], -1)) {
                layout_bank_pay.setVisibility(View.VISIBLE);
                view_bank_pay.setVisibility(View.VISIBLE);
            } else if (PayTypeEnum.Weixin.getCode() == StringUtil.getInt(pay_type_split[i], -1)) {
                layout_weixin_pay.setVisibility(View.VISIBLE);
                view_weixin_pay.setVisibility(View.VISIBLE);
            } else if (PayTypeEnum.Pingtai.getCode() == StringUtil.getInt(pay_type_split[i], -1)) {
                layout_balance_pay.setVisibility(View.VISIBLE);
                view_balance_pay.setVisibility(View.VISIBLE);
                tv_balance.setText(getIntent().getStringExtra("platbalance"));
            }
        }
    }


    @Override
    public void onFailed(int reqcode, String result) {
        Wethod.ToFailMsg(this, result);
    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
