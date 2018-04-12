package com.bjypt.vipcard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.view.PayAwayView;
import com.bjypt.vipcard.view.ToastUtil;
import com.sinia.orderlang.utils.StringUtil;
import com.unionpay.UPPayAssistEx;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by 崔龙 on 2017/5/5.
 * 支付中心
 */

public class PayCenterActivity extends BaseActivity implements VolleyCallBack, PayAwayView.OnPayListener {

    private PayAwayView payAwayView;
    private Button btn_ok_pay;
    private LinearLayout ll_pay_center_back;
    private TextView tv_sum;
    private String out_trade_no;
    private String amount;
    private String payBody;
    private String paySubject;
    private int UN_FLAG = 1;
    private String dealType;
    private String notifyUrl;
    private String callbackUrl;
    BroadCastReceiverUtils utils;
    MyBroadCastReceiver myBroadCastReceiver;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_pay_center);
    }

    @Override
    public void beforeInitView() {
        Uri uri = getIntent().getData();
        if (uri != null) {


            out_trade_no = uri.getQueryParameter("out_trade_no");
            amount = uri.getQueryParameter("amount");
            payBody = uri.getQueryParameter("payBody");
            paySubject = uri.getQueryParameter("paySubject");
            dealType = uri.getQueryParameter("dealType");
            notifyUrl = uri.getQueryParameter("notifyUrl");
            callbackUrl = uri.getQueryParameter("callbackUrl");
            //验证是否有效
            String sign = uri.getQueryParameter("sign");
            if (StringUtil.isEmpty(sign)) {
                ToastUtil.showToast(this, "支付Sign不能为空");
                finish();
                return;
            }
            SortedMap<String, String> sortedMap = new TreeMap<String, String>();
            sortedMap.put("out_trade_no", out_trade_no);
            sortedMap.put("amount", amount);
            sortedMap.put("paySubject", paySubject);
            sortedMap.put("payBody", payBody);
            sortedMap.put("dealType", dealType);
            sortedMap.put("notifyUrl", notifyUrl);
            sortedMap.put("callbackUrl", callbackUrl);
            if(!sign.equals(AES.createSign(sortedMap))){
                finish();
                ToastUtil.showToast(this, "Sign不匹配");
                return ;
            }
        }

        utils = new BroadCastReceiverUtils();
        myBroadCastReceiver = new MyBroadCastReceiver();
        utils.registerBroadCastReceiver(this, "ccb_pay_notify", myBroadCastReceiver);//ccb 支付通知
    }

    @Override
    public void initView() {
        payAwayView = (PayAwayView) findViewById(R.id.payAwayView);
        payAwayView.setActivity(this);//必须设置
        btn_ok_pay = (Button) findViewById(R.id.btn_ok_pay);
        ll_pay_center_back = (LinearLayout) findViewById(R.id.ll_pay_center_back);
        tv_sum = (TextView) findViewById(R.id.tv_sum);
        btn_ok_pay.setEnabled(true);
    }

    @Override
    public void afterInitView() {
        tv_sum.setText(amount);
        payAwayView.sendHttpPayAways(dealType, "", getFromSharePreference(Config.userConfig.pkregister));
    }

    @Override
    public void bindListener() {
        payAwayView.setOnPayListener(this);
        btn_ok_pay.setOnClickListener(this);
        ll_pay_center_back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_pay_center_back:
                finish();
                break;
            case R.id.btn_ok_pay:
                //                ToastUtil.showToast(this,"确定支付");
                if (payAwayView.getSelectPayCode() == null) {
                    Toast.makeText(this, "请选择充值方式", Toast.LENGTH_LONG).show();
                    return;
                }
                payAwayView.startPay();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (utils != null) {
            utils.UnRegisterBroadCastReceiver(this, myBroadCastReceiver);
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    public void OnPayFinish() {
        payFinish();
    }

    @Override
    public void onPayCancel() {

    }

    @Override
    public Map<String, String> toPayParams(String outorderid, String payMoney) {
        Map<String, String> param = new HashMap<>();
        param.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        param.put("orderid", out_trade_no);//订单主键
        param.put("dealtype", dealType);//业务类型 1.充值 2立即买单 3优惠券  后续待定
        param.put("amount", amount);//金额
        param.put("paytype", "" + payAwayView.getSelectPayCode().getCode() + "");
        param.put("paySubject", paySubject);
        param.put("payBody", payBody);
        if (StringUtil.isNotEmpty(notifyUrl)) {
            param.put("notifyUrl", notifyUrl);
        }
        return param;
    }

    @Override
    public Map<String, String> createOrderParams() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        params.put("pkmuser", "");
        params.put("waitMoney", AES.encrypt(amount, AES.key));
        params.put("payEntrance", "1");
        params.put("pkpropre", "");
        return params;
    }

    @Override
    protected void onResume() {
        super.onResume();
        payAwayView.onResume();
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
                            UPPayAssistEx
                                    .installUPPayPlugin(PayCenterActivity.this);
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

    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals("ccb_pay_notify")) {
                payFinish();
            }
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
                payFinish();
            } else if (str.equalsIgnoreCase("fail")) {
                /***********支付失败************/
                payFinish();
            } else if (str.equalsIgnoreCase("cancel")) {
                /***********取消支付************/
                payAwayView.chargeUpdateCancle();
            }
        } else {
            UN_FLAG = 1;
        }
    }

    private void payFinish() {
        if (StringUtil.isNotEmpty(callbackUrl)) {
            Intent intent = new Intent();
            intent.setAction("ccb_pay_notify_callback");
            intent.putExtra("callbackUrl", callbackUrl);//回跳地址
            sendBroadcast(intent);
            finish();
        } else {
            finish();
        }
    }
}
