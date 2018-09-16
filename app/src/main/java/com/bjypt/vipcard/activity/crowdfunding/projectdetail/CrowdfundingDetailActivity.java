package com.bjypt.vipcard.activity.crowdfunding.projectdetail;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.crowdfunding.SupportInfoActivity;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.BaseFraActivity;
import com.bjypt.vipcard.base.BaseFragment;
import com.bjypt.vipcard.fragment.MineFragment;
import com.bjypt.vipcard.fragment.crowdfunding.CrowdfundingFragment;
import com.bjypt.vipcard.pulltorefresh.social.custom.AppBarHeaderBehavior;
import com.flyco.tablayout.SlidingTabLayout;
import com.githang.statusbar.StatusBarCompat;

public class CrowdfundingDetailActivity extends BaseFraActivity {

    private SlidingTabLayout slidingTab = null;
    private ViewPager viewPager = null;

    private AppBarLayout appBar = null;
    private AppBarHeaderBehavior behavior;
    private HomeSubFragmentAdapter fragmentAdapter;
    BaseFragment[] projectContentFragments;

    Button btn_topay;

    @Override
    public void setContentLayout() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.cf_account_detail_head));
        setContentView(R.layout.activity_crowdfunding_detail);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        slidingTab = findViewById(R.id.slidingTab);
        viewPager = findViewById(R.id.viewPager);
        appBar = findViewById(R.id.appBar);
        behavior = (AppBarHeaderBehavior) ((CoordinatorLayout.LayoutParams) appBar.getLayoutParams()).getBehavior();
        appBar.setExpanded(true, false);

        btn_topay = findViewById(R.id.btn_topay);
    }

    @Override
    public void afterInitView() {
        projectContentFragments = new ProjectContentFragment[4];
//        for (int i=0;i< projectContentFragments.length;i++){
//
//        }
        projectContentFragments[0] =  ProjectContentFragment.newInstance("http://baidu.com");
        projectContentFragments[1] =  ProjectContentFragment.newInstance("http://jd.com");
        projectContentFragments[2] =  ProjectContentFragment.newInstance("http://qq.com");
        projectContentFragments[3] =  ProjectContentFragment.newInstance("http://sina.com");
        fragmentAdapter = new HomeSubFragmentAdapter(getSupportFragmentManager(), projectContentFragments);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(projectContentFragments.length);

        slidingTab.setViewPager(viewPager);
    }

    @Override
    public void bindListener() {
        btn_topay.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
          case  R.id.btn_topay:
              Intent topay =  new Intent(this, SupportInfoActivity.class);
              topay.putExtra("pkprogressitemid", 2);
              startActivity(topay);
            break;
        }
    }

    /**
     * ViewPager 的适配器
     */
    private static class HomeSubFragmentAdapter extends FragmentStatePagerAdapter {
        private BaseFragment[] fragment = null;
        private String[] titles = null;
        public HomeSubFragmentAdapter(FragmentManager fm, BaseFragment... fragment) {
            super(fm);
            this.fragment = fragment;
            this.titles = new String[]{"产品介绍", "团队介绍", "常见问题", "项目进展"};
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
}
