package com.bjypt.vipcard.activity.shangfeng.primary.pay;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.base.BaseActivity;
import com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode;
import com.bjypt.vipcard.activity.shangfeng.common.enums.UserInformationFields;
import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.ResultDataBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.ScanQcodeDiscountBean;
import com.bjypt.vipcard.activity.shangfeng.util.OkHttpUtil;
import com.bjypt.vipcard.activity.shangfeng.util.SharedPreferencesUtils;
import com.bjypt.vipcard.activity.shangfeng.util.ToastUtils;
import com.bjypt.vipcard.activity.shangfeng.widget.PayTypeView;
import com.bjypt.vipcard.common.Config;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 支付页面
 */
public class ScanPayActivity extends BaseActivity implements PayTypeView.OnPayListener {

    @BindView(R.id.tv_order_moeny)
    TextView tv_order_moeny;
//    @BindView(R.id.tv_pay_moeny)
//    TextView tvPayMoeny;
    @BindView(R.id.tv_pay_offer)
    TextView tvPayOffer;
    @BindView(R.id.payAwayView)
    PayTypeView payIv;
    @BindView(R.id.btnPay)
    Button btnPay;
    @BindView(R.id.tv_mnname)
    TextView tv_mnname;
    //优惠金额
    @BindView(R.id.tv_order_discout)
    TextView tv_order_discout;
    //本单折扣
    @BindView(R.id.tv_order_offer)
    TextView tv_order_offer;


    ScanQcodeDiscountBean scanQcodeDiscountBean;

    private String dealtype;
    private String pkregister;
    private String barcode;

    public static void callActivity(Context context, String dealtype, String barcode) {
        Intent intent = new Intent(context, ScanPayActivity.class);
        intent.putExtra("dealtype", dealtype);
        intent.putExtra("barcode", barcode);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_pay;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void init() {
        payIv.setOnPayListener(this);
        dealtype = getIntent().getStringExtra("dealtype");
        barcode = getIntent().getStringExtra("barcode");
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payIv.startPay();
            }
        });

        initBackFinish();
        showProgress();
        getScanQcodeDiscount();
    }

    private void getScanQcodeDiscount() {
        Map<String, String> params = new HashMap<>();
        pkregister = (String) SharedPreferencesUtils.get(UserInformationFields.USER_KEY, "");
        params.put("pkregister", pkregister);
        params.put("barcode", barcode);

        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {

            @Override
            public void success(int resultCode, ResultDataBean data) {
                hideProgress();
                if (ResultCode.SUCCESS == data.getResultStatus()) {
                    Gson gson = new Gson();
                    scanQcodeDiscountBean = gson.fromJson(gson.toJson(data.getResultData()), ScanQcodeDiscountBean.class);
                    tv_order_moeny.setText(scanQcodeDiscountBean.getAmount().stripTrailingZeros().toPlainString()+"元");
                    tvPayOffer.setText(scanQcodeDiscountBean.getReal_pay_amount().stripTrailingZeros().toPlainString()+"元");
                    tv_mnname.setText(scanQcodeDiscountBean.getMuname() + "");
                    tv_order_discout.setText(scanQcodeDiscountBean.getDiscount_amount()+""+"元");
                    tv_order_offer.setText(scanQcodeDiscountBean.getDiscount_rate()+"");
                    payIv.sendHttpPayAways(Integer.parseInt(dealtype), scanQcodeDiscountBean.getPkmuser(), pkregister);
                } else {
                    ToastUtils.showToast(data.getResultData() + "");
                    finish();
                }
            }

            @Override
            public void onError(int resultCode, String errorMsg) {
                Logger.v(" resultCode : " + resultCode + " ; " + "errorMsg : " + errorMsg);
                hideProgress();
            }
        }, Config.web.SCANQCODE_DISCOUNT, params);
    }

    @Override
    public void OnPayFinish() {

//        Map<String, String> params = new HashMap<>();
//        params.put("barcode", barcode);
//        params.put("status", "3");
//        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
//
//            @Override
//            public void success(int resultCode, ResultDataBean data) {
//                ToastUtils.showToast("支付完成");
//                hideProgress();
//                finish();
//            }
//
//            @Override
//            public void onError(int resultCode, String errorMsg) {
//                Logger.v(" resultCode : " + resultCode + " ; " + "errorMsg : " + errorMsg);
//            }
//        }, Config.web.UpdateSCANPAY, params);

        ToastUtils.showToast("支付完成");
        hideProgress();
        finish();

    }

    @Override
    public Map<String, String> toPayParams(String outorderid) {
        Map<String, String> param = new HashMap<>();
        param.put("pkregister", pkregister);
        param.put("orderid", outorderid);//订单主键
        param.put("dealtype", dealtype);//业务类型 1.充值 2立即买单 3优惠券  后续待定
        param.put("amount", scanQcodeDiscountBean.getReal_pay_amount() + "");//金额
        param.put("paytype", "" + payIv.getSelectPayCode().getCode()+ "");
        param.put("paySubject", "商家收款");
        param.put("payBody", "商家收款");
//        if (StringUtil.isNotEmpty(notifyUrl)) {
//            param.put("notifyUrl", notifyUrl);
//        }
        return param;
    }

    @Override
    public Map<String, String> createOrderParams() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", pkregister);
        params.put("pkmuser", scanQcodeDiscountBean.getPkmuser());
        params.put("amount", scanQcodeDiscountBean.getAmount() + "");
        params.put("pay_type", payIv.getSelectPayCode().getCode() + "");
        params.put("barcode", barcode);
        params.put("order_type", "2");//2.用户扫码支付
        return params;
    }

    @Override
    public void onPayCancel() {

    }


}
