package com.bjypt.vipcard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.activity.HuiyuanbiRecordActivity;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.activity.NewTopupWayActivity;
import com.bjypt.vipcard.activity.RechargeAccountActivity;
import com.bjypt.vipcard.activity.RechargeRecordActivity;
import com.bjypt.vipcard.activity.SystemSettingActivity;
import com.bjypt.vipcard.activity.UserRecordActivity;
import com.bjypt.vipcard.activity.WithdrawActivity;
import com.bjypt.vipcard.adapter.AccountGridViewAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.orhanobut.logger.Logger;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bjypt.vipcard.common.Config.userConfig.pkregister;

/**
 * 账户余额
 */
public class AccountRemainingSumActivity extends BaseActivity implements AdapterView.OnItemClickListener, VolleyCallBack<String> {

    @BindView(R.id.ibtn_back)
    ImageButton ibtn_back;
    @BindView(R.id.ibtn_is_show_money)
    ImageView ibtn_is_show_money;
    @BindView(R.id.gv_item)
    GridView gv_item;

    @BindView(R.id.tv_my_zhanghu_yu_e)
    TextView tv_my_zhanghu_yu_e;
    @BindView(R.id.tv_my_vip_bi)
    TextView tv_my_vip_bi;


    private String IS_SHOW_MONEY = getFromSharePreference(Config.userConfig.phoneno) + "_money_ishow";

    private SystemInfomationBean systemInfomationBean;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    boolean state = getData(IS_SHOW_MONEY);
                    if (state) {
                        String menoy = systemInfomationBean.getResultData().getBalance();
                        tv_my_zhanghu_yu_e.setText(menoy.replaceAll(".", "*"));
                        ibtn_is_show_money.setSelected(state);

                    } else {
                        tv_my_zhanghu_yu_e.setText(systemInfomationBean.getResultData().getBalance());
                        ibtn_is_show_money.setSelected(state);

                    }
                    String testStr = getResources().getString(R.string.my_integral);
                    String result = String.format(testStr, sub(stringToDouble(systemInfomationBean.getResultData().getWholebalance()), stringToDouble(systemInfomationBean.getResultData().getBalance())) + "");
                    tv_my_vip_bi.setText(result);
                    break;
            }
        }
    };

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_account_remaining_sum);
        ButterKnife.bind(this);
        IS_SHOW_MONEY = getFromSharePreference(Config.userConfig.phoneno) + IS_SHOW_MONEY;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {

        gv_item.setOnItemClickListener(this);
    }

    @Override
    public void afterInitView() {
        if (!contain(IS_SHOW_MONEY)) {
            saveData(IS_SHOW_MONEY, false);
        }
        AccountGridViewAdapter adapter = new AccountGridViewAdapter(this);
        gv_item.setAdapter(adapter);

    }

    @OnClick({R.id.ibtn_back, R.id.ibtn_is_show_money})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_back:
                finish();
                break;

            case R.id.ibtn_is_show_money:
                ibtn_is_show_money.setSelected(!ibtn_is_show_money.isSelected());

                if (ibtn_is_show_money.isSelected()) {
                    String menoy = tv_my_zhanghu_yu_e.getText().toString();
                    tv_my_zhanghu_yu_e.setText(menoy.replaceAll(".", "*"));
                } else {
                    tv_my_zhanghu_yu_e.setText(systemInfomationBean.getResultData().getBalance());

                }
                Logger.e(IS_SHOW_MONEY + " : " + ibtn_is_show_money.isSelected());
                saveData(IS_SHOW_MONEY, ibtn_is_show_money.isSelected());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(pkregister));
        Wethod.httpPost(this, 1, Config.web.system_infomatiob, params, this);
    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:  // 余额记录
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    startActivity(new Intent(this, UserRecordActivity.class));
                } else {
                    startLogin();
                }
                break;
            case 1:  // 银行卡
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent3 = new Intent(this, NewBindBankCardActivity.class);
                    startActivity(intent3);
                } else {
                    startLogin();
                }
                break;

            case 2:  // 积分记录
                startActivity(new Intent(this, HuiyuanbiRecordActivity.class));
                break;
            case 3:   // 我提现
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent2 = new Intent(this, WithdrawActivity.class);
                    startActivity(intent2);
                } else {
                    startLogin();
                }

                break;
            case 4:  // 我充值
                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                    Intent intent4 = new Intent(this, NewTopupWayActivity.class);
                    intent4.putExtra("FLAG", 2);
                    intent4.putExtra("pkmuser", systemInfomationBean.getResultData().getPkmuser());
                    startActivity(intent4);
                } else {
                    startLogin();
                }
                break;
        }
    }

    public void startLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 1) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                systemInfomationBean = objectMapper.readValue(result.toString(), SystemInfomationBean.class);

                mHandler.sendEmptyMessage(1);

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

    public double sub(double v1, double v2) {
        if (v1 == v2) {
            return 0.00;
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        String value = b1.subtract(b2).doubleValue() + "";
        if (value.contains(".")) {
            if (value.substring(value.indexOf("."), value.length()).length() > 3) {
                return Double.parseDouble(value.substring(0, value.indexOf(".") + 3));
            }
        }
        return Double.parseDouble(value);
    }

    public double stringToDouble(String value) {
        String valusePiont = value.substring(value.indexOf("."), value.length());
        if (value != null && !value.equals("")) {
            double val;
            if (value.contains(".") && valusePiont.length() >= 3) {
                val = Double.parseDouble(value.substring(0, value.indexOf(".") + 3));
            } else {
                val = Double.parseDouble(value);
            }
            return val;
        }

        return 0.00;

    }

    private void saveData(String key, boolean value) {
        SharedPreferences sp = getSharedPreferences("hyb", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private boolean getData(String key) {
        SharedPreferences sp = getSharedPreferences("hyb", MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public Boolean contain(String key) {
        return getSharedPreferences("hyb", MODE_PRIVATE).contains(key);
    }

}
