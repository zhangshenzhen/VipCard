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
import com.bjypt.vipcard.model.ZbarBalanceBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.MD5;
import com.bjypt.vipcard.view.CustomProgressDialog;
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
 * Date by 2016/6/15
 * Use by 扫码成功后的支付页面
 */
public class ZbarPayActivity extends BaseActivity implements VolleyCallBack{

    private RelativeLayout mBack,mMerchantPay,mSystemPay,mOnlinePay,mWxPay,mAliPay,mUnPay;
    private TextView mPayTotalMoney;
    private TextView mMerchantBalance,mSystemBalance,mTrue;
    private ImageView mMerchantChoose,mSystemChoose,mOnlineChoose,mWxChoose,mAliChoose,mUnChoose;
    private LinearLayout mOnLineGoneLinear;
    private int PAY_TYPE = 0;//支付方式 1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付 9 平台余额支付


    private int ONLINE_FLAG = 1;//判断当前是否显示微信支付，支付宝支付，网银支付
    private String tn;

    private CustomProgressDialog dialog;
    private String scanResult;
    private String [] saomao = new String[6];
    private PsdDialog psdDialog;

    private Boolean PSD_TYPE = false;
    private Context mContext;
    private int UN_FLAG = 1;
    private String key = "";
    private EditText et;
    public static int WXPAY_FLAG = 1;

    private ZbarBalanceBean zbarBalanceBean;
    private int zbar_flag = 1;
    private String pksystem;//系统主键
    private String merchantBalance;//商家余额
    private String systemBalance;//平台余额
//    private LoadingFragDialog mFragDialog;

    private Handler topupHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what){
                case 1:
                    if(zbarBalanceBean.getResultData().getBalance_mer()==null||zbarBalanceBean.getResultData().getBalance_mer().equals("")){
                        mMerchantBalance.setText("0"+"元");
                    }else{
                        mMerchantBalance.setText(zbarBalanceBean.getResultData().getBalance_mer()+"元");
                    }

                    if(zbarBalanceBean.getResultData().getBalance_sys()==null||zbarBalanceBean.getResultData().getBalance_sys().equals("")){
                        mSystemBalance.setText("0"+"元");
                    }else{
                        mSystemBalance.setText(zbarBalanceBean.getResultData().getBalance_sys()+"元");
                    }
                    break;
            }
        }
    };



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
                    Toast.makeText(ZbarPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), saomao[3], saomao[4], "ALIPAY_SUCCESS", 1, saomao[5], 3, saomao[5],"1");
