package com.bjypt.vipcard.activity.shangfeng.primary.lifeservice.ui;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.adapter.LifeServiceGVAdapter;
import com.bjypt.vipcard.activity.shangfeng.base.BaseActivity;
import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;
import com.bjypt.vipcard.activity.shangfeng.primary.lifeservice.contract.LifeServiceContract;
import com.bjypt.vipcard.activity.shangfeng.primary.lifeservice.contract.impl.LifeServicePresenterImpl;
import com.bjypt.vipcard.activity.shangfeng.util.ShangfengUriHelper;
import com.bjypt.vipcard.activity.shangfeng.util.ToastUtils;
import com.githang.statusbar.StatusBarCompat;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LifeServiceActivity extends BaseActivity implements LifeServiceContract.view {

    @BindView(R.id.ll_life_service_back)
    LinearLayout ll_life_service_back;

    @BindView(R.id.gv_life_service)
    GridView gv_life_service;

    private LifeServiceGVAdapter lifeServiceGVAdapter;
    private LifeServicePresenterImpl lifeServicePresenter;
    private List<BannerBean> resultDataBeans;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shangfeng_life_service;
    }

    @Override
    protected void initInjector() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));

        lifeServicePresenter = new LifeServicePresenterImpl();
        lifeServicePresenter.attachView(this);
        superPresenter = lifeServicePresenter;
    }

    @Override
    protected void init() {

        lifeServicePresenter.loadLifeServiceData();

        gv_life_service.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BannerBean bean = resultDataBeans.get(position);
                ShangfengUriHelper shangfengUriHelper = new ShangfengUriHelper(LifeServiceActivity.this);
                shangfengUriHelper.onAppCategoryItemClick(bean, true);

            }
        });
    }


    @OnClick(R.id.ll_life_service_back)
    public void viewClick() {
        finish();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void addDatas(List<BannerBean> data) {
        if(data == null){
            ToastUtils.showToastBottom("暂无可用的生活服务");
            return;
        }
        resultDataBeans = data;
        lifeServiceGVAdapter = new LifeServiceGVAdapter(this, data);
        gv_life_service.setAdapter(lifeServiceGVAdapter);
    }
}
