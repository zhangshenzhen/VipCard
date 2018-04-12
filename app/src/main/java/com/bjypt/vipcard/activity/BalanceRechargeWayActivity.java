package com.bjypt.vipcard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.base.RespBase;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.Alipay;
import com.bjypt.vipcard.model.WxpayData;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.MD5;
import com.bjypt.vipcard.utils.MyDialogUtil;
import com.bjypt.vipcard.view.CustomProgressDialog;
import com.bjypt.vipcard.view.MyDialog;
import com.bjypt.vipcard.wxapi.WXHelper;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.unionpay.UPPayAssistEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/6/18
 * Use by 当支付时余额不足,跳转到此页面支付充值金额
 *
 * 定义：当向商家充值时  FLAG = 1;  当向平台充值时 FLAG = 2;
 */
public class BalanceRechargeWayActivity extends BaseActivity implements VolleyCallBack {


    private LinearLayout back_way;//返回
    private TextView tv_way_amount;//支付的金额
    private TextView tv_ensure;//确认
    private LinearLayout layout_vipcard_amount;//惠员包余额支付
    private LinearLayout layout_wechant_amount;//微信支付
    private LinearLayout layout_alipay_amount;//支付宝支付
    private LinearLayout layout_unpay_amount;//银联支付
    private ImageView iv_vipcard_select; //惠员包 支付方式的选中与未选中状态图片
    private ImageView iv_wechant_select;    //微信 支付方式的选中与未选中状态图片
    private ImageView iv_alipay_select;   //支付宝 支付方式的选中与未选中状态图片
    private ImageView iv_unpay_select;    //银联 支付方式的选中与未选中状态图片
    private int PAY_TYPE = 0;//支付方式 1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付 9 平台余额支付

    private String payMoney;
    private int FLAG;
    private String tn;
    private String pkmuser;
    private String primaryk;//前一页面生成的订单返回的订单号
    private PsdDialog psdDialog;
    private CustomProgressDialog dialog;
    private Boolean PSD_TYPE = false;
    private int UN_FLAG = 1;
    private String key = "";
    private EditText et;
    Context mContext;



    /*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
    private final String mMode = "00";

    private Handler Alipay_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("CXY", "msg.what:" + msg.what);
            switch (msg.what) {
                case ALIPAY_SUCCESS://支付宝支付成功
                    Toast.makeText(BalanceRechargeWayActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                    updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, primaryk, 1, "ALIPAY_SUCCESS", 1, payMoney, 1, payMoney);
//                    finish();
                    break;
                case ALIPAY_FAIL://支付宝支付失败
                    Toast.makeText(BalanceRechargeWayActivity.this, "充值失败", Toast.LENGTH_SHORT).show();
                    break;
                case ALIPAY_TO_BE_CONFIRMED://支付宝支付待确认
                    Toast.makeText(BalanceRechargeWayActivity.this, "充值结果待确认", Toast.LENGTH_SHORT).show();
                    break;
                case ALIPAY_CANCEL://支付宝支付取消
                    Toast.makeText(BalanceRechargeWayActivity.this, "充值取消", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_topup_way);
    }

    @Override
    public void beforeInitView() {
        mContext = this;
        Intent intent = getIntent();
        payMoney = intent.getStringExtra("payMoney");
        FLAG = intent.getIntExtra("FLAG", 0);
        pkmuser = intent.getStringExtra("pkmuser");
        primaryk = intent.getStringExtra("primaryk");
        Log.e("liyunte","-BalanceRechargeWayActivity---primaryk"+primaryk);
    }

    @Override
    public void initView() {

        tv_way_amount = (TextView) findViewById(R.id.tv_way_amount);
        tv_ensure = (TextView) findViewById(R.id.tv_ensure);
        back_way = (LinearLayout) findViewById(R.id.back_way);
        layout_vipcard_amount = (LinearLayout) findViewById(R.id.layout_vipcard_amount);
        layout_wechant_amount = (LinearLayout) findViewById(R.id.layout_wechant_amount);
        layout_alipay_amount = (LinearLayout) findViewById(R.id.layout_alipay_amount);
        layout_unpay_amount = (LinearLayout) findViewById(R.id.layout_unpay_amount);
        iv_vipcard_select = (ImageView) findViewById(R.id.iv_vipcard_select);
        iv_wechant_select = (ImageView) findViewById(R.id.iv_wechant_select);
        iv_alipay_select = (ImageView) findViewById(R.id.iv_alipay_select);
        iv_unpay_select = (ImageView) findViewById(R.id.iv_unpay_select);


        tv_way_amount.setText("¥ " + payMoney);
        if (FLAG == 2 ) {
            layout_vipcard_amount.setVisibility(View.GONE);
        } else if (FLAG == 1) {
            layout_vipcard_amount.setVisibility(View.GONE);
        }

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        back_way.setOnClickListener(this);
        tv_ensure.setOnClickListener(this);
        layout_vipcard_amount.setOnClickListener(this);
        layout_wechant_amount.setOnClickListener(this);
        layout_alipay_amount.setOnClickListener(this);
        layout_unpay_amount.setOnClickListener(this);


    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.back_way:
                finish();
                break;
            /**
             * 确定
             */
            case R.id.tv_ensure:
                if (PAY_TYPE == 0) {
                    Toast.makeText(this, "请选择充值方式", Toast.LENGTH_LONG).show();
                } else if (PAY_TYPE == 9) {
                    //向商家充值时，此时使用的是平台余额充值
                    psdDialog = new PsdDialog(this, "充值", "", "¥" + payMoney);
                    psdDialog.show();

                } else {
                    dialog = new CustomProgressDialog(this, "正在加载中", R.anim.frame);

                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable());

