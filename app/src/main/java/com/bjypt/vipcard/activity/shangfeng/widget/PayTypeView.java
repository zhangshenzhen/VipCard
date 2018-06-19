package com.bjypt.vipcard.activity.shangfeng.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.common.EventBusMessageEvent;
import com.bjypt.vipcard.activity.shangfeng.common.enums.EventBusWhat;
import com.bjypt.vipcard.activity.shangfeng.common.enums.PayTypeEnum;
import com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode;
import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.ResultDataBean;
import com.bjypt.vipcard.activity.shangfeng.dialog.LoadingDialog;
import com.bjypt.vipcard.activity.shangfeng.util.LogUtils;
import com.bjypt.vipcard.activity.shangfeng.util.OkHttpUtil;
import com.bjypt.vipcard.activity.shangfeng.util.ToastUtils;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.NewAlipay;
import com.bjypt.vipcard.model.PayAway;
import com.bjypt.vipcard.model.PayAwayData;
import com.bjypt.vipcard.wxapi.WXHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanglei on 2018/5/15.
 */

public class PayTypeView extends LinearLayout {

    private LoadingDialog dialog;

    private static final int REQUEST_INFO = 2018124;
    /*成功*/
    public static final int PAY_SUCCESS = 9000;
    /*失败*/
    public static final int PAY_FAIL = 10000;
    /*待确认*/
    public final int ALIPAY_TO_BE_CONFIRMED = 8000;
    /*取消 */
    public static final int PAY_CANCEL = 6001;
    private static final int request_pay_away = 21345;
    private static final int request_create_order = 21344;
    private static final int request_topay = 21347;
    private static final int request_cancel_order = 21346;
    private static final int get_order_status = 21388;
    LinearLayout linear_pays;
    private OnPayListener onPayListener;
    private String primaryk;
    private PayAway selectPayAway;
    //    private int dealType; //业务类型
    private boolean preOrder = false; //是否已经预下单
    private AnimationDrawable drawable;
    private String orderId;
//    private BaseActivity activity;
//    private LoadingPageDialog loadingPageDialog;

    public static int WXPAY_FLAG = 1; // 微信支付标志

    private boolean isOrderFinish = false;
    private String mPkmuser;

    public PayTypeView(Context context) {
        super(context);
        init(context, null);
    }

