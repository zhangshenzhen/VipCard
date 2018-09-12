package com.bjypt.vipcard.activity.crowdfunding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.util.StringUtils;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.cf.CfUserInfoData;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyCrowdfundingActivity extends BaseActivity implements VolleyCallBack {

    ImageView iv_headpic;
    ImageView iv_back;
    ImageView iv_real_name;
    TextView tv_nickname;
    Button btn_realname;

    LinearLayout linear_item_account_list;
    LinearLayout linear_item_buy_record;
    LinearLayout linear_item_bind_bank;
    LinearLayout linear_item_favolist;
    LinearLayout linear_item_request_view;
    LinearLayout linear_item_request_online;

    private static final int request_user_code = 123;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_my_crowdfunding);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        iv_headpic = (ImageView) findViewById(R.id.iv_headpic);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_real_name = (ImageView) findViewById(R.id.iv_real_name);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        linear_item_account_list = (LinearLayout) findViewById(R.id.linear_item_account_list);
        linear_item_buy_record = (LinearLayout) findViewById(R.id.linear_item_buy_record);
        linear_item_bind_bank = (LinearLayout) findViewById(R.id.linear_item_bind_bank);
        linear_item_favolist = (LinearLayout) findViewById(R.id.linear_item_favolist);
        linear_item_request_view = (LinearLayout) findViewById(R.id.linear_item_request_view);
        linear_item_request_online = (LinearLayout) findViewById(R.id.linear_item_request_online);
        btn_realname = (Button) findViewById(R.id.btn_real_name);
    }

    @Override
    public void afterInitView() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getPkregister());
        Wethod.httpPost(this, request_user_code, Config.web.getCFUserData, params, this, View.GONE);
    }

    @Override
    public void bindListener() {
        linear_item_account_list.setOnClickListener(this);
        linear_item_buy_record.setOnClickListener(this);
        linear_item_bind_bank.setOnClickListener(this);
        linear_item_favolist.setOnClickListener(this);
        linear_item_request_view.setOnClickListener(this);
        linear_item_request_online.setOnClickListener(this);
        btn_realname.setOnClickListener(this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.linear_item_account_list:
                Intent intent = new Intent(this, CrowdfundingAccountListActivity.class);
                startActivity(intent);
                break;
            case R.id.linear_item_buy_record:

                break;
            case R.id.linear_item_bind_bank:

                break;
            case R.id.linear_item_favolist:

                break;
            case R.id.linear_item_request_view:

                break;
            case R.id.linear_item_request_online:


                break;
            case R.id.btn_real_name:

                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == request_user_code) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                CfUserInfoData userInfoData = objectMapper.readValue(result.toString(), CfUserInfoData.class);
                tv_nickname.setText(userInfoData.getResultData().getNickname());
                if (userInfoData != null && userInfoData.getResultData() != null && StringUtil.isNotEmpty(userInfoData.getResultData().getPosition())) {
                    ImageLoader.getInstance().displayImage(userInfoData.getResultData().getPosition(), iv_headpic, AppConfig.CF_HEADER_GRAY);
                }
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
