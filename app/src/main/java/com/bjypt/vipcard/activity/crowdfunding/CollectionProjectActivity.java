package com.bjypt.vipcard.activity.crowdfunding;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.CollectionProjectAdapter;
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
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshScrollView;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.RecyclerViewSpacesItemDecoration;

import org.codehaus.jackson.map.ObjectMapper;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjypt.vipcard.base.MyApplication.getContext;

public class CollectionProjectActivity  extends BaseActivity implements VolleyCallBack{

    private PullToRefreshScrollView Pull_seller_view;
    private RecyclerView recyclerView;

    private CollectionProjectAdapter adapter;
    //ArrayList<CfProjectItem> sellBeans;//集合数据
    private int pkmerchantid;
    private LoadMoreWrapper loadMoreWrapper;
    private EndlessRecyclerOnScrollListener onScrollListener;

    private ImageView iv_code_back;
    private String pkregister;
     List<CfProjectItem> SellerProjectBeans = new ArrayList<>();

    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private int page = 0;
    private int pageLength = 10;
    private boolean is_refresh;
    private TextView tv_sell_name;



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // adapter.notifyDataSetChanged();

            adapter.reFresh(SellerProjectBeans);

        }
    };


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_seller_project);
    }

    @Override
    public void beforeInitView() {
      //  sellBeans = new ArrayList<>();
        Intent intent = getIntent();
        pkregister = intent.getStringExtra("pkregister");
        pkmerchantid = intent.getIntExtra("pkmerchantid",0);
    }

    @Override
    public void initView() {
        tv_sell_name = findViewById(R.id.tv_sell_name);
        tv_sell_name.setText("收藏的项目");
        iv_code_back = findViewById(R.id.iv_code_back);
        Pull_seller_view = findViewById(R.id.Pull_seller_view);
        recyclerView = findViewById(R.id.rec_view);
        //设置间距
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION,5);//top间距
        recyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));
        //设置布局管理器
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
       //设置间距
        GridSpacingItemDecoration  gridSpacingItemDecoration = new GridSpacingItemDecoration(1, DensityUtil.dip2px(getContext(), 5));
        HorizontalSpaceItemDecoration horizontalSpaceItemDecoration = new HorizontalSpaceItemDecoration(DensityUtil.dip2px(getContext(), 5), DensityUtil.dip2px(getContext(), 8));
        recyclerView.removeItemDecoration(horizontalSpaceItemDecoration);
        recyclerView.addItemDecoration(gridSpacingItemDecoration);

        //设置适配器
        adapter = new CollectionProjectAdapter(this,SellerProjectBeans);
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
        maps.put("pkregister",getPkregister());
        maps.put("pageNum",page+"");
        maps.put("pageSize",pageLength+"");
        Wethod.httpPost(this,2, Config.web.h5_CFConsumeCollection,maps,this);
    }
    @Override
    public void afterInitView() {
        //下拉刷新
        Pull_seller_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getNetData(QUERY_EXERCISE_REFERSH);
                is_refresh = true;
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
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        CfProjectListDataBean cfProjectListDataBean = null;
        try {
            cfProjectListDataBean = objectMapper.readValue(result.toString(), CfProjectListDataBean.class);
            if(is_refresh){
             SellerProjectBeans.clear();
              is_refresh = false;
            }

         /* 根据调节隐藏某些Item   for (int i = 0; i < cfProjectListDataBean.getResultData().getList().size(); i++) {
               if (cfProjectListDataBean.getResultData().getList().get(i).getTypeImg() != 2){
                SellerProjectBeans.add(cfProjectListDataBean.getResultData().getList().get(i));
               }
            }*/
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
        Log.i("TAG,","连接成功 reqcode = ........onSuccess = "+ result);
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        Pull_seller_view.onRefreshComplete();// 停止刷新
        LogUtil.debugPrint("连接成功 reqcode = ........onFailed = "+ result);

        handler.sendEmptyMessage(1);
    }

    @Override
    public void onError(VolleyError volleyError) {
        Pull_seller_view.onRefreshComplete();// 停止刷新
      //  LogUtil.debugPrint("连接成功 reqcode = ........onError = "+ volleyError.getMessage());
        LogUtil.debugPrint("连接成功 reqcode = ........onError = "+ volleyError.getMessage());

        handler.sendEmptyMessage(1);
    }
}
