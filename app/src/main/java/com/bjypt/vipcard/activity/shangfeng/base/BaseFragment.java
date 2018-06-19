package com.bjypt.vipcard.activity.shangfeng.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Dell on 2018/3/22.
 */

public abstract class BaseFragment extends Fragment {

    protected BaseContract.Presenter superPresenter;

    public abstract BaseContract.Presenter getBasePresenter();

    public abstract void initInjector();

    public abstract int getLayoutId();

    public abstract void init(View view);

    public View mFragmentView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        if (superPresenter != null) {
            superPresenter.onStart();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, mFragmentView);

        init(mFragmentView);
        return mFragmentView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (superPresenter != null) {
            superPresenter.onDestroy();
        }
    }
}
