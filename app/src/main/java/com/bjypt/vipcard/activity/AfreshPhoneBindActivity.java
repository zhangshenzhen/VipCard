package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.AfreshPhoneRootBean;
import com.bjypt.vipcard.umeng.UmengCountContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/31
 * Use by 手机重新绑定
 */
public class AfreshPhoneBindActivity extends BaseActivity implements VolleyCallBack {
    private EditText tv_old_phone;//旧的手机号
    private EditText code;//验证码
    private TextView get_code_tv;//获取验证码
    private TextView tv_wj_phone;//忘记手机号
    private Button afresh_phone_entrue;//验证
    private String url = Config.web.change_phone;//更改旧手机号
    private String url_yanzheng = Config.web.check_old_verification_code;//验证旧手机验证码
    private Map<String, String> map;//获取验证码
    private Map<String, String> maps;//检查验证码
    private MyTimeCount timeCount;
    private ImageButton back_iv;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_afresh_phone_bind);
    }

    @Override
    public void beforeInitView() {
        map = new HashMap<>();
        maps = new HashMap<>();
    }

    @Override
    public void initView() {
        tv_old_phone = (EditText) findViewById(R.id.tv_old_phone);
        code = (EditText) findViewById(R.id.code_afresh);
        get_code_tv = (TextView) findViewById(R.id.get_code_tv);
        tv_wj_phone = (TextView) findViewById(R.id.tv_wj_phone);
        afresh_phone_entrue = (Button) findViewById(R.id.afresh_phone_entrue);
        back_iv = (ImageButton) findViewById(R.id.back_iv);
    }

    @Override
    public void afterInitView() {
        timeCount = new MyTimeCount(60000, 1000);

    }

    @Override
    public void bindListener() {
        get_code_tv.setOnClickListener(this);
        afresh_phone_entrue.setOnClickListener(this);
        tv_wj_phone.setOnClickListener(this);
        back_iv.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.get_code_tv:
                //获取手机验证码
                if ("".equals(tv_old_phone.getText().toString())) {
                    Toast.makeText(AfreshPhoneBindActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    timeCount.start();
                    map.put("phoneno", tv_old_phone.getText().toString());
                    Wethod.httpPost(AfreshPhoneBindActivity.this, 11, url, map, AfreshPhoneBindActivity.this);
                }

                break;
            case R.id.afresh_phone_entrue:
                if ("".equals(code.getText().toString())) {
                    Toast.makeText(AfreshPhoneBindActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else if ("".equals(tv_old_phone.getText().toString())) {
                    Toast.makeText(AfreshPhoneBindActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    maps.put("phoneno", tv_old_phone.getText().toString());
                    maps.put("code", code.getText().toString());
                    Wethod.httpPost(AfreshPhoneBindActivity.this, 12, url_yanzheng, maps, AfreshPhoneBindActivity.this);
                }
                //验证
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 11) {

        } else if (reqcode == 12) {
            try {
                AfreshPhoneRootBean rootBean = getConfiguration().readValue(result.toString(), AfreshPhoneRootBean.class);
                if ("Y".equals(rootBean.getResultData().getFlag())) {
                    Intent intent = new Intent(AfreshPhoneBindActivity.this, AfreshPhoneBindNextActivity.class);
                    intent.putExtra("oldPhoneno", tv_old_phone.getText().toString());
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(AfreshPhoneBindActivity.this, "验证错误", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


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

    class MyTimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyTimeCount(long millisInFuture, long countDownInterval) {
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
            get_code_tv.setText("获取验证码");

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

    @Override
    public void isConntectedAndRefreshData() {

    }
}
