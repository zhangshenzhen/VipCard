package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseFraActivity;
import com.bjypt.vipcard.fragment.EnvironmentFra;
import com.bjypt.vipcard.fragment.MerchantDetailsFra;
import com.bjypt.vipcard.fragment.MerchantOtherFra;
import com.bjypt.vipcard.umeng.UmengCountContext;


/**
 * Created by 涂有泽 .
 * Date by 2016/3/29
 * Use by 商家图片
 */
public class MerchantPicActivity extends BaseFraActivity{

    private GridView mPicGrid;
    private FragmentTransaction transaction;

    private EnvironmentFra environmentFra;
    private MerchantDetailsFra merchantDetailsFra;
    private MerchantOtherFra merchantOtherFra;
    private  Fragment[] fragments;
    private RelativeLayout[] mTabs;
    private View envir_v,details_v,other_v;
    private FragmentManager fragmentManager;
    // 记录当前位置
    private int currentposition;
    private int index;
    // 当前fragment的index
    private int currentTabIndex;
    private TextView other_tv,details_tv,envir_tv;
    private RelativeLayout mBack;
    private String pkmuser;//商家主键

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_merchant_pic);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        pkmuser = intent.getStringExtra("pkmuser");
    }

    @Override
    public void initView() {
        mBack = (RelativeLayout) findViewById(R.id.merchant_back);
        envir_v  = findViewById(R.id.envir_v);
        details_v = findViewById(R.id.details_v);
        other_v = findViewById(R.id.other_v);

        envir_tv = (TextView) findViewById(R.id.envir_tv);
        details_tv = (TextView) findViewById(R.id.details_tv);
        other_tv = (TextView) findViewById(R.id.other_tv);
        mTabs = new RelativeLayout[3];
        mTabs[0] = (RelativeLayout) findViewById(R.id.envir);
        mTabs[1] = (RelativeLayout) findViewById(R.id.details);
        mTabs[2] = (RelativeLayout) findViewById(R.id.other);
        fragments = new Fragment[]{environmentFra,merchantDetailsFra,merchantOtherFra};

        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
    }

    @Override
    public void afterInitView() {

    }


    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
        mTabs[0].setOnClickListener(this);
        mTabs[1].setOnClickListener(this);
        mTabs[2].setOnClickListener(this);
    }



    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.envir:
                index = 0;
                envir_v.setVisibility(View.VISIBLE);
                details_v.setVisibility(View.INVISIBLE);
                other_v.setVisibility(View.INVISIBLE);
                envir_tv.setTextColor(Color.parseColor("#EC594D"));
                details_tv.setTextColor(getResources().getColor(R.color.black));
                other_tv.setTextColor(getResources().getColor(R.color.black));
                currentposition = 0;
                setTabSelection(0);


                break;
            case R.id.details:
                index = 1;
                envir_v.setVisibility(View.INVISIBLE);
                details_v.setVisibility(View.VISIBLE);
                other_v.setVisibility(View.INVISIBLE);
                details_tv.setTextColor(Color.parseColor("#EC594D"));
                other_tv.setTextColor(getResources().getColor(R.color.black));
                envir_tv.setTextColor(getResources().getColor(R.color.black));

                setTabSelection(1);
                break;
            case R.id.other:
                index = 2;
                envir_v.setVisibility(View.INVISIBLE);
                details_v.setVisibility(View.INVISIBLE);
                other_v.setVisibility(View.VISIBLE);
                other_tv.setTextColor(Color.parseColor("#EC594D"));
                details_tv.setTextColor(getResources().getColor(R.color.black));
                envir_tv.setTextColor(getResources().getColor(R.color.black));

                setTabSelection(2);
                break;
            case R.id.merchant_back:
                finish();
                break;
        }

    }

    /**
     * 将所有的Fragment都置为隐藏状态
     */
    private void hideFragment(FragmentTransaction transaction){
        if(environmentFra !=null){
            transaction.hide(environmentFra);
        }
        if(merchantDetailsFra !=null){
            transaction.hide(merchantDetailsFra);
        }
        if(merchantOtherFra !=null){
            transaction.hide(merchantOtherFra);
        }


    }



    private void setTabSelection(int index){
        //重置按钮
//        resetBtn();
        transaction =fragmentManager.beginTransaction();
        //开启一个Fragment事物
        hideFragment(transaction);
        switch (index){
            case 0:

                if(environmentFra==null){
                    //如果Fragment为空，则创建一个新的并创建到界面上
                    environmentFra = new EnvironmentFra();
                    Bundle bundle = new Bundle();
                    bundle.putString("pkmuser",pkmuser);
                    environmentFra.setArguments(bundle);
                    transaction.add(R.id.fragment_container, environmentFra);


                }else{
                    //如果Fragment不为空，则直接将他显示出来
                    transaction.show(environmentFra);
                }
                break;
            case 1:

                if(merchantDetailsFra == null){
                    merchantDetailsFra = new MerchantDetailsFra();
                    Bundle bundle = new Bundle();
                    bundle.putString("pkmuser",pkmuser);
                    merchantDetailsFra.setArguments(bundle);
                    transaction.add(R.id.fragment_container,merchantDetailsFra);
                }else{
                    transaction.show(merchantDetailsFra);
                }
                break;
            case 2:

                if(merchantOtherFra == null){
                    merchantOtherFra = new MerchantOtherFra();
                    Bundle bundle = new Bundle();
                    bundle.putString("pkmuser",pkmuser);
                    merchantOtherFra.setArguments(bundle);
                    transaction.add(R.id.fragment_container,merchantOtherFra);
                }else{
                    transaction.show(merchantOtherFra);
                }
                break;


        }
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }
}
