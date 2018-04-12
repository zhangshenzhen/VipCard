package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.ListDetailelvAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.ListDetailTypeOneBean;
import com.bjypt.vipcard.model.ListDetailTypeTwoBean;
import com.bjypt.vipcard.model.UnPayOrderBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/5.
 */
public class ListDetailsActivity extends BaseActivity implements VolleyCallBack<String> {
    private TextView tv_listdetails_tradeMoney, tv_listdetails_tradePlace,
            tv_listdetails_storeName,
            tv_listdetails_allMomty, tv_listdetails_payWay, tv_listdetails_payTime, tv_trade_Ok,
            tv_listdetails_liushuinum;
    private TextView mDiscount;
    private ListView lv_listdetails;
    private ImageView iv_listdetails_def_img, iv_trade_Ok;
    private RelativeLayout layout_back;
    private LinearLayout linear_typeOne, linear_typeTwo;

    //红色部分
    private LinearLayout mlinear_otherDetail;
    private TextView mFrontMoney;
    private TextView mCouponAndRedbag;
    private TextView mPlatframAlmost;

    private Intent mIntent;
    private ListDetailTypeOneBean mTypeOneBean;//消费
    private ListDetailTypeTwoBean mTypeTwoBean;//充值

    private ScrollView mScroll;
    private  TextView tv_listdetails_payWay_other;
    private TextView tv_listdetails_payTime_other;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_listdetails);
    }

    @Override
    public void beforeInitView() {

        mIntent = getIntent();
        Wethod.configImageLoader(this);

//        Log.i("aaa", "" + mIntent.getStringExtra("tradeLogo"));
//        Log.i("aaa",""+mIntent.getStringExtra("tradeMoney"));
//        Log.i("aaa",""+mIntent.getStringExtra("tradeName"));
//        Log.i("aaa",""+mIntent.getStringExtra("tradeType"));
//        Log.i("aaa",""+mIntent.getStringExtra("pkid"));
//        preorderId = intent.getStringExtra("preorderId");
//        pkmuser = intent.getStringExtra("pkmuser");
//        "pkid：订单主键
//        type：(1：消费；2：充值)"

        Map<String, String> params = new HashMap<>();
//        params.put("pkId", "992e22676eba4e9daa273d51309f1b87");
//        params.put("pkId", "0c466435878d4d7b801a1fbff5d64c1c");

        Log.i("aaa","id="+mIntent.getStringExtra("pkid")+">>>>type="+mIntent.getStringExtra("tradeType"));
        params.put("pkId", mIntent.getStringExtra("pkid"));
        params.put("type", mIntent.getStringExtra("tradeType"));//type 1消费  2充值
        Wethod.httpPost(ListDetailsActivity.this,1, Config.web.get_billdetailbypkid, params, this);
    }

    @Override
    public void initView() {

        iv_trade_Ok = (ImageView) findViewById(R.id.iv_trade_Ok);
        tv_trade_Ok = (TextView) findViewById(R.id.tv_trade_Ok);//是否支付成功
        tv_listdetails_tradeMoney = (TextView) findViewById(R.id.tv_listdetails_tradeMoney);//交易金额
        tv_listdetails_tradePlace = (TextView) findViewById(R.id.tv_listdetails_tradePlace);//交易地点
        tv_listdetails_storeName = (TextView) findViewById(R.id.tv_listdetails_storeName);//交易商店名
        tv_listdetails_allMomty = (TextView) findViewById(R.id.tv_listdetails_allMomty);//总计
        tv_listdetails_payWay = (TextView) findViewById(R.id.tv_listdetails_payWay);//付费方式
        tv_listdetails_payTime = (TextView) findViewById(R.id.tv_listdetails_payTime);//付费时间
        tv_listdetails_liushuinum = (TextView) findViewById(R.id.tv_listdetails_liushuinum); //流水号
        mDiscount= (TextView) findViewById(R.id.tv_discount);

        layout_back = (RelativeLayout) findViewById(R.id.layout_back);
        lv_listdetails = (ListView) findViewById(R.id.lv_listdetails);
        iv_listdetails_def_img = (ImageView) findViewById(R.id.iv_listdetails_def_img);//商家图片
        linear_typeOne = (LinearLayout) findViewById(R.id.linear_typeOne);
        linear_typeTwo = (LinearLayout) findViewById(R.id.linear_typeTwo);

        mlinear_otherDetail= (LinearLayout) findViewById(R.id.linear_otherDetail);
        mFrontMoney= (TextView) findViewById(R.id.tv_frontmoney);//定金
        mCouponAndRedbag= (TextView) findViewById(R.id.tv_couponAndredbag);//优惠券/红包/商家补贴
        mPlatframAlmost= (TextView) findViewById(R.id.tv_platformAlmost);//平台补款


    }

    @Override
    public void afterInitView() {
        if (mIntent != null) {
            ImageLoader.getInstance().displayImage(Config.web.picUrl + mIntent.getStringExtra("tradeLogo"), iv_listdetails_def_img, AppConfig.DEFAULT_IMG_OPTIONS_AD);
            //   1消费 2充值
            if (mIntent.getStringExtra("tradeType").equals("2")) {

                linear_typeOne.setVisibility(View.GONE);
                linear_typeTwo.setVisibility(View.VISIBLE);
                mlinear_otherDetail.setVisibility(View.GONE);
                tv_listdetails_tradeMoney.setText("+" + mIntent.getStringExtra("tradeMoney"));
                tv_listdetails_tradePlace.setText(mIntent.getStringExtra("tradeName") + "充值");
            } else {

                linear_typeOne.setVisibility(View.VISIBLE);
                linear_typeTwo.setVisibility(View.GONE);
                mlinear_otherDetail.setVisibility(View.VISIBLE);
                tv_listdetails_tradeMoney.setText("-" + mIntent.getStringExtra("tradeMoney"));
                tv_listdetails_tradePlace.setText(mIntent.getStringExtra("tradeName") + "消费");
                tv_listdetails_payTime.setText("消费时间：\t\t" + mIntent.getStringExtra("tradeTime"));
            }
        }

    }

    @Override
    public void bindListener() {

        layout_back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {

        switch (v.getId()) {
            case R.id.layout_back:

                this.finish();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {

        if (reqcode == 1) {
            if (mIntent.getStringExtra("tradeType").equals("2")) {  //充值

                setRecharge(result.toString());
            } else {   //消费

                setXiaofei(result.toString());
            }


        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

        Log.i("aaa", "OnFailed");
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result.toString());
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    /**
     * tradeType 为2   充值记录
     */
    public void setRecharge(String result) {

        try {
            mTypeTwoBean = getConfiguration().readValue(result.toString(), ListDetailTypeTwoBean.class);

            if (mTypeTwoBean.getResultData() != null) {

                tv_listdetails_payWay.setText("支付方式：\t\t" + mTypeTwoBean.getResultData().getRechargetype());
                tv_listdetails_payTime.setText("消费时间：\t\t" + StringUtils.setTimeFormat(mTypeTwoBean.getResultData().getCreatetime()));
                tv_listdetails_liushuinum.setText("流水号:\t\t" + mTypeTwoBean.getResultData().getIncrase());
                if (mTypeTwoBean.getResultData().getStatus().toString().equals("1")) {
                    tv_trade_Ok.setText("充值成功");
                    iv_trade_Ok.setVisibility(View.VISIBLE);
                } else {
                    tv_trade_Ok.setText("充值未完成");
                    iv_trade_Ok.setVisibility(View.GONE);
                }
            } else {

                Log.i("aaa", "return data to null!!");
            }

        } catch (IOException e) {
            Log.i("aaa", "" + e.toString());
            e.printStackTrace();
        }

    }


    /**
     * tradeType 为1   消费记录
     */
    public void setXiaofei(String result) {

        try {
            mTypeOneBean = getConfiguration().readValue(result.toString(), ListDetailTypeOneBean.class);

            if (mTypeOneBean.getResultData() != null) {
                List<UnPayOrderBean> list = new ArrayList<>();
                for (int i = 0; i < mTypeOneBean.getResultData().getGoodsDetailList().size(); i++) {
                    if (mTypeOneBean.getResultData().getGoodsDetailList().get(i).getDetailType().equals("1")) {
                        UnPayOrderBean Bean = new UnPayOrderBean();
                        Bean.setGoodsPrice(Double.parseDouble(mTypeOneBean.getResultData().getGoodsDetailList().get(i).getOrderprice()));
                        Bean.setGoodsNum(Integer.parseInt(mTypeOneBean.getResultData().getGoodsDetailList().get(i).getOrderCount()));
                        Bean.setGoodsName(mTypeOneBean.getResultData().getGoodsDetailList().get(i).getDetailName());

                        list.add(Bean);
                    }
                }
                ListDetailelvAdapter mAdapter = new ListDetailelvAdapter(list, this);
                lv_listdetails.setAdapter(mAdapter);

                if(mTypeOneBean.getResultData().getDiscount().equals("10.00"))
                    mDiscount.setText("折扣:无折扣");
                    else
                mDiscount.setText("折扣:" + mTypeOneBean.getResultData().getDiscount());

                tv_listdetails_allMomty.setText("总计:" + StringUtils.setScale(mTypeOneBean.getResultData().getOrdertotalprice()) + "元");
                if(mTypeOneBean.getResultData().getPayment_sub()==null){
                    tv_listdetails_payWay.setText("支付方式：\t\t" +setPayway(mTypeOneBean.getResultData().getPayment()));
                }else{
                    tv_listdetails_payWay.setText("支付方式：\t\t" +setPayway(mTypeOneBean.getResultData().getPayment_sub())+"+"
                            +setPayway(mTypeOneBean.getResultData().getPayment()));
                }


                mFrontMoney.setText(mTypeOneBean.getResultData().getEarnestmoney()+"/"+mTypeOneBean.getResultData().getWaitmoney());
                mCouponAndRedbag.setText(mTypeOneBean.getResultData().getCouponpay()+"/"+mTypeOneBean.getResultData().getWealpay()+"/"+mTypeOneBean.getResultData().getFavourable_merchant());
                mPlatframAlmost.setText(mTypeOneBean.getResultData().getSubsidies_system());

                tv_trade_Ok.setText("交易成功");
                iv_trade_Ok.setVisibility(View.VISIBLE);
            } else {

                Log.i("aaa", "return data to null!!");
            }

        } catch (IOException e) {
            Log.i("aaa", "" + e.toString());
            e.printStackTrace();
        }

    }

    /**
     * payway  支付方式
     */
    public String setPayway(String payStr) {

        String str = "";
        switch (payStr) {
            case "1":
                str = "支付宝";
                break;
            case "2":
                str = "财付通";
                break;
            case "3":
                str = "百度钱包";
                break;
            case "4":
                str = "网银";
                break;
            case "5":
                str = "余额支付";
                break;
            case "6":
                str = "微信支付";
                break;
            case "7":
                str = "现金支付";
                break;
            case "8":
                str = "其它支付";
                break;
            case "9":
                str = "平台余额支付";
                break;
        }
        return str;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }
}
