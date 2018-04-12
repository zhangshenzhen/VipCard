package com.bjypt.vipcard.utils;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.LocationDingweiBean;
import com.sinia.orderlang.utils.SharedPreferencesUtils;
import com.sinia.orderlang.utils.StringUtil;

import java.util.List;

/**
 * Created by wanglei on 2017/11/2.
 */

public class GaoDeTimerLocation implements AMapLocationListener {
    private Context context;
    public final static int MSG_LOCATION_START = 0;//开始定位
    public final static int MSG_LOCATION_FINISH = 1;//定位完成
    public final static int MSG_LOCATION_STOP = 2;//停止定位
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private PoiSearch.Query query;//poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private PoiResult poiResult; // poi返回的结果
    private GaoDeMapLocation.DingWeiSuccessListener dingWeiSuccessListener;
    private BroadCastReceiverUtils utils;

    public GaoDeTimerLocation(Context context) {
        this.context = context;
        initdata();
        utils = new BroadCastReceiverUtils();
    }


    private void initdata() {
        locationClient = new AMapLocationClient(context);
        locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        locationClient.setLocationListener(this);
    }

    /*在启动当前APP的时候启动定位功能*/
    public void startLocation() {
        Log.e("dingwei", "#########" + Build.VERSION.SDK_INT);
        locationClient.setLocationOption(locationOption);
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
                    break;
                //定位完成
                case MSG_LOCATION_FINISH:
                    AMapLocation loc = (AMapLocation) msg.obj;
                    LocationDingweiBean ld = getLocationStr(loc);
                    String oldCityCode = SharedPreferenceUtils.getFromSharedPreference(context, Config.userConfig.cname);
                    Log.i("gps", "oldCityCode = " + oldCityCode + " ld=" + ld);
                    if (ld == null || StringUtil.isEmpty(ld.getCityCode())) {
                        //提示默认阜阳，并弹出提示是否选择城市
                        locationClient.stopLocation();
                        locationHandler.sendEmptyMessage(MSG_LOCATION_STOP);
                        if (dingWeiSuccessListener != null) {
                            dingWeiSuccessListener.onDingWeiSuccessListener();
                        }
                    } else {
                        Log.i("gps", ld.getCityAdress()+"");
                        SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.lngu, ld.getmLng() + "");
                        SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.latu, ld.getmLat() + "");
                        if (dingWeiSuccessListener != null) {
                            dingWeiSuccessListener.onDingWeiSuccessListener();
                        }
                        if (Wethod.isConnected(context)) {
                            locationClient.stopLocation();
                            locationHandler.sendEmptyMessage(MSG_LOCATION_STOP);
                        }
                    }
                    break;
                case MSG_LOCATION_STOP:
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
        if (location.getErrorCode() == 0) {
            locationDingweiBean.setmLng(location.getLongitude());
            locationDingweiBean.setmLat(location.getLatitude());
            if (location.getProvider().equalsIgnoreCase(
                    android.location.LocationManager.GPS_PROVIDER)) {
            } else {
                SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.pname, location.getProvince());
                SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.adName, location.getDistrict());
                SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.cname, location.getCity());
                SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.detail_adress, location.getAddress());
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

        }
        return locationDingweiBean;
    }


    public void setDingWeiSuccessListener(GaoDeMapLocation.DingWeiSuccessListener dingWeiSuccessListener) {
        this.dingWeiSuccessListener = dingWeiSuccessListener;
    }

    private GaoDeMapLocation.CityChangeListener cityChangeListener;

    public void setCityChangeListener(GaoDeMapLocation.CityChangeListener cityChangeListener) {
        this.cityChangeListener = cityChangeListener;
    }

    public interface DingWeiSuccessListener {
        public void onDingWeiSuccessListener();
    }

    public interface CityChangeListener {
        public void onCityChangeListner();
    }

    public void setIsSaveLng() {
        this.isSave = true;
    }

    private boolean isSave = false;


}