//                    finish();
                    break;
                case ALIPAY_FAIL://支付宝支付失败
                    Toast.makeText(ZbarPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    break;
                case ALIPAY_TO_BE_CONFIRMED://支付宝支付待确认
                    Toast.makeText(ZbarPayActivity.this, "支付结果待确认", Toast.LENGTH_SHORT).show();
                    break;
                case ALIPAY_CANCEL://支付宝支付取消
//                    Toast.makeText(ZbarPayActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
                    chargeUpdateCancle(saomao[4]);
                    break;
            }
        }
    };


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_zbar_pay);

    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        scanResult = intent.getStringExtra("scanResult");
        saomao = scanResult.split(",");


    }

    @Override
    public void initView() {

//        mFragDialog=new LoadingFragDialog(this,R.anim.loadingpage,R.style.MyDialogStyle);
        mContext = this;
        mBack = (RelativeLayout) findViewById(R.id.layout_back);//返回键
        mPayTotalMoney = (TextView) findViewById(R.id.pay_totalmoney);//支付金额
        mPayTotalMoney.setText("¥"+saomao[5]+"元");

        mMerchantPay = (RelativeLayout) findViewById(R.id.merchant_pay_rela);//商家余额支付
        mMerchantBalance = (TextView) findViewById(R.id.merchant_balance);//商家余额
        mMerchantChoose = (ImageView) findViewById(R.id.merchant_pay_chose);//商家余额支付时后面的选择圈

        mSystemPay = (RelativeLayout) findViewById(R.id.system_pay_rela);//平台余额支付
        mSystemBalance = (TextView) findViewById(R.id.system_balance);//平台余额
        mSystemChoose = (ImageView) findViewById(R.id.system_pay_chose);//平台余额支付时后面的选择圈

        mOnLineGoneLinear = (LinearLayout) findViewById(R.id.online_choose_gone_linear);//显示或者隐藏的在线支付布局

        mOnlinePay = (RelativeLayout) findViewById(R.id.online_pay_rela);//点击在线支付弹出下面的在线支付选项
        mOnlineChoose = (ImageView) findViewById(R.id.online_pay_choose);//在线支付弹出后是箭头向上

        mWxPay = (RelativeLayout) findViewById(R.id.wx_pay_rela);//微信支付
        mWxChoose = (ImageView) findViewById(R.id.wx_pay_chose);//微信支付时后面的选择圈

        mAliPay = (RelativeLayout) findViewById(R.id.ali_pay_rela);//支付宝支付
        mAliChoose = (ImageView) findViewById(R.id.ali_pay_choose);//支付宝支付时后面的选择圈

        mUnPay = (RelativeLayout) findViewById(R.id.un_pay_rela);//网银在线支付
        mUnChoose = (ImageView) findViewById(R.id.un_pay_choose);//网银支付时后面选择圈

        mTrue = (TextView) findViewById(R.id.tv_ensure);//确认充值
        setSelect(1);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
        mMerchantPay.setOnClickListener(this);
        mSystemPay.setOnClickListener(this);
        mOnlinePay.setOnClickListener(this);
        mWxPay.setOnClickListener(this);
        mAliPay.setOnClickListener(this);
        mUnPay.setOnClickListener(this);
        mTrue.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_back:
                finish();
                break;
            case R.id.merchant_pay_rela:
                //商家余额支付
                setSelect(1);
                break;
            case R.id.system_pay_rela:
                //平台余额支付
                setSelect(2);
                break;
            case R.id.online_pay_rela:
                //在线支付
                if(ONLINE_FLAG == 1){
                    mOnLineGoneLinear.setVisibility(View.VISIBLE);
                    mOnlineChoose.setImageResource(R.mipmap.pull_to_up);
                    ONLINE_FLAG = 2;
                }else{
                    mOnLineGoneLinear.setVisibility(View.GONE);
                    mOnlineChoose.setImageResource(R.mipmap.pull_to_down);
                    ONLINE_FLAG = 1;
                }
                break;
            case R.id.wx_pay_rela:
                //微信支付
                setSelect(3);
                break;
            case R.id.ali_pay_rela:
                //支付宝支付
                setSelect(4);
                break;
            case R.id.un_pay_rela:
                //网银支付
                setSelect(5);
                break;
            case R.id.tv_ensure:
                //确认充值
                /****************************只有使用第三方支付的时候才会去QueryTn*********************************/

                if(PAY_TYPE == 9){
                    //平台余额支付
                    //判断支付金额是否是否小于平台余额
                    if(Double.parseDouble(saomao[5])>Double.parseDouble(systemBalance)){
                        //弹出选择框，去充值还是取消平台余额支付
                        new AlertDialog.Builder(this).setTitle("充值确认").setMessage("是否去充值？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        startRechage(2,pksystem);
                                    }})
                                .setNegativeButton("取消",null)
                                .show();
                        Toast.makeText(this,"余额不足，请充值",Toast.LENGTH_LONG).show();
                    }else{
                        psdDialog = new PsdDialog(this, "支付", "", "¥" + saomao[5]);
                        psdDialog.show();
                    }
                }else if(PAY_TYPE == 5){
                    //商家余额支付
                    //判断支付金额是否小于商家余额
                    Log.e("yyuu","saomao[5]:"+saomao[5]+"----merchantBalance:"+zbarBalanceBean.getResultData().getBalance_mer());
                    if(Double.parseDouble(saomao[5])>Double.parseDouble(zbarBalanceBean.getResultData().getBalance_mer())){
                        //弹出选择框，去充值还是取消商家余额支付
                        new AlertDialog.Builder(this).setTitle("充值确认").setMessage("是否去充值？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        startRechage(1,saomao[3]);
                                    }})
                                .setNegativeButton("取消",null)
                                .show();
                        Toast.makeText(this,"余额不足，请充值",Toast.LENGTH_LONG).show();
                    }else{
                        psdDialog = new PsdDialog(this, "支付", "", "¥" + saomao[5]);
                        psdDialog.show();
                    }

                }else{
                    dialog = new CustomProgressDialog(this, "正在加载中", R.anim.frame);
                    zbar_flag = 2;
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable());

                    Map<String, String> tnMap = new HashMap<>();
                    tnMap.put("userId", AES.encrypt(getFromSharePreference(Config.userConfig.pkregister), AES.key));
                    //根据选择的CheckBox的选中状态判断其支付类型//支付类型  1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付
                    Log.e("TYZ", "PAY_TYPE:" + PAY_TYPE);
                    tnMap.put("rechargeCode", AES.encrypt(PAY_TYPE + "", AES.key));
                    tnMap.put("queryType", AES.encrypt("3", AES.key));//// 1 前台充值 2 后台充值 3 前台消费     4定金支付
                    tnMap.put("phoneno", AES.encrypt(getFromSharePreference(Config.userConfig.phoneno), AES.key));//用户手机号
                    tnMap.put("timeStamp", AES.encrypt(System.currentTimeMillis() + "", AES.key));

                    tnMap.put("balance", AES.encrypt(saomao[5], AES.key));
                    tnMap.put("primaryk", AES.encrypt(saomao[4], AES.key));

                    if(PAY_TYPE == 6){
                        //微信支付
                        Map<String,String> wxMap = new HashMap<>();
                        wxMap.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
                        wxMap.put("muname","惠缘包");
                        wxMap.put("payment","6");
                        wxMap.put("paysource","3");
                        wxMap.put("amount",AES.encrypt(saomao[5],AES.key));
                        wxMap.put("primaryid",saomao[4]);
                        Wethod.httpPost(ZbarPayActivity.this,1992,Config.web.wx_pay,wxMap,this);
                    }else{
                        getTnHttpPost(Config.web.URL_pay + "pay/yinlian/queryTn", tnMap);
                    }

                }
                break;
        }
    }

    /**
     * 跳转到余额不足充值页面
     */
    public void startRechage(int flag,String pk){
        Intent intent = new Intent(this,NewTopupWayActivity.class);
        intent.putExtra("FLAG",flag);
        intent.putExtra("pkmuser",pk);
        startActivity(intent);
    }

    /**
     * 获取微信订单号
     *//*
    private void getPrepayId() {

        IWXAPI api = WXAPIFactory.createWXAPI(getApplicationContext(), null);
        if (!api.isWXAppInstalled()) {
            Message msg = handler.obtainMessage(0);
            handler.sendMessage(msg);
        } else if (!api.isWXAppSupportAPI()) {
            Message msg = handler.obtainMessage(1);
            handler.sendMessage(msg);
        } else {
            Map<String, Object> params = BeanHelper.getReq(this);
            params.put("body", mBundle.getString("eventName"));
            params.put("total_fee", mTotal);
            params.put("goodsType", 1);
            JieneiApplication.volleyHelper.httpPost(QUERY_PREPAYID, Constant.web.getPrepayId_wx, params,
                    RespBase.class, this, false);
        }
    }*/


    /**
     * 更新后台改变的数据
     *
     * @param pkregister
     * @param phoneNo
     * @param pkmuser
     * @param pkpayid
     * @param payResult
     * @param payment
     * @param amount
     * @param payEntrance
     */
    private void updatePayResult(String pkregister, String phoneNo, String pkmuser, String pkpayid , String payResult
            , int payment, String amount, int payEntrance, String waitMoney,String consume_source) {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", pkregister);
        params.put("phoneNo", phoneNo);
        params.put("pkmuser", pkmuser);
        params.put("pkpayid", pkpayid);
        params.put("payResult", payResult);
        params.put("payment", payment + "");
        params.put("amount", AES.encrypt(amount, AES.key));
        params.put("payEntrance", payEntrance + "");
        params.put("waitMoney", AES.encrypt(waitMoney, AES.key));
        params.put("consume_source",consume_source);
        Wethod.httpPost(ZbarPayActivity.this,2, Config.web.update_payresult, params, this);
//        mFragDialog.showDialog();
    }

    /**
     * 就是QueryTn中的两个值来检测到第三方内部取消时调用该接口
     * @param primaryk
     */
    private void chargeUpdateCancle(String primaryk){
        Map<String,String> cancleMap = new HashMap<>();
        cancleMap.put("primaryk",primaryk);
        cancleMap.put("queryType","3");
        Wethod.httpPost(ZbarPayActivity.this,1999, Config.web.update_cancle_result, cancleMap, this);
//        mFragDialog.showDialog();
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
                            Alipay alipay = new Alipay(ZbarPayActivity.this, Alipay_handler);
                            alipay.setProduct("支付", "支付", saomao[5], tn, saomao[4], "3");
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
                                    .installUPPayPlugin(ZbarPayActivity.this);
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
                updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), saomao[3], saomao[4], "Success", 4, saomao[5], 3, saomao[5],"1");
            } else if (str.equalsIgnoreCase("fail")) {
                /***********支付失败************/
                updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), saomao[3], saomao[4],  "fail", 4, saomao[5], 3, saomao[5],"1");
            } else if (str.equalsIgnoreCase("cancel")) {
                /***********取消支付************/
                chargeUpdateCancle(saomao[4]);
//                updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), saomao[3], saomao[4], "cancel", 4, saomao[5], 3, saomao[5],"1");
            }
        }else{
            UN_FLAG = 1;
        }



    }




    private void setSelect(int type){
        if(type == 1){
            PAY_TYPE = 5;
            mMerchantChoose.setImageResource(R.mipmap.select_true);
            mSystemChoose.setImageResource(R.mipmap.select_false);
            mWxChoose.setImageResource(R.mipmap.select_false);
            mAliChoose.setImageResource(R.mipmap.select_false);
            mUnChoose.setImageResource(R.mipmap.select_false);
        }else if(type == 2){
            PAY_TYPE = 9;
            mMerchantChoose.setImageResource(R.mipmap.select_false);
            mSystemChoose.setImageResource(R.mipmap.select_true);
            mWxChoose.setImageResource(R.mipmap.select_false);
            mAliChoose.setImageResource(R.mipmap.select_false);
            mUnChoose.setImageResource(R.mipmap.select_false);
        }else if(type == 3){
            PAY_TYPE = 6;
            mMerchantChoose.setImageResource(R.mipmap.select_false);
            mSystemChoose.setImageResource(R.mipmap.select_false);
            mWxChoose.setImageResource(R.mipmap.select_true);
            mAliChoose.setImageResource(R.mipmap.select_false);
            mUnChoose.setImageResource(R.mipmap.select_false);
        }else if(type == 4){
            PAY_TYPE =1 ;
            mMerchantChoose.setImageResource(R.mipmap.select_false);
            mSystemChoose.setImageResource(R.mipmap.select_false);
            mWxChoose.setImageResource(R.mipmap.select_false);
            mAliChoose.setImageResource(R.mipmap.select_true);
            mUnChoose.setImageResource(R.mipmap.select_false);
        }else if(type == 5){
            PAY_TYPE = 4;
            mMerchantChoose.setImageResource(R.mipmap.select_false);
            mSystemChoose.setImageResource(R.mipmap.select_false);
            mWxChoose.setImageResource(R.mipmap.select_false);
            mAliChoose.setImageResource(R.mipmap.select_false);
            mUnChoose.setImageResource(R.mipmap.select_true);
        }
    }







    @Override
    public void onSuccess(int reqcode, Object result) {
        if(reqcode == 2){
            //支付成功
            dialog.dismiss();
            startActivity(new Intent(this, BillDetailsActivity.class));
            finish();
        }else if(reqcode == 1314){
            psdDialog.dismiss();
            dialog = new CustomProgressDialog(this, "正在加载中", R.anim.frame);

            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
            Toast.makeText(this, "密码校验通过", Toast.LENGTH_LONG).show();
            if(PAY_TYPE == 5){
                updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), saomao[3], saomao[4], "success", 5 , saomao[5], 3, saomao[5],"1");
            }else if(PAY_TYPE == 9){
                updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), saomao[3], saomao[4], "success", 9 , saomao[5], 3, saomao[5],"1");
            }

        }else if(reqcode == 1111){
            try {
                zbarBalanceBean = getConfiguration().readValue(result.toString(), ZbarBalanceBean.class);
                pksystem = zbarBalanceBean.getResultData().getPksystem();
                String merchantBalance = zbarBalanceBean.getResultData().getBalance_mer();
                systemBalance = zbarBalanceBean.getResultData().getBalance_sys();
                topupHandler.sendEmptyMessage(1);


            } catch (IOException e) {
                e.printStackTrace();
            }
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
        }else if(reqcode == 1999){
            Wethod.ToFailMsg(this,result);
        }
