package com.bjypt.vipcard.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;

/**
 * Created by 涂有泽 .
 * Date by 2016/9/28
 * Use by 应用宝下载
 */
public class DownLoadActivity extends BaseActivity{

    private WebView webView;
    private RelativeLayout mBack;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_use_pack);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        webView  = (WebView) findViewById(R.id.use_pack_webview);
        mBack = (RelativeLayout) findViewById(R.id.use_pack_back);
    }

    @Override
    public void afterInitView() {
        webView.loadUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.bjypt.vipcard");
    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.use_pack_back:
                finish();
                break;
        }
    }
}
