package com.bjypt.vipcard.activity.shangfeng.primary.lifeservice.contract;


import com.bjypt.vipcard.activity.shangfeng.base.BaseContract;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */

public interface LifeServiceContract {
    interface view extends BaseContract.View{
        //显示数据
        void addDatas(List<BannerBean> data);
    }

    interface presenter extends BaseContract.Presenter{

        void loadLifeServiceData();
    }
}
