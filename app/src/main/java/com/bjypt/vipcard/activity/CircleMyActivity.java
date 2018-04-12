package com.bjypt.vipcard.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseFraActivity;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.bean.CircleBgPic;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.dialog.NormalDialog;
import com.bjypt.vipcard.fragment.AttentionFragment;
import com.bjypt.vipcard.fragment.FansFragment;
import com.bjypt.vipcard.fragment.MyHomePageFragment;
import com.bjypt.vipcard.model.MemberCountInfoBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshScrollView;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.ChildViewPager;
import com.bjypt.vipcard.view.RoundImageView;
import com.bjypt.vipcard.view.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CircleMyActivity extends BaseFraActivity implements VolleyCallBack<String> {

    /* 控件 */
    private TextView add_attention;
    private LinearLayout add_linear;
    private TabLayout my_tablayout;
    private ChildViewPager my_viewpager;
    private MyFragmentAdapter myFragmentAdapter;
    private TextView tvTabName;
    private TextView tvTabNumber;
    private RelativeLayout circle_back;
    private RelativeLayout camera_relative;
    private TextView qianming_text;
    private LinearLayout qianming_linear;
    private ImageView qianming_iv;
    private TextView userIconName;
    private PullToRefreshScrollView pull_to_refresh_scroll;
    private RoundImageView iv_icon;
    private ImageView background_image;
    private TextView tag_textview;

    /* 数据 */
    private ArrayList<BaseFrament> mFragments = new ArrayList<>();
    private final String[] mTitles = {"主页", "关注", "收藏", "粉丝"};
    private int[] tabNumbers = {0, 0, 0, 0};
    private String myUid;
    private MemberCountInfoBean memberCountInfoBean;
    private int tabPosition;
    private RefreshReceiver mRefreshReceiver;
    private int isFollow;

    /*常量*/
    private static final int MEMBER_COUNT_REQUEST = 0001;
    private static final int REQUEST_PIC = 0011;
    private static final int Confirm_Attention = 0111;
    private static final int Cancle_Attention = 1111;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_circle_my);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        myUid = String.valueOf(intent.getStringExtra("uid"));                                //uid

        mRefreshReceiver = new RefreshReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("RESULT_OK");
        registerReceiver(mRefreshReceiver, filter);

        //获取主页关注收藏粉丝条数
        getMemberCount();
        //获取背景图和签名
        requestBgPic();

    }

    @Override
    public void initView() {
        add_attention = (TextView) findViewById(R.id.add_attention);
        add_linear = (LinearLayout) findViewById(R.id.add_linear);
        tag_textview = (TextView) findViewById(R.id.tag_textview);
        background_image = (ImageView) findViewById(R.id.background_image);
        iv_icon = (RoundImageView) findViewById(R.id.iv_icon);
        pull_to_refresh_scroll = (PullToRefreshScrollView) findViewById(R.id.pull_to_refresh_scroll);
        userIconName = (TextView) findViewById(R.id.userIconName);
        qianming_iv = (ImageView) findViewById(R.id.qianming_iv);
        circle_back = (RelativeLayout) findViewById(R.id.circle_back);
        qianming_text = (TextView) findViewById(R.id.qianming_text);
        qianming_linear = (LinearLayout) findViewById(R.id.qianming_linear);
        camera_relative = (RelativeLayout) findViewById(R.id.camera_relative);
        my_tablayout = (TabLayout) findViewById(R.id.my_tablayout);
        my_viewpager = (ChildViewPager) findViewById(R.id.my_viewpager);
        initFragments();
        initViewPager();

        //根据传递的uid和登录时的UID是否相同判断是我的还是他的界面
        if (getFromSharePreference(Config.userConfig.uid).equals(myUid)) {
            //UID相同我的界面   设置头像，昵称，签名  最下面的加关注不显示
            qianming_iv.setVisibility(View.VISIBLE);
            camera_relative.setVisibility(View.VISIBLE);
            tag_textview.setVisibility(View.GONE);
            add_linear.setVisibility(View.GONE);              //我的最下面的加关注不显示
            camera_relative.setOnClickListener(this);
            qianming_linear.setOnClickListener(this);
        } else {
            //UID不同他的界面
            camera_relative.setVisibility(View.GONE);
            qianming_iv.setVisibility(View.GONE);
            tag_textview.setVisibility(View.VISIBLE);
            add_linear.setVisibility(View.VISIBLE);          //他的最下面的加关注显示
            add_linear.setOnClickListener(this);
        }


    }

    private void initFragments() {
        mFragments.add(new MyHomePageFragment(0, myUid));
        mFragments.add(new AttentionFragment(myUid));
        mFragments.add(new MyHomePageFragment(2, myUid));
        mFragments.add(new FansFragment(myUid));
    }

    @Override
    public void afterInitView() {
        setTabView();
    }

    @Override
    public void bindListener() {
        circle_back.setOnClickListener(this);
        pull_to_refresh_scroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mFragments.get(tabPosition).afterInitView();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        my_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                refreshFragmentData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                refreshFragmentData();
            }
        });

    }

    /*
    * 刷新对应fragment的数据
    * */
    private void refreshFragmentData() {
        BaseFrament baseFrament = mFragments.get(my_viewpager.getCurrent());
        if (baseFrament != null && baseFrament.isVisible()) {
            baseFrament.afterInitView();
        }
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.add_linear:
                //0 未关注
                if (isFollow == 0) {
                    showAttentionDialog("确定关注此人？", 0);
                } else {
                    showAttentionDialog("确定不再关注此人？", 1);
                }
                break;
            case R.id.qianming_linear:
                Intent intents = new Intent(this, ChangeSignNameActivity.class);
                intents.putExtra("uid", myUid);
                startActivity(intents);
                break;
            case R.id.camera_relative:
                Intent intent = new Intent(CircleMyActivity.this, ReleaseInformationActivity.class);
                intent.putExtra("uid", myUid);
                startActivity(intent);
                break;
            case R.id.circle_back:
                finish();
                break;
        }
    }

    /**
     * 初始化Viewpager
     */
    private void initViewPager() {
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        my_viewpager.setAdapter(myFragmentAdapter);
        my_tablayout.setupWithViewPager(my_viewpager);

        //设置预缓存页
        my_viewpager.setOffscreenPageLimit(4);
        my_viewpager.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        my_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                tabPosition = position;
                my_viewpager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置Tab的样式
     */
    private void setTabView() {
        my_tablayout.removeAllTabs();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < mTitles.length; i++) {
            //依次获取标签
            View view = inflater.inflate(R.layout.layout_tab_title, null);
            tvTabName = (TextView) view.findViewById(R.id.tv_tab_name);
            tvTabNumber = (TextView) view.findViewById(R.id.tv_tab_number);
            //为标签填充数据
            tvTabName.setText(mTitles[i]);
            tvTabNumber.setText(String.valueOf(tabNumbers[i]));
            my_tablayout.addTab(my_tablayout.newTab().setCustomView(view));
        }

    }

    private void updateTabView() {
        for (int i = 0; i < mTitles.length; i++) {
            //依次获取标签
            View view = my_tablayout.getTabAt(i).getCustomView();
            tvTabName = (TextView) view.findViewById(R.id.tv_tab_name);
            tvTabNumber = (TextView) view.findViewById(R.id.tv_tab_number);
            //为标签填充数据
            tvTabName.setText(mTitles[i]);
            tvTabNumber.setText(String.valueOf(tabNumbers[i]));
        }
    }

    public void onRefreshComplete() {
        if (pull_to_refresh_scroll.isRefreshing()) {
            pull_to_refresh_scroll.onRefreshComplete();
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == MEMBER_COUNT_REQUEST) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                memberCountInfoBean = objectMapper.readValue(result.toString(), MemberCountInfoBean.class);

                tabNumbers[0] = memberCountInfoBean.getResultData().getThreads();
                tabNumbers[1] = memberCountInfoBean.getResultData().getFollowing();
                tabNumbers[2] = memberCountInfoBean.getResultData().getFavorites();
                tabNumbers[3] = memberCountInfoBean.getResultData().getFollower();

                userIconName.setText(memberCountInfoBean.getResultData().getUserResponse().getUsername());
                ImageLoader.getInstance().displayImage(memberCountInfoBean.getResultData().getUserResponse().getAvatar(), iv_icon, AppConfig.CIRCLE_HEADER_GRAY);
                isFollow = memberCountInfoBean.getResultData().getIsFollow();

                //isFollow为零表示没有关注过此人
                if (isFollow == 0) {
                    add_attention.setText("关注");
                } else {
                    add_attention.setText("已关注");
                }
                updateTabView();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == REQUEST_PIC) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                CircleBgPic circleBgPic = objectMapper.readValue(result.toString(), CircleBgPic.class);
                String picUrl = circleBgPic.getResultData().getPicUrl();
                ImageLoader.getInstance().displayImage(picUrl, background_image, AppConfig.CIRCLE_BACKGROUND_IMAGE);
                if (myUid.equals(getFromSharePreference(Config.userConfig.uid))) {
                    //我的
                    if (StringUtil.isEmpty(circleBgPic.getResultData().getSigns())) {
                        qianming_text.setText("还没有个性签名，快来编辑吧");
                    } else {
                        qianming_text.setText(circleBgPic.getResultData().getSigns());
                    }
                } else {
                    //他的
                    if (StringUtil.isEmpty(circleBgPic.getResultData().getSigns())) {
                        qianming_text.setText("这个人很懒，还没有签名");
                    } else {
                        qianming_text.setText(circleBgPic.getResultData().getSigns());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == Cancle_Attention) {
            ToastUtil.showToast(this, "取消关注成功");
            add_attention.setText("关注");
            getMemberCount();
        } else if (reqcode == Confirm_Attention) {
            ToastUtil.showToast(this, "关注成功");
            add_attention.setText("已关注");
            getMemberCount();
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    /**
     * ViewPager适配器
     */
    private class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

    }

    /* 显示对话框 是否取消关注*/
    private void showAttentionDialog(String string, final int s) {
        final NormalDialog mNormalDialog = NormalDialog.getInstance(this);
        mNormalDialog.setDesc(string);
        mNormalDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s == 1) {
                    mNormalDialog.dismiss();
                    //取消关注
                    cancleAttention();
                } else {
                    mNormalDialog.dismiss();
                    //关注
                    insertAttention();
                }
            }
        });
        mNormalDialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNormalDialog.dismiss();
            }
        });
    }

    /* 添加关注接口请求*/
    private void insertAttention() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", getFromSharePreference(Config.userConfig.uid));                          //uid
        params.put("followUid", myUid);
        params.put("system_code", "android");
        Wethod.httpPost(this, Confirm_Attention, Config.web.confirm_attention, params, this,View.GONE);
    }

    /* 取消关注的 数据请求 */
    private void cancleAttention() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", getFromSharePreference(Config.userConfig.uid));                          //uid
        params.put("followUid", myUid);
        params.put("system_code", "android");
        Wethod.httpPost(this, Cancle_Attention, Config.web.cancle_attention, params, this,View.GONE);
    }

    //获取我的、关注、收藏、粉丝、条数
    private void getMemberCount() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", getFromSharePreference(Config.userConfig.uid));        //uid+""
        params.put("system_code", "android");
        params.put("otherUid", myUid);
        Wethod.httpPost(this, MEMBER_COUNT_REQUEST, Config.web.my_member_count, params, this ,View.GONE);
    }

    //获取签名和背景图
    private void requestBgPic() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", myUid);
        Wethod.httpPost(this, REQUEST_PIC, Config.web.request_bg_pic, params, this,View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //修改签名完成后 请求签名接口
        requestBgPic();
        //刷新个数
        getMemberCount();
        refreshFragmentData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshReceiver);
    }

    class RefreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("RESULT_OK".equals(intent.getAction())) {
                //发布动态成功之后刷新主页数据
                mFragments.get(tabPosition).afterInitView();
                //刷新个数
                getMemberCount();
            }
        }
    }
}



