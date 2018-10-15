package com.bjypt.vipcard.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.TitleView;
import com.gallerypick.utils.SystemBarTintManager;
import com.orhanobut.logger.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.piwik.sdk.Tracker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/8
 * Use by 基类activity
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener, TitleView.TitleImageListener, Handler.Callback {

    /*成功*/
    public final int ALIPAY_SUCCESS = 9000;
    /*失败*/
    public final int ALIPAY_FAIL = 10000;
    /*待确认*/
    public final int ALIPAY_TO_BE_CONFIRMED = 8000;
    /*取消 */
    public final int ALIPAY_CANCEL = 6001;

    public final static int request_code_ccb = 998;

    protected MyApplication myApplication;
    private boolean isconnected = true;
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

    public static final int MIN_CLICK_DELAY_TIME = 600;
    long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onClickEvent(v);
        }

    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

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

    @Override
    public void ClickLeft() {
        // TODO Auto-generated method stub
    }

    @Override
    public void ClickRight() {
        // TODO Auto-generated method stub
    }

    private BroadCastReceiverUtils utils;
    private MyBroad myBroad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSystemBar();

        utils = new BroadCastReceiverUtils();
        myBroad = new MyBroad();
        utils.registerBroadCastReceiver(this, "re_dingwei", myBroad);
        myApplication = (MyApplication) getApplicationContext();
        MyApplication.getInstance().addActivity(this);
        mHandler = new Handler(this);
        setContentLayout();
        beforeInitView();
        initView();
        afterInitView();
        bindListener();
        isconnected = Wethod.isConnected(this);
        Log.e("liyunte", "isconnected----------" + isconnected);

    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        if (!isconnected){
           if (Wethod.isConnected(this)){
               Log.e("liyunte","isconnected----------"+isconnected);
               isconnected = true;
               isConntectedAndRefreshData();
           }

        }
    }*/

    /**
     * 获取手机厂商
     *
     * @return
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 跳转页面
     **/
    public void SkipIntent(Class myClass) {
        Intent intent = new Intent(this, myClass);
        startActivity(intent);
    }

    public void isConntectedAndRefreshData() {

    }

    /**
     * 存储数据到SharePreference
     **/
    public void saveDataToSharePreference(String key, String value) {
        SharedPreferenceUtils.saveToSharedPreference(this, key, value);
    }

    /**
     * 丛SharePreference中获取数据
     **/
    public String getFromSharePreference(String key) {
        return SharedPreferenceUtils.getFromSharedPreference(this, key);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        utils.UnRegisterBroadCastReceiver(this, myBroad);
        MyApplication.getInstance().deleteActivity(this);
    }


    public static final String LOG_TAG = "PayDemo";
    private Handler mHandler = null;
    //	private ProgressDialog mLoadingDialog = null;

    public static final int PLUGIN_VALID = 0;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;

    /*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
    private final String mMode = "00";

    /**
     * 银联支付调用的方法
     *
     * @author syj
     */
    public void goUnionpay(String tn) {

        Message msg = mHandler.obtainMessage();
        msg.obj = tn;
        mHandler.sendMessage(msg);
    }

    public void doStartUnionPayPlugin(Activity activity, String tn,
                                      String mode) {

    }

    public String getPkregister(){
        return SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.pkregister);
    }

    public String getPhene(){
        return SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.phoneno);
    }

    @Override
    public boolean handleMessage(Message msg) {


        String tn = "";
        Log.e("HHHH", "msg.obj:" + msg.obj + "  tn:" + tn);
        if (msg.obj == null || ((String) msg.obj).length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("错误提示");
            builder.setMessage("网络连接失败,请重试!");
            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            if (this.isFinishing()) {
                builder.create().show();
            }
        } else {
            tn = (String) msg.obj;
            /*************************************************
             * 步骤2：通过银联工具类启动支付插件
             ************************************************/
            doStartUnionPayPlugin(this, tn, mMode);
        }

        return false;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    int startpay(Activity act, String tn, int serverIdentifier) {
        return 0;
    }

    class MyBroad extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            isConntectedAndRefreshData();
        }
    }

    public Tracker getTracker() {
        return ((MyPiwikApplication) getApplication()).getTracker();
    }
}
