package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
 * Date by 2016/4/1
 * Use by 修改密码界面(已设置过)
 */
public class ChangePasswordActivity extends BaseActivity implements VolleyCallBack {

    private EditText mOldpsd, mNewPsd, mNewPsds;
    private Button mBtn;
    private boolean PSD_TYPE = true;//修改的密码类型
    private RelativeLayout relative_oldPsd;
    private boolean isVisible = true;// 原密码是否显示
    private String oldPsd;
    private ImageButton back_iv;
    private TextView mForgetPsd;
    private ImageView mStrengthImg;
    private final int MSTRENGTH_GRADE_ONE = 7;//密码强度一
    private final int MSTRENGTH_GRADE_TWO = 9;//密码强度二
    private final int MSTRENGTH_GRADE_THREE = 11;//密码强度三
    private final int MSTRENGTH_GRADE_Four = 13;//密码强度四
    private int SAVE_GRADE = 0;
    private TextView tv_tishi;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_change_psd);
    }

    @Override
    public void beforeInitView() {
        Intent mIntent = getIntent();
        PSD_TYPE = mIntent.getBooleanExtra("psdType", true);
    }

    @Override
    public void initView() {

        mOldpsd = (EditText) findViewById(R.id.et_oldPassword);//旧密码
        mNewPsd = (EditText) findViewById(R.id.et_newPassword);//新密码
        mNewPsds = (EditText) findViewById(R.id.et_newPasswords);//密码确认
        mBtn = (Button) findViewById(R.id.btn_sureUpdate);//确认
        tv_tishi = (TextView) findViewById(R.id.tv_tishi);

        mStrengthImg = (ImageView) findViewById(R.id.forget_code_strength);

        back_iv = (ImageButton) findViewById(R.id.back_iv);
        mForgetPsd = (TextView) findViewById(R.id.tv_gotoForgetPsd);

        relative_oldPsd = (RelativeLayout) findViewById(R.id.relative_oldPsd);
    }

    @Override
    public void afterInitView() {

        if (!PSD_TYPE) {
            //判断获取的支付密码是否为空
            if (!PSD_TYPE && (getFromSharePreference(Config.userConfig.paypassword) == null ||
                    getFromSharePreference(Config.userConfig.paypassword).equals(""))) {
                relative_oldPsd.setVisibility(View.GONE);
                isVisible = false;//不显示
            }
            //隐藏提示
            /*tv_tishi.setVisibility(View.GONE);*/
            //设置最大长度
            mNewPsd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            mNewPsds.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            /**
             * 限制只能输入字母和数字，默认弹出英文输入法
             */
            mNewPsd.setKeyListener(mListener);
            mNewPsds.setKeyListener(mListener);

            //隐藏强度显示
            mStrengthImg.setVisibility(View.GONE);

            //设置点击数字键盘
            mNewPsd.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            mNewPsds.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        }

        if(!PSD_TYPE){
            mOldpsd.setHint("");
            mNewPsd.setHint("");
            mNewPsds.setHint("");
        }
    }

    DigitsKeyListener mListener = new DigitsKeyListener() {
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

        mBtn.setOnClickListener(this);
        back_iv.setOnClickListener(this);
        mForgetPsd.setOnClickListener(this);
        mNewPsd.addTextChangedListener(textWatcher);
    }

    @Override
    public void onClickEvent(View v) {

        switch (v.getId()) {
            case R.id.btn_sureUpdate:

                if (isPsdOk() == 1) {
                    /**"phoneno：电话号码
                     oldPassword：旧密码
                     newPassword：新密码
                     "
                     */
                    Map<String, String> params = new HashMap<>();
                    params.put("phoneno", getFromSharePreference(Config.userConfig.phoneno));
                    try {
                        params.put("newPassword", MD5.getMd5(mNewPsd.getText().toString(), MD5.key));
                        params.put("oldPassword", MD5.getMd5(mOldpsd.getText().toString(), MD5.key));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }


                    Wethod.httpPost(ChangePasswordActivity.this, 1, Config.web.updata_logpassword, params, this);

                } else if (isPsdOk() == 2) {

                    if (getFromSharePreference(Config.userConfig.paypassword).equals("") ||
                            getFromSharePreference(Config.userConfig.paypassword) == null) {
                         /*"phoneno：电话号码
                       paypassword:支付密码"
                      **/
                        Map<String, String> params = new HashMap<>();
                        params.put("phoneno", getFromSharePreference(Config.userConfig.phoneno));
                        try {
                            params.put("paypassword", MD5.getMd5(mNewPsd.getText().toString(), MD5.key));
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }


                        Wethod.httpPost(ChangePasswordActivity.this, 2, Config.web.add_paypassword, params, this);
                    } else {
                       /* "phoneno：电话号码
                        oldPayPassword：旧支付密码
//                        newPayPassword：新支付密码"*/

                        Map<String, String> params = new HashMap<>();
                        params.put("phoneno", getFromSharePreference(Config.userConfig.phoneno));
                        try {
                            params.put("oldPayPassword", MD5.getMd5(mOldpsd.getText().toString(), MD5.key));
                            params.put("newPayPassword", MD5.getMd5(mNewPsd.getText().toString(), MD5.key));

                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }

                        Wethod.httpPost(ChangePasswordActivity.this, 3, Config.web.updata_paypassword, params, this);
                    }

                } else {
                    Log.i("aaa", "返回的值为---->>>false!!!");
                }
                break;
            case R.id.back_iv:

                this.finish();
                break;
            case R.id.tv_gotoForgetPsd:

                Intent mIntent = new Intent();
                if (PSD_TYPE) {

                    //忘记密码--------登陆
                    mIntent.setClass(ChangePasswordActivity.this, ForgetPsdActivity.class);

                } else {

                    //忘记密码-------支付
                    mIntent.setClass(ChangePasswordActivity.this, ForgetPayPsdActivity.class);

                }
                startActivity(mIntent);
                ChangePasswordActivity.this.finish();
                break;
        }
    }

    public int isPsdOk() {

        int returnType = 0;
        if (isVisible) {
            if (mOldpsd.getText().toString().equals("") || mNewPsd.getText().toString().equals("")
                    || mNewPsds.getText().toString().equals("")) {

                toastText("用户名或密码不能为空");
            } else {

                oldPsd = "";
                if (PSD_TYPE)
                    oldPsd = getFromSharePreference(Config.userConfig.loginpassword) + "";//登陆原密码
                else
                    oldPsd = getFromSharePreference(Config.userConfig.paypassword);//支付原密码

                Log.i("aaa", "现在密码未加密=" + oldPsd);
                Log.i("aaa", "现在密码MD5=" + StringUtils.md5(oldPsd));
                Log.i("aaa", "数的密码MD5=" + StringUtils.md5(mOldpsd.getText().toString()));

                try {
                    if (MD5.getMd5(mOldpsd.getText().toString(), MD5.key).equals(oldPsd)) {

                        if (judgeLength()) {
                            if (mNewPsd.getText().toString().equals(mNewPsds.getText().toString())) {

                                if (PSD_TYPE) returnType = 1;
                                else returnType = 2;
                                return returnType;
                            } else {
                                toastText("两次的密码不一致");
                            }
                        }
                    } else {

                        toastText("输入的原密码不正确");
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (mNewPsd.getText().toString().equals("") || mNewPsds.getText().toString().equals("")) {

                toastText("用户名或密码不能为空");
            } else {
                if (judgeLength()) {
                    if (mNewPsd.getText().toString().equals(mNewPsds.getText().toString())) {

                        returnType = 2;
                        return returnType;
                    } else {
                        toastText("两次的密码不一致");
                    }
                }
            }
        }
        return returnType;
    }

    public boolean judgeLength() {
        if (PSD_TYPE) {
            if (mNewPsd.getText().toString().length() < 6 || mNewPsds.getText().toString().length() < 6) {
                toastText("密码长度必须大于6个字符");
                return false;
            }
        } else {
            if (mNewPsd.getText().toString().length() != 6 || mNewPsds.getText().toString().length() != 6) {

                toastText("密码长度必须等于6个字符");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onSuccess(int reqcode, Object result) {

        if (reqcode == 1) {
            try {
                saveDataToSharePreference(Config.userConfig.loginpassword, MD5.getMd5(mNewPsd.getText().toString(), MD5.key));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            toastText("登陆密码修改成功");
            saveDataToSharePreference(Config.userConfig.strength, SAVE_GRADE + "");
            this.finish();
        } else if (reqcode == 2) {

            try {
                saveDataToSharePreference(Config.userConfig.paypassword, MD5.getMd5(mNewPsd.getText().toString(), MD5.key));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            toastText("支付密码设置成功");
            this.finish();
        } else if (reqcode == 3) {

            try {
                saveDataToSharePreference(Config.userConfig.paypassword, MD5.getMd5(mNewPsd.getText().toString(), MD5.key));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            toastText("支付密码修改成功");
            this.finish();
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

        try {
            saveDataToSharePreference(Config.userConfig.paypassword, MD5.getMd5(mNewPsd.getText().toString(), MD5.key));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result.toString());
        } else if (reqcode == 2) {
            Wethod.ToFailMsg(this, result.toString());
        } else if (reqcode == 3) {
            Wethod.ToFailMsg(this, result.toString());
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    public void toastText(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void setStrengthImg(int sGrade) {
        SAVE_GRADE = sGrade;
        switch (sGrade) {
            case MSTRENGTH_GRADE_ONE:
                mStrengthImg.setImageResource(R.mipmap.psd_1);
                break;
            case MSTRENGTH_GRADE_TWO:
                mStrengthImg.setImageResource(R.mipmap.psd_2);
                break;
            case MSTRENGTH_GRADE_THREE:
                mStrengthImg.setImageResource(R.mipmap.psd_3);
                break;
            case MSTRENGTH_GRADE_Four:
                mStrengthImg.setImageResource(R.mipmap.psd_4);
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
            if (start >= 6) {
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
