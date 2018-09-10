package com.bjypt.vipcard.fragment.social;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseFragment;
import com.bjypt.vipcard.pulltorefresh.social.PullListLayout;
import com.bjypt.vipcard.pulltorefresh.social.custom.AppBarHeaderBehavior;
import com.bjypt.vipcard.pulltorefresh.social.custom.CustomCollapsingToolbarLayout;
import com.flyco.tablayout.SlidingTabLayout;

public class SocialTalkAboutFragment extends BaseFragment {

    private View view;
    public static final String TAG = "HomeFragment";

    private PullListLayout pullList = null;
    private AppBarLayout appBar = null;
    private CustomCollapsingToolbarLayout toolBar = null;
    private LinearLayout titleLayout = null;
    private SlidingTabLayout slidingTab = null;
    private ViewPager viewPager = null;

    private AppBarHeaderBehavior behavior;

    private SocialTalkAboutSubFragment homeSubFragment1;
    private SocialTalkAboutSubFragment homeSubFragment2;
//    private HomeSubFragmentAdapter fragmentAdapter;

    private View adList = null;
    private RecyclerView horizontalList = null;
    private RecyclerView grid3x3List = null;
    private RecyclerView grid5x2List = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_social_talkabout, container, false);
        return view;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }
}
