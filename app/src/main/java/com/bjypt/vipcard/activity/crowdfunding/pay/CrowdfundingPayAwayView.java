package com.bjypt.vipcard.activity.crowdfunding.pay;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.activity.NewTopupWayActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.PayDealTypeEnum;
import com.bjypt.vipcard.common.PayTypeEnum;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.NewAlipay;
import com.bjypt.vipcard.dialog.CCBCurrencyDialog;
import com.bjypt.vipcard.model.AddOrderBean;
import com.bjypt.vipcard.model.NewPayData;
import com.bjypt.vipcard.model.NewPayWxData;
import com.bjypt.vipcard.model.NewYinLianPayData;
import com.bjypt.vipcard.model.OrderStatusBean;
import com.bjypt.vipcard.model.PayAway;
import com.bjypt.vipcard.model.PayAwayBean;
import com.bjypt.vipcard.model.PayAwayData;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.utils.VirtualmoneySuccessHelper;
import com.bjypt.vipcard.view.CheckPwdDialog;
import com.bjypt.vipcard.view.LoadingPageDialog;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.wxapi.WXHelper;
import com.lidroid.xutils.util.LogUtils;
import com.orhanobut.logger.Logger;
import com.sinia.orderlang.utils.StringUtil;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/26.
 */

public class CrowdfundingPayAwayView extends LinearLayout implements VolleyCallBack<String> {

    /*成功*/
    public final int ALIPAY_SUCCESS = 9000;
    /*失败*/
    public final int ALIPAY_FAIL = 10000;
    /*待确认*/
    public final int ALIPAY_TO_BE_CONFIRMED = 8000;
    /*取消 */
    public final int ALIPAY_CANCEL = 6001;
    private static final int request_create_order = 21344;
    private static final int request_topay = 21347;
    private static final int request_cancel_order = 21346;
    private static final int get_order_status = 21388;
    LinearLayout linear_pays;
    LinearLayout linear_pays_more;
    private RelativeLayout rl_select;
    private RelativeLayout rl_pay_anim_show;
    private OnPayListener onPayListener;
    private TextView tv_more_pay;
    private ImageView iv_pay_anim;
    private PayAway selectPayAway;
    private int dealType = 17; //业务类型
    private AnimationDrawable drawable;
    private String orderId;
    private Activity activity;
    private LoadingPageDialog loadingPageDialog;

    public static int WXPAY_FLAG = 1; // 微信支付标志

    private boolean isOrderFinish = false;

    private PayAwayData payAwayData;

    public CrowdfundingPayAwayView(Context context) {
        super(context);
        init(context, null);
    }

    public CrowdfundingPayAwayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CrowdfundingPayAwayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CrowdfundingPayAwayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 获取支付方式
     */
    public void setPayAway(int payway) {
        payAwayData = new PayAwayData();
        payAwayData.setShownum(100);
        List<PayAway> payTypeList = new ArrayList<>();
        int zhifubo = payway & PayTypeEnum.Zhifubao.getCode();
        int weixin = payway & PayTypeEnum.Weixin.getCode();
        if (zhifubo == PayTypeEnum.Zhifubao.getCode()) {
            PayAway payAway = new PayAway();
            payAway.setPayname(PayTypeEnum.Zhifubao.getPayName());
            payAway.setCode(PayTypeEnum.Zhifubao.getCode());
            payTypeList.add(payAway);
        }
        if (weixin == PayTypeEnum.Weixin.getCode()) {
            PayAway payAway = new PayAway();
            payAway.setPayname(PayTypeEnum.Weixin.getPayName());
            payAway.setCode(PayTypeEnum.Weixin.getCode());
            payTypeList.add(payAway);
        }
        payAwayData.setPayTypeList(payTypeList);
        initPayWay();
    }



