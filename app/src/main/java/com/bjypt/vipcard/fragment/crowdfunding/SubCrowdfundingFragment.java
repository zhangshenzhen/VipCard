package com.bjypt.vipcard.fragment.crowdfunding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.cf.listener.EndlessRecyclerOnScrollListener;
import com.bjypt.vipcard.adapter.cf.HomeCrowdfundingGridProjectAdapter;
import com.bjypt.vipcard.adapter.cf.HomeCrowdfundingListProjectAdapter;
import com.bjypt.vipcard.adapter.cf.listener.LoadMoreWrapper;
import com.bjypt.vipcard.base.BaseFragment;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.fragment.crowdfunding.decoration.GridSpacingItemDecoration;
import com.bjypt.vipcard.fragment.crowdfunding.decoration.HorizontalSpaceItemDecoration;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfProjectItem;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfProjectListDataBean;

import com.bjypt.vipcard.fragment.crowdfunding.entity.CfTabData;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SubCrowdfundingFragment extends BaseFragment implements VolleyCallBack {

    static final int request_code_project_list = 1001;

    View view;

    private RecyclerView recyclerView = null;
    private HomeCrowdfundingGridProjectAdapter homeCrowdfundingGridProjectAdapter;
    private HomeCrowdfundingListProjectAdapter homeCrowdfundingListProjectAdapter;
    private GridSpacingItemDecoration gridSpacingItemDecoration;
    private HorizontalSpaceItemDecoration horizontalSpaceItemDecoration;
    private LoadMoreWrapper loadMoreWrapper;

    List<CfProjectItem> projectList = new ArrayList<>();
    CfTabData cfTabData;


    final int QUERY_EXERCISE_MORE = 0x0101;
    final int QUERY_EXERCISE_REFERSH = 0x0110;
    private int page = 0;
    private int pageLength = 10;
    private boolean is_refresh = true;

    private EndlessRecyclerOnScrollListener onScrollListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cfTabData = (CfTabData) getArguments().getSerializable("tabData");
        view = inflater.inflate(R.layout.fragment_crowdfunding_sub, container, false);
        return view;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        homeCrowdfundingGridProjectAdapter = new HomeCrowdfundingGridProjectAdapter(mActivity);
        homeCrowdfundingGridProjectAdapter.setDatas(projectList);

        homeCrowdfundingListProjectAdapter = new HomeCrowdfundingListProjectAdapter(mActivity);
        homeCrowdfundingListProjectAdapter.setDatas(projectList);
        gridSpacingItemDecoration = new GridSpacingItemDecoration(1, DensityUtil.dip2px(getContext(), 5));
        horizontalSpaceItemDecoration = new HorizontalSpaceItemDecoration(DensityUtil.dip2px(getContext(), 5), DensityUtil.dip2px(getContext(), 10));

       // listDisplayType();
       gridDisplayType();
    }

    private void changeAdapter(boolean isGrid) {
        if (isGrid) {
            gridDisplayType();
        } else {
            listDisplayType();
        }
    }

    public void refresh() {
        if (cfTabData != null) {
            projectList.clear();
            if (homeCrowdfundingGridProjectAdapter != null && homeCrowdfundingGridProjectAdapter.getDatas() != null) {
                homeCrowdfundingGridProjectAdapter.getDatas().clear();
            }
            if (homeCrowdfundingListProjectAdapter != null && homeCrowdfundingListProjectAdapter.getDatas() != null) {
                homeCrowdfundingListProjectAdapter.getDatas().clear();
            }

            loadDatas(QUERY_EXERCISE_REFERSH);
        }
    }

    /**
     * 网格的显示设置
     */
    private void gridDisplayType() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);//网格列变为1
        recyclerView.setLayoutManager(gridLayoutManager);
        GridLayoutAnimationController controller = new GridLayoutAnimationController(getContext(), null);
        controller.setColumnDelay(0.15f);
        controller.setDirection(GridLayoutAnimationController.DIRECTION_TOP_TO_BOTTOM | GridLayoutAnimationController.DIRECTION_LEFT_TO_RIGHT);
        controller.setRowDelay(0.15f);
        controller.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.item_animation_from_bottom));

        recyclerView.removeItemDecoration(horizontalSpaceItemDecoration);
        recyclerView.addItemDecoration(gridSpacingItemDecoration);

        loadMoreWrapper = new LoadMoreWrapper(homeCrowdfundingGridProjectAdapter);
        recyclerView.setAdapter(loadMoreWrapper);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();

        initScrollListener();
    }

    /**
     * 列表的显示设置
     */
    private void listDisplayType() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.removeItemDecoration(gridSpacingItemDecoration);
        recyclerView.addItemDecoration(horizontalSpaceItemDecoration);

        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_right);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutAnimation(controller);
        loadMoreWrapper = new LoadMoreWrapper(homeCrowdfundingListProjectAdapter);
        recyclerView.setAdapter(loadMoreWrapper);
        recyclerView.scheduleLayoutAnimation();

        initScrollListener();
    }

    private void initScrollListener() {
        if (onScrollListener != null)
            recyclerView.removeOnScrollListener(onScrollListener);

        onScrollListener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
                loadDatas(QUERY_EXERCISE_MORE);
            }
        };
        // 设置加载更多监听
        recyclerView.addOnScrollListener(onScrollListener);
    }


    /**
     * 显示方式， true grid   false list
     *
     * @param displayType
     */
    public void setDisplayType(boolean displayType) {
        cfTabData.setGridDisplay(displayType);
        changeAdapter(cfTabData.getGridDisplay());
    }


    @Override
    public void afterInitView() {
        loadDatas(QUERY_EXERCISE_REFERSH);
    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        LogUtil.debugPrint("request_code_project_list  = "+result);
        if (reqcode == request_code_project_list) {
            try {
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                CfProjectListDataBean cfProjectListDataBean = objectMapper.readValue(result.toString(), CfProjectListDataBean.class);
                if (is_refresh) { // 下拉刷新
                    projectList.clear();
                }
                projectList.addAll(cfProjectListDataBean.getResultData().getList());

                if (cfTabData.getGridDisplay()) {
                    homeCrowdfundingGridProjectAdapter.notifyDataSetChanged();
                } else {
                    homeCrowdfundingListProjectAdapter.notifyDataSetChanged();
                }
                if (cfProjectListDataBean.getResultData().getList().size() < pageLength) {
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    recyclerView.removeOnScrollListener(onScrollListener);
                } else {
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    private void loadDatas(int refresh_type) {
        Map<String, String> params = new HashMap<>();
        if (refresh_type == QUERY_EXERCISE_REFERSH) {
            page = 1;
            is_refresh = true;
        } else {
            page += 1;
            is_refresh = false;
        }
        params.put("pkregister", getPkregister());
        params.put("pageNum", page + "");
        params.put("pageSize", pageLength + "");
        params.put("sortType", cfTabData.getTabValue() + "");
        Wethod.httpPost(getContext(), request_code_project_list, Config.web.zhongchou_list_url, params, this, View.GONE);
    }

    public static SubCrowdfundingFragment newInstance(CfTabData cfTabData) {

        Bundle args = new Bundle();
        args.putSerializable("tabData", cfTabData);
        SubCrowdfundingFragment fragment = new SubCrowdfundingFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
