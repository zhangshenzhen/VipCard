package com.bjypt.vipcard.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseFragment;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.NewsFuYangTab;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshScrollView;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.NewsViewpager;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment implements VolleyCallBack<String> {

    private static final int NEWS_CATEGORY = 20180326;

    private View view;
    //    private ScrollView ptrs_news;
    private SlidingTabLayout stl_news;
    private NewsViewpager vp_news;
    private MyPagerAdapter myPagerAdapter;

    private ArrayList<HomeNewsFragment> mFragments = new ArrayList<HomeNewsFragment>();
    private String[] listName;
    private List<String> tabList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        return view;
    }

    @Override
    public void beforeInitView() {
        View statusBar = view.findViewById(R.id.statusBarView);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
    }

    @Override
    public void initView() {
//        ptrs_news = (ScrollView) view.findViewById(R.id.ptrs_news);
        stl_news = (SlidingTabLayout) view.findViewById(R.id.stl_news);
        vp_news = (NewsViewpager) view.findViewById(R.id.vp_news);

    }

    @Override
    public void afterInitView() {
        requestNewsCategory();
    }

    @Override
    public void bindListener() {
        vp_news.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                vp_news.resetHeight(position);
            }

            @Override
            public void onPageSelected(int position) {
                vp_news.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == NEWS_CATEGORY) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            NewsFuYangTab newsFuYangTab = null;
            try {
                newsFuYangTab = objectMapper.readValue(result.toString(), NewsFuYangTab.class);
                if (null != newsFuYangTab.getResultData() && !("").equals(newsFuYangTab.getResultData())) {
                    for (int i = 0; i < newsFuYangTab.getResultData().size(); i++) {
                        tabList.add(newsFuYangTab.getResultData().get(i).getCatname());
                        mFragments.add(HomeNewsFragment.getInstance(newsFuYangTab.getResultData().get(i).getCatname(), newsFuYangTab.getResultData().get(i).getCatid(), vp_news, i));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            listName = tabList.toArray(new String[tabList.size()]);

            initNewsData();
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

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
            return listName[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

    }

    /**
     * 初始化新闻数据
     */
    private void initNewsData() {
        if (isAdded()) {
            myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
            vp_news.setAdapter(myPagerAdapter);
            vp_news.setOffscreenPageLimit(tabList.size());
            stl_news.setViewPager(vp_news);
            stl_news.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    mFragments.get(position).onRefresh();
                }

                @Override
                public void onTabReselect(int position) {
                    mFragments.get(position).onRefresh();
                }
            });
        }
    }

    /**
     * 请求新闻分类网络
     */
    private void requestNewsCategory() {
        Wethod.httpPost(getActivity(), NEWS_CATEGORY, Config.web.fyzx_news_category, null, this, View.GONE);
    }
}
