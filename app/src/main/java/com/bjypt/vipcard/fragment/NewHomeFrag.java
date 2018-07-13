package com.bjypt.vipcard.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.activity.MainActivity;
import com.bjypt.vipcard.adapter.HomeRecyclerViewAdapter;
import com.bjypt.vipcard.adapter.NewHomeAdapter;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.bean.AnnouncementBean;
import com.bjypt.vipcard.bean.WeatherBean;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.AppCategoryBean;
import com.bjypt.vipcard.model.LifeHuiData;
import com.bjypt.vipcard.model.XinWenAdBean;
import com.bjypt.vipcard.model.XinWenData;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshScrollView;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.GaoDeMapLocation;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.CheckUpdateAppVersionContext;
import com.bjypt.vipcard.view.TextViewMult;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.view.categoryview.AppCategoryContextView;
import com.bjypt.vipcard.view.categoryview.AppCategoryHomeBannerView;
import com.bjypt.vipcard.view.categoryview.AppCategoryHomeMenuView;
import com.bjypt.vipcard.view.categoryview.AppCategoryHomeRecyclerViewList;
import com.bjypt.vipcard.widget.GlidePopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.sinia.orderlang.bean.RedpacketBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2016/12/13.
 */

public class NewHomeFrag extends BaseFrament implements VolleyCallBack, TextViewMult.OnItemClickListener {
    /*title  start*/
    private LinearLayout ly_change_city_new_home;        //切换城市
    private TextView tv_city_new_home;                    //城市名称
    // title
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;


    // 天气
    @BindView(R.id.ibtn_weather)
    ImageView ibtn_weather;

    @BindView(R.id.tv_hot_news)
    TextViewMult tv_hot_news;

    private BroadCastReceiverUtils utils;
//    private MyBroadCastReceiver myBroadCastReceiver;            //切换城市的推送

    // 标题右侧图标按钮
    private String address;
    private Context context;


    private NewHomeAdapter newHomeAdapter;
    private List<Integer> picList = new ArrayList<>();                    //表示新闻当中惠出现几张图片
    private XinWenData xinWenData;
    private List<XinWenAdBean> xinwenList = new ArrayList<>();
    private int page = 1;
    private long lasttime;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isRefresh = 0;
    private RedpacketBean redpacketBean;
    private LifeHuiData lifeHuiData;
    private static final int SIGN_IN_GONGGAO = 1124;
    private static final int CONVERSION = 1001;      // 商家立即买单去兑换

    //    private SlidingTabLayout tl_10;                     //新闻分类tab
    private ArrayList<HomeNewsFragment> mFragments = new ArrayList<HomeNewsFragment>();
    private String[] listName;
    //    private static final int NEWS_CATEGORY = 1775;
    //    private MyPagerAdapter mAdapter;
//    private NewsViewpager vp;
    private List<String> tabList = new ArrayList<>();
    private PullToRefreshScrollView ptrs_new_home;
    private boolean is_refresh = true;

    private HomeRecyclerViewAdapter recyclerViewAdapter;

    CheckUpdateAppVersionContext updateAppVersionContext;//更新容器

    AppCategoryHomeBannerView appCategoryHomeBannerView;//首页banner

    AppCategoryHomeMenuView appCategoryHomeMenuView;//首页八大汇菜单

    AppCategoryHomeRecyclerViewList appCategoryHomeRecyclerViewList;//公共服务 生活服务 交通出行

    private View view;
    private WeatherBean weatherBean;
    private GlidePopupWindow glidePopupWindow;
    private ArrayList<AnnouncementBean> announcementBeans;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv_hot_news.setTexts(announcementBeans, 1);

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_new_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void beforeInitView() {

//        utils = new BroadCastReceiverUtils();
//        myBroadCastReceiver = new MyBroadCastReceiver();
//        utils.registerBroadCastReceiver(getActivity(), "change_city", myBroadCastReceiver);//注册切换城市的广播

        View statusBar = view.findViewById(R.id.statusBarView);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();

        getWeather();
        requestGongGao();
    }

