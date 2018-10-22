package com.bjypt.vipcard.activity.crowdfunding;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.android.volley.VolleyError;

import com.bjypt.vipcard.R;

import com.bjypt.vipcard.adapter.SellerProjectAdapter;
import com.bjypt.vipcard.adapter.cf.listener.EndlessRecyclerOnScrollListener;
import com.bjypt.vipcard.adapter.cf.listener.LoadMoreWrapper;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.fragment.crowdfunding.decoration.GridSpacingItemDecoration;
import com.bjypt.vipcard.fragment.crowdfunding.decoration.HorizontalSpaceItemDecoration;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfProjectItem;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfProjectListDataBean;
import com.bjypt.vipcard.model.cf.CfProjectDetailItemDataBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshScrollView;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.RecyclerViewSpacesItemDecoration;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjypt.vipcard.base.MyApplication.getContext;

public class CrowdfundingSellerProjectActivity extends BaseActivity implements VolleyCallBack{

    private PullToRefreshScrollView Pull_seller_view;
    private RecyclerView recyclerView;
    private LoadMoreWrapper loadMoreWrapper;
    private EndlessRecyclerOnScrollListener onScrollListener;
    private SellerProjectAdapter adapter;
   // ArrayList<SellerProjectBean.SellBean> sellBeans = new ArrayList<>();
    private int pkmerchantid;
    List<CfProjectItem> SellerProjectBeans = new ArrayList<>();
    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private int page = 0;
    private int pageLength = 10;
    private boolean is_refresh;
    private ImageView iv_code_back;



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                adapter.reFresh(SellerProjectBeans);

         }
    };

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_seller_project);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        pkmerchantid = intent.getIntExtra("pkmerchantid",0);
       // getNetData();//联网获取数据
    }

    @Override
    public void initView() {
        iv_code_back = findViewById(R.id.iv_code_back);
        Pull_seller_view = findViewById(R.id.Pull_seller_view);

        recyclerView = findViewById(R.id.rec_view);
        //设置间距
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION,10);//top间距
        recyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));
        //设置布局管理器
        LinearLayoutManager layoutManager=new LinearLayoutManager(CrowdfundingSellerProjectActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        //设置间距
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(1, DensityUtil.dip2px(getContext(), 5));
        HorizontalSpaceItemDecoration horizontalSpaceItemDecoration = new HorizontalSpaceItemDecoration(DensityUtil.dip2px(getContext(), 5), DensityUtil.dip2px(getContext(), 8));
        recyclerView.removeItemDecoration(horizontalSpaceItemDecoration);
        recyclerView.addItemDecoration(gridSpacingItemDecoration);

        //设置适配器
        adapter = new SellerProjectAdapter(CrowdfundingSellerProjectActivity.this,SellerProjectBeans);
        loadMoreWrapper = new LoadMoreWrapper(adapter);
        recyclerView.setAdapter(loadMoreWrapper);


        //设置下拉刷新
        Pull_seller_view.getLoadingLayoutProxy().setLastUpdatedLabel("lastUpdateLabel");
        Pull_seller_view.getLoadingLayoutProxy().setPullLabel("PULLLABLE");
        Pull_seller_view.getLoadingLayoutProxy().setRefreshingLabel("refreshingLabel");
        Pull_seller_view.getLoadingLayoutProxy().setReleaseLabel("releaseLabel");
        //上拉、下拉设定
        Pull_seller_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        iv_code_back.setOnClickListener(this);

        getNetData(QUERY_EXERCISE_REFERSH);//联网获取数据
        initScrollListener();
    }

    private void initScrollListener() {
        if (onScrollListener != null)
            recyclerView.removeOnScrollListener(onScrollListener);

          onScrollListener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
                getNetData(QUERY_EXERCISE_MORE);
            }
        };
        // 设置加载更多监听
      recyclerView.addOnScrollListener(onScrollListener);
    }



    public void getNetData(int refresh_type) {
        if (refresh_type == QUERY_EXERCISE_REFERSH) {
            page = 1;
            is_refresh = true;
        } else {
            page += 1;
            is_refresh = false;
        }

        Map<String,String> maps = new HashMap<>();
        maps.put("pkmerchantid",pkmerchantid+"");
        maps.put("pkregister",getPkregister());
        maps.put("pageNum",page+"");
        maps.put("pageSize",pageLength+"");
        //String url = "http://123.57.232.188:19096/api/hybCfProject/getProjectByMerchantId";
        Wethod.httpPost(this,2, Config.web.Seller_project_url,maps,this);
    }
    @Override
    public void afterInitView() {
        //下拉刷新
        Pull_seller_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getNetData(QUERY_EXERCISE_REFERSH);
                is_refresh   = true;
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

        });
    }

    @Override
    public void bindListener() {

     }

    @Override
    public void onClickEvent(View v) {

        switch (v.getId()){
            case R.id.iv_code_back:
                finish();
                break;
        }

    }


    private void jsonData( Object result) {
        try {

              ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
              CfProjectListDataBean cfProjectListDataBean = objectMapper.readValue(result.toString(), CfProjectListDataBean.class);
              if(is_refresh){
                 SellerProjectBeans.clear();
                 is_refresh = false;
              }

              SellerProjectBeans.addAll(cfProjectListDataBean.getResultData().getList());

              if(cfProjectListDataBean.getResultData().getList().size()< pageLength){
                  loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                  recyclerView.removeOnScrollListener(onScrollListener);
              }else {
                  loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                 // loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
              }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onSuccess(int reqcode, Object result) {
        Pull_seller_view.onRefreshComplete();// 停止刷新
        jsonData(result);
        LogUtil.debugPrint("连接成功 reqcode = ........onSuccess = "+ result);
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        Pull_seller_view.onRefreshComplete();// 停止刷新
        LogUtil.debugPrint("连接成功 reqcode = ........onFailed = "+ result);
        // adapter.notifyDataSetChanged();
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onError(VolleyError volleyError) {
        Pull_seller_view.onRefreshComplete();// 停止刷新
        LogUtil.debugPrint("连接成功 reqcode = ........onError = "+ volleyError.getMessage());
        //adapter.notifyDataSetChanged();
        handler.sendEmptyMessage(1);
    }

}
