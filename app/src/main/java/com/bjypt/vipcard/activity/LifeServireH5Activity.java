package com.bjypt.vipcard.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.h5.h5override.H5OverrideUrlFactory;
import com.bjypt.vipcard.h5.h5override.H5OverrideUrlListener;
import com.bjypt.vipcard.utils.AES;
import com.bjypt.vipcard.utils.AppUtil;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.PhoneCpuId;
import com.bjypt.vipcard.view.LoadingPageDialog;
import com.bjypt.vipcard.view.ToastUtil;
import com.gallerypick.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sinia.orderlang.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2016/12/14 0014.
 * 生活服务H5所有展示页面
 */

public class LifeServireH5Activity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    private WebView h5Web;
    private int ERROR_CODE = 1;
    private String h5Url, isLogin;
    private LinearLayout ll_close;
    private ProgressBar bar;
    private List<String> urlList = new ArrayList<>();
    private TextView tv;
    private String serviceName;
    private LoadingPageDialog loadingPageDialog;
    private LinearLayout h5_back;

    private static final int requestPayCode = 17;

    BroadCastReceiverUtils utils;
    MyBroadCastReceiver myBroadCastReceiver;
    private String isNews;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    urlList.add("file:///android_asset/index.html");
                    h5Web.loadUrl("file:///android_asset/index.html");
                    break;
            }
        }
    };

    @Override
    public void setContentLayout() {
        Intent intent = getIntent();
        h5Url = intent.getStringExtra("life_url");
        isLogin = intent.getStringExtra("isLogin");
        serviceName = intent.getStringExtra("serviceName");
        isNews = intent.getStringExtra("isNews");
        setContentView(contentLayout());
    }

    /**
     * 返回布局文件
     *
     * @return R.layout.xxx.xml
     */
    private int contentLayout() {
//        if (h5Url.contains(Config.web.DZURL) || h5Url.contains(Config.web.FYDZURL)) {
        if (isNews != null && isNews.equals("Y")) {
            return R.layout.activity_lifeservice_h5_new;
        }
        return R.layout.activity_lifeservice_h5;
    }

    @Override
    public void beforeInitView() {

        if (isLogin.equals("Y")) {
            urlList.add(Config.web.type_base + h5Url + getFromSharePreference(Config.userConfig.pkregister));
        }
        Log.e("tuyouze", "isLogin H5 beforeInitView:" + isLogin);

        utils = new BroadCastReceiverUtils();
        myBroadCastReceiver = new MyBroadCastReceiver();
        utils.registerBroadCastReceiver(this, "ccb_pay_notify_callback", myBroadCastReceiver);//ccb 支付通知
    }

    @Override
    public void initView() {
        h5_back = (LinearLayout) findViewById(R.id.h5_back);
        ll_close = (LinearLayout) findViewById(R.id.ll_close);
        h5Web = (WebView) findViewById(R.id.life_service_web);
        bar = (ProgressBar) findViewById(R.id.myProgressBar);
        tv = (TextView) findViewById(R.id.service_h5_tv);

        loadingPageDialog = new LoadingPageDialog(this);
        loadingPageDialog.show();
        h5Web.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && h5Web.canGoBack()) {
                        /*Log.e("tuyouze","点击返回");
                        h5Web.goBack();*/
                        if (urlList.size() > 1) {
                            if (urlList.get(urlList.size() - 1).contains("alipay") || urlList.get(urlList.size() - 1).contains("weixin.qq.com") || urlList.get(urlList.size() - 1).contains("95516")) {
                                Log.e("hh", "bbbbb" + urlList.get(0)
                                );
                                h5Web.loadUrl(urlList.get(0));
                                //此时清空list。然后再次点击返回的时候返回Activity
                                urlList.clear();
                            } else {
                                Log.e("hh", "ccccc");
                                if (urlList.size() == 0) {
                                    Log.e("hh", "ddddd");
                                    finish();
                                } else {
                                    h5Web.goBack();
                                }
                            }
                        } else {
                            finish();
                        }
                        return true;
                    }
                }
                return false;
            }


        });


        h5Web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.INVISIBLE);
                    if (loadingPageDialog != null) {
                        if (loadingPageDialog.isShowing()) {
                            loadingPageDialog.dismiss();
                        }
                    }
                } else {
                    if (loadingPageDialog != null) {
                        if (!LifeServireH5Activity.this.isFinishing() && !loadingPageDialog.isShowing()) {
                            loadingPageDialog.show();
                        }
                    }
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, true);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });

        h5Web.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebView.HitTestResult hitTestResult = view.getHitTestResult();
                //hitTestResult == null 解决重定向问题
                if (url.startsWith("hybpay://")) {
                    Log.i("wanglei", url);
                    h5Web.goBack(); //解决重复下单问题
                    startAlipayActivity(url);
                    return true;
                } else if (url.startsWith("hybweb://webaction.com/")) {
                    H5OverrideUrlListener listener = H5OverrideUrlFactory.getInstance(url);
                    if (listener != null) {
                        listener.process(LifeServireH5Activity.this);
                    } else {
                        ToastUtil.showToast(LifeServireH5Activity.this, "暂未开通");
                    }
                    return true;
                } else {
                    if (!TextUtils.isEmpty(url) && hitTestResult == null) {
                        Log.i("wanglei", "test ---" + url);
                        view.loadUrl(url);
                        return true;
                    }
                    Log.i("wanglei", "test 3---" + url);

                /*Log.e("killme","three:"+url);
                view.loadUrl(url);
                return true;*/
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            /*@Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                resend.sendToTarget();
            }*/

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                ERROR_CODE = errorCode;
                mHandler.sendEmptyMessage(1);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                urlList.add(url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("kill", "onPageFinished:" + url);
                super.onPageFinished(view, url);
            }


        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void afterInitView() {
        tv.setText(serviceName);
        WebSettings ws = h5Web.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSupportMultipleWindows(true);
        ws.setGeolocationEnabled(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        ws.setGeolocationDatabasePath(getFilesDir().getPath());
        h5Web.requestFocus();
        Log.e("killme", "one:" + h5Url);
        if (isLogin.equals("N")) {
            h5Web.loadUrl(h5Url);
        } else {
            if (getIntent().hasExtra("isallurl") && getIntent().getStringExtra("isallurl").equals("Y")) {
                h5Web.loadUrl(h5Url + getFromSharePreference(Config.userConfig.pkregister) + "&versionCode=" + getVersion());
            } else {
                h5Web.loadUrl(Config.web.type_base + h5Url + getFromSharePreference(Config.userConfig.pkregister) + "&versionCode=" + getVersion());
            }
        }
        h5Web.addJavascriptInterface(new AndroidJavaScript(this), "jump");
//        h5Web.addJavascriptInterface(new AndroidJavascriptExtends(this), "android");
    }

    // 调起支付宝并跳转到指定页面
    private void startAlipayActivity(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url,
                    Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setPackage(getApplication().getPackageName());
            startActivityForResult(intent, requestPayCode);
//            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void bindListener() {
        h5_back.setOnClickListener(this);
        ll_close.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.ll_close:
                finish();
                break;
            case R.id.h5_back:
                if (h5Web.canGoBack()) {
                    Log.e("hh", "aaaaa");
                    if (urlList.size() > 1) {
                        if (urlList.get(urlList.size() - 1).contains("android_asset/index.html")) {
                            //TODO 此时当检测到该网页无法显示的时候直接finish当前页面
                            finish();
                        }
                        if (urlList.get(urlList.size() - 1).contains("alipay") || urlList.get(urlList.size() - 1).contains("weixin.qq.com") || urlList.get(urlList.size() - 1).contains("95516")) {
                            Log.e("hh", "bbbbb" + urlList.get(0));
                            h5Web.loadUrl(urlList.get(0));
                            //此时清空list。然后再次点击返回的时候返回Activity
                            urlList.clear();
                        } else {
                            Log.e("hh", "ccccc");
                            if (urlList.size() == 0) {
                                Log.e("hh", "ddddd");
                                finish();
                            } else {
                                h5Web.goBack();
                            }

                        }
                    } else {
                        finish();
                    }


                } else {
                    Log.e("hh", "eeeeee");
                    finish();
                }
                break;
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    public class AndroidJavaScript {
        private Context context;

        public AndroidJavaScript(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void closeWindow() {
            Log.e("woyaokk", "ddd");
            finish();
        }

        @JavascriptInterface
        public String getDeviceId(){
            LogUtil.debugPrint(PhoneCpuId.getDeviceId(context));
            return PhoneCpuId.getDeviceId(context);
        }

        @JavascriptInterface
        public void requestPermissionDeviceId(){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String[] perms = {Manifest.permission.READ_PHONE_STATE};
                    if(!EasyPermissions.hasPermissions(LifeServireH5Activity.this, perms)){
                        EasyPermissions.requestPermissions(this, "该功能需要获取系统设备号的权限",
                                R.string.confirm,
                                R.string.cancel,
                                0,
                                Manifest.permission.READ_PHONE_STATE);
                    }
                }
            }, 100);
        }

        @JavascriptInterface
        public String getParamsSign(String paramsStr){
            LogUtil.debugPrint(paramsStr.toString());
            try {
                JSONObject jsonObject = new JSONObject(paramsStr);
                Iterator<String> keys =  jsonObject.keys();
                Map<String, String> params = new HashMap<>();
                while (keys.hasNext()){
                    String key = keys.next();
                    LogUtil.debugPrint("key=" + key + " value=" + jsonObject.get(key).toString());
                    params.put(key,  jsonObject.get(key).toString());
                }
                JSONObject json = new JSONObject();
                json.put("sign", AES.createSign(new TreeMap<String, String>(params)));
                int versionCode = AppUtil.getVersionCode(LifeServireH5Activity.this);
                json.put("version", AES.encrypt(versionCode + "", AES.key));
                return json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

    }

    public class AndroidJavascriptExtends extends AndroidJavaScript{
        private Context context;
        public AndroidJavascriptExtends(Context context) {
            super(context);
            this.context = context;
        }

        @JavascriptInterface
        public String getSign(Object paramsStr){
            LogUtil.debugPrint(paramsStr.toString());
            java.lang.reflect.Type type = new TypeToken<HashMap<Integer, String>>() {
            }.getType();
            Map<String, String> params = new Gson().fromJson(paramsStr.toString(), type);
            return AES.createSign(new TreeMap<String, String>(params));
        }
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals("ccb_pay_notify_callback")) {
                String callBack = intent.getStringExtra("callbackUrl");
                ;
                if (StringUtil.isNotEmpty(callBack)) {
                    if (callBack.endsWith("pkregister=")) {
                        h5Web.loadUrl(callBack + getFromSharePreference(Config.userConfig.pkregister));
                    } else {
                        h5Web.loadUrl(callBack);
                    }
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (utils != null) {
            utils.UnRegisterBroadCastReceiver(this, myBroadCastReceiver);
        }
    }
}
