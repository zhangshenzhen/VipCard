package com.bjypt.vipcard.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.common.Config;

/**
 * Created by Administrator on 2016/12/1 0001.
 * 积分记录
 */

public class HuiyuanbiRecordActivity extends BaseActivity{
    private WebView feePointWeb;
    private ChromeClient chomeClient;
    private RelativeLayout rl_financial_detail_back;
    private TextView tv_finalcial_detail;
    private int ERROR_CODE = 1;


    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what =  msg.what;
            switch (what){
                case 1:
                    Log.e("kill","handleMessage:"+what);
                    feePointWeb.loadUrl("file:///android_asset/index.html");
                    break;
            }
        }
    };

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_deduction_store);
    }

    @Override
    public void beforeInitView() {
        chomeClient = new ChromeClient();
    }

    @Override
    public void initView() {
        tv_finalcial_detail = (TextView) findViewById(R.id.tv_finalcial_detail);
        tv_finalcial_detail.setText("积分记录");
        rl_financial_detail_back = (RelativeLayout) findViewById(R.id.rl_financial_detail_back);
        feePointWeb = (WebView) findViewById(R.id.deduction_store_web);

        feePointWeb.setWebViewClient(new WebViewClient(){

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("kill","errorCode:"+errorCode);
                ERROR_CODE = errorCode;
                mHandler.sendEmptyMessage(1);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("kill","onPageStarted:"+url);
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

    @Override
    public void afterInitView() {
        WebSettings ws = feePointWeb.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSupportMultipleWindows(true);
        feePointWeb.requestFocus();
        feePointWeb.getSettings().setJavaScriptEnabled(true);//添加对JavaScript支持
        feePointWeb.loadUrl(Config.web.recharge_account_ + getFromSharePreference(Config.userConfig.pkregister)+"&type_main=6");
        feePointWeb.addJavascriptInterface(new AndroidJavaScript(this), "jump");
    }

    @Override
    public void bindListener() {
        rl_financial_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /*监听h5页面中的关闭按钮*/

    @Override
    public void onBackPressed() {
        if(feePointWeb.canGoBack()){
            if(feePointWeb.getUrl().equals(Config.web.my_fee_point+getFromSharePreference(Config.userConfig.pkregister))){
                super.onBackPressed();
            }else{
                feePointWeb.goBack();
            }
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClickEvent(View v) {

    }

    class ChromeClient extends WebChromeClient {

        @Override
        public void onCloseWindow(WebView window) {
            //TODO something
            super.onCloseWindow(window);
            finish();
            Log.e("woyaokk", "hahahhahaha-----------------------");
            //onCloseWindow(window);
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
