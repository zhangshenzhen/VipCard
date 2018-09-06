package com.bjypt.vipcard.activity;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.common.LocateResultFields;
import com.bjypt.vipcard.activity.shangfeng.util.SharedPreferencesUtils;
import com.bjypt.vipcard.base.BaseFraActivity;
import com.bjypt.vipcard.base.MyPiwikApplication;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.TrackCommon;
import com.bjypt.vipcard.dialog.NormalDialog;
import com.bjypt.vipcard.fragment.HomeFragment;
import com.bjypt.vipcard.fragment.ManyFragment;
import com.bjypt.vipcard.fragment.MerchantFragment;
import com.bjypt.vipcard.fragment.MineFragment;
import com.bjypt.vipcard.fragment.MyFragment;
import com.bjypt.vipcard.fragment.NewHomeFrag;
import com.bjypt.vipcard.fragment.NewsFragment;
import com.bjypt.vipcard.fragment.ShopStreetFragment;
import com.bjypt.vipcard.fragment.VipTrainFragment;
import com.bjypt.vipcard.model.LocationDingweiBean;
import com.bjypt.vipcard.service.MQTTService;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.GaoDeMapLocation;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.sinia.orderlang.activity.SpecialPriceMainActivity;
import com.sinia.orderlang.fragment.SpecialPriceFra;
import com.sinia.orderlang.utils.StringUtil;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;


public class MainActivity extends BaseFraActivity {
    //测试提交代码

    private FragmentManager fragmentManager;
    private LinearLayout homeButton, merchantButton, myButton, manyButton;
    private RelativeLayout relayout_main;
    private FrameLayout id_content;
    private RelativeLayout ly_main_tab_bottom;
    private HomeFragment mHomeFra;
    private ShopStreetFragment shopStreetFragment;
    private MerchantFragment mMerchantFra;
    //    private MyFragment mMyFra;
    private ManyFragment mManyFra;
    private VipTrainFragment mVipTrain;
    private NewsFragment newsFragment;
    private SpecialPriceFra specialPriceFra;
    //    private LinearLayout  mTotalBottom; ads
    FragmentTransaction transaction;
    public static int SECKILL_FLAG_LIST = 1;
    public static int SECKILL_FLAG = 1;

    private Handler mHandler;
    private boolean isclick = true;
    public static int lang_flag_tejia = 1;
    public static int lang_flag_redpack = 1;
    //    private static int FLAG_SPECIAL_PRICE = 1;
    private ImageView iv_news_red;
    private MyReceiver myReceiver;
    private MerchantTypeActivity merchantTypeActivity;
    private NewHomeFrag newHomeFrag;
    private TextView tv_main_my_news;
    private TextView tv_main_my;
    private TextView tv_main_life_sup;
    private TextView tv_main_vip_jie;
    private TextView tv_main_home;
    private LinearLayout ly_main_new_home;
    private LinearLayout ly_vip_jie;
    private LinearLayout ly_life_sup;
    private LinearLayout ly_my;
    private RelativeLayout rl_news;

