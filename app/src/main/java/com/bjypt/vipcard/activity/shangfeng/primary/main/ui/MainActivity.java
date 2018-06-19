package com.bjypt.vipcard.activity.shangfeng.primary.main.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.activity.shangfeng.adapter.MenuRCVAdapter;
import com.bjypt.vipcard.activity.shangfeng.base.BaseActivity;
import com.bjypt.vipcard.activity.shangfeng.common.EventBusMessageEvent;
import com.bjypt.vipcard.activity.shangfeng.common.LocateResultFields;
import com.bjypt.vipcard.activity.shangfeng.common.enums.EventBusWhat;
import com.bjypt.vipcard.activity.shangfeng.common.enums.UserInformationFields;
import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CityBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.OrderBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.ResultDataBean;
import com.bjypt.vipcard.activity.shangfeng.dialog.LoadingDialog;
import com.bjypt.vipcard.activity.shangfeng.primary.capture.MyCaptureActivity;
import com.bjypt.vipcard.activity.shangfeng.primary.main.contract.MainContract;
import com.bjypt.vipcard.activity.shangfeng.primary.main.contract.impl.MainPresenterImpl;
import com.bjypt.vipcard.activity.shangfeng.primary.merchant.ui.MerchantActivity;
import com.bjypt.vipcard.activity.shangfeng.primary.merchant.ui.MerchantDetailsActivity;
import com.bjypt.vipcard.activity.shangfeng.primary.selectCity.ui.SelectCityActivity;
import com.bjypt.vipcard.activity.shangfeng.primary.systemmessages.ui.SystemMessagesActivity;
import com.bjypt.vipcard.activity.shangfeng.util.ApplicationUtils;
import com.bjypt.vipcard.activity.shangfeng.util.DisplayUtil;
import com.bjypt.vipcard.activity.shangfeng.util.LogUtils;
import com.bjypt.vipcard.activity.shangfeng.util.MapLocationUtil;
import com.bjypt.vipcard.activity.shangfeng.util.OkHttpUtil;
import com.bjypt.vipcard.activity.shangfeng.util.ShangfengUriHelper;
import com.bjypt.vipcard.activity.shangfeng.util.SharedPreferencesUtils;
import com.bjypt.vipcard.activity.shangfeng.util.StringUtils;
import com.bjypt.vipcard.activity.shangfeng.util.ToastUtils;
import com.bjypt.vipcard.activity.shangfeng.widget.dialog.MyOrderDialog;
import com.bjypt.vipcard.adapter.SpaceItemDecoration;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.common.Config;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;
/**
 * 主页面
 */
public class MainActivity extends BaseActivity implements MainContract.View, AMapLocationListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener, GeocodeSearch.OnGeocodeSearchListener {

