package com.bjypt.vipcard.activity.shangfeng.primary.main.contract;


import com.bjypt.vipcard.activity.shangfeng.base.BaseContract;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.OrderBean;
import com.bjypt.vipcard.activity.shangfeng.data.bean.WeatherBean;

import java.util.List;

/**
 * 主界面契约类
 */
public interface MainContract {
    interface View extends BaseContract.View {

//        /**
//         * 设置天气数据
//         */
//        void setWeather(WeatherBean weather);

        /**
         * 初始化 菜单数据
         * @param bannerBeanList
         */
        void initMenuData(List<BannerBean> bannerBeanList);

        /**
         * 版本更新提示
         * @param messages
         */
//        void showUpdateDialog(String[] messages);

        /**
         * 显示最近一笔未完成订单信息
         */
        void showLastOrderDialog(OrderBean orderBean);


    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 获取 banner、菜单数据
         * @param platform_type 设备类型 0: Android 1: IOS 2: ALL
         * @param versioncode  版本号
         */
        void getMenuData(String platform_type, String versioncode, String cityCode);

        /**
         * 获取天气
         * @param city 城市
         */
//        void getWeather(String city);

        /**
         * 版本校验
         * @param type
         */
//        void getVersionMessage(int type, String currentVersionName);

        /**
         * 获取最后一笔未完成订单
         * @param pkregister
         */
        void getLastOrder(String pkregister);

        /**
         * 取消订单
         * @param pkregister
         * @param pkmuser
         * @param id
         */
        void cancelOrder(String pkregister, String pkmuser, int id);

    }
}

