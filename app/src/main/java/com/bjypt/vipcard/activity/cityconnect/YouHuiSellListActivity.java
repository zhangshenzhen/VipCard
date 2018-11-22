package com.bjypt.vipcard.activity.cityconnect;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;

import com.bjypt.vipcard.adapter.cf.listener.EndlessRecyclerOnScrollListener;
import com.bjypt.vipcard.adapter.cf.listener.LoadMoreWrapper;
import com.bjypt.vipcard.adapter.cityconnect.YouHuiSellerAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.fragment.crowdfunding.decoration.GridSpacingItemDecoration;
import com.bjypt.vipcard.fragment.crowdfunding.decoration.HorizontalSpaceItemDecoration;
import com.bjypt.vipcard.model.NewHomeMerchantListBean;
import com.bjypt.vipcard.model.NewMerchantListBean;

import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshScrollView;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.RecyclerViewSpacesItemDecoration;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjypt.vipcard.base.MyApplication.getContext;

public class YouHuiSellListActivity extends BaseActivity implements VolleyCallBack {

    private LinearLayout liner_back;
    private String pkcoupon;
    private String lat;
    private String lng;
    private int begin;
    private int pageLength = 10;
    private int request_sell_code = 18;
    private List<NewMerchantListBean>   sellerList = new ArrayList<>();
    private RecyclerView recycleView;
    private YouHuiSellerAdapter sellerAdapter;
    private PullToRefreshScrollView Pull_seller_view;
    private LoadMoreWrapper loadMoreWrapper;
    private EndlessRecyclerOnScrollListener onScrollListener;
    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private boolean is_refresh;
    private ImageView img_no_list;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_seller_list);
    }

    @Override
    public void beforeInitView() {
        pkcoupon = getIntent().getStringExtra("pkcoupon");
        lat = SharedPreferenceUtils.getFromSharedPreference(YouHuiSellListActivity.this, Config.userConfig.CURRENT_LATU);
        lng = SharedPreferenceUtils.getFromSharedPreference(YouHuiSellListActivity.this, Config.userConfig.CURRENT_LNGU);
        Log.e("lat", "YouHuiSellListActivity...>: " + lat+"    :  "+lng);

    }

    @Override
    public void initView() {
        liner_back = findViewById(R.id.liner_back);
        Pull_seller_view = findViewById(R.id.Pull_seller_view);
        recycleView = findViewById(R.id.recycleView);
        img_no_list = findViewById(R.id.img_no_list);

        //设置间距
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION,5);//top间距
        recycleView.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));
        //设置布局管理器
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        //设置间距
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(1, DensityUtil.dip2px(getContext(), 5));
        HorizontalSpaceItemDecoration horizontalSpaceItemDecoration = new HorizontalSpaceItemDecoration(DensityUtil.dip2px(getContext(), 5), DensityUtil.dip2px(getContext(), 8));
        recycleView.removeItemDecoration(horizontalSpaceItemDecoration);
        recycleView.addItemDecoration(gridSpacingItemDecoration);

        //适配器
        sellerAdapter = new YouHuiSellerAdapter(this,sellerList);
        loadMoreWrapper = new LoadMoreWrapper(sellerAdapter);
        recycleView.setAdapter(loadMoreWrapper);

        //设置下拉刷新
        Pull_seller_view.getLoadingLayoutProxy().setLastUpdatedLabel("lastUpdateLabel");
        Pull_seller_view.getLoadingLayoutProxy().setPullLabel("PULLLABLE");
        Pull_seller_view.getLoadingLayoutProxy().setRefreshingLabel("refreshingLabel");
        Pull_seller_view.getLoadingLayoutProxy().setReleaseLabel("releaseLabel");
        //上拉、下拉设定
        Pull_seller_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        initScrollListener();//监听
    }
    private void initScrollListener() {
        if (onScrollListener != null)
            recycleView.removeOnScrollListener(onScrollListener);

        onScrollListener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
                getMacherListData(QUERY_EXERCISE_MORE);
            }
        };
        // 设置加载更多监听
        recycleView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void afterInitView() {
        getMacherListData(QUERY_EXERCISE_REFERSH);

        //下拉刷新
        Pull_seller_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getMacherListData(QUERY_EXERCISE_REFERSH);
                is_refresh = true;
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

        });
    }
   private void getMacherListData(int refresh_type ){
       if (refresh_type == QUERY_EXERCISE_REFERSH) {
           begin = 0;
           is_refresh = true;
       } else {
           begin += 1;
           is_refresh = false;
       }

        Map<String ,String> parmaps = new HashMap<>();
        parmaps.put("pkcoupon",pkcoupon);
        parmaps.put("lat",lat);
        parmaps.put("lng",lng);
        parmaps.put("cityCode", Config.web.cityCode);
        parmaps.put("begin",begin+"");
        parmaps.put("pageLength",pageLength+"");
        Wethod.httpPost(this, request_sell_code, Config.web.city_connectin_YouHui_sell, parmaps, this, View.GONE);

   }

   private Handler handler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           if(sellerAdapter != null){
               sellerAdapter.reFresh(sellerList);
           }
       }
   };
    @Override
    public void bindListener() {
        liner_back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.liner_back:
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        Pull_seller_view.onRefreshComplete();// 停止刷新
        if(reqcode == request_sell_code){
            LogUtil.debugPrint("优惠券商家列表onSuccess = " + result);
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                NewHomeMerchantListBean  merchantListBean = objectMapper.readValue(result.toString(), NewHomeMerchantListBean.class);

                 if(is_refresh){
                    sellerList.clear();
                  is_refresh = false;
                }

                if(merchantListBean == null){
                    img_no_list.setVisibility(View.VISIBLE);
                    recycleView.setVisibility(View.GONE);
                    return;
                }
                sellerList.addAll(merchantListBean.getResultData());
                 if(merchantListBean.getResultData().size()<pageLength){
                     loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                     recycleView.removeOnScrollListener(onScrollListener);
                 }else {
                     loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                 }

                    /* if (merchantListBean != null && merchantListBean.getResultData().size() >0){
                         for (int i = 0; i < merchantListBean.getResultData().size(); i++) {
                             sellerList.add(merchantListBean.getResultData().get(i));
                         }
                     }*/
                if (sellerList.size() > 0) {
                    handler.sendEmptyMessage(1);//通知刷新

                } else {//背景显示没有优惠券的图标
                    img_no_list.setVisibility(View.VISIBLE);
                    recycleView.setVisibility(View.GONE);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFailed(int reqcode, Object result) {
        Pull_seller_view.onRefreshComplete();// 停止刷新
        LogUtil.debugPrint("onFailed = " + result);
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
