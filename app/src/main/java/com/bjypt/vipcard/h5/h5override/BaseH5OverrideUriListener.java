package com.bjypt.vipcard.h5.h5override;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.sinia.orderlang.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public abstract  class BaseH5OverrideUriListener implements  H5OverrideUrlListener  {

    public static final String closeH5ParamKey = "closeH5";
    public static final String nativeClassParamKey = "androidclass";
    public static final String needLoginParamKey = "needlogin";//Y 需要登陆 N不需要登陆
    public static final String h5UrlKey = "life_url";

    protected Uri mUri;
    public BaseH5OverrideUriListener(Uri uri){
        this.mUri = uri;
    }

    @Override
    public void process(Activity context) {
        boolean next = isLogin(context);
        if(next){
            handler(context);
        }
        closeSelf(context);
    }

    @Override
    public String getQuery() {
        return mUri.getQuery();
    }

    public boolean excludeParamKey(String key){
        List<String> excludeParamKeys = new ArrayList<>();
        excludeParamKeys.add(closeH5ParamKey);
        excludeParamKeys.add(nativeClassParamKey);
        excludeParamKeys.add(needLoginParamKey);
//        excludeParamKeys.add(h5UrlKey);
        return  excludeParamKeys.contains(key);
    }

    /**
     * 是否已经登陆
     * @param activity
     * @return
     */
    public boolean isLogin(Activity activity){
        String needLogin = mUri.getQueryParameter(needLoginParamKey);
        String loginStatus = SharedPreferenceUtils.getFromSharedPreference(activity,Config.userConfig.is_Login);
        if("Y".equals(needLogin)){
            if(!"Y".equals(loginStatus)){
                activity.startActivity(new Intent(activity, LoginActivity.class));
                return false;
            }
        }
        return true;
    }

    public void closeSelf(Activity activity){
        String needClose = mUri.getQueryParameter(closeH5ParamKey);
        if("Y".equals(needClose)){
            activity.finish();
        }
    }
}
