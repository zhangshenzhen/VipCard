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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.NewAlipay;
import com.bjypt.vipcard.model.NewPayData;
import com.bjypt.vipcard.model.NewPayWxData;
import com.bjypt.vipcard.model.NewYinLianPayData;
import com.bjypt.vipcard.model.OrderIdData;
import com.bjypt.vipcard.model.YouhuiBuyBean;
import com.bjypt.vipcard.wxapi.WXHelper;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/10 0010.=
 */

public class YouhuiBuyActivity extends BaseActivity implements VolleyCallBack<String> {

    private TextView youhuiName;//优惠劵名称
    private TextView youhuiOnePrice,youhuiTotalPrice,youhuiTime;//优惠劵单价,总价，时效
    private TextView youhuiTrue;//确认支付
    private LinearLayout youhuiRedece,youhuiAdd;//加减
    private LinearLayout mBack;
//    private String pkcoupon;
    private String pkmuser;//商家主键
    private YouhuiBuyBean youhuiBuyBean;
    private TextView buyNum;
    private int num = 1;
    private LinearLayout mSystemPay;
    private LinearLayout mAlipayPay;
    private LinearLayout mWxPay;
    private LinearLayout mBankPay;
    private ImageView iv_weixin_pay;
    private ImageView iv_zhifubao_pay;
    private ImageView iv_bank_pay;
    private ImageView iv_system_pay;
    private int PAY_STYLE_FLAG = 0;
    private OrderIdData orderIdData;
    private NewPayData newPayData;
    private NewPayWxData newPayWxData;
    private NewYinLianPayData newYinLianPayData;
    private String sourceid;
    private Context mContext;
    private String pkmerchantcoupon;//优惠劵主键


