package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.DetailsNoOrderBean;
import com.bjypt.vipcard.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/6/15
 * Use by
 */
public class ListDetailsNoOrderActivity extends BaseActivity implements VolleyCallBack{

    private RelativeLayout mBack;
    private RoundImageView mHead;
    private TextView mPayMoney;
    private TextView mMerchantIntro;
    private ImageView mTradeOkIv;
    private TextView mTradeOkTv;
    private TextView mPayStyle,mPayTime;
    private DetailsNoOrderBean detailsNoOrderBean;
    private Intent mIntent;
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_detailis_no_order);
    }

    @Override
    public void beforeInitView() {
        mIntent = getIntent();

        Wethod.configImageLoader(this);
        Map<String, String> params = new HashMap<>();
        params.put("pkId", mIntent.getStringExtra("pkid"));
        params.put("type", mIntent.getStringExtra("tradeType"));//type 1消费  2充值 3.非预约支付
        Wethod.httpPost(ListDetailsNoOrderActivity.this,1, Config.web.get_billdetailbypkid, params, this);

    }

    @Override
    public void initView() {
        mBack = (RelativeLayout) findViewById(R.id.layout_back);//返回
        mHead = (RoundImageView) findViewById(R.id.iv_listdetails_def_img);//头像
        mPayMoney = (TextView) findViewById(R.id.tv_listdetails_tradeMoney);//支付金额
        mMerchantIntro = (TextView) findViewById(R.id.tv_listdetails_tradePlace);//消费简介
        mTradeOkIv = (ImageView) findViewById(R.id.iv_trade_Ok);//消费成功的图片
        mTradeOkTv = (TextView) findViewById(R.id.tv_trade_Ok);//消费成功的文字

        mPayStyle = (TextView) findViewById(R.id.tv_listdetails_payWay);//支付方式
        mPayTime = (TextView) findViewById(R.id.tv_listdetails_payTime);//付款时间
    }

    @Override
    public void afterInitView() {


    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_back:
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if(reqcode == 1){
            try {
                detailsNoOrderBean = getConfiguration().readValue(result.toString(), DetailsNoOrderBean.class);

                ImageLoader.getInstance().displayImage(Config.web.picUrl + detailsNoOrderBean.getResultData().getLogo(), mHead, AppConfig.DEFAULT_IMG_OPTIONS_AD);
                mPayMoney.setText("-" + detailsNoOrderBean.getResultData().getBalance());
                mMerchantIntro.setText(detailsNoOrderBean.getResultData().getMuname()+"消费");
                mPayStyle.setText("支付方式:  "+detailsNoOrderBean.getResultData().getPayment_desc());
                mPayTime.setText("消费时间:  "+mIntent.getStringExtra("tradeTime"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
