package com.bjypt.vipcard.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bjypt.vipcard.utils.CustomHurlStack;
import com.bjypt.vipcard.wxapi.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/8
 * Use by 基类
 */
public class MyApplication extends MultiDexApplication {
    public static RequestQueue requestQueue;
    public static IWXAPI mWxApi;
    private ArrayList<Activity> activities = new ArrayList<Activity>();
    private static MyApplication instance;
    private static Context mContext;



    private boolean is_upgrading; //true: 正在升级  false:服务正常

    @Override
    public void onCreate() {
        super.onCreate();
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
}
