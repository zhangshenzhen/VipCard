package com.bjypt.vipcard.activity.shangfeng.primary.merchant.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.adapter.ViewPageFragmentAdapter;
import com.bjypt.vipcard.activity.shangfeng.base.BaseActivity;
import com.bjypt.vipcard.activity.shangfeng.base.BaseFragment;
import com.bjypt.vipcard.activity.shangfeng.common.LocateResultFields;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MerchantListBean;
import com.bjypt.vipcard.activity.shangfeng.util.SharedPreferencesUtils;
import com.bjypt.vipcard.activity.shangfeng.widget.NumberIncreaseView;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 商家类别列表
 */
public class MerchantActivity extends BaseActivity {

    @BindView(R.id.ll_data)
    LinearLayout ll_data;

    @BindView(R.id.ibtn_back)
    ImageButton ibtn_back;
    /**
     * 搜索
     */
    @BindView(R.id.ibtn_search)
    ImageButton ibtn_search;
    /**
     * 搜搜标题
     */
    @BindView(R.id.rl_search_title)
    RelativeLayout rl_search_title;
    /**
     * 输入框
     */
    @BindView(R.id.et_search_content)
    EditText et_search_content;
    /**
     * 取消
     */
    @BindView(R.id.btn_cancal)
    TextView btn_cancal;