    /**
     * 天气
     */
    private void getWeather() {
        Logger.e("城市 :"+getFromSharePreference(Config.userConfig.CURRENT_CITY));
        final Map<String, String> maps = new HashMap<>();
        maps.put("city", getFromSharePreference(Config.userConfig.CURRENT_CITY));
        Wethod.httpPost(getActivity(), 20, Config.web.WEATHER, maps, new VolleyCallBack<String>() {
            @Override
            public void onSuccess(int reqcode, String result) {
//                com.orhanobut.logger.Logger.e("reqcode :" + reqcode);
                com.orhanobut.logger.Logger.e("result :" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String resultStatus = jsonObject.getString("resultStatus");
//                    com.orhanobut.logger.Logger.e("resultStatus :" + resultStatus);
                    JSONObject resultData = jsonObject.getJSONObject("resultData");
                    if (resultData != null) {
                        weatherBean = new WeatherBean();
                        String imageUrl = resultData.getString("img");
                        weatherBean.setImageUrl(imageUrl);
                        if (resultData.toString().contains("aqi")) {
                            // 空气质量
                            String quality = resultData.getJSONObject("aqi").getString("quality");
                            weatherBean.setAir_quality(quality);
                        }
                        // 日期
                        String data = resultData.getString("date");
                        weatherBean.setDate(data);
                        // 气温
                        String temp = resultData.getString("temp");
                        weatherBean.setAir_temperature(temp);

                        ImageLoader.getInstance().displayImage(imageUrl, ibtn_weather);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int reqcode, String result) {
                com.orhanobut.logger.Logger.e("result :" + result);
            }

            @Override
            public void onError(VolleyError volleyError) {
                com.orhanobut.logger.Logger.e("volleyError :" + volleyError.toString());
            }
        }, View.GONE);
    }


    //公告数据
    public void requestGongGao() {
        Map<String, String> params = new HashMap<>();
        params.put("city_code", SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.web.cityCode, "1558"));
        params.put("app_type", "8");
        Wethod.httpPost(getActivity(), SIGN_IN_GONGGAO, Config.web.MY_WALLET, params, new VolleyCallBack<String>() {
            @Override
            public void onSuccess(int reqcode, String result) {
                if (reqcode == SIGN_IN_GONGGAO) {
                    com.orhanobut.logger.Logger.e("广告: " + result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("resultData");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            announcementBeans = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(0);
                                AnnouncementBean announcementBean = new AnnouncementBean();
                                String app_id = data.getString("app_id");
                                announcementBean.setApp_id(app_id);
                                String messages = data.getString("app_name");
                                announcementBean.setApp_name(messages);
                                String app_icon = data.getString("app_icon");
                                announcementBean.setApp_icon(app_icon);
                                String mtlevel = data.getString("mtlevel");
                                announcementBean.setMtlevel(mtlevel);
                                String parent_app_id = data.getString("parent_app_id");
                                announcementBean.setParent_app_id(parent_app_id);
                                String city_code = data.getString("city_code");
                                announcementBean.setCity_code(city_code);
                                int isentry = data.getInt("isentry");
                                announcementBean.setIsentry(isentry);
                                int link_type = data.getInt("link_type");
                                announcementBean.setLink_type(link_type);
                                if(data.has("link_url")){
                                    String link_url = data.getString("link_url");
                                    announcementBean.setLink_url(link_url);
                                }
                                announcementBeans.add(announcementBean);
                            }
                            handler.sendEmptyMessage(1);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int reqcode, String result) {

            }

            @Override
            public void onError(VolleyError volleyError) {

            }
        });
    }

    @OnClick(R.id.ibtn_weather)
    public void viewClick(View view) {
        if (weatherBean != null) {
            glidePopupWindow = new GlidePopupWindow(getContext());
            glidePopupWindow.setmPopupWindowData(weatherBean);
            glidePopupWindow.showAsDropDown(rl_title);
        } else {
            ToastUtil.showToast(getContext(), "加载失败,请刷新后重试");
        }
    }

    @Override
    public void initView() {

        ptrs_new_home = (PullToRefreshScrollView) getActivity().findViewById(R.id.ptrs_new_home);
        ptrs_new_home.getLoadingLayoutProxy().setLastUpdatedLabel("lastUpdateLabel");
        ptrs_new_home.getLoadingLayoutProxy().setPullLabel("PULLLABLE");
        ptrs_new_home.getLoadingLayoutProxy().setRefreshingLabel("refreshingLabel");
        ptrs_new_home.getLoadingLayoutProxy().setReleaseLabel("releaseLabel");

        //上拉、下拉设定
        ptrs_new_home.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        address = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.pname) + SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.cname) + SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.adName);
        ly_change_city_new_home = (LinearLayout) getActivity().findViewById(R.id.ly_change_city_new_home);
        tv_city_new_home = (TextView) getActivity().findViewById(R.id.tv_city_new_home);
        appCategoryHomeBannerView = (AppCategoryHomeBannerView) getActivity().findViewById(R.id.appCategoryHomeBannerView);
        appCategoryHomeMenuView = (AppCategoryHomeMenuView) getActivity().findViewById(R.id.appCategoryHomeMenuView);
        appCategoryHomeRecyclerViewList = (AppCategoryHomeRecyclerViewList)view.findViewById(R.id.appCategoryHomeRecyclerViewList);

