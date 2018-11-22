package com.bjypt.vipcard.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.SearchMerchantActivity;
import com.bjypt.vipcard.adapter.cityconnect.ReaycleBannerAdapter;
import com.bjypt.vipcard.adapter.cityconnect.YouHuiQuanGoBannerPager;
import com.bjypt.vipcard.base.BaseFragment;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.bean.YiuHuiCountGoBean;
import com.bjypt.vipcard.bean.YouHuiQuanBean;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.dialog.GetYouHuiListDialog;
import com.bjypt.vipcard.dialog.GetYouHuiListDialog2;
import com.bjypt.vipcard.model.AppCategoryBean;
import com.bjypt.vipcard.model.AppCategroyResultDataBean;
import com.bjypt.vipcard.model.HomeTest;
import com.bjypt.vipcard.model.HomeTypeBean;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.GaoDeMapLocation;
import com.bjypt.vipcard.utils.GaoDeTimerLocation;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.NewsViewpager;
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
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


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
    // private LinearLayout youhui_go_list;
    private GetYouHuiListDialog2 getYouHuiListDialog;
    private int request_YouHui_code_data = 13;
    private int request_go_code_data = 14;
    private YouHuiQuanBean youHuiQuanBean;
    private List<YouHuiQuanBean.YouHuiQuanDataBean> YouHuiQuanDataBeanlist = new ArrayList<>();
    private int[] mImageIds = {R.mipmap.youhui1_bg, R.mipmap.youhui2_bg};
    private int currentItem;
    private YiuHuiCountGoBean.ResultDataBean resultDataBean;
    boolean isSliding = false;
    private boolean isScoll = true;//可滑
    private boolean isLoop = true;//轮播
    private ViewPager viewpager_go;

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
        viewpager_go = view.findViewById(R.id.viewpager_go);
    }

    @Override
    public void afterInitView() {
        getYouHuiGoInfo();
        getYouHuiNetWork();
        //开启banner页
        int bannerType = 9;
        appCategoryHomeMerchantBannerView.reload();//布局中已经隐藏了;
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

    /*
     * 优惠券Go信息展示*/
    private void getYouHuiGoInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getPkregister());
        Wethod.httpPost(getActivity(), request_go_code_data, Config.web.city_connectin_Go_info, params, this, View.GONE);

    }

    /*
     * 请求弹框dialog优惠券信息的*/
    private void getYouHuiNetWork() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getPkregister());
        Wethod.httpPost(getActivity(), request_YouHui_code_data, Config.web.city_connectin_youhuiquann, params, this, View.GONE);

    }

    /*
     * 获取店铺街banner数据*/
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
        viewpager_go.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Toast.makeText(getActivity(), "Down", Toast.LENGTH_SHORT).show();
                        isLoop = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(getActivity(), "Up", Toast.LENGTH_SHORT).show();
                        isLoop = true;
                        break;
                }
                return isSliding; //是否可以左右滑动
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //返回到这个Fragment 界面时关闭Dialog
        if (getYouHuiListDialog != null) {
            // getYouHuiListDialog.closeDialog();
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
        } else if (reqcode == request_YouHui_code_data) { //优惠券
            LogUtil.debugPrint("优惠券成功信息 : " + result);
            try {
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                youHuiQuanBean = objectMapper.readValue(result.toString(), YouHuiQuanBean.class);
                if (youHuiQuanBean != null && youHuiQuanBean.getResultData().size() > 0) {
                    for (int i = 0; i < youHuiQuanBean.getResultData().size(); i++) {
                        YouHuiQuanDataBeanlist.add(youHuiQuanBean.getResultData().get(i));
                    }
                    //这里得到数据 传递给dialog zai 再有diaog 加载适配器
                    getYouHuiListDialog = new GetYouHuiListDialog2(getActivity(), YouHuiQuanDataBeanlist);
                    getYouHuiListDialog.showDialog();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == request_go_code_data) {
            LogUtil.debugPrint("优惠券个数 : " + result);
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                YiuHuiCountGoBean countGoBean = objectMapper.readValue(result.toString(), YiuHuiCountGoBean.class);
                if (countGoBean != null) {
                    resultDataBean = countGoBean.getResultData();
                    //增加判断，如果可用数量为0 则不显示轮播图
                    YouHuiQuanGoBannerPager goBannerPager = new YouHuiQuanGoBannerPager(getActivity(), mImageIds, resultDataBean);
                    viewpager_go.setAdapter(goBannerPager);
                        viewpager_go.setCurrentItem(0);
                 /* if (resultDataBean.getCanUseCoupon() == 0) {//可个数为0时可用界面不显示
                        viewpager_go.setCurrentItem(1);
                        isSliding = true;//不可滑动
                        isScoll = false;//不可轮播
                    } else {
                      viewpager_go.setCurrentItem(0);
                    }
                    if (resultDataBean.getOfflineCoupon() == 0) {//关闭线下功能//关闭轮播，切不能滑动
                        isSliding = true;//不可滑动
                        isScoll = false;//不可轮播
                    }*/
                    //定时轮播
                    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
                    executor.scheduleWithFixedDelay(runtask, 3000, 3000, TimeUnit.MILLISECONDS);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    Runnable runtask = new Runnable() {
        @Override
        public void run() {
            if (isLoop && isScoll) {
                handler.sendEmptyMessage(1);
            }
        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isLoop && isScoll) {
                nextItem();
            }
        }

    };

    /*
    播放下一个
    * */
    private void nextItem() {
        currentItem++;
        viewpager_go.setCurrentItem(currentItem);
        System.out.println("这是什么额原因 = " + currentItem);
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

        //banner 点击事件
        webBannerAdapter.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (listName.size() > 0) {
                    //调用封装的工具
                    appCategoryHomeMerchantBannerView.onAppCategoryItemClick(getItemBean(position));
                }
            }
        });

    }

    /*
     *  获取banner 点击参数*/
    public AppCategoryBean getItemBean(int postion) {
        if (appCategroyResultDataBean != null && appCategroyResultDataBean.getResultData().size() > postion) {
            return appCategroyResultDataBean.getResultData().get(postion);
        }
        return null;
    }

    @Override
    public void onFailed(int reqcode, String result) {
        if (reqcode == request_code_data) {
            loadRecyclerBanner();//加载默认数据
        }
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

