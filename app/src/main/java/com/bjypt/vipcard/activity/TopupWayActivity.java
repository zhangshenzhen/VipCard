package com.bjypt.vipcard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.bjypt.vipcard.common.PayDealTypeEnum;
import com.bjypt.vipcard.common.PayTypeEnum;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.Alipay;
import com.bjypt.vipcard.config.NewAlipay;
import com.bjypt.vipcard.model.NewPayData;
import com.bjypt.vipcard.model.NewPayWxData;
import com.bjypt.vipcard.model.NewYinLianPayData;
import com.bjypt.vipcard.model.WxpayData;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.MD5;
import com.bjypt.vipcard.utils.MyDialogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
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

public class TopupWayActivity extends BaseActivity implements VolleyCallBack {


    private static final int request_pay_before = 2222;
    public static int TOPUP_FLAG = 1;
    private LinearLayout back_way;                       //返回
    private TextView tv_way_amount;                     //支付的金额
    private TextView tv_ensure;                         //确认
    private LinearLayout layout_vipcard_amount;       //惠员包余额支付
    private LinearLayout layout_wechant_amount;       //微信支付
    private LinearLayout layout_alipay_amount;        //支付宝支付
    private LinearLayout layout_unpay_amount;         //银联支付
    private LinearLayout layout_jianhang;             //建行支付
    private ImageView iv_vipcard_select;              //惠员包 支付方式的选中与未选中状态图片
    private ImageView iv_wechant_select;              //微信 支付方式的选中与未选中状态图片
    private ImageView iv_alipay_select;               //支付宝 支付方式的选中与未选中状态图片
    private ImageView iv_unpay_select;                //银联 支付方式的选中与未选中状态图片
    private ImageView iv_select_jianhang;             //建行
    private int PAY_TYPE = 0;                         //支付方式 1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付 9 平台余额支付

    private String payMoney;
    private int FLAG;
    private String tn;
    private String pkmuser;
    private String primaryk;                          //前一页面生成的订单返回的订单号
    private String outorderid;                       //支付中心对用的订单号
    private PsdDialog psdDialog;
    // private CustomProgressDialog dialog;
    private Boolean PSD_TYPE = false;
    private int UN_FLAG = 1;
    private String key = "";
    private EditText et;
    Context mContext;
    public static int WXPAY_FLAG = 1;


