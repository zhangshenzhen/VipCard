package com.bjypt.vipcard.view.categoryview;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.activity.shangfeng.common.LocateResultFields;
import com.bjypt.vipcard.activity.shangfeng.common.enums.UserInformationFields;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CommonWebData;
import com.bjypt.vipcard.activity.shangfeng.primary.commonweb.CommonWebActivity;
import com.bjypt.vipcard.activity.shangfeng.util.SharedPreferencesUtils;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.base.MyPiwikApplication;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.TrackCommon;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.AppCategoryBean;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.ToastUtil;
import com.sinia.orderlang.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wanglei on 2017/12/26.
 */

public abstract class AppCategoryContextView extends LinearLayout implements VolleyCallBack<String> {

    private static final int request_code_data = 1;
    private OnloadCompleteListener onloadCompleteListener;

    public AppCategoryContextView(Context context) {
        super(context);
    }

    public AppCategoryContextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AppCategoryContextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AppCategoryContextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 开始加载数据
     *
     * @param method
     * @param params
     */
    public void startLoading(String method, Map<String, String> params) {
        Wethod.httpPost(getContext(), request_code_data, method, params, this, View.GONE);
    }


    public abstract void initView(Context context, AttributeSet attrs);


    private void init(Context context, AttributeSet attrs){
        initView(context, attrs);
//        httpGetData();
    }


    public void reload() {
        httpGetData();
    }

    /**
     * 获取对象信息
     *
     * @param postion
     * @return
     */
    public abstract AppCategoryBean getItemBean(int postion);


    public abstract void httpGetData();

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == request_code_data) {
            initCategoryView(result);
            if(onloadCompleteListener != null){
//                Logger.json(result);
                onloadCompleteListener.onComplete(result);
            }
        }
    }


    public abstract void initCategoryView(String result);


    @Override
    public void onFailed(int reqcode, String result) {
        LogUtil.debugPrint("网络请求失败：reqcode=" + reqcode + "  result=" + result);
    }

    @Override
    public void onError(VolleyError volleyError) {
        LogUtil.debugPrint("网络请求错误：" + volleyError.getMessage());
        if(onloadCompleteListener != null){
            onloadCompleteListener.onError(volleyError);
        }
    }

    /**
     * 设置点击事件
     * @return
     */
    public void onAppCategoryItemClick(AppCategoryBean appCategoryBean) {
//        AppCategoryBean appCategoryBean = getItemBean(position);
        if (appCategoryBean != null) {
            if(appCategoryBean.getIsentry() == AppCategoryBean.LoginTypeEnum.Login.getValue() && !"Y".equals(SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.userConfig.is_Login))){
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
            if (appCategoryBean.getLink_type() == AppCategoryBean.ActionTypeEnum.H5.getValue()) {
                String url = appCategoryBean.getLink_url();
                if (appCategoryBean.getIsentry() == AppCategoryBean.LoginTypeEnum.Login.getValue()) {
                    if (!url.endsWith("pkregister=") ) {
                        url = url + "pkregister=";
                    }
                    url = url + SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.userConfig.pkregister);
                    url = url + "&phoneno=" + SharedPreferencesUtils.get(UserInformationFields.PHONE_NUMBER, "");
                }
                if(appCategoryBean.getLink_url().contains("alipays://platformapi")){
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appCategoryBean.getLink_url()));
                        getContext().startActivity(intent);
                        return;
                    }catch (ActivityNotFoundException e){
                        e.printStackTrace();
                        ToastUtil.showToast(getContext(), "该功能需跳转支付宝");
                    }
                }
                if(url.startsWith(Config.web.shangfengh5)){
                    if (!url.endsWith("&")) {
                        url = url + "&";
                    }
                    url = url + "citycode=" + SharedPreferencesUtils.get(LocateResultFields.CITY_CODE, "");
                    url = url + "&latitude=" + SharedPreferenceUtils.getFromSharedPreference(MyApplication.getContext(), Config.userConfig.CURRENT_LATU);
                    url = url + "&longitude=" + SharedPreferenceUtils.getFromSharedPreference(MyApplication.getContext(), Config.userConfig.CURRENT_LNGU);
                    Log.i("连接", "#########" + url);
                    CommonWebData commonWebData = new CommonWebData(url, appCategoryBean.getApp_name());
                    CommonWebActivity.callActivity(getContext(), commonWebData );
                }else {
                    Intent intent = new Intent(getContext(), LifeServireH5Activity.class);
                    Log.i("连接", appCategoryBean.getApp_name()+"#########:" + url);
                    intent.putExtra("life_url", url);
                    intent.putExtra("isLogin", "N");
                    intent.putExtra("serviceName", appCategoryBean.getApp_name());
                    getContext().startActivity(intent);
                }

            } else if (appCategoryBean.getLink_type() == AppCategoryBean.ActionTypeEnum.Native.getValue()) {
                if (StringUtil.isEmpty(appCategoryBean.getAndroid_native_url())) {
                    ToastUtil.showToast(getContext(), "暂未开通");
                } else {
                    nativeHandler(appCategoryBean);
                }
            }else if(appCategoryBean.getLink_type() == AppCategoryBean.ActionTypeEnum.NoAction.getValue()){
                ToastUtil.showToast(getContext(), "暂未开通");
            }
        }
    }
    //http://sun.hybjiekou.com/hyb_ct_h5app/#/schoollist?pkregister=cbe515ddf7c04f958ab0c19709efbff5&phoneno=15151962093
    private void nativeHandler(AppCategoryBean appCategoryBean) {
        Intent intent = new Intent();
        Class aClass = null;
        try {
            aClass = Class.forName(appCategoryBean.getAndroid_native_url());
            intent.setClass(getContext(), aClass);
            if (getContext().getPackageManager().resolveActivity(intent, 0) == null) {
                // 说明系统中不存在这个activity
                ToastUtil.showToast(getContext(), "暂未开通");
            } else {
                if(StringUtil.isNotEmpty(appCategoryBean.getNative_params())){
                    JSONObject params = new JSONObject(appCategoryBean.getNative_params());
                    Iterator<String> iterator = params.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        Object value = params.get(key);
                        if (value instanceof String) {
                            intent.putExtra(key, value.toString());
                        } else if (value instanceof Integer) {
                            intent.putExtra(key, (Integer) value);
                        } else {
                            JSONArray jsonArray = (JSONArray) value;
                            if (jsonArray != null) {
                                ArrayList<String> item = new ArrayList<String>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    item.add(jsonArray.getString(i));
                                }
                                intent.putStringArrayListExtra(key, item);
                            }
                        }
                        //do something
                    }
                }
                getContext().startActivity(intent);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "暂未开通");
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "暂未开通");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setOnloadCompleteListener(OnloadCompleteListener onloadCompleteListener) {
        this.onloadCompleteListener = onloadCompleteListener;
    }

    public interface   OnloadCompleteListener{
        void onComplete(String result);
        void onError(VolleyError volleyError);
    }


    public Tracker getTracker() {
        return MyApplication.getInstance().getTracker();
    }
//
    public void postTracker(String actionName, String name){

        TrackHelper.track().event(TrackCommon.ViewTrackCategroy, actionName).name(name).value(1f).with(getTracker());

    }

}