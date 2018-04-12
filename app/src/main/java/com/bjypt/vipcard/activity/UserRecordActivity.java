package com.bjypt.vipcard.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

public class UserRecordActivity extends BaseActivity {

    private ChromeClient chomeClient;
    private RelativeLayout back;
    private WebView webView;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    webView.loadUrl("file:///android_asset/index.html");
                    break;
            }
        }
    };

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_user_record);
    }

    @Override
    public void beforeInitView() {
        chomeClient = new ChromeClient();
    }

    @Override
    public void initView() {
        back = (RelativeLayout) findViewById(R.id.user_record_back);
        webView = (WebView) findViewById(R.id.user_record_web);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("kill", "errorCode:" + errorCode);
                mHandler.sendEmptyMessage(1);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("kill", "onPageStarted:" + url);
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

    @Override
    public void afterInitView() {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSupportMultipleWindows(true);
        webView.requestFocus();
        webView.getSettings().setJavaScriptEnabled(true);//添加对JavaScript支持
       /* Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(this,1, Config.web.system_infomatiob, params, this);*/
//        feePointWeb.loadUrl("http://192.168.1.112:7778/hyb/ws/post/getUserBillsMainH5?pkregister=5a77ae772cb34106b334338673956c44&type_main=0");
        webView.loadUrl(Config.web.user_record_h5 + getFromSharePreference(Config.userConfig.pkregister));
        webView.addJavascriptInterface(new AndroidJavaScript(this), "jump");
    }

    @Override
    public void bindListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClickEvent(View v) {

    }

    class ChromeClient extends WebChromeClient {

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
            finish();
        }
    }

    public class AndroidJavaScript {
        private Context context;

        public AndroidJavaScript(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void closeWindowBill() {
            Log.e("woyaokk", "ddd");
            finish();
        }

    }

}
