package com.bjypt.vipcard.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.CityActivity;
import com.bjypt.vipcard.activity.FirstActivity;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.activity.MerchantTypeActivity;
import com.bjypt.vipcard.activity.NewSubscribeDishesActivity;
import com.bjypt.vipcard.activity.NewsActivity;
import com.bjypt.vipcard.activity.SearchMerchantActivity;
import com.bjypt.vipcard.activity.ZbarPayActivity;
import com.bjypt.vipcard.adapter.MerchantItemAdapter;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.DownLoadBean;
import com.bjypt.vipcard.model.DownLoadResultBean;
import com.bjypt.vipcard.model.HomeTest;
import com.bjypt.vipcard.model.HomeTypeBean;
import com.bjypt.vipcard.model.HomeTypeData;
import com.bjypt.vipcard.model.NewHomeMerchantListBean;
import com.bjypt.vipcard.model.NewMerchantListBean;
import com.bjypt.vipcard.model.Sentence;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshExpandableListView;
import com.bjypt.vipcard.service.DownLoadService;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.GaoDeMapLocation;
import com.bjypt.vipcard.utils.GaoDeTimerLocation;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.ViewPagerGridView;
import com.bjypt.vipcard.view.categoryview.AppCategoryHomeBannerView;
import com.bjypt.vipcard.zbar.activity.CaptureActivity;
import com.flyco.tablayout.SlidingTabLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by 涂有泽 .
 * Date by 2016/3/22
 * Use by 主界面首页Fragment
 */
public class HomeFragment extends BaseFrament implements VolleyCallBack<String> {

    private EditText search_merchant;
    private ViewPager mViewPager;
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private ImageView[] imageViews;
    private ImageView imageView;
    private ImageView nav_news;
    private ImageView iv_hone_news_red_point;//红点
    private List<View> gridViewList = new ArrayList<View>();
    private List<String> porList = new ArrayList<String>();
    private List<String> nameList = new ArrayList<String>();
    private AdPageAdapter adPageAdapter;
    private PullToRefreshExpandableListView mpullList;
    private View mHeadView;
    private ImageView iv_saomiao;
    private int next = 0;
    private LinearLayout mChangeCity;
    private List<NewMerchantListBean> listmerchant = new ArrayList<NewMerchantListBean>();
    private Map<String, String> maps = new HashMap<>();
    private int currentPage;
    private int pageLength = 10;
    private int isRefresh;
    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private List<String> pkmuserList = new ArrayList<>();//商户主键
    private List<String> distanceList = new ArrayList<>();//距离
    private List<Sentence> adList = new ArrayList<>();//广告
    private List<HomeTypeBean> gridview_list = new ArrayList<>();
    private MerchantItemAdapter mAdapter;
    private String popPicUrl;
    private TextView home_now_city;
    private NewHomeMerchantListBean homeMerchantListBean;
    private String lng;
    private String lat;
    private String cityCode;
    private GridView gridView;
    private HomeTest homeInfoBean;
    private BroadCastReceiverUtils utils;
    private RelativeLayout ly_popop;
    private ExpandableListView pullList;
    private MyBroadCastReceiver recei;
    private ViewPager vp_home_gridview;
    private LinearLayout layout_home_gridview_point;
    private ViewPagerGridView viewPagerGridView;
    private Context context;
    private AppCategoryHomeBannerView appCategoryHomeMerchantBannerView;

    private GaoDeTimerLocation  gaoDeTimerLocation;
    private Timer timer;
    private TimerTask timerTask;
    private SlidingTabLayout stl_store;
    private ViewPager vp_store;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        return inflater.inflate(R.layout.fra_home_page, container, false);
    }
    @Override
    public void beforeInitView() {
        utils = new BroadCastReceiverUtils();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;

        lat = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.latu);
        lng = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.lngu);
        cityCode = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.citycode);
//        Logger.e("_________ cityCode"+cityCode);
        recei = new MyBroadCastReceiver();
        utils.registerBroadCastReceiver(getActivity(), "change_city", recei);
        utils.registerBroadCastReceiver(getActivity(), "action_news", recei);
        utils.registerBroadCastReceiver(getActivity(), "updata_home_utils", recei);
        listmerchant = new ArrayList<NewMerchantListBean>();
        Wethod.httpPost(getActivity(), 1, Config.web.home_service, null, this);
        gaoDeTimerLocation = new GaoDeTimerLocation(this.getContext());
        gaoDeTimerLocation.setDingWeiSuccessListener(new GaoDeMapLocation.DingWeiSuccessListener() {
            @Override
            public void onDingWeiSuccessListener() {
                Log.i("wanglei", "xxxx");
                onRequest(QUERY_EXERCISE_REFERSH);
            }
        });
        timerTask = new TimerTask() {
            @Override
            public void run() {
                gaoDeTimerLocation.startLocation();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 5* 1000 * 60, 5* 1000 * 60);
//        timer.schedule(timerTask, 5* 1000, 5* 1000);

    }





