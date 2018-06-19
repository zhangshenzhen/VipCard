package com.bjypt.vipcard.activity.shangfeng.primary.pay;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.base.BaseActivity;
import com.bjypt.vipcard.activity.shangfeng.common.EventBusMessageEvent;
import com.bjypt.vipcard.activity.shangfeng.common.enums.EventBusWhat;
import com.bjypt.vipcard.activity.shangfeng.common.enums.UserInformationFields;
import com.bjypt.vipcard.activity.shangfeng.util.SharedPreferencesUtils;
import com.bjypt.vipcard.activity.shangfeng.util.StringUtils;
import com.bjypt.vipcard.activity.shangfeng.util.ToastUtils;
import com.bjypt.vipcard.activity.shangfeng.widget.PayTypeView;
import com.bjypt.vipcard.utils.AES;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanglei on 2018/5/18.
 */
public class H5PayActivity extends BaseActivity implements PayTypeView.OnPayListener {

    private String userId;

    public static void callActivity(Context context, String uri){
        Intent intent = new Intent(context, H5PayActivity.class);
        intent.putExtra("uri", uri);
        context.startActivity(intent);
    }

    @BindView(R.id.tv_merchant_title_name)
    TextView tvMerchantTitleName;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    @BindView(R.id.pay_iv)
    PayTypeView payIv;
    @BindView(R.id.btn_ok_pay)
    Button btnOkPay;

    private String out_trade_no;
    private String amount;
    private String payBody;
    private String paySubject;
    private String dealType;
    private String notifyUrl;
    private String callbackUrl;
    private String merchantid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_h5_pay;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void init() {
        initBackFinish();
        userId = String.valueOf(SharedPreferencesUtils.get(UserInformationFields.USER_KEY,""));

        Uri uri = Uri.parse(getIntent().getStringExtra("uri"));
        if (uri != null) {

            out_trade_no = uri.getQueryParameter("out_trade_no");
            amount = uri.getQueryParameter("amount");
            payBody = uri.getQueryParameter("payBody");
            paySubject = uri.getQueryParameter("paySubject");
            dealType = uri.getQueryParameter("dealType");
            notifyUrl = uri.getQueryParameter("notifyUrl");
            callbackUrl = uri.getQueryParameter("callbackUrl");
            merchantid = uri.getQueryParameter("merchantid");
            //验证是否有效
            String sign = uri.getQueryParameter("sign");
            if (StringUtils.isEmpty(sign)) {
                ToastUtils.showToast("支付Sign不能为空");
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
            sortedMap.put("merchantid", merchantid);
            if(!sign.equals(AES.createSign(sortedMap))){
                finish();
                ToastUtils.showToast("Sign不匹配");
                return ;
            }
            tvSum.setText(amount+"");
        }

        btnOkPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payIv.startPay();
            }
        });
        payIv.setOnPayListener(this);
        payIv.sendHttpPayAways(Integer.parseInt(dealType), "", userId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void OnPayFinish() {
        EventBusMessageEvent event = new EventBusMessageEvent();
        event.setWhat(EventBusWhat.H5pay);
        EventBus.getDefault().post(event);//CommonWebActivity
        finish();
    }

    @Override
    public Map<String, String> toPayParams(String outorderid) {
        Map<String,String> params = new HashMap<>();
        params.put("pkregister", userId);
        params.put("pkmuser",  "");
        params.put("amount", amount+"");
        params.put("orderid", out_trade_no);
        params.put("dealtype", dealType);
        params.put("paytype", payIv.getSelectPayCode().getCode()+"");
        params.put("paySubject", paySubject);
        params.put("payBody", payBody);
        if(StringUtils.isNotEmpty(notifyUrl)){
            params.put("notifyUrl", notifyUrl);
        }
        return params;
    }

    @Override
    public Map<String, String> createOrderParams() {
        return null;
    }

    @Override
    public void onPayCancel() {

    }
}
