package com.bjypt.vipcard.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;

import java.util.Calendar;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/8
 * Use by 基类Fragment
 */
public abstract  class  BaseFrament extends Fragment implements View.OnClickListener{
    protected View mRootView;
    private BroadCastReceiverUtils utils;
    private MyBroad myBroad;

    /*在实例化布局之前处理的逻辑*/
    public abstract void beforeInitView();

    /*实例化布局文件/组件*/
    public abstract void initView();

    /*在实例化之后处理的逻辑*/
    public abstract void afterInitView();

    /*绑定监听事件*/
    public abstract void bindListener();

    /**点击事件**/
    public abstract void onClickEvent(View v);

    /**存储数据到SharePreference**/
    public  void saveDataToSharePreference(String key,String value){
        SharedPreferenceUtils.saveToSharedPreference(getActivity(), key, value);
    }

    /**丛SharePreference中获取数据**/
    public String getFromSharePreference(String key){
        return SharedPreferenceUtils.getFromSharedPreference(getActivity(),key);
    }

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    long lastClickTime = 0;
    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onClickEvent(v);
        }

    }

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

    public void isConntectedAndRefreshData(){

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        utils = new BroadCastReceiverUtils();
        myBroad = new MyBroad();
        utils.registerBroadCastReceiver(getActivity(),"re_dingwei",myBroad);
        mRootView = getView();
        beforeInitView();
        initView();
        afterInitView();
        bindListener();
    }
    class MyBroad extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            isConntectedAndRefreshData();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        utils.UnRegisterBroadCastReceiver(getActivity(),myBroad);
    }
}
