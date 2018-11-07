package com.bjypt.vipcard.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.WelcomeBean;
import com.bjypt.vipcard.model.ZbarMerchantData;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.GaoDeMapLocation;
import com.bjypt.vipcard.utils.ImageUtils;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.categoryview.AppCategoryGuideView;
import com.gallerypick.utils.SystemBarTintManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/9/26
 * Use by 首页欢饮页面
 */
public class FirstActivity extends AppCompatActivity implements VolleyCallBack<String>, ActivityCompat.OnRequestPermissionsResultCallback, AppCategoryGuideView.OnChangePageLastListener, View.OnClickListener {

    private TextView textView;
    //跳过引导页
    private ImageView mSkip;
    public static final int REQUEST_ACCESS_FINE_LOCATION = 0;
    private ZbarMerchantData zbarMerchantData;
    public static int HOME_IMAGE_FLAG = 1;
    public static int WXPAY_PHONE = 1;
    public static int WXPAY_XX = 1;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private GaoDeMapLocation gaoDeMapLocation;

    private AppCategoryGuideView appCategoryGuideView;
    private RelativeLayout el_skip_welcome;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        setContentView(R.layout.activity_first);
        SharedPreferenceUtils.saveToSharedPreference(this, Config.userConfig.CURRENT_CITY, "阜阳");
        if (Build.VERSION.SDK_INT >= 23) {
            if (!getFromSharePreference("dingwei").equals("") && getFromSharePreference("dingwei") != null && getFromSharePreference("dingwei").equals("2")) {
                gaoDeMapLocation = new GaoDeMapLocation(this);
                gaoDeMapLocation.startLocation();
                mayRequestLocation();//android 6.0获取用户许可打开定位权限
            } else {
                if (getFromSharePreference("gengxinxiazai").equals("1")) {
                    gaoDeMapLocation = new GaoDeMapLocation(this);
                    gaoDeMapLocation.startLocation();
                } else {
                    Log.e("ceshi", "3");
                    gaoDeMapLocation = new GaoDeMapLocation(MyApplication.getContext());
                    gaoDeMapLocation.startLocation();
                   // gaoDeMapLocation.StartGaoDe();
                    mayRequestLocation();//android 6.0获取用户许可打开定位权限
                }
            }
        } else {
            //            高德定位
            Log.e("ceshi", "4");
            gaoDeMapLocation = new GaoDeMapLocation(this);
            gaoDeMapLocation.startLocation();

        }

        beforeInitView();
        initView();
        afterInitView();
        bindListener();
    }

    /**
     * 丛SharePreference中获取数据
     **/
    public String getFromSharePreference(String key) {
        return SharedPreferenceUtils.getFromSharedPreference(this, key);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void mayRequestLocation() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("获取位置信息");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("修改或删除您的SD卡中的内容");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("读取您的SD卡中的内容");
        if (!addPermission(permissionsList, Manifest.permission.BLUETOOTH_ADMIN)) {

        }
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE)) {
            permissionsNeeded.add("读取手机信息");
        }
        if (permissionsList.size() > 0) {
            Log.e("ceshi", "5");
            if (permissionsNeeded.size() > 0) {
                Log.e("ceshi", "6");
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    Log.e("cashi", "permissionsNeeded");
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_ACCESS_FINE_LOCATION);
                if (gaoDeMapLocation == null) {
                    gaoDeMapLocation = new GaoDeMapLocation(this);
                    gaoDeMapLocation.startLocation();
                }
                return;
            } else {
                Log.e("ceshi", "7");
                gaoDeMapLocation = new GaoDeMapLocation(this);
                gaoDeMapLocation.startLocation();
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        Log.e("ceshi", "onRequestPermissionsResult" + requestCode);
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION: {
                Log.e("ceshi", "8");
                //                startLocation();
                if (gaoDeMapLocation == null) {
                    gaoDeMapLocation = new GaoDeMapLocation(this);
                    Log.e("ceshi", "9");

                }

                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.BLUETOOTH_ADMIN, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        ) {
                } else {
                }
                saveDataToSharePreference("dingwei", "2");
                Log.e("ceshi", "10");

                gaoDeMapLocation.startLocation();
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void beforeInitView() {
        Map<String, String> params = new HashMap<>();
        params.put("clientIp", "");
        Wethod.httpPost(FirstActivity.this, 1314, Config.web.zbar_merchant, params, this, View.GONE);
    }

    public void initView() {
        textView = (TextView) findViewById(R.id.guideTv);
        el_skip_welcome = (RelativeLayout) findViewById(R.id.el_skip_welcome);
        mSkip = (ImageView) findViewById(R.id.skip_welcome);
        appCategoryGuideView = (AppCategoryGuideView) findViewById(R.id.appCategoryGuideView);
    }


    public void afterInitView() {
        appCategoryGuideView.setOnChangePageLastListener(this);
        appCategoryGuideView.reload();
    }

    public void bindListener() {
        gaoDeMapLocation.setDingWeiSuccessListener(new GaoDeMapLocation.DingWeiSuccessListener() {
            @Override
            public void onDingWeiSuccessListener() {
                //定位成功的监听
                Log.e("liyunte", "定位成功！-------------");
            }
        });
        el_skip_welcome.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.el_skip_welcome:
                if (getFromSharePreference(Config.userConfig.citycode) != null) {
                    if (zbarMerchantData != null) {
                        Intent intent = new Intent(FirstActivity.this, NewSubscribeDishesActivity.class);
                        intent.putExtra("pkmuser", zbarMerchantData.getResultData());
                        intent.putExtra("distance", "*");
                        startActivity(intent);
                        finish();
                    } else {
                        HOME_IMAGE_FLAG = 2;
                        startActivity(new Intent(FirstActivity.this, MainActivity.class));
                        finish();
                    }

                }
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (gaoDeMapLocation != null) {
            gaoDeMapLocation.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 1314) {
            try {
                zbarMerchantData = getConfiguration().readValue(result.toString(), ZbarMerchantData.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }


    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    public void onLastEvent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!isFinishing() && !isDestroyed()) {
                startActivity(new Intent(FirstActivity.this, MainActivity.class));
                finish();
            }
        }

    }

    /**
     * 存储数据到SharePreference
     **/
    public void saveDataToSharePreference(String key, String value) {
        SharedPreferenceUtils.saveToSharedPreference(this, key, value);
    }

    /***********
     * jackson解析配置信息
     ************/
    public ObjectMapper getConfiguration() {
        return ObjectMapperFactory.createObjectMapper();
    }



}