                    Map<String, String> tnMap = new HashMap<>();
                    tnMap.put("userId", AES.encrypt(getFromSharePreference(Config.userConfig.pkregister), AES.key));
                    //根据选择的CheckBox的选中状态判断其支付类型//支付类型  1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付
                    tnMap.put("rechargeCode", AES.encrypt(PAY_TYPE + "", AES.key));
                    tnMap.put("queryType", AES.encrypt("1", AES.key));//// 1 前台充值 2 后台充值 3 前台消费     4定金支付
                    tnMap.put("phoneno", AES.encrypt(getFromSharePreference(Config.userConfig.phoneno), AES.key));//用户手机号
                    tnMap.put("timeStamp", AES.encrypt(System.currentTimeMillis() + "", AES.key));

                    tnMap.put("balance", AES.encrypt(payMoney, AES.key));
                    tnMap.put("primaryk", AES.encrypt(primaryk, AES.key));
                    if(PAY_TYPE == 6){
                        //微信支付
                        Map<String,String> wxMap = new HashMap<>();
                        wxMap.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
                        wxMap.put("muname","惠缘包");
                        wxMap.put("payment","6");
                        wxMap.put("paysource","1");
                        wxMap.put("amount",AES.encrypt(payMoney,AES.key));
                        wxMap.put("primaryid",primaryk);
                        Wethod.httpPost(BalanceRechargeWayActivity.this,1992,Config.web.wx_pay,wxMap,this);
                    }else{
                        getTnHttpPost(Config.web.URL_pay + "pay/yinlian/queryTn", tnMap);
                    }


                }
                break;
            /**
             * 惠员包余额支付方式
             */
            case R.id.layout_vipcard_amount:
                setSelect(1);
                break;
            /**
             * 微信支付方式
             */
            case R.id.layout_wechant_amount:
                setSelect(2);
                break;
            /**
             * 支付宝支付方式
             */
            case R.id.layout_alipay_amount:
                setSelect(3);
                break;
            /**
             * 银联支付方式
             */
            case R.id.layout_unpay_amount:
                setSelect(4);
                break;
        }
    }

    public void setSelect(int type) {
        if (type == 1) {
            PAY_TYPE = 9;
            iv_vipcard_select.setBackgroundResource(R.mipmap.select_true);
            iv_wechant_select.setBackgroundResource(R.mipmap.select_false);
            iv_alipay_select.setBackgroundResource(R.mipmap.select_false);
            iv_unpay_select.setBackgroundResource(R.mipmap.select_false);
        } else if (type == 2) {
            PAY_TYPE = 6;
            iv_vipcard_select.setBackgroundResource(R.mipmap.select_false);
            iv_wechant_select.setBackgroundResource(R.mipmap.select_true);
            iv_alipay_select.setBackgroundResource(R.mipmap.select_false);
            iv_unpay_select.setBackgroundResource(R.mipmap.select_false);
        } else if (type == 3) {
            PAY_TYPE = 1;
            iv_vipcard_select.setBackgroundResource(R.mipmap.select_false);
            iv_wechant_select.setBackgroundResource(R.mipmap.select_false);
            iv_alipay_select.setBackgroundResource(R.mipmap.select_true);
            iv_unpay_select.setBackgroundResource(R.mipmap.select_false);
        } else {
            PAY_TYPE = 4;
            iv_vipcard_select.setBackgroundResource(R.mipmap.select_false);
            iv_wechant_select.setBackgroundResource(R.mipmap.select_false);
            iv_alipay_select.setBackgroundResource(R.mipmap.select_false);
            iv_unpay_select.setBackgroundResource(R.mipmap.select_true);
        }
    }


    /**
     * 使用平台余额充值
     *
     * @param pkregister   用户Id
     * @param phoneNo      用户手机号
     * @param pkmuser      商家Id
     * @param pkpayid      充值或者预约或者消费主键
     * @param amountStatus 定金支付状态【0：未完成   1：已完成    2：充值异   常     3：充值失败 4：充值取消】(当用余额支付时，直接传入 1 )
     * @param payResult    支付结果
     * @param payment      支付方式 【1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付 9 平台余额支付】
     * @param amount       支付金额
     * @param payEntrance  支付入口 【1 充值支付 2 预约定金支付 3 消费支付】
     */
    public void payMoneyByBalance(String pkregister, String phoneNo, String pkmuser, String pkpayid,
                                  String amountStatus, String payResult, String payment, String amount, String payEntrance, String waitMoney) {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", pkregister);
        params.put("phoneNo", phoneNo);
        params.put("pkmuser", pkmuser);
        params.put("pkpayid", pkpayid);
        params.put("amountStatus", amountStatus);
        params.put("payResult", payResult);
        params.put("payment", payment);
        params.put("amount", AES.encrypt(amount, AES.key));
        params.put("payEntrance", payEntrance);
        params.put("waitMoney", AES.encrypt(waitMoney, AES.key));
        Log.e("wexpay-2","notice2");
        Wethod.httpPost(BalanceRechargeWayActivity.this,1, Config.web.update_payresult, params, this);
    }


    /**
     * 获取TN值
     *
     * @param url
     * @param hMap
     */
    public void getTnHttpPost(String url, final Map<String, String> hMap) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject object = new JSONObject(s.toString());
                    JSONArray array = object.getJSONArray("result");
                    if (null != array && array.length() > 0) {
                        tn = array.getJSONObject(0).getString("tn");
                        dialog.cancel();
                        if (PAY_TYPE == 1) {
                            /**
                             * 支付宝
                             */
                            Alipay alipay = new Alipay(BalanceRechargeWayActivity.this, Alipay_handler);
                            alipay.setProduct("充值", "充值", payMoney, tn, primaryk, "1");
                            alipay.startAlipay();
                        } else if (PAY_TYPE == 4) {
                            /**
                             * 银联支付
                             */
                            goUnionpay(tn);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map = hMap;
                return map;
            }
        };

        MyApplication.getHttpQueue().add(stringRequest);
    }

    /**
     * 银联
     *
     * @param activity
     * @param tn
     * @param mode
     */
    @Override
    public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
        int ret = UPPayAssistEx.startPay(this, null, null, tn, mode);
        Log.e("TYZ", "ret:" + ret);
        if(ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED){
            // 需要重新安装控件
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UPPayAssistEx
                                    .installUPPayPlugin(BalanceRechargeWayActivity.this);
                            UN_FLAG = 2;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /********************************************
         *  银联支付回调处理结果
         ********************************************/

        if(UN_FLAG == 1){
            String str = data.getExtras().getString("pay_result");
            if (str.equalsIgnoreCase("success")) {
                /**********支付成功************/
                updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, primaryk, 1, "Success", 4, payMoney, 1, payMoney);
            } else if (str.equalsIgnoreCase("fail")) {
                /***********支付失败************/
                updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, primaryk, 1, "fail", 4, payMoney, 1, payMoney);
            } else if (str.equalsIgnoreCase("cancel")) {
                /***********取消支付************/
                updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, primaryk, 1, "cancel", 4, payMoney, 1, payMoney);
            }
        }else{
            UN_FLAG = 1;
        }



    }

    /**
     * 更新后台改变的数据
     *
     * @param pkregister
     * @param phoneNo
     * @param pkmuser
     * @param pkpayid
     * @param amountStatus
     * @param payResult
     * @param payment
     * @param amount
     * @param payEntrance
     */
    private void updatePayResult(String pkregister, String phoneNo, String pkmuser, String pkpayid, int amountStatus, String payResult
            , int payment, String amount, int payEntrance, String waitMoney) {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", pkregister);
        params.put("phoneNo", phoneNo);
        params.put("pkmuser", pkmuser);
        params.put("pkpayid", pkpayid);
        params.put("amountStatus", amountStatus + "");
        params.put("payResult", payResult);
        params.put("payment", payment + "");
        params.put("amount", AES.encrypt(amount, AES.key));
        params.put("payEntrance", payEntrance + "");
        params.put("waitMoney", AES.encrypt(waitMoney, AES.key));
        Log.e("wexpay-1","notice1");
        Wethod.httpPost(BalanceRechargeWayActivity.this,2, Config.web.update_payresult, params, this);
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
                MyDialogUtil.setDialog(this, "充值成功", "恭喜您已充值成功!", "", "返回明细", "继续支付", new MyDialog.ClickListenerInterface() {
                    @Override
                    public void doButtomOne() {
                        //跳转到资金明细页面
                        Intent intent = new Intent(BalanceRechargeWayActivity.this, RechargeRecordActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void doButtomTwo() {
                        //继续去支付
                        finish();
                    }
                });


        } else if (reqcode == 2) {
            Log.e("liyunte","balanceRechargeWayAcitivyt -------------------");
                MyDialogUtil.setDialog(this, "充值成功", "恭喜您已充值成功!", "", "返回明细", "继续支付", new MyDialog.ClickListenerInterface() {
                    @Override
                    public void doButtomOne() {
                        //跳转到资金明细页面
                        if (FLAG == 1) {
                            //跳转到商家明细
                            Intent intent = new Intent(BalanceRechargeWayActivity.this, FundListActivity.class);

                            startActivity(intent);
                            finish();
                        } else if(FLAG == 2){
                            finish();
                        }

                    }

                    @Override
                    public void doButtomTwo() {
                        //继续支付
                        finish();
                    }
                });
        } else if (reqcode == 1314) {
            psdDialog.dismiss();
            Toast.makeText(this, "密码校验通过", Toast.LENGTH_LONG).show();
            payMoneyByBalance(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, primaryk, 1 + "", "success", 9 + "", payMoney, 1 + "", payMoney);
        }else if(reqcode == 1992){
            dialog.dismiss();
            try {
                WxpayData wxpayData = getConfiguration().readValue(result.toString(), WxpayData.class);
                WXHelper wxHelper = new WXHelper(mContext,wxpayData.getResultData().getAppid());
                PayReq req = new PayReq();
                req.appId = wxpayData.getResultData().getAppid();
                req.partnerId = wxpayData.getResultData().getPartnerid();
                req.prepayId = wxpayData.getResultData().getPrepayid();
                req.nonceStr = wxpayData.getResultData().getNoncestr();
                req.timeStamp = wxpayData.getResultData().getTimestamp();
                req.packageValue = wxpayData.getResultData().getPackage_app();
                req.extData = "success";

                wxHelper.pay(req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 2) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 1314) {

            try {
                RespBase respBase = getConfiguration().readValue(result.toString(), RespBase.class);
                if(respBase.getResultData().toString().contains("未查询到用户支付密码信息")) {
                    psdDialog.dismiss();
                    Intent intent = new Intent(this,ChangePasswordActivity.class);
                    intent.putExtra("psdType", PSD_TYPE);
                    startActivity(intent);
                }else if(respBase.getResultData().toString().contains("支付密码输入有误")){

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
                                    Intent intent = new Intent(BalanceRechargeWayActivity.this,ForgetPayPsdActivity.class);
                                    startActivity(intent);
                                }
                            });
                    builder.create().show();

                }else{
                    Toast.makeText(this,respBase.getResultData().toString(),Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(reqcode == 1992){
            dialog.cancel();
            Wethod.ToFailMsg(this,result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    /*@Override
    public void onReq(BaseReq baseReq) {
        Toast.makeText(this,"onReq",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Toast.makeText(this,"onResp",Toast.LENGTH_LONG).show();
    }*/


    class PsdDialog extends Dialog {


        private Context context;
        private String messageOne, MessageTwo, title;
        private TextView mProductName;
        private TextView mProductMoney;
        private RelativeLayout mBack;
        private TextView mPsdTv;

        TextView t1, t2, t3, t4, t5, t6;

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
                    Wethod.httpPost(BalanceRechargeWayActivity.this,1314, Config.web.checkout_pay_psd, payPsdParams, BalanceRechargeWayActivity.this);
                }
                setKey();
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
        if(PayOnLineActivity.WXPAY_FLAG == 2){
            updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, primaryk, 1, "Success", 6, payMoney, 1, payMoney);
            Log.e("liyunte","BalanceRecharger----"+"pkmuser"+pkmuser+"primaryk"+primaryk+"payMoney"+payMoney);
            PayOnLineActivity.WXPAY_FLAG = 1;
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }
    // 微信发送请求到第三方应用时，会回调到该方法

}