    private static final int qcode_scan_code = 2;
    // 城市父布局
    @BindView(R.id.rl_place)
    RelativeLayout rl_place;
    // 城市
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.ibtn_select_city)
    ImageButton ibtn_select_city;
    /**
     * 天气
     */
    @BindView(R.id.ibtn_weather)
    ImageButton ibtn_weather;
    /**
     * 当前地址
     */
    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.rg_main_type)
    RadioGroup rg_main_type;
    /**
     * 加油
     */
    @BindView(R.id.rb_home)
    AppCompatRadioButton rb_home;
    /**
     * 商超
     */
    @BindView(R.id.rb_supermarket)
    AppCompatRadioButton rb_supermarket;
    /**
     * 住宿
     */
    @BindView(R.id.rb_accommodation)

    AppCompatRadioButton rb_accommodation;
    /**
     * 更多
     */
    @BindView(R.id.btn_more)
    Button btn_more;
    /**
     * 语音识别
     */
    @BindView(R.id.ibtn_voice)
    ImageButton ibtn_voice;

    @BindView(R.id.edit_price)
    EditText edit_price;

    @BindView(R.id.mapView)
    MapView mMapView;

    // 生活服务
    @BindView(R.id.rl_business_history)
    RelativeLayout rl_business_history;
    /**
     * 扫一扫
     */
    @BindView(R.id.rl_my_qcode_sacn)
    RelativeLayout rl_my_qcode_sacn;
    /**
     * 我的订单
     */
    @BindView(R.id.rl_my_order)
    RelativeLayout rl_my_order;

    /**
     * 菜单
     */
    @BindView(R.id.rcv_more_serve)
    RecyclerView rcv_more_serve;
    /**
     * 定位
     */
    @BindView(R.id.ibtn_location)
    ImageButton ibtn_location;
    /**
     * 搜索商家
     */
    @BindView(R.id.ib_postion)
    ImageButton ib_postion;

    private MainPresenterImpl mainPresenter;
    private AMap aMap;
    private RxPermissions rxPermissions;
    private MarkerOptions markerOptions;
    private ProgressDialog dialog;
    private String currentCity = "";
    private MenuRCVAdapter menuRCVAdapter;
    private MapLocationUtil mapLocationUtil;
    private String locationCity;
    private LoadingDialog loadingDialog;

    private GeocodeSearch geocodeSearch;

    public static void callActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 要申请的权限
     */
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shangfeng_main;
    }

    @Override
    protected void initInjector() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.app_theme_color));

        mainPresenter = new MainPresenterImpl();
        //让presenter保持view接口的引用
        mainPresenter.attachView(this);
        //让baseactivity自动执行oncreate 以及 在activitydestroy时能及时释放subscribe
        superPresenter = mainPresenter;

        EventBus.getDefault().register(this);

    }


    @Override
    protected void init() {
//        mainPresenter.getVersionMessage(1, getVersionName(this));

        mMapView.onCreate(savedInstanceState);

        initPermissions();

        edit_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                if (temp.startsWith("0")) {
                    s.delete(0, 1);
                }
                if (!temp.endsWith(".") && !StringUtils.isEmpty(temp)) {
                    if (Double.valueOf(temp) > 10000) {
                        s.delete(temp.length() - 1, temp.length());
                    }
                }
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });

        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        if (isLogin()) {
            String pkregister = String.valueOf(SharedPreferencesUtils.get(UserInformationFields.USER_KEY, ""));
            mainPresenter.getLastOrder(pkregister);
        }

        if (getIntent().getStringExtra("to_message") != null) {
            SystemMessagesActivity.callActivity(this);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 获取定位权限
     */
    private void initPermissions() {
        //点击拒绝
        rxPermissions = new RxPermissions(this);
        rxPermissions.request(permissions)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (!granted) {
                            //点击拒绝
                            showAlertDialog("需要定位权限");
                        } else {
                            initMap();
                        }
                    }
                });
    }

    /**
     * 展示Dialog
     */
    public void showAlertDialog(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage(message)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("立即开启", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        goToAppSetting();
                    }
                }).show();


    }

    // 跳转到当前应用的设置界面
    public void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 1);
    }


    @OnClick({R.id.ibtn_voice, R.id.btn_more, R.id.ibtn_location})
    public void voiceClick(View view) {
        switch (view.getId()) {
//            case R.id.ibtn_voice:  // 语音识别、更多
//            case R.id.btn_more:
//                VoiceOrMoreActivity.callActivity(this);
//                break;

            case R.id.ibtn_location:
                //设置了定位的监听,这里要实现LocationSource接口
                MyApplication.getInstance().getMapLocationUtil().satrtMapLocation(this);
                break;
        }
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            // 设置地图中心点 默认阜阳市
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Config.DEFAULT_LATITUDE, Config.DEFAULT_LONGITUDE), 19));
            UiSettings mUiSettings = aMap.getUiSettings();
            mUiSettings.setScaleControlsEnabled(false);//显示地图的比例尺
            mUiSettings.setZoomControlsEnabled(false);//缩放按钮是否可见
            mUiSettings.setCompassEnabled(false);//指南针是否显示
            mUiSettings.setScrollGesturesEnabled(true);//是否可以手动滑动
            mUiSettings.setZoomGesturesEnabled(true);//是否可手动放缩
            mUiSettings.setMyLocationButtonEnabled(false);
            mUiSettings.setScaleControlsEnabled(false);
            mUiSettings.setLogoBottomMargin(-50);//隐藏logo
            //设置了定位的监听,这里要实现LocationSource接口
            mapLocationUtil = MyApplication.getInstance().getMapLocationUtil();
            mapLocationUtil.satrtMapLocation(this);
            aMap.setOnMarkerClickListener(this);
            aMap.setOnMapClickListener(this);
            aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {

                    Point point = aMap.getProjection().toScreenLocation(aMap.getCameraPosition().target);

                    LatLng latLng = aMap.getProjection().fromScreenLocation(new Point(point.x, point.y + DisplayUtil.dip2px(150)));
                    RegeocodeQuery regeocodeQuery = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 150f, GeocodeSearch.AMAP);
                    SharedPreferencesUtils.put(LocateResultFields.LOCATION_LATITUDE, String.valueOf(latLng.latitude));
                    SharedPreferencesUtils.put(LocateResultFields.LOCATION_LONGITUDE, String.valueOf(latLng.longitude));

                    LogUtils.print("onCameraChangeFinish latlng:" + latLng.longitude + "," + latLng.latitude);
                    geocodeSearch.getFromLocationAsyn(regeocodeQuery);
                    //onRegeocodeSearched 回调
                }
            });
            aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
                @Override
                public void onTouch(MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ib_postion.setBackgroundResource(R.mipmap.icon_position2);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        ib_postion.setBackgroundResource(R.mipmap.icon_position);
                    }
                }
            });
        }


    }

