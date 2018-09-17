package com.bjypt.vipcard.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.TitleView;
import com.gallerypick.utils.SystemBarTintManager;

import org.codehaus.jackson.map.ObjectMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by 涂有泽 .
 * Date by 2016/3/29
 * Use by
 */
public abstract class BaseFraActivity extends FragmentActivity implements View.OnClickListener, TitleView.TitleImageListener {
    protected MyApplication myApplication;
    private BroadCastReceiverUtils utils;
    private MyBroad myBroad;
    private SystemBarTintManager tintManager;


    /**
     * 设置布局文件
     **/
    public abstract void setContentLayout();

    /**
     * 在实例化布局前处理的逻辑
     **/
    public abstract void beforeInitView();

    /**
     * 实例化布局文件/组件
     **/
    public abstract void initView();

    /**
     * 在实例化布局后处理的逻辑
     **/
    public abstract void afterInitView();

    /**
     * 绑定监听事件
     **/
    public abstract void bindListener();

    /**
     * 点击事件
     **/
    public abstract void onClickEvent(View v);

    //    public static final int MIN_CLICK_DELAY_TIME = 100;
//    long lastClickTime = 0;
    @Override
    public void onClick(View v) {
//        long currentTime = Calendar.getInstance().getTimeInMillis();
//        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
//            lastClickTime = currentTime;
        onClickEvent(v);
//        }

    }

    @Override
    public void ClickLeft() {
        // TODO Auto-generated method stub
    }

    @Override
    public void ClickRight() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSystemBar();

        utils = new BroadCastReceiverUtils();
        myBroad = new MyBroad();
        utils.registerBroadCastReceiver(this, "re_dingwei", myBroad);
        myApplication = (MyApplication) getApplicationContext();
        MyApplication.getInstance().addActivity(this);
        setContentLayout();
//        setStatusBarTranslucent();
        beforeInitView();
        initView();
        afterInitView();
        bindListener();
    }

//    //适配4.4及以上的沉浸式
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private void setStatusBarTranslucent() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(R.color.theme_color);
//        }
//    }

    public void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        // 创建状态栏的管理实例
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);// 激活状态栏设置
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.transparency));//设置状态栏颜色

        // false 状态栏字体颜色是白色 true 颜色是黑色
        setStatusBarDarkMode(true, this);
    }

    /**
     * 获取手机厂商
     * @return
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 跳转页面
     **/
    public void SkipIntent(Class myClass) {
        Intent intent = new Intent(this, myClass);
        startActivity(intent);
    }

    /**
     * 存储数据到SharePreference
     **/
    public void SaveDataToSharePreference(String key, String value) {
        SharedPreferenceUtils.saveToSharedPreference(this, key, value);
    }

    /**
     * 丛SharePreference中获取数据
     **/
    public String getFromSharePreference(String key) {
        return SharedPreferenceUtils.getFromSharedPreference(this, key);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        utils.UnRegisterBroadCastReceiver(this, myBroad);
        MyApplication.getInstance().deleteActivity(this);
    }

    public String getPkregister(){
        return SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.pkregister);
    }

    class MyBroad extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            isConntectedAndRefreshData();
        }
    }

    public void isConntectedAndRefreshData() {

    }

    /***********
     * jackson解析配置信息
     ************/
    public ObjectMapper getConfiguration() {
        //        ObjectMapper objectMapper = new ObjectMapper();
        //        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        return ObjectMapperFactory.createObjectMapper();
    }

    /**
     * 利用反射获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
