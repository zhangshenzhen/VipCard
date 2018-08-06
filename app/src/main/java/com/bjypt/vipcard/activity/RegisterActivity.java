package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.RespBase;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.Constant;
import com.bjypt.vipcard.model.LoginData;
import com.bjypt.vipcard.service.JpushService;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.MD5;
import com.bjypt.vipcard.utils.PhoneCpuId;
import com.bjypt.vipcard.utils.StringUtils;
import com.bjypt.vipcard.view.LoadingPageDialog;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/18
 * Use by 注册
 */
public class RegisterActivity extends BaseActivity implements VolleyCallBack {
    private LinearLayout mBack;
    private TextView mGetCode;
    private EditText mRegisterPhone, mRegisterPsd, mRegisterCode, mRegisterPsdAgain;
    private Button mTrue;
    private MyTimer myTimer;
    //    private LinearLayout cb_isAgree;
    private TextView tv_protocol;
        private boolean isAgree = true;
    private boolean isClick = true;
    //    private ImageView cb_img;
    private Intent Intentintent;
    private String sre = "";
    private PopupWindow popupWindow;
    private int screenWidth;
    private int screenHeigh;
    private TextView stringPop;
    private TextView canclePop;
    private TextView textview_back;
    private LinearLayout relative_pop;
    private WebView webView;
    LoadingPageDialog loadingPageDialog;
    private ImageButton iv_agreed;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_register_new);
        loadingPageDialog = new LoadingPageDialog(this);
    }

    @Override
    public void beforeInitView() {
        myTimer = new MyTimer(60000, 1000);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;
    }

    @Override
    public void initView() {
        iv_agreed = (ImageButton) findViewById(R.id.iv_agreed);
        iv_agreed.setSelected(true);
        relative_pop = (LinearLayout) findViewById(R.id.relative_pop);
        mBack = (LinearLayout) findViewById(R.id.register_back);//返回键
        mGetCode = (TextView) findViewById(R.id.register_get_code_tv);//获取验证码
        webView = (WebView) findViewById(R.id.webView);
        mRegisterPhone = (EditText) findViewById(R.id.register_phone_num);//手机号码
        mRegisterCode = (EditText) findViewById(R.id.register_code);//输入的验证码
        mRegisterPsd = (EditText) findViewById(R.id.register_psd);//密码
        mRegisterPsd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mRegisterPsdAgain = (EditText) findViewById(R.id.register_psd_one);//再次输入密码
        mRegisterPsdAgain.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mTrue = (Button) findViewById(R.id.register_entrue);//确认
//        cb_isAgree = (LinearLayout) findViewById(R.id.cb_isAgree);//协议选择
//        cb_img = (ImageView) findViewById(R.id.cb_img);
        tv_protocol = (TextView) findViewById(R.id.tv_protocol);//协议内容
        webView = (WebView) findViewById(R.id.webView);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSupportMultipleWindows(true);
        ws.setGeolocationEnabled(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setGeolocationDatabasePath(getFilesDir().getPath());
        webView.requestFocus();
    }

    @Override
    public void afterInitView() {
        mRegisterPhone.addTextChangedListener(textWatcher);
        mRegisterCode.addTextChangedListener(textWatcher);
        mRegisterPsd.addTextChangedListener(textWatcher);
        mRegisterPsdAgain.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!mRegisterCode.getText().toString().equals("") && !mRegisterPhone.getText().toString().equals("")
                    && !mRegisterPsd.getText().toString().equals("") && !mRegisterPsdAgain.getText().toString().equals("")) {
                mTrue.setBackgroundResource(R.drawable.background_bindbank_confirm);
                mTrue.setClickable(true);
            } else {
                mTrue.setBackgroundResource(R.drawable.background_register);
                mTrue.setClickable(false);
            }
        }
    };

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
        mGetCode.setOnClickListener(this);
        mTrue.setOnClickListener(this);
        tv_protocol.setOnClickListener(this);
        iv_agreed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_agreed.setSelected(!iv_agreed.isSelected());
            }
        });

    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.register_back:
                finish();
                break;
            case R.id.register_get_code_tv:
                if (mRegisterPhone.getText().toString() == null || mRegisterPhone.getText().toString().equals("")) {
                    popWindowView("请输入手机号码");
                } else if (mRegisterPhone.getText().toString().length() != 11) {
                    popWindowView("请输入正确的手机号码");
                } else {
                    myTimer.start();
                    Map<String, String> params = new HashMap<>();
                    params.put("phoneno", mRegisterPhone.getText().toString());
                    Wethod.httpPost(RegisterActivity.this, 1, Config.web.get_verification_code, params, this);
                }

                break;
            case R.id.register_entrue:
                if (mRegisterPhone.getText().toString() == null || mRegisterPhone.getText().toString().equals("")) {
                    popWindowView("请输入手机号码");
                } else if (mRegisterPhone.getText().toString().length() != 11) {
                    popWindowView("请输入正确的手机号码");
                } else if (mRegisterCode.getText().toString() == null || mRegisterCode.getText().toString().equals("")) {
                    popWindowView("请输入验证码");
                } else if (mRegisterPsd.getText().toString() == null || mRegisterPsd.getText().toString().equals("")) {
                    popWindowView("请输入密码");
                } else if (mRegisterPsdAgain.getText().toString() == null || mRegisterPsdAgain.getText().toString().equals("")) {
                    popWindowView("请再次输入密码");
                } else if (!mRegisterPsd.getText().toString().equals(mRegisterPsdAgain.getText().toString())) {
                    popWindowView("您两次输入的密码不一致");
                } else if (!iv_agreed.isSelected()) {
                    Toast.makeText(this, "您尚未同意注册协议", Toast.LENGTH_LONG).show();
                } else {
                    //调用注册接口
//                    phoneno：手机号
//                    loginpassword：密码
//                    code：验证码
                    Map<String, String> registerParams = new HashMap<>();
                    registerParams.put("phoneno", mRegisterPhone.getText().toString());
                    registerParams.put("cpuid", PhoneCpuId.getDeviceId(this));
                    try {
                        registerParams.put("loginpassword", MD5.getMd5(mRegisterPsd.getText().toString(), MD5.key));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    registerParams.put("code", mRegisterCode.getText().toString());
                    Wethod.httpPost(RegisterActivity.this, 2, Config.web.register_, registerParams, this);
                }
                break;
            case R.id.tv_protocol:
                //跳转到协议
                Intent intent = new Intent(RegisterActivity.this, RegisterProtocolActivity.class);
                startActivityForResult(intent, Constant.ResultCode_ProtocolActivity);
                break;
        }
    }

    public void popWindowView(String text) {
        View view = View.inflate(RegisterActivity.this, R.layout.layout_register_popwindow, null);
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

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {

        } else if (reqcode == 2) {
            popWindowView("注册成功");
            Map<String, String> params = new HashMap<>();
            params.put("phoneno", mRegisterPhone.getText().toString());
            params.put("cpuid", PhoneCpuId.getDeviceId(this));
            try {
                params.put("loginpassword", MD5.getMd5(mRegisterPsd.getText().toString(), MD5.key));
                params.put("nativeLoginPassword", mRegisterPsd.getText().toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            loadingPageDialog.show();
            loadingPageDialog.setMsg("正在登陆...");
            Wethod.httpPost(RegisterActivity.this, 1234, Config.web.login_, params, this);
        } else if (reqcode == 1234) {
            try {
                LoginData loginData = getConfiguration().readValue(result.toString(), LoginData.class);
                /********************
                 *  将登录后返回的数据存入本地
                 ********************/
                saveDataToSharePreference(Config.userConfig.phoneno, loginData.getResultData().getPhoneno());
                saveDataToSharePreference(Config.userConfig.loginpassword, loginData.getResultData().getLoginpassword());
                saveDataToSharePreference(Config.userConfig.pkregister, loginData.getResultData().getPkregister());
                saveDataToSharePreference(Config.userConfig.nickname, loginData.getResultData().getNickname());
                saveDataToSharePreference(Config.userConfig.paypassword, loginData.getResultData().getPaypassword());
                saveDataToSharePreference(Config.userConfig.position, loginData.getResultData().getPosition());
                saveDataToSharePreference(Config.userConfig.nativeLoginPassword, loginData.getResultData().getNativeLoginPassword());
                saveDataToSharePreference(Config.userConfig.uid, loginData.getResultData().getUid());
                saveDataToSharePreference(Config.userConfig.is_card_sales, loginData.getResultData().getIs_card_sales());
                saveDataToSharePreference(Config.userConfig.is_Login, "Y");
                savePsdStrengthGrade(mRegisterPsd.getText().toString());//保存密码强度

                String bbs_result = loginData.getResultData().getBbs_result();
                String regStr = "src\\s*=\\s*\"?(.*?)(\"|>|\\s+)";
                Pattern pattern = Pattern.compile(regStr);
                Matcher matcher = pattern.matcher(bbs_result);
                final Queue<String> queue = new LinkedList<String>();
                while (matcher.find()) {
                    queue.offer(matcher.group(1));
                }

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        Log.i("wanglei", url);
                        if (queue.isEmpty()) {
                            if (loadingPageDialog.isShowing())
                                loadingPageDialog.cancel();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            Intentintent = new Intent(RegisterActivity.this, JpushService.class);
                            finish();
                        } else {
                            RegisterActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    webView.loadUrl(queue.poll());
                                }
                            });
                        }
                    }
                });
                webView.loadUrl(queue.poll());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePsdStrengthGrade(String psg) {
        saveDataToSharePreference(Config.userConfig.strength, StringUtils.judgePsdAllNum(psg) + "");
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 1) {
            try {
                RespBase respBase = getConfiguration().readValue(result.toString(), RespBase.class);
                popWindowView(respBase.getResultData().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 2) {
            try {
                RespBase respBase = getConfiguration().readValue(result.toString(), RespBase.class);
                popWindowView(respBase.getResultData().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 1234) {
            try {
                loadingPageDialog.cancel();
                RespBase respBase = getConfiguration().readValue(result.toString(), RespBase.class);
                if (respBase.getResultData().toString().equals("-1")) {
                    popWindowView("服务器异常，请稍后重试！");
                } else {
                    popWindowView(respBase.getResultData().toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
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
            mGetCode.setText("倒计时" + millisUntilFinished / 1000);
            mGetCode.setClickable(false);
        }

        @Override
        public void onFinish() {
            mGetCode.setText("获取验证码");
            mGetCode.setClickable(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ResultCode_ProtocolActivity) {
            switch (resultCode) {
                case Constant.Accept://接受
//                    cb_isAgree.setChecked(true);
//                    cb_img.setImageResource(R.mipmap.register_click);
//                    isAgree = true;
                    break;
                case Constant.Reject://拒绝
//                    cb_isAgree.setChecked(false);
//                    cb_img.setImageResource(R.mipmap.register_noclick);
//                    isAgree = false;
                    RegisterActivity.this.finish();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
        if (loadingPageDialog != null) {
            if (loadingPageDialog.isShowing()) {
                loadingPageDialog.dismiss();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingPageDialog != null) {
            if (loadingPageDialog.isShowing()) {
                loadingPageDialog.dismiss();
            }
        }
    }
}
