package com.bjypt.vipcard.activity.shangfeng.base.impl;

import android.support.annotation.NonNull;

import com.bjypt.vipcard.activity.shangfeng.base.BaseContract;


/**
 * Created by Dell on 2018/3/22.
 */

public abstract class BasePresenterImpl<T extends BaseContract.View> implements BaseContract.Presenter {
    protected T mView;

    @Override
    public void attachView(@NonNull BaseContract.View view) {
        mView = (T) view;
    }

    @Override
    public void onDestroy() {
        mView = null;
    }



}
