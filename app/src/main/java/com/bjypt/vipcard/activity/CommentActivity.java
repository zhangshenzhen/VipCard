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

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.fragment.CommentAllFra;
import com.bjypt.vipcard.fragment.CommentGoodFra;
import com.bjypt.vipcard.fragment.CommentOtherFra;
import com.bjypt.vipcard.model.CommentDetailsBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 涂有泽 .
 * Date by 2016/4/21
 * Use by 评论列表页面
 */
public class CommentActivity extends FragmentActivity implements View.OnClickListener, VolleyCallBack {

    private CommentAllFra mFragment1 = new CommentAllFra();
    private CommentGoodFra mFragment2 = new CommentGoodFra();
    private CommentOtherFra mFragment3 = new CommentOtherFra();

    private static final int TAB_INDEX_COUNT = 3;

    private static final int TAB_INDEX_ONE = 0;
    private static final int TAB_INDEX_TWO = 1;
    private static final int TAB_INDEX_THREE = 2;

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private RelativeLayout mRelaOne, mRelaThree, mRelaTwo;
    private TextView tv_1, tv_2, tv_3;
    private ImageView iv_1, iv_2, iv_3;
    public static String commentPkmuser;
    private RelativeLayout mBack;

    private TextView mScore;
    private TextView mCompare;
    private CommentDetailsBean commentDetailsBean;
    private ImageView mStarOne, mStarTwo, mStarThree, mStarFour, mStarFive;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentlist);
        setUpViewPager();
        initView();
    }

    public void initView() {

        Intent intent = getIntent();
        commentPkmuser = intent.getStringExtra("pkmuser");
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", commentPkmuser);
        Wethod.httpPost(CommentActivity.this, 1, Config.web.comment_details, params, this);

        mRelaOne = (RelativeLayout) findViewById(R.id.relative_1);
        mRelaTwo = (RelativeLayout) findViewById(R.id.relative_2);
        mRelaThree = (RelativeLayout) findViewById(R.id.relative_3);

        mStarOne = (ImageView) findViewById(R.id.star_one);
        mStarTwo = (ImageView) findViewById(R.id.star_two);
        mStarThree = (ImageView) findViewById(R.id.star_three);
        mStarFour = (ImageView) findViewById(R.id.star_four);
        mStarFive = (ImageView) findViewById(R.id.star_five);

        mBack = (RelativeLayout) findViewById(R.id.comment_back);
        mBack.setOnClickListener(this);

        mScore = (TextView) findViewById(R.id.tv_fenshu);
        mCompare = (TextView) findViewById(R.id.tv_bijiao);
        //注册Tab点击事件
        mRelaOne.setOnClickListener(this);
        mRelaTwo.setOnClickListener(this);
        mRelaThree.setOnClickListener(this);

        //tab文字
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);

        //tab标签图片
        iv_1 = (ImageView) findViewById(R.id.iv_1);
        iv_2 = (ImageView) findViewById(R.id.iv_2);
        iv_3 = (ImageView) findViewById(R.id.iv_3);
    }

    /***********************处理显示星星的方法***********************/
    public void showStar(float num) {
        if (num == 0.0) {
            mStarOne.setImageResource(R.mipmap.pf_wf);
            mStarTwo.setImageResource(R.mipmap.pf_wf);
            mStarThree.setImageResource(R.mipmap.pf_wf);
            mStarFour.setImageResource(R.mipmap.pf_wf);
            mStarFive.setImageResource(R.mipmap.pf_wf);
        } else if (num > 0 && num <= 0.5) {
            mStarOne.setImageResource(R.mipmap.pf_bf);
            mStarTwo.setImageResource(R.mipmap.pf_wf);
            mStarThree.setImageResource(R.mipmap.pf_wf);
            mStarFour.setImageResource(R.mipmap.pf_wf);
            mStarFive.setImageResource(R.mipmap.pf_wf);
        } else if (num > 0.5 && num <= 1) {
            mStarOne.setImageResource(R.mipmap.pf_mf);
            mStarTwo.setImageResource(R.mipmap.pf_wf);
            mStarThree.setImageResource(R.mipmap.pf_wf);
            mStarFour.setImageResource(R.mipmap.pf_wf);
            mStarFive.setImageResource(R.mipmap.pf_wf);
        } else if (num > 1 && num <= 1.5) {
            mStarOne.setImageResource(R.mipmap.pf_mf);
            mStarTwo.setImageResource(R.mipmap.pf_bf);
            mStarThree.setImageResource(R.mipmap.pf_wf);
            mStarFour.setImageResource(R.mipmap.pf_wf);
            mStarFive.setImageResource(R.mipmap.pf_wf);
        } else if (num > 1.5 && num <= 2) {
            mStarOne.setImageResource(R.mipmap.pf_mf);
            mStarTwo.setImageResource(R.mipmap.pf_mf);
            mStarThree.setImageResource(R.mipmap.pf_wf);
            mStarFour.setImageResource(R.mipmap.pf_wf);
            mStarFive.setImageResource(R.mipmap.pf_wf);
        } else if (num > 2 && num <= 2.5) {
            mStarOne.setImageResource(R.mipmap.pf_mf);
            mStarTwo.setImageResource(R.mipmap.pf_mf);
            mStarThree.setImageResource(R.mipmap.pf_bf);
            mStarFour.setImageResource(R.mipmap.pf_wf);
            mStarFive.setImageResource(R.mipmap.pf_wf);
        } else if (num > 2.5 && num <= 3) {
            mStarOne.setImageResource(R.mipmap.pf_mf);
            mStarTwo.setImageResource(R.mipmap.pf_mf);
            mStarThree.setImageResource(R.mipmap.pf_mf);
            mStarFour.setImageResource(R.mipmap.pf_wf);
            mStarFive.setImageResource(R.mipmap.pf_wf);
        } else if (num > 3 && num <= 3.5) {
            mStarOne.setImageResource(R.mipmap.pf_mf);
            mStarTwo.setImageResource(R.mipmap.pf_mf);
            mStarThree.setImageResource(R.mipmap.pf_mf);
            mStarFour.setImageResource(R.mipmap.pf_bf);
            mStarFive.setImageResource(R.mipmap.pf_wf);
        } else if (num > 3.5 && num <= 4) {
            mStarOne.setImageResource(R.mipmap.pf_mf);
            mStarTwo.setImageResource(R.mipmap.pf_mf);
            mStarThree.setImageResource(R.mipmap.pf_mf);
            mStarFour.setImageResource(R.mipmap.pf_mf);
            mStarFive.setImageResource(R.mipmap.pf_wf);
        } else if (num > 4 && num <= 4.5) {
            mStarOne.setImageResource(R.mipmap.pf_mf);
            mStarTwo.setImageResource(R.mipmap.pf_mf);
            mStarThree.setImageResource(R.mipmap.pf_mf);
            mStarFour.setImageResource(R.mipmap.pf_mf);
            mStarFive.setImageResource(R.mipmap.pf_bf);
        } else if (num > 4.5 && num <= 5) {
            mStarOne.setImageResource(R.mipmap.pf_mf);
            mStarTwo.setImageResource(R.mipmap.pf_mf);
            mStarThree.setImageResource(R.mipmap.pf_mf);
            mStarFour.setImageResource(R.mipmap.pf_mf);
            mStarFive.setImageResource(R.mipmap.pf_mf);
        }
    }

    private void setUpViewPager() {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.comment_pager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //此处设置Tab上面的文字
                if (position == 0) {
                    iv_1.setVisibility(View.VISIBLE);
                    iv_2.setVisibility(View.GONE);
                    iv_3.setVisibility(View.GONE);
                } else if (position == 1) {
                    iv_1.setVisibility(View.GONE);
                    iv_2.setVisibility(View.VISIBLE);
                    iv_3.setVisibility(View.GONE);

                } else if (position == 2) {
                    iv_1.setVisibility(View.GONE);
                    iv_2.setVisibility(View.GONE);
                    iv_3.setVisibility(View.VISIBLE);
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
        switch (id) {
            case R.id.relative_1:
                iv_1.setVisibility(View.VISIBLE);
                iv_2.setVisibility(View.GONE);
                iv_3.setVisibility(View.GONE);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.relative_2:
                iv_1.setVisibility(View.GONE);
                iv_2.setVisibility(View.VISIBLE);
                iv_3.setVisibility(View.GONE);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.relative_3:
                iv_1.setVisibility(View.GONE);
                iv_2.setVisibility(View.GONE);
                iv_3.setVisibility(View.VISIBLE);
                mViewPager.setCurrentItem(2);
                break;
            case R.id.comment_back:
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                commentDetailsBean = objectMapper.readValue(result.toString(), CommentDetailsBean.class);
                tv_1.setText("全部(" + commentDetailsBean.getResultData().getTotalComment() + ")");
                tv_2.setText("好评(" + commentDetailsBean.getResultData().getPraiseComment() + ")");
                tv_3.setText("其他(" + commentDetailsBean.getResultData().getOtherComment() + ")");
                showStar(commentDetailsBean.getResultData().getAvgScore());
                mScore.setText(commentDetailsBean.getResultData().getAvgScore() + "");
                mCompare.setText("高于同行" + commentDetailsBean.getResultData().getRank() + "%");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 1) {

        }
    }

    @Override
    public void onError(VolleyError volleyError) {

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