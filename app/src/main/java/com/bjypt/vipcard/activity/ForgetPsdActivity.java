package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.bjypt.vipcard.utils.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/18
 * Use by 忘记密码界面
 */
public class ForgetPsdActivity extends BaseActivity implements VolleyCallBack {
    private ImageButton back_iv;
    private TextView mGetCode;
    private EditText mForgetPhone;
    private EditText mForgetCode;
    private TextView mNext;
    private EditText mForgetPsd;
    private EditText mForgetPsdOne;
    private MyTimer myTimer;
    private ImageView mPsdStrength;
    private final int MSTRENGTH_GRADE_ONE = 7;//密码强度一
    private final int MSTRENGTH_GRADE_TWO = 9;//密码强度二
    private final int MSTRENGTH_GRADE_THREE = 11;//密码强度三
    private final int MSTRENGTH_GRADE_Four = 13;//密码强度四
    private int SAVE_GRADE=0;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_forget);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        back_iv = (ImageButton) findViewById(R.id.back_iv);
        mGetCode = (TextView) findViewById(R.id.forget_get_code_tv);
        mForgetPhone = (EditText) findViewById(R.id.forget_phone);
        mForgetCode = (EditText) findViewById(R.id.forget_code);
        mNext = (TextView) findViewById(R.id.forget_next);
        mPsdStrength = (ImageView) findViewById(R.id.forget_code_strength);
        mForgetPsd = (EditText) findViewById(R.id.forget_code_one);
        mForgetPsdOne = (EditText) findViewById(R.id.forget_code_two);
    }

    @Override
    public void afterInitView() {
        myTimer = new MyTimer(60000, 1000);

        mForgetPsd.addTextChangedListener(textWatcher);
    }

    @Override
    public void bindListener() {
        mGetCode.setOnClickListener(this);
        back_iv.setOnClickListener(this);
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
                    params.put("phoneno", mForgetPhone.getText().toString());
                    Wethod.httpPost(ForgetPsdActivity.this,1, Config.web.get_forget_code, params, this);
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
//                    phoneno：手机号
//                    loginpassword：密码
//                    code：验证码
                    params.put("phoneno", mForgetPhone.getText().toString());
                    try {
                        params.put("loginpassword", MD5.getMd5(mForgetPsd.getText().toString(), MD5.key));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    params.put("code", mForgetCode.getText().toString());
                    Wethod.httpPost(ForgetPsdActivity.this,2, Config.web.get_password, params, this);
                }

                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {

        saveDataToSharePreference(Config.userConfig.strength,SAVE_GRADE+"");
        if (reqcode == 1) {
            //获取验证码

        } else if (reqcode == 2) {
//            Log.i("aaa","手写密码加密="+StringUtils.md5(mForgetPsd.getText().toString()));

            try {
                saveDataToSharePreference(Config.userConfig.loginpassword, MD5.getMd5(mForgetPsd.getText().toString(), MD5.key));
                saveDataToSharePreference(Config.userConfig.loginpassword,MD5.getMd5(mForgetPsd.getText().toString(), MD5.key));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            //找回密码
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result.toString());
        } else if (reqcode == 2) {
            Wethod.ToFailMsg(this, result.toString());
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

        public void setStrengthImg(int sGrade) {
            SAVE_GRADE=sGrade;
            switch (sGrade) {
                case MSTRENGTH_GRADE_ONE:
                    mPsdStrength.setImageResource(R.mipmap.psd_1);
                    break;
                case MSTRENGTH_GRADE_TWO:
                    mPsdStrength.setImageResource(R.mipmap.psd_2);
                    break;
                case MSTRENGTH_GRADE_THREE:
                    mPsdStrength.setImageResource(R.mipmap.psd_3);
                    break;
                case MSTRENGTH_GRADE_Four:
                    mPsdStrength.setImageResource(R.mipmap.psd_4);
                    break;
            }
        }

        public TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Log.d("TAG", "afterTextChanged--------------->");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Log.d("TAG", "beforeTextChanged--------------->");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Log.i("aaa","start="+start);
                if (start >= 5) {
                    setStrengthImg(StringUtils.judgePsdAllNum(s.toString()));
                }
            }
        };

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
