package com.bjypt.vipcard.activity.shangfeng.primary.selectCity.contract.impl;

import android.content.Context;

import com.bjypt.vipcard.activity.shangfeng.base.impl.BasePresenterImpl;
import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CityBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.ResultDataBean;
import com.bjypt.vipcard.activity.shangfeng.primary.selectCity.contract.SelectCityContract;
import com.bjypt.vipcard.activity.shangfeng.util.OkHttpUtil;
import com.bjypt.vipcard.common.Config;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市列表 实现类
 */
public class SelectCityPresenterImpl extends BasePresenterImpl<SelectCityContract.View> implements SelectCityContract.Presenter {


    @Override
    public void onStart() {

    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void loadData(Context context) {
        mView.showProgress();
        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
            @Override
            public void success(int resultCode, ResultDataBean data) {
                if (mView != null) {
                    Gson gson = new Gson();
                    List<CityBean> cityBeans = gson.fromJson(gson.toJson(data.getResultData()), new TypeToken<ArrayList<CityBean>>() {
                    }.getType());
                    if (cityBeans != null && cityBeans.size() > 0) {
                        mView.setCityList(cityBeans);
                    }
                    mView.hideProgress();
                }
            }

            @Override
            public void onError(int resultCode, String errorMsg) {
                Logger.e(errorMsg);
                if (mView != null) {
                    mView.hideProgress();
                }
            }
        }, Config.web.SELECT_CITY_URL, null);
    }
}