    private Handler Alipay_handlers = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ALIPAY_SUCCESS://支付宝支付成功
                    Toast.makeText(YouhuiBuyActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case ALIPAY_FAIL://支付宝支付失败
                    Toast.makeText(YouhuiBuyActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    break;
                case ALIPAY_TO_BE_CONFIRMED://支付宝支付待确认

                    break;
                case ALIPAY_CANCEL://支付宝支付取消
                    Toast.makeText(YouhuiBuyActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_youhuibuy);
    }

    @Override
    public void beforeInitView() {
        mContext = this;
        Intent intent = getIntent();
//        pkcoupon = intent.getStringExtra("pkcoupon");
        pkmuser = intent.getStringExtra("pkmuser");
        pkmerchantcoupon = intent.getStringExtra("pkmerchantcoupon");

        Map<String,String> params = new HashMap<>();
        params.put("pkmuser",pkmuser);
        params.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
        params.put("pkmerchantcoupon",pkmerchantcoupon);
        Wethod.httpPost(this,1122, Config.web.youhui_buy_before,params,this);
    }

    @Override
    public void initView() {
        youhuiName= (TextView) findViewById(R.id.youhui_name);
        youhuiOnePrice = (TextView) findViewById(R.id.youhui_one_price);
        youhuiTotalPrice = (TextView) findViewById(R.id.youhui_total_price);
        youhuiTime = (TextView) findViewById(R.id.youhui_time);
        youhuiTrue = (TextView) findViewById(R.id.youhui_true);
         mBack = (LinearLayout) findViewById(R.id.layout_back_youhui_buy);
        buyNum = (TextView) findViewById(R.id.buy_num);

        youhuiRedece = (LinearLayout) findViewById(R.id.youhui_num_reduce);
        youhuiAdd = (LinearLayout) findViewById(R.id.youhui_num_add);

        mSystemPay = (LinearLayout) findViewById(R.id.youhui_system_pay);
        mAlipayPay = (LinearLayout) findViewById(R.id.youhui_zhifubao_pay);
        mWxPay = (LinearLayout) findViewById(R.id.youhui_weixin_pay);
        mBankPay = (LinearLayout) findViewById(R.id.youhui_bank_pay);

        iv_bank_pay = (ImageView) findViewById(R.id.iv_bank_pay);
        iv_zhifubao_pay = (ImageView) findViewById(R.id.iv_zhifubao_pay);
        iv_weixin_pay = (ImageView) findViewById(R.id.iv_weixin_pay);
        iv_system_pay = (ImageView) findViewById(R.id.iv_system_pay);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        youhuiTrue.setOnClickListener(this);
        youhuiAdd.setOnClickListener(this);
        youhuiRedece.setOnClickListener(this);
        mBack.setOnClickListener(this);

        mSystemPay.setOnClickListener(this);
        mAlipayPay.setOnClickListener(this);
        mBankPay.setOnClickListener(this);
        mWxPay.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.youhui_system_pay:
                //TODO 系统余额支付
                PAY_STYLE_FLAG = 1;
                iv_bank_pay.setBackgroundResource(R.mipmap.select_false);
                iv_zhifubao_pay.setBackgroundResource(R.mipmap.select_false);
                iv_weixin_pay.setBackgroundResource(R.mipmap.select_false);
                iv_system_pay.setBackgroundResource(R.mipmap.select_true);
                break;
            case R.id.youhui_zhifubao_pay:
                //TODO 支付宝支付
                PAY_STYLE_FLAG = 2;
                iv_bank_pay.setBackgroundResource(R.mipmap.select_false);
                iv_zhifubao_pay.setBackgroundResource(R.mipmap.select_true);
                iv_weixin_pay.setBackgroundResource(R.mipmap.select_false);
                iv_system_pay.setBackgroundResource(R.mipmap.select_false);
                break;

            case R.id.youhui_weixin_pay:
                //TODO 微信支付
                PAY_STYLE_FLAG = 3;
                iv_bank_pay.setBackgroundResource(R.mipmap.select_false);
                iv_zhifubao_pay.setBackgroundResource(R.mipmap.select_false);
                iv_weixin_pay.setBackgroundResource(R.mipmap.select_true);
                iv_system_pay.setBackgroundResource(R.mipmap.select_false);
                break;

            case R.id.youhui_bank_pay:
                //TODO 银联支付
                PAY_STYLE_FLAG = 4;
                iv_bank_pay.setBackgroundResource(R.mipmap.select_true);
                iv_zhifubao_pay.setBackgroundResource(R.mipmap.select_false);
                iv_weixin_pay.setBackgroundResource(R.mipmap.select_false);
                iv_system_pay.setBackgroundResource(R.mipmap.select_false);
                break;

            case R.id.youhui_true:
                //TODO 确认支付
                //1.首先需要创建购买优惠劵订单
                Map<String,String> youhuiParams = new HashMap<>();
                youhuiParams.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
                youhuiParams.put("pkmuser",pkmuser);
                youhuiParams.put("pkmerchantcoupon",pkmerchantcoupon);
                youhuiParams.put("paycount",num+"");
                youhuiParams.put("payamount",youhuiBuyBean.getResultData().getActualPayment());
                Wethod.httpPost(this,1111,Config.web.create_buy_youhui_order,youhuiParams,this);
                break;
            case R.id.layout_back_youhui_buy:
                finish();
                break;
            case R.id.youhui_num_add:
                //TODO 优惠劵数量添加
                buyNum.setText(++num+"");
                youhuiTotalPrice.setText("￥ "+Integer.parseInt(youhuiBuyBean.getResultData().getActualPayment())*num);
                youhuiTrue.setText("确认支付 ￥"+Integer.parseInt(youhuiBuyBean.getResultData().getActualPayment())*num);
                break;
            case R.id.youhui_num_reduce:
                //TODO 优惠劵数量减少
                if(num>0){
                    buyNum.setText(--num+"");
                    youhuiTotalPrice.setText("￥ "+Integer.parseInt(youhuiBuyBean.getResultData().getActualPayment())*num);
                    youhuiTrue.setText("确认支付 ￥"+Integer.parseInt(youhuiBuyBean.getResultData().getActualPayment())*num);
                }else{
                    youhuiTrue.setText("确认支付 ￥0");
                    buyNum.setText("0");
                    youhuiTotalPrice.setText("0");
                }

                break;
        }
    }

    private void getPayStyle(int count,String [] style){
        //首先判断共有几种支付方式
        if(count == 1){
            showPayStyle(Integer.parseInt(style[0]));
        }else if(count == 2){
            showPayStyle(Integer.parseInt(style[0]));
            showPayStyle(Integer.parseInt(style[1]));
        }else if(count == 3){
            showPayStyle(Integer.parseInt(style[0]));
            showPayStyle(Integer.parseInt(style[1]));
            showPayStyle(Integer.parseInt(style[2]));
        }else if(count == 4){
            showPayStyle(Integer.parseInt(style[0]));
            showPayStyle(Integer.parseInt(style[1]));
            showPayStyle(Integer.parseInt(style[2]));
            showPayStyle(Integer.parseInt(style[3]));
        }else if(count == 5){
            showPayStyle(Integer.parseInt(style[0]));
            showPayStyle(Integer.parseInt(style[1]));
            showPayStyle(Integer.parseInt(style[2]));
            showPayStyle(Integer.parseInt(style[3]));
            showPayStyle(Integer.parseInt(style[4]));
        }

    }

