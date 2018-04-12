package com.bjypt.vipcard.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
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
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.Alipay;
import com.bjypt.vipcard.model.AddOrderBean;
import com.bjypt.vipcard.model.WxpayData;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.MyDialogUtil;
import com.bjypt.vipcard.view.CustomProgressDialog;
import com.bjypt.vipcard.view.MyDialog;
import com.bjypt.vipcard.wxapi.WXHelper;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.unionpay.UPPayAssistEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 在线支付
 */
 public class PayOnLineActivity extends BaseActivity implements VolleyCallBack,IWXAPIEventHandler{
private LinearLayout layout_back;//返回
     private LinearLayout layout_bank_pay;//银行卡支付

     private LinearLayout layout_zhifubao_pay;//支付宝支付
     private LinearLayout layout_weixin_pay;//微信支付
     private RelativeLayout layout_price;//金额layout
     private ImageView iv_bank_pay;//银行卡radiobutton
     private ImageView iv_price;//金额 radiobutton
     private ImageView iv_zhifubao_pay;//支付宝radiobutton
     private ImageView iv_weixin_pay;//微信 radiobutton
    private TextView tv_merchant_price;
    private TextView tv_merchant_caiming;//商品名称

     private TextView queren_zhifu;//确认支付

    private int PAY_TYPE = 0;
    private String orderId;//商品流水号  例如:20160414120040
    private String primaryk;//商品订单主键  例如:fb3d0e64263745a3a0d699ca5999bdb3
    private String pkmuser;//商家主键
    private String createtime;//创建订单时间
    private String tn;
    private String pkpayid;
    private String muname;//商家名称
    private String earnest;//支付定金的金额
    private String preorderId;
    private String dingjinId;

    public static final int PLUGIN_VALID = 0;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;

    public static int FLLA_PAY_SUCCESS = 1;

    private String flag;
    private String queryPkid;
    private String amount;


    private String redPacket;//红包金额
    private String pkWeal;//优惠卷主键
    private String couponPay;//优惠卷金额
    private String point;//积分
    private String pointMoney;//积分金额
    private String virtualMoney;//惠员币
    private CustomProgressDialog dialog;
    private  int UN_FLAG = 1;
    private String pkId;

    private Context mContext;

    public static int WXPAY_FLAG = 1;//判断在onResume方法中是否调用pay接口===1的时候不调用，2的时候调用
    private int pay_flag = 1;

    /*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
//    private final String mMode = "00";

    private Handler Alipay_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ALIPAY_SUCCESS://支付宝支付成功
//                    Toast.makeText(PayOnLineActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

                    if(flag.equals("Y")){
                        updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "ALIPAY_SUCCESS", 1, earnest, 2, earnest,"","","","","");
                    }else{
                        updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "ALIPAY_SUCCESS", 1, amount, 3, earnest,redPacket,pkWeal,couponPay,point,pointMoney);
                    }
                    /*PayOneActivity.FLAG_SKIP_PAY = 2;
                    finish();*/
                    break;
                case ALIPAY_FAIL://支付宝支付失败
                    Toast.makeText(PayOnLineActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    break;
                case ALIPAY_TO_BE_CONFIRMED://支付宝支付待确认
                    Toast.makeText(PayOnLineActivity.this, "支付结果待确认", Toast.LENGTH_SHORT).show();
                    if(flag.equals("Y")){
                        updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "ALIPAY_TO_BE_CONFIRMED", 1, earnest, 2, earnest,"","","","","");
                    }else{
                        updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "ALIPAY_TO_BE_CONFIRMED", 1, amount, 3, earnest,redPacket,pkWeal,couponPay,point,pointMoney);
                    }
                    break;
                case ALIPAY_CANCEL://支付宝支付取消
