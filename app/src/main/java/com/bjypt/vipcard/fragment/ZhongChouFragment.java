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
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.activity.MainActivity;
import com.bjypt.vipcard.adapter.HomeRecyclerViewAdapter;
import com.bjypt.vipcard.adapter.NewHomeAdapter;
import com.bjypt.vipcard.adapter.RecommendAdapter;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.bean.AnnouncementBean;
import com.bjypt.vipcard.bean.WeatherBean;
import com.bjypt.vipcard.bean.ZhongChouBanerBean;
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
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.CheckUpdateAppVersionContext;
import com.bjypt.vipcard.view.NewsViewpager;
import com.bjypt.vipcard.view.TextViewMult;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.view.ZhongchouTextViewMult;
import com.bjypt.vipcard.view.categoryview.AppCategoryContextView;
import com.bjypt.vipcard.view.categoryview.AppCategoryHomeBannerView;
import com.bjypt.vipcard.view.categoryview.AppCategoryHomeMenuView;
import com.bjypt.vipcard.view.categoryview.AppCategoryHomeRecyclerViewList;
import com.bjypt.vipcard.view.categoryview.ZhongchouBannerView;
import com.bjypt.vipcard.widget.GlidePopupWindow;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by Administrator on 2016/12/13.
 */

public class ZhongChouFragment extends BaseFrament implements VolleyCallBack, ZhongchouTextViewMult.OnItemClickListener {

    // 标题右侧图标按钮
    private String address;
    private Context context;

    private List<Integer> picList = new ArrayList<>();                    //表示新闻当中惠出现几张图片
    private XinWenData xinWenData;
    private List<XinWenAdBean> xinwenList = new ArrayList<>();

    private static final int CONVERSION = 1001;      // 商家立即买单去兑换

    private List<String> tabList = new ArrayList<>();
    AppCategoryHomeRecyclerViewList appCategoryHomeRecyclerViewList;//公共服务 生活服务 交通出行

    private View view;
    private WeatherBean weatherBean;
    private GlidePopupWindow glidePopupWindow;
    private ArrayList<AnnouncementBean> announcementBeans;

    private ViewPager zhongchou_pager;
    int  CurrentItem = Integer.MAX_VALUE/2;
    private ScheduledExecutorService executor;
    private ArrayList<ZhongChouBanerBean> zhongChouBanerBeans;
    private ZhongchouBannerView bannerView;
    private ZhongchouTextViewMult tv_tuijian;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
               tv_tuijian.setTexts(zhongChouBanerBeans , 1);
                    break;
                case 2:

