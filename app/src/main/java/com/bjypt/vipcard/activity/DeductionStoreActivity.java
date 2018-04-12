package com.bjypt.vipcard.activity;

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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;

/**
 * Created by 涂有泽 .
 * Date by 2016/8/10
 * Use by 抵扣商城
 */
public class DeductionStoreActivity extends BaseActivity implements VolleyCallBack {

    private WebView deductionWeb;
    private ChromeClient chomeClient;
    private RelativeLayout rl_financial_detail_back;
    private TextView tv_finalcial_detail;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_deduction_store);
    }

    @Override
    public void beforeInitView() {
        chomeClient = new ChromeClient();

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    Log.e("kill", "handleMessage:" + what);
                    deductionWeb.loadUrl("file:///android_asset/index.html");
                    break;
            }
        }
    };

    @Override
    public void initView() {
        tv_finalcial_detail = (TextView) findViewById(R.id.tv_finalcial_detail);
        tv_finalcial_detail.setText("兑换商城");
        rl_financial_detail_back = (RelativeLayout) findViewById(R.id.rl_financial_detail_back);
        deductionWeb = (WebView) findViewById(R.id.deduction_store_web);

        deductionWeb.setWebViewClient(new WebViewClient() {
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
    public void afterInitView() {
        WebSettings ws = deductionWeb.getSettings();
//        feePointWeb.setWebChromeClient(chomeClient);
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSupportMultipleWindows(true);
        deductionWeb.requestFocus();
        deductionWeb.getSettings().setJavaScriptEnabled(true);//添加对JavaScript支持
        /*Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(this,1, Config.web.system_infomatiob, params, this);*/
        deductionWeb.loadUrl(Config.web.deduction_store + getFromSharePreference(Config.userConfig.pkregister));
        deductionWeb.addJavascriptInterface(new AndroidJavaScripts(this), "jump");
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
        Log.e("woyaokk", "deductionWeb.getUrl():" + deductionWeb.getUrl() + "     ===" + Config.web.deduction_store);
        if (deductionWeb.canGoBack()) {
            if (deductionWeb.getUrl().equals(Config.web.deduction_store)) {
                super.onBackPressed();
            } else {
                deductionWeb.goBack();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
            deductionWeb.loadUrl(Config.web.deduction_store + getFromSharePreference(Config.userConfig.pkregister));
        }

    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 1) {
            deductionWeb.loadUrl("file:///android_asset/index.html");
        }

    }

    @Override
    public void onError(VolleyError volleyError) {
        deductionWeb.loadUrl("file:///android_asset/index.html");
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

    public class AndroidJavaScripts {
        private Context context;

        public AndroidJavaScripts(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void closeWindow() {
            Log.e("woyaokk", "ddd");
            finish();
        }

    }

    @Override
    public void isConntectedAndRefreshData() {
        deductionWeb.loadUrl(Config.web.deduction_store + getFromSharePreference(Config.userConfig.pkregister));
    }
}
