package com.bjypt.vipcard.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.TrackCommon;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.CarouselBean;
import com.bjypt.vipcard.model.DiplaybtnBean;
import com.bjypt.vipcard.model.NewMerchantDetailBean;
import com.bjypt.vipcard.model.UserMechantInfoBean;
import com.bjypt.vipcard.receiver.Logger;
import com.bjypt.vipcard.utils.BaiduUtil;
import com.bjypt.vipcard.utils.DialogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.PermissionUtils;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.utils.StarUtils;
import com.bjypt.vipcard.utils.StatusBarUtil;
import com.bjypt.vipcard.view.ClTextView;
import com.bjypt.vipcard.view.MyScrollView;
import com.bjypt.vipcard.view.RoundImageView;
import com.brioal.adtextviewlib.entity.AdEntity;
import com.brioal.adtextviewlib.view.ADTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sinia.orderlang.utils.StringUtil;
import com.ta.utdid2.android.utils.StringUtils;

import net.frakbot.jumpingbeans.JumpingBeans;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.piwik.sdk.extra.TrackHelper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjypt.vipcard.common.Config.userConfig.pkregister;

/**
 * Created by 崔龙 on 2017/4/17.
 * 新商品详情界面
 */

public class NewSubscribeDishesActivity extends BaseActivity implements VolleyCallBack<String>, MyScrollView.ScrollViewListener {
    // 常量
    private static final int CHAIN_MORE = 101;
    private static final int MERCHANT_DETAILS = 102;
    private static final int MERCHANT_DETAILS_BALANCE = 103;
    private static final int REQUEST_CODE_CAROUSEL = 104;
    // 控件
    private ImageView iv_title_bigImg;           // 头布局样式1的图片
    private ImageView iv_now_go2;                // 头布局样式2的图片
    private ImageView iv_caipin_sub;             // 菜品图片
    private ImageView iv_huanjing_sub;           // 环境图片
    private ImageView iv_star_1;                 // 星星图片
    private ImageView iv_star_2;                 // 星星图片
    private ImageView iv_star_3;                 // 星星图片
    private ImageView iv_star_4;                 // 星星图片
    private ImageView iv_star_5;                 // 星星图片
    private ImageView iv_new_sub_back;           // 返回键
    private MyScrollView scrollView_sub;         // 自定义ScrollView
    private ADTextView mSendMore;                // 滚动TextView
    private TextView tv_title_sub;               // title中间的字
    private TextView tv_title_bigTxt_1;          // title1中间的字
    private TextView tv_title_bigTxt_2;          // title2中间的字
    private TextView tv_juli_sub_1;              // title1中间的距离
    private TextView tv_juli_sub_2;              // title2中间的距离
    private TextView tv_dizhi_sub_1;             // title1中间的地址
    private TextView tv_dizhi_sub_2;             // title2中间的地址
    private TextView tv_balance_sub;             // 商家余额
    private TextView tv_phone_number_sub;        // 电话号码
    private TextView tv_discount_sub;            // 折扣
    private TextView tv_xiangce_number_sub;      // 相册总数
    private TextView tv_jianjie_sub;             // 商家简介
    private TextView tv_zong_pingfen;            // 总评分
    private TextView tv_purchaserules_sub;       // 商家活动须知内容
    private TextView tv_caipin_sub;              // 相册第一张
    private TextView tv_huanjing_sub;            // 相册第二张
    private TextView tv_pingjia_number_sub;      // 评论数量
    private TextView tv_huoquzhong;              // 动态获取
    private LinearLayout ll_back_sub;            // 返回键
    private LinearLayout ll_recharge_sub;        // 充值
    private LinearLayout ll_now_Pay_sub;         // 买单
    private ImageView ll_call_phone_sub;      // 电话按钮
    private LinearLayout ll_more_store_sub;      // 更多分店
    private LinearLayout ll_more_services;       // 更多服务
    private RelativeLayout ll_now_go1;             // title1 去这里
    private RelativeLayout ll_now_go2;           // title2 去这里
    private LinearLayout ll_merchandise_sub;     // 商品条目
    private LinearLayout ll_discount_subitem;    // 打折条目
    private LinearLayout ll_jianjie_title;       // 简介标题
    private LinearLayout ll_jianjie_item;        // 简介内容
    private LinearLayout ll_album;               // 相册
    private LinearLayout ll_combo;               // 套餐
    private LinearLayout ll_time_sub;            // 相册父
    private LinearLayout ll_pingjia;             // 评价
    private LinearLayout ll_lubbo_sub;           // 评价
    private LinearLayout ll_evaluate;            // 内容容器
    private LinearLayout ll_mroe_pingjia;        // 更多评价
    private LinearLayout ll_yue_item;            // 余额item
    private LinearLayout ll_yue;                 // 余额
    private LinearLayout ll_combo_more;          // 查看更多套餐
    private LinearLayout ll_album_go;            // 查看更多相册
    private LinearLayout ll_purchaserules_title; // 商家活动须知标题
    private LinearLayout ll_purchaserules_sub;   // 商家活动须知内容
    private LinearLayout ll_merchant_activitys;  // 优惠券列表
    private View v_is_more;                      // 商家 上面一个线条
    private View v_is_more2;                     // 更多分店 上面一个线条
    private View v_line1;                        // 线
    private View v_line0;                        // 线
    private View v_line2;                        // 线
    private View v_line3;                        // 线
    private View v_line4;                        // 线
    private View v_line5;                        // 线
    private View v_line6;                        // 线
    private View v_line7;                        // 线
    private View v_line8;                        // 线
    private View v_line9;                        // 线
    private View v_line10;                       // 线
    private View v_line11;                       // 线
    private View v_line12;                       // 线
    private RelativeLayout rl_title_big2;        // 头布局2
    private RelativeLayout rl_title_big;         // 头布局1
    // 数据
    private String pkmuser;                      // 商家主键
    private String distance;                     // 距离
    private String merdesc = "";                 // 商家简介
    private String linkage_pkdealer;             // 连锁店主键
    private boolean display;                     // 是否显示连锁店
    private String address;                      // 地址
    private String album_total;                  // 图片总数
    private String discount1;                    // 折扣
    private String logo;                         // 是否显示头布局
    private String moreservices;                 // 提供的服务
    private List<String> opening_times;          // 底部时间
    private String switch_pay;                   // 支付功能开关
    private String switch_product;               // 商品入口开关
    private String switch_recharge;              // 充值功能开关
    // 计算
    private int height;                          // 计算的图片的高度
    // 集合
    private List<AdEntity> texts = new ArrayList<>();// 返回轮播的数据
    private List<NewMerchantDetailBean.ResultDataBean.TcsBean> tcs;
    private List<NewMerchantDetailBean.ResultDataBean.CommentsBean> comments;
    private List<NewMerchantDetailBean.ResultDataBean.AlbumRecordsBean> album_records; // 图片记录
    private List<NewMerchantDetailBean.ResultDataBean.MerchantActivitysBean> merchant_activitys; // 折扣活动&优惠券列表
    // 定位
    private double latitude;
    private double longitude;