    public static int tab_select_flag = -1;         //-1 不选中Tab  tab_select_flag>=0 时选中对应的Tab
    GaoDeMapLocation gaoDeMapLocation;
    private boolean isShowingDialog = false;
    private BroadCastReceiverUtils utils;
    private MineFragment mineFragment;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_main);

        utils = new BroadCastReceiverUtils();
        merchantTypeActivity = new MerchantTypeActivity();
        gaoDeMapLocation = new GaoDeMapLocation(this);
        gaoDeMapLocation.setOnCityLocationChangeListener(new GaoDeMapLocation.OnCityLocationChangeListener() {
            @Override
            public void onChange(LocationDingweiBean ld) {
                LogUtil.debugPrint(ld.getmLat()+"");
                LogUtil.debugPrint(ld.getmLng()+"");
                if (!isShowingDialog) {
                    isShowingDialog = true;
//                    onSureChangeCityCode(ld);
                }


            }
        });
        //确认城市选择
        onSureSelectCityCode();

        gaoDeMapLocation.startLocation();
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        //   receiver = new HomeKeyEventBroadCastReceiver();
        //  registerReceiver(receiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        SECKILL_FLAG_LIST = 1;
        SECKILL_FLAG = 1;
    }

    @Override
    public void initView() {
        if (SharedPreferenceUtils.
                getFromSharedPreference(this, Config.userConfig.is_Login).equals("Y")
                && !SharedPreferenceUtils.getFromSharedPreference(this, "isstart").equals("Y")) {
            //startService(new Intent(this, MQTTService.class));
        }

        myReceiver = new MyReceiver();
        utils = new BroadCastReceiverUtils();
        utils.registerBroadCastReceiver(this, "news_red_point", myReceiver);

        /***底部按钮初始化***/
        tv_main_my_news = (TextView) findViewById(R.id.tv_main_my_news);
        tv_main_my = (TextView) findViewById(R.id.tv_main_my);
        tv_main_life_sup = (TextView) findViewById(R.id.tv_main_life_sup);
        tv_main_vip_jie = (TextView) findViewById(R.id.tv_main_vip_jie);
        tv_main_home = (TextView) findViewById(R.id.tv_main_home);
        ly_main_new_home = (LinearLayout) findViewById(R.id.ly_main_new_home);
        ly_vip_jie = (LinearLayout) findViewById(R.id.ly_vip_jie);
        rl_news = (RelativeLayout) findViewById(R.id.rl_news);
        ly_life_sup = (LinearLayout) findViewById(R.id.ly_life_sup);
        ly_my = (LinearLayout) findViewById(R.id.ly_my);
        iv_news_red = (ImageView) findViewById(R.id.iv_news_red);
        id_content = (FrameLayout) findViewById(R.id.id_content);
        fragmentManager = getSupportFragmentManager();

        setTabSelection(0);
    }

    @Override
    public void afterInitView() {
        TrackHelper.track().screen(TrackCommon.ViewTrackPagesMainTab).title("首页").with(getTracker());
    }

    private Tracker getTracker() {
        return ((MyPiwikApplication) getApplication()).getTracker();
    }

    @Override
    public void bindListener() {
        ly_main_new_home.setOnClickListener(this);
        ly_vip_jie.setOnClickListener(this);
        ly_life_sup.setOnClickListener(this);
        ly_my.setOnClickListener(this);
        rl_news.setOnClickListener(this);

    }


    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ly_main_new_home:
                setTabSelection(0);
                 break;
            case R.id.ly_vip_jie:
                setTabSelection(1);
                   break;
            case R.id.ly_life_sup:

                setTabSelection(2);
                break;
            case R.id.ly_my:
                setTabSelection(4);
                break;
            case R.id.rl_news:
                setTabSelection(3);
                break;

        }
    }

    private void onSureSelectCityCode() {
        String citycode = SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.citycode);
        if (StringUtil.isEmpty(citycode)) {
            gaoDeMapLocation.setIsSaveLng();
            gaoDeMapLocation.doSearchQuery("阜阳");
            gaoDeMapLocation.setCityChangeListener(new GaoDeMapLocation.CityChangeListener() {
                @Override
                public void onCityChangeListner() {
                    if (!isShowingDialog) {
                        isShowingDialog = true;
                        utils.sendBroadCastReceiver(MainActivity.this, "change_city", "updata", "阜阳");
//
                    }
                }
            });

        }
    }

    /**
     * 清楚掉所有选中状态
     */
    private void resetBtn() {
        tv_main_home.setTextColor(Color.parseColor("#7b7b7b"));
        tv_main_vip_jie.setTextColor(Color.parseColor("#7b7b7b"));
        tv_main_life_sup.setTextColor(Color.parseColor("#7b7b7b"));
        tv_main_my.setTextColor(Color.parseColor("#7b7b7b"));
        tv_main_my_news.setTextColor(Color.parseColor("#7b7b7b"));
        tv_main_home.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.home_normal, 0, 0);
        tv_main_vip_jie.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.store_normal, 0, 0);
        tv_main_life_sup.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.merchant_94, 0, 0);
        tv_main_my.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.my_normal, 0, 0);
        tv_main_my_news.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.news_normal, 0, 0);

    }

    public void setTabSelection(int index) {
        //重置按钮
        resetBtn();
        transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 0:
                tv_main_home.setTextColor(Color.parseColor("#52b8b8"));
                tv_main_home.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.home_first, 0, 0);
                if (newHomeFrag == null) {
                    newHomeFrag = new NewHomeFrag();
                    transaction.replace(R.id.id_content, newHomeFrag);
                } else {
                    transaction.show(newHomeFrag);
                }
