package com.bjypt.vipcard.activity.cityconnect;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;

import com.bjypt.vipcard.adapter.cf.listener.EndlessRecyclerOnScrollListener;
import com.bjypt.vipcard.adapter.cf.listener.LoadMoreWrapper;
import com.bjypt.vipcard.adapter.cityconnect.YouHuiQuanListAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;

import com.bjypt.vipcard.bean.YouHuiquanListBean;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.fragment.crowdfunding.decoration.GridSpacingItemDecoration;
import com.bjypt.vipcard.fragment.crowdfunding.decoration.HorizontalSpaceItemDecoration;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshScrollView;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.RecyclerViewSpacesItemDecoration;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import A.V;

public class YouHuiQuanListActivity extends BaseActivity implements VolleyCallBack {


    private RecyclerView youhuilist_recv;
    private LinearLayout liner_back;
    private List<YouHuiquanListBean.ResultDataBean> ResultDataBeanlist;
    private int request_YouHuiList_code = 15;
    private YouHuiquanListBean listBean;
    private YouHuiQuanListAdapter listAdapter;

    private LoadMoreWrapper loadMoreWrapper;
    private EndlessRecyclerOnScrollListener onScrollListener;
    private ImageView img_no_list;
    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private int page = 0;
    private int pageLength = 10;
    private boolean is_refresh;
    private PullToRefreshScrollView pull_list_view;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_youhuiquanlist);
    }

    @Override
    public void beforeInitView() {
        ResultDataBeanlist = new ArrayList<>();
        // youHuiQuanDataBeanlist = (List<YouHuiQuanBean.YouHuiQuanDataBean>) getIntent().getSerializableExtra("youHuiQuanDataBeanlist");

    }

    @Override
    public void initView() {
        youhuilist_recv = findViewById(R.id.youhuilist_recv);
        pull_list_view = findViewById(R.id.Pull_list_view);
        liner_back = findViewById(R.id.liner_back);
        img_no_list = findViewById(R.id.img_no_list);
        img_no_list.setVisibility(View.GONE);


    }

    @Override
    public void afterInitView() {
        getYouHuiQuanList(QUERY_EXERCISE_REFERSH);//刷新
        //下拉刷新
        pull_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getYouHuiQuanList(QUERY_EXERCISE_REFERSH);
                is_refresh = true;
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

        });
    }

    /*
    * 请求数据*/

    private void getYouHuiQuanList(int refresh_type) {
        if(refresh_type ==QUERY_EXERCISE_REFERSH){
            page = 1;
            is_refresh = true;
        }else {
            page += 1;
            is_refresh = false;
        }
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getPkregister());
        params.put("begin", page+"");
        params.put("pageLength", 10 + "");//
        params.put("sortType", "");//
        Wethod.httpPost(this, request_YouHuiList_code, Config.web.city_connectin_youhui_list, params, this, View.GONE);

    }

    @Override
    public void bindListener() {
        liner_back.setOnClickListener(this);
        //设置间距
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION,5);//top间距
        youhuilist_recv.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        youhuilist_recv.setLayoutManager(layoutManager);

        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(1, DensityUtil.dip2px(this, 8));//5
        HorizontalSpaceItemDecoration horizontalSpaceItemDecoration = new HorizontalSpaceItemDecoration(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 0));
        youhuilist_recv.removeItemDecoration(horizontalSpaceItemDecoration);
        youhuilist_recv.addItemDecoration(gridSpacingItemDecoration);

        listAdapter = new YouHuiQuanListAdapter(this, ResultDataBeanlist);
        loadMoreWrapper = new LoadMoreWrapper(listAdapter);
        youhuilist_recv.setAdapter(loadMoreWrapper);

        //设置下拉刷新
        pull_list_view.getLoadingLayoutProxy().setLastUpdatedLabel("lastUpdateLabel");
        pull_list_view.getLoadingLayoutProxy().setPullLabel("PULLLABLE");
        pull_list_view.getLoadingLayoutProxy().setRefreshingLabel("refreshingLabel");
        pull_list_view.getLoadingLayoutProxy().setReleaseLabel("releaseLabel");
        //上拉、下拉设定
        pull_list_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        initScrollListener();
     }
    private void initScrollListener() {
        if (onScrollListener != null)
            youhuilist_recv.removeOnScrollListener(onScrollListener);

        onScrollListener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
                getYouHuiQuanList(QUERY_EXERCISE_MORE);
            }
        };
        // 设置加载更多监听
         youhuilist_recv.addOnScrollListener(onScrollListener);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.liner_back://返回
                finish();
                break;
        }
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(listAdapter != null){
                listAdapter.reFresh(ResultDataBeanlist);
            }
        }
    };

    @Override
    public void onSuccess(int reqcode, Object result) {
        pull_list_view.onRefreshComplete();// 停止刷新
        if (reqcode == request_YouHuiList_code) {
            LogUtil.debugPrint("优惠券列表信息 : " + result);
            try {
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                listBean = objectMapper.readValue(result.toString(), YouHuiquanListBean.class);

                if(is_refresh){ //判断是刷新还是加载更多
                    ResultDataBeanlist.clear();
                    is_refresh = false;
                }
                if(listBean == null){
                    img_no_list.setVisibility(View.VISIBLE);
                    youhuilist_recv.setVisibility(View.GONE);
                    return;
                }
                ResultDataBeanlist.addAll(listBean.getResultData());//
                 if(listBean.getResultData().size()<pageLength){
                     loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                     youhuilist_recv.removeOnScrollListener(onScrollListener);
                 }else {
                     loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                 }

                if (ResultDataBeanlist.size() > 0) {
                  Message msg = Message.obtain();
                  msg.what = 1;
                  handler.sendEmptyMessage(1);//通知刷新

                } else {//背景显示没有优惠券的图标
                    img_no_list.setVisibility(View.VISIBLE);
                    youhuilist_recv.setVisibility(View.GONE);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onFailed(int reqcode, Object result) {
        pull_list_view.onRefreshComplete();// 停止刷新
    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
