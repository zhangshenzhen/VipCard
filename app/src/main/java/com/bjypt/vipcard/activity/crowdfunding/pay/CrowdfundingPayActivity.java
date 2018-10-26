package com.bjypt.vipcard.activity.crowdfunding.pay;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.CrowdfundingPayFinishActivity;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.PayDealTypeEnum;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.view.ToastUtil;
import com.sinia.orderlang.utils.StringUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CrowdfundingPayActivity extends BaseActivity implements CrowdfundingPayAwayView.OnPayListener {

    private int pkprogressitemid;
    private int pkmerchantid;
    private int paytype;
    private String amount;
    private TextView tv_sum;
    private CrowdfundingPayAwayView crowdfundingPayAwayView;

    private Button btn_ok_pay;

    private String orderid;

    private static final int request_pay_result_code = 10001;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_crowdfunding_pay);
    }

    @Override
    public void beforeInitView() {
        pkprogressitemid = getIntent().getIntExtra("pkprogressitemid", 0);
        paytype = getIntent().getIntExtra("paytype", 0);
        amount = getIntent().getStringExtra("amount");
        pkmerchantid = getIntent().getIntExtra("pkmerchantid", 0);
    }

    @Override
    public void initView() {
        tv_sum = findViewById(R.id.tv_sum);
        btn_ok_pay = findViewById(R.id.btn_ok_pay);
        crowdfundingPayAwayView = findViewById(R.id.crowdfundingPayAwayView);
        findViewById(R.id.ll_pay_center_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_ok_pay.setEnabled(true);
    }

    @Override
    public void afterInitView() {
        tv_sum.setText(amount + "");
        crowdfundingPayAwayView.setOnPayListener(this);
        crowdfundingPayAwayView.setPayAway(paytype);
    }

    @Override
    protected void onResume() {
        super.onResume();
        crowdfundingPayAwayView.onResume();
    }

    @Override
    public void bindListener() {
        btn_ok_pay.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_ok_pay:
                if (crowdfundingPayAwayView.getSelectPayCode() == null) {
                    Toast.makeText(this, "请选择充值方式", Toast.LENGTH_LONG).show();
                    return;
                }
                crowdfundingPayAwayView.startPay();
                break;
        }
    }

    @Override
    public void OnPayFinish() {
//        finish();
        Intent intent = new Intent(this, CrowdfundingPayFinishActivity.class);
        intent.putExtra("orderid", orderid);
        intent.putExtra("pkmerchantid",pkmerchantid);
        startActivityForResult(intent,request_pay_result_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == request_pay_result_code){
            if(resultCode == RESULT_CANCELED){
                finish();
            }else if(resultCode == RESULT_OK){
                boolean gotoMain = data.getBooleanExtra("gotoCfMain", false);
                Intent intent = new Intent();
                intent.putExtra("gotoCfMain", gotoMain);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public void onPayCancel() {

    }

    @Override
    public Map<String, String> toPayParams(String outorderid, String payMoney) {
        orderid = outorderid;
        Map<String, String> param = new HashMap<>();
        param.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        param.put("pkmuser", pkmerchantid + "");//订单主键
        param.put("amount", amount + "");//业务类型 1.充值 2立即买单 3优惠券  后续待定
        param.put("orderid", outorderid);//金额
        param.put("dealtype", PayDealTypeEnum.CrowdfundingBuy.getCode() + "");
        param.put("paytype", crowdfundingPayAwayView.getSelectPayCode().getCode() + "");
        param.put("paySubject", "购买众筹项目");
        param.put("payBody", "购买众筹项目");
        return param;
    }

    @Override
    public Map<String, String> createOrderParams() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        params.put("pkprogressitemid", pkprogressitemid + "");
        params.put("amount", amount + "");
        params.put("pay_type", crowdfundingPayAwayView.getSelectPayCode().getCode() + "");
        params.put("order_type", "4");
        return params;
    }
}
