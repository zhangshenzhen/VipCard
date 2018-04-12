package com.bjypt.vipcard.umeng;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by 涂有泽 .
 * Date by 2016/5/18
 * Use by 友盟错误统计分析
 */
public class UmengCountContext {
    private static final String CLIENT_ACTIVE = "StormLockActive";

    public static void setDebugMode(boolean isDebug){
        MobclickAgent.setDebugMode(isDebug);
    }

    public static void init(Context context){
        MobclickAgent.updateOnlineConfig(context);
    }

    public static void uploadLeftoverCounts(Context context){
    }

    public static void active(Context context){
        MobclickAgent.onEvent(context,CLIENT_ACTIVE);
    }

    public static void onResume(Context context){
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context){
        MobclickAgent.onPause(context);
    }

    public static void onPageStart(String pageName){
        MobclickAgent.onPageStart(pageName);
    }

    public static void onPageEnd(String pageName){
        MobclickAgent.onPageEnd(pageName);
    }

    public static void onEvent(Context context,String eventId){
        MobclickAgent.onEvent(context, eventId);
    }

    public static void onEvent(Context context,String eventId,String extra){
        MobclickAgent.onEvent(context,eventId,extra);
    }
}
