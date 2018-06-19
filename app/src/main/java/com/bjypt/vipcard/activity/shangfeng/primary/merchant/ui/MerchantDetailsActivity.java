package com.bjypt.vipcard.activity.shangfeng.primary.merchant.ui;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.activity.shangfeng.base.BaseActivity;
import com.bjypt.vipcard.activity.shangfeng.common.LocateResultFields;
import com.bjypt.vipcard.activity.shangfeng.common.enums.UserInformationFields;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MerchantListBean;
import com.bjypt.vipcard.activity.shangfeng.dialog.LoadingDialog;
import com.bjypt.vipcard.activity.shangfeng.primary.merchant.contract.MerchantDetailsContract;
import com.bjypt.vipcard.activity.shangfeng.primary.merchant.contract.impl.MerchantDetailsPresenterImpl;
import com.bjypt.vipcard.activity.shangfeng.util.IsJudgeUtils;
import com.bjypt.vipcard.activity.shangfeng.util.SharedPreferencesUtils;
import com.bjypt.vipcard.activity.shangfeng.util.StringUtils;
import com.bjypt.vipcard.activity.shangfeng.util.ToastUtils;
import com.bjypt.vipcard.activity.shangfeng.widget.dialog.BookingOrderDialog;
import com.bjypt.vipcard.activity.shangfeng.widget.dialog.BottomDialog;
import com.bjypt.vipcard.widget.DropZoomScrollView;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商家详情页面
 */
public class MerchantDetailsActivity extends BaseActivity implements MerchantDetailsContract.View, AMap.OnMapClickListener {

    @BindView(R.id.ibtn_back)
    ImageButton ibtn_back;

    @BindView(R.id.scrollview)
    DropZoomScrollView scrollview;

    /**
     * title  商家名
     */
    @BindView(R.id.tv_merchant_title_name)
    TextView tv_merchant_title_name;
    /**
     * 商家大图
     */
    @BindView(R.id.iv_merchant_photo)
    ImageView iv_merchant_photo;
    /**
     * 商家位置
     */
    @BindView(R.id.tv_location_address)
    TextView tv_location_address;
    /**
     * 拨打商家电话
     */
    @BindView(R.id.ibtn_play_phone)
    ImageButton ibtn_play_phone;

    /**
     * 折扣
     */
    @BindView(R.id.tv_discount)
    TextView tv_discount;
    /**
     * 商家活动
     */
    @BindView(R.id.tv_merchant_activity)
    TextView tv_merchant_activity;
    /**
     * 商家信息
     */
    @BindView(R.id.tv_merchant_details)
    TextView tv_merchant_details;
    /**
     * 占位 View
     */
    @BindView(R.id.statusBarView)
    View statusBarView;

    @BindView(R.id.mapView)
    MapView mapView;
    /**
     * 预约下单
     */
    @BindView(R.id.btn_booking_order)
    Button btn_booking_order;

    private MerchantDetailsPresenterImpl presenter;
    private String pkmuser;
    private AMap aMap;
    private MarkerOptions markerOptions;
    private MerchantListBean merchantLBean;
    private String merchantPhone;

    private static final int REQUEST_CODE = 0; // 请求码

    /**
     * 要申请的权限
     */
    private String[] permissions = {Manifest.permission.CALL_PHONE};
    private String currentAddress;
    private String price;
    private LoadingDialog loadingDialog;
    private String pkregister = "";


    public static void callActivity(Context context, String pkmuser, String price) {
        Intent intent = new Intent(context, MerchantDetailsActivity.class);
        intent.putExtra("pkmuser", pkmuser);
        intent.putExtra("price", price);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchant_details;
    }

    @Override
    protected void initInjector() {
        ViewGroup.LayoutParams layoutParams = statusBarView.getLayoutParams();
        layoutParams.height = getStatusBarHeight();

        presenter = new MerchantDetailsPresenterImpl();
        presenter.attachView(this);
        superPresenter = presenter;
    }

    @Override
    protected void init() {

        mapView.onCreate(savedInstanceState);

        pkmuser = getIntent().getStringExtra("pkmuser");
        price = getIntent().getStringExtra("price");
        if (null != pkmuser) {
            if (isLogin()) {
                pkregister = String.valueOf(SharedPreferencesUtils.get(UserInformationFields.USER_KEY, ""));
            }
            presenter.getMerchantData(pkmuser, pkregister, price);
        }
    }

    @Override
    public void initMerchantDetails(MerchantListBean merchantListBean) {
        tv_merchant_title_name.setText(merchantListBean.getMuname());
        loadImageUrl(merchantListBean.getLogo(), iv_merchant_photo);
        tv_location_address.setText(merchantListBean.getAddress());
        if (StringUtils.isEmpty(merchantListBean.getFee())) {
            findViewById(R.id.cardview_discount).setVisibility(View.GONE);
        } else {
            tv_discount.setText(merchantListBean.getFee());
        }
        Integer order_status = merchantListBean.getOrder_status();

        if (0 == order_status) {
            btn_booking_order.setText("已预约下单");
            btn_booking_order.setClickable(false);
            btn_booking_order.setBackgroundColor(getResources().getColor(R.color.black_9));
        }
        tv_merchant_activity.setText(merchantListBean.getActivity_content());
        tv_merchant_details.setText(merchantListBean.getMerdesc());
        merchantPhone = merchantListBean.getPhone();
        initMap(new LatLng(merchantListBean.getLatitude(), merchantListBean.getLongitude()));
        merchantLBean = merchantListBean;
    }

    @Override
    public void updateView() {
        btn_booking_order.setText("已预约下单");
        btn_booking_order.setClickable(false);
        btn_booking_order.setBackgroundColor(getResources().getColor(R.color.black_9));
    }

