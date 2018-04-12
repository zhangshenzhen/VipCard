package com.bjypt.vipcard.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.h5.WebViewPushInfo;
import com.sinia.orderlang.utils.StringUtil;


/**
 * Created by WANG427 on 2016/3/22.
 */
public class CommonWebViewActivity extends BaseWebViewActivity {

    private TextView tv_right;

    WebViewPushInfo webViewPushInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_common_webview);
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.in_right_enter, R.anim.in_left_exit);
//        StatusBarCompat.compat(this, getResources().getColor(R.color.red_txt));
        findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webViewPushInfo = (WebViewPushInfo) getIntent().getSerializableExtra(PUSHINFO);
        if (webViewPushInfo == null) {
            finish();
            return;
        }
        init((WebView) findViewById(R.id.webView));

        setTitle(webViewPushInfo.getTitle());
        tv_right = (TextView) findViewById(R.id.tv_right);
        if (webViewPushInfo.getBtndisplay() == WebViewPushInfo.DISPLAY[1]) {
            tv_right.setVisibility(View.VISIBLE);
            if (StringUtil.isNotEmpty(webViewPushInfo.getBtntext())) {
                tv_right.setText(webViewPushInfo.getBtntext());
            }
            tv_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webView.loadUrl("javascript:" + webViewPushInfo.getBtnevent() + "");
                }
            });
        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        permissionsHelperCamera.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    @Override
    public String getUrl() {
        return webViewPushInfo.getUrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        AnimUtil.exitAnim(this);
    }
}