//    public void getrequestLoacation() {
//        lat = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.latu);
//        lng = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.lngu);
//        cityCode = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.citycode);
//        getMerchantList(lat, lng, cityCode);
//    }

    @Override
    public void initView() {

        Log.e("SISI", "cityCode:" + cityCode + "   lat:" + lat + "   lng:" + lng);

        ly_popop = (RelativeLayout) getActivity().findViewById(R.id.ly_popop);
        mpullList = (PullToRefreshExpandableListView) getActivity().findViewById(R.id.home_list);
        pullList = mpullList.getRefreshableView();
        pullList.setGroupIndicator(null);
        pullList.setDividerHeight(0);
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.fra_home, null);
        pullList.addHeaderView(mHeadView);
        appCategoryHomeMerchantBannerView = (AppCategoryHomeBannerView)mHeadView.findViewById(R.id.appCategoryHomeMerchantBannerView);
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footview, null);
        pullList.addFooterView(footerView);

        stl_store = (SlidingTabLayout) mHeadView.findViewById(R.id.stl_store);
        vp_store= (ViewPager) mHeadView.findViewById(R.id.vp_store);
        mpullList.setMode(PullToRefreshBase.Mode.BOTH);
        nav_news = (ImageView) getActivity().findViewById(R.id.nav_news);
        home_now_city = (TextView) getActivity().findViewById(R.id.home_now_city);
        iv_hone_news_red_point = (ImageView) getActivity().findViewById(R.id.iv_hone_news_red_point);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getActivity(), "当前定位失败，默认定位阜阳", Toast.LENGTH_SHORT).show();