    private String longitudeFrom;
    private String bigPic;
    private String muname;
    private String purchaserules;
    private String phone;
    private String latitudeFrom;
    private int comment_total;
    private String startLevel;

    private boolean is_half = false;//是否为五折店详情
    private String balance_mer;
    private JumpingBeans jumpingBeans;

    private String categoryId;
    private View view_v;
    private RelativeLayout rl_base;
    private NewMerchantDetailBean newMerchantDetailBeans;
    private RelativeLayout rl_bottom_btn;

    @Override
    public void setContentLayout() {
        // 设置沉浸式状态栏
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        StatusBarUtil.setImgTransparent(this);

        setContentView(R.layout.activity_new_subscribedisher);


        TrackHelper.track().screen(TrackCommon.ViewTrackMerchantDetail).title(TrackCommon.ViewTrackMerchantDetail).with(getTracker());
    }

    @Override
    public void beforeInitView() {
        is_half = (getIntent().hasExtra("is_half") && "Y".equals(getIntent().getStringExtra("is_half")));
        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryid");
        pkmuser = intent.getStringExtra("pkmuser");
        /********当扫描商家二维码进入此页面的时候，distance所传的距离为扫码所获得的数据*********/
        merdesc = intent.getStringExtra("rechargeactivity");

        iv_title_bigImg = (ImageView) findViewById(R.id.iv_title_bigImg);
        iv_new_sub_back = (ImageView) findViewById(R.id.iv_new_sub_back);
        iv_now_go2 = (ImageView) findViewById(R.id.iv_now_go2);
        iv_star_1 = (ImageView) findViewById(R.id.iv_star_1);
        iv_star_2 = (ImageView) findViewById(R.id.iv_star_2);
        iv_star_3 = (ImageView) findViewById(R.id.iv_star_3);
        iv_star_4 = (ImageView) findViewById(R.id.iv_star_4);
        iv_star_5 = (ImageView) findViewById(R.id.iv_star_5);
        iv_caipin_sub = (ImageView) findViewById(R.id.iv_caipin_sub);
        iv_huanjing_sub = (ImageView) findViewById(R.id.iv_huanjing_sub);
        tv_title_sub = (TextView) findViewById(R.id.tv_title_sub);
        tv_title_bigTxt_1 = (TextView) findViewById(R.id.tv_title_bigTxt_1);
        tv_zong_pingfen = (TextView) findViewById(R.id.tv_zong_pingfen);
        tv_title_bigTxt_2 = (TextView) findViewById(R.id.tv_title_bigTxt_2);
        tv_phone_number_sub = (TextView) findViewById(R.id.tv_phone_number_sub);
        tv_xiangce_number_sub = (TextView) findViewById(R.id.tv_xiangce_number_sub);
        tv_jianjie_sub = (TextView) findViewById(R.id.tv_jianjie_sub);
        tv_juli_sub_1 = (TextView) findViewById(R.id.tv_juli_sub_1);
        tv_juli_sub_2 = (TextView) findViewById(R.id.tv_juli_sub_2);
        tv_caipin_sub = (TextView) findViewById(R.id.tv_caipin_sub);
        tv_dizhi_sub_1 = (TextView) findViewById(R.id.tv_dizhi_sub_1);
        tv_dizhi_sub_2 = (TextView) findViewById(R.id.tv_dizhi_sub_2);
        tv_balance_sub = (TextView) findViewById(R.id.tv_balance_sub);
        tv_discount_sub = (TextView) findViewById(R.id.tv_discount_sub);
        tv_huanjing_sub = (TextView) findViewById(R.id.tv_huanjing_sub);
        tv_huoquzhong = (TextView) findViewById(R.id.tv_huoquzhong);
        tv_purchaserules_sub = (TextView) findViewById(R.id.tv_purchaserules_sub);
        tv_pingjia_number_sub = (TextView) findViewById(R.id.tv_pingjia_number_sub);
        scrollView_sub = (MyScrollView) findViewById(R.id.scrollView_sub);
        mSendMore = (ADTextView) findViewById(R.id.tv_sendMore);
        ll_purchaserules_title = (LinearLayout) findViewById(R.id.ll_purchaserules_title);
        ll_merchant_activitys = (LinearLayout) findViewById(R.id.ll_merchant_activitys);
        ll_purchaserules_sub = (LinearLayout) findViewById(R.id.ll_purchaserules_sub);
        ll_discount_subitem = (LinearLayout) findViewById(R.id.ll_discount_subitem);
        ll_merchandise_sub = (LinearLayout) findViewById(R.id.ll_merchandise_sub);
        ll_call_phone_sub = (ImageView) findViewById(R.id.ll_call_phone_sub);
        ll_more_store_sub = (LinearLayout) findViewById(R.id.ll_more_store_sub);
        ll_more_services = (LinearLayout) findViewById(R.id.ll_more_services);
        ll_jianjie_title = (LinearLayout) findViewById(R.id.ll_jianjie_title);
        ll_jianjie_item = (LinearLayout) findViewById(R.id.ll_jianjie_item);
        ll_mroe_pingjia = (LinearLayout) findViewById(R.id.ll_mroe_pingjia);
        ll_recharge_sub = (LinearLayout) findViewById(R.id.ll_recharge_sub);
        ll_now_Pay_sub = (LinearLayout) findViewById(R.id.ll_now_Pay_sub);
        rl_title_big2 = (RelativeLayout) findViewById(R.id.rl_title_big2);
        ll_combo_more = (LinearLayout) findViewById(R.id.ll_combo_more);
        rl_title_big = (RelativeLayout) findViewById(R.id.rl_title_big);
        ll_lubbo_sub = (LinearLayout) findViewById(R.id.ll_lubbo_sub);
        ll_album_go = (LinearLayout) findViewById(R.id.ll_album_go);
        ll_yue_item = (LinearLayout) findViewById(R.id.ll_yue_item);
        ll_yue = (LinearLayout) findViewById(R.id.ll_yue);
        ll_now_go2 = (RelativeLayout) findViewById(R.id.ll_now_go2);
        ll_back_sub = (LinearLayout) findViewById(R.id.ll_back_sub);
        ll_time_sub = (LinearLayout) findViewById(R.id.ll_time_sub);
        ll_evaluate = (LinearLayout) findViewById(R.id.ll_evaluate);
        ll_now_go1 = (RelativeLayout) findViewById(R.id.ll_now_go1);
        ll_pingjia = (LinearLayout) findViewById(R.id.ll_pingjia);
        ll_album = (LinearLayout) findViewById(R.id.ll_album);
        ll_combo = (LinearLayout) findViewById(R.id.ll_combo);
        v_is_more2 = findViewById(R.id.v_is_more2);
        v_is_more = findViewById(R.id.v_is_more);
        v_line11 = findViewById(R.id.v_line11);
        v_line10 = findViewById(R.id.v_line10);
        v_line12 = findViewById(R.id.v_line12);
        v_line1 = findViewById(R.id.v_line1);
        v_line2 = findViewById(R.id.v_line2);
        v_line3 = findViewById(R.id.v_line3);
        v_line4 = findViewById(R.id.v_line4);
        v_line5 = findViewById(R.id.v_line5);
        v_line6 = findViewById(R.id.v_line6);
        v_line7 = findViewById(R.id.v_line7);
        v_line8 = findViewById(R.id.v_line8);
        v_line9 = findViewById(R.id.v_line9);
        v_line0 = findViewById(R.id.v_line0);

        rl_bottom_btn = (RelativeLayout) findViewById(R.id.rl_bottom_btn);

        rl_base = (RelativeLayout) findViewById(R.id.rl_base);

        view_v = findViewById(R.id.view_v);
        iv_title_bigImg.requestFocus();
        iv_title_bigImg.setFocusable(true);
        iv_title_bigImg.setFocusableInTouchMode(true);

        /** 头条速度和样式 */
        mSendMore.setSpeed(1);
        mSendMore.setFrontColor(Color.parseColor("#f85d72"));
        mSendMore.setBackColor(Color.parseColor("#f85d72"));
        texts.add(new AdEntity("", "", ""));
        mSendMore.setTexts(texts);
        ll_yue.setVisibility(View.VISIBLE);


        ViewGroup.LayoutParams layoutParams = view_v.getLayoutParams();
        //获取屏幕尺寸，不包括虚拟功能高度
        int windowHieght = getWindowManager().getDefaultDisplay().getHeight();
        if ((getDpi() - windowHieght) > 0) {
            layoutParams.height = getNavigationBarHeight(NewSubscribeDishesActivity.this);
        }else {
            layoutParams.height = 0;
        }

        initListeners();
        // 没登录的情况下不显示余额
        isShowYe();
        // 显示是否有连锁店网络接口
        isMore();
        // 商家详情接口 merchantDetails
        merchantDetails();
        // 商家余额接口
        loadMerchantDetailsBalance();
        // 轮播接口
        getHttpCarousel();

    }

