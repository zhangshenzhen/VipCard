package com.bjypt.vipcard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.MyApplication;
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
import com.bjypt.vipcard.model.RightAwayOonLineData;
import com.bjypt.vipcard.model.WxpayData;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.VirtualmoneySuccessHelper;
import com.bjypt.vipcard.wxapi.WXHelper;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.unionpay.UPPayAssistEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/19 0019.
 * 立即买单在线支付
 */

public class RightAwayOnLineActivity extends BaseActivity implements VolleyCallBack<String> {

    private static final int request_pay_before = 2222;

    private LinearLayout layout_bank_pay;//银行卡支付

    private LinearLayout layout_zhifubao_pay;//支付宝支付
    private LinearLayout layout_weixin_pay;//微信支付

    private ImageView iv_bank_pay;//银行卡radiobutton
    private ImageView iv_zhifubao_pay;//支付宝radiobutton
    private ImageView iv_weixin_pay;//微信 radiobutton

    private Button queren_zhifu;
    private String mname;//商家名称
    private String waitMoney;//待支付金额
    private String pkmuser;//商家主键
    private String pk_merchant;
    private String redPacket;//红包
    private String pkWeal;
    private String queryPkid;

    private int PAY_TYPE = 0;
//    private CustomProgressDialog dialog;

    private String tn;
    private Context mContext;
    private int UN_FLAG = 1;
    private LinearLayout layout_back;
    public static int WXPAY_FLAG = 1;
    private int zbar_flag = 1;
    private TextView tv_merchant_caiming, tv_merchant_price;
    private String virtualMoney;
    private String amount;

    private String orderId;


    private Handler Alipay_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ALIPAY_SUCCESS://支付宝支付成功
//                    Toast.makeText(PayOnLineActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                    updatePayResult(getFromSharePreference(Config.userConfig.pkregister),getFromSharePreference(Config.userConfig.phoneno),pkmuser,queryPkid,"ALIPAY_SUCCESS",1,amount,3,waitMoney,"2");
                    payFinish();
                    break;
                case ALIPAY_FAIL://支付宝支付失败
                    payFinish();
                    Toast.makeText(RightAwayOnLineActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
//                    updatePayResult(getFromSharePreference(Config.userConfig.pkregister),getFromSharePreference(Config.userConfig.phoneno),pkmuser,queryPkid,"ALIPAY_FAIL",1,amount,3,waitMoney,"2");
                    break;
                case ALIPAY_TO_BE_CONFIRMED://支付宝支付待确认
                    payFinish();
//                    updatePayResult(getFromSharePreference(Config.userConfig.pkregister),getFromSharePreference(Config.userConfig.phoneno),pkmuser,queryPkid,"ALIPAY_TO_BE_CONFIRMED",1,amount,3,waitMoney,"2");
                    break;
                case ALIPAY_CANCEL://支付宝支付取消
//                    Toast.makeText(PayOnLineActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
                    updadteCancleResult(queryPkid, "3");
                    break;
            }
        }
    };
    private String non_discount_amount_aes;//不可优惠金额

    private void updadteCancleResult(String primaryk, String querytype) {

//        if(dialog!=null){
//            dialog.dismiss();
//        }

        Map<String, String> cancleMap = new HashMap<>();
        cancleMap.put("primaryk", primaryk);//以前就是primark思密达
        cancleMap.put("queryType", querytype);
        Wethod.httpPost(RightAwayOnLineActivity.this, 1918, Config.web.base + "post/thirdpay/app/userOrderCancelBack", cancleMap, this);
    }


