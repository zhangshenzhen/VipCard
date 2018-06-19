package com.bjypt.vipcard.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.dialog.IntegralToast;
import com.bjypt.vipcard.model.CommentDetailsClBean;
import com.bjypt.vipcard.model.IntegralBean;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.ShareSDKUtil;
import com.bjypt.vipcard.view.LoadingPageDialog;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.widget.ShareSelelctPopupWindow;
import com.just.agentweb.AgentWeb;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by 崔龙 on 2017/11/29.
 * <p>
 * 朋友圈详情web
 */

public class FriendsCircleWeb extends BaseActivity implements PlatformActionListener, VolleyCallBack {
    private LinearLayout ll_my_ll;
    private RelativeLayout rl_rl;
    private AgentWeb mAgentWeb;
    private String mUrl;
    private View view_black;
    private ShareSelelctPopupWindow mPopupWindow;
    private LoadingPageDialog loadingPageDialog;
    private static final int REQUEST_INTEGRAL = 784761461;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_friends_circle_web);
        loadingPageDialog = new LoadingPageDialog(this);
        ShareSDK.initSDK(this);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
    }

    @Override
    public void initView() {
        ll_my_ll = (LinearLayout) findViewById(R.id.ll_my_ll);
        view_black = findViewById(R.id.view_black_2);
    }

    @Override
    public void afterInitView() {
        mAgentWeb = AgentWeb.with(this)//传入Activity or Fragment
                .setAgentWebParent(ll_my_ll, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()// 使用默认进度条
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()//
                .ready()
                .go(mUrl);
        showDialog();
        mAgentWeb.getJsInterfaceHolder().addJavaObject("hybApp", new AndroidInterface(mAgentWeb, this));
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        ObjectMapper objectMapper2 = ObjectMapperFactory.createObjectMapper();
        try {
            IntegralBean integralBean = objectMapper2.readValue(result.toString(), IntegralBean.class);
            if (StringUtil.isNotEmpty(integralBean.getResultData().getVirtualBalance())) {
                String resultData = integralBean.getResultData().getVirtualBalance();
                if (!resultData.equals("0")) {
                    IntegralToast.getIntegralToast().ToastShow(FriendsCircleWeb.this, null, "已分享，加" + resultData + "积分");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        ObjectMapper objectMapper2 = ObjectMapperFactory.createObjectMapper();
        try {
            CommentDetailsClBean commentDetailsClBean = objectMapper2.readValue(result.toString(), CommentDetailsClBean.class);
            if (StringUtil.isNotEmpty(commentDetailsClBean.getResultData())) {
                String resultData = commentDetailsClBean.getResultData();
                ToastUtil.showToast(FriendsCircleWeb.this, resultData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        ToastUtil.showToast(this, "请检查您的网络");
    }

    class AndroidInterface {

        private Handler deliver = new Handler(Looper.getMainLooper());

        private AgentWeb agent;
        private Context context;

        public AndroidInterface(AgentWeb agent, Context context) {
            this.agent = agent;
            this.context = context;
        }

        @JavascriptInterface
        public void showToast(final String msg) {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(context, msg);
                }
            });
        }

        @JavascriptInterface
        public String getUid() {
            return getFromSharePreference(Config.userConfig.uid);
        }

        @JavascriptInterface
        public void finish() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    FriendsCircleWeb.this.finish();
                }
            });
        }

        @JavascriptInterface
        public void goNewPages(final String json) {
            LogUtil.debugPrint("goNewPages" + json);
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.has("androidClass")) {
                            String androidClass = jsonObject.getString("androidClass");
                            try {
                                Class aClass = Class.forName(androidClass);
                                Intent intent = new Intent();
                                JSONObject params = jsonObject.getJSONObject("params");
                                Iterator<String> iterator = params.keys();
                                while (iterator.hasNext()) {
                                    String key = iterator.next();
                                    Object value = params.get(key);
                                    if (value instanceof String) {
                                        intent.putExtra(key, value.toString());
                                    } else if (value instanceof Integer) {
                                        intent.putExtra(key, (Integer) value);
                                    } else {
                                        JSONArray jsonArray = (JSONArray) value;
                                        if (jsonArray != null) {
                                            ArrayList<String> item = new ArrayList<String>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                item.add(jsonArray.getString(i));
                                            }
                                            intent.putStringArrayListExtra(key, item);
                                        }

                                    }
                                    //do something
                                }
                                intent.setClass(FriendsCircleWeb.this, aClass);
                                startActivity(intent);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }

        @JavascriptInterface
        public void showDialog() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    showDialog();
                }
            });
        }

        @JavascriptInterface
        public void dismissDialog() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    cancelDialog();
                }
            });
        }

        @JavascriptInterface
        public void share(final String username, final String subject, final String picUrl) {
            LogUtil.debugPrint("share: " + subject);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPopupWindow = new ShareSelelctPopupWindow(FriendsCircleWeb.this, new View.OnClickListener() {
                        public void onClick(View v) {
                            mPopupWindow.dismiss();
                            switch (v.getId()) {
                                case R.id.ll_wechat_share:
                                    ShareSDKUtil.shareWechatWeb(FriendsCircleWeb.this, picUrl, mUrl, MyApplication.mWxApi, 1, username, subject);
                                    break;
                                case R.id.ll_wechat_circle_share:
                                    ShareSDKUtil.shareWechatWeb(FriendsCircleWeb.this, picUrl, mUrl, MyApplication.mWxApi, 2, username, subject);
                                    break;
                                case R.id.ll_qq_friends_share:
                                    ShareSDKUtil.shareQQWeb(FriendsCircleWeb.this, picUrl, mUrl, username, subject);
                                    break;
                                case R.id.ll_qq_zone_share:
                                    ShareSDKUtil.shareQZoneWeb(FriendsCircleWeb.this, picUrl, mUrl, username, subject);
                                    break;
                                default:
                                    view_black.setVisibility(View.GONE);
                                    break;
                            }
                        }
                    });
                    mPopupWindow.showAtLocation(findViewById(R.id.rl_rl), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    view_black.setVisibility(View.VISIBLE);
                    mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            view_black.setVisibility(View.GONE);
                        }
                    });
                }
            });

        }

    }


    private void showDialog() {
        if (!loadingPageDialog.isShowing()) {
            loadingPageDialog.show();
        }
    }

    private void cancelDialog() {
        if (loadingPageDialog.isShowing()) {
            loadingPageDialog.dismiss();
        }
    }


    @Override
    public void bindListener() {
    }

    @Override
    public void onClickEvent(View v) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    //WebViewClient
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            cancelDialog();
            super.onPageFinished(webView, s);
        }
    };

    //WebChromeClient
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
        }
    };
    // 为弹出窗口实现监听类
//    private View.OnClickListener itemsOnClick =

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        requestIntegral();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Toast.makeText(FriendsCircleWeb.this, "分享失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Toast.makeText(FriendsCircleWeb.this, "取消分享", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingPageDialog.dismiss();
    }

    /**
     * 请求积分
     */
    private void requestIntegral() {
        Map<String, String> param = new HashMap<>();
        param.put("uid", getFromSharePreference(Config.userConfig.uid));
        param.put("action", "share");
        Wethod.httpPost(this, REQUEST_INTEGRAL, Config.web.request_credit, param, this);
    }
}