    private int topup_flag = 1;

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
                    payFinish();
                    //                    Toast.makeText(TopupWayActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                    //                    updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, primaryk, 1, "ALIPAY_SUCCESS", 1, payMoney, 1, payMoney);
                    //                    finish();
                    break;
                case ALIPAY_FAIL://支付宝支付失败
                    Toast.makeText(TopupWayActivity.this, "充值失败", Toast.LENGTH_SHORT).show();
                    break;
                case ALIPAY_TO_BE_CONFIRMED://支付宝支付待确认
                    Toast.makeText(TopupWayActivity.this, "充值结果待确认", Toast.LENGTH_SHORT).show();
                    break;
                case ALIPAY_CANCEL://支付宝支付取消
                    //                    Toast.makeText(TopupWayActivity.this, "充值取消", Toast.LENGTH_SHORT).show();
                    chargeUpdateCancle(primaryk);
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
        Log.e("TYZ", "FLAG:" + FLAG);
        pkmuser = intent.getStringExtra("pkmuser");
        primaryk = intent.getStringExtra("primaryk");
        outorderid = intent.getStringExtra("outorderid");
    }

    @Override
    public void initView() {
        TOPUP_FLAG = 1;
        tv_way_amount = (TextView) findViewById(R.id.tv_way_amount);
        tv_ensure = (TextView) findViewById(R.id.tv_ensure);
        back_way = (LinearLayout) findViewById(R.id.back_way);
        layout_vipcard_amount = (LinearLayout) findViewById(R.id.layout_vipcard_amount);
        layout_wechant_amount = (LinearLayout) findViewById(R.id.layout_wechant_amount);
        layout_alipay_amount = (LinearLayout) findViewById(R.id.layout_alipay_amount);
        layout_unpay_amount = (LinearLayout) findViewById(R.id.layout_unpay_amount);
        layout_jianhang = (LinearLayout) findViewById(R.id.layout_jianhang);
        iv_select_jianhang = (ImageView) findViewById(R.id.iv_select_jianhang);
        iv_vipcard_select = (ImageView) findViewById(R.id.iv_vipcard_select);
        iv_wechant_select = (ImageView) findViewById(R.id.iv_wechant_select);
        iv_alipay_select = (ImageView) findViewById(R.id.iv_alipay_select);
        iv_unpay_select = (ImageView) findViewById(R.id.iv_unpay_select);
        tv_way_amount.setText("¥ " + payMoney);
        if (FLAG == 2 || FLAG == 3) {
            layout_vipcard_amount.setVisibility(View.GONE);
        } else if (FLAG == 1) {
            layout_vipcard_amount.setVisibility(View.VISIBLE);
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
        layout_jianhang.setOnClickListener(this);

    }

    /**
     * 就是QueryTn中的两个值来检测到第三方内部取消时调用该接口
     *
     * @param primaryk
     */
    private void chargeUpdateCancle(String primaryk) {
        Map<String, String> cancleMap = new HashMap<>();
        cancleMap.put("primaryk", primaryk);
        cancleMap.put("queryType", "1");
        Wethod.httpPost(TopupWayActivity.this, 1999, Config.web.update_cancle_result, cancleMap, this);
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
                } else if (PAY_TYPE == PayTypeEnum.Pingtai.getCode()) {
                    //向商家充值时，此时使用的是平台余额充值
                    psdDialog = new PsdDialog(this, "充值", "", "¥" + payMoney);
                    psdDialog.show();
                } else {
                    toPay();
                    //                        dialog = new CustomProgressDialog(this, "正在加载中", R.anim.frame);
                    //                        topup_flag = 2;
                    //                        dialog.show();
                    //                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable());

                    //                    Map<String, String> tnMap = new HashMap<>();
                    //                    tnMap.put("userId", AES.encrypt(getFromSharePreference(Config.userConfig.pkregister), AES.key));
                    //                    //根据选择的CheckBox的选中状态判断其支付类型//支付类型  1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付
                    //                    Log.e("TYZ", "PAY_TYPE:" + PAY_TYPE);
                    //                    tnMap.put("rechargeCode", AES.encrypt(PAY_TYPE + "", AES.key));
                    //                    tnMap.put("queryType", AES.encrypt("1", AES.key));//// 1 前台充值 2 后台充值 3 前台消费     4定金支付
                    //                    tnMap.put("phoneno", AES.encrypt(getFromSharePreference(Config.userConfig.phoneno), AES.key));//用户手机号
                    //                    tnMap.put("timeStamp", AES.encrypt(System.currentTimeMillis() + "", AES.key));
                    //
                    //                    tnMap.put("balance", AES.encrypt(payMoney, AES.key));
                    //                    tnMap.put("primaryk", AES.encrypt(primaryk, AES.key));


                    //                    if(PAY_TYPE == PayTypeEnum.Weixin.getCode()){
                    //                        //微信支付充值
                    //                        Map<String,String> wxMap = new HashMap<>();
                    //                        wxMap.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
                    //                        wxMap.put("muname","惠缘包");
                    //                        wxMap.put("payment","6");
                    //                        wxMap.put("paysource","1");
                    //                        wxMap.put("amount",AES.encrypt(payMoney,AES.key));
                    //                        wxMap.put("primaryid",primaryk);
                    //                        Wethod.httpPost(TopupWayActivity.this,1992,Config.web.wx_pay,wxMap,this);
                    //
                    //                    }else{
                    //                        getTnHttpPost(Config.web.URL_pay + "pay/yinlian/queryTn", tnMap);
                    //通过支付中心下单
                    //                    }
                }
                break;
            /**
             * 惠员包余额支付方式
             */
            case R.id.layout_vipcard_amount:
                setSelect(PayTypeEnum.Pingtai.getCode());
                break;
            /**
             * 微信支付方式
             */
            case R.id.layout_wechant_amount:
                FirstActivity.WXPAY_XX = 2;
                setSelect(PayTypeEnum.Weixin.getCode());
                break;
            /**
             * 支付宝支付方式
             */
            case R.id.layout_alipay_amount:
                setSelect(PayTypeEnum.Zhifubao.getCode());
                break;
            /**
             * 银联支付方式
             */
            case R.id.layout_unpay_amount:
                setSelect(PayTypeEnum.Wangyin.getCode());
                break;
            /**
             * 建行支付方式
             */
            case R.id.layout_jianhang:
                setSelect(PayTypeEnum.Jianhang.getCode());
                break;
        }
    }

    private void toPay() {
        Map<String, String> param = new HashMap<>();
        param.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        param.put("orderid", outorderid);//订单主键
        param.put("pkmuser", pkmuser);
        param.put("dealtype", PayDealTypeEnum.Chongzhi.getCode() + "");//业务类型 1.充值 2立即买单 3优惠券  后续待定
        param.put("amount", payMoney);//金额
        param.put("paytype", "" + PAY_TYPE);
        param.put("paySubject", "平台充值");
        param.put("payBody", "充值");
        Wethod.httpPost(this, request_pay_before, Config.web.pay_new_before, param, this);
    }

    public void setSelect(int type) {
        PAY_TYPE = type;
        if (type == PayTypeEnum.Pingtai.getCode()) {
            iv_vipcard_select.setImageResource(R.mipmap.select_true);
            iv_wechant_select.setImageResource(R.mipmap.select_false);
            iv_alipay_select.setImageResource(R.mipmap.select_false);
            iv_unpay_select.setImageResource(R.mipmap.select_false);
            iv_select_jianhang.setImageResource(R.mipmap.select_false);
        } else if (type == PayTypeEnum.Weixin.getCode()) {
            iv_vipcard_select.setImageResource(R.mipmap.select_false);
            iv_wechant_select.setImageResource(R.mipmap.select_true);
            iv_alipay_select.setImageResource(R.mipmap.select_false);
            iv_unpay_select.setImageResource(R.mipmap.select_false);
            iv_select_jianhang.setImageResource(R.mipmap.select_false);
        } else if (type == PayTypeEnum.Zhifubao.getCode()) {
            iv_vipcard_select.setImageResource(R.mipmap.select_false);
            iv_wechant_select.setImageResource(R.mipmap.select_false);
            iv_alipay_select.setImageResource(R.mipmap.select_true);
            iv_unpay_select.setImageResource(R.mipmap.select_false);
            iv_select_jianhang.setImageResource(R.mipmap.select_false);
        } else if (type == PayTypeEnum.Jianhang.getCode()) {
            iv_vipcard_select.setImageResource(R.mipmap.select_false);
            iv_wechant_select.setImageResource(R.mipmap.select_false);
            iv_alipay_select.setImageResource(R.mipmap.select_false);
            iv_unpay_select.setImageResource(R.mipmap.select_false);
            iv_select_jianhang.setImageResource(R.mipmap.select_true);
        } else {
            iv_vipcard_select.setImageResource(R.mipmap.select_false);
            iv_wechant_select.setImageResource(R.mipmap.select_false);
            iv_alipay_select.setImageResource(R.mipmap.select_false);
            iv_unpay_select.setImageResource(R.mipmap.select_true);
            iv_select_jianhang.setImageResource(R.mipmap.select_false);
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
    //    public void payMoneyByBalance(String pkregister, String phoneNo, String pkmuser, String pkpayid,
    //                                  String amountStatus, String payResult, String payment, String amount, String payEntrance, String waitMoney) {
    //        Map<String, String> params = new HashMap<>();
    //        params.put("pkregister", pkregister);
    //        params.put("phoneNo", phoneNo);
    //        params.put("pkmuser", pkmuser);
    //        params.put("pkpayid", pkpayid);
    //        params.put("amountStatus", amountStatus);
    //        params.put("payResult", payResult);
    //        params.put("payment", payment);
    //        params.put("amount", AES.encrypt(amount, AES.key));
    //        params.put("payEntrance", payEntrance);
    //        params.put("waitMoney", AES.encrypt(waitMoney, AES.key));
    //        Wethod.httpPost(TopupWayActivity.this,1, Config.web.update_payresult, params, this);
    //    }


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
                        //                        dialog.cancel();
                        if (PAY_TYPE == 1) {
                            /**
                             * 支付宝
                             */
                            Alipay alipay = new Alipay(TopupWayActivity.this, Alipay_handler);
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
        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
            // 需要重新安装控件
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UPPayAssistEx.installUPPayPlugin(TopupWayActivity.this);
                            UN_FLAG = 2;
                            dialog.dismiss();
                        }
                    });

            builder.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TopupWayActivity.this.dialog.dismiss();
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
        if (UN_FLAG == 1) {
            String str = data.getExtras().getString("pay_result");
            if (str.equalsIgnoreCase("success")) {
                /**********支付成功************/
                // updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, primaryk, 1, "Success", 4, payMoney, 1, payMoney);
                payFinish();
            } else if (str.equalsIgnoreCase("fail")) {
                /***********支付失败************/
                payFinish();
                // updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, primaryk, 1, "fail", 4, payMoney, 1, payMoney);
            } else if (str.equalsIgnoreCase("cancel")) {
                /***********取消支付************/
                // updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, primaryk, 1, "cancel", 4, payMoney, 1, payMoney);
                chargeUpdateCancle(primaryk);
            }
        } else {
            UN_FLAG = 1;
        }
    }

    /**
     * 更新后台改变的数据
     * <p>
     * //     * @param pkregister
     * //     * @param phoneNo
     * //     * @param pkmuser
     * //     * @param pkpayid
     * //     * @param amountStatus
     * //     * @param payResult
     * //     * @param payment
     * //     * @param amount
     * //     * @param payEntrance
     */

    //    private void updatePayResult(String pkregister, String phoneNo, String pkmuser, String pkpayid, int amountStatus, String payResult
    //            , int payment, String amount, int payEntrance, String waitMoney) {
    //        Map<String, String> params = new HashMap<>();
    //        params.put("pkregister", pkregister);
    //        params.put("phoneNo", phoneNo);
    //        params.put("pkmuser", pkmuser);
    //        params.put("pkpayid", pkpayid);
    //        params.put("amountStatus", amountStatus + "");
    //        params.put("payResult", payResult);
    //        params.put("payment", payment + "");
    //        params.put("amount", AES.encrypt(amount, AES.key));
    //        params.put("payEntrance", payEntrance + "");
    //        params.put("waitMoney", AES.encrypt(waitMoney, AES.key));
    //        Wethod.httpPost(TopupWayActivity.this,2, Config.web.update_payresult, params, this);
    //    }
    private void payFinish() {
        if (FLAG == 3) {
            MyDialogUtil.setDialog(this, "充值成功", "恭喜您已充值成功!", "", "返回明细", "返回话费充值", new MyDialog.ClickListenerInterface() {
                @Override
                public void doButtomOne() {
                    //跳转到资金明细页面
                    Intent intent = new Intent(TopupWayActivity.this, BillDetailsActivity.class);
                    TOPUP_FLAG = 2;
                    startActivity(intent);
                    finish();
                }

                @Override
                public void doButtomTwo() {
                    //跳转到首页
                    TOPUP_FLAG = 2;
                    finish();
                }
            });
        } else {
                /*MyDialogUtil.setDialog(this, "充值成功", "恭喜您已充值成功!", "", "返回明细", "返回首页", new MyDialog.ClickListenerInterface() {
                    @Override
                    public void doButtomOne() {
                        //跳转到资金明细页面
                        if (FLAG == 1) {
                            //跳转到商家明细
                            Intent intent = new Intent(TopupWayActivity.this, BillDetailsActivity.class);
                            TOPUP_FLAG = 2;
                            startActivity(intent);
                            finish();
                        } else if(FLAG == 2){
                            Intent intent = new Intent(TopupWayActivity.this, BillDetailsActivity.class);
                            TOPUP_FLAG = 2;
                            startActivity(intent);
                            finish();
                        }
                    }
                    @Override
                    public void doButtomTwo() {
                        //跳转到首页
                        Intent intent = new Intent(TopupWayActivity.this, MainActivity.class);
                        TOPUP_FLAG = 2;
                        startActivity(intent);
                        finish();
                    }
                });*/
            VipCenterAccountActivity.FLAG_MY_FRAGMENT = 2;
            finish();
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
            if (FLAG == 3) {
                MyDialogUtil.setDialog(this, "充值成功", "恭喜您已充值成功!", "", "返回明细", "返回话费充值", new MyDialog.ClickListenerInterface() {
                    @Override
                    public void doButtomOne() {
                        //跳转到资金明细页面
                        Intent intent = new Intent(TopupWayActivity.this, BillDetailsActivity.class);
                        TOPUP_FLAG = 2;
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void doButtomTwo() {
                        //跳转到首页
                        TOPUP_FLAG = 2;
                        finish();
                    }
                });
            } else {
                finish();
               /* MyDialogUtil.setDialog(this, "充值成功", "恭喜您已充值成功!", "", "返回明细", "返回首页", new MyDialog.ClickListenerInterface() {
                    @Override
                    public void doButtomOne() {
                        //跳转到资金明细页面
                        Intent intent = new Intent(TopupWayActivity.this, BillDetailsActivity.class);
                        TOPUP_FLAG = 2;
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void doButtomTwo() {
                        //跳转到首页
                        Intent intent = new Intent(TopupWayActivity.this, MainActivity.class);
                        TOPUP_FLAG = 2;
                        startActivity(intent);
                        finish();
                    }
                });*/

            }
        } else if (reqcode == 2) {
            if (FLAG == 3) {
                MyDialogUtil.setDialog(this, "充值成功", "恭喜您已充值成功!", "", "返回明细", "返回话费充值", new MyDialog.ClickListenerInterface() {
                    @Override
                    public void doButtomOne() {
                        //跳转到资金明细页面
                        Intent intent = new Intent(TopupWayActivity.this, BillDetailsActivity.class);
                        TOPUP_FLAG = 2;
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void doButtomTwo() {
                        //跳转到首页
                        TOPUP_FLAG = 2;
                        finish();
                    }
                });
            } else {
                VipCenterAccountActivity.FLAG_MY_FRAGMENT = 2;
                finish();
                /*MyDialogUtil.setDialog(this, "充值成功", "恭喜您已充值成功!", "", "返回明细", "返回首页", new MyDialog.ClickListenerInterface() {
                    @Override
                    public void doButtomOne() {
                        //跳转到资金明细页面
                        if (FLAG == 1) {
                            //跳转到商家明细
                            Intent intent = new Intent(TopupWayActivity.this, BillDetailsActivity.class);
                            TOPUP_FLAG = 2;
                            startActivity(intent);
                            finish();
                        } else if(FLAG == 2){
                            Intent intent = new Intent(TopupWayActivity.this, BillDetailsActivity.class);
                            TOPUP_FLAG = 2;
                            startActivity(intent);
                            finish();
                        }
                    }
                    @Override
                    public void doButtomTwo() {
                        //跳转到首页
                        Intent intent = new Intent(TopupWayActivity.this, MainActivity.class);
                        TOPUP_FLAG = 2;
                        startActivity(intent);
                        finish();
                    }
                });*/
            }
        } else if (reqcode == 1314) {
            psdDialog.dismiss();
            Toast.makeText(this, "密码校验通过", Toast.LENGTH_LONG).show();
            toPay();
            //            payMoneyByBalance(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, primaryk, 1 + "", "success", 9 + "", payMoney, 1 + "", payMoney);
            //            payFinish();
        } else if (reqcode == 1992) {
            // dialog.dismiss();
            try {
                WxpayData wxpayData = getConfiguration().readValue(result.toString(), WxpayData.class);
                WXHelper wxHelper = new WXHelper(mContext, wxpayData.getResultData().getAppid());
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
        } else if (reqcode == 1999) {
            //dialog.cancel();
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == request_pay_before) {
            if (PAY_TYPE == PayTypeEnum.Pingtai.getCode()) {
                //平台余额
                // Toast.makeText(this,"购买成功",Toast.LENGTH_LONG).show();
                // finish();
                payFinish();
            } else if (PAY_TYPE == PayTypeEnum.Zhifubao.getCode()) {
                //支付宝
                try {
                    NewPayData newPayData = getConfiguration().readValue(result.toString(), NewPayData.class);
                    NewAlipay newAlipay = new NewAlipay(TopupWayActivity.this, Alipay_handler);
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
                        WXHelper wxHelper = new WXHelper(mContext, json.getString("appid"));
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

            } else if (PAY_TYPE == PayTypeEnum.Wangyin.getCode()) {        //银联 NewYinLianPayData
                try {
                    NewYinLianPayData newYinLianPayData = getConfiguration().readValue(result.toString(), NewYinLianPayData.class);
                    Log.e("ppp", "newYinLianPayData:" + newYinLianPayData.getResultData().getTn());
                    goUnionpay(newYinLianPayData.getResultData().getTn());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (PAY_TYPE == PayTypeEnum.Jianhang.getCode()) {      //建行支付
                Log.e("yang", result.toString());
                try {
                    NewPayData newPayData = ObjectMapperFactory.createObjectMapper().readValue(result.toString(), NewPayData.class);
                    String url = newPayData.getResultData().getContent();
                    //                    // PackageManager pm = getPackageManager();
                    //                    // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    //                    // intent.setPackage("com.chinamworld.main");
                    //                    Log.e("yang", Uri.parse(url) + "");
                    //                    Intent intent = new Intent(TopupWayActivity.this, LifeServireH5Activity.class);
                    //                    intent.putExtra("life_url", url);
                    //                    intent.putExtra("isLogin", "N");
                    //                    intent.putExtra("serviceName", "建行支付");
                    //                    startActivity(intent);

                    PackageManager pm = getPackageManager();
                    Intent checkIntent = pm.getLaunchIntentForPackage("com.chinamworld.main");
                    if (checkIntent != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, LifeServireH5Activity.class);
                        intent.putExtra("life_url", url);
                        intent.putExtra("isLogin", "N");
                        intent.putExtra("serviceName", "建行支付");
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                                    Intent intent = new Intent(TopupWayActivity.this, ForgetPayPsdActivity.class);
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
        } else if (reqcode == 1992) {
            // dialog.cancel();
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 1999) {
            // dialog.cancel();
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == request_pay_before) {
            // dialog.cancel();
            Wethod.ToFailMsg(this, result);
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
                    Wethod.httpPost(TopupWayActivity.this, 1314, Config.web.checkout_pay_psd, payPsdParams, TopupWayActivity.this);
                }
                setKey();
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
        if (TopupWayActivity.WXPAY_FLAG == 2) {
            // updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, primaryk, 1, "Success", 6, payMoney, 1, payMoney);
            payFinish();
            Log.e("liyunte", "TopupWayActivity---" + "pkmuser" + pkmuser + "primaryk" + primaryk + "payMoney" + payMoney);
            TopupWayActivity.WXPAY_FLAG = 1;
        } else if (TopupWayActivity.WXPAY_FLAG == 3 && topup_flag == 2 && PAY_TYPE == 6) {
            chargeUpdateCancle(primaryk);
            topup_flag = 1;
        }
        /*if(TOPUP_FLAG == 2){
            finish();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

}
