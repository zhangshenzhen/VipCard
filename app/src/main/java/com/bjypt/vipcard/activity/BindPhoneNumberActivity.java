package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.LoginData;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 崔龙 on 2017/11/1.
 * <p>
 * 绑定手机界面
 */

public class BindPhoneNumberActivity extends BaseActivity implements VolleyCallBack {

    private static final int GET_VERIFICATION_CODE = 201711111;
    private static final int REQUEST_SEND_PHONENO = 2017;
    private ImageButton citizen_card_back;
    private Button btn_ok_bind;
    private TextView yan_zheng_number;
    private EditText login_password;
    private EditText login_name;
    private PopupWindow popupWindow;
    private int screenWidth;
    private int screenHeigh;
    private MyTimer myTimer;
    private TextView stringPop;
    private TextView canclePop;
    private TextView textview_back;
    private LinearLayout relative_pop;
    private String openId;
    private String loginType;
    private String headImgUrl;
    private String nickName;

    @Override

    public void setContentLayout() {
        setContentView(R.layout.activity_bind_number);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        openId = intent.getStringExtra("openId");
        loginType = intent.getStringExtra("loginType");
        headImgUrl = intent.getStringExtra("headImgUrl");
        nickName = intent.getStringExtra("nickName");
    }

    @Override
    public void initView() {
        citizen_card_back = (ImageButton) findViewById(R.id.citizen_card_back);
        yan_zheng_number = (TextView) findViewById(R.id.yan_zheng_number);
        relative_pop = (LinearLayout) findViewById(R.id.relative_pop);
        login_password = (EditText) findViewById(R.id.login_password);
        login_name = (EditText) findViewById(R.id.login_name);
        btn_ok_bind = (Button) findViewById(R.id.btn_ok_bind);
        btn_ok_bind.setClickable(false);
    }

    @Override
    public void afterInitView() {
        myTimer = new MyTimer(60000, 1000);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;
    }

    @Override
    public void bindListener() {
        citizen_card_back.setOnClickListener(this);
        yan_zheng_number.setOnClickListener(this);
        btn_ok_bind.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.citizen_card_back:
                finish();
                break;
            case R.id.yan_zheng_number:
                giveMessage();
                break;
            case R.id.btn_ok_bind:
                okGoBind();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case GET_VERIFICATION_CODE:
                break;
            case REQUEST_SEND_PHONENO:
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    LoginData loginData = objectMapper.readValue(result.toString(), LoginData.class);
                    saveDataToSharePreference(Config.userConfig.phoneno, loginData.getResultData().getPhoneno());
                    saveDataToSharePreference(Config.userConfig.loginpassword, loginData.getResultData().getLoginpassword());
                    saveDataToSharePreference(Config.userConfig.pkregister, loginData.getResultData().getPkregister());
                    saveDataToSharePreference(Config.userConfig.nickname, loginData.getResultData().getNickname());
                    saveDataToSharePreference(Config.userConfig.paypassword, loginData.getResultData().getPaypassword());
                    saveDataToSharePreference(Config.userConfig.position, loginData.getResultData().getPosition());
                    saveDataToSharePreference(Config.userConfig.nativeLoginPassword, loginData.getResultData().getNativeLoginPassword());
                    saveDataToSharePreference(Config.userConfig.uid, loginData.getResultData().getUid());
                    saveDataToSharePreference(Config.userConfig.is_Login, "Y");
                    BindPhoneNumberActivity.this.finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    /**
     * 执行绑定的一些操作
     */
    private void okGoBind() {
        if (!login_password.getText().toString().trim().equals("") && login_password.getText().toString().trim().length() > 1) {
            btn_ok_bind.setClickable(true);
//            ToastUtil.showToast(this, "ok");
            String yanzheng = login_password.getText().toString().trim();
            Map<String, String> params = new HashMap<>();
            params.put("phoneno", login_name.getText().toString());
            params.put("openId", openId);
            params.put("code", yanzheng);
            params.put("loginType", loginType);
            params.put("headImgUrl", headImgUrl);
            params.put("nickName", nickName);
            Wethod.httpPost(BindPhoneNumberActivity.this, REQUEST_SEND_PHONENO, Config.web.send_phoneno, params, this);
        } else {
            btn_ok_bind.setClickable(false);
            popWindowView("请输入验证码");
        }
    }

    /**
     * 点击确定按钮所做的操作
     */
    private void giveMessage() {
        if (login_name.getText().toString() == null || login_name.getText().toString().equals("")) {
            popWindowView("请输入手机号码");
        } else if (login_name.getText().toString().length() != 11) {
            popWindowView("请输入正确的手机号码");
        } else {
            myTimer.start();
            Map<String, String> params = new HashMap<>();
            params.put("phoneno", login_name.getText().toString());
            Wethod.httpPost(BindPhoneNumberActivity.this, GET_VERIFICATION_CODE, Config.web.get_bind_sms, params, this);
        }
    }

    private class MyTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            yan_zheng_number.setText("倒计时" + millisUntilFinished / 1000);
            yan_zheng_number.setClickable(false);
        }

        @Override
        public void onFinish() {
            yan_zheng_number.setText("获取验证码");
            yan_zheng_number.setClickable(true);
        }
    }

    public void popWindowView(String text) {
        View view = View.inflate(BindPhoneNumberActivity.this, R.layout.layout_register_popwindow, null);
        popupWindow = new PopupWindow(view, screenWidth, screenHeigh);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        stringPop = (TextView) view.findViewById(R.id.tv_toast);
        canclePop = (TextView) view.findViewById(R.id.textview_cancle);
        textview_back = (TextView) view.findViewById(R.id.textview_back);
        stringPop.setText(text);
        canclePop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        textview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(relative_pop, Gravity.BOTTOM, 0, 0);
    }
}
