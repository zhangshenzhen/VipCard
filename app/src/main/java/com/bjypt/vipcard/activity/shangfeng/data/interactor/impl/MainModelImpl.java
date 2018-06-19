package com.bjypt.vipcard.activity.shangfeng.data.interactor.impl;

import com.bjypt.vipcard.activity.shangfeng.data.BaseRequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MenuBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MenuSecondLevelBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MerchantListBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.PageBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.ResultDataBean;
import com.bjypt.vipcard.activity.shangfeng.data.interactor.MainInteractor;
import com.bjypt.vipcard.common.Config;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 主页实际请求类
 */
public class MainModelImpl implements MainInteractor {

    @Override
    public void loadMenuData(final RequestCallBack callBack, String versioncode, String city_code, String app_type) {
        OkHttpUtils
                .post()
                .url(Config.web.MENU_URL)
                .addParams("platform_type", city_code)
                .addParams("versioncode", versioncode)
//                .addParams("city_code", "1.0.0")
//                .addParams("app_type", app_type)
                .addHeader("versionInt", "1.0.0")
                .build()
                .execute(new BaseRequestCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.e("resultStatus = " + id + " ; " + "Exception = " + e.getMessage());
                        callBack.onError(id, e.getMessage());
                    }

                    @Override
                    public void onResponse(ResultDataBean response, int id) {
                        Gson gson = new Gson();
                        MenuSecondLevelBean menuSecondLevelBean = gson.fromJson(gson.toJson(response.getResultData()), MenuSecondLevelBean.class);
                        callBack.success(id, menuSecondLevelBean);
                    }
                });
    }

    @Override
    public List<BannerBean> getBannerData(MenuSecondLevelBean menuSecondLevelBean) {
        Gson gson = new Gson();
        List<BannerBean> bannerBeans = gson.fromJson(gson.toJson(menuSecondLevelBean.getBanner()), new TypeToken<ArrayList<BannerBean>>() {
        }.getType());
        return bannerBeans;
    }

    @Override
    public List<MenuBean> getMenuDtat(MenuSecondLevelBean menuSecondLevelBean) {
        Gson gson = new Gson();
        List<MenuBean> menuBeans = gson.fromJson(gson.toJson(menuSecondLevelBean.getMenu()), new TypeToken<ArrayList<MenuBean>>() {
        }.getType());
        return menuBeans;
    }

    @Override
    public void loadMerchantList(final RequestCallBack callBack, String cityCode, String latitude, String longitude, String pageNo, String pageSize) {
        OkHttpUtils
                .post()
                .url(Config.web.MERCHANTS_URL)
//                .addHeader("versionInt", "1.0.0")
                .addParams("cityCode", cityCode)
                .addParams("latitude", latitude)
                .addParams("longitude", longitude)
                .addParams("pageNo", pageNo)
                .addParams("pageSize", pageSize)
                .build()
                .execute(new BaseRequestCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.e("resultStatus = " + id + " ; " + "Exception = " + e.getMessage());
                        callBack.onError(id, e.getMessage());
                    }

                    @Override
                    public void onResponse(ResultDataBean response, int id) {

                        Gson gson = new Gson();
                        PageBean pageBean = gson.fromJson(gson.toJson(response.getResultData()), PageBean.class);
                        callBack.success(id, pageBean);
                    }
                });
    }

    @Override
    public List<MerchantListBean> getMerchants(PageBean pageBean) {
        Gson gson = new Gson();
        List<MerchantListBean> merchantListBeans = gson.fromJson(gson.toJson(pageBean.getList()), new TypeToken<ArrayList<MerchantListBean>>() {
        }.getType());
        return merchantListBeans;
    }

//    @Override
//    public void loadVersionMessage(final RequestCallBack callBack, int type) {
//        OkHttpUtils
//                .post()
//                .url(Config.UPDATE_APK_URL)
//                .addParams("type", String.valueOf(type))
//                .build()
//                .execute(new BaseRequestCallBack() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        callBack.onError(id, e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(ResultDataBean response, int id) {
//
//                        Gson gson = new Gson();
//                        UpdateMessageBean updateMessageBean = gson.fromJson(gson.toJson(response.getResultData()), UpdateMessageBean.class);
//                        callBack.success(id, updateMessageBean);
//                    }
//                });
//    }


}
