package com.bjypt.vipcard.activity;

import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bjypt.vipcard.utils.MD5;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/22.
 * 忘记支付密码
 */
public class ForgetPayPsdActivity extends BaseActivity implements VolleyCallBack {
    private RelativeLayout mBack;
    private TextView mGetCode;
    private EditText mForgetPhone;
    private EditText mForgetCode;
    private Button mNext;
    private EditText mForgetPsd;
    private EditText mForgetPsdOne;
    private MyTimer myTimer;
    private ImageView mStrengthImg;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_forget);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        mBack = (RelativeLayout) findViewById(R.id.forget_back);
        mGetCode = (TextView) findViewById(R.id.forget_get_code_tv);
        mForgetPhone = (EditText) findViewById(R.id.forget_phone);
        mForgetCode = (EditText) findViewById(R.id.forget_code);
        mNext = (Button) findViewById(R.id.forget_next);
        mStrengthImg = (ImageView) findViewById(R.id.forget_code_strength);
        mForgetPsd = (EditText) findViewById(R.id.forget_code_one);
        mForgetPsdOne = (EditText) findViewById(R.id.forget_code_two);
        mForgetPsd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mForgetPsdOne.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @Override
    public void afterInitView() {
        myTimer = new MyTimer(60000, 1000);

        //设置最大长度
        mForgetPsd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mForgetPsdOne.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        /**
         * 限制只能输入字母和数字，默认弹出英文输入法
         */
        mForgetPsd.setKeyListener(mListener);
        mForgetPsdOne.setKeyListener(mListener);

        //隐藏强度显示
        mStrengthImg.setVisibility(View.GONE);
    }

    DigitsKeyListener mListener=new DigitsKeyListener() {
        @Override
        public int getInputType() {
            return InputType.TYPE_TEXT_VARIATION_PASSWORD;
        }

        @Override
        protected char[] getAcceptedChars() {
            char[] data = getStringData(R.string.limit_pay).toCharArray();
            return data;
        }
    };
    public String getStringData(int id) {
        return getResources().getString(id);
    }

    @Override
    public void bindListener() {
        mGetCode.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mNext.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.forget_get_code_tv:
                //获取验证码
                if (mForgetPhone.getText().toString() == null || mForgetPhone.getText().toString().equals("")) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show();
                } else if (mForgetPhone.getText().toString().length() != 11) {
                    Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
                } else {
                    myTimer.start();
                    Map<String, String> params = new HashMap<>();

//                    params.put("phoneno",mForgetPhone.getText().toString());;
                    params.put("phoneno", getFromSharePreference(Config.userConfig.phoneno));

                    Wethod.httpPost(ForgetPayPsdActivity.this,1, Config.web.get_paypsdcode, params, this);
                }

                break;
            case R.id.forget_next:
                //下一步
                if (mForgetPhone.getText().toString() == null || mForgetPhone.getText().toString().equals("")) {
                    Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_LONG).show();
                } else if (mForgetPhone.getText().toString().length() != 11) {
                    Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
                } else if (mForgetCode.getText().toString().equals("") || mForgetCode.getText().toString() == null) {
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_LONG).show();
                } else if (mForgetPsd.getText().toString() == null || mForgetPsd.getText().toString().equals("") || mForgetPsdOne.getText().toString() == null || mForgetPsdOne.getText().toString().equals("")) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
                } else if (!mForgetPsd.getText().toString().equals(mForgetPsdOne.getText().toString())) {
                    Toast.makeText(this, "您两次输入的密码不一致", Toast.LENGTH_LONG).show();
                } else {
                    Map<String, String> params = new HashMap<>();
//                  "phoneno：电话号码
//                    paypassword：支付密码
//                    code：验证码"

                    params.put("phoneno", getFromSharePreference(Config.userConfig.phoneno));
                    try {
                        params.put("paypassword", MD5.getMd5(mForgetPsd.getText().toString(), MD5.key));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    params.put("code", mForgetCode.getText().toString());

                    Wethod.httpPost(ForgetPayPsdActivity.this,2, Config.web.reset_paypsd, params, this);
                }

                break;
            case R.id.forget_back:
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
            //获取验证码

        } else if (reqcode == 2) {
            //找回密码

            try {
                saveDataToSharePreference(Config.userConfig.paypassword, MD5.getMd5(mForgetPsd.getText().toString(), MD5.key));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            this.finish();
            Toast.makeText(this, "找回密码成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 2) {
            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    class MyTimer extends CountDownTimer {

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
            mGetCode.setClickable(false);
            mGetCode.setText("倒计时" + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            mGetCode.setClickable(true);
            mGetCode.setText("获取验证码");
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