//        mFragDialog.cancelDialog();
    }

    @Override
    public void onFailed(int reqcode, Object result) {
//        mFragDialog.cancelDialog();
        if(reqcode == 2){
            if (dialog!=null&&dialog.isShowing()){
                dialog.dismiss();
            }

            Wethod.ToFailMsg(this,result);
        }else if(reqcode == 1314){
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
                                    Intent intent = new Intent(ZbarPayActivity.this,ForgetPayPsdActivity.class);
                                    startActivity(intent);
                                }
                            });
                    builder.create().show();

                }else {
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
                    Wethod.httpPost(ZbarPayActivity.this,1314, Config.web.checkout_pay_psd, payPsdParams, ZbarPayActivity.this);
//                    mFragDialog.showDialog();
                }
                setKey();
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
        Map<String,String> params = new HashMap<>();
        params.put("pkmuser",saomao[3]);
        params.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(ZbarPayActivity.this,1111, Config.web.get_system_merchant_balance, params, this);
//        mFragDialog.showDialog();
        if(ZbarPayActivity.WXPAY_FLAG == 2&&FirstActivity.WXPAY_XX == 1){

            updatePayResult(getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), saomao[3], saomao[4], "Success", 6, saomao[5], 3, saomao[5],"1");
            Log.e("liyunte","ZbarPayAcitivy---"+"saomao[3]"+saomao[3]+"saomao[4]"+saomao[4]+"saomao[5]"+saomao[5]);
            ZbarPayActivity.WXPAY_FLAG = 1;
        }else if(ZbarPayActivity.WXPAY_FLAG == 3&&zbar_flag == 2&&PAY_TYPE == 6){
            chargeUpdateCancle(saomao[4]);
            zbar_flag = 1;
        }
        FirstActivity.WXPAY_XX = 1;
    }
}
