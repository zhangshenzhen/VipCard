package com.bjypt.vipcard.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.YuEBaoBuyActivity;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.FinancingListBean;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;
import com.sinia.orderlang.city.CityPicker;

import android.webkit.JavascriptInterface;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.os.StrictMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FinancingProjectFragment extends BaseFrament implements View.OnClickListener, VolleyCallBack {
    private WebView h5Web;
    private View view;
    String pkmuser="";
    private ProgressBar bar;
    final private static int REQUEST_BALANCE_INVEST_LIST = 20181456;
    final private static int REQUEST_BALANCE_INVEST_INFO = 20171312;
    Map<String,String> map=new HashMap<>();
    private ArrayList<FinancingListBean.ResultDataBean> lists = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_financing_project, container, false);
        return view;
    }
    @Override
    public void onSuccess(int reqcode, Object result) {
        Logger.d(result);
            switch (reqcode) {
                case REQUEST_BALANCE_INVEST_INFO:
                    String data = new JsonParser().parse(result.toString()).getAsJsonObject().getAsJsonPrimitive("resultData").getAsString();
                    loadFinancingList1(data);
                    break;
                case REQUEST_BALANCE_INVEST_LIST:
                    LogUtil.debugPrint("INVEST_LIST========" + result);
                    loadFinancingList(result);
                    break;
            }
    }
    private void loadFinancingList(Object result) {
        if (lists.size() > 0) {
            lists.clear();
        }
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            FinancingListBean financingListBean = objectMapper.readValue(result.toString(), FinancingListBean.class);
            lists.addAll(financingListBean.getResultData());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadFinancingList1(String result) {
            try{
                h5Web.loadDataWithBaseURL("http://m.qb-jr.com", result, "text/html", "utf-8", null);
                //
            } catch (Exception e) {
                e.printStackTrace();
            }




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
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        h5Web.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, true);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });

        h5Web.setWebViewClient(new WebViewClient() {
          @Override
            public void onPageFinished(WebView view,String url)
            {
                //CookieManager cookieManager=CookieManager.getInstance();
                //String CookieStr=cookieManager.getCookie(url);
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
               // Log.i("url:",url);
              if(url.indexOf("licai://")>=0)
                {
                    //Log.i("url:",url);
                    Integer position=Integer.parseInt(url.substring(9));
                    //position=position;
                    if (lists.get(position).getProductstatus() == 1) {
                        Intent intent = new Intent(view.getContext(), YuEBaoBuyActivity.class);
                        intent.putExtra("productName", lists.get(position).getProductname());
                        intent.putExtra("time", lists.get(position).getDiffdate());
                        intent.putExtra("productrate", lists.get(position).getProductrate());
                        intent.putExtra("investMoney", lists.get(position).getProducttotalmoney());
                        intent.putExtra("pkProduct", lists.get(position).getPkproduct());
                        intent.putExtra("pkmuser", pkmuser);
                        view.getContext().startActivity(intent);
                    }
                }
                    else if(url.indexOf("m.qb-jr.com/home")>=0)
                {
                    getBalanceInvestList();
                }
                else if(url.indexOf("m.qb-jr.com/")>=0&&url.length()<=21)
                {
                    getBalanceInvestList();
                }else{
                  view.loadUrl(url);
              }
                return true;
            }

        });
        getBalanceInvestList();
    }

    @Override
    public void afterInitView() {


    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    private void getBalanceInvestList() {
//        Map<String, String> params = new HashMap<>();
//        params.put("pkmuser", pkmuser);
        Wethod.httpPost(view.getContext(), REQUEST_BALANCE_INVEST_INFO, Config.web.yu_e_bao_index, null, this, View.GONE);
        Wethod.httpPost(view.getContext(), REQUEST_BALANCE_INVEST_LIST, Config.web.yu_e_bao_list, null, this,View.GONE);
    }

}