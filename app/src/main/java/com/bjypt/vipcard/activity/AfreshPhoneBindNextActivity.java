package com.bjypt.vipcard.activity;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.umeng.UmengCountContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/31
 * Use by 手机重新绑定
 */
public class AfreshPhoneBindNextActivity extends BaseActivity implements VolleyCallBack {
    private EditText tv_old_phone;//旧的手机号
    private EditText code;//验证码
    private TextView get_code_tv;//获取验证码
    private TextView afresh_phone_entrue;//验证
    private RelativeLayout afresh_phone_back;//返回
    private String url = Config.web.change_phone;//更改新手机号
    private String update_phone = Config.web.update_phone;//验证新手机
    private Map<String, String> map;//获取验证码
    private Map<String, String> maps;//检查验证码
    private String oldPhoneno;
    private MyTime myTime;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_afresh_phone_bind_next);
    }

    @Override
    public void beforeInitView() {
        oldPhoneno = getIntent().getStringExtra("oldPhoneno");
        map = new HashMap<>();
        maps = new HashMap<>();
    }

    @Override
    public void initView() {
        tv_old_phone = (EditText) findViewById(R.id.tv_old_phone_next);
        code = (EditText) findViewById(R.id.code_next);
        get_code_tv = (TextView) findViewById(R.id.get_code_tv_next);
        afresh_phone_entrue = (TextView) findViewById(R.id.afresh_phone_entrue_next);
        afresh_phone_back = (RelativeLayout) findViewById(R.id.afresh_phone_back_next);
    }

    @Override
    public void afterInitView() {
        myTime = new MyTime(60000, 1000);

    }

    @Override
    public void bindListener() {
        get_code_tv.setOnClickListener(this);
        afresh_phone_entrue.setOnClickListener(this);
        afresh_phone_back.setOnClickListener(this);

    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.afresh_phone_back_next:
                finish();
                break;
            case R.id.get_code_tv_next:
                //获取手机验证码
                if ("".equals(tv_old_phone.getText().toString())) {
                    Toast.makeText(AfreshPhoneBindNextActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    myTime.start();
                    map.put("phoneno", tv_old_phone.getText().toString());
                    Wethod.httpPost(AfreshPhoneBindNextActivity.this, 11, url, map, AfreshPhoneBindNextActivity.this);
                }

                break;
            case R.id.afresh_phone_entrue_next:
                if ("".equals(code.getText().toString())) {
                    Toast.makeText(AfreshPhoneBindNextActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else if ("".equals(tv_old_phone.getText().toString())) {
                    Toast.makeText(AfreshPhoneBindNextActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    maps.put("phoneno", tv_old_phone.getText().toString());
                    maps.put("code", code.getText().toString());
                    maps.put("oldPhoneno", oldPhoneno);
                    Wethod.httpPost(AfreshPhoneBindNextActivity.this, 12, update_phone, maps, AfreshPhoneBindNextActivity.this);
                }
                //验证
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 11) {
        } else if (reqcode == 12) {
            Toast.makeText(AfreshPhoneBindNextActivity.this, "手机号更换成功", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 11) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 12) {
            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    class MyTime extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            get_code_tv.setClickable(false);
            get_code_tv.setText("倒计时" + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            get_code_tv.setClickable(true);
            get_code_tv.setText("验证倒计时");

        }
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
