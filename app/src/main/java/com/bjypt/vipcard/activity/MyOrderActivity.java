package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.fragment.AllOrderFra;
import com.bjypt.vipcard.fragment.CommentAllFra;
import com.bjypt.vipcard.fragment.CommentGoodFra;
import com.bjypt.vipcard.fragment.CommentOtherFra;
import com.bjypt.vipcard.fragment.WaitCommentFra;
import com.bjypt.vipcard.fragment.WaitPayOrderFra;
import com.bjypt.vipcard.fragment.WaitUseFra;
import com.bjypt.vipcard.umeng.UmengCountContext;


/**
 * Created by Administrator on 2016/12/1 0001.
 * 我的预定订单
 */

public class MyOrderActivity extends FragmentActivity implements View.OnClickListener{

    private AllOrderFra mFragment1 = new AllOrderFra();//全部订单
    private WaitPayOrderFra mFragment3 = new WaitPayOrderFra();//待支付订单
    private WaitUseFra mFragment2 = new WaitUseFra();//待使用订单
    private WaitCommentFra mFragment4 = new WaitCommentFra();//待评价订单

    private static final int TAB_INDEX_COUNT = 4;

    private static final int TAB_INDEX_ONE = 0;
    private static final int TAB_INDEX_TWO = 1;
    private static final int TAB_INDEX_THREE = 2;
    private static final int TAB_INDEX_FOUR = 3;

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private RelativeLayout mRelaOne,mRelaThree,mRelaTwo,mRelaFour;
    private TextView tv_1,tv_2,tv_3,tv_4;
    private ImageView iv_1,iv_2,iv_3,iv_4;
    private RelativeLayout mBack;
    private TextView mEditOrder;
    public static boolean isShow = false;

    private TextView mScore;
    private TextView mCompare;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        setUpViewPager();
        initView();
    }

    public void initView(){


        mRelaOne = (RelativeLayout) findViewById(R.id.relative_1);
        mRelaTwo = (RelativeLayout) findViewById(R.id.relative_2);
        mRelaThree = (RelativeLayout) findViewById(R.id.relative_3);
        mRelaFour = (RelativeLayout) findViewById(R.id.relative_4);


        mEditOrder = (TextView) findViewById(R.id.edit_order);

        mBack = (RelativeLayout) findViewById(R.id.comment_back);
        mBack.setOnClickListener(this);

        mScore = (TextView) findViewById(R.id.tv_fenshu);
        mCompare = (TextView) findViewById(R.id.tv_bijiao);
        //注册Tab点击事件
        mRelaOne.setOnClickListener(this);
        mRelaTwo.setOnClickListener(this);
        mRelaThree.setOnClickListener(this);
        mRelaFour.setOnClickListener(this);

        mEditOrder.setOnClickListener(this);
        //tab文字
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);

        //tab标签图片
        iv_1 = (ImageView) findViewById(R.id.iv_1);
        iv_2 = (ImageView) findViewById(R.id.iv_2);
        iv_3 = (ImageView) findViewById(R.id.iv_3);
        iv_4 = (ImageView) findViewById(R.id.iv_4);
    }

    private void setUpViewPager() {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.comment_pager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //此处设置Tab上面的文字
                if(position == 0){
                    iv_1.setVisibility(View.VISIBLE);
                    iv_2.setVisibility(View.GONE);
                    iv_3.setVisibility(View.GONE);
                    iv_4.setVisibility(View.GONE);
                }else if(position == 1){
                    iv_1.setVisibility(View.GONE);
                    iv_2.setVisibility(View.VISIBLE);
                    iv_3.setVisibility(View.GONE);
                    iv_4.setVisibility(View.GONE);
                }else if(position == 2){
                    iv_1.setVisibility(View.GONE);
                    iv_2.setVisibility(View.GONE);
                    iv_3.setVisibility(View.VISIBLE);
                    iv_4.setVisibility(View.GONE);
                }else if(position == 3){
                    iv_1.setVisibility(View.GONE);
                    iv_2.setVisibility(View.GONE);
                    iv_3.setVisibility(View.GONE);
                    iv_4.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        //TODO
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        //TODO
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        //TODO
                        break;
                    default:
                        //TODO
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.relative_1:
                iv_1.setVisibility(View.VISIBLE);
                iv_2.setVisibility(View.GONE);
                iv_3.setVisibility(View.GONE);
                iv_4.setVisibility(View.GONE);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.relative_2:
                iv_1.setVisibility(View.GONE);
                iv_2.setVisibility(View.VISIBLE);
                iv_3.setVisibility(View.GONE);
                iv_4.setVisibility(View.GONE);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.relative_3:
                iv_1.setVisibility(View.GONE);
                iv_2.setVisibility(View.GONE);
                iv_3.setVisibility(View.VISIBLE);
                iv_4.setVisibility(View.GONE);
                mViewPager.setCurrentItem(2);
                break;
            case R.id.relative_4:
                iv_1.setVisibility(View.GONE);
                iv_2.setVisibility(View.GONE);
                iv_3.setVisibility(View.GONE);
                iv_4.setVisibility(View.VISIBLE);
                mViewPager.setCurrentItem(3);
                break;
            case R.id.comment_back:
                finish();
                break;
            case R.id.edit_order:
                if(mEditOrder.getText().toString().equals("编辑")){
                    mEditOrder.setText("完成");
                    isShow = true;
                    Intent intent = new Intent();
                    intent.putExtra("UPDATE_FLAG", 1);
                    intent.setAction("update_ui");
                    sendBroadcast(intent);
                }else if(mEditOrder.getText().toString().equals("完成")){
                    mEditOrder.setText("编辑");
                    isShow = false;
                    Intent intent = new Intent();
                    intent.putExtra("UPDATE_FLAG", 2);
                    intent.setAction("update_ui");
                    sendBroadcast(intent);
                }

                break;
        }
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            switch (position) {

                case TAB_INDEX_ONE:
                    return mFragment1;
                case TAB_INDEX_TWO:

                    return mFragment2;
                case TAB_INDEX_THREE:

                    return mFragment3;
                case TAB_INDEX_FOUR:
                    return mFragment4;
            }
            throw new IllegalStateException("No fragment at position " + position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return TAB_INDEX_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String tabLabel = null;
            switch (position) {
                case TAB_INDEX_ONE:
                    tabLabel = "R";
                    break;
                case TAB_INDEX_TWO:
                    tabLabel = getString(R.string.tab_2);
                    break;
                case TAB_INDEX_THREE:
                    tabLabel = getString(R.string.tab_3);
                    break;
                case TAB_INDEX_FOUR:
                    tabLabel = getString(R.string.tab_4);
                    break;
            }
            return tabLabel;
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
