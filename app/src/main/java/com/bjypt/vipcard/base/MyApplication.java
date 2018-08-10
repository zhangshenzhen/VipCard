package com.bjypt.vipcard.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bjypt.vipcard.activity.shangfeng.util.MapLocationUtil;
import com.bjypt.vipcard.activity.shangfeng.util.StringUtils;
import com.bjypt.vipcard.activity.shangfeng.util.ToastMgr;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.CustomHurlStack;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.utils.SsX509TrustManager;
import com.bjypt.vipcard.wxapi.Constants;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;


import org.piwik.sdk.TrackerConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/8
 * Use by 基类
 */
public class MyApplication extends MyPiwikApplication {
    public static RequestQueue requestQueue;
    public static IWXAPI mWxApi;
    private ArrayList<Activity> activities = new ArrayList<Activity>();
    private static MyApplication instance;
    private static Context mContext;

    private MapLocationUtil mapLocationUtil;


    private boolean is_upgrading; //true: 正在升级  false:服务正常

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registToWX();
        MultiDex.install(this);
//        MyException.getInstance().init(getApplicationContext());
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("MyApplication", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        QbSdk.initX5Environment(getApplicationContext(), cb);
        MobclickAgent.setCatchUncaughtExceptions(true);
        mContext = getApplicationContext();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);  //初始化
        requestQueue = Volley.newRequestQueue(getApplicationContext(), new CustomHurlStack());

        initOkHttp();
        initToastMgr();
    }

    public  MapLocationUtil getMapLocationUtil(){
        if(mapLocationUtil == null){
            mapLocationUtil = MapLocationUtil.getInstance(this);
        }
        return mapLocationUtil;
    }

    public void initOkHttp() {
        File sdcache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(SsX509TrustManager.getSSLSocketFactory())
                .hostnameVerifier(SsX509TrustManager.getHostnameVerifier())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize))
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 初始化Toast
     */
    private void initToastMgr() {
        ToastMgr.ToastEnum.builder.init(this);
    }



    public static RequestQueue getHttpQueue() {
        return requestQueue;
    }

    //单例模式中获取唯一的MyApplication实例
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }

        return instance;
    }

    public void exit() {
        try {
            for (Activity activity : activities) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }


    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public Activity getFirstActivity(){
        return activities.get(0);
    }

    //从容器中删除Activity
    public void deleteActivity(Activity activity) {
        activities.remove(activity);
    }

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(Constants.APP_ID);
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getResourceString(int stringId) {
        String str = "";
        try {
            str = mContext.getResources().getString(stringId);
        } catch (Resources.NotFoundException e) {
            Logger.e(e, "找不到指定资源", stringId);
        }
        return str;
    }

    /**
     * true: 正在升级  false:服务正常
     * @return
     */
    public boolean isUpgrading(){
        return is_upgrading;
    }

    public void setUpgrading(boolean is_upgrading){
        this.is_upgrading = is_upgrading;
    }

    @Override
    public TrackerConfig onCreateTrackerConfig() {
        return TrackerConfig.createDefault("http://123.57.232.188:19090/piwik/piwik.php", 2);
    }

}