//            home_now_city.setText("阜阳");
//            GaoDeMapLocation gaoDeMapLocation = new GaoDeMapLocation(getActivity());
//            gaoDeMapLocation.setIsSaveLng();
//            gaoDeMapLocation.doSearchQuery("阜阳");
//            gaoDeMapLocation.setCityChangeListener(new GaoDeMapLocation.CityChangeListener() {
//                @Override
//                public void onCityChangeListner() {
//                    setReqest();
//                }
//            });
            setCityName(SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.cname));

            setReqest();

        } else {
            if (SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.adress) == null || SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.adress).equals("")) {
                GaoDeMapLocation gaoDeMapLocation = new GaoDeMapLocation(getActivity());
                gaoDeMapLocation.startLocation();
                gaoDeMapLocation.setDingWeiSuccessListener(new GaoDeMapLocation.DingWeiSuccessListener() {
                    @Override
                    public void onDingWeiSuccessListener() {
                        setReqest();
                    }
                });
            } else {
                setReqest();
            }
        }

        search_merchant = (EditText) getActivity().findViewById(R.id.search_merchant);
        iv_saomiao = (ImageView) getActivity().findViewById(R.id.iv_saomiao);
        mChangeCity = (LinearLayout) getActivity().findViewById(R.id.change_city);

        //        layout_home_point = (LinearLayout) mHeadView.findViewById(R.id.layout_home_point);
        //        viewPagerManager = new ViewPagerManager(getActivity(), vpList, layout_home_point, viewpager_home);
        vp_home_gridview = (ViewPager) mHeadView.findViewById(R.id.vp_home_gridview);
        layout_home_gridview_point = (LinearLayout) mHeadView.findViewById(R.id.layout_home_gridview_point);

        mViewPager = new ViewPager(getActivity());

        //获取屏幕相关信息
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        //根据屏幕信息设置ViewPager广告容器的宽高
        mViewPager.setLayoutParams(new ViewGroup.LayoutParams(dm.widthPixels, dm.heightPixels));

        //将ViewPager容器设置父容器中
        initCirclePoint();
        mAdapter = new MerchantItemAdapter(listmerchant, getActivity(), handler_content);
        pullList.setAdapter(mAdapter);
        Wethod.configImageLoader(getActivity());

        /****
         * 上拉下拉双向监听事件
         */

        mpullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                onRequest(QUERY_EXERCISE_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                onRequest(QUERY_EXERCISE_MORE);
            }
        });

    }

    private void onRequest(int type) {

        if (type == QUERY_EXERCISE_REFERSH) {
            appCategoryHomeMerchantBannerView.reload();
            if (maps != null) {
                maps.clear();
            }
            isRefresh = 1;
            currentPage = 0;
            maps.put("cityCode", cityCode);
            maps.put("lng", lng);
            maps.put("lat", lat);
            maps.put("begin", 0 + "");
            maps.put("pageLength", pageLength + "");

        } else {
            if (maps != null) {
                maps.clear();
            }
            currentPage += 10;
            Log.e("woyaokk", "currentPage：" + currentPage);

            isRefresh = 2;
            maps.put("cityCode", cityCode);
            maps.put("lng", lng);
            maps.put("lat", lat);
            maps.put("begin", currentPage + "");
            maps.put("pageLength", pageLength + "");
        }

        // Wethod.httpPost(getActivity(), 2, Config.web.home_merchant, maps, this);
        Wethod.httpPost(getActivity(), 100, Config.web.new_home_merchant_listview, maps, this, View.GONE);
    }

    @Override
    public void afterInitView() {
        Moren_show();//处理轮播和商家分类在网络慢显示空白的情况

        //开启banner页
        appCategoryHomeMerchantBannerView.reload();
    }

    @Override
    public void bindListener() {
        search_merchant.setOnClickListener(this);
        iv_saomiao.setOnClickListener(this);
        mChangeCity.setOnClickListener(this);
        nav_news.setOnClickListener(this);
    }

    public void startLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.iv_saomiao:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{
                                android.Manifest.permission.CAMERA,}, 110
                        );
                    } else {
                        if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                            Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
                            startActivityForResult(openCameraIntent, 0);
                        } else {
                            startLogin();
                        }
                    }

                } else {
                    if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                        Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
                        startActivityForResult(openCameraIntent, 0);
                    } else {
                        startLogin();
                    }
                }
                break;
            case R.id.search_merchant:
                Log.e("tyz", "success");
                Intent searchMerchantIntent = new Intent(getActivity(), SearchMerchantActivity.class);
                startActivity(searchMerchantIntent);
                break;
            case R.id.change_city:
                startActivity(new Intent(getActivity(), CityActivity.class));
                break;
            case R.id.nav_news:
                startActivity(new Intent(getActivity(), NewsActivity.class));
                iv_hone_news_red_point.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 110) {
            if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            } else {
                startLogin();
            }
        }
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 扫描二维码解析出的数据  切换城市
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {//二维码
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Log.e("GGG", scanResult);
           /* String split = ",";
            StringTokenizer token = new StringTokenizer(scanResult, split);
            while (token.hasMoreTokens()) {
               Log.e("liyunte",token.nextToken());
            }*/
            Log.e("woyaokk", "scanResult:" + scanResult);
            if (scanResult.contains("扫码支付")) {
                Intent intent = new Intent(getActivity(), ZbarPayActivity.class);
                intent.putExtra("scanResult", scanResult);
                getActivity().startActivity(intent);
            } else if (scanResult.contains("optionflag=1")) {

                String pkmuser = scanResult.substring(scanResult.indexOf("&") + 9, scanResult.length());
                Log.e("woyaokk", "pkmuser:" + pkmuser);
                Intent intent = new Intent(getActivity(), NewSubscribeDishesActivity.class);
                intent.putExtra("pkmuser", pkmuser);
                intent.putExtra("distance", "0km");
                getActivity().startActivity(intent);
            } else {
                Toast.makeText(getActivity(), scanResult, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<HomeTypeBean> homeInfoBeans = new ArrayList<>();

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 1) {
            Log.e("liyunte", "homeifforbean---------" + result.toString());
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                homeInfoBean = objectMapper.readValue(result.toString(), HomeTest.class);

                homeInfoBeans = homeInfoBean.getResultData().getMerCategory();
                popPicUrl = homeInfoBean.getResultData().getPopPicUrl();
                if (gridview_list.size() > 0) {
                    gridview_list.clear();
                }
                //TODO
                gridview_list.addAll(homeInfoBean.getResultData().getMerCategory());
                viewPagerGridView = new ViewPagerGridView(context, vp_home_gridview,
                        layout_home_gridview_point, gridview_list, 10);

                for (int m = 0; m < homeInfoBean.getResultData().getSysNotice().size(); m++) {
                    adList.add(new Sentence(m, homeInfoBean.getResultData().getSysNotice().get(m).getSlogan()));
                }
            } catch (IOException e) {

                e.printStackTrace();
            }

          /*
          * 商家分类的gridview的点击事件
          * */
            viewPagerGridView.setOnGridViewClickListener(new ViewPagerGridView.GridViewClickListener() {
                @Override
                public void onCridViewClickListener(View v, String pkmertype, String name) {
                    Intent intent = new Intent(getActivity(), MerchantTypeActivity.class);
                    intent.putExtra("pkmertype", pkmertype);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
            });
        } else if (reqcode == 110) {
            Intent intent = new Intent(getActivity(), NewSubscribeDishesActivity.class);
            intent.putExtra("pkmuser", "");
            intent.putExtra("distance", "");
            startActivity(intent);
        } else if (reqcode == 100) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                homeMerchantListBean = objectMapper.readValue(result.toString(), NewHomeMerchantListBean.class);
                if (homeMerchantListBean.getResultData().size() > 0) {
                    // iv_default_pic_home.setVisibility(View.GONE);
                    if (isRefresh == 1) {
                        if (mAdapter != null) {
                            listmerchant.clear();
                            distanceList.clear();
                            pkmuserList.clear();
                        }
                    }
                    for (int i = 0; i < homeMerchantListBean.getResultData().size(); i++) {
                        distanceList.add(homeMerchantListBean.getResultData().get(i).getDistance());
                        pkmuserList.add(homeMerchantListBean.getResultData().get(i).getPkmuser());
                        Log.e("eee", "" + homeMerchantListBean.getResultData().get(i).getMuname());
                        NewMerchantListBean listBean = new NewMerchantListBean();
                        listBean.setDiscount(homeMerchantListBean.getResultData().get(i).getDiscount());
                        listBean.setDistance(homeMerchantListBean.getResultData().get(i).getDistance());
                        listBean.setLogo(homeMerchantListBean.getResultData().get(i).getLogo());
                        listBean.setMuname(homeMerchantListBean.getResultData().get(i).getMuname());
                        listBean.setPkmuser(homeMerchantListBean.getResultData().get(i).getPkmuser());
                        listBean.setAddress(homeMerchantListBean.getResultData().get(i).getAddress());
                        listBean.setIntegral(homeMerchantListBean.getResultData().get(i).getIntegral());
                        listBean.setMerdesc(homeMerchantListBean.getResultData().get(i).getMerdesc());
                        if (!"".equals(homeMerchantListBean.getResultData().get(i).getLinkage_pkdealer())) {
                            listBean.setLinkage_pkdealer(homeMerchantListBean.getResultData().get(i).getLinkage_pkdealer());
                        }
                        if (!"".equals(homeMerchantListBean.getResultData().get(i).getVirtualActivity())) {
                            listBean.setVirtualActivity(homeMerchantListBean.getResultData().get(i).getVirtualActivity());
                        }
                        if (!"".equals(homeMerchantListBean.getResultData().get(i).getFirstConsume())) {
                            listBean.setFirstConsume(homeMerchantListBean.getResultData().get(i).getFirstConsume());
                        }
                        if (!"".equals(homeMerchantListBean.getResultData().get(i).getConsumeReduction())) {
                            listBean.setConsumeReduction(homeMerchantListBean.getResultData().get(i).getConsumeReduction());
                        }
                        listmerchant.add(listBean);
                    }

                    if (isRefresh == 1) {
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            mpullList.onRefreshComplete();
                        } else {
                            mAdapter = new MerchantItemAdapter(listmerchant, getActivity(), handler_content);
                            mAdapter.notifyDataSetChanged();
                            mpullList.onRefreshComplete();
                        }
                    } else if (isRefresh == 2) {
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                            mpullList.onRefreshComplete();
                        } else {
                            mAdapter = new MerchantItemAdapter(listmerchant, getActivity(), handler_content);
                            mAdapter.notifyDataSetChanged();
                            mpullList.onRefreshComplete();
                        }
                    } else {
                        mAdapter.add(listmerchant);
                        mAdapter.notifyDataSetChanged();
                        mpullList.onRefreshComplete();
                    }
                } else if (homeMerchantListBean.getResultData().size() == 0 && isRefresh != 2) {
                    Log.e("GGGG", "NULL-----------------------");
                    listmerchant.clear();
                    // iv_default_pic_home.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                    mpullList.onRefreshComplete();
                } else if (homeMerchantListBean.getResultData().size() == 0 && isRefresh == 2) {
                    mpullList.onRefreshComplete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            pullList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    Intent intent = new Intent(getActivity(), NewSubscribeDishesActivity.class);
                    intent.putExtra("distance", distanceList.get(groupPosition));
                    intent.putExtra("pkmuser", pkmuserList.get(groupPosition));
                    startActivity(intent);
                    return true;
                }
            });

        }

