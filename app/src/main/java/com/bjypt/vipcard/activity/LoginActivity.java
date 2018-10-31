package com.bjypt.vipcard.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;
import com.bjypt.vipcard.activity.shangfeng.util.ShangfengUriHelper;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.base.RespBase;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.TrackCommon;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.CommentDetailsClBean;
import com.bjypt.vipcard.model.LoginData;
import com.bjypt.vipcard.service.MQTTService;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.MD5;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.PhoneCpuId;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.utils.StringUtils;
import com.bjypt.vipcard.view.LoadingPageDialog;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.wxapi.Constants;
import com.sinia.orderlang.utils.StringUtil;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.piwik.sdk.extra.TrackHelper;

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
 * Date by 2016/3/19
 * Use by 登录界面
 */
public class LoginActivity extends BaseActivity implements VolleyCallBack {
    private static final int REQUEST_AUTHORIZE = 4564545;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_UNION_ID = 34321321;
    private ImageButton mBack;
    private RelativeLayout mRegister;
    private EditText mLoginName, mLoginPsd;
    private Button mLogin;
    //  private JpushService.MyBinder binder;
    private String sre = "";
    private Intent Intentintent;
    private String isLoginSubscri = "N";
    private String pkmuser = "";
    private String distance = "";
    public final int REQUESTCODE = 1;
    private boolean isCheck = false;
    private PopupWindow popupWindow;
    private TextView stringPop;
    private TextView canclePop;
    private int screenWidth;
    private int screenHeigh;
    private LinearLayout linear_pop;
    private WebView webView;
    LoadingPageDialog loadingPageDialog;
    private ImageView qq_login;
    private ImageView wechat_login;
    //需要腾讯提供的一个Tencent类
    private Tencent mTencent;
    //还需要一个IUiListener 的实现类（LogInListener implements IUiListener）
    private LogInListener mListener;
    private Handler mHandler;
    private String openid;
    private String nickname;
    private String headUrl;
    private TextView textview_back;
    private String unionid;
    private TextView yan_zheng_number;

    private String callbackData;
    private BannerBean bannerBean;

    /****************************shangfeng 登录 start*******************************************/
    public static void callActivity(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public static void callActivity(Context context, String callbackData, BannerBean bannerBean) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("callback", callbackData);
        intent.putExtra("bannerBean", bannerBean);
        context.startActivity(intent);
    }
    /*****************************shangfeng 登录 end************************************************/

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_login);
        //首先需要用APP ID 获取到一个Tencent实例
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this.getApplicationContext());
        //初始化一个IUiListener对象，在IUiListener接口的回调方法中获取到有关授权的某些信息
        // （千万别忘了覆写onActivityResult方法，否则接收不到回调）
        loadingPageDialog = new LoadingPageDialog(this);
        mListener = new LogInListener();
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        isLoginSubscri = intent.getStringExtra("loginsss");
        callbackData = getIntent().getStringExtra("callback");
        bannerBean = (BannerBean) getIntent().getSerializableExtra("bannerBean");

        Log.e("isLoginSubscri", "isLoginSubscri:" + isLoginSubscri);
