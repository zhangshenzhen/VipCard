package com.bjypt.vipcard.activity;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.RespBase;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.orhanobut.logger.Logger;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BandCitizenCardActivity extends BaseActivity implements VolleyCallBack<String> {

    private RelativeLayout band_citizen_card_back;                //返回
    private TextView confirm_tv;                                   //确定
    private EditText et_card_name;                                //一卡通卡号
    private EditText et_verification_code;                       //验证码
    private TextView send_vertification_code;                   //发送验证码
    private MyTimeCount timeCount;
    private Map<String, String> map;
    private static int vertification_code_request = 1101;         //请求验证码
    private static int band_citizen_card_request = 1110;          //绑卡请求
    private LinearLayout ll_phone;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_band_citizen_card);
    }

    @Override
    public void beforeInitView() {
        map = new HashMap<>();
    }

    @Override
    public void initView() {
        band_citizen_card_back = (RelativeLayout) findViewById(R.id.band_citizen_card_back);
        et_card_name = (EditText) findViewById(R.id.et_card_name);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
        send_vertification_code = (TextView) findViewById(R.id.send_vertification_code);
        confirm_tv = (TextView) findViewById(R.id.confirm_tv);


    }

    @Override
    public void afterInitView() {
        timeCount = new MyTimeCount(60000, 1000);
    }

    @Override
    public void bindListener() {
        band_citizen_card_back.setOnClickListener(this);
        send_vertification_code.setOnClickListener(this);
        confirm_tv.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.band_citizen_card_back:                       //返回
                finish();
                break;
            case R.id.send_vertification_code:                      //发送验证码
                if ("".equals(et_card_name.getText().toString())) {
                    Toast.makeText(BandCitizenCardActivity.this, "一卡通卡号不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    map.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                    map.put("cardnum", et_card_name.getText().toString());
                    Wethod.httpPost(BandCitizenCardActivity.this, vertification_code_request, Config.web.send_sms_citizen_card, map, BandCitizenCardActivity.this);
                }
                break;
            case R.id.confirm_tv:                                    //确定
                if ("".equals(et_card_name.getText().toString())) {
                    Toast.makeText(BandCitizenCardActivity.this, "一卡通卡号不能为空", Toast.LENGTH_SHORT).show();
//                } else if ("".equals(et_verification_code.getText().toString())) {
//                    Toast.makeText(BandCitizenCardActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, String> maps = new HashMap<>();
                    maps.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
//                    maps.put("smscode", et_verification_code.getText().toString());
                    maps.put("cardnum", et_card_name.getText().toString());
                    Wethod.httpPost(BandCitizenCardActivity.this, band_citizen_card_request, Config.web.band_citizen_card, maps, BandCitizenCardActivity.this);
                }
                break;

        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == band_citizen_card_request) {
            Toast.makeText(BandCitizenCardActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
            finish();
        } else if (reqcode == vertification_code_request) {
            timeCount.start();
//            Toast.makeText(BandCitizenCardActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        if (reqcode == band_citizen_card_request) {
            Logger.e("result :"+result.toString());
            Wethod.ToFailMsg(this, result.toString());
        } else if (reqcode == vertification_code_request) {
            Wethod.ToFailMsg(this, result.toString());
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
            send_vertification_code.setClickable(false);
            send_vertification_code.setText("倒计时" + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            send_vertification_code.setClickable(true);
            send_vertification_code.setText("获取验证码");

        }
    }

}
