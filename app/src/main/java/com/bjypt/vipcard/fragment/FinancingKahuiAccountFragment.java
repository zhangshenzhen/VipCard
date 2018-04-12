package com.bjypt.vipcard.fragment;

import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;


public class FinancingKahuiAccountFragment extends BaseFrament implements View.OnClickListener, VolleyCallBack {
    private WebView h5Web;
    private View view;

    private ProgressBar bar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_financing_calc, container, false);
        return view;
    }

    @Override
    public void onSuccess(int reqcode, Object result) {

    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {

        h5Web = (WebView) view.findViewById(R.id.life_service_web);
        bar = (ProgressBar) view.findViewById(R.id.myProgressBar);


        WebSettings webSettings = h5Web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        h5Web.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, true);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });

        h5Web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                if(url!=null){
                    String fun="javascript:function getClass(parent,sClass) { var aEle=parent.getElementsByTagName('div'); var aResult=[]; var i=0; for(i<0;i<aEle.length;i++) { if(aEle[i].className==sClass) { aResult.push(aEle[i]); } }; return aResult; } ";

                    view.loadUrl(fun);

                    String fun2="javascript:function hideOther() {getClass(document,'footer_ad J_downApp')[0].style.display='none';;}";

                    view.loadUrl(fun2);

                    view.loadUrl("javascript:hideOther();");
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                super.onReceivedError(view, errorCode, description, failingUrl);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.indexOf("m.qb-jr.com/bid")>=0||url.length()<=21)
                {
                    //转道另一个fragment
                    FragmentManager fm = getParentFragment().getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();


                    transaction.replace(R.id.id_content, new FinancingProjectFragment () );

                    // 事务提交
                    transaction.commit();

                }
                    else
                    view.loadUrl(url);
                return true;
            }

        });
    }

    @Override
    public void afterInitView() {
        h5Web.loadUrl("http://m.qb-jr.com/my/info");
    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }
}