//    @Override
//    public void setWeather(WeatherBean weather) {
//        Glide.with(this)
//                .load(weather.getImg())
//                .error(R.mipmap.meun_error)
//                .into(ibtn_weather);
//    }

    @Override
    public void initMenuData(List<BannerBean> bannerBeans) {
        if (menuRCVAdapter == null) {
            LinearLayoutManager lManager = new LinearLayoutManager(this);
            lManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rcv_more_serve.setLayoutManager(lManager);
            rcv_more_serve.addItemDecoration(new SpaceItemDecoration(35));
            menuRCVAdapter = new MenuRCVAdapter(this, bannerBeans);
            rcv_more_serve.setAdapter(menuRCVAdapter);
        } else {
            menuRCVAdapter.updateMenu(bannerBeans);
        }
    }

//    @Override
//    public void showUpdateDialog(String[] messages) {
//        updateApkDialog = new UpdateApkDialog(this, "升级", messages);
//        updateApkDialog.setClickListener(new UpdateApkDialog.BottonClickListener() {
//            @Override
//            public void onBtnListener() {
//                // 升级
//                updateApkDialog.dismiss();
//            }
//
//            @Override
//            public void close() {
//
//            }
//        });
//        updateApkDialog.setCancelable(false);
//        updateApkDialog.show();
//
//    }

    @Override
    public void showLastOrderDialog(OrderBean orderBean) {
        rl_my_order.setSelected(true);
        MyOrderDialog myOrderDialog = new MyOrderDialog(this, orderBean);
        myOrderDialog.show();
        myOrderDialog.setCancelable(false);
        myOrderDialog.setOrderStateListener(new MyOrderDialog.OrderStateListener() {
            @Override
            public void cancleOrder(OrderBean orderBean) {
                String pkregister = String.valueOf(SharedPreferencesUtils.get(UserInformationFields.USER_KEY, ""));
                mainPresenter.cancelOrder(pkregister, orderBean.getPkmuser(), orderBean.getId());
            }

            @Override
            public void toMerchant(OrderBean orderBean) {
                MerchantDetailsActivity.callActivity(MainActivity.this, orderBean.getPkmuser(), orderBean.getConsume_amount());
            }
        });
    }


    @Override
    public void showProgress() {
        loadingDialog = new LoadingDialog(this, getResources().getString(R.string.wait_a_moment));
        loadingDialog.shows();

    }

    @Override
    public void hideProgress() {
        if (loadingDialog != null) {
            loadingDialog.dismisss();
            loadingDialog = null;
        }
    }

    private static final int CITY_RESULT_CODE = 0;

    @OnClick({R.id.rl_place, R.id.rl_my_qcode_sacn, R.id.rl_business_history, R.id.rl_my_order, R.id.ib_postion})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_place:  // 城市列表
                startActivityForResult(new Intent(this, SelectCityActivity.class), CITY_RESULT_CODE);
                break;
            case R.id.rl_my_qcode_sacn:           // 扫一扫
                if (isLogin()) {
                    String[] permissions = {Manifest.permission.CAMERA};
                    rxPermissions = new RxPermissions(this);
                    rxPermissions.request(permissions)
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean granted) {
                                    if (!granted) {
                                        //点击拒绝
                                        showAlertDialog("扫码需要使用相机权限");
                                    } else {
                                        Intent qcode_intent = new Intent();
//                                        qcode_intent.putExtra("type", 1);
                                        qcode_intent.setClass(MainActivity.this, MyCaptureActivity.class);
                                        qcode_intent.putExtra("type", 1);
                                        startActivityForResult(qcode_intent, qcode_scan_code);
                                    }
                                }
                            });
                } else {
                    LoginActivity.callActivity(this);
                }
                break;
            case R.id.rl_business_history:          //交易历史
                if (isLogin()) {
                    ShangfengUriHelper shangfengUriHelper = new ShangfengUriHelper(this);
                    String url = Config.web.BusinessHistory;
                    String title = "交易历史";
                    String appUrl = shangfengUriHelper.buildWebPage(url, title);
                    shangfengUriHelper.startSearch(appUrl);
                } else {
                    LoginActivity.callActivity(this);
                }
                break;
            case R.id.rl_my_order:  // 我的订单
                if (isLogin()) {
                    ShangfengUriHelper shangfengUriHelper = new ShangfengUriHelper(this);
                    String url = Config.web.OrderRecordList;
                    String title = "我的订单";
                    String appUrl = shangfengUriHelper.buildWebPage(url, title);
                    shangfengUriHelper.startSearch(appUrl);
                } else {
                    LoginActivity.callActivity(this);
                }
                break;
            case R.id.ib_postion:
                searchMerchant();
                break;
        }
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            if (aMapLocation.getErrorCode() == 0) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                locationCity = aMapLocation.getCity().replace("市", "");
                double latitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                Logger.v("定位：" + latitude + " ; " + longitude);
                SharedPreferencesUtils.put(LocateResultFields.LOCATION_LATITUDE, String.valueOf(latitude));
                SharedPreferencesUtils.put(LocateResultFields.LOCATION_LONGITUDE, String.valueOf(longitude));

                drowMarker(new LatLng(latitude, longitude));//绘制当前当前经纬度

                currentCity = locationCity;
                tv_city.setText(currentCity);

                tv_address.setText(aMapLocation.getStreet() + aMapLocation.getStreetNum());

                String versionCode = ApplicationUtils.getVersionName(getApplicationContext());
                Logger.i("versionCode:" + versionCode);
                mainPresenter.getMenuData(String.valueOf(0), versionCode, aMapLocation.getCityCode());