    public PayTypeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PayTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PayTypeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 获取支付方式
     *
     * @param pkmuser
     * @param pkregister
     */
    public void sendHttpPayAways(int dealType, String pkmuser, String pkregister) {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", pkregister);
        params.put("pkmuser", pkmuser);
        params.put("dealtype", dealType + "");
        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
            @Override
            public void success(int resultCode, ResultDataBean data) {
                hideProgress();
                Gson gson = new Gson();
                LogUtils.print(" sendHttpPayAways : " + gson.toJson(data));
                if (ResultCode.SUCCESS == data.getResultStatus()) {

                    PayAwayData payAways = gson.fromJson(gson.toJson(data.getResultData()), new TypeToken<PayAwayData>() {
                    }.getType());
                    linear_pays.removeAllViews();
                    if (payAways != null && !payAways.getPayTypeList().isEmpty()) {
                        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                        for (int i = 0; i < payAways.getPayTypeList().size(); i++) {
                            View view = layoutInflater.inflate(R.layout.layout_payaway_item2, null);
                            ImageView pay_iv = (ImageView) view.findViewById(R.id.pay_iv);
                            TextView tv_pay_name = (TextView) view.findViewById(R.id.tv_pay_name);
                            tv_pay_name.setText(payAways.getPayTypeList().get(i).getPayname());
                            pay_iv.setImageResource(getPayIcon(payAways.getPayTypeList().get(i).getCode()));
                            view.setTag(payAways.getPayTypeList().get(i));
                            view.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    PayAway payAway = (PayAway) view.getTag();
                                    refreshCheckIcon(payAway);
                                }
                            });
                            linear_pays.addView(view);
                        }
                        if (payAways.getPayTypeList().size() > 0) {//初始化选中第一个
                            refreshCheckIcon(payAways.getPayTypeList().get(0));
                        }
                    }
                }
            }

            @Override
            public void onError(int resultCode, String errorMsg) {
                hideProgress();
            }
        }, Config.web.get_pay_away, params);
    }

    /**
     * 发起支付
     */
    public void startPay() {
        showProgress();
        orderAdd();
    }


    private void orderAdd() {
        Map<String, String> params = onPayListener.createOrderParams();
        if (params == null) {
            Map<String, String> params1 = onPayListener.toPayParams(null);
            toPay(params1);
        } else {
            OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
                @Override
                public void success(int resultCode, ResultDataBean data) {
                    if (ResultCode.SUCCESS == data.getResultStatus()) {
                        try {
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(data.getResultData()));
                            String orderid = jsonObject.getString("orderid");
                            Map<String, String> params = onPayListener.toPayParams(orderid);
                            toPay(params);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        hideProgress();
                        ToastUtils.showToast(data.getResultData() + "");
                    }
                }

                @Override
                public void onError(int resultCode, String errorMsg) {
                    hideProgress();
                    LogUtils.print("orderAdd  resultCode= " + resultCode + " errorMsg " + errorMsg);
                }
            }, Config.web.ORDERADD, params);
        }
    }

    private void toPay(Map<String, String> params) {
        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
            @Override
            public void success(int resultCode, ResultDataBean data) {
                if (ResultCode.SUCCESS == data.getResultStatus()) {
                    JSONObject jsonObject = null;
                    switch (selectPayAway.getCode()) {
                        case PayTypeEnum.Alipay:

                            try {
                                jsonObject = new JSONObject(new Gson().toJson(data.getResultData()));
                                String content = jsonObject.getString("content");
                                NewAlipay newAlipay = new NewAlipay(getContext(), Alipay_handler);
                                newAlipay.setProduct(content);
                                newAlipay.startAlipay();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            break;
                        case PayTypeEnum.WechatPay:
                            try {
                                jsonObject = new JSONObject(new Gson().toJson(data.getResultData()));
                                JSONObject json = new JSONObject(jsonObject.getString("content"));
                                WXHelper wxHelper = new WXHelper(getContext(), json.getString("appid"));
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
                                showProgress();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                } else {
                    hideProgress();
                    ToastUtils.showToast(data.getResultData() + "");
                }
            }

            @Override
            public void onError(int resultCode, String errorMsg) {
                hideProgress();
                LogUtils.print("orderAdd  resultCode= " + resultCode + " errorMsg " + errorMsg);
            }
        }, Config.web.TOPAY, params);
    }

    private void init(Context context, AttributeSet attrs) {
        EventBus.getDefault().register(this);
        LayoutInflater.from(context).inflate(R.layout.layout_pay_away_view, this);
        linear_pays = (LinearLayout) findViewById(R.id.linear_pays);
    }

    public void showProgress() {
        dialog = new LoadingDialog(getContext(), "正在加载...");
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }


    Handler Alipay_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideProgress();
            switch (msg.what) {
                case PAY_SUCCESS://支付宝支付成功
                    payFinish();
                    break;
                case PAY_FAIL://支付宝支付失败
                    Toast.makeText(PayTypeView.this.getContext(), "支付失败", Toast.LENGTH_SHORT).show();
                    break;
                case ALIPAY_TO_BE_CONFIRMED://支付宝支付待确认
                    Toast.makeText(PayTypeView.this.getContext(), "支付结果待确认", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void payFinish() {
        if (!isOrderFinish) {
            isOrderFinish = true;
            if (onPayListener != null) {
                onPayListener.OnPayFinish();
            }
        }
    }

    private void payCancel(){
        if (!isOrderFinish) {
            isOrderFinish = true;
            if (onPayListener != null) {
                onPayListener.onPayCancel();
            }
        }
    }

    private void refreshCheckIcon(PayAway payAway) {
        selectPayAway = payAway;
        for (int i = 0; i < linear_pays.getChildCount(); i++) {
            View childView = linear_pays.getChildAt(i);
            PayAway childPayAway = (PayAway) childView.getTag();
            ImageButton imageView = (ImageButton) childView.findViewById(R.id.iv_vipcard_select);
            if (childPayAway.getCode() == selectPayAway.getCode()) {
                imageView.setSelected(true);
            } else {
                imageView.setSelected(false);
            }
        }
    }

    public PayAway getSelectPayCode() {
        return selectPayAway;
    }

    private int getPayIcon(int code) {
        if (code == PayTypeEnum.Alipay) {
            return R.mipmap.alipay_image;
        } else if (code == PayTypeEnum.WechatPay) {
            return R.mipmap.wechant_image;
        }
        return R.mipmap.alipay_image;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessageEvent event) {
        if (event.getWhat() == EventBusWhat.WxPayResult) {
            Integer resultCode = Integer.parseInt(event.getData()+"");
            if(resultCode == PAY_SUCCESS){
                payFinish();
            }else if(resultCode == PAY_FAIL){
                ToastUtils.showToast("微信支付异常");
            }else if(resultCode == PAY_CANCEL){
                payCancel();
            }

        }
    }

    public void onDestory(){
        EventBus.getDefault().unregister(this);
    }



    public void setOnPayListener(OnPayListener listener) {
        this.onPayListener = listener;
    }

    public interface OnPayListener {

        /**
         * 支付完成后处理业务逻辑
         */
        void OnPayFinish();

        void onPayCancel();

        /**
         * 设置topay参数
         *
         * @return
         */
        Map<String, String> toPayParams(String outorderid);

        Map<String, String> createOrderParams();

    }



}
