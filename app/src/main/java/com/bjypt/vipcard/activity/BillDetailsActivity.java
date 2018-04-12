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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;

public class BillDetailsActivity extends BaseActivity implements VolleyCallBack {

    private RelativeLayout rl_bill_detail_back;
    private WebView feePointWeb;
    private ChromeClient chomeClient;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    Log.e("kill", "handleMessage:" + what);
                    feePointWeb.loadUrl("file:///android_asset/index.html");
                    break;
            }
        }
    };

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_bill_details);
    }

    @Override
    public void beforeInitView() {
        chomeClient = new ChromeClient();
    }

    @Override
    public void initView() {
        rl_bill_detail_back = (RelativeLayout) findViewById(R.id.rl_bill_detail_back);
        feePointWeb = (WebView) findViewById(R.id.web_bill_detail);
        feePointWeb.setWebViewClient(new WebViewClient() {
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

    @SuppressLint("JavascriptInterface")
    @Override
    public void afterInitView() {
        WebSettings ws = feePointWeb.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSupportMultipleWindows(true);
        feePointWeb.requestFocus();
        feePointWeb.getSettings().setJavaScriptEnabled(true);//添加对JavaScript支持
       /* Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(this,1, Config.web.system_infomatiob, params, this);*/
//        feePointWeb.loadUrl("http://192.168.1.112:7778/hyb/ws/post/getUserBillsMainH5?pkregister=5a77ae772cb34106b334338673956c44&type_main=0");
        feePointWeb.loadUrl(Config.web.recharge_account_ + getFromSharePreference(Config.userConfig.pkregister) + "&type_main=2");
        feePointWeb.addJavascriptInterface(new AndroidJavaScript(this), "jump");

    }

    @Override
    public void bindListener() {
        rl_bill_detail_back.setOnClickListener(new View.OnClickListener() {
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
        Log.e("woyaokk", "feePointWeb.getUrl():" + feePointWeb.getUrl() + "     ===" + Config.web.my_bill_detail);
        if (feePointWeb.canGoBack()) {
            Log.e("woyaokk", "1111");
            if (feePointWeb.getUrl().equals(Config.web.my_bill_detail + getFromSharePreference(Config.userConfig.pkregister) + "&type_main=0")) {
//            if(feePointWeb.getUrl().equals("http://192.168.1.112:7778/hyb/ws/post/getUserBillsMainH5?pkregister=5a77ae772cb34106b334338673956c44&type_main=0")){
                Log.e("woyaokk", "2222");
                super.onBackPressed();
            } else {
                Log.e("woyaokk", "3333");
                feePointWeb.goBack();
            }
        } else {
            Log.e("woyaokk", "4444");
            super.onBackPressed();
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
            feePointWeb.loadUrl(Config.web.my_bill_detail + getFromSharePreference(Config.userConfig.pkregister) + "&type_main=0");
        }

    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 1) {
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

    @Override
    public void isConntectedAndRefreshData() {
        feePointWeb.loadUrl(Config.web.my_bill_detail + getFromSharePreference(Config.userConfig.pkregister) + "&type_main=0");
    }
}