//        if (reqcode == 2) {
            //            ObjectMapper objectMapper = new ObjectMapper();
            //            try {
            //                homeMerchantListBean = objectMapper.readValue(result.toString(), HomeMerchantListBean.class);
            //                if (homeMerchantListBean.getResultData().size() > 0) {
            //                    // iv_default_pic_home.setVisibility(View.GONE);
            //                    if (isRefresh == 1) {
            //                        if (mAdapter != null) {
            //                            listmerchant.clear();
            //                            distanceList.clear();
            //                            pkmuserList.clear();
            //                        }
            //                    }
            //                    for (int i = 0; i < homeMerchantListBean.getResultData().size(); i++) {
            //                        distanceList.add(homeMerchantListBean.getResultData().get(i).getDistance());
            //                        pkmuserList.add(homeMerchantListBean.getResultData().get(i).getPkmuser());
            //                        Log.e("eee", "" + homeMerchantListBean.getResultData().get(i).getMuname());
            //                        MerchantListBean listBean = new MerchantListBean();
            //                        listBean.setSpecialPrice(homeMerchantListBean.getResultData().get(i).getSpecialPrice());
            //                        listBean.setIsfirst(homeMerchantListBean.getResultData().get(i).getIsfirst());
            //                        listBean.setDiscount(homeMerchantListBean.getResultData().get(i).getDiscount());
            //                        listBean.setDistance(homeMerchantListBean.getResultData().get(i).getDistance());
            //                        listBean.setJudgeAllNum(homeMerchantListBean.getResultData().get(i).getJudgeAllNum());
            //                        listBean.setLogo(homeMerchantListBean.getResultData().get(i).getLogo());
            //                        listBean.setMerdesc(homeMerchantListBean.getResultData().get(i).getMerdesc());
            //                        listBean.setMuname(homeMerchantListBean.getResultData().get(i).getMuname());
            //                        listBean.setPkmuser(homeMerchantListBean.getResultData().get(i).getPkmuser());
            //                        listBean.setStartLevel(homeMerchantListBean.getResultData().get(i).getStartLevel());
            //                        listBean.setAddress(homeMerchantListBean.getResultData().get(i).getAddress());
            //                        listBean.setCouponWelfare(homeMerchantListBean.getResultData().get(i).getCouponWelfare());
            //                        listBean.setMemberCount(homeMerchantListBean.getResultData().get(i).getMemberCount());
            //
            //                        int sum = 7;
            //                        List<ContentBean> list_content = new ArrayList<>();
            //                        if ("".equals(homeMerchantListBean.getResultData().get(i).getConsumeRedPackage()) ||
            //                                null == homeMerchantListBean.getResultData().get(i).getConsumeRedPackage()) {
            //                            sum--;
            //                            if ("".equals(homeMerchantListBean.getResultData().get(i).getMaxDiscount()) ||
            //                                    null == homeMerchantListBean.getResultData().get(i).getMaxDiscount()) {
            //                                if ("".equals(homeMerchantListBean.getResultData().get(i).getRechargeRedPackage())
            //                                        ||
            //                                        null == homeMerchantListBean.getResultData().get(i).getRechargeRedPackage()) {
            //                                    if ("".equals(homeMerchantListBean.getResultData().get(i).getRechargeWelfare())
            //                                            ||
            //                                            null == homeMerchantListBean.getResultData().get(i).getRechargeWelfare()) {
            //                                        if ("".equals(homeMerchantListBean.getResultData().get(i).getRegistRedPackage())
            //                                                ||
            //                                                null == homeMerchantListBean.getResultData().get(i).getRegistRedPackage()) {
            //                                            if ("".equals(homeMerchantListBean.getResultData().get(i).getHybcoin())
            //                                                    ||
            //                                                    null == homeMerchantListBean.getResultData().get(i).getHybcoin()) {
            //                                                if ("".equals(homeMerchantListBean.getResultData().get(i).getIntegral())
            //                                                        ||
            //                                                        null == homeMerchantListBean.getResultData().get(i).getIntegral()) {
            //                                                    listBean.setConsumeRedPackage("");
            //                                                } else {
            //                                                    listBean.setFlag(5);
            //                                                    listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getIntegral());
            //                                                }
            //
            //                                            } else {
            //                                                listBean.setFlag(4);
            //                                                listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getHybcoin());
            //                                            }
            //
            //                                        } else {
            //                                            listBean.setFlag(3);
            //                                            listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getRegistRedPackage());
            //                                        }
            //                                    } else {
            //                                        listBean.setFlag(2);
            //                                        listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getRechargeWelfare());
            //                                    }
            //                                } else {
            //                                    listBean.setFlag(1);
            //                                    listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getRechargeRedPackage());
            //                                }
            //                            } else {
            //                                listBean.setFlag(0);
            //                                listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getMaxDiscount());
            //                            }
            //                        } else {
            //                            listBean.setFlag(11);
            //                            listBean.setConsumeRedPackage(homeMerchantListBean.getResultData().get(i).getConsumeRedPackage());
            //                        }
            //                        if ("".equals(homeMerchantListBean.getResultData().get(i).getMaxDiscount()) ||
            //                                null == homeMerchantListBean.getResultData().get(i).getMaxDiscount()) {
            //                            sum--;
            //                        }
            //                        if ("".equals(homeMerchantListBean.getResultData().get(i).getRechargeRedPackage()) ||
            //                                null == homeMerchantListBean.getResultData().get(i).getRechargeRedPackage()) {
            //                            sum--;
            //                        }
            //                        if ("".equals(homeMerchantListBean.getResultData().get(i).getRechargeWelfare()) ||
            //                                null == homeMerchantListBean.getResultData().get(i).getRechargeWelfare()) {
            //                            sum--;
            //                        }
            //                        if ("".equals(homeMerchantListBean.getResultData().get(i).getRegistRedPackage()) ||
            //                                null == homeMerchantListBean.getResultData().get(i).getRegistRedPackage()) {
            //                            sum--;
            //                        }
            //                        if ("".equals(homeMerchantListBean.getResultData().get(i).getIntegral()) ||
            //                                null == homeMerchantListBean.getResultData().get(i).getIntegral()) {
            //                            sum--;
            //                        }
            //                        if ("".equals(homeMerchantListBean.getResultData().get(i).getHybcoin()) ||
            //                                null == homeMerchantListBean.getResultData().get(i).getHybcoin()) {
            //                            sum--;
            //                        }
            //                        listBean.setActivitysSum(sum);
            //
            //                        list_content.add(new ContentBean(homeMerchantListBean.getResultData().get(i).getMaxDiscount()));
            //                        list_content.add(new ContentBean(homeMerchantListBean.getResultData().get(i).getRechargeRedPackage()));
            //                        list_content.add(new ContentBean(homeMerchantListBean.getResultData().get(i).getRechargeWelfare()));
            //                        list_content.add(new ContentBean(homeMerchantListBean.getResultData().get(i).getRegistRedPackage()));
            //                        list_content.add(new ContentBean(homeMerchantListBean.getResultData().get(i).getHybcoin()));
            //                        list_content.add(new ContentBean(homeMerchantListBean.getResultData().get(i).getIntegral()));
            //                        listBean.setContentBeans(list_content);
            //                        listmerchant.add(listBean);
            //                    }
            //
            //
            //                    if (isRefresh == 1) {
            //                        if (mAdapter != null) {
            //                            mAdapter.notifyDataSetChanged();
            //                            mpullList.onRefreshComplete();
            //                        } else {
            //                            mAdapter = new MyExpandListViewAdapter(getActivity(), listmerchant, handler_content);
            //                            mAdapter.notifyDataSetChanged();
            //                            mpullList.onRefreshComplete();
            //                        }
            //
            //                    } else if (isRefresh == 2) {
            //                        if (mAdapter != null) {
            //                            mAdapter.notifyDataSetChanged();
            //                            mpullList.onRefreshComplete();
            //                        } else {
            //                            mAdapter = new MyExpandListViewAdapter(getActivity(), listmerchant, handler_content);
            //                            mAdapter.notifyDataSetChanged();
            //                            mpullList.onRefreshComplete();
            //                        }
            //                    } else {
            //                        mAdapter.add(listmerchant);
            //                        mAdapter.notifyDataSetChanged();
            //                        mpullList.onRefreshComplete();
            //                    }
            //                } else if (homeMerchantListBean.getResultData().size() == 0 && isRefresh != 2) {
            //                    Log.e("GGGG", "NULL-----------------------");
            //                    listmerchant.clear();
            //                    // iv_default_pic_home.setVisibility(View.VISIBLE);
            //                    mAdapter.notifyDataSetChanged();
            //                    mpullList.onRefreshComplete();
            //                } else if (homeMerchantListBean.getResultData().size() == 0 && isRefresh == 2) {
            //                    mpullList.onRefreshComplete();
            //                }
            //
            //
            //            } catch (IOException e) {
            //                e.printStackTrace();
            //            }

            //            pullList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            //                @Override
            //                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            //                    Intent intent = new Intent(getActivity(), NewSubscribeDishesActivity.class);
            //                    intent.putExtra("distance", distanceList.get(groupPosition));
            //                    intent.putExtra("rechargeactivity", listmerchant.get(groupPosition).getRechargeActivity());
            //
            //                    intent.putExtra("pkmuser", pkmuserList.get(groupPosition));
            //                    Log.e("GGGG", "pkmuser" + pkmuserList.get(groupPosition));
            //                    startActivity(intent);
            //
            //                    return true;
            //                }
            //            });

