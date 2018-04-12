package com.bjypt.vipcard.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseFrament;
import com.bjypt.vipcard.utils.SystemBarUtil;


/**
 * Created by 崔龙 on 2017/9/26.
 */

public class YMFragment extends BaseFrament {
    @Override
    public void beforeInitView() {

        if (isApplyKitKatTranslucency()) {
            setTranslucentStatus(true);
        }
        if (isApplyColorPrimary()) {
            setSystemBarTintDrawable(getResources().getDrawable(R.color.basic_color_primary));
        }
    }
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    protected boolean isApplyColorPrimary() {
        return true;
    }

    private void setSystemBarTintDrawable(Drawable tintDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarUtil mTintManager = new SystemBarUtil(getActivity());
            if (tintDrawable != null) {
                mTintManager.setStatusBarTintEnabled(true);
                mTintManager.setTintDrawable(tintDrawable);
            } else {
                mTintManager.setStatusBarTintEnabled(false);
                mTintManager.setTintDrawable(null);
            }
        }
    }

    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getActivity().getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }
    @Override
    public void initView() {

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }
}
