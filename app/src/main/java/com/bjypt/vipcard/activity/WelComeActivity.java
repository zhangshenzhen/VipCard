package com.bjypt.vipcard.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.LocationDingweiBean;
import com.bjypt.vipcard.model.WelcomeBean;
import com.bjypt.vipcard.model.ZbarMerchantData;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.ImageUtils;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/9
 * Use by 欢迎页面
 */
public class WelComeActivity extends BaseActivity implements VolleyCallBack, AMapLocationListener {

    private LinearLayout ll;
    private List<String> imageUrl;
    private List<ImageView> imageViews;//滑动图片的集合
    private List<View> dots;//图片标题正文下的点
    private int currentItem = 0;//当前图片的索引号
    private ImageView dot;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private ViewPager viewPager;
    private TextView tv_toMain;

    public static final int REQUEST_ACCESS_FINE_LOCATION = 0;

    String permissions[] = {Manifest.permission.RECORD_AUDIO, Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 开始定位
     */
    public final static int MSG_LOCATION_START = 0;
    /**
     * 定位完成
     */
    public final static int MSG_LOCATION_FINISH = 1;
    /**
     * 停止定位
     */
    public final static int MSG_LOCATION_STOP = 2;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private ZbarMerchantData zbarMerchantData;
    private Runnable runnable;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_welcome);
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为低功耗模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);
        if (Build.VERSION.SDK_INT >= 23) {
            mayRequestLocation();//android 6.0获取用户许可打开定位权限
            Log.e("jhjh", "进入");
        } else {
            startLocation();
        }
        //        checkPermissions();
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
        if (!addPermission(permissionsList, Manifest.permission.BLUETOOTH)) {

        }
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS)) {
            permissionsNeeded.add("读取联系人");
        }
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE)) {
            permissionsNeeded.add("读取手机信息");
        }

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                //                String message = "此应用需要获取" + permissionsNeeded.get(0) + "的权限";
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    //                    message = message + ", " + permissionsNeeded.get(i);
                    //                showMessageOKCancel(message,
                    //                        new DialogInterface.OnClickListener() {
                    //                            @Override
                    //                            public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_ACCESS_FINE_LOCATION);
                Log.e("jhjh", "点击了");
                //                            }
                //                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(WelComeActivity.this)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", null)
                .create()
                .show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION: {
                startLocation();
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.BLUETOOTH_ADMIN, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.BLUETOOTH, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



/*
    */

    /**
     * 检测当前权限是否注册
     *//*
    public void checkPermissions(){
        //版本判断，只有当手机系统大于23时，才需要判断权限是否注册
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){

                //检查该权限是否已经获取
                int p = ContextCompat.checkSelfPermission(this,permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            Log.e("oooo","pppp:"+p);
                if(p !=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, permissions, 321);
                    Log.e("oooo","执行");
                }
        }
    }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode == 321){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    Log.e("oooo", "bbbbbbbb:" + b);
                    startLocation();
                }
            }
        }

    public void checkPermission(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//请求权限
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.RECORD_AUDIO,Manifest.permission.GET_ACCOUNTS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1
            );
            *//*ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INSTALL_LOCATION_PROVIDER},1);

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},1);*//*
//判断是否需要 向用户解释，为什么要申请该权限
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
    @Override
    public void beforeInitView() {



        /*Intent i_getvalue = getIntent();

        String action = i_getvalue.getAction();

        Log.e("KKKK","Intent.ACTION_VIEW:"+Intent.ACTION_VIEW+"   action:"+action);

        if(Intent.ACTION_VIEW.equals(action)){

            Uri uri = i_getvalue.getData();

            Log.e("KKKK","uri:"+uri);
            if(uri != null){

                String name = uri.getQueryParameter("name");

//                String age= uri.getQueryParameter("age");
                Log.e("KKKK","NAME:"+name);
            }

        }*/


    }

    @Override
    public void initView() {


        ll = (LinearLayout) findViewById(R.id.ll);
        tv_toMain = (TextView) findViewById(R.id.tv_toMain);
        imageUrl = new ArrayList<String>();
        imageViews = new ArrayList<ImageView>();
        dots = new ArrayList<View>();
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                //设置图片下载期间显示的图片
                .cacheOnDisc(true)
                .showStubImage(R.drawable.welcome02)
                // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.welcome02)
                // 设置图片uri为空时显示的图片
                .showImageOnFail(R.drawable.welcome02)
                // 设置图片加载或解码转换失败时显示的图片
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();

        if (!imageLoader.isInited()) {
            ImageUtils.initImageLoader(this);

        }
        viewPager = (ViewPager) findViewById(R.id.vp);
        //            get();
        JPushInterface.stopPush(getApplicationContext());
        Wethod.httpPost(WelComeActivity.this, 1112, Config.web.welcome_ad, null, this);
        Map<String, String> params = new HashMap<>();
        params.put("clientIp", "");
        Wethod.httpPost(WelComeActivity.this, 1314, Config.web.zbar_merchant, params, this);
    }

    @Override
    public void ClickLeft() {
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        tv_toMain.setOnClickListener(this);

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.tv_toMain:
                handler.removeCallbacks(run);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }


    private void getFail() {
        imageUrl.add("");
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.welcome02);
        //imageView.setBackgroundResource(R.drawable.welcome);
        imageViews.add(imageView);
        viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        //        handler.postDelayed(run, 2500);


    }

    private void getImageUrl() {
        // 初始化图片资源
        for (int i = 0; i < imageUrl.size(); i++) {
            ImageView imageView = new ImageView(this);
            //imageView.setImageResource(imageResId[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Log.e("GGG", "imageUrl.get(i)：" + imageUrl.get(i));
           /* imageLoader.displayImage(Config.web.picUrl + imageUrl.get(i), imageView,
                    options);*/

            ImageLoader.getInstance().displayImage(Config.web.picUrl + imageUrl.get(i), imageView, AppConfig.DEFAULT_IMG_OPTIONS_WELCOME);

            imageViews.add(imageView);
            dot = new ImageView(this);
            dot.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));

            if (i == 0)
                dot.setBackgroundResource(R.drawable.dian_click);
            else
                dot.setBackgroundResource(R.drawable.dian_normal);
            lp.leftMargin = DensityUtil.dip2px(this, 3);
            lp.rightMargin = DensityUtil.dip2px(this, 3);

            dots.add(dot);
            ll.addView(dot, lp);
        }
        viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        // 当Activity显示出来后，每3秒钟切换一次图片显示
        Log.e("liyunte", "开始进入runnable");
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentItem == imageUrl.size() - 1) {
                    Log.e("liyunte", "进入runnable");
                    viewPager.setCurrentItem(currentItem);
                    if (zbarMerchantData != null && getFromSharePreference(Config.userConfig.citycode) != null) {
                        startActivity(new Intent(WelComeActivity.this, MainActivity.class));
                        finish();
                    }
                    return;
                }
                currentItem++;
                viewPager.setCurrentItem(currentItem);
                viewPager.postDelayed(runnable, 2000);
            }
        };
        viewPager.postDelayed(runnable, 2000);
      /*  IpFindThread ipFindThread = new IpFindThread();
        ipFindThread.isRunning = true;
        ipFindThread.start();*/
        //        handler.postDelayed(run, 1000);
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1112) {
            try {
                WelcomeBean welcomeBean = getConfiguration().readValue(result.toString(), WelcomeBean.class);
                imageUrl = welcomeBean.getResultData();
                Log.e("liyunte", "1112------------");
                get();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (reqcode == 1314) {
            try {
                zbarMerchantData = getConfiguration().readValue(result.toString(), ZbarMerchantData.class);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class IpFindThread extends Thread {

        public boolean isRunning = true;

        @Override
        public void run() {
            if (isRunning) {
                try {
                    Thread.sleep(500);
                    Log.e("KJKJ", "IpFindThread");
                    if (zbarMerchantData != null && getFromSharePreference(Config.userConfig.citycode) != null) {
                        startActivity(new Intent(WelComeActivity.this, MainActivity.class));
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }


    /**
     * 当ViewPager中页面的状态发生改变时调用
     *
     * @author Administrator
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            currentItem = position;
            //tv_title.setText(titles[position]);
            dots.get(oldPosition).setBackgroundResource(R.drawable.dian_normal);
            dots.get(position).setBackgroundResource(R.drawable.dian_click);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    /**
     * 填充ViewPager页面的适配器
     *
     * @author Administrator
     */
    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageUrl.size();
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public Object instantiateItem(View container, int position) {
            View view = imageViews.get(position);
            ViewPager viewPager = (ViewPager) container;
            viewPager.addView(view);
            return imageViews.get(position);

        }
    }

    //    public void onClick(View arg0) {
    //        // TODO Auto-generated method stub
    //        switch(arg0.getId()){
    //
    //        }
    //
    //    }
    private void get() {
        //        imageUrl.add("http://g.hiphotos.baidu.com/image/w%3D310/sign=bb99d6add2c8a786be2a4c0f5708c9c7/d50735fae6cd7b8900d74cd40c2442a7d9330e29.jpg");
        //        imageUrl.add("http://g.hiphotos.baidu.com/image/w%3D310/sign=7cbcd7da78f40ad115e4c1e2672e1151/eaf81a4c510fd9f9a1edb58b262dd42a2934a45e.jpg");
        //        imageUrl.add("http://e.hiphotos.baidu.com/image/w%3D310/sign=392ce7f779899e51788e3c1572a6d990/8718367adab44aed22a58aeeb11c8701a08bfbd4.jpg");
        //        imageUrl.add("http://g.hiphotos.baidu.com/image/w%3D310/sign=bb99d6add2c8a786be2a4c0f5708c9c7/d50735fae6cd7b8900d74cd40c2442a7d9330e29.jpg");
        Log.e("liyunte", "get()");

        if (imageUrl.size() > 0) {
            Log.e("liyunte", "getInamgeUrl()");
            getImageUrl();
        } else {
            Log.e("liyunte", "getFail()");
            getFail();
        }

    }


    // 切换当前显示的图片
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (currentItem < imageViews.size() - 1) {
                Log.e("liyunte", "当前currentItem=" + currentItem);
                currentItem = currentItem + 1;
                viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
                handler.postDelayed(run, 1000);
            } else {
                Log.e("liyunte", "准备跳转到mainAcitvy");
                handler.removeCallbacks(run);
                if (getFromSharePreference(Config.userConfig.citycode) != null) {

                    if (zbarMerchantData != null) {
                        Intent intent = new Intent(WelComeActivity.this, NewSubscribeDishesActivity.class);
                        intent.putExtra("pkmuser", zbarMerchantData.getResultData());
                        intent.putExtra("distance", "*");
                        WelComeActivity.this.startActivity(intent);
                        WelComeActivity.this.finish();
                    } else {
                        WelComeActivity.this.startActivity(new Intent(WelComeActivity.this, MainActivity.class));
                        WelComeActivity.this.finish();
                    }

                }

            }
         /*   currentItem = currentItem + 1;
            viewPager.setCurrentItem(currentItem);
            handler.postDelayed(run,2500);
            if(currentItem == imageViews.size()-1){
                currentItem = -1;
//                startActivity(new Intent(WelComeActivity.this,LangMainActivity.class));
//                finish();
            }*/

        }

        ;
    };
    public Runnable run = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            handler.obtainMessage().sendToTarget(); // 通过Handler切换图片

        }
    };

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

    /*在启动当前APP的时候启动定位功能*/
    private void startLocation() {
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
        locationHandler.sendEmptyMessage(MSG_LOCATION_START);
    }


    /*地址切换*/
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            Message msg = locationHandler.obtainMessage();
            msg.obj = aMapLocation;
            msg.what = MSG_LOCATION_FINISH;
            locationHandler.sendMessage(msg);
        }
    }


    Handler locationHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOCATION_START:
                    //                    tvReult.setText("正在定位...");
                    break;
                //定位完成
                case MSG_LOCATION_FINISH:
                    AMapLocation loc = (AMapLocation) msg.obj;
                    //                    String result = getLocationStr(loc);
                    //                    tvReult.setText(result);
                    LocationDingweiBean ld = getLocationStr(loc);
                    /*SharedPreferenceUtils.saveToSharedPreference(WelComeActivity.this, "lng", ld.getmLng()+"");
                    SharedPreferenceUtils.saveToSharedPreference(WelComeActivity.this,"lat",ld.getmLat()+"");
                    SharedPreferenceUtils.saveToSharedPreference(WelComeActivity.this,"cityCode",ld.getCityCode());*/
                    SharedPreferenceUtils.saveToSharedPreference(WelComeActivity.this, Config.userConfig.lngu, ld.getmLng() + "");
                    SharedPreferenceUtils.saveToSharedPreference(WelComeActivity.this, Config.userConfig.latu, ld.getmLat() + "");
                    SharedPreferenceUtils.saveToSharedPreference(WelComeActivity.this, Config.userConfig.citycode, ld.getCityCode() + "");
                    SharedPreferenceUtils.saveToSharedPreference(WelComeActivity.this, Config.userConfig.adress, ld.getAdress() + "");
                    SharedPreferenceUtils.saveToSharedPreference(WelComeActivity.this, Config.userConfig.cityAdress, ld.getCityAdress() + "");
                    Log.e("woyaokkk", "adress:" + ld.getAdress());
                    Log.e("GGG", "ld.getCityCode():" + ld.getCityCode());
                    if (Wethod.isConnected(WelComeActivity.this)) {
                        //                        if(ld.getCityCode()!=null||!ld.getCityCode().equals("0")){
                        locationClient.stopLocation();
                        locationHandler.sendEmptyMessage(MSG_LOCATION_STOP);
                        //         }
                    }


                    break;
                case MSG_LOCATION_STOP:
                    //                    tvReult.setText("定位停止");
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 根据定位结果返回定位信息的字符串
     *
     * @return
     */
    private LocationDingweiBean getLocationStr(AMapLocation location) {

        LocationDingweiBean locationDingweiBean = new LocationDingweiBean();

        if (null == location) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        Log.i("aaa", "定位成功1");
        if (location.getErrorCode() == 0) {
            Log.i("aaa", "定位成功22222222222");
            sb.append("定位成功" + "\n");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
            sb.append("提供者    : " + location.getProvider() + "\n");
            locationDingweiBean.setmLng(location.getLongitude());
            locationDingweiBean.setmLat(location.getLatitude());

            if (location.getProvider().equalsIgnoreCase(
                    android.location.LocationManager.GPS_PROVIDER)) {
                // 以下信息只有提供者是GPS时才会有
                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : "
                        + location.getSatellites() + "\n");
            } else {
                // 提供者是GPS时是没有以下信息的
                sb.append("国    家    : " + location.getCountry() + "\n");
                sb.append("省            : " + location.getProvince() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("城市编码 : " + location.getCityCode() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("区域 码   : " + location.getAdCode() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");
                sb.append("兴趣点    : " + location.getPoiName() + "\n");

                Log.e("woyaokkk", "----:" + location.getAddress() + "=====:" + location.getPoiName());

                SharedPreferenceUtils.saveToSharedPreference(this, Config.userConfig.pname, location.getProvince());
                SharedPreferenceUtils.saveToSharedPreference(this, Config.userConfig.adName, location.getDistrict());
                SharedPreferenceUtils.saveToSharedPreference(this, Config.userConfig.cname, location.getCity());
                SharedPreferenceUtils.saveToSharedPreference(this, Config.userConfig.detail_adress, location.getAddress());

                if (location.getAddress().contains("省")) {
                    int sort = location.getAddress().indexOf("省");
                    String b = location.getAddress().substring(sort + 1, location.getAddress().length());
                    if (location.getAddress().contains("市")) {
                        int sortCity = b.indexOf("市");
                        String c = b.substring(sortCity + 1, b.length());
                        locationDingweiBean.setCityAdress(c);
                    } else {
                        locationDingweiBean.setCityAdress(b);
                    }

                } else {
                    locationDingweiBean.setCityAdress(location.getAddress());
                }

                locationDingweiBean.setAdress(location.getCity());
                locationDingweiBean.setCityCode(location.getCityCode());
            }
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }
        Log.e("liyunte", sb.toString());
        return locationDingweiBean;
    }


}