        appCategoryHomeMenuView.setOnloadCompleteListener(new AppCategoryContextView.OnloadCompleteListener() {
            @Override
            public void onComplete(String result) {
                ptrs_new_home.onRefreshComplete();
            }

            @Override
            public void onError(VolleyError volleyError) {
                ptrs_new_home.onRefreshComplete();
            }
        });

        tv_hot_news.setOnItemClickListener(this);
    }


    @Override
    public void afterInitView() {
//        requestNewsCategory();
//        check_permission();

        updateAppVersionContext = new CheckUpdateAppVersionContext(getActivity(), ibtn_weather);
        updateAppVersionContext.startCheck();

        reReqeust();
        lasttime = Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0, 10));
//        getClassifyData();

    }

    /*
    * 检查定位权限并请求服务器
    * */
    public void check_permission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            getWeather();
        } else {
            if (SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.adress) == null || SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.adress).equals("")) {
                GaoDeMapLocation gaoDeMapLocation = new GaoDeMapLocation(getActivity());
                gaoDeMapLocation.startLocation();
                gaoDeMapLocation.setDingWeiSuccessListener(new GaoDeMapLocation.DingWeiSuccessListener() {
                    @Override
                    public void onDingWeiSuccessListener() {
                        getWeather();
                    }
                });
                gaoDeMapLocation.setCityChangeListener(new GaoDeMapLocation.CityChangeListener() {
                    @Override
                    public void onCityChangeListner() {
                        getWeather();
                    }
                });
            } else {
                getWeather();
            }
        }
        getWeather();
    }

    /*
    * 请求服务器获取
    * */
//    private void setReqest() {
//        //setTianQi();
////        String cityname = getFromSharePreference(Config.userConfig.cname);
////        if (StringUtil.isEmpty(cityname)) {
////            utils.sendBroadCastReceiver(getActivity(), "change_city", "updata", "阜阳");
////        }
//        reReqeust();
////        setCityName(cityname == null ? "阜阳" : cityname);
//    }

    /*
    * 获取大分类
    * 获取论坛、聊一聊、新闻、常用分类、系统消息
    * */
    private void reReqeust() {
        appCategoryHomeBannerView.reload();
        appCategoryHomeMenuView.reload();
        appCategoryHomeRecyclerViewList.reload();
    }

    @Override
    /*
    * 绑定事件
    * */
    public void bindListener() {

        newHomeAdapter = new NewHomeAdapter(getActivity(), xinwenList, picList);

        ptrs_new_home.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getWeather();
                requestGongGao();
                onRequest(QUERY_REFERSH);
                reReqeust();
//                getClassifyData();
//                com.orhanobut.logger.Logger.e("已刷新。。。。。");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                onRequest(QUERY_MORE);
            }
        });
    }

    private void onRequest(int type) {
        if (type == QUERY_REFERSH) {
            appCategoryHomeBannerView.reload();
            is_refresh = true;
        } else {
            is_refresh = false;
        }
    }