    private int getDpi() {
        int dpi = 0;
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    //获取虚拟按键的高度
    public int getNavigationBarHeight(Context context) {
        int result = 0;
        com.orhanobut.logger.Logger.e("虚拟按钮: " + checkDeviceHasNavigationBar());
        if (checkDeviceHasNavigationBar()) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
                com.orhanobut.logger.Logger.e("虚拟按钮高度:"+result);
            }
        }
        return result;
    }

    /**
     * 判断是否存在虚拟按键
     *
     * @return
     */
    public boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }


    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }


    @Override
    protected void onResume() {
        super.onResume();
        tv_huoquzhong.setVisibility(View.VISIBLE);
        ll_yue.setVisibility(View.GONE);

        jumpingBeans = JumpingBeans.with(tv_huoquzhong)
                .appendJumpingDots()
                .build();

        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                loadMerchantDetailsBalance();
                return false;
            }
        }).sendEmptyMessageDelayed(0, 3000);


    }

    @Override
    protected void onPause() {
        super.onPause();
        jumpingBeans.stopJumping();
    }

    @Override
    public void initView() {

    }

    @Override
    public void afterInitView() {
        if (is_half) {
            ll_discount_subitem.setVisibility(View.GONE);
            ll_recharge_sub.setVisibility(View.GONE);
            ll_yue_item.setVisibility(View.GONE);
            v_line2.setVisibility(View.GONE);
            v_line1.setVisibility(View.GONE);
            v_line3.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindListener() {
        ll_discount_subitem.setOnClickListener(this);
        ll_merchandise_sub.setOnClickListener(this);
        ll_more_store_sub.setOnClickListener(this);
        ll_call_phone_sub.setOnClickListener(this);
        ll_recharge_sub.setOnClickListener(this);
        ll_mroe_pingjia.setOnClickListener(this);
        ll_now_Pay_sub.setOnClickListener(this);
        ll_combo_more.setOnClickListener(this);
        ll_back_sub.setOnClickListener(this);
        ll_album_go.setOnClickListener(this);
        ll_now_go1.setOnClickListener(this);
        ll_now_go2.setOnClickListener(this);
        ll_pingjia.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_back_sub:
                // 返回键
                finish();
                break;
            case R.id.ll_more_store_sub:
                // 连锁店进入
                goMoreStore();
                break;
            case R.id.ll_now_go1:
                // 去这里
                goToHere();
                break;
            case R.id.ll_call_phone_sub:
                // 拨打电话
                isShowCallPhone();
                break;
            case R.id.ll_recharge_sub:
                // 充值
                nowRecharge();
                break;
            case R.id.ll_now_Pay_sub:
                // 买单
                nowPay();
                break;
            case R.id.ll_merchandise_sub:
                // 商品
                goMerchandise();
                break;
            case R.id.ll_discount_subitem:
                // 打折
                nowPay();
                break;
            case R.id.ll_now_go2:
                // 去这里
                goToHere();
                break;
            case R.id.ll_album_go:
                // 查看更多照片
                lookMorePhoto();
                break;
            case R.id.ll_combo_more:
                // 查看更多套餐
                lookMoreCombo();
                break;
            case R.id.ll_mroe_pingjia:
                // 更多评价
                jumpCommentActivity();
                break;
            case R.id.ll_pingjia:
                // 更多评价
                jumpCommentActivity();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {

        switch (reqcode) {
            // 是否显示更多连锁店
            case CHAIN_MORE:
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    DiplaybtnBean diplaybtnBean = objectMapper.readValue(result.toString(), DiplaybtnBean.class);
                    linkage_pkdealer = diplaybtnBean.getResultData().getLinkage_pkdealer();
                    display = diplaybtnBean.getResultData().isDisplay();
                    if (display) {
                        ll_more_store_sub.setVisibility(View.VISIBLE);
                        v_is_more2.setVisibility(View.VISIBLE);
                    } else {
                        ll_more_store_sub.setVisibility(View.GONE);
                        v_is_more2.setVisibility(View.GONE);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            // 商家详情
            case MERCHANT_DETAILS:
                Log.e("clnb521", result);
                com.orhanobut.logger.Logger.json(result);
                loadMerchantDetails(result);
                break;
            // 商家余额
            case MERCHANT_DETAILS_BALANCE:
                loadBalance(result);
                break;
            // 商家通知轮播
            case REQUEST_CODE_CAROUSEL:
                loadRequestCode(result);
                break;
        }

    }

    @Override
    public void onFailed(int reqcode, String result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {
        ViewTreeObserver vto = iv_title_bigImg.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tv_title_sub.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = iv_title_bigImg.getHeight();

                scrollView_sub.setScrollViewListener(NewSubscribeDishesActivity.this);
            }
        });
    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        int height1 = height / 2;
        if (y <= 2) {   //设置标题的背景颜色
            tv_title_sub.setBackgroundColor(Color.argb((int) 0, 235, 88, 88));
            iv_new_sub_back.setBackgroundResource(R.mipmap.new_sub_back);
        } else if (y > 0 && y <= height1) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height1;
            float alpha = (255 * scale);
            tv_title_sub.setTextColor(Color.argb((int) alpha, 255, 255, 255));
            tv_title_sub.setBackgroundColor(Color.argb((int) alpha, 82, 184, 184));
        } else {    //滑动到banner下面设置普通颜色
            tv_title_sub.setBackgroundColor(Color.parseColor("#52b8b8"));
            iv_new_sub_back.setBackgroundResource(R.mipmap.new_sub_wht);
        }
    }

    /**
     * 更多连锁店请求网络
     */
    public void isMore() {
        Map<String, String> params = new HashMap<>();
        params.put("versionCode", getVersion());
        params.put("pkmuser", pkmuser);
        Wethod.httpPost(this, CHAIN_MORE, Config.web.chain_stores_list_diplaybtn, params, this);
    }

    /**
     * 轮播请求
     *
     * @param result
     */
    private void loadRequestCode(String result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        CarouselBean carouselBean = null;
        texts.clear();
        try {
            carouselBean = objectMapper.readValue(result.toString(), CarouselBean.class);
            for (int i = 0; i < carouselBean.getResultData().size(); i++) {
                texts.add(new AdEntity(carouselBean.getResultData().get(i).getMsg(), "", ""));
            }
            if (texts.size() == 0 || texts == null || texts.equals("")) {
                texts.add(new AdEntity("", "", ""));
                ll_lubbo_sub.setVisibility(View.GONE);
            } else {
                mSendMore.setTexts(texts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商家详情请求网络
     */
    private void merchantDetails() {
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", pkmuser);
        params.put("lng", SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.lngu));
        params.put("lat", SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.latu));
        params.put("userId", getFromSharePreference(pkregister));
        if (is_half) {
            params.put("special_price", "1");
            params.put("versionCode", getVersion());
        }
        Wethod.httpPost(this, MERCHANT_DETAILS, Config.web.new_merchant_details, params, this);
    }

    /**
     * 商家详情网络加载
     *
     * @param result
     */
    private void loadMerchantDetails(String result) {
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        }
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            newMerchantDetailBeans = objectMapper.readValue(result.toString(), NewMerchantDetailBean.class);
            pkmuser = newMerchantDetailBeans.getResultData().getPkmuser();
            address = newMerchantDetailBeans.getResultData().getAddress();
            album_total = newMerchantDetailBeans.getResultData().getAlbum_total();
            discount1 = newMerchantDetailBeans.getResultData().getDiscount();
            album_records = newMerchantDetailBeans.getResultData().getAlbum_records();
            logo = newMerchantDetailBeans.getResultData().getLogo();
            merchant_activitys = newMerchantDetailBeans.getResultData().getMerchant_activitys();
            merdesc = newMerchantDetailBeans.getResultData().getMerdesc();
            moreservices = newMerchantDetailBeans.getResultData().getMoreservices();
            opening_times = newMerchantDetailBeans.getResultData().getOpening_times();
            switch_pay = newMerchantDetailBeans.getResultData().getSwitch_pay();
            switch_product = newMerchantDetailBeans.getResultData().getSwitch_product();
            switch_recharge = newMerchantDetailBeans.getResultData().getSwitch_recharge();
            muname = newMerchantDetailBeans.getResultData().getMuname();
            phone = newMerchantDetailBeans.getResultData().getPhone();
            purchaserules = newMerchantDetailBeans.getResultData().getPurchaserules();
            bigPic = newMerchantDetailBeans.getResultData().getBigPic();
            tcs = newMerchantDetailBeans.getResultData().getTcs();
            latitudeFrom = newMerchantDetailBeans.getResultData().getLatitude();
            longitudeFrom = newMerchantDetailBeans.getResultData().getLongitude();
            comment_total = newMerchantDetailBeans.getResultData().getComment_total();
            comments = newMerchantDetailBeans.getResultData().getComments();
            startLevel = newMerchantDetailBeans.getResultData().getStartLevel();
            distance = newMerchantDetailBeans.getResultData().getDistance();

            // 距离
            if (StringUtil.isEmpty(distance)) {
                tv_juli_sub_1.setText("0m");
                tv_juli_sub_2.setText("0m");
            }
            tv_juli_sub_1.setText(distance);
            tv_juli_sub_2.setText(distance);

            // 更多服务 ll_more_services
            if (StringUtil.isEmpty(moreservices)) {
                ll_more_services.setVisibility(View.GONE);
            } else {
                if (moreservices.length() >= 2) {
                    String[] split = moreservices.split(" ");
                    for (int i = 0; i < split.length; i++) {
                        LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.item_sub_service, null);
                        TextView tv_service_name = (TextView) view.findViewById(R.id.tv_service_name);
                        tv_service_name.setText(split[i]);
                        ll_more_services.addView(view);
                    }
                } else {
                    LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.item_sub_service, null);
                    TextView tv_service_name = (TextView) view.findViewById(R.id.tv_service_name);
                    tv_service_name.setText(moreservices);
                    ll_more_services.addView(view);
                }
            }

            if("0".equals(switch_pay)){
                rl_bottom_btn.setVisibility(View.GONE);
            }

            // 评论
            if (comments.size() == 0 || comments == null) {

                v_line11.setVisibility(View.GONE);
                v_line10.setVisibility(View.GONE);
                ll_mroe_pingjia.setVisibility(View.GONE);
                ll_pingjia.setVisibility(View.GONE);

            } else {

                tv_pingjia_number_sub.setText(comment_total + "");
                tv_zong_pingfen.setText(startLevel);
//                showStar(Float.parseFloat(startLevel));
                StarUtils.showStar(Float.parseFloat(startLevel), iv_star_1, iv_star_2, iv_star_3, iv_star_4, iv_star_5);

                for (int i = 0; i < comments.size(); i++) {

                    LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.item_evaluate_sub, null);

                    String content = comments.get(i).getContent();          // 评论内容
                    String pkregister = comments.get(i).getPkregister();    // 用户主键
                    String score = comments.get(i).getScore();              // 评分
                    String replycontent = comments.get(i).getReplycontent();// 回复内容
                    String commenttime = comments.get(i).getCommenttime();  // 评论时间
                    String replytime = comments.get(i).getReplytime();      // 回复时间
                    String nickname = comments.get(i).getNickname();        // 昵称
                    String phoneno = comments.get(i).getPhoneno();          // 电话号码
                    String position = comments.get(i).getPosition();        // 头像

                    RoundImageView iv_item_icon = (RoundImageView) view.findViewById(R.id.iv_item_icon);
                    TextView tv_item_name_sub = (TextView) view.findViewById(R.id.tv_item_name_sub);
                    TextView tv_item_time = (TextView) view.findViewById(R.id.tv_item_time);
                    TextView tv_evaluate = (TextView) view.findViewById(R.id.tv_evaluate);
                    ImageView iv_star1 = (ImageView) view.findViewById(R.id.iv_star1);
                    ImageView iv_star2 = (ImageView) view.findViewById(R.id.iv_star2);
                    ImageView iv_star3 = (ImageView) view.findViewById(R.id.iv_star3);
                    ImageView iv_star4 = (ImageView) view.findViewById(R.id.iv_star4);
                    ImageView iv_star5 = (ImageView) view.findViewById(R.id.iv_star5);

                    tv_item_name_sub.setText(nickname);
                    tv_item_time.setText(commenttime);
                    tv_evaluate.setText(content);
                    ImageLoader.getInstance().displayImage(Config.web.picUrl + position, iv_item_icon, AppConfig.DEFAULT_IMG_MERCHANT_BG);
                    int num = Integer.parseInt(score);
                    StarUtils.showFiveHalfStar(num, iv_star1, iv_star2, iv_star3, iv_star4, iv_star5);
                    ll_evaluate.addView(view);
                }
            }

            // 套餐
            if (tcs == null) {
                v_line0.setVisibility(View.GONE);
                ll_combo_more.setVisibility(View.GONE);
                v_line12.setVisibility(View.GONE);
            } else {
                for (int i = 0; i < tcs.size(); i++) {
                    LinearLayout comboChild = (LinearLayout) getLayoutInflater().inflate(R.layout.item_combo, null);
                    String orginprice = tcs.get(i).getOrginprice();       // 实际价格
                    String pkproduct = tcs.get(i).getPkproduct();         // 套餐主键
                    String productImgUrl = tcs.get(i).getProductImgUrl(); // 套餐图
                    String productName = tcs.get(i).getProductName();     // 套餐名
                    String productPrice = tcs.get(i).getProductPrice();   // 套餐价格

                    TextView tv_name = (TextView) comboChild.findViewById(R.id.tv_name);
                    TextView tv_yuanjia = (TextView) comboChild.findViewById(R.id.tv_yuanjia);
                    TextView tv_menshijia = (TextView) comboChild.findViewById(R.id.tv_menshijia);
                    ImageView iv_icon_sub1 = (ImageView) comboChild.findViewById(R.id.iv_icon_sub1);

                    tv_name.setText(productName);
                    tv_yuanjia.setText(productPrice);
                    tv_menshijia.setText(orginprice);
                    if (!productImgUrl.isEmpty()) {
                        ImageLoader.getInstance().displayImage(Config.web.picUrl + productImgUrl, iv_icon_sub1, AppConfig.DEFAULT_IMG_MERCHANT_BG);
                    }

                    ll_combo.addView(comboChild);
                }

            }

            // 营业时间 tv_time_sub
            ClTextView tv = new ClTextView(this);
            tv.setMarqueeRepeatLimit(Integer.MAX_VALUE);
            tv.setFocusable(true);
            tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tv.setSingleLine();
            tv.setFocusableInTouchMode(true);
            tv.setHorizontallyScrolling(true);
            tv.setTextColor(android.graphics.Color.parseColor("#666666"));
            if (opening_times == null || opening_times.size() == 0) {
                ll_time_sub.setVisibility(View.GONE);
            } else {
                String openingtime = "";
                for (int i = 0; i < opening_times.size(); i++) {
                    openingtime += opening_times.get(i) + "，";
                }
                if (StringUtil.isNotEmpty(openingtime)) {
                    openingtime = openingtime.substring(0, openingtime.length() - 2);
                }
                tv.setText(openingtime);
                ll_time_sub.addView(tv);
            }
            // 优惠券
            for (int i = 0; i < merchant_activitys.size(); i++) {
                LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.item_new_sub, null);
                String payamount = merchant_activitys.get(i).getPayamount();               // 支付金额
                String valueamount = merchant_activitys.get(i).getValueamount();           // 抵用金额
                String remark = merchant_activitys.get(i).getRemark();                     // 描述
                String pkactivity = merchant_activitys.get(i).getPkactivity();             // 活动主键
                String pkmerchantcoupon = merchant_activitys.get(i).getPkmerchantcoupon(); // 商家优惠券主键

                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                TextView tv_payamount = (TextView) view.findViewById(R.id.tv_payamount);
                TextView tv_valueamount = (TextView) view.findViewById(R.id.tv_valueamount);
                TextView tv_payamount_red = (TextView) view.findViewById(R.id.tv_payamount_red);
                TextView tv_hengxian_sub = (TextView) view.findViewById(R.id.tv_hengxian_sub);

                tv_title.setText(muname);
                tv_payamount.setText(payamount);
                tv_valueamount.setText(valueamount);
                tv_payamount_red.setText(payamount);
                tv_hengxian_sub.setText(valueamount);
                tv_hengxian_sub.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
                view.setTag(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int) view.getTag();
                        jumpDiscountBuy(position);
                    }
                });
                ll_merchant_activitys.addView(view);
            }

            // 活动规则 purchaserules
            if (StringUtils.isEmpty(purchaserules)) {
                ll_purchaserules_sub.setVisibility(View.GONE);
                ll_purchaserules_title.setVisibility(View.GONE);
                v_line6.setVisibility(View.GONE);
                v_line7.setVisibility(View.GONE);
            } else {
                tv_purchaserules_sub.setText(purchaserules);
            }

            // 商家简介 merdesc
            if (StringUtils.isEmpty(merdesc)) {
                ll_jianjie_title.setVisibility(View.GONE);
                ll_jianjie_item.setVisibility(View.GONE);
                v_line4.setVisibility(View.GONE);
                v_line5.setVisibility(View.GONE);
            } else {
                tv_jianjie_sub.setText(merdesc);
            }

            // 商家大图
            if (bigPic != null) {
                ImageLoader.getInstance().displayImage(Config.web.picUrl + bigPic, iv_title_bigImg, AppConfig.DEFAULT_IMG_OPTIONS_AD);
            }
            if (logo != null) {
                ImageLoader.getInstance().displayImage(Config.web.picUrl + logo, iv_now_go2, AppConfig.DEFAULT_IMG_MERCHANT_BG);
            }

            if (Integer.parseInt(album_total) > 0) {
                // 图片总数
                tv_xiangce_number_sub.setText(album_total);

                // 商家相册
                if (album_records.size() > 0 && !album_records.isEmpty()) {
                    String imageurl1 = album_records.get(0).getImageurl();
                    String typename1 = album_records.get(0).getTypename();
                    if (Integer.parseInt(album_total) > 1) {
                        String imageurl2 = album_records.get(1).getImageurl();
                        String typename2 = album_records.get(1).getTypename();
                        ImageLoader.getInstance().displayImage(Config.web.picUrl + imageurl2, iv_huanjing_sub, AppConfig.DEFAULT_IMG_MERCHANT_BG);
                        tv_huanjing_sub.setText(typename2);
                    }
                    ImageLoader.getInstance().displayImage(Config.web.picUrl + imageurl1, iv_caipin_sub, AppConfig.DEFAULT_IMG_MERCHANT_BG);
                    tv_caipin_sub.setText(typename1);
                }
            } else {
                ll_album_go.setVisibility(View.GONE);
                ll_album.setVisibility(View.GONE);
                v_line8.setVisibility(View.GONE);
                v_line9.setVisibility(View.GONE);
            }

            // 标题
            tv_title_sub.setText(muname);

            // 电话号码
            tv_phone_number_sub.setText(phone);

            // 折扣
            float discount = StringUtil.stringToFloat(discount1);
            if (discount > 0 && discount < 100) {
                ll_discount_subitem.setVisibility(View.VISIBLE);
                tv_discount_sub.setText((discount / 10) + "折");
                v_line3.setVisibility(View.VISIBLE);
                if (is_half) {
                    ll_discount_subitem.setVisibility(View.GONE);
                    v_line3.setVisibility(View.GONE);
                }
            } else {
                ll_discount_subitem.setVisibility(View.GONE);
                v_line3.setVisibility(View.GONE);
            }

            // 判断是否是默认商家
            if (StringUtil.isEmpty(logo)) {
                rl_title_big2.setVisibility(View.VISIBLE);
                rl_title_big.setVisibility(View.GONE);
                tv_title_bigTxt_2.setText(muname);
                tv_dizhi_sub_2.setText(address);
            } else {
                rl_title_big2.setVisibility(View.GONE);
                rl_title_big.setVisibility(View.VISIBLE);
                tv_title_bigTxt_1.setText(muname);
                tv_dizhi_sub_1.setText(address);
            }

            // 支付功能开关
            if (switch_pay.equals("1")) {
                ll_now_Pay_sub.setVisibility(View.VISIBLE);
                v_line2.setVisibility(View.VISIBLE);
                if (is_half) {
                    v_line2.setVisibility(View.GONE);
                }
            } else {
                ll_now_Pay_sub.setVisibility(View.GONE);
                v_line2.setVisibility(View.GONE);
            }

            // 充值开关
            if (switch_recharge.equals("1")) {
                ll_recharge_sub.setVisibility(View.VISIBLE);
                v_line1.setVisibility(View.VISIBLE);
                if (is_half) {
                    ll_recharge_sub.setVisibility(View.GONE);
                    v_line1.setVisibility(View.GONE);
                }
            } else {
                ll_yue_item.setVisibility(View.GONE);
                v_line2.setVisibility(View.GONE);
                ll_recharge_sub.setVisibility(View.GONE);
                v_line1.setVisibility(View.GONE);
            }

            // 商品开关
            if (switch_product.equals("1")) {
                ll_merchandise_sub.setVisibility(View.VISIBLE);
                v_is_more.setVisibility(View.VISIBLE);
            } else {
                ll_merchandise_sub.setVisibility(View.GONE);
                v_is_more.setVisibility(View.GONE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商家余额网络请求
     */
    public void loadMerchantDetailsBalance() {
        if (!this.isFinishing()) {
            Map<String, String> isShowUserMachant = new HashMap<>();
            isShowUserMachant.put("pkmuser", pkmuser);
            isShowUserMachant.put("pkregister", getFromSharePreference(pkregister));
            Wethod.httpPost(this, MERCHANT_DETAILS_BALANCE, Config.web.get_UserMerchantInfo, isShowUserMachant, this, View.GONE);
        }
    }

    /**
     * 商家通知接口请求
     */
    private void getHttpCarousel() {
        Map<String, String> params = new HashMap<>();
        params.put("pkmuser", pkmuser);
        Wethod.httpPost(this, REQUEST_CODE_CAROUSEL, Config.web.halfstore_recharge_carousel, params, this);
    }

    /**
     * 商家余额网络加载
     *
     * @param result
     */
    private void loadBalance(String result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        UserMechantInfoBean userMechantInfoBean = null;
        try {
            userMechantInfoBean = objectMapper.readValue(result.toString(), UserMechantInfoBean.class);
            tv_huoquzhong.setVisibility(View.GONE);
            ll_yue.setVisibility(View.VISIBLE);
            if (userMechantInfoBean == null || userMechantInfoBean.getResultData() == null) {
                tv_balance_sub.setText("0.00");
            } else {
                balance_mer = userMechantInfoBean.getResultData().getBalance_mer();
                tv_balance_sub.setText(balance_mer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打电话弹窗
     */
    private void isShowCallPhone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否要拨打" + tv_phone_number_sub.getText() + "？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                //拨打电话
//                Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv_phone_number_sub.getText()));
//                if (ActivityCompat.checkSelfPermission(NewSubscribeDishesActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                startActivity(intentPhone);
                dialog.dismiss();

                //先new出一个监听器，设置好监听
                DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_POSITIVE:
//                                requestPermission();
                                PermissionUtils permissionUtils = new PermissionUtils(NewSubscribeDishesActivity.this, tv_phone_number_sub.getText());
                                permissionUtils.requestPermission();
                                break;
                            case Dialog.BUTTON_NEGATIVE:
                                break;
                            case Dialog.BUTTON_NEUTRAL:
                                break;
                        }
                    }
                };
                //弹窗让用户选择，是否允许申请权限
                DialogUtil.showConfirm(NewSubscribeDishesActivity.this, "申请权限", "是否允许获取打电话权限？", dialogOnclicListener, dialogOnclicListener);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 查看更多照片
     */
    private void lookMorePhoto() {
        Intent intent = new Intent(this, MerchantPicActivity.class);
        intent.putExtra("pkmuser", pkmuser);
        startActivity(intent);
    }

    /**
     * 去这里
     */
    private void goToHere() {
        String adress = getFromSharePreference(Config.userConfig.adress);
        String mudi = address;
        latitude = Double.parseDouble(getFromSharePreference(Config.userConfig.latu));
        longitude = Double.parseDouble(getFromSharePreference(Config.userConfig.lngu));
        if (!"".equals(latitudeFrom) && !"".equals(longitudeFrom)) {
            BaiduUtil.Daozheli(this, latitude, longitude, Double.parseDouble(latitudeFrom), Double.parseDouble(longitudeFrom), adress, mudi);
        } else {
            Toast.makeText(this, "暂未获取到商家经纬度", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 更多连锁店
     */
    private void goMoreStore() {
        Intent intent = new Intent(this, MerchantListActivity.class);
        intent.putExtra("pkmuser", pkmuser);
        intent.putExtra("linkage_pkdealer", linkage_pkdealer);
        intent.putExtra("versionCode", getVersion());
        intent.putExtra("lng", getFromSharePreference(Config.userConfig.lngu));
        intent.putExtra("lat", getFromSharePreference(Config.userConfig.latu));
        startActivity(intent);
    }

    /**
     * 立即充值
     */
    private void nowRecharge() {
        if (getFromSharePreference(Config.userConfig.is_Login).equals("Y")) {
            Intent intent1 = new Intent(this, OneKeyTopupAmountActivity.class);
            intent1.putExtra("pkmuser", pkmuser);
            intent1.putExtra("muname", muname);
            intent1.putExtra("categoryid", categoryId);
            Log.e("NewSub", "nowRecharge: " + categoryId);
            intent1.putExtra("FLAG", 1);
            startActivity(intent1);
        } else {
            Intent intentLogin = new Intent(this, LoginActivity.class);
            intentLogin.putExtra("loginsss", "Y");
            startActivity(intentLogin);
        }
    }

    /**
     * 立即买单
     */
    private void nowPay() {
        if (getFromSharePreference(Config.userConfig.is_Login).equals("Y")) {
            Intent intent1 = new Intent();
            //这是写立即买单的点击
            if (is_half) {
                intent1.setClass(this, RightAwayNewActivity.class);
            } else {
                intent1.setClass(this, RightAwayActivity.class);
            }
            intent1.putExtra("muname", muname);
            intent1.putExtra("pkmuser", pkmuser);
            intent1.putExtra("merchantName", muname);
            intent1.putExtra("balance_mer", balance_mer);
            intent1.putExtra("categoryid", categoryId);
            startActivity(intent1);
        } else {
            Intent intentLogin = new Intent(this, LoginActivity.class);
            intentLogin.putExtra("loginsss", "Y");
            startActivity(intentLogin);
        }
    }

    /**
     * 查看更多套餐
     */
    private void lookMoreCombo() {
        goMerchandise();
    }

    /**
     * 进入商品界面
     */
    private void goMerchandise() {
        Intent intent01 = new Intent(this, MenuActivity.class);
        intent01.putExtra("pkmuser", pkmuser);
        startActivity(intent01);
    }

    /**
     * 跳转到优惠券购买界面
     *
     * @param position
     */
    private void jumpDiscountBuy(int position) {
        if (getFromSharePreference(Config.userConfig.is_Login).equals("Y")) {
            Intent ins = new Intent(NewSubscribeDishesActivity.this, YouhuiBuyActivity.class);
            ins.putExtra("pkmerchantcoupon", merchant_activitys.get(position).getPkmerchantcoupon());
            ins.putExtra("pkcoupon", merchant_activitys.get(position).getPkactivity());
            ins.putExtra("pkmuser", pkmuser);
            startActivity(ins);
        } else {
            Intent intent = new Intent(NewSubscribeDishesActivity.this, LoginActivity.class);
            intent.putExtra("loginsss", "Y");
            startActivity(intent);
        }
    }

    /**
     * 跳转到查看评价
     */
    private void jumpCommentActivity() {
        Intent commentIntent = new Intent(this, CommentActivity.class);
        commentIntent.putExtra("pkmuser", pkmuser);
        startActivity(commentIntent);
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 没登录的情况下不显示余额
     */
    private void isShowYe() {
        if (getFromSharePreference(Config.userConfig.is_Login).equals("Y")) {

        } else {
            ll_yue_item.setVisibility(View.GONE);
            v_line2.setVisibility(View.GONE);
        }
    }

}