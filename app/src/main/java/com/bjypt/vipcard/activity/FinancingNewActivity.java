package com.bjypt.vipcard.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseFraActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.fragment.FinancingAccountFragment;
import com.bjypt.vipcard.fragment.FinancingCalcFragment;
import com.bjypt.vipcard.fragment.FinancingProjectFragment;
import com.bjypt.vipcard.view.ToastUtil;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.gallerypick.utils.SystemBarTintManager;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class FinancingNewActivity extends BaseFraActivity implements VolleyCallBack {
    BottomNavigationBar bottomNavigationBar;
    private FinancingCalcFragment financingCalcFragment;
    private FinancingAccountFragment financingAccountFragment;
    private FinancingProjectFragment financingProjectFragment;
    String pkmuser = "";
    String canApp = "0";
    private SystemBarTintManager tintManager;
    private TextView tv_title;
    private TextView tv;

    // private ShapeBadgeItem badgeItem;
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_financing_new);
    }

    @Override
    public void beforeInitView() {
        initSystemBar();

        View statusBar = findViewById(R.id.statusBarView);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();

//        Intent intent = getIntent();
//        pkmuser = intent.getStringExtra("pkmuser");
//        com.orhanobut.logger.Logger.e("pkmuser:"+pkmuser);
    }

    public void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        // 创建状态栏的管理实例
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);// 激活状态栏设置
//        tintManager.setNavigationBarTintEnabled(true);// 激活导航栏设置
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.transparency_color));//设置状态栏颜色
//        tintManager.setStatusBarDarkMode(false, this);//false 状态栏字体颜色是白色 true 颜色是黑色
        setStatusBarDarkMode(true, this);
    }

    @Override
    public void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv = (TextView) findViewById(R.id.tv);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        //badgeItem = new BadgeItem().setBackgroundColor(Color.RED).setText("99");
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
        bottomNavigationBar.setBarBackgroundColor(R.color.red_txt);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.home_h5, "首页").setActiveColorResource(R.color.white).setInActiveColorResource(R.color.white).setInactiveIcon(ContextCompat.getDrawable(this, R.drawable.home_h5_normal)))
                .addItem(new BottomNavigationItem(R.mipmap.calculator_h5, "理财计算器").setActiveColorResource(R.color.white).setInActiveColorResource(R.color.white).setInactiveIcon(ContextCompat.getDrawable(this, R.drawable.calculator_h5_normal)))
                .addItem(new BottomNavigationItem(R.mipmap.banking_account_h5, "理财账户").setActiveColorResource(R.color.white).setInActiveColorResource(R.color.white).setInactiveIcon(ContextCompat.getDrawable(this, R.drawable.banking_account_h5_normal)))
                .setFirstSelectedPosition(0)
                .initialise(); //所有的设置需在调用该方法前完成
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int i) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                switch (i) {
                    case 0:
                        if (financingProjectFragment == null) {
                            financingProjectFragment = new FinancingProjectFragment();
                        }
                        transaction.replace(R.id.id_content, financingProjectFragment);
                        break;
                    case 1:
                        if (financingCalcFragment == null) {
                            financingCalcFragment = new FinancingCalcFragment();
                        }
                        transaction.replace(R.id.id_content, financingCalcFragment);

                        break;
                    case 2:

                        if (financingAccountFragment == null) {
                            financingAccountFragment = new FinancingAccountFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("pkmuser", pkmuser);
                            bundle.putString("appCan", canApp);
                            financingAccountFragment.setArguments(bundle);
                            Logger.e("pkmuser："+pkmuser);
                            financingAccountFragment.setOnTabClick(new FinancingAccountFragment.TabClickListener() {
                                @Override
                                public void showHidden(int state) {
                                    // 0:显示  1.隐藏
                                    switch (state){
                                        case 0:
                                            tv_title.setVisibility(View.VISIBLE);
                                            tv.setVisibility(View.VISIBLE);
                                            break;
                                        case 1:
                                            tv_title.setVisibility(View.GONE);
                                            tv.setVisibility(View.GONE);
                                            break;
                                    }
                                }
                            });
                        }
                        transaction.replace(R.id.id_content, financingAccountFragment);

                        break;
                    default:
                        break;
                }
                // 事务提交
                transaction.commit();
            }

            @Override
            public void onTabUnselected(int i) {

            }

            @Override
            public void onTabReselected(int i) {

            }
        });
        findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        TextView title = (TextView) findViewById(R.id.tv_title);
//        title.setText("钱包金融");
        //setTitle("投资理财");
    }


    @Override
    public void afterInitView() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (financingProjectFragment == null) {
            financingProjectFragment = new FinancingProjectFragment();
        }
        transaction.replace(R.id.id_content, financingProjectFragment);

        // 事务提交
        transaction.commit();
        Map<String, String> params = new HashMap<>();
//        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
//        params.put("pkmuser", pkmuser);
        Wethod.httpPost(this, 1120, Config.web.yu_e_bao_can, params, this);
    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        Logger.json(result+"");
        switch (reqcode) {
            case 1120:
                String data = new JsonParser().parse(result.toString()).getAsJsonObject().getAsJsonPrimitive("resultData").getAsString();
                canApp = data;
                break;

        }
    }


    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {
        com.orhanobut.logger.Logger.e(volleyError.toString());
        ToastUtil.showToast(this, "网络错误，请检查您的网络");
    }


//    /**
//     * 打电话给客服
//     */
//    private void veryBestCall() {
//        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case Dialog.BUTTON_POSITIVE:
//                        PermissionUtils permissionUtils = new PermissionUtils(FinancingNewActivity.this, "025-58520902");
//                        permissionUtils.requestPermission();
//                        break;
//                    case Dialog.BUTTON_NEGATIVE:
//                        break;
//                    case Dialog.BUTTON_NEUTRAL:
//                        break;
//                }
//            }
//        };
//        //弹窗让用户选择，是否允许申请权限
//        DialogUtil.showConfirm(FinancingNewActivity.this, "客服热线", "是否拨打客服热线025-58520902", dialogOnclicListener, dialogOnclicListener);
//    }


}
