package com.bjypt.vipcard.activity.crowdfunding.projectdetail;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.primary.commonweb.AndroidInterface;
import com.bjypt.vipcard.activity.shangfeng.primary.commonweb.BaseAgentWebActivity;
import com.bjypt.vipcard.activity.shangfeng.primary.commonweb.DefaultAndroidInterface;
import com.bjypt.vipcard.base.BaseFragment;
import com.bjypt.vipcard.fragment.crowdfunding.SubCrowdfundingFragment;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfTabData;

public class ProjectContentFragment extends BaseFragment{

    View view;
    String url;

    WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        url = getArguments().getString("url");
        view = inflater.inflate(R.layout.fragment_crowdfunding_project_detail, container, false);
        return view;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        webView = view.findViewById(R.id.webView);
    }

    @Override
    public void afterInitView() {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSupportMultipleWindows(true);
        ws.setGeolocationEnabled(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        ws.setGeolocationDatabasePath(getFilesDir().getPath());
        webView.requestFocus();
        webView.addJavascriptInterface( new DefaultAndroidInterface(this.getActivity(), this.getContext()),"android");
        webView.setWebViewClient(new WebViewClient() {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    public static ProjectContentFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        ProjectContentFragment fragment = new ProjectContentFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
