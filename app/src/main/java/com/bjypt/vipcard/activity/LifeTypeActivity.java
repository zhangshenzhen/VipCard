package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.AppCategroyLifeTypeResultDataBean;
import com.bjypt.vipcard.model.GridViewTestData;
import com.bjypt.vipcard.model.LifeTypeBean;
import com.bjypt.vipcard.model.ListTestData;
import com.bjypt.vipcard.utils.PhoneCpuId;
import com.bjypt.vipcard.view.InnerGridView;
import com.bjypt.vipcard.view.categoryview.AppCategoryLifeTypeMenuView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/13 0013.
 * 首页生活服务分类
 */

public class LifeTypeActivity extends BaseActivity  {

    private RelativeLayout mBack;
    private AppCategoryLifeTypeMenuView appCategoryLifeTypeMenuView;
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_lifetype);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        mBack = (RelativeLayout) findViewById(R.id.life_type_back);
        appCategoryLifeTypeMenuView = (AppCategoryLifeTypeMenuView)findViewById(R.id.appCategoryLifeTypeMenuView);
        appCategoryLifeTypeMenuView.reload();
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.life_type_back:
                finish();
                break;
        }
    }



}
