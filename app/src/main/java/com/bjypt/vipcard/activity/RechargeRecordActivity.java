package com.bjypt.vipcard.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.fragment.PrecentRecordFragment;
import com.bjypt.vipcard.fragment.RechangeRecordFragment;
import com.bjypt.vipcard.fragment.TransactionRecordFragment;
import com.bjypt.vipcard.umeng.UmengCountContext;

import java.util.ArrayList;
import java.util.List;

public class RechargeRecordActivity extends FragmentActivity implements View.OnClickListener{
    private List<Fragment> lists;
    private TextView tv_rechange_one;
    private TextView tv_rechange_two;
    private TextView tv_rechange_three;
    private RelativeLayout rechange_back;
    private RelativeLayout rl_rechange_one;
    private RelativeLayout rl_rechange_two;
    private RelativeLayout rl_rechange_three;
    private View view_one;
    private View view_two;
    private View view_three;
    private ViewPager viewPager;
    private MyFragmentAdapter adapter;
    PrecentRecordFragment precentRecordFragment;
     RechangeRecordFragment rechangeRecordFragment;
    TransactionRecordFragment transactionRecordFragment;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_record);
        initView();
         initData();
        initListener();
    }

    private void initView() {
        type = getIntent().getIntExtra("type",0);
        lists = new ArrayList<Fragment>();
        precentRecordFragment = new PrecentRecordFragment();
        rechangeRecordFragment = new RechangeRecordFragment();
        transactionRecordFragment = new TransactionRecordFragment();
        lists.add(transactionRecordFragment);
        lists.add(rechangeRecordFragment);
        lists.add(precentRecordFragment);
        viewPager = (ViewPager) findViewById(R.id.rechange_viewpager);
        view_one = findViewById(R.id.view_one);
        view_two = findViewById(R.id.view_two);
        view_three = findViewById(R.id.view_three);
        rl_rechange_one = (RelativeLayout) findViewById(R.id.rl_rechange_one);
        rl_rechange_two = (RelativeLayout) findViewById(R.id.rl_rechange_two);
        rl_rechange_three = (RelativeLayout) findViewById(R.id.rl_rechange_three);
        rechange_back = (RelativeLayout) findViewById(R.id.rechange_back);
        tv_rechange_one = (TextView) findViewById(R.id.tv_rechange_one);
        tv_rechange_two = (TextView) findViewById(R.id.tv_rechange_two);
        tv_rechange_three = (TextView) findViewById(R.id.tv_rechange_three);

    }

    private void initData() {



    }

    private void initListener() {

        rl_rechange_one.setOnClickListener(this);
        rl_rechange_two.setOnClickListener(this);
        rl_rechange_three.setOnClickListener(this);
        rechange_back.setOnClickListener(this);
        adapter = new MyFragmentAdapter(getSupportFragmentManager(),lists);
        viewPager.setAdapter(adapter);
        if (type==3){
            setChanger(3);
        }else {
            setChanger(1);
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    setChanger(1);
                }else if (position==1){
                    setChanger(2);
                }else {
                    setChanger(3);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_rechange_one:
                setChanger(1);
                break;
            case R.id.rl_rechange_two:
                setChanger(2);
                break;
            case R.id.rl_rechange_three:
                setChanger(3);
                break;
            case R.id.rechange_back:
                finish();
                break;
        }

    }

    class MyFragmentAdapter extends FragmentPagerAdapter{
         private List<Fragment> lists;

         public MyFragmentAdapter(FragmentManager fm, List<Fragment> lists) {
             super(fm);
             this.lists = lists;
         }

         @Override
         public android.support.v4.app.Fragment getItem(int position) {
             return lists.get(position);
         }

         @Override
         public int getCount() {
             return lists.size();
         }
     }
    public void setChanger(int type){
        if (type==1){
            tv_rechange_one.setTextColor(Color.RED);
            tv_rechange_two.setTextColor(Color.BLACK);
            tv_rechange_three.setTextColor(Color.BLACK);
            view_one.setVisibility(View.VISIBLE);
            view_two.setVisibility(View.GONE);
            view_three.setVisibility(View.GONE);
            viewPager.setCurrentItem(0);
        }else if (type==2){
            tv_rechange_one.setTextColor(Color.BLACK);
            tv_rechange_two.setTextColor(Color.RED);
            tv_rechange_three.setTextColor(Color.BLACK);
            view_one.setVisibility(View.GONE);
            view_two.setVisibility(View.VISIBLE);
            view_three.setVisibility(View.GONE);
            viewPager.setCurrentItem(1);
        }else {
            tv_rechange_one.setTextColor(Color.BLACK);
            tv_rechange_two.setTextColor(Color.BLACK);
            tv_rechange_three.setTextColor(Color.RED);
            view_one.setVisibility(View.GONE);
            view_two.setVisibility(View.GONE);
            view_three.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(2);
        }
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