//                    Toast.makeText(PayOnLineActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
                    Log.e("tuyouze","queryPkid---:"+queryPkid);
                    if(flag.equals("Y")){
                        updadteCancleResult(dingjinId,"4");
                    }else{
                        Log.e("yytt","pkId:"+pkId);
                        updadteCancleResult(queryPkid,"3");

                    }

                    break;
            }
        }
    };




     @Override
     public void setContentLayout() {
         setContentView(R.layout.activity_pay_on_line);
     }

     @Override
     public void beforeInitView() {
         mContext = this;


         Intent intent = getIntent();
         dingjinId = intent.getStringExtra("primaryk");
         primaryk = intent.getStringExtra("preorderId");
         Log.e("yytt","primaryk:"+primaryk);
         pkpayid = primaryk;
         orderId = intent.getStringExtra("orderId");
         muname = intent.getStringExtra("muname");
         pkmuser = intent.getStringExtra("pkmuser");
         createtime = intent.getStringExtra("createtime");
         earnest = intent.getStringExtra("earnest");
         preorderId = intent.getStringExtra("preorderId");

         flag = intent.getStringExtra("flag");
         amount = intent.getStringExtra("amount");

         redPacket = intent.getStringExtra("redPacket");
         pkWeal = intent.getStringExtra("pkWeal");
         couponPay = intent.getStringExtra("couponPay");
         point = intent.getStringExtra("point");
         pointMoney = intent.getStringExtra("pointMoney");
         virtualMoney = intent.getStringExtra("virtualMoney");
         Log.e("TYZ", "redPacket:" + redPacket);
     }

     @Override
     public void initView() {
         queren_zhifu = (TextView) findViewById(R.id.queren_zhifu);
         layout_back = (LinearLayout) findViewById(R.id.layout_back);
         layout_price = (RelativeLayout) findViewById(R.id.layout_price );
         layout_bank_pay = (LinearLayout) findViewById(R.id.layout_bank_pay);
         layout_zhifubao_pay = (LinearLayout) findViewById(R.id.layout_zhifubao_pay);
         layout_weixin_pay = (LinearLayout) findViewById(R.id.layout_weixin_pay);
         iv_bank_pay = (ImageView) findViewById(R.id.iv_bank_pay);
         iv_zhifubao_pay = (ImageView) findViewById(R.id.iv_zhifubao_pay);
         iv_weixin_pay = (ImageView) findViewById(R.id.iv_weixin_pay);

         tv_merchant_price = (TextView) findViewById(R.id.tv_merchant_price);
         tv_merchant_price.setText(earnest);
         tv_merchant_caiming = (TextView) findViewById(R.id.tv_merchant_caiming);
         tv_merchant_caiming.setText(muname);
     }

     @Override
     public void afterInitView() {

     }

     @Override
     public void bindListener() {
         queren_zhifu.setOnClickListener(this);
         layout_back.setOnClickListener(this);
         layout_price.setOnClickListener(this);
         layout_bank_pay.setOnClickListener(this);
         layout_zhifubao_pay.setOnClickListener(this);
         layout_weixin_pay.setOnClickListener(this);

     }

     @Override
     public void onClickEvent(View v) {
         switch (v.getId()){
             /**
              *
              */
             case R.id.queren_zhifu:
                 dialog =new CustomProgressDialog(this, "正在加载中",R.anim.frame);
                 pay_flag = 2;
                 pkId = primaryk;
                 dialog.show();
                 dialog.getWindow().setBackgroundDrawable(new ColorDrawable());

                 if(flag.equals("Y")){
                     //flag == Y 付定金  flag==N 前台消费
                     Map<String,String> tnMap = new HashMap<>();

                     /*tnMap.put("userId", AES.encrypt(getFromSharePreference(Config.userConfig.pkregister), AES.key));
                     //根据选择的CheckBox的选中状态判断其支付类型//支付类型  1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付
                     if(PAY_TYPE == 0){
                         Toast.makeText(this,"请选择支付方式",Toast.LENGTH_LONG).show();
                         return;
                     }else if(PAY_TYPE == 1){
                         tnMap.put("rechargeCode",AES.encrypt(PAY_TYPE+"",AES.key));
                     }else if(PAY_TYPE == 4){
                         tnMap.put("rechargeCode",AES.encrypt(PAY_TYPE+"",AES.key));
                     }

                     tnMap.put("queryType",AES.encrypt("4",AES.key));//// 1 前台充值 2 后台充值 3 前台消费     4定金支付
                     tnMap.put("primaryk", AES.encrypt(primaryk, AES.key));
                     tnMap.put("phoneno",AES.encrypt(getFromSharePreference(Config.userConfig.phoneno),AES.key));//用户手机号
                     tnMap.put("timeStamp", AES.encrypt(System.currentTimeMillis() + "",AES.key));
                     tnMap.put("balance", AES.encrypt(Double.parseDouble(earnest)+"",AES.key));*/


                     tnMap.put("userId", AES.encrypt(getFromSharePreference(Config.userConfig.pkregister), AES.key));
                     //根据选择的CheckBox的选中状态判断其支付类型//支付类型  1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付
                     if(PAY_TYPE == 0){
                         Toast.makeText(this,"请选择支付方式",Toast.LENGTH_LONG).show();
                         return;
                     }else if(PAY_TYPE == 1){
                         tnMap.put("rechargeCode",AES.encrypt(PAY_TYPE+"",AES.key));
                     }else if(PAY_TYPE == 4){
                         tnMap.put("rechargeCode",AES.encrypt(PAY_TYPE+"",AES.key));
                     }

                     if(PAY_TYPE == 6){

                         //此时为微信支付，不需要再去调用quertTn

//                         pkregister： 用户Id
//                         pkmuser ： 商户Id，如果往平台充值则为系统Id
//                         payment：支付类型: 1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付 8 其他支付 9 平台支付
//                         paysource ：支付来源: 1 前台充值3前台消费 4 定金支付
//                         amount : 充值金额(元,最多2位小数)
//                         primaryid: 流水主键

                         Map<String,String> wxMap = new HashMap<>();
                         wxMap.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
                         wxMap.put("muname",muname);
                         wxMap.put("payment","6");
                         wxMap.put("paysource","4");
                         wxMap.put("amount",AES.encrypt(Double.parseDouble(earnest) + "",AES.key));
                         wxMap.put("primaryid",dingjinId);
                         Wethod.httpPost(PayOnLineActivity.this,1992,Config.web.wx_pay,wxMap,this);
                     }else{
                         tnMap.put("queryType",AES.encrypt("4",AES.key));//// 1 前台充值 2 后台充值 3 前台消费     4定金支付
                         tnMap.put("primaryk", AES.encrypt(dingjinId, AES.key));
                         tnMap.put("phoneno",AES.encrypt(getFromSharePreference(Config.userConfig.phoneno),AES.key));//用户手机号
                         tnMap.put("timeStamp", AES.encrypt(System.currentTimeMillis() + "",AES.key));
                         tnMap.put("balance", AES.encrypt(Double.parseDouble(earnest) + "", AES.key));
                         getTnHttpPost(Config.web.URL_pay + "pay/yinlian/queryTn", tnMap);
                     }
Log.e("liyunteee","dingjinId----------"+dingjinId);

                 }else{
                     Map<String,String> orderAddParams = new HashMap<>();
                     orderAddParams.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
                     orderAddParams.put("pkmuser",pkmuser);
                     orderAddParams.put("waitMoney",AES.encrypt(earnest,AES.key));
                     orderAddParams.put("payEntrance","3");
                     orderAddParams.put("pkpropre",preorderId);
                     orderAddParams.put("redPacket", AES.encrypt(redPacket, AES.key));
                     Log.e("TYZ", "redPacket:" + redPacket);
                     orderAddParams.put("pkWeal",pkWeal);
                     orderAddParams.put("point",point);
                     orderAddParams.put("virtualMoney",AES.encrypt(virtualMoney,AES.key));
                     if(PAY_TYPE == 1){
                         //支付宝
                         orderAddParams.put("payment","1");
                     }else if(PAY_TYPE == 4){
                         //银联
                         orderAddParams.put("payment","4");
                     }else if(PAY_TYPE == 6){
                         orderAddParams.put("payment","6");
                     }


                     Wethod.httpPost(PayOnLineActivity.this,12345, Config.web.create_new_order, orderAddParams, this);
                }





                 break;
             /**
              *
              */
             case R.id.layout_back:
                 finish();
                 break;
           case R.id.layout_price:
//                 finish();

                 break;
             /**
              *
              */
             case R.id.layout_bank_pay:
                 PAY_TYPE = 4;
                 iv_bank_pay.setBackgroundResource(R.mipmap.selected_true);
                 iv_zhifubao_pay.setBackgroundResource(R.mipmap.selected_false);
                 iv_weixin_pay.setBackgroundResource(R.mipmap.selected_false);
                 break;
             /**
              *
              */
             case R.id.layout_zhifubao_pay:
                 PAY_TYPE = 1;
                 iv_bank_pay.setBackgroundResource(R.mipmap.selected_false);
                 iv_zhifubao_pay.setBackgroundResource(R.mipmap.selected_true);
                 iv_weixin_pay.setBackgroundResource(R.mipmap.selected_false);
                 break;
             /**
              *
              */
             case R.id.layout_weixin_pay:
                 PAY_TYPE = 6;
                 iv_bank_pay.setBackgroundResource(R.mipmap.selected_false);
                 iv_zhifubao_pay.setBackgroundResource(R.mipmap.selected_false);
                 iv_weixin_pay.setBackgroundResource(R.mipmap.selected_true);
                 break;




         }

     }

    @Override
    public void onSuccess(int reqcode, final Object result) {

        if(reqcode == 1){


        }else if(reqcode == 12345){
            try {
                AddOrderBean addOrderBean = getConfiguration().readValue(result.toString(), AddOrderBean.class);
                queryPkid = addOrderBean.getResultData().getPkpayid();
                Map<String,String> tnMap = new HashMap<>();
                tnMap.put("userId", AES.encrypt(getFromSharePreference(Config.userConfig.pkregister), AES.key));
                if(redPacket.equals("")||redPacket==null){
                    tnMap.put("redPacket",AES.encrypt("",AES.key));
                }else{
                    tnMap.put("redPacket",AES.encrypt(Double.parseDouble(redPacket)+"",AES.key));
                }
                if(pkWeal.equals("")||pkWeal==null){
                    tnMap.put("pkWeal",AES.encrypt("",AES.key));
                }else{
                    tnMap.put("pkWeal",AES.encrypt(Double.parseDouble(pkWeal)+"",AES.key));
                }
                if(couponPay.equals("")||couponPay==null){
                    tnMap.put("couponPay",AES.encrypt("",AES.key));
                }else{
                    tnMap.put("couponPay",AES.encrypt(Double.parseDouble(couponPay)+"",AES.key));
                }
                if(virtualMoney.equals("")||virtualMoney==null){
                    tnMap.put("virtualMoney",AES.encrypt("",AES.key));
                }else{
                    tnMap.put("virtualMoney",AES.encrypt(Double.parseDouble(virtualMoney)+"",AES.key));
                }

                //根据选择的CheckBox的选中状态判断其支付类型//支付类型  1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付
                if(PAY_TYPE == 0){
                    Toast.makeText(this,"请选择支付方式",Toast.LENGTH_LONG).show();
                    return;
                }else if(PAY_TYPE == 1){
                    tnMap.put("rechargeCode",AES.encrypt(PAY_TYPE+"",AES.key));
                }else if(PAY_TYPE == 4){
                    tnMap.put("rechargeCode",AES.encrypt(PAY_TYPE+"",AES.key));
                }
                tnMap.put("queryType",AES.encrypt("3",AES.key));//// 1 前台充值 2 后台充值 3 前台消费     4定金支付
                tnMap.put("primaryk", AES.encrypt(queryPkid, AES.key));
                tnMap.put("phoneno",AES.encrypt(getFromSharePreference(Config.userConfig.phoneno),AES.key));//用户手机号
                tnMap.put("timeStamp", AES.encrypt(System.currentTimeMillis() + "",AES.key));
                tnMap.put("balance", AES.encrypt(Double.parseDouble(earnest) + "", AES.key));

                if(PAY_TYPE == 6){
                        Map<String,String> wxMap = new HashMap<>();
                        wxMap.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
                        wxMap.put("muname",muname);
                        wxMap.put("payment","6");
                        wxMap.put("paysource","3");
                        wxMap.put("amount",AES.encrypt(Double.parseDouble(earnest)+"",AES.key));
                        wxMap.put("primaryid",queryPkid);
                        wxMap.put("redPacket",AES.encrypt(Double.parseDouble(redPacket)+"",AES.key));
                        wxMap.put("pkWeal",pkWeal);
                        wxMap.put("couponPay",AES.encrypt(Double.parseDouble(couponPay)+"",AES.key));
                        wxMap.put("virtualMoney",AES.encrypt(Double.parseDouble(virtualMoney)+"",AES.key));
                        Wethod.httpPost(PayOnLineActivity.this,1992,Config.web.wx_pay,wxMap,this);
                }else{
                    getTnHttpPost(Config.web.URL_pay + "pay/yinlian/queryTn", tnMap);

                }





            } catch (IOException e) {
                e.printStackTrace();
            }





        }else if(reqcode == 2){
//            Toast.makeText(this,"支付成功",Toast.LENGTH_LONG).show();
            MyDialogUtil.setDialog(this, "支付成功", "恭喜您已支付成功!请尽快使用此订单", "消费有更多优惠哦", "返回订单", "返回首页", new MyDialog.ClickListenerInterface() {
                @Override
                public void doButtomOne() {
                    /*Intent intent = new Intent(PayOnLineActivity.this, UnPayOrderActivity.class);
                    intent.putExtra("flag", 2);
                    startActivity(intent);*/

                    if (flag.equals("Y")) {
                        Wethod.ToFailMsg(PayOnLineActivity.this, result);
                        Intent intent = new Intent(PayOnLineActivity.this, UnPayOrderActivity.class);
                        Toast.makeText(PayOnLineActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                        intent.putExtra("falg", 2);
                        startActivity(intent);
                        PayOneActivity.FLAG_SKIP_PAY = 2;
                        finish();
                    } else {
                        Wethod.ToFailMsg(PayOnLineActivity.this, result);
//                        Intent intent = new Intent(PayOneActivity.this, UnPayOrderActivity.class);
                        Toast.makeText(PayOnLineActivity.this, "支付成功", Toast.LENGTH_LONG).show();
//                        intent.putExtra("falg", 2);
//                        startActivity(intent);
                        PayOneActivity.FLAG_SKIP_PAY = 2;
                        finish();

                    }
                }

                @Override
                public void doButtomTwo() {
                    Toast.makeText(PayOnLineActivity.this, "返回首页", Toast.LENGTH_LONG).show();
                    PayOneActivity.FLAG_SKIP_PAY = 2;
                    finish();
                }
            });
            /*Intent intent = new Intent(this,UnPayOrderActivity.class);
            intent.putExtra("flag",2);
            startActivity(intent);
            PayOneActivity.FLAG_SKIP_PAY = 2;
            finish();*/
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
        }else if(reqcode == 1918){
            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if(reqcode == 12345){
            dialog.cancel();
            Wethod.ToFailMsg(this,result);
        }else if(reqcode == 2){
            Wethod.ToFailMsg(this,result);
        }else if (reqcode == 1992){
            dialog.cancel();
            Wethod.ToFailMsg(this, result);
        }else if(reqcode == 1918){
            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }


    /**
     * 就是QueryTn中的两个值来检测到第三方内部取消时调用该接口
     * @param primaryk //流水主键 不管是充值还是消费，都需要当前操作业务的主键
     * @param querytype // 1 前台充值 2 后台充值 3 前台消费     4定金支付
     */
    private void updadteCancleResult(String primaryk,String querytype){

        Map<String,String> cancleMap = new HashMap<>();
        cancleMap.put("primaryk",primaryk);//以前就是primark思密达
        cancleMap.put("queryType",querytype);
        Wethod.httpPost(PayOnLineActivity.this,1918,Config.web.base+"post/thirdpay/app/userOrderCancelBack",cancleMap,this);
    }

    /**
     * 更新订单状态
     * @param pkregister 用户ID
     * @param phoneNo 用户手机号
     * @param pkmuser 商家Id
     * @param amountStatus 定金支付状态【0：未完成   1：已完成    2：充值异常     3：充值失败 4：充值取消】
     * @param payResult 支付结果 --- 【余额：Success   】【支付宝：ALIPAY_SUCCESS 充值成功
    ALIPAY_FAIL 充值失败
    ALIPAY_CANCEL 充值取消
    ALIPAY_TO_CONFIRMED 待确认
    ALIPAY_TO_EXCEPTION 充值异常】
    【银联：Success 成功
    fail 失败
    cancel 取消】
     * @param payment 支付方式 --- 【1 支付宝 2 财付通 3 百度钱包 4 网银 5余额支付 6 微信支付 7现金支付  8 其他支付 9 平台余额支付】
     * @param amount 总金额
     * @param payEntrance 支付入口 --- 【1 充值支付 2 预约定金支付 3 消费支付】
     */
    private   void updatePayResult(String virtualMoney,String pkregister,String phoneNo,String pkmuser,String pkpayid,int amountStatus,String payResult
            ,int payment,String amount,int payEntrance,String waitMoney,String redPacket,String pkWeal,String couponPay,String point,String pointMoney){
        Map<String,String> params = new HashMap<>();
        params.put("virtualMoney",AES.encrypt(virtualMoney,AES.key));
        params.put("pkregister",pkregister);
        params.put("phoneNo",phoneNo);
        params.put("pkmuser",pkmuser);
        params.put("pkpayid",pkpayid);
        params.put("amountStatus",amountStatus+"");
        params.put("payResult",payResult);
        params.put("payment",payment+"");
        params.put("amount",AES.encrypt(amount,AES.key));
        params.put("payEntrance",payEntrance+"");

        params.put("waitMoney",AES.encrypt(waitMoney,AES.key));
        params.put("redPacket",AES.encrypt(redPacket,AES.key));
        params.put("pkWeal",pkWeal);
        params.put("couponPay",AES.encrypt(couponPay,AES.key));
        params.put("point",point);
        params.put("pointMoney",AES.encrypt(pointMoney,AES.key));
        Wethod.httpPost(PayOnLineActivity.this,2, Config.web.update_payresult, params,this);
    }



    public  void getTnHttpPost(String url, final Map<String,String> hMap) {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject object = new JSONObject(s.toString());
                    JSONArray array = object.getJSONArray("result");
                if(null !=array && array.length() > 0){
                    tn = array.getJSONObject(0).getString("tn");
                    Log.e("tyz","tn"+tn+"--------------->"+orderId);
                    dialog.cancel();
                    if(PAY_TYPE == 1){
                        /**
                         * 支付宝
                         */

                        Alipay alipay = new Alipay(PayOnLineActivity.this, Alipay_handler);
                        if(flag.equals("Y")){
                            alipay.setProduct(muname, muname, earnest, tn, primaryk, "1");
                        }else{
                            alipay.setProduct(muname, muname, earnest, tn, queryPkid, "1");
                        }

                        Log.e("liyunte","muname"+muname+"earnest"+earnest+"tn"+tn+"primaryk"+primaryk);
                        alipay.startAlipay();

                    }else if(PAY_TYPE == 4){
                        /**
                         * 银联支付
                         */
                    Log.e("TYZ","tn:"+tn);
                        goUnionpay(tn);
                    }



                }
                } catch (JSONException e) {
                    Log.e("TYZ","JSONException:"+e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TYZ","volleyError:"+volleyError.toString());
            }
        }){
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
            if(ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED){
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
                                        .installUPPayPlugin(PayOnLineActivity.this);
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
        if(resultCode == 22){
            String result = data.getExtras().getString("result");
            Toast.makeText(this,"result:"+result,Toast.LENGTH_LONG).show();
        }else{
            if(UN_FLAG == 1){
                String str = data.getExtras().getString("pay_result");
                if(str.equalsIgnoreCase("success")){
                    /**********支付成功************/
                    if(flag.equals("Y")){
                        updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "Success", 4, earnest, 2,earnest,"","","","","");
                    }else{
                        updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "Success", 4, amount, 3,earnest,redPacket,pkWeal,couponPay,point,pointMoney);
                    }



                }else if(str.equalsIgnoreCase("fail")){
                    /***********支付失败************/
                Log.e("tyz","fail");
                    if(flag.equals("Y")){
                        updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "fail", 4, earnest, 2,earnest,"","","","","");
                    }else{
                        updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "fail", 4, amount, 3,earnest,redPacket,pkWeal,couponPay,point,pointMoney);
                    }
                }else if(str.equalsIgnoreCase("cancel")){
                    Log.e("tyz","cancel");
                    if(flag.equals("Y")){
                        updadteCancleResult(dingjinId,"4");
                    }else{
                        updadteCancleResult(queryPkid,"3");
                    }
                    /***********取消支付************/
                    /*if(flag.equals("Y")){
                        updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "cancel", 4, earnest, 2,earnest,"","","","","");
                    }else{
                        updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "cancel", 4, amount, 3,earnest,redPacket,pkWeal,couponPay,point,pointMoney);
                    }*/
                }
            }else{
                UN_FLAG = 1;
            }
        }





    }


    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
        Log.e("tuyouze","queryPkid+++:"+queryPkid);

        if(WXPAY_FLAG == 2){
            if(flag.equals("Y")){
                updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "Success", 6, earnest, 2,earnest,"","","","","");
                Log.e("liyunte","PayOnLineActiivty---"+"pkmuser"+pkmuser+"preorderId"+preorderId+"earnest"+earnest);

            }else{
                updatePayResult(virtualMoney,getFromSharePreference(Config.userConfig.pkregister), getFromSharePreference(Config.userConfig.phoneno), pkmuser, preorderId, 1, "Success", 6, amount, 3,earnest,redPacket,pkWeal,couponPay,point,pointMoney);
                Log.e("liyunte","PayOnLineActiivty---"+"pkmuser"+pkmuser+"preorderId"+preorderId+"amount"+amount+"earnest"+earnest+"redPacket"+redPacket+"pkWeal"+pkWeal+"couponPay"+couponPay+"point"+point+"pointMoney");

            }

            WXPAY_FLAG = 1;
        }else if(WXPAY_FLAG == 3&&pay_flag == 2&&PAY_TYPE == 6){
            if(flag.equals("Y")){
                updadteCancleResult(dingjinId,"4");
            }else{
                updadteCancleResult(queryPkid,"3");
            }
            pay_flag = 1;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {
//       Toast.makeText(this,"onReq",Toast.LENGTH_LONG).show();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp baseResp) {
//        Toast.makeText(this,"onResp",Toast.LENGTH_LONG).show();
    }
}