    //获取显示各是那种方式
    private void showPayStyle(int size){
        if(size == 1){
            //支付宝
            mAlipayPay.setVisibility(View.VISIBLE);
        }else if(size == 6){
            //微信
            mWxPay.setVisibility(View.VISIBLE);
        }else if(size == 9){
            //平台余额
            mSystemPay.setVisibility(View.VISIBLE);
        }else if(size == 4){
            //网银
            mBankPay.setVisibility(View.VISIBLE);
        }else if(size == 5){
            //商家余额支付

        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if(reqcode == 1122){
            try {
                youhuiBuyBean  = getConfiguration().readValue(result.toString(), YouhuiBuyBean.class);
                youhuiName.setText("购买"+youhuiBuyBean.getResultData().getActualPayment()+"抵扣"+youhuiBuyBean.getResultData().getValueamount());
                youhuiOnePrice.setText("￥ "+youhuiBuyBean.getResultData().getActualPayment());
                youhuiTotalPrice.setText("￥ "+youhuiBuyBean.getResultData().getActualPayment());
                youhuiTrue.setText("确认支付 ￥"+youhuiBuyBean.getResultData().getActualPayment());
                youhuiTime.setText("优惠劵时效为"+youhuiBuyBean.getResultData().getStartdate()+"-"+youhuiBuyBean.getResultData().getEnddate());


                String[] payStyle = new String[]{};
                payStyle = youhuiBuyBean.getResultData().getPaytype().split("\\|");
                Log.e("youhuibuy","payStyle:"+payStyle.length+"========:"+payStyle[0]);
                //TODO 处理有多少种支付方式
                getPayStyle(payStyle.length,payStyle);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(reqcode == 1111){
            try {
                orderIdData = getConfiguration().readValue(result.toString(), OrderIdData.class);
                //创建优惠劵购买订单返回的订单主键 （支付前向后台服务器下单的订单主键入参)
                sourceid = orderIdData.getResultData().getPkorderid();

                //TODO 当请求创建订单接口成功后立即发起  支付前向后台服务器下单 接口 (此时需要区分是何种支付方式)
                Map<String,String> param = new HashMap<>();
                if(PAY_STYLE_FLAG == 1){
                    //当使用平台余额支付时候需要pkregister.pkmuser
//                    param.put("pkmuser",pkmuser);
                    param.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
                }
                param.put("orderid",sourceid);//订单主键
                param.put("dealtype",3+"");//业务类型 1.充值 2立即买单 3优惠券  后续待定
                param.put("amount",num*Integer.parseInt(youhuiBuyBean.getResultData().getPayamount())+"");//金额
//                param.put("amount","0.01");//金额
                // 1支付宝 2财付通 3百度钱包 4 网银 5余额支付 6微信支付 7现金支付 8其他支付 9平台支付 10建行在线
                if(PAY_STYLE_FLAG == 1){
                    param.put("paytype",""+9);
                }else if(PAY_STYLE_FLAG == 2){
                    param.put("paytype",""+1);
                }else if(PAY_STYLE_FLAG == 3){
                    param.put("paytype",""+6);
                }else if(PAY_STYLE_FLAG == 4){
                    param.put("paytype",""+4);
                }
                param.put("paySubject","优惠劵购买");
                param.put("payBody","优惠劵购买");
                Wethod.httpPost(this,2222,Config.web.pay_new_before,param,this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(reqcode == 2222){
            if(PAY_STYLE_FLAG == 1){
                //平台余额
                Toast.makeText(this,"购买成功",Toast.LENGTH_LONG).show();
                finish();
            }else if(PAY_STYLE_FLAG == 2){
                //支付宝
                try {
                    newPayData = getConfiguration().readValue(result.toString(), NewPayData.class);

                    NewAlipay newAlipay = new NewAlipay(YouhuiBuyActivity.this, Alipay_handlers);

                    newAlipay.setProduct(newPayData.getResultData().getContent());
                    newAlipay.startAlipay();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(PAY_STYLE_FLAG == 3){
                //微信
                try {
                    newPayWxData = getConfiguration().readValue(result.toString(), NewPayWxData.class);

                    try {
                        JSONObject json = new JSONObject(newPayWxData.getResultData().getContent());

                        WXHelper wxHelper = new WXHelper(mContext,json.getString("appid"));
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

            }else if(PAY_STYLE_FLAG == 4){
                //银联 NewYinLianPayData
                try {
                    newYinLianPayData  = getConfiguration().readValue(result.toString(), NewYinLianPayData.class);
                    Log.e("ppp","newYinLianPayData:"+newYinLianPayData.getResultData().getTn());
                    goUnionpay(newYinLianPayData.getResultData().getTn());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
                            UPPayAssistEx
                                    .installUPPayPlugin(YouhuiBuyActivity.this);
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
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 22){
            String result = data.getExtras().getString("result");
            Toast.makeText(this,"result:"+result,Toast.LENGTH_LONG).show();
        }else {
                String str = data.getExtras().getString("pay_result");
                if (str.equalsIgnoreCase("success")) {
                    /**********支付成功************/
                    Toast.makeText(this,"支付成功",Toast.LENGTH_LONG);
                    finish();
                } else if (str.equalsIgnoreCase("fail")) {
                    /***********支付失败************/
                    Toast.makeText(this,"支付失败",Toast.LENGTH_LONG);
                } else if (str.equalsIgnoreCase("cancel")) {
                    Toast.makeText(this,"支付取消",Toast.LENGTH_LONG);
                }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
