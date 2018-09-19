package com.bjypt.vipcard.activity.crowdfunding;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CommonWebData;
import com.bjypt.vipcard.activity.shangfeng.primary.commonweb.CommonWebActivity;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.cf.CfAccountDetailData;
import com.bjypt.vipcard.model.cf.CfAccountListData;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.githang.statusbar.StatusBarCompat;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 账户详情
 */
public class CrowdfundingAccountInfoActivity extends BaseActivity implements VolleyCallBack {

    int pkuseraccountid;
    String displaycardno;
    String cardno;
    int pkmerchantid;
    String merchant_name;
    int type_num;//vip等级
    String vip_name;//vip等级名称

    TextView tv_cardnum;
    TextView tv_vipname;
    ImageView iv_qr;
    TextView tv_totle_assets;
    TextView tv_expected_earnings;
    TextView tv_amount_of_cash;
    TextView tv_consumption_amount;

    LinearLayout linear_withdraw;
    LinearLayout linear_balance_record;
    LinearLayout linear_withdraw_record;
    LinearLayout linear_interest_record;
    LinearLayout linear_merchant_project;
    LinearLayout linear_buy_record;
    LinearLayout linear_consume_record;

    LinearLayout linear_display_amount;
    ImageView iv_amount_display;

    CfAccountDetailData cfAccountDetailData;
    String show_amount_display_key = "";


    private static final int request_code_account_data = 124;


