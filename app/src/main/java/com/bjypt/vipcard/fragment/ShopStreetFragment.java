package com.bjypt.vipcard.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.SearchMerchantActivity;
import com.bjypt.vipcard.adapter.interconnect.ReaycleBannerAdapter;
import com.bjypt.vipcard.base.BaseFragment;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.AppCategroyResultDataBean;
import com.bjypt.vipcard.model.HomeTest;
import com.bjypt.vipcard.model.HomeTypeBean;
import com.bjypt.vipcard.model.NewMerchantListBean;
import com.bjypt.vipcard.receiver.Logger;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.GaoDeMapLocation;
import com.bjypt.vipcard.utils.GaoDeTimerLocation;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.NewsViewpager;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.view.categoryview.AppCategoryHomeBannerView;
import com.bjypt.vipcard.view.layoutbanner.BannerLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class ShopStreetFragment extends BaseFragment implements VolleyCallBack<String> {

    private View view;
    private EditText search_merchant;
    private AppCategoryHomeBannerView appCategoryHomeMerchantBannerView;
    private SlidingTabLayout stl_store;
    private NewsViewpager vp_store;
    private MyPagerAdapter myPagerAdapter;
    private List<HomeTypeBean> tabList = new ArrayList<>();
    private HomeTest homeInfoBean;
    private List<ShopStreetViewpagerFragment> mFragments = new ArrayList<>();
    private List<String> listName = new ArrayList<>();
    private LinearLayout mChangeCity;
    private TextView home_now_city;

    private String lng;
    private String lat;
    private String cityCode;
    private BroadCastReceiverUtils utils;
    private MyBroadCastReceiver recei;
    private GaoDeTimerLocation gaoDeTimerLocation;
    private Timer timer;
    private TimerTask timerTask;
    private BannerLayout recyclerBanner;//新增的banner样式
    private int request_code_data = 12;
    private List<String> list_banner = new ArrayList<>();
    public AppCategroyResultDataBean appCategroyResultDataBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop_street, container, false);
        return view;
    }

    @Override
    public void beforeInitView() {
        View statusBar = view.findViewById(R.id.statusBarView);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();

        recei = new MyBroadCastReceiver();
        utils = new BroadCastReceiverUtils();
        utils.registerBroadCastReceiver(getActivity(), "change_city", recei);
        utils.registerBroadCastReceiver(getActivity(), "action_news", recei);
        utils.registerBroadCastReceiver(getActivity(), "updata_home_utils", recei);
        gaoDeTimerLocation = new GaoDeTimerLocation(this.getContext());
        gaoDeTimerLocation.setDingWeiSuccessListener(new GaoDeMapLocation.DingWeiSuccessListener() {
            @Override
            public void onDingWeiSuccessListener() {
                setCityName(SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.cname));
            }
        });
        timerTask = new TimerTask() {
            @Override
            public void run() {
                gaoDeTimerLocation.startLocation();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 5 * 1000 * 60, 5 * 1000 * 60);

        Wethod.httpPost(getActivity(), 1, Config.web.home_service, null, this, View.VISIBLE);

    }

    @Override
    public void initView() {
        search_merchant = (EditText) view.findViewById(R.id.search_merchant);
        appCategoryHomeMerchantBannerView = (AppCategoryHomeBannerView) view.findViewById(R.id.appCategoryHomeMerchantBannerView);
        stl_store = (SlidingTabLayout) view.findViewById(R.id.stl_store);
        vp_store = (NewsViewpager) view.findViewById(R.id.vp_store);
        home_now_city = (TextView) view.findViewById(R.id.home_now_city);
        mChangeCity = (LinearLayout) view.findViewById(R.id.change_city);
        recyclerBanner = view.findViewById(R.id.recyclerBanner);
    }

    @Override
    public void afterInitView() {
        //开启banner页
        int bannerType = 9;
        // appCategoryHomeMerchantBannerView.reload();
        Map<String, String> params = new HashMap<>();
        params.put("city_code", Config.web.cityCode);//SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.userConfig.citycode, "1558")
        params.put("app_type", bannerType + "");
        startLoading(Config.web.application_ad, params);
        //loadRecyclerBanner();//
        setCityName(SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.cname));

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            setCityName(SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.cname));

        } else {
            if (SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.adress) == null || SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.adress).equals("")) {
                GaoDeMapLocation gaoDeMapLocation = new GaoDeMapLocation(getActivity());
                gaoDeMapLocation.startLocation();
                gaoDeMapLocation.setDingWeiSuccessListener(new GaoDeMapLocation.DingWeiSuccessListener() {
                    @Override
                    public void onDingWeiSuccessListener() {

                        setCityName(SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.cname));

                    }
                });
            }
        }
    }

    public void startLoading(String method, Map<String, String> params) {
        Wethod.httpPost(getActivity(), request_code_data, method, params, this, View.GONE);
    }


    @Override
    public void bindListener() {
        mChangeCity.setOnClickListener(this);
        search_merchant.setOnClickListener(this);
        vp_store.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                vp_store.resetHeight(position);
            }

            @Override
            public void onPageSelected(int position) {
                vp_store.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.search_merchant:
                Intent searchMerchantIntent = new Intent(getActivity(), SearchMerchantActivity.class);
                startActivity(searchMerchantIntent);
                break;
            case R.id.change_city:
//                startActivity(new Intent(getActivity(), CityActivity.class));
                break;
        }
    }

    private void initSlidingTabData() {
        if (isAdded()) {
            myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
            vp_store.setAdapter(myPagerAdapter);
            stl_store.setViewPager(vp_store);
            stl_store.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    mFragments.get(position).onRefresh();
                    vp_store.resetHeight(position);
                }

                @Override
                public void onTabReselect(int position) {
                    mFragments.get(position).onRefresh();
                    vp_store.resetHeight(position);
                }
            });
            vp_store.setOffscreenPageLimit(mFragments.size());
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 1) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                homeInfoBean = objectMapper.readValue(result.toString(), HomeTest.class);
                com.orhanobut.logger.Logger.d(homeInfoBean);
                if (tabList.size() > 0) {
                    tabList.clear();
                }
                tabList.addAll(homeInfoBean.getResultData().getMerCategory());

                if (listName.size() > 0) {
                    listName.clear();
                }

                //SlidingLayout
                for (int i = 0; i < tabList.size(); i++) {
                    listName.add(tabList.get(i).getMtname());
                    mFragments.add(ShopStreetViewpagerFragment.getInstance(tabList.get(i).getMtlevel(), tabList.get(i).getPkmertype(), vp_store, i));

                }

                initSlidingTabData();

            } catch (IOException e) {

                e.printStackTrace();
            }
        } else if (reqcode == request_code_data) {
            try {
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                if (list_banner.size() > 0) {
                    list_banner.clear();
                }
                appCategroyResultDataBean = objectMapper.readValue(result.toString(), AppCategroyResultDataBean.class);
                if (appCategroyResultDataBean != null && appCategroyResultDataBean.getResultData().size() > 0) {

                    for (int i = 0; i < appCategroyResultDataBean.getResultData().size(); i++) {
                        list_banner.add(appCategroyResultDataBean.getResultData().get(i).getApp_icon());
                    }
                    //轮播图个数要大于等于3个
                    if (appCategroyResultDataBean.getResultData().size() == 1) {
                        list_banner.add(appCategroyResultDataBean.getResultData().get(0).getApp_icon());
                        list_banner.add(appCategroyResultDataBean.getResultData().get(0).getApp_icon());
                    } else if (appCategroyResultDataBean.getResultData().size() == 2) {
                        list_banner.add(appCategroyResultDataBean.getResultData().get(0).getApp_icon());
                        list_banner.add(appCategroyResultDataBean.getResultData().get(1).getApp_icon());
                    }
                }

                loadRecyclerBanner();//加载
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 加载banner*/
    private void loadRecyclerBanner() {

       /* if (list_banner.size()==0){ //添加默认的地址
            list_banner.add("http://img3.imgtn.bdimg.com/it/u=2293177440,3125900197&fm=27&gp=0.jpg");
            list_banner.add("http://img3.imgtn.bdimg.com/it/u=3967183915,4078698000&fm=27&gp=0.jpg");
            list_banner.add("http://img0.imgtn.bdimg.com/it/u=3184221534,2238244948&fm=27&gp=0.jpg");
            list_banner.add("http://img4.imgtn.bdimg.com/it/u=1794621527,1964098559&fm=27&gp=0.jpg");
        }*/
        ReaycleBannerAdapter webBannerAdapter = new ReaycleBannerAdapter(getActivity(), list_banner);
        recyclerBanner.setShowIndicator(false);
        recyclerBanner.setCenterScale(1.2f);
        recyclerBanner.setMoveSpeed(0.8f);
        recyclerBanner.setAdapter(webBannerAdapter);

    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listName.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    private void setCityName(String city) {
        if (StringUtil.isEmpty(city)) {
            return;
        }
        if (city.length() >= 5) {
            city = city.substring(0, 5);
        }
        Log.e("liyunte", city);
        if (city.contains("市")) {
            city = city.substring(0, city.indexOf("市"));
        }
//        home_now_city.setText(city);
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("rrrr", intent.getAction());
//            if ("change_city".equals(intent.getAction())) {
//                String city = intent.getStringExtra("updata");
//                setCityName(city);
//                GaoDeMapLocation gaoDeMapLocation = new GaoDeMapLocation(getActivity());
//                gaoDeMapLocation.doSearchQuery(city);
//                gaoDeMapLocation.setCityChangeListener(new GaoDeMapLocation.CityChangeListener() {
//                    @Override
//                    public void onCityChangeListner() {
//                        lat = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.latu);
//                        lng = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.lngu);
//                        cityCode = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.citycode);
////                        changeBanner();
//                    }
//                });
//            }
        }
    }

}

