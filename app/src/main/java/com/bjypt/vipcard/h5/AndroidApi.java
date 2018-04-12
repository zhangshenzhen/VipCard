package com.bjypt.vipcard.h5;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.JavascriptInterface;

import com.bjypt.vipcard.activity.BaseWebViewActivity;
import com.bjypt.vipcard.activity.CommonWebViewActivity;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.utils.ToastUtils;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by WANG427 on 2016/3/29.
 */
public class AndroidApi {
    private Context ctx;
    private JavaScriptListener javaScriptListener;
    public AndroidApi(Context context, JavaScriptListener javaScriptListener ) {
        this.ctx = context;
        this.javaScriptListener = javaScriptListener;
    }

    @JavascriptInterface
    public void popView() {
        if (ctx instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) ctx;
            baseActivity.finish();
//            AnimUtil.exitAnim(baseActivity);
        }
    }



    @JavascriptInterface
    public void setResumeRefresh() {
        javaScriptListener.isResumeRefresh(true);
    }

    @JavascriptInterface
    public void setTitle(final String title) {
        if (this.ctx instanceof BaseWebViewActivity) {
            ((BaseWebViewActivity) ctx).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BaseWebViewActivity baseWebViewActivity = (BaseWebViewActivity) ctx;
                    baseWebViewActivity.setTitle(title);
                }
            });
        }
    }



    @JavascriptInterface
    public  void webViewReload(){
        javaScriptListener.webViewReload();
    }



    @JavascriptInterface
    public void setNativeCacheData(String key, String value) {
        SharedPreferenceUtils.saveToSharedPreference(ctx,  key, value);
    }


    @JavascriptInterface
    public void showToast(String msg) {
        ToastUtils.showMessage(ctx, msg);
    }

    @JavascriptInterface
    public void pushView(String pushInfo) {
        Gson gson = new Gson();
        WebViewPushInfo webViewPushInfo = gson.fromJson(pushInfo, WebViewPushInfo.class);
        Intent intent = new Intent();
        intent.setClass(ctx, CommonWebViewActivity.class);
        intent.putExtra(CommonWebViewActivity.PUSHINFO, webViewPushInfo);
        ctx.startActivity(intent);
//        if (ctx instanceof Activity) {
//            if(ctx instanceof FragmentActivity){
//                AnimUtil.enterAnim((Activity) ctx);
//            }
//
//        }
    }

//    @JavascriptInterface
//    public void needLogin() {
//        if (ctx instanceof MyApplication) {
//            BaseActivity baseActivity = (BaseActivity) ctx;
//            Intent intent = new Intent();
//            intent.setAction(ActionCommon.LoginAction);
//            intent.setPackage(baseActivity.application.getPackageName());
//            baseActivity.startActivity(intent);
//            baseActivity.finish();
////            AnimUtil.enterAnim(baseActivity);
//        }
//    }

//    @JavascriptInterface
//    public void startWeChatPayment(String json) {
//        try {
//            JSONObject object = new JSONObject(json);
//            String appid = BaseApplication.application.isCustomApp() ? WXConstants.APP_ID : WXConstants.ServerAPPID;
//            IWXAPI api = WXAPIFactory.createWXAPI(ctx, appid);
//            PayReq req = new PayReq();
//            req.appId =appid;
//            req.partnerId = object.getString("partnerid");
//            req.prepayId = object.getString("prepayid");
//            req.packageValue = object.getString("package");
//            req.nonceStr = object.getString("noncestr");
//            req.timeStamp = object.getString("timestamp");
//            req.sign = object.getString("sign");
//            if (req.checkArgs()) {
//                boolean isReg = api.sendReq(req);
//                LogUtil.print("pay status: " + isReg);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }




}
