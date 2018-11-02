package com.bjypt.vipcard.fragment.crowdfunding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.CrowdfundingMainActivity;
import com.bjypt.vipcard.activity.crowdfunding.projectdetail.CrowdfundingDetailActivity;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;
import com.bjypt.vipcard.activity.shangfeng.util.ShangfengUriHelper;
import com.bjypt.vipcard.adapter.cf.HomeCrowdfundingRecommendAdapter;
import com.bjypt.vipcard.base.BaseFragment;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.fragment.crowdfunding.decoration.HorizontalSpaceItemDecoration;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfProjectItem;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfProjectItemNew;

import com.bjypt.vipcard.fragment.crowdfunding.entity.CfRecommentProjectItemDataBeanNew;
import com.bjypt.vipcard.fragment.crowdfunding.entity.CfTabData;
import com.bjypt.vipcard.listener.RecycleViewItemListener;
import com.bjypt.vipcard.model.cf.BannerBeanResultData;
import com.bjypt.vipcard.pulltorefresh.social.PullListLayout;
import com.bjypt.vipcard.pulltorefresh.social.custom.AppBarHeaderBehavior;
import com.bjypt.vipcard.pulltorefresh.social.custom.CustomCollapsingNoToolbarLayout;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.ZhongchouTextViewMult;
import com.bjypt.vipcard.view.categoryview.ZhongchouBannerView;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.flyco.tablayout.SlidingTabLayout;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrowdfundingFragment extends BaseFrament implements VolleyCallBack, ZhongchouTextViewMult.OnItemClickListener {

    static final int request_code_notices = 1;
    static final int request_code_recoment_project = 2;
    static final int request_code_normal_project = 3;

    View view;

    private PullListLayout pullList = null;
    private AppBarLayout appBar = null;
    //    private CustomCollapsingNoToolbarLayout toolBar = null;
//    private LinearLayout titleLayout = null;
    private SlidingTabLayout slidingTab = null;
    private ViewPager viewPager = null;

    private AppBarHeaderBehavior behavior;
    private HomeSubFragmentAdapter fragmentAdapter;

    SubCrowdfundingFragment[] subCrowdfundingFragmentArray;


    private ZhongchouBannerView bannerView;
    private ZhongchouTextViewMult tv_tuijian;
    private RecyclerView rv_recoment_projects;
    private HomeCrowdfundingRecommendAdapter homeCrowdfundingRecommendAdapter;
    private List<CfProjectItemNew> recommentProjectList;
    private RelativeLayout relate_notices;

    private ImageView iv_display_type;
    private ImageButton ibtn_back;

    private boolean is_recomment_scroll = false;
    private String pkregister;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_crowdfunding, container, false);
        return view;
    }

    @Override
    public void beforeInitView() {
        recommentProjectList = new ArrayList<>();
    }

    @Override
    public void initView() {
        View statusBarView = view.findViewById(R.id.statusBarView);
        ViewGroup.LayoutParams layoutParams = statusBarView.getLayoutParams();
        layoutParams.height = getStatusBarHeight();

        ibtn_back = view.findViewById(R.id.ibtn_back);
        pullList = view.findViewById(R.id.pullList);
        appBar = view.findViewById(R.id.appBar);
//        toolBar = view.findViewById(R.id.toolBar);
//        titleLayout = view.findViewById(R.id.titleLayout);
        slidingTab = view.findViewById(R.id.slidingTab);
        viewPager = view.findViewById(R.id.viewPager);

        bannerView = view.findViewById(R.id.zhongchouHomeBanner);
        tv_tuijian = view.findViewById(R.id.tv_hot);
        relate_notices = view.findViewById(R.id.relate_notices);
        rv_recoment_projects = view.findViewById(R.id.rv_recoment_projects);

        iv_display_type = view.findViewById(R.id.iv_display_type);

        pullList.setPtrHandler(ptrDefaultHandler);

        subCrowdfundingFragmentArray = new SubCrowdfundingFragment[4];
        for (int i = 0; i < subCrowdfundingFragmentArray.length; i++) {
            CfTabData cfTabData = new CfTabData();
            cfTabData.setTabType(CfTabData.TYPE.Sort.getValue());
            cfTabData.setTabValue(i + 1);
            cfTabData.setGridDisplay(false);
            subCrowdfundingFragmentArray[i] = SubCrowdfundingFragment.newInstance(cfTabData);
        }

        fragmentAdapter = new HomeSubFragmentAdapter(getChildFragmentManager(), subCrowdfundingFragmentArray);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(subCrowdfundingFragmentArray.length);

        slidingTab.setViewPager(viewPager);

        behavior = (AppBarHeaderBehavior) ((CoordinatorLayout.LayoutParams) appBar.getLayoutParams()).getBehavior();
        appBar.setExpanded(true, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_recoment_projects.setLayoutManager(layoutManager);
        homeCrowdfundingRecommendAdapter = new HomeCrowdfundingRecommendAdapter(getContext());
        homeCrowdfundingRecommendAdapter.setDatas(recommentProjectList);
        rv_recoment_projects.setAdapter(homeCrowdfundingRecommendAdapter);
        HorizontalSpaceItemDecoration horizontalSpaceItemDecoration = new HorizontalSpaceItemDecoration(DensityUtil.dip2px(getContext(), 5), 0);
        rv_recoment_projects.addItemDecoration(horizontalSpaceItemDecoration);

        rv_recoment_projects.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    is_recomment_scroll = false;
                } else {
                    is_recomment_scroll = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    @Override
    public void afterInitView() {


        bannerView.reload();

        getNotices();
        getRecommendData();
    }

    /**
     * 下拉刷新时刷新的数据
     */
    private void onDataRefresh() {
        bannerView.reload();
        getNotices();
        getRecommendData();

        for (int i = 0; i < subCrowdfundingFragmentArray.length; i++) {
            subCrowdfundingFragmentArray[i].refresh();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        CrowdfundingMainActivity cfActivity = (CrowdfundingMainActivity) context;
        pkregister = cfActivity.getPkregister();
    }

    /**
     * 今日推荐
     */
    private void getNotices() {
        Map<String, String> params = new HashMap<>();
        params.put("city_code", SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.web.cityCode, "1558"));
        params.put("app_type", "14");
        Wethod.httpPost(getActivity(), request_code_notices, Config.web.MY_WALLET, params, this, View.GONE);
    }

    /**
     * 获取项目列表
     */
    private void getNormalProjectList() {

    }


    /**
     * 推荐项目
     */
    public void getRecommendData() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister",pkregister);
        Wethod.httpPost(getActivity(), request_code_recoment_project, Config.web.zhongchou_recomend_url, new HashMap<String, String>(), this, View.GONE);
    }

    @Override
    public void bindListener() {
        tv_tuijian.setOnItemClickListener(this);

        iv_display_type.setOnClickListener(this);
        ibtn_back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.iv_display_type:
               // displayProjectAdapter();//暂停切换
                break;
            case R.id.ibtn_back:
                getActivity().finish();
                break;
        }
    }


    @Override
    public void onSuccess(int reqcode, Object result) {
        pullList.refreshComplete();
        if (reqcode == request_code_notices) {
            handlerNotices(result);
        } else if (reqcode == request_code_recoment_project) {
            LogUtil.debugPrint("CrowfunFragmentRecomment : " + result);
            handlerRecommentProject(result);
        } else if (reqcode == request_code_normal_project) {

        }

    }



    private void handlerRecommentProject(Object result) {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            CfRecommentProjectItemDataBeanNew cfRecommentProjectItemDataBean = objectMapper.readValue(result.toString(), CfRecommentProjectItemDataBeanNew.class);
            homeCrowdfundingRecommendAdapter.getDatas().clear();
            if (cfRecommentProjectItemDataBean == null || cfRecommentProjectItemDataBean.getResultData() == null || cfRecommentProjectItemDataBean.getResultData().isEmpty()) {
                relate_notices.setVisibility(View.GONE);
            } else {
                relate_notices.setVisibility(View.VISIBLE);
                homeCrowdfundingRecommendAdapter.getDatas().addAll(cfRecommentProjectItemDataBean.getResultData());
            }
            homeCrowdfundingRecommendAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlerNotices(Object result) {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            BannerBeanResultData bannerResultdataBean = objectMapper.readValue(result.toString(), BannerBeanResultData.class);
            tv_tuijian.setTexts(bannerResultdataBean.getResultData(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if(reqcode == request_code_recoment_project || reqcode == request_code_notices){
            pullList.refreshComplete();
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
    }
    private void displayProjectAdapter() {
        if (iv_display_type.isSelected()) {
            iv_display_type.setSelected(false);
        } else {
            iv_display_type.setSelected(true);
        }
        for (int i = 0; i < subCrowdfundingFragmentArray.length; i++) {
            subCrowdfundingFragmentArray[i].setDisplayType(iv_display_type.isSelected());
        }
    }

    /**
     * 下拉刷新监听
     */
    private PtrDefaultHandler ptrDefaultHandler = new PtrDefaultHandler() {

        @Override
        public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
            return !is_recomment_scroll && behavior.getCurrentVerticalOffset() == 0 && super.checkCanDoRefresh(frame, content, header);
//            return false;
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            onDataRefresh();
        }
    };

    /**
     * ViewPager 的适配器
     */
    private static class HomeSubFragmentAdapter extends FragmentStatePagerAdapter {

        private BaseFragment[] fragment = null;
        private String[] titles = null;

        public HomeSubFragmentAdapter(FragmentManager fm, BaseFragment... fragment) {
            super(fm);
            this.fragment = fragment;
            this.titles = new String[]{"综合推荐", "即将结束", "金额最大", "份数最多"};
        }

        @Override
        public Fragment getItem(int position) {
            return fragment[position];
        }

        @Override
        public int getCount() {
            return fragment.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }


    }

    @Override
    public void onNoticeClick(BannerBean zhongChouBean) {
        ShangfengUriHelper helper = new ShangfengUriHelper(getContext());
        helper.onAppCategoryItemClick(zhongChouBean);
    }
}
