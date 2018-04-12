package com.bjypt.vipcard.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.common.Config;

import static com.bjypt.vipcard.R.id.myProgressBar;

/**
 * Created by 崔龙 on 2017/3/30.
 */
public class H5DeductionActivity extends BaseActivity {

    private WebView wv_h5_return_web;         // webView
    private ProgressBar mProgressBar;        // 加载框
    private RelativeLayout rl_h5_return_back; // 返回按钮
    public final String URL = Config.web.vip_minszx;
//    http://121.196.233.207/hyb/
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_deduction_h5);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        wv_h5_return_web = (WebView) findViewById(R.id.wv_h5_return_web);
        mProgressBar = (ProgressBar) findViewById(myProgressBar);
        rl_h5_return_back = (RelativeLayout) findViewById(R.id.rl_h5_return_back);

    }

    @Override
    public void afterInitView() {
        WebSettings ws = wv_h5_return_web.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSupportMultipleWindows(true);
        ws.setGeolocationEnabled(true);
        ws.setAppCacheEnabled(true);
//        ws.setBlockNetworkImage(true);
//        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);
//        ws.setBlockNetworkLoads(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        wv_h5_return_web.loadUrl(URL +getFromSharePreference(Config.userConfig.pkregister));
//        http://www.qq.com
//        wv_h5_return_web.loadUrl("http://www.qq.com");
        wv_h5_return_web.setWebViewClient(new webViewClient());
        wv_h5_return_web.addJavascriptInterface(new AndroidJavaScripts(this), "jump");
    }

    @Override
    public void bindListener() {
        rl_h5_return_back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.rl_h5_return_back:
                    finish();
                break;
        }
    }

    /*监听h5页面中的关闭按钮*/
    @Override
    public void onBackPressed() {
        if(wv_h5_return_web.canGoBack()){
            if(wv_h5_return_web.getUrl().equals(URL)){
                super.onBackPressed();
            }else{
                wv_h5_return_web.goBack();
            }
        }else {
            super.onBackPressed();
        }
    }
    private class webViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            mProgressBar.setVisibility(View.GONE);
        }
    }


    public class AndroidJavaScripts{
        private Context context;

        public AndroidJavaScripts(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void closeWindow(){
            finish();
        }

    }
}
