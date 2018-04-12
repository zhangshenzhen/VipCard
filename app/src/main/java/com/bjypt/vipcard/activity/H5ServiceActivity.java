package com.bjypt.vipcard.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.common.Config;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class H5ServiceActivity extends BaseActivity{

    private ArrayList<String> loadHistoryUrls = new ArrayList<String>();

    private WebView h5Web;
    private int ERROR_CODE = 1;
    private String h5Url,isLogin;
    private RelativeLayout mBack;
    private ProgressBar bar;
    private String firstUrl;
    private String lastUrl;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what =  msg.what;
            switch (what){
                case 1:
                    h5Web.loadUrl("file:///android_asset/index.html");
                    break;
            }
        }
    };
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_lifeservice_h5);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        h5Url = intent.getStringExtra("life_url");
        isLogin =intent.getStringExtra("isLogin");
        Log.e("tuyouze","activity_lifeservice_h5:"+isLogin);
        if(isLogin.equals("N")){
            loadHistoryUrls.add(h5Url);
        }else{
            loadHistoryUrls.add(Config.web.type_base + h5Url+getFromSharePreference(Config.userConfig.pkregister));
        }
    }

    @Override
    public void initView() {
        h5Web = (WebView) findViewById(R.id.life_service_web);
        bar = (ProgressBar) findViewById(R.id.myProgressBar);


        h5Web.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_BACK && h5Web.canGoBack()){
                        if(loadHistoryUrls.size()>0&&loadHistoryUrls.get(loadHistoryUrls.size()-1).contains("alipay.com")){
                            loadHistoryUrls.remove(loadHistoryUrls.get(loadHistoryUrls.size()-1));
                            loadHistoryUrls.remove(loadHistoryUrls.get(loadHistoryUrls.size()-1));
                            h5Web.loadUrl(loadHistoryUrls.get(loadHistoryUrls.size()-1));
                        }else{
                            h5Web.goBack();
                        }
                        Log.e("tuyouze","点击返回");
//
                        return true;
                    }
                }
                return false;
            }


        });


        h5Web.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100){
                    bar.setVisibility(View.INVISIBLE);
                }else{
                    if(View.INVISIBLE == bar.getVisibility()){
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, true);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });

        h5Web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("killme","three:"+url);
                lastUrl = url;
                view.loadUrl(url);
                return true;
            }

            /*@Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                resend.sendToTarget();
            }*/

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("kill","errorCode:"+description);
                ERROR_CODE = errorCode;
                mHandler.sendEmptyMessage(1);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("kill","onPageStarted:"+url);
                if(url.contains("alipay.com")){

                }else{
                    firstUrl = url;
                }
                loadHistoryUrls.add(url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("kill","onPageFinished:"+url);
                super.onPageFinished(view, url);
            }


        });
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void afterInitView() {
        WebSettings ws = h5Web.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSupportMultipleWindows(true);
        ws.setGeolocationEnabled(true);
        ws.setGeolocationDatabasePath(getFilesDir().getPath());
        h5Web.requestFocus();
        h5Web.getSettings().setJavaScriptEnabled(true);//添加对JavaScript支持
        Log.e("tuyouze","isLogin H5 afterInitView:"+isLogin);
        if(isLogin.equals("N")){

            h5Web.loadUrl(h5Url);
            Log.e("killme","one:"+h5Url);
        }else{
            h5Web.loadUrl(Config.web.type_base + h5Url+getFromSharePreference(Config.userConfig.pkregister));
            Log.e("killme","two");
        }
        h5Web.addJavascriptInterface(new AndroidJavaScript(this), "jump");
    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.h5_back:
                Log.e("tuyouze","lastUrl:"+lastUrl+"   firstUrl:"+firstUrl+"  h5Web.canGoBack():"+h5Web.canGoBack());
                if(h5Web.canGoBack()){
                    /*if(loadHistoryUrls.size()>0&&loadHistoryUrls.get(loadHistoryUrls.size()-1).contains("alipay.com")){
                        loadHistoryUrls.remove(loadHistoryUrls.get(loadHistoryUrls.size()-1));
                        loadHistoryUrls.remove(loadHistoryUrls.get(loadHistoryUrls.size()-1));
                        h5Web.loadUrl(loadHistoryUrls.get(loadHistoryUrls.size()-1));
                    }else{
                        h5Web.goBack();
                    }*/
                    if(lastUrl.contains("alipay.com")){
                        h5Web.loadUrl(firstUrl);
                    }else{
                        h5Web.goBack();
                    }
//
                }else{
                    finish();
                }
                break;
        }
    }
    public class AndroidJavaScript{
        private Context context;

        public AndroidJavaScript(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void closeWindow(){
            Log.e("woyaokk","ddd");
            finish();
        }

    }
}
