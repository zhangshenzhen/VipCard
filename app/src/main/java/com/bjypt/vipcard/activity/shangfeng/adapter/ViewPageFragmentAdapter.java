package com.bjypt.vipcard.activity.shangfeng.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.bjypt.vipcard.activity.shangfeng.base.BaseFragment;

import java.util.List;

/**
 * 可购买卡劵、搜索商家卡劵 分类适配器
 */
public class ViewPageFragmentAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> mFragmnet;

    public ViewPageFragmentAdapter(FragmentManager fm, List<BaseFragment> mFragmnet) {
        super(fm);
        this.mFragmnet = mFragmnet;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmnet.get(position);
    }

    @Override
    public int getCount() {
        return mFragmnet.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