    @BindView(R.id.tvtablayout)
    TabLayout tvtablayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.statusBarView)
    View statusBarView;

    @BindView(R.id.tv_hint)
    TextView tv_hint;

    @BindView(R.id.ll_progress)
    LinearLayout ll_progress;
    @BindView(R.id.iv_loading)
    ImageView iv_loading;
    @BindView(R.id.tv_count)
    NumberIncreaseView tv_count;

    @BindView(R.id.ll_merchant_data)
    LinearLayout ll_merchant_data;

    /**
     * 搜索
     */
    @BindView(R.id.btn_search)
    Button btn_search;

    private static String TAG_KEY = "TAG";
    private static String PRICE = "price";

    private List<BaseFragment> mFregment = null;
    private String cityCode;
    private String longitude;
    private String latitude;
    private String tag;
    private String price;
    private ViewPageFragmentAdapter adapter;
    private AnimationDrawable animationWave;
    private MyBroadcastReceiver receiver;
    private List<MerchantListBean> merchantListBeanLis;


    /**
     * @param context
     * @param tag
     * @param price
     */
    public static void callActivity(Context context, String tag, String price) {
        Intent intent = new Intent(context, MerchantActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(TAG_KEY, tag);
        bundle.putString(PRICE, price);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchant;
    }

    @Override
    protected void initInjector() {
//        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.app_theme_color));

        //广播接受者实例
        receiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.shangfeng.yiyou.MERCHANT_COUNT");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void init() {
        ll_progress.setVisibility(View.VISIBLE);
        animationWave = (AnimationDrawable) iv_loading.getDrawable();
        animationWave.start();

        ViewGroup.LayoutParams layoutParams = statusBarView.getLayoutParams();
        layoutParams.height = getStatusBarHeight();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tag = bundle.getString(TAG_KEY);
            price = bundle.getString(PRICE);

        }

        viewPager.setOffscreenPageLimit(3);
        mFregment = new ArrayList<>();
        Object city_code = SharedPreferencesUtils.get(LocateResultFields.CITY_CODE, "");
        if (city_code != null) {
            cityCode = String.valueOf(city_code);
        }

        Object longitude_ = SharedPreferencesUtils.get(LocateResultFields.LONGITUDE, "");
        if (longitude_ != null) {
            longitude = String.valueOf(longitude_);
        }

        Object latitude_ = SharedPreferencesUtils.get(LocateResultFields.LATITUDE, "");
        if (city_code != null) {
            latitude = String.valueOf(latitude_);
        }
        mFregment.add(MerchantListFragment.newInstance(cityCode, tag, price, longitude, latitude, 1));
        mFregment.add(MerchantListFragment.newInstance(cityCode, tag, price, longitude, latitude, 0));
        mFregment.add(MerchantListFragment.newInstance(cityCode, tag, price, longitude, latitude, 2));
        adapter = new ViewPageFragmentAdapter(getSupportFragmentManager(), mFregment);
        viewPager.setAdapter(adapter);

        tvtablayout.addTab(tvtablayout.newTab());
        tvtablayout.addTab(tvtablayout.newTab());
        tvtablayout.addTab(tvtablayout.newTab());

        tvtablayout.setupWithViewPager(viewPager);

        tvtablayout.getTabAt(1).setText(getResources().getString(R.string.recently));
        tvtablayout.getTabAt(0).setText(getResources().getString(R.string.most_preferential));
        tvtablayout.getTabAt(2).setText(getResources().getString(R.string.Intelligent_sorting));
        tvtablayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tvtablayout, 20, 20);
            }
        });

        tvtablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(rl_search_title.getVisibility() == View.VISIBLE){
                    int currentPosition = viewPager.getCurrentItem();
                    ((MerchantListFragment) mFregment.get(currentPosition)).searchMerchant(null);
                    rl_search_title.setVisibility(View.GONE);
                    ibtn_search.setVisibility(View.VISIBLE);
                    et_search_content.setText("");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @OnClick(R.id.btn_search)
    public void searchMerchants(View view){
        String keyword = et_search_content.getText().toString();
        Logger.v("搜索 ：" + keyword);
        int currentPosition = viewPager.getCurrentItem();
        ((MerchantListFragment) mFregment.get(currentPosition)).searchMerchant(keyword);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @OnClick({R.id.ibtn_search, R.id.btn_cancal})
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_search:  // 搜索
                if (merchantListBeanLis != null) {
                    rl_search_title.setVisibility(View.VISIBLE);
                    ibtn_search.setVisibility(View.GONE);
                }
                break;

            case R.id.btn_cancal: // 取消
                int currentPosition = viewPager.getCurrentItem();
                ((MerchantListFragment) mFregment.get(currentPosition)).searchMerchant(null);
                rl_search_title.setVisibility(View.GONE);
                ibtn_search.setVisibility(View.VISIBLE);
                et_search_content.setText("");

                break;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * 设置tabLayoyt 下划线宽度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.v("接收到广播");
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                ArrayList<MerchantListBean> merchantListBeans = bundle.getParcelableArrayList("MerchantData");
                merchantListBeanLis = merchantListBeans;
                int time = (merchantListBeanLis.size()<10 ? 2: 4);
                startCount(time +1, 0);
                tv_count.startAnim(merchantListBeans.size(), time*1000);

            } else {

                startCount(2, 1);
                tv_count.startAnim(0, 5000);

            }

            if (receiver != null) {
                unregisterReceiver(receiver);
                receiver = null;
            }
        }
    }

    /**
     * 计数器
     * @param time
     * @param type
     */
    public void startCount(final int time, final int type) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return time - increaseTime.intValue();
                    }
                })
                .take(time + 1)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Logger.v("开始");
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        if (0 == type) {
                            ll_progress.setVisibility(View.GONE);
                            ll_data.setVisibility(View.VISIBLE);
                        } else {
                            MerchantActivity.this.finish();
                        }
                        if (animationWave != null) {
                            animationWave.stop();
                        }
                        refresh();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Logger.v("当前："+integer);
                        if(0 != type && integer == (time-2)){
                            tv_count.setVisibility(View.GONE);
                            tv_hint.setText("暂未匹配到商家，请重新输入···");
                        }
                    }
                });
    }


    /**
     * 抢单效果结束后 显示刷新停留一秒
     */
    private void refresh(){
        if(merchantListBeanLis != null && merchantListBeanLis.size() > 0){
            showProgress();
            Observable.timer(1000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            hideProgress();
                        }
                    });
        }
    }


    @OnClick(R.id.ibtn_back)
    public void finishActivity() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ibtn_back.getWindowToken(), 0);
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
}