//    private void updatePayResult(String pkregister, String phoneNo, String pkmuser, String pkpayid , String payResult
//            , int payment, String amount, int payEntrance, String waitMoney,String consume_source) {
//        Map<String, String> params = new HashMap<>();
//        params.put("pkregister", pkregister);
//        params.put("phoneNo", phoneNo);
//        params.put("pkmuser", pkmuser);
//        params.put("pkpayid", pkpayid);
//        params.put("payResult", payResult);
//        params.put("payment", payment + "");
//        params.put("amount", AES.encrypt(amount, AES.key));
//        params.put("payEntrance", payEntrance + "");
//        params.put("waitMoney", AES.encrypt(waitMoney, AES.key));
//        params.put("consume_source",consume_source);
//        Wethod.httpPost(RightAwayOnLineActivity.this,666, Config.web.update_payresult, params, this);
//    }


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_rightaway_online);
    }

    @Override
    public void beforeInitView() {
        mContext = this;
        Intent intent = getIntent();
        mname = intent.getStringExtra("mname");
        waitMoney = intent.getStringExtra("waitMoney");
        pkmuser = intent.getStringExtra("pkmuser");
        pk_merchant = intent.getStringExtra("pk_merchant");
        redPacket = intent.getStringExtra("redPacket");
        pkWeal = intent.getStringExtra("pkWeal");
        virtualMoney = intent.getStringExtra("virtualMoney");
        amount = intent.getStringExtra("amount");
        non_discount_amount_aes = intent.getStringExtra("non_discount_amount_aes");

    }

    @Override
    public void initView() {
        layout_bank_pay = (LinearLayout) findViewById(R.id.layout_bank_pay);
        layout_zhifubao_pay = (LinearLayout) findViewById(R.id.layout_zhifubao_pay);
        layout_weixin_pay = (LinearLayout) findViewById(R.id.layout_weixin_pay);

        iv_bank_pay = (ImageView) findViewById(R.id.iv_bank_pay);
        iv_zhifubao_pay = (ImageView) findViewById(R.id.iv_zhifubao_pay);
        iv_weixin_pay = (ImageView) findViewById(R.id.iv_weixin_pay);

        queren_zhifu = (Button) findViewById(R.id.queren_zhifu);

        layout_back = (LinearLayout) findViewById(R.id.layout_back);

        tv_merchant_caiming = (TextView) findViewById(R.id.tv_merchant_caiming);
        tv_merchant_price = (TextView) findViewById(R.id.tv_merchant_price);
        tv_merchant_caiming.setText(mname);
        tv_merchant_price.setText(waitMoney + "元");
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        layout_bank_pay.setOnClickListener(this);
        layout_zhifubao_pay.setOnClickListener(this);
        layout_weixin_pay.setOnClickListener(this);
        queren_zhifu.setOnClickListener(this);
        layout_back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_bank_pay:
                PAY_TYPE = 4;
                iv_bank_pay.setImageResource(R.mipmap.select_true);
                iv_zhifubao_pay.setImageResource(R.mipmap.select_false);
                iv_weixin_pay.setImageResource(R.mipmap.select_false);
                break;
            /**
             *
             */
            case R.id.layout_zhifubao_pay:
                PAY_TYPE = 1;
                iv_bank_pay.setImageResource(R.mipmap.select_false);
                iv_zhifubao_pay.setImageResource(R.mipmap.select_true);
                iv_weixin_pay.setImageResource(R.mipmap.select_false);
                break;
            /**
             *
             */
            case R.id.layout_weixin_pay:
                PAY_TYPE = 6;
                iv_bank_pay.setImageResource(R.mipmap.select_false);
                iv_zhifubao_pay.setImageResource(R.mipmap.select_false);
                iv_weixin_pay.setImageResource(R.mipmap.select_true);
                break;
            case R.id.queren_zhifu:
                //TODO 确认支付
//                dialog =new CustomProgressDialog(this, "正在加载中",R.anim.frame);
                zbar_flag = 2;
//                dialog.show();
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                Map<String, String> orderAddParams = new HashMap<>();
                orderAddParams.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                orderAddParams.put("pkmuser", pkmuser);
                orderAddParams.put("waitMoney", AES.encrypt(waitMoney, AES.key));
                orderAddParams.put("payEntrance", "5");
                orderAddParams.put("non_discount_amount_aes", AES.encrypt(non_discount_amount_aes, AES.key));
//                orderAddParams.put("redPacket", AES.encrypt(redPacket, AES.key));
                Log.e("pkWeal", "pkWeal:" + pkWeal);
                orderAddParams.put("pkWeal", AES.encrypt(pkWeal, AES.key));
                Log.e("pkWeal", "pkWeal_AES:" + AES.encrypt(pkWeal, AES.key));
                orderAddParams.put("amount", AES.encrypt(amount, AES.key));
                if (PAY_TYPE == 1) {
                    //支付宝
                    orderAddParams.put("payment", "1");
                } else if (PAY_TYPE == 4) {
                    //银联
                    orderAddParams.put("payment", "4");
                } else if (PAY_TYPE == 6) {
                    orderAddParams.put("payment", "6");
                }
                orderAddParams.put("virtualMoney", AES.encrypt(virtualMoney, AES.key));

                Wethod.httpPost(RightAwayOnLineActivity.this, 12345, Config.web.create_new_order, orderAddParams, this);

                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 12345) {
            try {
                RightAwayOonLineData addOrderBean = getConfiguration().readValue(result.toString(), RightAwayOonLineData.class);
                queryPkid = addOrderBean.getResultData().getPkpayid();
//                orderId = addOrderBean.getResultData().getOutorderid();
//                Map<String,String> tnMap = new HashMap<>();
//                tnMap.put("userId", AES.encrypt(getFromSharePreference(Config.userConfig.pkregister), AES.key));
//
//                //根据选择的CheckBox的选中状态判断其支付类型//支付类型  1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付
//                if(PAY_TYPE == 0){
//                    Toast.makeText(this,"请选择支付方式",Toast.LENGTH_LONG).show();
//                    return;
//                }else if(PAY_TYPE == 1){
//                    tnMap.put("rechargeCode",AES.encrypt(PAY_TYPE+"",AES.key));
//                }else if(PAY_TYPE == 4){
//                    tnMap.put("rechargeCode",AES.encrypt(PAY_TYPE+"",AES.key));
//                }
//                Log.e("onLine","onLine:"+PAY_TYPE);
//                tnMap.put("queryType",AES.encrypt("3",AES.key));//// 1 前台充值 2 后台充值 3 前台消费     4定金支付
//                tnMap.put("primaryk", AES.encrypt(queryPkid, AES.key));
//                tnMap.put("phoneno",AES.encrypt(getFromSharePreference(Config.userConfig.phoneno),AES.key));//用户手机号
//                tnMap.put("timeStamp", AES.encrypt(System.currentTimeMillis() + "",AES.key));
//                tnMap.put("balance", AES.encrypt(Double.parseDouble(waitMoney) + "", AES.key));
////                tnMap.put("virtualMoney", AES.encrypt(Double.parseDouble(virtualMoney) + "", AES.key));
//                if(PAY_TYPE == 6){
//                    Map<String,String> wxMap = new HashMap<>();
//                    wxMap.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
//                    wxMap.put("muname",mname);
//                    wxMap.put("payment","6");
//                    wxMap.put("paysource","3");
//                    wxMap.put("amount",AES.encrypt(Double.parseDouble(waitMoney)+"",AES.key));
//                    wxMap.put("primaryid",queryPkid);
////                    wxMap.put("redPacket",AES.encrypt(Double.parseDouble(redPacket)+"",AES.key));
////                    wxMap.put("pkWeal",AES.encrypt(pkWeal,AES.key));
////                    wxMap.put("couponPay",AES.encrypt(Double.parseDouble(couponPay)+"",AES.key));
////                    wxMap.put("virtualMoney",AES.encrypt(Double.parseDouble(virtualMoney)+"",AES.key));
//                    Wethod.httpPost(RightAwayOnLineActivity.this,1992,Config.web.wx_pay,wxMap,this);
//                }else{
//                    getTnHttpPost(Config.web.URL_pay + "pay/yinlian/queryTn", tnMap);
//
//                }
                toPay(addOrderBean.getResultData().getOutorderid(), waitMoney);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (reqcode == 1992) {
//            dialog.dismiss();

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
                req.sign = wxpayData.getResultData().getSign();
                req.extData = "success";

                wxHelper.pay(req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (reqcode == 666) {
            payFinish();
        } else if (reqcode == 1918) {
//            dialog.cancel();
        } else if (reqcode == request_pay_before) {
            try {
                if (PAY_TYPE == 1) {
                    /**
                     * 支付宝
                     */
//                    Alipay alipay = new Alipay(RightAwayOnLineActivity.this, Alipay_handler);
//                   alipay.setProduct(mname, mname, waitMoney, tn, queryPkid, "1");
//                   alipay.startAlipay();

                    NewPayData newPayData = getConfiguration().readValue(result.toString(), NewPayData.class);
                    NewAlipay newAlipay = new NewAlipay(RightAwayOnLineActivity.this, Alipay_handler);
                    newAlipay.setProduct(newPayData.getResultData().getContent());
                    newAlipay.startAlipay();

                } else if (PAY_TYPE == PayTypeEnum.Weixin.getCode()) {
                    //微信
                    NewPayWxData newPayWxData = getConfiguration().readValue(result.toString(), NewPayWxData.class);
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
                } else if (PAY_TYPE == 4) {
                    /**
                     * 银联支付
                     */
                    Log.e("TYZ", "tn:" + tn);
//                  goUnionpay(tn);
                    NewYinLianPayData newYinLianPayData = getConfiguration().readValue(result.toString(), NewYinLianPayData.class);
                    goUnionpay(newYinLianPayData.getResultData().getTn());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void payFinish() {
        VirtualmoneySuccessHelper virtualmoneySuccessHelper = new VirtualmoneySuccessHelper(this, "支付成功");
        virtualmoneySuccessHelper.checkVirtualmoney(getFromSharePreference(Config.userConfig.pkregister), queryPkid);
        RightAwayActivity.CANCLE_ACTIVITY = 2;
    }

    private void toPay(String outorderid, String payMoney) {
        Map<String, String> param = new HashMap<>();
        param.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        param.put("pkmuser", pk_merchant);
        param.put("orderid", outorderid);//订单主键
        param.put("dealtype", PayDealTypeEnum.Lijimaidan.getCode() + "");//业务类型 1.充值 2立即买单 3优惠券  后续待定
        param.put("amount", waitMoney);//金额
        param.put("paytype", "" + PAY_TYPE);
        param.put("paySubject", "在" + mname + "消费");
        param.put("payBody", "消费");
        Wethod.httpPost(this, request_pay_before, Config.web.pay_new_before, param, this);
    }

    @Override
    public void onFailed(int reqcode, String result) {
        if (request_pay_before == reqcode) {
//            dialog.cancel();
//          Wethod.ToFailMsg(this,result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }


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

                            Alipay alipay = new Alipay(RightAwayOnLineActivity.this, Alipay_handler);

                            alipay.setProduct(mname, mname, waitMoney, tn, queryPkid, "1");

                            alipay.startAlipay();

                        } else if (PAY_TYPE == 4) {
                            /**
                             * 银联支付
                             */
                            Log.e("TYZ", "tn:" + tn);
                            goUnionpay(tn);
                        }


                    }
                } catch (JSONException e) {
                    Log.e("TYZ", "JSONException:" + e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TYZ", "volleyError:" + volleyError.toString());
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
                            UN_FLAG = 2;
                            UPPayAssistEx
                                    .installUPPayPlugin(RightAwayOnLineActivity.this);
                            dialog.dismiss();
                        }
                    });

            builder.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dialog.dismiss();
//                            RightAwayOnLineActivity.this.dialog.dismiss();
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
        if (resultCode == 22) {
            String result = data.getExtras().getString("result");
            Toast.makeText(this, "result:" + result, Toast.LENGTH_LONG).show();
        } else {
            if (UN_FLAG == 1) {
                String str = data.getExtras().getString("pay_result");
                if (str.equalsIgnoreCase("success")) {
                    /**********支付成功************/
//                        updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser,queryPkid,"Success",1,amount,3,waitMoney,"2");
                    payFinish();
                } else if (str.equalsIgnoreCase("fail")) {
                    /***********支付失败************/
                    Log.e("tyz", "fail");
                    payFinish();
//                    updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser,queryPkid,"fail",1,amount,3,waitMoney,"2");
                } else if (str.equalsIgnoreCase("cancel")) {
                    Log.e("tyz", "cancel");
                    updadteCancleResult(queryPkid, "3");
                    /***********取消支付************/
                    /*if(flag.equals("Y")){
                        updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "cancel", 4, earnest, 2,earnest,"","","","","");
                    }else{
                        updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "cancel", 4, amount, 3,earnest,redPacket,pkWeal,couponPay,point,pointMoney);
                    }*/
                }
            } else {
                UN_FLAG = 1;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (RightAwayOnLineActivity.WXPAY_FLAG == 2 && FirstActivity.WXPAY_XX == 1) {
//            updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser,queryPkid,"Success",6,amount,3,waitMoney,"2");
            payFinish();
            RightAwayOnLineActivity.WXPAY_FLAG = 1;
        } else if (RightAwayOnLineActivity.WXPAY_FLAG == 3 && zbar_flag == 2 && PAY_TYPE == 6) {
            updadteCancleResult(queryPkid, "3");
            zbar_flag = 1;
        }
        FirstActivity.WXPAY_XX = 1;
    }
}
