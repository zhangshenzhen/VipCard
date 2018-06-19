package com.bjypt.vipcard.activity.shangfeng.primary.selectCity.contract;

import android.content.Context;


import com.bjypt.vipcard.activity.shangfeng.base.BaseContract;
import com.bjypt.vipcard.activity.shangfeng.data.bean.CityBean;

import java.util.List;

/**
 * 选择城市契约类
 */
public interface SelectCityContract {

    interface View extends BaseContract.View {

        void setCityList(List<CityBean> cityBeanList);


    }

    interface Presenter extends BaseContract.Presenter {

        void loadData(Context context);

    }

}