//                mainPresenter.getWeather(currentCity);

                LatLng latLng = new LatLng(latitude, longitude);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, aMap.getCameraPosition().zoom));
                CameraUpdate cameraUpdate = CameraUpdateFactory.scrollBy(0, DisplayUtil.dip2px(-46));
                aMap.moveCamera(cameraUpdate);
                saveCity(new CityBean(locationCity
                        , null
                        , aMapLocation.getCityCode()
                        , aMapLocation.getStreet() + aMapLocation.getStreetNum()
                        , latitude
                        , longitude));
                if (mapLocationUtil != null) {
                    mapLocationUtil.stopMapLocation(this);
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Logger.e("定位失败");
                Logger.e("ErrCode = " + aMapLocation.getErrorCode());
                Logger.e("ErrorInfo = " + aMapLocation.getErrorInfo());
                // 定位失败 当前位置默认显示阜阳
                initCity(new CityBean(Config.DEFAULT_CITY, null, Config.DEFAULT_CITY_CODE
                        , getResources().getString(R.string.Location_hint)
                        , Config.DEFAULT_LATITUDE
                        , Config.DEFAULT_LONGITUDE));
                if (mapLocationUtil != null) {
                    mapLocationUtil.stopMapLocation(this);
                }
            }
        }
    }

    /**
     * 设置定位数据
     *
     * @param cityBean
     */
    public void initCity(CityBean cityBean) {
        currentCity = cityBean.getCityname();
        tv_city.setText(currentCity);


        tv_address.setText(cityBean.getAddress());

        saveCity(cityBean);
        String versionCode = ApplicationUtils.getVersionName(getApplicationContext());
        Logger.i("versionCode:" + versionCode);
        mainPresenter.getMenuData(String.valueOf(0), versionCode, cityBean.getCitycode());
//        mainPresenter.getWeather(cityBean.getCityname());

        LatLng latLng = new LatLng(cityBean.getLatitude(), cityBean.getLongitude());
//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, aMap.getCameraPosition().zoom));

        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, aMap.getCameraPosition().zoom));
        CameraUpdate cameraUpdate = CameraUpdateFactory.scrollBy(0, DisplayUtil.dip2px(-135));
        aMap.moveCamera(cameraUpdate);

    }

    /**
     * 绘制当前位置
     *
     * @param latLng
     */
    public void drowMarker(LatLng latLng) {
        if (markerOptions != null) {
            aMap.clear();
            markerOptions = null;
        }

        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, aMap.getCameraPosition().zoom));

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(getResources()
                        , R.mipmap.my_location));
        markerOptions = new MarkerOptions().anchor(0.5f, 1)
                .position(latLng)
                .icon(bitmapDescriptor)
                .visible(true);
        aMap.addMarker(markerOptions);


        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, aMap.getCameraPosition().zoom));
        CameraUpdate cameraUpdate = CameraUpdateFactory.scrollBy(0, DisplayUtil.dip2px(-135));
        aMap.moveCamera(cameraUpdate);
    }

    /**
     * 存储 城市信息
     *
     * @param cityBean
     */
    public void saveCity(CityBean cityBean) {
        SharedPreferencesUtils.put(LocateResultFields.CITY, cityBean.getCityname());
        SharedPreferencesUtils.put(LocateResultFields.CITY_CODE, cityBean.getCitycode());
        SharedPreferencesUtils.put(LocateResultFields.LONGITUDE, String.valueOf(cityBean.getLongitude()));
        SharedPreferencesUtils.put(LocateResultFields.LATITUDE, String.valueOf(cityBean.getLatitude()));
        SharedPreferencesUtils.put(LocateResultFields.CURRENT_ADDRESS, cityBean.getAddress());
    }

    /**
     * 当前位置（marker覆盖物）点击事件的回调
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        return searchMerchant();
    }

    private boolean searchMerchant() {
        String price = edit_price.getText().toString();
        if (price.endsWith(".")) {
            ToastUtils.showToast(getResources().getString(R.string.main_hint));
            return true;
        }
        if (StringUtils.isEmpty(price)) {
            ToastUtils.showToast(getResources().getString(R.string.main_hint));
            return true;
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mMapView.getWindowToken(), 0);

        String tag = ((AppCompatRadioButton) findViewById(rg_main_type.getCheckedRadioButtonId())).getText().toString();
        // 跳转商家 分类列表
        MerchantActivity.callActivity(this, tag, price);
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mMapView.getWindowToken(), 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CITY_RESULT_CODE:
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    CityBean cityBean = bundle.getParcelable("picked_city");
                    if (cityBean != null) {
                        Logger.d(cityBean);
                        Logger.d("currentCity = " + currentCity + " ; " + cityBean.getCityname());
                        if (!currentCity.equals(cityBean.getCityname())) {
                            initCity(cityBean);
                        }
                    }
                }
                break;
            case qcode_scan_code:
                if (resultCode == RESULT_OK) {
                    ShangfengUriHelper helper = new ShangfengUriHelper(this);
                    helper.startSearch(data.getExtras().getString("result"), true);
                    break;
                }
                break;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismisss();
            loadingDialog = null;
        }
        EventBus.getDefault().unregister(this);

    }


    /************************* 经纬度查询 *****************************************/
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int resultId) {
        if (resultId == 1000) {
            StringBuffer sb = new StringBuffer();
            sb.append(regeocodeResult.getRegeocodeAddress().getFormatAddress());
            tv_address.setText(sb.substring(sb.indexOf("市") + 1, sb.length()));
            tv_address.setSelected(true);
            StringBuffer ss = new StringBuffer();
            ss.append(regeocodeResult.getRegeocodeAddress().getDistrict() + " ");
            ss.append(regeocodeResult.getRegeocodeAddress().getNeighborhood() + " ");
            ss.append(regeocodeResult.getRegeocodeAddress().getTownship());
            ss.append(regeocodeResult.getRegeocodeAddress().getBuilding());
            LogUtils.print("address =" + ss);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessageEvent event) {
        if (event.getWhat() == EventBusWhat.NewOrder) {
//            ToastUtils.showToast("收到订单更新请求， 正在请求接口。。。");
            if (isLogin()) {
                String pkregister = String.valueOf(SharedPreferencesUtils.get(UserInformationFields.USER_KEY, ""));
                Map<String, String> map = new LinkedHashMap<>();
                map.put("pkregister", pkregister);
                OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
                    @Override
                    public void success(int resultCode, ResultDataBean data) {
                        Gson gson = new Gson();
                        OrderBean orderBean = gson.fromJson(gson.toJson(data.getResultData()), OrderBean.class);
                        if (orderBean != null) {
                            rl_my_order.setSelected(true);
                        } else {
                            rl_my_order.setSelected(false);
                        }
                    }

                    @Override
                    public void onError(int resultCode, String errorMsg) {

                    }
                }, Config.web.LAST_ORDER_URL, map);
            } else {
                rl_my_order.setSelected(false);
            }
        }
    }

}
