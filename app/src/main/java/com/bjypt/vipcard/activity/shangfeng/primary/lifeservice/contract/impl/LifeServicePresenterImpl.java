package com.bjypt.vipcard.activity.shangfeng.primary.lifeservice.contract.impl;


import com.bjypt.vipcard.activity.shangfeng.base.impl.BasePresenterImpl;
import com.bjypt.vipcard.activity.shangfeng.data.RequestCallBack;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;
import com.bjypt.vipcard.activity.shangfeng.data.interactor.impl.LifeServiceModelImpl;
import com.bjypt.vipcard.activity.shangfeng.primary.lifeservice.contract.LifeServiceContract;

import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */

public class LifeServicePresenterImpl extends BasePresenterImpl<LifeServiceContract.view> implements LifeServiceContract.presenter{

    private LifeServiceModelImpl lifeServiceModel;

    public LifeServicePresenterImpl() {
        lifeServiceModel = new LifeServiceModelImpl();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void loadLifeServiceData() {

        lifeServiceModel.getLifeServiceData(new RequestCallBack<List<BannerBean>>() {

            @Override
            public void success(int resultCode, List<BannerBean> data) {
                mView.addDatas(data);
            }

            @Override
            public void onError(int resultCode, String errorMsg) {

            }
        });
    }
}
