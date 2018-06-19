package com.bjypt.vipcard.activity.shangfeng.primary.merchant.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.adapter.MerchantListAdapter;
import com.bjypt.vipcard.activity.shangfeng.base.BaseContract;
import com.bjypt.vipcard.activity.shangfeng.base.BaseFragment;
import com.bjypt.vipcard.activity.shangfeng.common.LocateResultFields;
import com.bjypt.vipcard.activity.shangfeng.common.PageNumberSetting;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MerchantListBean;
import com.bjypt.vipcard.activity.shangfeng.primary.merchant.contract.MerchantListContract;
import com.bjypt.vipcard.activity.shangfeng.primary.merchant.contract.impl.MerchantListPresenterImpl;
import com.bjypt.vipcard.activity.shangfeng.util.SharedPreferencesUtils;
import com.bjypt.vipcard.activity.shangfeng.util.StringUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 商家列表
 */
public class MerchantListFragment extends BaseFragment implements MerchantListContract.View, SwipeRefreshLayout.OnRefreshListener, MerchantListAdapter.PlayMerchantPhoneListener {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.rcv_list)
    RecyclerView rcv_list;

    @BindView(R.id.ll_error)
    LinearLayout ll_error;

    private MerchantListAdapter adapter;
    private MerchantListPresenterImpl presenter;

    private static final String CITY_CODE = "citycode";
    private static final String TAG = "tag";
    private static final String PRICE = "price";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String ORDER_YPE = "orderType";

    private String citycode;
    private String tag;
    private String price;
    public String latitude;
    public String longitue;
    private int orderType;
    private ArrayList<MerchantListBean> mListBeans;
    private static final int REQUEST_CODE = 0; // 请求码
    /**
     * 要申请的权限
     */
    private String[] permissions = {Manifest.permission.CALL_PHONE};
    private String merchantPhone;

    /**
     * 构建商家列表Fragment
     *
     * @param citycode  城市编码
     * @param tag       商家类型标签
     * @param price     预计消费金额
     * @param orderType 排序方式  0：离我最近 1：优惠最多 2：智能排序
     * @return
     */
    public static MerchantListFragment newInstance(String citycode, String tag, String price, String longitude, String latitude, int orderType) {
        MerchantListFragment fragment = new MerchantListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CITY_CODE, citycode);
        bundle.putString(TAG, tag);
        bundle.putString(PRICE, price);
        bundle.putString(LONGITUDE, longitude);
        bundle.putString(LATITUDE, latitude);
        bundle.putInt(ORDER_YPE, orderType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return null;
    }

    @Override
    public void initInjector() {
        presenter = new MerchantListPresenterImpl();
        presenter.attachView(this);
        superPresenter = presenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_merchant_list;
    }

    @Override
    public void init(View view) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            citycode = bundle.getString(CITY_CODE);
            tag = bundle.getString(TAG);
            price = bundle.getString(PRICE);
            longitue = bundle.getString(LONGITUDE);
            latitude = bundle.getString(LATITUDE);
            orderType = bundle.getInt(ORDER_YPE);
        } else {
            Logger.e("无参数");
        }

        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeResources(R.color.app_theme_color);
        swipeRefresh.setDistanceToTriggerSync(500);
        swipeRefresh.setProgressBackgroundColor(R.color.white);
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        Logger.v("纬度：" + SharedPreferencesUtils.get(LocateResultFields.LOCATION_LATITUDE, ""));
        searchMerchantList();


    }

    private void searchMerchantList() {
        String locationLatitude = String.valueOf(SharedPreferencesUtils.get(LocateResultFields.LOCATION_LATITUDE, ""));
        String locationLongitude = String.valueOf(SharedPreferencesUtils.get(LocateResultFields.LOCATION_LONGITUDE, ""));
        if (!StringUtils.isEmpty(locationLatitude) && !StringUtils.isEmpty(locationLongitude)) {
            presenter.getMerchantsData(citycode, tag, price, locationLongitude, locationLatitude, 1, PageNumberSetting.PAGE_NUMBER, orderType);
        } else {
            presenter.getMerchantsData(citycode, tag, price, longitue, latitude, 1, PageNumberSetting.PAGE_NUMBER, orderType);
        }
    }


    @Override
    public void onRefresh() {
        searchMerchantList();
    }

    @Override
    public void showProgress() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefresh.setRefreshing(false);
    }

    private boolean state = true;


    public void searchMerchant(String merchantName) {
        if (mListBeans != null) {
            if (merchantName == null) {
                initMerchantList(0, mListBeans);
            } else {
                Logger.v("查询" + merchantName);
                initMerchantList(0, queryMerchantList(mListBeans, merchantName));
            }
        } else {
            initMerchantList(1, null);
        }
    }

    public ArrayList<MerchantListBean> queryMerchantList(List<MerchantListBean> merchantListBeans, String merchantName) {
        ArrayList<MerchantListBean> listBeans = new ArrayList<>();
        for (MerchantListBean merchantListBean : merchantListBeans) {
            if (merchantListBean.getMuname().contains(merchantName)) {
                listBeans.add(merchantListBean);
            }
        }
        return listBeans;
    }


    @Override
    public void initMerchantList(int statusCode, ArrayList<MerchantListBean> merchantListBeans) {
        if (state) {
            Intent intent = new Intent();
            intent.setAction("com.shangfeng.yiyou.MERCHANT_COUNT");
            if (merchantListBeans != null && merchantListBeans.size() > 0) {

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("MerchantData", merchantListBeans);
                intent.putExtras(bundle);
            }
            getActivity().sendBroadcast(intent);//发送普通广播
            Logger.v("发送广播");
            state = !state;
        }

        switch (statusCode) {
            case 0:
                rcv_list.setVisibility(View.VISIBLE);
                ll_error.setVisibility(View.GONE);
                if (mListBeans == null) {
                    mListBeans = merchantListBeans;
                }
                if (null == adapter) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    rcv_list.setLayoutManager(layoutManager);
                    adapter = new MerchantListAdapter(getActivity(), merchantListBeans,price);
                    rcv_list.setAdapter(adapter);
                    adapter.playMerchantPhone(this);
                } else {
                    adapter.updateMerchantList(merchantListBeans);
                }

                break;

            case -1:
                ll_error.setVisibility(View.VISIBLE);
                rcv_list.setVisibility(View.GONE);
                break;

            case 1:
                ll_error.setVisibility(View.VISIBLE);
                rcv_list.setVisibility(View.GONE);

                break;
        }
    }

    @Override
    public void callPhone(String phoneNumber) {
        initPermissions(phoneNumber);
    }

    /**
     * 获取拨打电话权限
     */
    private void initPermissions(final String phoneNumber) {
        merchantPhone = phoneNumber;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions,
                    REQUEST_CODE);
        } else {
            play(phoneNumber);
        }
    }

    /**
     * 拨打商家电话
     *
     * @param number
     */
    private void play(String number) {
//        ToastUtils.showToastBottom(String.valueOf(number));
        Uri data = Uri.parse("tel:" + number); // 设置数据
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, data);
        getActivity().startActivity(phoneIntent);

    }

    /**
     * 展示Dialog
     */
    public void showAlertDialog(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示")
                .setMessage(message)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

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
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 1);
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
}