//        pkmuser = intent.getStringExtra("pkmuser");
//        distance = intent.getStringExtra("distance");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;

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

        TrackHelper.track().screen(TrackCommon.ViewTrackLogin).title(TrackCommon.ViewTrackLogin).with(getTracker());

    }

    @Override
    public void initView() {
        checkGetReadPermission();
        linear_pop = (LinearLayout) findViewById(R.id.linear_pop);
        mBack = (ImageButton) findViewById(R.id.login_back);
        mLoginName = (EditText) findViewById(R.id.login_name);
        wechat_login = (ImageView) findViewById(R.id.wechat_login);
        qq_login = (ImageView) findViewById(R.id.qq_login);
        mLoginPsd = (EditText) findViewById(R.id.login_password);
        yan_zheng_number = (TextView) findViewById(R.id.yan_zheng_number);
        mLoginPsd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        mLogin = (Button) findViewById(R.id.login_btn);
        mRegister = (RelativeLayout) findViewById(R.id.register);
        if (getFromSharePreference(Config.userConfig.phoneno) != null || !getFromSharePreference(Config.userConfig.phoneno).equals("")) {
            mLoginName.setText(getFromSharePreference(Config.userConfig.phoneno));
        }

    }

    @Override
    public void afterInitView() {
    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        wechat_login.setOnClickListener(this);
        qq_login.setOnClickListener(this);
        yan_zheng_number.setOnClickListener(this);
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

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.login_back:
                finish();
                break;
            case R.id.login_btn:
                checkGetReadPermission();
                if (isCheck) {
                    if (mLoginName.getText().toString().equals("") || mLoginName.getText().toString() == null) {
                        popWindowView("请输入手机号或用户名");
                    } else if (mLoginPsd.getText().toString() == null || mLoginPsd.getText().toString().equals("")) {
                        popWindowView("请输入登录密码");
                    } else {
                        Map<String, String> params = new HashMap<>();
                        params.put("phoneno", mLoginName.getText().toString());
                        params.put("cpuid", PhoneCpuId.getDeviceId(this));
                        params.put("extralparam", AES.encrypt(getFromSharePreference(Config.userConfig.detail_adress), AES.key));
                        try {
                            params.put("loginpassword", MD5.getMd5(mLoginPsd.getText().toString(), MD5.key));
                            params.put("nativeLoginPassword", mLoginPsd.getText().toString());
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        Wethod.httpPost(LoginActivity.this, 1, Config.web.login_, params, this);
                        loadingPageDialog.show();
                        loadingPageDialog.setMsg("正在登陆...");
                    }
                }
                break;
            case R.id.yan_zheng_number:
                startActivity(new Intent(this, ForgetPsdActivity.class));
                break;
            case R.id.register:
                /*****************注册账号********************/
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.wechat_login:
                wxLogin();
                break;
            case R.id.qq_login:
                qqLogin();
                break;
        }
    }

    private void qqLogin() {
        loadingPageDialog.show();
        mTencent.login(LoginActivity.this, "all", mListener);
    }

    public void popWindowView(String text) {
        View view = View.inflate(LoginActivity.this, R.layout.layout_register_popwindow, null);
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
        popupWindow.showAtLocation(linear_pop, Gravity.BOTTOM, 0, 0);
    }

    private void checkGetReadPermission() {
        /**
         * 判断是否获取到相机权限
         */
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("kiss", "111:" + (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED));
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUESTCODE);
            } else {        //如果已经获取到了权限则直接进行下一步操作
                isCheck = true;
                Log.e("kiss", "22");
            }
        } else {
            isCheck = true;
            Log.e("kiss", "33");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("kiss", "44");
        switch (requestCode) {
            case REQUESTCODE:
                Log.e("kiss", "55");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("kiss", "66");
                    isCheck = true;
                } else {
                    Log.e("kiss", "77");
                    popWindowView("您拒绝了获取到读取手机信息权限,请手动获取或重装软件");
                    isCheck = false;
                }
                break;
        }
    }

    // private JpushService mService;

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
            handlerLogin(result.toString());
        } else if (reqcode == REQUEST_AUTHORIZE) {
            handlerLogin(result.toString());
        } else if (reqcode == REQUEST_UNION_ID) {
            Log.e("qqResult", result + "su");
        }
    }

    private void handlerLogin(String result) {
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

            savePsdStrengthGrade(mLoginPsd.getText().toString());//保存密码强度


            String bbs_result = loginData.getResultData().getBbs_result();
            String regStr = "src\\s*=\\s*\"?(.*?)(\"|>|\\s+)";
            Pattern pattern = Pattern.compile(regStr);
            Matcher matcher = pattern.matcher(bbs_result);
            final Queue<String> queue = new LinkedList<String>();
            while (matcher.find()) {
                queue.offer(matcher.group(1));
            }

            if (queue.isEmpty()) {
                if (isLoginSubscri == null || isLoginSubscri.isEmpty()) {
//                    startActivity(new Intent(this, MainActivity.class));
                    loginFinish();
                } else if (isLoginSubscri.equals("Y")) {
                    Log.e("isLoginSubscri", "?????????????????????????");
                }
            } else {
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        Log.i("wanglei", url);
                        if (queue.isEmpty()) {
                            if (loadingPageDialog.isShowing())
                                loadingPageDialog.cancel();
                            loginFinish();
                        } else {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    webView.loadUrl(queue.poll());
                                }
                            });
                        }
                    }
                });
                webView.loadUrl(queue.poll());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loginFinish(){
        ShangfengUriHelper shangfengUriHelper = new ShangfengUriHelper(this);
        if (bannerBean != null) {
            shangfengUriHelper.onAppCategoryItemClick(bannerBean);
        } else if (StringUtil.isNotEmpty(callbackData)) {
            shangfengUriHelper.startSearch(callbackData);
        }

        String clientId = SharedPreferenceUtils.getFromSharedPreference(getApplicationContext(), Config.userConfig.phoneno);
        getTracker().setUserId(clientId);

        startService(new Intent(LoginActivity.this, MQTTService.class));
        finish();
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        switch (reqcode) {
            case 1:
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
                break;
            case REQUEST_AUTHORIZE:
                loadBindAndJumpPhoneInfo(result);
                break;
            case REQUEST_UNION_ID:
                Log.e("qqResult", result + "ff");
                String re1 = ".*?";    // Non-greedy match on filler
                String re3 = ".*?";    // Non-greedy match on filler
                String re4 = "(c)";    // Any Single Character 1
                String re5 = ".*?";    // Non-greedy match on filler
                String re6 = "(\\{.*?\\})";    // Curly Braces 1

                Pattern p = Pattern.compile(re1 + re3 + re4 + re5 + re6, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m = p.matcher(result + "");
                if (m.find()) {
                    String cbraces1 = m.group(2);
                    try {
                        JSONObject jsonObject = new JSONObject(cbraces1);
                        unionid = jsonObject.getString("unionid");
                        Log.e("unionid=========================", unionid);
                        getUserInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    private void savePsdStrengthGrade(String psg) {
        saveDataToSharePreference(Config.userConfig.strength, StringUtils.judgePsdAllNum(psg) + "");
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

    public void wxLogin() {
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            ToastUtil.showToast(this, "您还未安装微信客户端");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        MyApplication.mWxApi.sendReq(req);
        LoginActivity.this.finish();
    }

    //确保能接收到回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, mListener);
    }

    private class LogInListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            Toast.makeText(LoginActivity.this, "授权成功！", Toast.LENGTH_LONG).show();
            System.out.println("o.toString() ------------------------->        " + o.toString());
            JSONObject jsonObject = (JSONObject) o;
            initOpenidAndToken(jsonObject);

        }

        @Override
        public void onError(UiError uiError) {

            Toast.makeText(LoginActivity.this, "授权出错！", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消！", Toast.LENGTH_LONG).show();
        }

    }

    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            openid = jsonObject.getString("openid");
            String token = jsonObject.getString("access_token");
            String expires = jsonObject.getString("expires_in");
            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openid);
            getUnionId(token);
//            getUserInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserInfo() {

        //sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
        QQToken mQQToken = mTencent.getQQToken();
        UserInfo userInfo = new UserInfo(LoginActivity.this, mQQToken);
        userInfo.getUserInfo(new IUiListener() {
                                 @Override
                                 public void onComplete(final Object o) {
                                     JSONObject userInfoJson = (JSONObject) o;
                                     if (((JSONObject) o).has("figureurl")) {
                                         try {
                                             //直接传递一个昵称的内容过去
                                             nickname = userInfoJson.getString("nickname");
                                             headUrl = ((JSONObject) o).getString("figureurl_qq_2");
                                             Log.e("Nounionid", unionid + "nonono");
                                             requestBindPhone(unionid);
                                         } catch (JSONException e) {
                                             e.printStackTrace();
                                         }
                                     }

                                 }

                                 @Override
                                 public void onError(UiError uiError) {
                                     Log.e("GET_QQ_INFO_ERROR", "获取qq用户信息错误");
                                     Toast.makeText(LoginActivity.this, "获取qq用户信息错误", Toast.LENGTH_SHORT).show();
                                 }

                                 @Override
                                 public void onCancel() {
                                     Log.e("GET_QQ_INFO_CANCEL", "获取qq用户信息取消");
                                     Toast.makeText(LoginActivity.this, "获取qq用户信息取消", Toast.LENGTH_SHORT).show();
                                 }
                             }
        );
    }

    private void requestBindPhone(String unionid) {
        Map<String, String> param = new HashMap<>();
        param.put("openId", unionid);
        param.put("loginType", "2");
        param.put("headImgUrl", headUrl);
        param.put("nickName", nickname);
        Wethod.httpPost(this, REQUEST_AUTHORIZE, Config.web.authorize, param, this);
    }

    /**
     * 加载绑定手机号与跳转信息
     *
     * @param result
     */
    private void loadBindAndJumpPhoneInfo(Object result) {
        Log.e(TAG, "loadBindAndJumpPhoneInfo====" + result + "");
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            CommentDetailsClBean commentDetailsClBean = objectMapper.readValue(result.toString(), CommentDetailsClBean.class);
            String resultData = commentDetailsClBean.getResultData();
            if (resultData != null && resultData.contains("绑定手机")) {
                Intent intent = new Intent(this, BindPhoneNumberActivity.class);
                intent.putExtra("openId", unionid);
                intent.putExtra("loginType", "2");
                intent.putExtra("headImgUrl", headUrl);
                intent.putExtra("nickName", nickname);
                startActivity(intent);
                LoginActivity.this.finish();
            } else {
                ToastUtil.showToast(LoginActivity.this, resultData);
            }
            ToastUtil.showToast(LoginActivity.this, resultData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求token
     *
     * @param token
     */
    private void getUnionId(String token) {
        Log.e(TAG, "getAccessToken=====" + token);
        String URL = "https://graph.qq.com/oauth2.0/me?access_token="
                + token
                + "&unionid=1";
        Log.e(TAG, "getAccessTokenUrl=====" + URL);
        Wethod.httpGetOutSystem(LoginActivity.this, REQUEST_UNION_ID, URL, this);
    }
}
