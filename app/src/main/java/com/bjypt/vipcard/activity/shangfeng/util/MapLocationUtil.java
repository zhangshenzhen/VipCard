package com.bjypt.vipcard.activity.shangfeng.util;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * 定位工具类
 */
public class MapLocationUtil {


    private static AMapLocationClient mLocationClient;
    private static AMapLocationClientOption mLocationOption;

    private MapLocationUtil(Context applicationContext) {
        // 初始化定位
        mLocationClient = new AMapLocationClient(applicationContext);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption
                .AMapLocationMode.Hight_Accuracy);
        // 退出时杀死进程
        mLocationOption.setKillProcess(true);
        //设置是否单次定位,默认为false
        mLocationOption.setOnceLocation(true);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    public static MapLocationUtil getInstance(Context applicationContext) {
        return new MapLocationUtil(applicationContext);
    }

    /**
     * 开始定位
     */
    public void satrtMapLocation(AMapLocationListener locationListener) {
        //设置定位回调监听
        mLocationClient.setLocationListener(locationListener);
        //启动定位
        mLocationClient.startLocation();
    }


    public void stopMapLocation(AMapLocationListener listener){
        if(mLocationClient != null){
//            mLocationClient.stopLocation();
            mLocationClient.unRegisterLocationListener(listener);
        }
    }

}
