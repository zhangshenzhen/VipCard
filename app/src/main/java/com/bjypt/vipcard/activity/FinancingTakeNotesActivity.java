package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseFraActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.fragment.FinancingTakeNotesFragment;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * Created by 崔龙 on 2018/1/4.
 * 理财记录页面
 */

public class FinancingTakeNotesActivity extends BaseFraActivity implements VolleyCallBack {
    private SlidingTabLayout take_notes_tab;
    private RelativeLayout rl_back;
    private ViewPager vp;
    private String pkmuser;
    private ArrayList<FinancingTakeNotesFragment> mFragments = new ArrayList<>();
    private String[] listName = new String[]{"进行中", "已结束"};    // tap名字
    private String pageFlag;
    private String categoryid;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_financing_take_notes);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        pkmuser = intent.getStringExtra("pkmuser");
        pageFlag = intent.getStringExtra("pageFlag");
        categoryid = intent.getStringExtra("categoryid");
    }

    @Override
    public void initView() {
        take_notes_tab = (SlidingTabLayout) findViewById(R.id.take_notes_tab);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        vp = (ViewPager) findViewById(R.id.vp);
    }

    @Override
    public void afterInitView() {
        // pageFlag: "1" 代表商家理财  "2" 代表普通理财
        mFragments.add(FinancingTakeNotesFragment.getInstance("进行中", pkmuser, pageFlag,categoryid));
        mFragments.add(FinancingTakeNotesFragment.getInstance("已结束", pkmuser, pageFlag,categoryid));
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(mFragments.size());
        take_notes_tab.setViewPager(vp);
        take_notes_tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
//                mFragments.get(position).setRefresh();
            }

            @Override
            public void onTabReselect(int position) {
//                mFragments.get(position).setRefresh();
            }
        });
    }

    @Override
    public void bindListener() {
        rl_back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {

    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
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
}
