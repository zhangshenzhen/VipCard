package com.bjypt.vipcard.activity.shangfeng.data.interactor.impl;

import com.bjypt.vipcard.activity.shangfeng.common.LocateResultFields;
import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.ResultDataBean;
import com.bjypt.vipcard.activity.shangfeng.util.ApplicationUtils;
import com.bjypt.vipcard.activity.shangfeng.util.OkHttpUtil;
import com.bjypt.vipcard.activity.shangfeng.util.SharedPreferencesUtils;
import com.bjypt.vipcard.base.MyApplication;
import com.bjypt.vipcard.common.Config;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/4/26.
 */

public class LifeServiceModelImpl {

    public void getLifeServiceData(final RequestCallBack<List<BannerBean>> callBack) {
        //网络请求
        Object city_code = SharedPreferencesUtils.get(LocateResultFields.CITY_CODE, "");
        String versionCode = ApplicationUtils.getVersionName(MyApplication.getContext());
        Map<String, String> params = new HashMap<>();
        params.put("platform_type", "0");
        params.put("versioncode", versionCode);
        params.put("city_code", city_code + "");
        params.put("app_type", "2");

        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
            @Override
            public void success(int resultCode, ResultDataBean data) {
                Gson gson = new Gson();
                List<BannerBean> resultDataBeans = gson.fromJson(gson.toJson(data.getResultData()), new TypeToken<ArrayList<BannerBean>>() {
                }.getType());
                callBack.success(resultCode, resultDataBeans);
            }

            @Override
            public void onError(int resultCode, String errorMsg) {

            }
        }, Config.web.LIFE_SERVICE, params);

    }

}