                    break;
            }
        }
    };
    private RecyclerView recycle_view;
    private SlidingTabLayout stl_zhongchou;
    private NewsViewpager vp_zhongchou;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_zhong_chou, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void beforeInitView() {
        View statusBar = view.findViewById(R.id.statusBarView);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        bannerView = getActivity().findViewById(R.id.zhongchouHomeBanner);
        tv_tuijian = getActivity().findViewById(R.id.tv_hot);
        recycle_view = (RecyclerView) getActivity().findViewById(R.id.recycle_view);

        stl_zhongchou = getActivity().findViewById(R.id.stl_zhongchou);
        vp_zhongchou = getActivity().findViewById(R.id.vp_zhongchou);

        getbanerData(); //辐条轮播
        getRecommendData();//今日推荐
       // getZhongchouData();//今日推荐
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recycle_view.setLayoutManager(layoutManager);
        recycle_view.setAdapter(new RecommendAdapter());

     }



    @Override
    public void initView() {
        tv_tuijian.setOnItemClickListener(this);
    }


    @Override
    public void afterInitView() {
        // lasttime = Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0, 10));
        bannerView.reload();
    }

     /*轮播图的数据
     * */

    public void getbanerData() {
        Wethod.httpPost(getActivity(),3,Config.web.zhongchou_baner_url,new HashMap<String, String>(),this);
      }

     /*
     今日推荐数据
     * */
     public void getRecommendData() {
         Wethod.httpPost(getActivity(), 3, Config.web.zhongchou_recomend_url, new HashMap<String, String>(), new VolleyCallBack<String>() {
             @Override
             public void onSuccess(int reqcode, String result) {
              LogUtil.debugPrint("Recommend onSuccess reqcode "+ result );
               Recommend(result);
             }

             @Override
             public void onFailed(int reqcode, String result) {
             LogUtil.debugPrint("Recommend onFailed reqcode "+ result );
             }

             @Override
             public void onError(VolleyError volleyError) {
              LogUtil.debugPrint("Recommend VolleyError reqcode "+ volleyError.getMessage() );
             }
         });
     }

      private void Recommend(String result) {
          try {
            JSONObject jsonObject = new JSONObject((String)result);
             JSONArray jsonArray = jsonObject.getJSONArray("resultData");
          } catch (JSONException e) {
              e.printStackTrace();
          }

      }

      private void getZhongchouData() {
          Map <String , String> maps = new HashMap<>();
          maps.put("pageNum","1");
          maps.put("pageSize","2");
          maps.put("sortType","3");
          Wethod.httpPost(getActivity(), 3, Config.web.zhongchou_list_url, maps, new VolleyCallBack<String>() {
            @Override
            public void onSuccess(int reqcode, String result) {
                LogUtil.debugPrint("ZhongchouData onSuccess reqcode "+ result );
            }

            @Override
            public void onFailed(int reqcode, String result) {
                LogUtil.debugPrint("ZhongchouData onFailed reqcode "+ result );
            }

            @Override
            public void onError(VolleyError volleyError) {
                LogUtil.debugPrint("ZhongchouData VolleyError reqcode "+ volleyError.getMessage() );
            }
        });

    }

    /*
    * 检查定位权限并请求服务器
    * */
    public void check_permission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
           // getWeather();
        } else {
            if (SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.adress) == null || SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.adress).equals("")) {
                GaoDeMapLocation gaoDeMapLocation = new GaoDeMapLocation(getActivity());
                gaoDeMapLocation.startLocation();
                gaoDeMapLocation.setDingWeiSuccessListener(new GaoDeMapLocation.DingWeiSuccessListener() {
                    @Override
                    public void onDingWeiSuccessListener() {
                     //   getWeather();
                    }
                });
                gaoDeMapLocation.setCityChangeListener(new GaoDeMapLocation.CityChangeListener() {
                    @Override
                    public void onCityChangeListner() {
                     //   getWeather();
                    }
                });
            } else {
               // getWeather();
            }
        }

    }


    @Override
    /*
    * 绑定事件
    * */
    public void bindListener() {
        vp_zhongchou.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                vp_zhongchou.resetHeight(position);
            }
            @Override
            public void onPageSelected(int position) {
                vp_zhongchou.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClickEvent(View v) {

    }

    private void onRequest(int type) {

    }


//    <---------------首页分类-------------->

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONVERSION && resultCode == 12) {
            ((MainActivity) getActivity()).setTabSelection(1);         //点击去抵扣中的商家立即买单跳到店铺街页面
        }
    }

    /**
     * 服务器返回的结果
     * 1.首页广告
     */
    @Override
    public void onSuccess(int reqcode, Object result) {
          LogUtil.debugPrint("onSuccess reqcoderesult "+ reqcode +" currentThread :" +Thread.currentThread()  );
        try {
            JSONObject jsonObject = new JSONObject((String)result);
            JSONArray jsonArray = jsonObject.getJSONArray("resultData");
            LogUtil.debugPrint("onSuccess reqcode "+ reqcode +" jsonArray " +jsonArray   );
           if(jsonArray != null &&jsonArray.length()>0 ){
               zhongChouBanerBeans  = new ArrayList<>();
               for (int i = 0; i <jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    ZhongChouBanerBean banerBean = new ZhongChouBanerBean();
                     String appIcon = data.getString("app_icon");
                     banerBean.setApp_icon(appIcon);
                   String messages = data.getString("app_name");
                   banerBean.setApp_name(messages);
                   String app_icon = data.getString("app_icon");
                   banerBean.setApp_icon(app_icon);
                   String city_code = data.getString("city_code");
                   banerBean.setCity_code(city_code);
                   int isentry = data.getInt("isentry");
                   banerBean.setIsentry(isentry);
                   int link_type = data.getInt("link_type");
                   banerBean.setLink_type(link_type);
                   zhongChouBanerBeans.add(banerBean);
               }
                 handler.sendEmptyMessage(1);
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    /*
    * 请求服务器失败
    * */
    public void onFailed(int reqcode, Object result) {
         LogUtil.debugPrint("onFailed reqcode "+ reqcode +" result " +result   );
    }

    @Override
    /*
    * 请求服务器异常
    * */
    public void onError(VolleyError volleyError) {
        LogUtil.debugPrint("onError reqcode "+ volleyError.getMessage()  );
    }

    /**
     * 请求新闻分类网络
     */
    private void requestNewsCategory() {

    }

    @Override
    /*
    当网络重新连接时调用
    * */
    public void isConntectedAndRefreshData() {

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


    }


    @Override
    public void onClick(ZhongChouBanerBean zhongChouBean) {

    }
}
