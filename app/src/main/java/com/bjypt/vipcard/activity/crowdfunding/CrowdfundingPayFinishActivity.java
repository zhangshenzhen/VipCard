package com.bjypt.vipcard.activity.crowdfunding;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.projectdetail.entity.CfOrderInfoDataBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CommonWebData;
import com.bjypt.vipcard.activity.shangfeng.primary.commonweb.CommonWebActivity;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.PayDealTypeEnum;
import com.bjypt.vipcard.common.PayTypeEnum;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.PayAway;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.githang.statusbar.StatusBarCompat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CrowdfundingPayFinishActivity extends BaseActivity implements VolleyCallBack<String> {

    private static final int request_code_order_info = 100023;

    private ImageButton ibtn_back;
    private TextView tv_project_name;
    private TextView tv_pay_time;
    private TextView tv_pay_amout;
    private TextView tv_pay_away;
    private TextView tv_income_amount;
    private TextView tv_end_at;
    private TextView tv_ordernum;
    private Button btn_next_buy;
    private Button btn_main_page;
    private int pkmerchantid;


    @Override
    public void setContentLayout() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.cf_account_detail_head));
        setContentView(R.layout.activity_crowdfunding_payfinish);
    }

    @Override
    public void beforeInitView() {
        pkmerchantid = getIntent().getIntExtra("pkmerchantid", 0);
    }

    @Override
    public void initView() {
        ibtn_back = findViewById(R.id.ibtn_back);
        tv_project_name = findViewById(R.id.tv_project_name);
        tv_pay_time = findViewById(R.id.tv_pay_time);
        tv_pay_amout = findViewById(R.id.tv_pay_amout);
        tv_pay_away = findViewById(R.id.tv_pay_away);
        tv_income_amount = findViewById(R.id.tv_income_amount);
        tv_end_at = findViewById(R.id.tv_end_at);
        tv_ordernum = findViewById(R.id.tv_ordernum);
        btn_next_buy = findViewById(R.id.btn_next_buy);
        btn_main_page = findViewById(R.id.btn_main_page);
    }

    @Override
    public void afterInitView() {
        Map<String,String> params = new HashMap<>();
        params.put("orderid", getIntent().getStringExtra("orderid"));
        Wethod.httpPost(this, request_code_order_info, Config.web.cf_order_info, params, this, View.GONE);
    }

    @Override
    public void bindListener() {
        ibtn_back.setOnClickListener(this);
        btn_next_buy.setOnClickListener(this);
        btn_main_page.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.ibtn_back:
            case R.id.btn_next_buy:
                Intent data1 = new Intent();
                data1.putExtra("gotoCfMain", false);
                setResult(RESULT_OK,data1);
                finish();
                break;
            case R.id.btn_main_page://返回到购买记录
                Intent data = new Intent();
               // data.putExtra("gotoCfMain", true);
               // setResult(RESULT_OK, data);
               // finish();
                String params = "pkregister=" + getPkregister() + "&pkmerchantid=" + pkmerchantid;
                CommonWebData buy_record = new CommonWebData();
                buy_record.setUrl(Config.web.h5_CFBuyRecord + params);
                buy_record.setTitle("项目购买记录");
                CommonWebActivity.callActivity(this, buy_record);

                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if(reqcode == request_code_order_info){
            try {
                LogUtil.debugPrint("支付信息 = "+ result);
                CfOrderInfoDataBean orderInfoDataBean = ObjectMapperFactory.createObjectMapper().readValue(result, CfOrderInfoDataBean.class);
                if(orderInfoDataBean != null && orderInfoDataBean.getResultData() != null){
                    tv_project_name.setText("项目名称："+orderInfoDataBean.getResultData().getProjectName());
                    tv_pay_time.setText("支付时间：" + orderInfoDataBean.getResultData().getPayDate());
                    tv_pay_amout.setText("支付金额："+ orderInfoDataBean.getResultData().getPayAmount());
                    if(orderInfoDataBean.getResultData().getPayAway() == PayTypeEnum.Zhifubao.getCode()){
                        tv_pay_away.setText("支付方式：" + PayTypeEnum.Zhifubao.getPayName());
                    }else if(orderInfoDataBean.getResultData().getPayAway() == PayTypeEnum.Weixin.getCode()){
                        tv_pay_away.setText("支付方式：" + PayTypeEnum.Weixin.getPayName());
                    }else{
                        tv_pay_away.setText("支付方式：其他");
                    }
                    tv_income_amount.setText("预计收益：" + orderInfoDataBean.getResultData().getIncomeAmount());
                    tv_end_at.setText("到期时间："+ orderInfoDataBean.getResultData().getSettleEndAt());
                  //  tv_ordernum.setText("交易订单号："+ orderInfoDataBean.getResultData().getOutOrderId());
                    tv_ordernum.setText("交易订单号："+ orderInfoDataBean.getResultData().getOrderid());

                }
            } catch (IOException e) {
                e.printStackTrace();
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
