package com.bjypt.vipcard.utils;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * Created by 涂有泽 .
 * Date by 2016/8/15
 * Use by
 */
public class WebInterface {
    Activity mContext;
    WebInterface(Activity mContext){
        this.mContext = mContext;
    }

    @JavascriptInterface
    public void exitH5(){
        mContext.finish();
    }
}