    private void initMap(LatLng latLng) {
        if (aMap == null) {
            Logger.v("初始化地图");
            aMap = mapView.getMap();
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom
                    (latLng, 18));
            UiSettings mUiSettings = aMap.getUiSettings();
            mUiSettings.setScaleControlsEnabled(false);//显示地图的比例尺
            mUiSettings.setZoomControlsEnabled(false);//缩放按钮是否可见
            mUiSettings.setCompassEnabled(false);//指南针是否显示
            mUiSettings.setScrollGesturesEnabled(true);//是否可以手动滑动
            mUiSettings.setZoomGesturesEnabled(true);//是否可手动放缩
            mUiSettings.setMyLocationButtonEnabled(false);
            mUiSettings.setScaleControlsEnabled(false);
            mUiSettings.setLogoBottomMargin(-50);//隐藏logo
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom
                    (latLng, 18));
            aMap.setOnMapClickListener(this);
            drowMarker(latLng);
        }

    }

    /**
     * 绘制当前位置
     *
     * @param latLng
     */
    public void drowMarker(LatLng latLng) {
        Logger.v("绘制商家覆盖物");
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(getResources()
                        , R.mipmap.icon_merchant_location));
        markerOptions = new MarkerOptions().anchor(0.5f, 1)
                .position(latLng)
                .icon(bitmapDescriptor)
                .visible(true);
        aMap.addMarker(markerOptions);
    }


    /**
     * 拨打商家电话
     */
    @OnClick({R.id.ibtn_play_phone})
    public void playMerchantPhone() {
        initPermissions();
    }

    /**
     * 预约下单
     */
    @OnClick(R.id.btn_booking_order)
    public void bookingOrder() {
        if (isLogin()) {
            if (merchantLBean != null) {
                BookingOrderDialog bookingOrderDialog = new BookingOrderDialog(this, price, merchantLBean.getFee());
                bookingOrderDialog.show();
                bookingOrderDialog.setCancelable(false);
                bookingOrderDialog.toBookingOrder(new BookingOrderDialog.BookingOrderListener() {
                    @Override
                    public void bookingOrder() {
                        String pkregister = String.valueOf(SharedPreferencesUtils.get(UserInformationFields.USER_KEY, ""));
                        presenter.bookingOrder(pkregister, merchantLBean.getPkmuser(), price);
                    }
                });
            }
        } else {
            LoginActivity.callActivity(this);
        }


    }


    /**
     * 获取打电话权限
     */
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions,
                    REQUEST_CODE);
        } else {
            play(merchantPhone);
        }

    }


    @OnClick(R.id.ibtn_back)
    public void finishActivity() {
        finish();
    }


    @Override
    public void showProgress() {
        loadingDialog = new LoadingDialog(this, getResources().getString(R.string.wait_a_moment));
        loadingDialog.shows();
    }

    @Override
    public void hideProgress() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 加载图片
     *
     * @param url
     * @param imageView
     */
    private void loadImageUrl(String url, final ImageView imageView) {
        Glide.with(this)
                .load(url)
                .error(R.mipmap.merchant_details_error)
                .into(imageView);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        showDialog();
    }

    /**
     * 显示 第三方地图应用
     */
    private void showDialog() {

        currentAddress = String.valueOf(SharedPreferencesUtils.get(LocateResultFields.CURRENT_ADDRESS, ""));

        BottomDialog dialog = new BottomDialog(this, "使用高德地图", "使用百度地图");
        dialog.setClickListener(new BottomDialog.BtnBottomListener() {
            @Override
            public void onBtn1Click() {
                if (IsJudgeUtils.isAvilible(getApplicationContext(), "com.autonavi.minimap")) {
                    try {
                        Intent intent = Intent.getIntent("androidamap://navi?sourceApplication= &poiname=" + merchantLBean.getAddress() + "&lat="
                                + merchantLBean.getLatitude() + "&lon="
                                + merchantLBean.getLongitude() + "&dev=0");
                        startActivity(intent);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    //未安装
                    //market为路径，id为包名
                    //显示手机上所有的market商店
                    ToastUtils.showToast("您尚未安装高德地图");
                    try {
                        Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
                        Intent mIntent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(mIntent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onBtn2Click() {
                if (IsJudgeUtils.isAvilible(getApplicationContext(), "com.baidu.BaiduMap")) {
                    try {
//                        String uri = "intent://map/direction?origin=latlng:0,0|name:"+currentAddress+"&destination=" + merchantLBean.getAddress() + "&mode=drivingion=" + "城市" + "&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";

                        Intent intent = Intent.getIntent("intent://map/direction?" +
                                "destination=latlng:" + merchantLBean.getLatitude() + ","
                                + merchantLBean.getLongitude() + "|name:" + merchantLBean.getAddress() +       //终点
                                "&mode=driving&" +          //导航路线方式
                                "region=北京" +           //
                                "&src=慧医#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                        startActivity(intent); //启动调用
                    } catch (URISyntaxException e) {
                        Log.e("intent", e.getMessage());
                    }
                } else {
                    //未安装
                    //market为路径，id为包名
                    //显示手机上所有的market商店
                    ToastUtils.showToast("您尚未安装百度地图");
                    Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(mIntent);
                }

            }
        });
        dialog.show();
    }

    /**
     * 拨打商家电话
     *
     * @param number
     */
    private void play(String number) {
        Uri data = Uri.parse("tel:" + number); // 设置数据
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, data);
        startActivity(phoneIntent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 同意
                play(merchantPhone);
            } else {
                // 拒绝
                showAlertDialog("需要拨打电话权限");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }


}
