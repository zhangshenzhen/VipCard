package com.bjypt.vipcard.fragment;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.fragment.FinancingKakaAccountFragment;
import com.bjypt.vipcard.fragment.FinancingKahuiAccountFragment;
import com.orhanobut.logger.Logger;

import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;

import java.util.List;
import java.util.ArrayList;

public class FinancingAccountFragment  extends BaseFrament implements View.OnClickListener, VolleyCallBack, TabLayout.OnTabSelectedListener {
    private View view;
    private ViewPager viewPager;

    private List<Fragment> list;
    private MyAdapter adapter;
    private String[] titles = {"钱包金融","我的卡卡"};
    private TabLayout tabLayout;
    String pkmuser="";
    String appCan="0";
    private TabClickListener tabClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_financing_account, container, false);
        return view;
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


    public void setOnTabClick(TabClickListener listener){
        tabClickListener = listener;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tabClickListener != null){
            tabClickListener.showHidden(tab.getPosition());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public interface TabClickListener{
        // 0:显示  1.隐藏
        void showHidden(int state);
    }


    @Override
    public void beforeInitView() {
      Bundle bundle=getArguments();
      pkmuser=bundle.getString("pkmuser");
      appCan=bundle.getString("appCan");
    }

    @Override
    public void initView() {
        //实例化
        viewPager = (ViewPager) view.findViewById(R.id.pager);

        //页面，数据源
        list = new ArrayList<>();

        list.add(new FinancingKahuiAccountFragment());
        if(appCan.equals("1")) {
            FinancingKakaAccountFragment financingKakaAccountFragment = new FinancingKakaAccountFragment();
            Bundle bundle = new Bundle();
            Logger.e("pkmuser："+pkmuser);
            bundle.putString("pkmuser", pkmuser);
            financingKakaAccountFragment.setArguments(bundle);
            list.add(financingKakaAccountFragment);
        }
        //ViewPager的适配器
        adapter = new MyAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) view.findViewById(R.id.tab);
        //绑定
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);
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
    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