//        }
        if (reqcode == 70) {
            JSONObject object = null;
            Log.e("liyunteee", "reqcode == 70");
            try {
                object = new JSONObject(result.toString());
                if ("ok".equalsIgnoreCase(object.getString("status"))) {
                    JSONObject re = object.getJSONObject("result");
                    JSONObject location = re.getJSONObject("location");
                    String nowadress = re.getString("formatted_address");
                    cityCode = re.getString("cityCode");
                    SharedPreferenceUtils.saveToSharedPreference(getActivity(), "lng", lng);
                    SharedPreferenceUtils.saveToSharedPreference(getActivity(), "lat", lat);
                    SharedPreferenceUtils.saveToSharedPreference(getActivity(), "cityCode", cityCode);
                    onRequest(QUERY_EXERCISE_REFERSH);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (reqcode == 2016) {
            Log.e("liyunte", result.toString());
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                DownLoadBean downLoadBean = objectMapper.readValue(result.toString(), DownLoadBean.class);
                downLoadResultBean = downLoadBean.getResultData();
                if (downLoadResultBean.getUrl() != null && !"".equals(downLoadResultBean.getUrl())) {
//                    setPopup();
                } else {
                    // Toast.makeText(getActivity(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
                }
                Log.e("yytt", "popPicUrl:" + popPicUrl + "    ----:" + FirstActivity.HOME_IMAGE_FLAG);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

        if (reqcode == 1) {
            Wethod.ToFailMsg(getActivity(), result.toString());
        } else if (reqcode == 2) {
            Wethod.ToFailMsg(getActivity(), result.toString());
        } else if (reqcode == 3) {
            Wethod.ToFailMsg(getActivity(), result.toString());
        } else if (reqcode == 110) {
            Toast.makeText(getActivity(), "未注册第一商家", Toast.LENGTH_LONG).show();
        }
        mpullList.onRefreshComplete();
    }

    int i_flag = 0;

    @Override
    public void onError(VolleyError volleyError) {
        mpullList.onRefreshComplete();
        i_flag++;
        if (i_flag == 1) {
            Moren_show();
        }
    }

    public void Moren_show() {
        // ArrayList<String> strings = new ArrayList<>();
        // strings.add("");
        // strings.add("");
        // strings.add("");
        // strings.add("");
        // ViewPagerManager viewPagerManager = new ViewPagerManager(getActivity(), strings, layout_home_point, viewpager_home);
        // viewPagerManager.setLooping(false);
        List<HomeTypeBean> homeTypeBeanList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HomeTypeBean homeTypeBean = new HomeTypeBean();
            homeTypeBean.setAddressurl("");
            homeTypeBean.setLogourl("");
            homeTypeBean.setMerchantCount("");
            homeTypeBean.setMtlevel("");
            if (i == 0) {
                homeTypeBean.setMtname("美食");
            } else if (i == 1) {
                homeTypeBean.setMtname("酒店");
            } else if (i == 2) {
                homeTypeBean.setMtname("旅游");
            } else if (i == 3) {
                homeTypeBean.setMtname("购物");
            } else if (i == 4) {
                homeTypeBean.setMtname("爱美丽");
            } else if (i == 5) {
                homeTypeBean.setMtname("健康");
            } else if (i == 6) {
                homeTypeBean.setMtname(" 休闲娱乐");
            } else if (i == 7) {
                homeTypeBean.setMtname("智能");
            } else if (i == 8) {
                homeTypeBean.setMtname("生活服务");
            } else {
                homeTypeBean.setMtname("更多");
            }
            homeTypeBean.setType("");
            homeTypeBean.setPkmertype("");
            homeTypeBean.setParentpk("");
            homeTypeBeanList.add(homeTypeBean);
        }
        ViewPagerGridView viewPagerGridView = new ViewPagerGridView(getActivity(), vp_home_gridview, layout_home_gridview_point, homeTypeBeanList, 10);

    }

    /*
     * 获取商家列表数据
     */
    public void getMerchantList(String latityde, String longitude, String cityCode) {
        this.lat = latityde;
        this.lng = longitude;
        this.cityCode = cityCode;
        onRequest(QUERY_EXERCISE_REFERSH);
    }


    /**
     * ViewPager页面切换监听器
     */
    private final class AdPageChangeListener implements ViewPager.OnPageChangeListener {


        /**
         * 页面滚动的时候触发
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 页面选中的时候触发
         */
        @Override
        public void onPageSelected(int position) {
            //获取当前页面是哪个页面
            atomicInteger.getAndSet(position);
            //重新设置原点布局结合
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[position].setBackgroundResource(R.drawable.dian_click);
                if (position != i) {
                    imageViews[i].setBackgroundResource(R.drawable.dian_normal);
                }
            }
        }

        /**
         * 页面滚动状态发生改变的时候触发
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 初始化类型的小圆点
     */
    private void initCirclePoint() {
        imageViews = new ImageView[gridViewList.size()];
        //广告栏的小圆点图标
        for (int i = 0; i < gridViewList.size(); i++) {
            //创建一个ImageView，并设置宽高，将该对象放入数组中
            imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 0, 10, 0);
            imageView.setLayoutParams(layoutParams);
            imageViews[i] = imageView;

            // 初始值, 默认第0个选中
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.dian_click);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.dian_normal);
            }
            // 将小圆点放入到布局中
            // viewGroup.addView(imageViews[i]);
        }
    }

    /**
     * 判断类型的时候会分几页显示
     */
    private void getGridView() {
        boolean bool = true;
        while (bool) {
            int result = next + 10;
            if (porList.size() != 0 && result < porList.size()) {
                gridView = new GridView(getActivity());
                gridView.setNumColumns(5);
                List<HomeTypeData> gridList = new ArrayList<>();
                for (int i = next; i < result; i++) {
                    gridList.add(new HomeTypeData(porList.get(i), nameList.get(i)));
                }
                MyAdapter myAdapter = new MyAdapter(gridList);
                gridView.setAdapter(myAdapter);
                next = result;
                gridViewList.add(gridView);
            } else if (result - porList.size() <= 10) {
                List<HomeTypeData> gridList = new ArrayList<>();
                for (int i = next; i < porList.size(); i++) {
                    gridList.add(new HomeTypeData(porList.get(i), nameList.get(i)));
                }
                gridView = new GridView(getActivity());
                gridView.setNumColumns(5);
                MyAdapter myAdapter = new MyAdapter(gridList);
                gridView.setAdapter(myAdapter);
                next = porList.size() - 1;
                gridViewList.add(gridView);
                bool = false;
            } else {
                bool = false;
            }
        }
        adPageAdapter = new AdPageAdapter(gridViewList);
        mViewPager.setAdapter(adPageAdapter);
        mViewPager.setOnPageChangeListener(new AdPageChangeListener());
    }

    private class MyAdapter extends BaseAdapter {
        List<HomeTypeData> listgrid;
        private LayoutInflater mInflater;

        private MyAdapter(List<HomeTypeData> gridList) {
            this.listgrid = gridList;
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return listgrid.size();
        }

        @Override
        public Object getItem(int position) {
            return listgrid.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.home_type_item, null);
            ImageView home_type_iv = (ImageView) convertView.findViewById(R.id.home_type_iv);
            TextView home_type_tv = (TextView) convertView.findViewById(R.id.home_type_tv);
            Log.e("woyaokk", "url---->" + listgrid.get(position).getName());
            ImageLoader.getInstance().displayImage(Config.web.picUrl + listgrid.get(position).getUrl(), home_type_iv, AppConfig.DEFAULT_IMG_OPTIONS_AD);
            home_type_tv.setText(listgrid.get(position).getName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("liyunte", "---------------------grideview");
                    String pkmertype = homeInfoBeans.get(position).getPkmertype();
                    String name = homeInfoBeans.get(position).getMtname();
                    String data = pkmertype + "," + name;
                    Message msg = handler_home.obtainMessage(1, data);
                    Log.e("liyunte", "" + pkmertype + "name" + name);
                    handler_home.sendMessage(msg);
                }
            });
            return convertView;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(timer != null)
            timer.cancel();
        if(timerTask != null)
            timerTask.cancel();
    }

    private final class AdPageAdapter extends PagerAdapter {

        private List<View> views = null;

        /**
         * 初始化数据源, 即View数组
         */
        public AdPageAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        /**
         * 从ViewPager中删除集合中对应索引的View对象
         */
        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        /**
         * 从View集合中获取对应索引的元素, 并添加到ViewPager中
         */
        @Override
        public Object instantiateItem(View container, final int position) {
            ((ViewPager) container).addView(views.get(position), 0);
            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        //        MainActivity mainActivity = (MainActivity) activity;
        //        handler_home = mainActivity.getHandler();
        super.onAttach(activity);
    }

//    public void changeBanner() {
//        Map<String, String> params = new HashMap<>();
//        params.put("citycode", getFromSharePreference(Config.userConfig.citycode));
//        params.put("versionCode", "v4.4.2");
//        Wethod.httpPost(getActivity(), 3, Config.web.get_banner, params, this);
//    }

    private Handler handler_home;

    class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.e("rrrr", intent.getAction());
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
//                        onRequest(QUERY_EXERCISE_REFERSH);
////                        changeBanner();
//                        appCategoryHomeMerchantBannerView.reload();
//                    }
//                });
//            } else if ("action_news".equals(intent.getAction())) {
//                iv_hone_news_red_point.setVisibility(View.VISIBLE);
//            } else if ("updata_home_utils".equals(intent.getAction())) {
//                setReqest();
//            }
        }
    }

    public void setReqest() {
        lat = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.latu);
        lng = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.lngu);
        cityCode = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.citycode);
        setCityName(SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.adress));
        onRequest(QUERY_EXERCISE_REFERSH);
        //startBanner();
    }

    private int screenWidth;
    private int screenHeigh;
    private TextView tv_back;
    private TextView tv_entry;
    private TextView tv_version;
    private PopupWindow popupWindow;
    private PopupWindow imgaePopupWindow;
    private DownLoadResultBean downLoadResultBean;
    private RelativeLayout mHomeImageRela;
    private ImageView mHomeImageView;

    public static String RECIVER_ACTIONS = "com.bjypt.vipcard";
    private DownLoadService.MyBinder myBinder;
    private boolean flag = false;
    private DownLoadReciver receiver;
    private int progress = 0;
    private RelativeLayout mImageBack;

    public void setImageViewPopup() {
        View view = View.inflate(getActivity(), R.layout.home_image_pup, null);
        imgaePopupWindow = new PopupWindow(view, screenWidth, screenHeigh);
        imgaePopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mHomeImageRela = (RelativeLayout) view.findViewById(R.id.home_image_rela);
        mHomeImageView = (ImageView) view.findViewById(R.id.home_image_view);
        mImageBack = (RelativeLayout) view.findViewById(R.id.image_delete);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        ImageLoader.getInstance().displayImage(popPicUrl, mHomeImageView, AppConfig.DEFAULT_IMG_OPTIONS_AD);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgaePopupWindow.dismiss();
            }
        });
        imgaePopupWindow.showAtLocation(ly_popop, Gravity.BOTTOM, 0, 0);
    }

    /* 卸载apk */
    public static void uninstallApk(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("aaa", "下载服务启动成功");
            myBinder = (DownLoadService.MyBinder) service;

            flag = true;
            //开始下载
            myBinder.setFlags(flag,"开始下载");
            tv_version.setText("正在准备资源...");
            myBinder.startdownLoad(downLoadResultBean.getUrl());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("aaa", "下载服务启动失败");
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // imgaePopupWindow.dismiss();
        if (utils != null) {
            utils.UnRegisterBroadCastReceiver(getActivity(), recei);
        }

        if (myBinder != null) {
            getActivity().unbindService(mConnection);
        }
        //if (viewPagerManager != null) {
        //    viewPagerManager.setLooping(false);
        //}
    }

    public class DownLoadReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(RECIVER_ACTIONS)) {
                int size = Integer.parseInt(intent.getStringExtra("size"));
                int numRead = Integer.parseInt(intent.getStringExtra("numRead"));
                progress = numRead / (size / 100);
                Log.e("liyunte", "progress" + progress);

                if (progress == 100) {
                    unBind();
                    Toast.makeText(getActivity(), "下载完成", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                    flag = !flag;
                } else {
                    tv_version.setText("正在下载" + progress + "%");
                }
            }
        }
    }

    public void unBind() {
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public String getVersion() {
        try {
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        lat = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.latu);
        lng = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.lngu);
        cityCode = SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.citycode);
        UmengCountContext.onPageStart("HomeFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengCountContext.onPageEnd("HomeFragment");
    }

    Handler handler_content = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 110) {
                String value = (String) msg.obj;
                int sum = Integer.parseInt(value.substring(0, value.indexOf("a")));
                if (sum % 2 == 0) {
                    pullList.collapseGroup(Integer.parseInt(value.substring(value.indexOf("a") + 1)));
                } else {
                    pullList.expandGroup(Integer.parseInt(value.substring(value.indexOf("a") + 1)));
                }
            }
        }
    };

    @Override
    public void isConntectedAndRefreshData() {
        Wethod.httpPost(getActivity(), 1, Config.web.home_service, null, this);
        Map<String, String> params = new HashMap<>();
        params.put("citycode", getFromSharePreference(Config.userConfig.citycode));
        Wethod.httpPost(getActivity(), 3, Config.web.get_banner, params, this);
        GaoDeMapLocation gaoDeMapLocation = new GaoDeMapLocation(getActivity());
        gaoDeMapLocation.startLocation();
        gaoDeMapLocation.setDingWeiSuccessListener(new GaoDeMapLocation.DingWeiSuccessListener() {
            @Override
            public void onDingWeiSuccessListener() {
                setReqest();
            }
        });
    }

    private void setCityName(String city){
        if(StringUtil.isEmpty(city)){
            return;
        }
        if (city.length() >= 5) {
            city = city.substring(0, 5);
        }
        Log.e("liyunte", city);
        if (city.contains("市")) {
            city = city.substring(0, city.indexOf("市"));
        }
        home_now_city.setText(city);
    }

}
