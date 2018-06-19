package com.bjypt.vipcard.activity.shangfeng.base;

import android.support.annotation.NonNull;

/**
 * Created by Dell on 2018/3/22.
 */

public interface BaseContract {

    interface View{
        void showProgress();

        void hideProgress();
    }

    interface Presenter {
        void onStart();

        void attachView(@NonNull View view);

        void onDestroy();

        void cancelRequest();
    }

}
