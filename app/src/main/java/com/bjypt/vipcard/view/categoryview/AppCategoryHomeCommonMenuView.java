package com.bjypt.vipcard.view.categoryview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.adapter.InnGridViewAdapter;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.model.AppCategoryBean;
import com.bjypt.vipcard.model.AppCategroyResultDataBean;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.InnerGridView;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanglei on 2017/12/28.
 * 常用功能菜单
 */

public class AppCategoryHomeCommonMenuView extends AppCategoryContextView {

    InnerGridView igv_new_home;
    AppCategroyResultDataBean appCategroyResultDataBean;
    InnGridViewAdapter innGridViewAdapter;

    public AppCategoryHomeCommonMenuView(Context context) {
        super(context);
    }

    public AppCategoryHomeCommonMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AppCategoryHomeCommonMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AppCategoryHomeCommonMenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_app_category_commonmenu_view, this);
        igv_new_home = (InnerGridView) findViewById(R.id.Igv_new_home);
    }

    @Override
    public AppCategoryBean getItemBean(int postion) {
        if(appCategroyResultDataBean != null && appCategroyResultDataBean.getResultData() != null && postion < appCategroyResultDataBean.getResultData().size()){
            return appCategroyResultDataBean.getResultData().get(postion);
        }else{
            return null;
        }

    }

    @Override
    public void httpGetData() {
        Map<String, String> params = new HashMap<>();
        params.put("city_code", SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.userConfig.citycode, "1558"));
        params.put("mtlevel", "2");
        startLoading(Config.web.application_common_menu, params);
    }

    @Override
    public void initCategoryView(String result) {
        LogUtil.debugPrint("app commonView:" + result);
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            appCategroyResultDataBean = objectMapper.readValue(result.toString(), AppCategroyResultDataBean.class);
            innGridViewAdapter = new InnGridViewAdapter(getContext(), appCategroyResultDataBean.getResultData());
            igv_new_home.setAdapter(innGridViewAdapter);
            igv_new_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onAppCategoryItemClick(getItemBean(position));
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
