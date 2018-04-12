package com.bjypt.vipcard.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 涂有泽 .
 * Date by 2016/8/11
 * Use by 我的分润记录
 */
public class FeePointActivity extends BaseActivity implements VolleyCallBack{

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
        tv_finalcial_detail.setText("消费分红");
        rl_financial_detail_back = (RelativeLayout) findViewById(R.id.rl_financial_detail_back);
        feePointWeb = (WebView) findViewById(R.id.deduction_store_web);

        feePointWeb.setWebViewClient(new WebViewClient(){
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

    @SuppressLint("JavascriptInterface")
    @Override
    public void afterInitView() {
        WebSettings ws = feePointWeb.getSettings();
//        feePointWeb.setWebChromeClient(chomeClient);
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSupportMultipleWindows(true);
        feePointWeb.requestFocus();
        feePointWeb.getSettings().setJavaScriptEnabled(true);//添加对JavaScript支持
       /* Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(this,1, Config.web.system_infomatiob, params, this);*/

        feePointWeb.loadUrl(Config.web.my_fee_point + getFromSharePreference(Config.userConfig.pkregister));
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

    @Override
    public void onClickEvent(View v) {

    }




    /*监听h5页面中的关闭按钮*/

    @Override
    public void onBackPressed() {
        Log.e("woyaokk","feePointWeb.getUrl():"+feePointWeb.getUrl()+"     ==="+Config.web.my_fee_point);
        if(feePointWeb.canGoBack()){
            Log.e("woyaokk","1111");
            if(feePointWeb.getUrl().equals(Config.web.my_fee_point+getFromSharePreference(Config.userConfig.pkregister))){
                Log.e("woyaokk","2222");
                super.onBackPressed();
            }else{
                Log.e("woyaokk","3333");
                feePointWeb.goBack();
            }
        }else {
            Log.e("woyaokk","4444");
//            finish();
           super.onBackPressed();
        }
    }


    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode==1){
            feePointWeb.loadUrl(Config.web.my_fee_point + getFromSharePreference(Config.userConfig.pkregister));
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
if (reqcode==1){
    feePointWeb.loadUrl("file:///android_asset/index.html");
}
    }

    @Override
    public void onError(VolleyError volleyError) {
        feePointWeb.loadUrl("file:///android_asset/index.html");
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


       /* @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {

            //TODO something

            return true;
        }*/

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