//    <---------------首页分类-------------->



//    private void getClassifyData() {
//        Map<String, String> params = new HashMap<>();
//        params.put("city_code", SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.web.cityCode, "1558"));
////        params.put("city_code", Config.userConfig.citycode);
//        params.put("mtlevel", "1");
//
//        Wethod.httpPost(getContext(), request_code_data, Config.web.application_common_menu, params, new VolleyCallBack<String>() {
//
//
//            private HomeRecyclerViewAdapter homeRecyclerViewAdapter;
//
//            @Override
//            public void onSuccess(int reqcode, String result) {
//                com.orhanobut.logger.Logger.e("reqcode :" + reqcode);
//                com.orhanobut.logger.Logger.json(result);
//                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
//                if (StringUtil.isNotEmpty(result)) {
//                    try {
//                        AppCategroyLifeTypeResultDataBean appCategroyLifeTypeResultDataBean = objectMapper.readValue(result.toString(), AppCategroyLifeTypeResultDataBean.class);
//                        if (appCategroyLifeTypeResultDataBean != null && appCategroyLifeTypeResultDataBean.getResultData() != null) {
//                            List<AppCategoryLifeTypeBean> appCategoryLifeTypeBeans = appCategroyLifeTypeResultDataBean.getResultData();
//                            for (int i = 0; i < appCategoryLifeTypeBeans.size(); i++) {
//
//                                AppCategoryLifeTypeBean appCategoryLifeTypeBean = appCategoryLifeTypeBeans.get(i);
//                                Logger.d(appCategoryLifeTypeBean);
//                                Logger.e(appCategoryLifeTypeBean.getApp_name());
//                                titles.get(i).setText(appCategoryLifeTypeBean.getApp_name());
//
//                                homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(getContext(), appCategoryLifeTypeBean.getSubLife());
//                                items.get(i).setAdapter(homeRecyclerViewAdapter);
//
//                            }
//                        } else {
////                            com.orhanobut.logger.Logger.e("appCategroyLifeTypeResultDataBean 为空");
//                        }
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailed(int reqcode, String result) {
//                LogUtil.debugPrint("网络请求失败：reqcode=" + reqcode + "  result=" + result);
//            }
//
//            @Override
//            public void onError(VolleyError volleyError) {
//                LogUtil.debugPrint("网络请求错误：" + volleyError.getMessage());
//            }
//        }, View.GONE);
//    }

    @Override
    /*
    * 处理事件
    * */
    public void onClickEvent(View v) {
        switch (v.getId()) {
//            case R.id.ly_change_city_new_home:
//                startActivity(new Intent(getActivity(), CityActivity.class));
//                break;
//            case R.id.tv_more_new_home:
//                startActivity(new Intent(getActivity(), LifeTypeActivity.class));
//                break;
//            case R.id.search_new_home:
//                startActivity(new Intent(getActivity(), SearchMerchantActivity.class));
//                break;
//            case R.id.iv_message:
//                startActivity(new Intent(getActivity(), NewsActivity.class));
//                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONVERSION && resultCode == 12) {
            ((MainActivity) getActivity()).setTabSelection(1);         //点击去抵扣中的商家立即买单跳到店铺街页面
        }
    }

    /**
     * 服务器返回的结果
     * 1.首页广告
     * 2.大分类
     * 3.检查更新
     * 4.首页常用分类、聊一聊、论坛、系统广告
     */
    @Override
    public void onSuccess(int reqcode, Object result) {
        ptrs_new_home.onRefreshComplete();// 停止刷新
//        if (reqcode == NEWS_CATEGORY) {
//            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
//            NewsFuYangTab newsFuYangTab = null;
//            try {
//                com.orhanobut.logger.Logger.json(result.toString());
//                newsFuYangTab = objectMapper.readValue(result.toString(), NewsFuYangTab.class);
//                if (null != newsFuYangTab.getResultData() && !("").equals(newsFuYangTab.getResultData())) {
//                    for (int i = 0; i < newsFuYangTab.getResultData().size(); i++) {
//                        tabList.add(newsFuYangTab.getResultData().get(i).getCatname());
////                        mFragments.add(HomeNewsFragment.getInstance(newsFuYangTab.getResultData().get(i).getCatname(), newsFuYangTab.getResultData().get(i).getCatid(), vp, i));
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            listName = tabList.toArray(new String[tabList.size()]);
//
////            initNewsData();
//        }

    }

    @Override
    public void onClick(AnnouncementBean announcementBean) {
        if (announcementBean.getIsentry() == AppCategoryBean.LoginTypeEnum.Login.getValue() && !"Y".equals(SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.userConfig.is_Login))) {
            getContext().startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }
        if (announcementBean.getLink_type() == AppCategoryBean.ActionTypeEnum.H5.getValue()) {
            String url = announcementBean.getLink_url();
            if (announcementBean.getIsentry() == AppCategoryBean.LoginTypeEnum.Login.getValue()) {
                if (!url.endsWith("pkregister=")) {
                    url = url + "pkregister=";
                }
                url = url + SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.userConfig.pkregister);
            }
            Intent intent = new Intent(getContext(), LifeServireH5Activity.class);
            intent.putExtra("life_url", url);
            intent.putExtra("isLogin", "N");
            intent.putExtra("serviceName", announcementBean.getApp_name());
            getContext().startActivity(intent);
        }
    }

    @Override
    /*
    * 请求服务器失败
    * */
    public void onFailed(int reqcode, Object result) {
        ptrs_new_home.onRefreshComplete();// 停止刷新
        if (reqcode == 2) {
            Wethod.ToFailMsg(getActivity(), result.toString());
        } else if (reqcode == 1991) {
            Wethod.ToFailMsg(getActivity(), result.toString());
        }
    }


    @Override
    /*
    * 请求服务器异常
    * */
    public void onError(VolleyError volleyError) {
        ptrs_new_home.onRefreshComplete();// 停止刷新
    }

    /**
     * 请求新闻分类网络
     */
    private void requestNewsCategory() {
//        Wethod.httpPost(getActivity(), NEWS_CATEGORY, Config.web.fyzx_news_category, null, this, View.GONE);
    }

    @Override
    /*
    当网络重新连接时调用
    * */
    public void isConntectedAndRefreshData() {
        // init_red_package_adv();//初始化红包广告
        reReqeust();
    }


    /* 卸载apk */
    public static void uninstallApk(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        updateAppVersionContext.destory();
//        if (utils != null) {
//            utils.UnRegisterBroadCastReceiver(getActivity(), myBroadCastReceiver);
//        }
    }


//    class MyBroadCastReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(final Context context, Intent intent) {
//            Log.e("liyunte", "onreceiver" + intent.getAction());
//            if ("change_city".equals(intent.getAction())) {
//                String city = intent.getStringExtra("updata");
//                setCityName(city);
//                // Wether.getWratherInfo(city, tv_wrather);
//                GaoDeMapLocation gaoDeMapLocation = new GaoDeMapLocation(getActivity());
//                gaoDeMapLocation.doSearchQuery(city);
//                gaoDeMapLocation.setCityChangeListener(new GaoDeMapLocation.CityChangeListener() {
//                    @Override
//                    public void onCityChangeListner() {
//                        Log.e("liyunte", "dosercht");
//                        address = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.pname) + SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.cname) + SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.adName);
//
//                        reReqeust();
//                    }
//                });
//            }
//        }
//    }


//    private void setCityName(String city) {
//        if (StringUtil.isEmpty(city)) {
//            return;
//        }
//        if (city.length() >= 5) {
//            city = city.substring(0, 5);
//        }
//        Log.e("liyunte", city);
//        if (city.contains("市")) {
//            city = city.substring(0, city.indexOf("市"));
//        }
//        tv_city_new_home.setText(city);
//    }


}