    @Override
    public void setContentLayout() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.cf_account_detail_head));
        setContentView(R.layout.activity_crowdfunding_account_info);
    }

    @Override
    public void beforeInitView() {

        pkuseraccountid = getIntent().getIntExtra("pkuseraccountid", 0);
        displaycardno = getIntent().getStringExtra("displaycardno");
        cardno = getIntent().getStringExtra("cardno");
        merchant_name = getIntent().getStringExtra("merchant_name");
        pkmerchantid = getIntent().getIntExtra("pkmerchantid", 0);
        vip_name = getIntent().getStringExtra("vip_name");
        type_num = getIntent().getIntExtra("type_num", 1);

        show_amount_display_key = Config.userConfig.cf_display_amount_key + getPkregister() + pkmerchantid;

        if (pkuseraccountid == 0) {
            finish();
        }

    }

    @Override
    public void initView() {
        tv_cardnum = (TextView) findViewById(R.id.tv_cardnum);
        tv_vipname = (TextView) findViewById(R.id.tv_vipname);
        iv_qr = (ImageView) findViewById(R.id.iv_qr);
        tv_totle_assets = (TextView) findViewById(R.id.tv_totle_assets);
        tv_expected_earnings = (TextView) findViewById(R.id.tv_expected_earnings);
        tv_amount_of_cash = (TextView) findViewById(R.id.tv_amount_of_cash);
        tv_consumption_amount = (TextView) findViewById(R.id.tv_consumption_amount);
        linear_withdraw = (LinearLayout) findViewById(R.id.linear_withdraw);
        linear_balance_record = (LinearLayout) findViewById(R.id.linear_balance_record);
        linear_withdraw_record = (LinearLayout) findViewById(R.id.linear_withdraw_record);
        linear_interest_record = (LinearLayout) findViewById(R.id.linear_interest_record);
        linear_merchant_project = (LinearLayout) findViewById(R.id.linear_merchant_project);
        linear_buy_record = (LinearLayout) findViewById(R.id.linear_buy_record);
        linear_consume_record = (LinearLayout) findViewById(R.id.linear_consume_record);
        linear_display_amount = (LinearLayout) findViewById(R.id.linear_display_amount);
        iv_amount_display = (ImageView) findViewById(R.id.iv_amount_display);
        findViewById(R.id.ibtn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public void afterInitView() {
        tv_cardnum.setText(displaycardno);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(merchant_name);

        getAccountData();
        showAmount();

        tv_vipname.setText(vip_name);
        if (type_num == 2) {
            tv_vipname.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.cf_vip_level_2), null, null, null);
        } else if (type_num == 3) {
            tv_vipname.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.cf_vip_level_3), null, null, null);
        } else {
            tv_vipname.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.cf_vip_level_1), null, null, null);
        }
    }

    private void getAccountData() {
        Map<String, String> params = new HashMap<>();
        params.put("pkmerchantid", pkmerchantid + "");
        params.put("pkregister", getPkregister());
        Wethod.httpPost(this, request_code_account_data, Config.web.getCFAccountInfo, params, this, View.GONE);
    }

    @Override
    public void bindListener() {
        iv_qr.setOnClickListener(this);
        linear_withdraw.setOnClickListener(this);
        linear_balance_record.setOnClickListener(this);
        linear_withdraw_record.setOnClickListener(this);
        linear_interest_record.setOnClickListener(this);
        linear_merchant_project.setOnClickListener(this);
        linear_buy_record.setOnClickListener(this);
        linear_consume_record.setOnClickListener(this);
        linear_display_amount.setOnClickListener(this);

    }

    @Override
    public void onClickEvent(View v) {
        String params = "pkregister=" + getPkregister() + "&pkmerchantid=" + pkmerchantid;
        switch (v.getId()) {
            case R.id.iv_qr:
                Intent intent = new Intent(this, CrowdfundingQRPayActivity.class);
                intent.putExtra("pkmerchantid", pkmerchantid);
                intent.putExtra("pkregister", getPkregister());
                intent.putExtra("pkmuser","" );
                startActivity(intent);
                break;
            case R.id.linear_withdraw:
                CommonWebData withdraw = new CommonWebData();
                withdraw.setTitle("我要提现");
                withdraw.setUrl(Config.web.h5_CFWithdraw + params);
                CommonWebActivity.callActivity(this, withdraw);
                break;
            case R.id.linear_balance_record:
                CommonWebData balance_record = new CommonWebData();
                balance_record.setTitle("余额记录");
                balance_record.setUrl(Config.web.h5_CFBalanceRecord + params);
                CommonWebActivity.callActivity(this, balance_record);
                break;
            case R.id.linear_withdraw_record:
                CommonWebData withdraw_record = new CommonWebData();
                withdraw_record.setTitle("提现记录");
                withdraw_record.setUrl(Config.web.h5_CFWithdrawRecord + params);
                CommonWebActivity.callActivity(this, withdraw_record);
                break;
            case R.id.linear_interest_record:
                CommonWebData interest_record = new CommonWebData();
                interest_record.setTitle("项目收益记录");
                interest_record.setUrl(Config.web.h5_CFProjectInterest + params + "&pkuseraccountid" + pkuseraccountid);
                CommonWebActivity.callActivity(this, interest_record);
                break;
            case R.id.linear_merchant_project:
                Intent intent2 = new Intent(this, CrowdfundingSellerProjectActivity.class);
                intent2.putExtra("pkmerchantid", pkmerchantid);
                startActivity(intent2);
                break;
            case R.id.linear_buy_record:
                CommonWebData buy_record = new CommonWebData();
                buy_record.setUrl(Config.web.h5_CFBuyRecord + params);
                buy_record.setTitle("项目购买记录");
                CommonWebActivity.callActivity(this, buy_record);
                break;
            case R.id.linear_consume_record:
                CommonWebData consume_record = new CommonWebData();
                consume_record.setTitle("会员消费记录");
                consume_record.setUrl(Config.web.h5_CFConsumeRecord + params);
                CommonWebActivity.callActivity(this, consume_record);
                break;
            case R.id.linear_display_amount:
                String is_show_display = SharedPreferenceUtils.getFromSharedPreference(this, show_amount_display_key);
                if (is_show_display.equals("close")) {
                    SharedPreferenceUtils.saveToSharedPreference(this, show_amount_display_key, "open");
                } else {
                    SharedPreferenceUtils.saveToSharedPreference(this, show_amount_display_key, "close");
                }
                showAmount();
                break;
        }
    }

    private void showAmount() {
        String mash = "********";
        String is_show_display = SharedPreferenceUtils.getFromSharedPreference(this, show_amount_display_key);
        if (is_show_display.equals("open")) {
            iv_amount_display.setImageDrawable(getResources().getDrawable(R.mipmap.cf_amount_display_open));
            if (cfAccountDetailData != null) {
                iv_amount_display.setImageDrawable(getResources().getDrawable(R.mipmap.cf_amount_display_open));
                SharedPreferenceUtils.saveToSharedPreference(this, show_amount_display_key, "open");
                tv_amount_of_cash.setText(cfAccountDetailData.getResultData().getAmount_of_cash() + "");
                tv_totle_assets.setText(cfAccountDetailData.getResultData().getTotle_assets() + "");
                tv_consumption_amount.setText(cfAccountDetailData.getResultData().getConsumption_amount() + "");
                tv_expected_earnings.setText(cfAccountDetailData.getResultData().getExpected_earnings() + "");
            }
        } else {
            iv_amount_display.setImageDrawable(getResources().getDrawable(R.mipmap.cf_amount_display_close));
            tv_amount_of_cash.setText(mash);
            tv_consumption_amount.setText(mash);
            tv_expected_earnings.setText(mash);
            tv_totle_assets.setText(mash);
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == request_code_account_data) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                cfAccountDetailData = objectMapper.readValue(result.toString(), CfAccountDetailData.class);

                showAmount();
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
