package com.bjypt.vipcard.base;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.utils.SystemBarUtil;

import java.util.Calendar;

/**
 * Created by 崔龙 on 2017/11/24.
 * <p>
 * tabFragment基类
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected View mRootView;

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    long lastClickTime = 0;
    protected Activity mActivity; // 给子类用的

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    /**
     * 存储数据到SharePreference
     **/
    public void saveDataToSharePreference(String key, String value) {
        SharedPreferenceUtils.saveToSharedPreference(getActivity(), key, value);
    }

    /**
     * 丛SharePreference中获取数据
     **/
    public String getFromSharePreference(String key) {
        return SharedPreferenceUtils.getFromSharedPreference(getActivity(), key);
    }

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onClickEvent(v);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRootView = getView();
        beforeInitView();
        initView();
        afterInitView();
        bindListener();
    }

//    protected boolean isApplyKitKatTranslucency() {
//        return true;
//    }
//
//    protected boolean isApplyColorPrimary() {
//        return true;
//    }
//
//    private void setSystemBarTintDrawable(Drawable tintDrawable) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            SystemBarUtil mTintManager = new SystemBarUtil(getActivity());
//            if (tintDrawable != null) {
//                mTintManager.setStatusBarTintEnabled(true);
//                mTintManager.setTintDrawable(tintDrawable);
//            } else {
//                mTintManager.setStatusBarTintEnabled(false);
//                mTintManager.setTintDrawable(null);
//            }
//        }
//    }
//
//    protected void setTranslucentStatus(boolean on) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window win = getActivity().getWindow();
//            WindowManager.LayoutParams winParams = win.getAttributes();
//            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//            if (on) {
//                winParams.flags |= bits;
//            } else {
//                winParams.flags &= ~bits;
//            }
//            win.setAttributes(winParams);
//        }
//    }

    /**
     * 在实例化布局之前处理的逻辑
     */
    public abstract void beforeInitView();

    /**
     * 实例化布局文件/组件
     */
    public abstract void initView();

    /**
     * 在实例化之后处理的逻辑
     */
    public abstract void afterInitView();

    /**
     * 绑定监听事件
     */
    public abstract void bindListener();

    /**
     * 点击事件
     **/
    public abstract void onClickEvent(View v);

    /**
     * 利用反射获取状态栏高度
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
