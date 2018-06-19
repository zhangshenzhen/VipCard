package com.bjypt.vipcard.activity.shangfeng.primary.main.contract.impl;


import com.bjypt.vipcard.activity.shangfeng.base.impl.BasePresenterImpl;
import com.bjypt.vipcard.activity.shangfeng.common.EventBusMessageEvent;
import com.bjypt.vipcard.activity.shangfeng.common.enums.EventBusWhat;
import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.MenuSecondLevelBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.OrderBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.ResultDataBean;
import com.bjypt.vipcard.activity.shangfeng.data.interactor.impl.MainModelImpl;
import com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode;
import com.bjypt.vipcard.activity.shangfeng.primary.main.contract.MainContract;
import com.bjypt.vipcard.activity.shangfeng.util.OkHttpUtil;
import com.bjypt.vipcard.activity.shangfeng.util.ToastUtils;
import com.bjypt.vipcard.common.Config;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 主界面 实现类
 */
public class MainPresenterImpl extends BasePresenterImpl<MainContract.View> implements MainContract.Presenter {

    private MainModelImpl mainModel;

    @Override
    public void onStart() {
        mainModel = new MainModelImpl();
    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void getMenuData(String platform_type, String versioncode, String cityCode) {
        mView.showProgress();
        Logger.v("mView.showProgress()");
        Map<String, String> mMap = new LinkedHashMap<>();
        mMap.put("platform_type", platform_type);
        mMap.put("versioncode", versioncode);
        mMap.put("city_code", cityCode);
        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
            @Override
            public void success(int resultCode, ResultDataBean data) {
                if (mView != null) {
                    mView.hideProgress();
                    if (data != null) {
                        Gson gson = new Gson();
                        MenuSecondLevelBean menuSecondLevelBean = gson.fromJson(gson.toJson(data.getResultData()), MenuSecondLevelBean.class);
                        if (menuSecondLevelBean != null && menuSecondLevelBean.getMenu() != null) {
                            List<BannerBean> bannerBeans = gson.fromJson(gson.toJson(menuSecondLevelBean.getMenu()), new TypeToken<ArrayList<BannerBean>>() {
                            }.getType());
                            if (bannerBeans != null) {
                                mView.initMenuData(bannerBeans);
                            }
                        }
                    }
                    Logger.v("mView.hideProgress()");
                } else {
                    Logger.e(" mView = null");
                }
            }

            @Override
            public void onError(int resultCode, String errorMsg) {
                if (mView != null) {
                    mView.hideProgress();
                } else {
                    Logger.e(" mView = null");
                }
            }
        }, Config.web.MENU_URL, mMap);

    }

//    @Override
//    public void getWeather(String city) {
//        final Map<String, String> mMap = new LinkedHashMap<>();
//        mMap.put("city", city);
//        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
//            @Override
//            public void success(int resultCode, ResultDataBean data) {
//                if (data != null) {
//                    Gson gson = new Gson();
//                    WeatherBean weatherBean = gson.fromJson(gson.toJson(data.getResultData()), WeatherBean.class);
//                    if (mView != null && weatherBean != null) {
//                        mView.setWeather(weatherBean);
//                    }
//                }
//            }
//
//            @Override
//            public void onError(int resultCode, String errorMsg) {
//
//            }
//        }, Config.web.WEATHER_URL, mMap);
//    }

//
//    @Override
//    public void getVersionMessage(int type, final String currentVersionName) {
//        mainModel.loadVersionMessage(new RequestCallBack<UpdateMessageBean>() {
//            @Override
//            public void success(int resultCode, UpdateMessageBean data) {
//                if (null != data) {
//                    if (!currentVersionName.equals(data.getVersion())) {
//                        String message = data.getDesc();
//                        if (null != message) {
//                            messages = message.split(";");
//                            mView.showUpdateDialog(messages);
//                        } else {
//                            mView.showUpdateDialog(null);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onError(int resultCode, String errorMsg) {
//                Logger.e("resultStatus = " + resultCode + " ; " + "Exception = " + errorMsg);
//            }
//        }, type);
//    }

    @Override
    public void getLastOrder(String pkregister) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("pkregister", pkregister);
        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
            @Override
            public void success(int resultCode, ResultDataBean data) {
                Logger.json(new Gson().toJson(data));
                if (mView != null && data.getResultData() != null) {
                    Gson gson = new Gson();
                    OrderBean orderBean = gson.fromJson(gson.toJson(data.getResultData()), OrderBean.class);
                    if (orderBean != null){
                        mView.showLastOrderDialog(orderBean);
                    }

                }
            }

            @Override
            public void onError(int resultCode, String errorMsg) {
                Logger.e("resultCode = "+resultCode+" ; "+"errorMsg = "+errorMsg);
            }
        }, Config.web.LAST_ORDER_URL, map);
    }

    @Override
    public void cancelOrder(String pkregister, String pkmuser, int id) {
        mView.showProgress();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("pkregister", pkregister);
        map.put("pkmuser", pkmuser);
        map.put("id", String.valueOf(id));
        OkHttpUtil.postUrl(new RequestCallBack<ResultDataBean>() {
            @Override
            public void success(int resultCode, ResultDataBean data) {
                if (mView != null) {
                    if(ResultCode.SUCCESS == data.getResultStatus()){
                        EventBusMessageEvent event = new EventBusMessageEvent();
                        event.setWhat(EventBusWhat.NewOrder);
                        EventBus.getDefault().post(event);
                        ToastUtils.showToastBottom("取消成功");
                    }
                    mView.hideProgress();
                }
            }

            @Override
            public void onError(int resultCode, String errorMsg) {
                if (mView != null) {
                    mView.hideProgress();
                }
            }
        }, Config.web.CANCEL_ORDER_URL, map);
    }


}
