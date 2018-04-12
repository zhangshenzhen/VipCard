package com.bjypt.vipcard.activity;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.NewsRootBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by 崔龙 Date by 2017/3/23
 */
public class NewsActivity extends BaseActivity implements VolleyCallBack {

    private LinearLayout ly_news_back;
    private LinearLayout ly_merchant_coupon_info;
    private LinearLayout ly_merchant_activitys;
    private LinearLayout sys_news;
    private TextView tv_time_merchant_counpon_info;
    private TextView tv_time_merchant_activitys;
    private TextView tv_time_merchant_sys;
    private String url;
    private Map<String, String> map;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_news);
    }

    @Override
    public void beforeInitView() {
        list_time = new ArrayList<>();


    }

    @Override
    public void initView() {
        ly_news_back = (LinearLayout) findViewById(R.id.ly_news_back);
        ly_merchant_coupon_info = (LinearLayout) findViewById(R.id.ly_merchant_coupon_info);
        ly_merchant_activitys = (LinearLayout) findViewById(R.id.ly_merchant_activitys);
        sys_news = (LinearLayout) findViewById(R.id.sys_news);
        tv_time_merchant_counpon_info = (TextView) findViewById(R.id.tv_time_merchant_counpon_info);
        tv_time_merchant_activitys = (TextView) findViewById(R.id.tv_time_merchant_activitys);
        tv_time_merchant_sys = (TextView) findViewById(R.id.tv_time_merchant_sys);

    }

    @Override
    public void afterInitView() {
        url = Config.web.push_message_main;
        map = new HashMap<>();
        map.put("userId",getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(this,11, url, map, this);
    }

    @Override
    public void bindListener() {
        ly_news_back.setOnClickListener(this);
        ly_merchant_coupon_info.setOnClickListener(this);
        ly_merchant_activitys.setOnClickListener(this);
        sys_news.setOnClickListener(this);

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            /**
             * 返回
             */
            case R.id.ly_news_back:
                finish();
                break;
            /**
             * 商家优惠信息
             */
            case R.id.ly_merchant_coupon_info:
                //                startActivity(new Intent(NewsActivity.this, CouponActivity.class));
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent_coupon = new Intent(this, InformationActivity.class);
                    intent_coupon.putExtra("type", "2");
                    startActivity(intent_coupon);
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            /**
             * 商家活动
             */
            case R.id.ly_merchant_activitys:
                //                startActivity(new Intent(NewsActivity.this, RedPacketActivity.class));
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent_activity = new Intent(this, InformationActivity.class);
                    intent_activity.putExtra("type", "0");
                    startActivity(intent_activity);
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            /**
             * 系统提醒
             */
            case R.id.sys_news:
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent_news = new Intent(this, InformationActivity.class);
                    intent_news.putExtra("type", "3");
                    startActivity(intent_news);
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;

        }

    }

    private List<String> list_time;

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 11) {
            Log.e("liyunteee", result.toString());
            try {
                NewsRootBean rootBean = getConfiguration().readValue(result.toString(), NewsRootBean.class);
                for (int i = 0; i < rootBean.getResultData().size(); i++) {
                    list_time.add(rootBean.getResultData().get(i).getLatestest_time());
                    if ("0".equals(rootBean.getResultData().get(i).getType())) {
                        tv_time_merchant_activitys.setText(rootBean.getResultData().get(i).getLatestest_time());
                    } else if ("3".equals(rootBean.getResultData().get(i).getType())) {
                        tv_time_merchant_sys.setText(rootBean.getResultData().get(i).getLatestest_time());
                    } else if ("2".equals(rootBean.getResultData().get(i).getType())) {
                        tv_time_merchant_counpon_info.setText(rootBean.getResultData().get(i).getLatestest_time());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFailed(int reqcode, Object result) {
//        if (reqcode == 11) {
//            Wethod.ToFailMsg(this, result);
//        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