    /**
     * 发起支付
     */
    public void startPay() {
        Logger.e("开启支付");
        if (selectPayAway.getCode() == PayTypeEnum.Pingtai.getCode() || selectPayAway.getCode() == PayTypeEnum.ShangjiaYuE.getCode()) {
            loadingShow();
            String paylable = payLable();
            Map<String, String> params = onPayListener.createOrderParams();
            String waitMoney = AES.decrypt(params.get("waitMoney").toString(), AES.key);
            if (checkYue(waitMoney)) {
                loadingStop();
                return;
            }
            CheckPwdDialog psdDialog = new CheckPwdDialog(getContext(), "", paylable, "¥" + waitMoney, new CheckPwdDialog.CheckPayPasswordListener() {
                @Override
                public void onPass() {
                    postPay();
                    loadingShow();
                }
            });
            psdDialog.show();
            loadingStop();
        } else {
            postPay();
        }
    }

    public void onResume() {
        if (selectPayAway != null && selectPayAway.getCode() == PayTypeEnum.Weixin.getCode()) {
            if (WXPAY_FLAG == 2) {
                payFinish();
                WXPAY_FLAG = 1;
            } else if (WXPAY_FLAG == 3) {
                //支付失败什么也不做
                WXPAY_FLAG = 1;
            }
        }

    }

    /**
     * 检查是否余额不足
     *
     * @param amount
     * @return
     */
    private boolean checkYue(String amount) {
        if (StringUtil.isNotEmpty(amount)) {
            PayAway payAway = getSelectPayCode();
            if (StringUtil.getFloat(amount) > StringUtil.getFloat(payAway.getBalance())) {
                ToastUtil.showToast(getContext(), "余额不足，请选择其他支付方式");
                return true;
            }
        }
        return false;
    }

