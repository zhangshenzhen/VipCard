package com.bjypt.vipcard.utils;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.bjypt.vipcard.activity.shangfeng.common.LocateResultFields;
import com.bjypt.vipcard.activity.shangfeng.util.SharedPreferencesUtils;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.LocationDingweiBean;
import com.sinia.orderlang.utils.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */


public class GaoDeMapLocation implements AMapLocationListener, PoiSearch.OnPoiSearchListener {
    private Context context;
    public final static int MSG_LOCATION_START = 0;//开始定位
    public final static int MSG_LOCATION_FINISH = 1;//定位完成
    public final static int MSG_LOCATION_STOP = 2;//停止定位
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private PoiSearch.Query query;//poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private PoiResult poiResult; // poi返回的结果
    private DingWeiSuccessListener dingWeiSuccessListener;
    private OnCityLocationChangeListener onCityLocationChangeListener;
    private BroadCastReceiverUtils utils;


    public GaoDeMapLocation(Context context) {
        this.context = context;
        initdata();
        utils = new BroadCastReceiverUtils();
    }


    private void initdata() {
        locationClient = new AMapLocationClient(context);
        locationOption = new AMapLocationClientOption();
        locationOption.setNeedAddress(true);
        locationOption.setMockEnable(true);
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

    //位置检索
    public void doSearchQuery(String cityName) {
        query = new PoiSearch.Query(cityName, "", "");
        query.setPageSize(2);//设置每页返回多少条条poiitem
        query.setPageNum(0);//设置查第一页
        poiSearch = new PoiSearch(context, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

    }

    /*地址切换*/
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            if(null != aMapLocation.getCity()) {
                SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.CURRENT_CITY, aMapLocation.getCity().replace("市", ""));
               LogUtil.debugPrint("定位回调:" + aMapLocation.getCity().replace("市", ""));
                SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.CURRENT_LATU, aMapLocation.getLatitude() + "");
                SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.CURRENT_LNGU, aMapLocation.getLongitude() + "");
                LogUtil.debugPrint("经度 :" + aMapLocation.getLongitude() + " ; 纬度 :" + aMapLocation.getLatitude());
            }

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
                    String oldCityCode = SharedPreferenceUtils.getFromSharedPreference(context, Config.userConfig.citycode);
                    AMapLocation loc = (AMapLocation) msg.obj;
                    LocationDingweiBean ld = getLocationStr(loc);
//                    if(ld != null) {
//                        SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.lngu, ld.getmLng() + "");
//                        SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.latu, ld.getmLat() + "");
//                        SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.citycode, ld.getCityCode() + "");
//                        SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.adress, ld.getAdress() + "");
//                        SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.cityAdress, ld.getCityAdress() + "");
//                        SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.cname, ld.getAdress());
//                    }


                    if ((ld == null || StringUtil.isEmpty(ld.getCityCode())) || StringUtil.isEmpty(oldCityCode)) {
                        //提示默认阜阳，并弹出提示是否选择城市
                        locationClient.stopLocation();
                        locationHandler.sendEmptyMessage(MSG_LOCATION_STOP);
                        if (dingWeiSuccessListener != null) {
                            dingWeiSuccessListener.onDingWeiSuccessListener();
                        }
                    } else {
                        Log.i("gps", "citycode= " + ld.getCityCode() +", " + oldCityCode);
                        SharedPreferencesUtils.put(LocateResultFields.CITY_CODE,ld.getCityCode());
                        SharedPreferencesUtils.put(LocateResultFields.CITY_CODE, ld.getCityCode());
                        SharedPreferencesUtils.put(LocateResultFields.LOCATION_LONGITUDE, String.valueOf(ld.getmLng()));
                        SharedPreferencesUtils.put(LocateResultFields.LOCATION_LATITUDE, String.valueOf(ld.getmLat()));
                        if (StringUtil.isNotEmpty(ld.getCityCode()) && StringUtil.isNotEmpty(oldCityCode) && !oldCityCode.equalsIgnoreCase(ld.getCityCode())) {
                            //城市都存在，但是和原来的citycode不一致
                            if (onCityLocationChangeListener != null) {
                                onCityLocationChangeListener.onChange(ld);
                            }
                        } else {
                            SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.lngu, ld.getmLng() + "");
                            SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.latu, ld.getmLat() + "");
                            SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.citycode, ld.getCityCode() + "");
                            SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.adress, ld.getAdress() + "");
                            SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.cityAdress, ld.getCityAdress() + "");
                        }

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

    public void onDestroy(){
        if(locationClient != null){
            locationClient.onDestroy();
        }
    }




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

    @Override
    public void onPoiSearched(PoiResult result, int resultCode) {

        Log.e("gps", "执行回调：" + resultCode + "    result:" + result);
        //地图搜索的回调
        if (resultCode == 1000) {
            if (result != null && result.getQuery() != null) {
                poiResult = result;
                //取得第一页的poiitem的数据，页数从0开始
                List<PoiItem> poiItems = poiResult.getPois();
                String cityCode = poiItems.get(0).getCityCode();
                String adName = poiItems.get(0).getAdName();//区的地址，为了给供货区提供区的地址
                String pname = poiItems.get(0).getProvinceName();//省的地址，为了给供货区提供省的地址
                String cname = poiItems.get(0).getCityName();//市的地址，为了给供货区提供市的地址
                LatLonPoint latLonPoint = poiItems.get(0).getLatLonPoint();
                double lat = latLonPoint.getLatitude();
                double lng = latLonPoint.getLongitude();
                SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.citycode, cityCode + "");
                SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.pname, pname);
                SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.adName, adName);
                SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.cname, cname);
                if (isSave) {
                    SharedPreferenceUtils.saveToSharedPreference(context, Config.userConfig.adress, cname);
                    SharedPreferenceUtils.saveToSharedPreference(context, "lng", lng + "");
                    SharedPreferenceUtils.saveToSharedPreference(context, "lngu", lng + "");
                    SharedPreferenceUtils.saveToSharedPreference(context, "lat", lat + "");
                    SharedPreferenceUtils.saveToSharedPreference(context, "latu", lat + "");
                }
//                SharedPreferenceUtils.saveToSharedPreference(context, "cityCode", cityCode);
                if (cityChangeListener != null) {
                    cityChangeListener.onCityChangeListner();
                }
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    public void setDingWeiSuccessListener(DingWeiSuccessListener dingWeiSuccessListener) {
        this.dingWeiSuccessListener = dingWeiSuccessListener;
    }

    private CityChangeListener cityChangeListener;

    public void setCityChangeListener(CityChangeListener cityChangeListener) {
        this.cityChangeListener = cityChangeListener;
    }

    public void setOnCityLocationChangeListener(OnCityLocationChangeListener onCityLocationChangeListener){
        this.onCityLocationChangeListener = onCityLocationChangeListener;
    }

    public interface DingWeiSuccessListener {
        public void onDingWeiSuccessListener();
    }

    public interface CityChangeListener {
        public void onCityChangeListner();
    }

    public interface  OnCityLocationChangeListener{

        public void onChange(LocationDingweiBean ld);
    }

    public void setIsSaveLng() {
        this.isSave = true;
    }

    private boolean isSave = false;


}
