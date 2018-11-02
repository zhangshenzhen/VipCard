package com.bjypt.vipcard.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.XinWenDetailData;
import com.bjypt.vipcard.view.LoadingPageDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/6 0006.
 * 11
 */

public class XinWenH5Activity extends BaseActivity implements VolleyCallBack<String> {
    private WebView h5Web;
    private int ERROR_CODE = 1;
    private String h5Url,isLogin;
    private String type_base = "http://120.55.199.24:8080/";
    private  RelativeLayout mBack;
    private ProgressBar bar;
    private List<String> urlList = new ArrayList<>();
    private TextView tv;
    private String serviceName;
    private LoadingPageDialog loadingPageDialog;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what =  msg.what;
            switch (what){
                case 1:
                    urlList.add("file:///android_asset/index.html");
                    h5Web.loadUrl("file:///android_asset/index.html");
                    break;
            }
        }
    };
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_xinwen_h5);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        h5Url = intent.getStringExtra("xinwen_id");
        Wethod.httpPost(this,1991,type_base+"hyb-lifeservice/news/newsDetail?id="+h5Url,null,this);
    }

    @Override
    public void initView() {

        mBack = (RelativeLayout) findViewById(R.id.h5_xinwen_back);
        h5Web = (WebView) findViewById(R.id.life_xinwen_web);
        bar = (ProgressBar) findViewById(R.id.myProgressBar);
//        tv = (TextView) findViewById(R.id.service_h5_tv);

        loadingPageDialog = new LoadingPageDialog(this);
        loadingPageDialog.show();
        /*h5Web.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_BACK && h5Web.canGoBack()){
                        *//*Log.e("tuyouze","点击返回");
                        h5Web.goBack();*//*
                        if(urlList.size()>1){
                            if(urlList.get(urlList.size()-1).contains("alipay")||urlList.get(urlList.size()-1).contains("weixin.qq.com")||urlList.get(urlList.size()-1).contains("95516")){
                                Log.e("hh","bbbbb"+urlList.get(0)
                                );
                                h5Web.loadUrl(urlList.get(0));
                                //此时清空list。然后再次点击返回的时候返回Activity
                                urlList.clear();
                            }else{
                                Log.e("hh","ccccc");
                                if(urlList.size()==0){
                                    Log.e("hh","ddddd");
                                    finish();
                                }else{
                                    h5Web.goBack();
                                }
                            }
                        }else{
                            finish();
                        }
                        return true;
                    }
                }
                return false;
            }


        });*/


        h5Web.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100){
                    bar.setVisibility(View.INVISIBLE);
                    if (loadingPageDialog !=null){
                        if (loadingPageDialog.isShowing()){
                            loadingPageDialog.dismiss();
                        }
                    }
                }else{
                    if(loadingPageDialog !=null){
                        if(!loadingPageDialog.isShowing()){
                            loadingPageDialog.show();
                        }
                    }
//                    if(View.INVISIBLE == bar.getVisibility()){
//                        bar.setVisibility(View.VISIBLE);
//                        loadingPageDialog.show();
//                    }
//                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, true);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView newWebView = new WebView(XinWenH5Activity.this);
                h5Web.addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }
        });

        h5Web.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebView.HitTestResult hitTestResult = view.getHitTestResult();
                //hitTestResult == null 解决重定向问题
                if(!TextUtils.isEmpty(url) && hitTestResult == null){
                    view.loadUrl(url);
                    return true;
                }


                /*Log.e("killme","three:"+url);
                view.loadUrl(url);
                return true;*/
                return super.shouldOverrideUrlLoading(view,url);
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
                urlList.add(url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("kill","onPageFinished:"+url);
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
            case R.id.h5_xinwen_back:
                finish();
                break;
        }

    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if(reqcode == 1991){
            try {
                XinWenDetailData xinWenDetailData = getConfiguration().readValue(result.toString(), XinWenDetailData.class);

                h5Web.loadUrl(type_base + xinWenDetailData.getResultData().getUrl());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

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