    private void postPay() {
        Wethod.httpPost(this.getContext(), request_create_order, Config.web.cf_buy_pre_order, onPayListener.createOrderParams(), this, View.VISIBLE);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (StringUtil.isNotEmpty(orderId) && !isOrderFinish) { //订单号不为空并且 订单未完成
            Map<String, String> orderMap = new HashMap<>();
            orderMap.put("orderid", orderId);
            Wethod.httpPost(this.getContext(), get_order_status, Config.web.get_order_status, orderMap, this, View.GONE);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_pay_away_view, this);

        linear_pays = (LinearLayout) findViewById(R.id.linear_pays);
        linear_pays_more = (LinearLayout) findViewById(R.id.linear_pays_more);
        rl_select = (RelativeLayout) findViewById(R.id.rl_select);
        rl_pay_anim_show = (RelativeLayout) findViewById(R.id.rl_pay_anim_show);
        iv_pay_anim = (ImageView) findViewById(R.id.iv_pay_anim);
        iv_pay_anim.setImageResource(R.drawable.animation_pay);
        drawable = (AnimationDrawable) iv_pay_anim.getDrawable();
        tv_more_pay = (TextView) findViewById(R.id.tv_more_pay);
        tv_more_pay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_pays_more.setVisibility(View.VISIBLE);
                tv_more_pay.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == request_cancel_order) {
            Wethod.ToFailMsg(getContext(), result);
            Log.e("clclclclcl", "::::" + result + "");
        } else if (reqcode == get_order_status) {
            try {
                OrderStatusBean orderStatusBean = ObjectMapperFactory.createObjectMapper().readValue(result.toString(), OrderStatusBean.class);
                Log.e("yang", "onSuccess: " + result.toString());
                if (orderStatusBean.getResultData().equals("付款成功")) {
                    Toast.makeText(getContext(), "付款成功", Toast.LENGTH_LONG).show();
                    payFinish();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == request_create_order) {
            try {
                AddOrderBean addOrderBean = ObjectMapperFactory.createObjectMapper().readValue(result.toString(), AddOrderBean.class);
                orderId = addOrderBean.getResultData().getOrderid();
                toPay(orderId, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == request_topay) {
            if (selectPayAway.getCode() == PayTypeEnum.Zhifubao.getCode()) {
                //支付宝
                Log.e("yang", result.toString());
                try {
                    NewPayData newPayData = ObjectMapperFactory.createObjectMapper().readValue(result.toString(), NewPayData.class);
                    NewAlipay newAlipay = new NewAlipay(getContext(), Alipay_handler);
                    newAlipay.setProduct(newPayData.getResultData().getContent());
                    newAlipay.startAlipay();
                    loadingStop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (selectPayAway.getCode() == PayTypeEnum.Weixin.getCode()) {
                //微信
                Log.e("yang", result.toString());
                try {
                    NewPayWxData newPayWxData = ObjectMapperFactory.createObjectMapper().readValue(result.toString(), NewPayWxData.class);
                    try {
                        JSONObject json = new JSONObject(newPayWxData.getResultData().getContent());
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
                        loadingStop();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private void initPayWay() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        for (int i = 0; i < payAwayData.getPayTypeList().size(); i++) {
            View view = layoutInflater.inflate(R.layout.layout_payaway_item, null);
            ImageView pay_iv = (ImageView) view.findViewById(R.id.pay_iv);
            TextView tv_pay_name = (TextView) view.findViewById(R.id.tv_pay_name);


            tv_pay_name.setText(payAwayData.getPayTypeList().get(i).getPayname());

            pay_iv.setImageResource(getPayIcon(payAwayData.getPayTypeList().get(i).getCode()));
            TextView tv_yue = (TextView) view.findViewById(R.id.tv_yue);
            ImageView add_money = (ImageView) view.findViewById(R.id.add_money);

            tv_yue.setVisibility(View.INVISIBLE);
            add_money.setVisibility(View.INVISIBLE);

            view.setTag(payAwayData.getPayTypeList().get(i));
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    PayAway payAway = (PayAway) view.getTag();
                    refreshCheckIcon(payAway);
                }
            });
            if (i + 1 > payAwayData.getShownum()) {
                linear_pays_more.addView(view);
            } else {
                linear_pays.addView(view);
            }
        }
        if (payAwayData.getPayTypeList().size() > 0) {//初始化选中第一个
            refreshCheckIcon(payAwayData.getPayTypeList().get(0));
        }
    }

    private Handler Alipay_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("CXY", "msg.what:" + msg.what);
            switch (msg.what) {
                case ALIPAY_SUCCESS://支付宝支付成功
                    payFinish();
                    break;
                case ALIPAY_FAIL://支付宝支付失败
                    Toast.makeText(CrowdfundingPayAwayView.this.getContext(), "支付失败", Toast.LENGTH_SHORT).show();
                    break;
                case ALIPAY_TO_BE_CONFIRMED://支付宝支付待确认
                    Toast.makeText(CrowdfundingPayAwayView.this.getContext(), "支付结果待确认", Toast.LENGTH_SHORT).show();
                    break;
                case ALIPAY_CANCEL://支付宝支付取消
                    LogUtil.debugPrint("支付宝支付取消");
                    break;
            }
        }
    };

    private void toPay(String outorderid, String payMoney) {
        Map<String, String> params = onPayListener.toPayParams(outorderid, payMoney);
        Wethod.httpPost(this.getContext(), request_topay, Config.web.cf_buy_topay, params, this, View.GONE);
    }

    private void payFinish() {
        if (!isOrderFinish) {
            isOrderFinish = true;
            if (onPayListener != null) {
                onPayListener.OnPayFinish();
            }
        }

    }

    private void refreshCheckIcon(PayAway payAway) {
        selectPayAway = payAway;
        for (int i = 0; i < linear_pays.getChildCount(); i++) {
            View childView = linear_pays.getChildAt(i);
            PayAway childPayAway = (PayAway) childView.getTag();
            ImageView imageView = (ImageView) childView.findViewById(R.id.iv_vipcard_select);
            if (childPayAway.getCode() == selectPayAway.getCode()) {
                imageView.setImageResource(R.mipmap.select_true);
            } else {
                imageView.setImageResource(R.mipmap.select_false);
            }
        }
        for (int i = 0; i < linear_pays_more.getChildCount(); i++) {
            View childView = linear_pays_more.getChildAt(i);
            PayAway childPayAway = (PayAway) childView.getTag();
            ImageView imageView = (ImageView) childView.findViewById(R.id.iv_vipcard_select);
            if (childPayAway.getCode() == selectPayAway.getCode()) {
                imageView.setImageResource(R.mipmap.select_true);
            } else {
                imageView.setImageResource(R.mipmap.select_false);
            }
        }
    }

    public PayAway getSelectPayCode() {
        return selectPayAway;
    }

    private int getPayIcon(int code) {
        if (code == PayTypeEnum.Zhifubao.getCode()) {
            return R.mipmap.alipay_image;
        } else if (code == PayTypeEnum.Weixin.getCode()) {
            return R.mipmap.wechant_image;
        } else if (code == PayTypeEnum.Wangyin.getCode()) {
            return R.mipmap.unpay_image;
        } else if (code == PayTypeEnum.ShangjiaYuE.getCode()) {
            return R.mipmap.app_ic_launcher;
        } else if (code == PayTypeEnum.Pingtai.getCode()) {
            return R.mipmap.app_ic_launcher;
        } else {
            return R.mipmap.bank_card_image;
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        if (reqcode == request_topay) {
            Wethod.ToFailMsg(getContext(), result);
        } else if (reqcode == request_create_order) {
            Wethod.ToFailMsg(getContext(), result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        LogUtils.i("volleyError=" + volleyError.getMessage());
    }

    public void setOnPayListener(OnPayListener listener) {
        this.onPayListener = listener;
    }

    private String payLable() {
        if (PayDealTypeEnum.Chongzhi.getCode() == dealType) {
            return PayDealTypeEnum.Chongzhi.getPayName();
        } else if (PayDealTypeEnum.Lijimaidan.getCode() == dealType) {
            return PayDealTypeEnum.Lijimaidan.getPayName();
        } else if (PayDealTypeEnum.YouhuiQuan.getCode() == dealType) {
            return PayDealTypeEnum.YouhuiQuan.getPayName();
        } else if (PayDealTypeEnum.CrowdfundingBuy.getCode() == dealType) {
            return PayDealTypeEnum.CrowdfundingBuy.getPayName();
        } else if (PayDealTypeEnum.PetroleumPay.getCode() == dealType) {
            return "";
        }
        return PayDealTypeEnum.Chongzhi.getPayName();
    }


    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public interface OnPayListener {

        /**
         * 支付完成后处理业务逻辑
         */
        void OnPayFinish();

        /**
         * 取消支付
         */
        void onPayCancel();

        /**
         * 发起银联支付
         */
        void goUnionpay(String tn);

        /**
         * 设置topay参数
         *
         * @return
         */
        Map<String, String> toPayParams(String outorderid, String payMoney);

        Map<String, String> createOrderParams();

    }

    /**
     * 显示加载动画
     */
    private void loadingShow() {

        loadingPageDialog = new LoadingPageDialog(getContext());
        loadingPageDialog.show();
//        drawable.start();
//        rl_pay_anim_show.setVisibility(VISIBLE);
//        new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message message) {
//                loadingStop();
//                return false;
//            }
//        }).sendEmptyMessageDelayed(0, 3000);
    }

    /**
     * 取消显示加载动画
     */
    private void loadingStop() {
        if (loadingPageDialog != null && loadingPageDialog.isShowing()) {
            loadingPageDialog.dismiss();
        }
//        drawable.stop();
//        rl_pay_anim_show.setVisibility(GONE);
    }

    /**
     * 根据应用包名，跳转到应用市场
     *
     * @param activity 承载跳转的Activity
     */
    public static void shareAppShop(Activity activity) {
        try {
            Uri uri = Uri.parse("market://details?id=com.chinamworld.main");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "您没有安装应用市场", Toast.LENGTH_SHORT).show();
        }
    }

}