//                TrackHelper.track().event(TrackCommon.ViewTrackCategroy, TrackCommon.ViewTrackPagesMainTab).name("首页").value(1f).with(getTracker());
                break;
            case 1:
                tv_main_vip_jie.setTextColor(Color.parseColor("#52b8b8"));
                tv_main_vip_jie.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.store, 0, 0);
                if (shopStreetFragment == null) {
                    shopStreetFragment = new ShopStreetFragment();
                    transaction.add(R.id.id_content, shopStreetFragment);
                } else {
                    transaction.show(shopStreetFragment);
                }
                TrackHelper.track().event(TrackCommon.ViewTrackCategroy, TrackCommon.ViewTrackPagesMerchatTab).name("铺子").value(1f).with(getTracker());
                break;
            case 2:
                tv_main_life_sup.setTextColor(Color.parseColor("#52b8b8"));
                tv_main_life_sup.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.merchant, 0, 0);
                if (!getFromSharePreference(Config.userConfig.citycode).equals("") && getFromSharePreference(Config.userConfig.citycode) != null) {
                    startActivity(new Intent(MainActivity.this, SpecialPriceMainActivity.class));
                } else {
                    Toast.makeText(this, "请先手动定位后再进入生活超市", Toast.LENGTH_SHORT).show();
                }
               break;
            case 3:
                tv_main_my_news.setTextColor(Color.parseColor("#52b8b8"));
                tv_main_my_news.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.news, 0, 0);
                if (newsFragment == null) {
//                    mManyFra = new ManyFragment();
                    newsFragment = new NewsFragment();
                    transaction.add(R.id.id_content, newsFragment);
                } else {
                    transaction.show(newsFragment);
                }
                TrackHelper.track().event(TrackCommon.ViewTrackCategroy, TrackCommon.ViewTrackPagesNewsTab).name("有料").value(1f).with(getTracker());
                break;
            case 4:
                tv_main_my.setTextColor(Color.parseColor("#52b8b8"));
                tv_main_my.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.my, 0, 0);
                if (mineFragment == null) {
//                    mMyFra = new MyFragment();
                    mineFragment = new MineFragment();
                    transaction.add(R.id.id_content, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                TrackHelper.track().event(TrackCommon.ViewTrackCategroy, TrackCommon.ViewTrackPagesMineTab).name("我的").value(1f).with(getTracker());
                break;


        }
        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (shopStreetFragment != null) {
            transaction.hide(shopStreetFragment);
        }
        if (newHomeFrag != null) {
            transaction.hide(newHomeFrag);
        }
        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }
        if (mMerchantFra != null) {
            transaction.hide(mMerchantFra);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }

    }


    private Long exitTime = 0l;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                int currentVersion = android.os.Build.VERSION.SDK_INT;
                if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    SharedPreferenceUtils.saveToSharedPreference(MainActivity.this, Config.userConfig.switch_citycode, "");
                    Log.i("gps", "citycode=4  " + getFromSharePreference(Config.userConfig.citycode));
                    System.exit(0);
                } else {// android2.1
                    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    SharedPreferenceUtils.saveToSharedPreference(MainActivity.this, Config.userConfig.switch_citycode, "");
                    Log.e("tuichu", "2");
                    am.restartPackage(getPackageName());
                }

               /* finish();
                System.exit(0);*/
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
        lang_flag_tejia = 1;
        lang_flag_redpack = 1;
        Log.e("kiss", "MainActivity onResume");

        //TODO 判断当是从同城送返回的时候加载首页Fragment
//        if(FLAG_SPECIAL_PRICE == 2){
//            Log.e("kiss","FLAG_SPECIAL_PRICE："+FLAG_SPECIAL_PRICE);
//            FLAG_SPECIAL_PRICE = 1;
////            mTotalBottom.setVisibility(View.VISIBLE);
//            setTabSelection(0);
//
//        }
        if (tab_select_flag >= 0) {
            setTabSelection(tab_select_flag);
            tab_select_flag = -1;
        }
    }

    @Override
    protected void onDestroy() {
        if (gaoDeMapLocation != null) {
            gaoDeMapLocation.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("gps", "citycode=1  " + getFromSharePreference(Config.userConfig.citycode));
        UmengCountContext.onPause(this);
    }

    class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {

        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_HOME_KEY = "homekey";//home key
        static final String SYSTEM_RECENT_APPS = "recentapps";//long home key

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason != null) {
                    if (reason.equals(SYSTEM_HOME_KEY)) {
                        // home key处理点
                      /*  finish();
                        System.exit(0);*/

                    } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                        // long home key处理点
                        int currentVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            System.exit(0);
                        } else {// android2.1
                            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                            am.restartPackage(getPackageName());
                        }
                    }
                }
            }
        }
    }


    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("news_red_point")) {
//                iv_news_red.setVisibility(View.VISIBLE);
            }
        }
    }


}
