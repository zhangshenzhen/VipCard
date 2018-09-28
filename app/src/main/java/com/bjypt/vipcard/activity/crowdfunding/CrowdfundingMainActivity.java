package com.bjypt.vipcard.activity.crowdfunding;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.MainActivity;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.BaseFraActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.TrackCommon;
import com.bjypt.vipcard.fragment.MineFragment;
import com.bjypt.vipcard.fragment.NewHomeFrag;
import com.bjypt.vipcard.fragment.NewsFragment;
import com.bjypt.vipcard.fragment.ShopStreetFragment;
import com.bjypt.vipcard.fragment.crowdfunding.CrowdfundingFragment;
import com.bjypt.vipcard.fragment.crowdfunding.MyCrowdfundingFragment;
import com.sinia.orderlang.activity.SpecialPriceMainActivity;

import org.piwik.sdk.extra.TrackHelper;

public class CrowdfundingMainActivity extends BaseFraActivity {

    private View crowdfunding_fragment;

    private LinearLayout tab_crowdfunding_main;
    private LinearLayout tab_crowdfunding_my;
    private TextView tv_crowdfunding_main;
    private TextView tv_crowdfunding_my;
    FrameLayout id_content;

    private FragmentManager fragmentManager;
    FragmentTransaction transaction;

    CrowdfundingFragment mainCrowdfundingFragment;
    MyCrowdfundingFragment myCrowdfundingFragment;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_crowdfunding_main);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tab_crowdfunding_main = findViewById(R.id.tab_crowdfunding_main);
        tab_crowdfunding_my = findViewById(R.id.tab_crowdfunding_my);
        tv_crowdfunding_main = findViewById(R.id.tv_crowdfunding_main);
        tv_crowdfunding_my = findViewById(R.id.tv_crowdfunding_my);
        fragmentManager = getSupportFragmentManager();

        setTabSelection(0);
    }

    public void setTabSelection(int index) {
        //重置按钮
        resetBtn();
        transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 0:
                tv_crowdfunding_main.setTextColor(Color.parseColor("#52b8b8"));
                tv_crowdfunding_main.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.cf_main_tab_select, 0, 0);
                if (mainCrowdfundingFragment == null) {
                    mainCrowdfundingFragment = new CrowdfundingFragment();
                    transaction.replace(R.id.id_content, mainCrowdfundingFragment);
                } else {
                    transaction.show(mainCrowdfundingFragment);
                }
//                TrackHelper.track().event(TrackCommon.ViewTrackCategroy, TrackCommon.ViewTrackPagesMainTab).name("首页").value(1f).with(getTracker());
                break;
            case 1:
                tv_crowdfunding_my.setTextColor(Color.parseColor("#52b8b8"));
                tv_crowdfunding_my.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.cf_my_tab_select, 0, 0);
                if (myCrowdfundingFragment == null) {
                    myCrowdfundingFragment = new MyCrowdfundingFragment();
                    transaction.add(R.id.id_content, myCrowdfundingFragment);
                } else {
                    transaction.show(myCrowdfundingFragment);
                }
//                TrackHelper.track().event(TrackCommon.ViewTrackCategroy, TrackCommon.ViewTrackPagesMerchatTab).name("铺子").value(1f).with(getTracker());
                break;
        }
        transaction.commit();
    }

    /**
     * 清楚掉所有选中状态
     */
    private void resetBtn() {
        tv_crowdfunding_main.setTextColor(Color.parseColor("#7b7b7b"));
        tv_crowdfunding_main.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.cf_main_tab_unselect, 0, 0);
        tv_crowdfunding_my.setTextColor(Color.parseColor("#7b7b7b"));
        tv_crowdfunding_my.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.cf_my_tab_unselect, 0, 0);
    }

    /**
     * 将所有的Fragment都置为隐藏状态
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (myCrowdfundingFragment != null) {
            transaction.hide(myCrowdfundingFragment);
        }
        if (mainCrowdfundingFragment != null) {
            transaction.hide(mainCrowdfundingFragment);
        }
    }


    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        tab_crowdfunding_main.setOnClickListener(this);
        tab_crowdfunding_my.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.tab_crowdfunding_main:
                setTabSelection(0);
                break;
            case R.id.tab_crowdfunding_my:
                setTabSelection(1);
                break;
        }
    }
}
