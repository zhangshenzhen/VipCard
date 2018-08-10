package com.bjypt.vipcard.view.categoryview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.HomeRecyclerViewAdapter;
import com.bjypt.vipcard.adapter.SpaceItemDecoration;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.TrackCommon;
import com.bjypt.vipcard.model.AppCategoryBean;
import com.bjypt.vipcard.model.AppCategoryLifeTypeBean;
import com.bjypt.vipcard.model.AppCategroyLifeTypeResultDataBean;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.orhanobut.logger.Logger;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglei on 2018/6/26.
 */

public class AppCategoryHomeRecyclerViewList extends AppCategoryContextView {

    //  公共服务
    private RecyclerView rcv_commonality_serve;
    //  生活服务
    private RecyclerView rcv_life_serve;
    //  交通出行
    private RecyclerView rcv_going_out;

    private TextView tv_title1;
    private TextView tv_title2;
    private TextView tv_title3;

    private List<TextView> titleTextViews = null;
    private List<RecyclerView> itemRecyclerViews = null;



    public AppCategoryHomeRecyclerViewList(Context context) {
        super(context);
    }

    public AppCategoryHomeRecyclerViewList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AppCategoryHomeRecyclerViewList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_app_category_menu_recycler_view_list, this);
//        ButterKnife.bind(this);
        rcv_commonality_serve = (RecyclerView) findViewById(R.id.rcv_commonality_serve);
        rcv_life_serve = (RecyclerView) findViewById(R.id.rcv_life_serve);
        rcv_going_out = (RecyclerView) findViewById(R.id.rcv_going_out);
        tv_title1 = (TextView) findViewById(R.id.tv_title1);
        tv_title2 = (TextView) findViewById(R.id.tv_title2);
        tv_title3 = (TextView) findViewById(R.id.tv_title3);

        initRecyclerView(rcv_commonality_serve);
        initRecyclerView(rcv_life_serve);
        initRecyclerView(rcv_going_out);

    }

    private void initListView() {
        itemRecyclerViews = new ArrayList<>();
        itemRecyclerViews.add(rcv_commonality_serve);
        itemRecyclerViews.add(rcv_life_serve);
        itemRecyclerViews.add(rcv_going_out);
        titleTextViews = new ArrayList<>();
        titleTextViews.add(tv_title1);
        titleTextViews.add(tv_title2);
        titleTextViews.add(tv_title3);
    }


    private void initRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        layoutmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置RecyclerView 布局
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(35));
    }

    @Override
    public AppCategoryBean getItemBean(int postion) {
        return null;
    }



    @Override
    public void httpGetData() {
        Map<String, String> params = new HashMap<>();
        params.put("city_code", SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.web.cityCode, Config.DEFAULT_CITY_CODE));
        params.put("mtlevel", "1");
        startLoading(Config.web.application_common_menu, params);
    }

    @Override
    public void initCategoryView(String result) {
        initListView();
        com.orhanobut.logger.Logger.e("result :" + result);
        com.orhanobut.logger.Logger.json(result);
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        if (StringUtil.isNotEmpty(result)) {
            try {
                AppCategroyLifeTypeResultDataBean appCategroyLifeTypeResultDataBean = objectMapper.readValue(result.toString(), AppCategroyLifeTypeResultDataBean.class);
                if (appCategroyLifeTypeResultDataBean != null && appCategroyLifeTypeResultDataBean.getResultData() != null) {
                    List<AppCategoryLifeTypeBean> appCategoryLifeTypeBeans = appCategroyLifeTypeResultDataBean.getResultData();
                    for (int i = 0; i < appCategoryLifeTypeBeans.size(); i++) {

                        AppCategoryLifeTypeBean appCategoryLifeTypeBean = appCategoryLifeTypeBeans.get(i);
                        Logger.d(appCategoryLifeTypeBean);
                        Logger.e(appCategoryLifeTypeBean.getApp_name());
                        titleTextViews.get(i).setText(appCategoryLifeTypeBean.getApp_name());

                        HomeRecyclerViewAdapter homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(getContext(), appCategoryLifeTypeBean.getSubLife());
                        homeRecyclerViewAdapter.setMyItemClickListener(new HomeRecyclerViewAdapter.MyItemClickListener() {
                            @Override
                            public void onItemClick(AppCategoryBean appCategoryBean) {
                                if(appCategoryBean != null){
                                    postTracker(TrackCommon.ViewTrackLifeService, appCategoryBean.getApp_name());
                                }
                                onAppCategoryItemClick(appCategoryBean);
                            }
                        });
                        itemRecyclerViews.get(i).setAdapter(homeRecyclerViewAdapter);

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
