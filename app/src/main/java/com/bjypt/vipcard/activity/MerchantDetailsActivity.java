package com.bjypt.vipcard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.common.Config;

/**
 * Created by 涂有泽 .
 * Date by 2016/11/2
 * Use by 商家详情H5
 */
public class MerchantDetailsActivity extends BaseActivity{

    private WebView deductionWeb;
    private ChromeClient chomeClient;
    private Intent mIntent;
    private RelativeLayout mBack;
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_deduction_store);
    }

    @Override
    public void beforeInitView() {
         mIntent = getIntent();
        chomeClient = new ChromeClient();
    }

    @Override
    public void initView() {
        deductionWeb = (WebView) findViewById(R.id.deduction_store_web);
        mBack = (RelativeLayout) findViewById(R.id.rl_financial_detail_back);

        deductionWeb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void afterInitView() {
        WebSettings ws = deductionWeb.getSettings();
//        feePointWeb.setWebChromeClient(chomeClient);
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSupportMultipleWindows(true);
        deductionWeb.requestFocus();
        deductionWeb.getSettings().setJavaScriptEnabled(true);//添加对JavaScript支持

        deductionWeb.loadUrl(Config.web.merchant_detail_+mIntent.getStringExtra("pkmuser"));
        deductionWeb.addJavascriptInterface(new AndroidJavaScripts(this), "jump");
    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.rl_financial_detail_back:
                finish();
                break;
        }
    }

    /*监听h5页面中的关闭按钮*/

    @Override
    public void onBackPressed() {
        Log.e("woyaokk", "deductionWeb.getUrl():" + deductionWeb.getUrl() + "     ===" + Config.web.deduction_store);
        if(deductionWeb.canGoBack()){
            if(deductionWeb.getUrl().equals(Config.web.deduction_store)){
                super.onBackPressed();
            }else{
                deductionWeb.goBack();
            }
        }else {
            super.onBackPressed();
        }
    }

    class ChromeClient extends WebChromeClient {

        @Override
        public void onCloseWindow(WebView window) {
            //TODO something
            super.onCloseWindow(window);
            finish();
        }


        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {

            //TODO something

            return true;
        }

    }

    public class AndroidJavaScripts{
        private Context context;

        public AndroidJavaScripts(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void closeWindow(){
            Log.e("woyaokk","ddd");
            finish();
        }

    }
}
