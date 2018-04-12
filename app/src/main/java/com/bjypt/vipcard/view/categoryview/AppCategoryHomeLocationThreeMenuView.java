package com.bjypt.vipcard.view.categoryview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.AppCategoryBean;
import com.bjypt.vipcard.model.AppCategroyResultDataBean;
import com.bjypt.vipcard.utils.DensityUtil;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglei on 2017/12/27.
 * 一行显示3个图片菜单
 */

public class AppCategoryHomeLocationThreeMenuView extends AppCategoryContextView {


    ImageView iv1;
    ImageView iv2;
    ImageView iv3;


    private AppCategroyResultDataBean appCategroyResultDataBean;

    public AppCategoryHomeLocationThreeMenuView(Context context) {
        super(context);
    }

    public AppCategoryHomeLocationThreeMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AppCategoryHomeLocationThreeMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AppCategoryHomeLocationThreeMenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_app_category_location_threemenu_view, this);
        iv1 = (ImageView)findViewById(R.id.iv1);
        iv2 = (ImageView)findViewById(R.id.iv2);
        iv3 = (ImageView)findViewById(R.id.iv3);
    }

    @Override
    public AppCategoryBean getItemBean(int postion) {
        return appCategroyResultDataBean.getResultData().get(postion);
    }

    @Override
    public void httpGetData() {

        Map<String, String> params = new HashMap<>();
        params.put("city_code", SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.userConfig.citycode, "1558"));
        params.put("app_type", "11");
        startLoading(Config.web.application_ad, params);
    }

    @Override
    public void initCategoryView(String result) {
        LogUtil.debugPrint("app locationBanner:" + result);
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            appCategroyResultDataBean = objectMapper.readValue(result.toString(), AppCategroyResultDataBean.class);

            if(appCategroyResultDataBean.getResultData() != null && appCategroyResultDataBean.getResultData().size() >0){
                ImageLoader.getInstance().displayImage(appCategroyResultDataBean.getResultData().get(0).getApp_icon(), iv1, AppConfig.DEFAULT_IMG_PRODUCT_BG);
                iv1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onAppCategoryItemClick(appCategroyResultDataBean.getResultData().get(0));
                    }
                });
                if(appCategroyResultDataBean.getResultData().size()>1){
                    ImageLoader.getInstance().displayImage(appCategroyResultDataBean.getResultData().get(1).getApp_icon(), iv2, AppConfig.DEFAULT_IMG_PRODUCT_BG);
                    iv2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onAppCategoryItemClick(appCategroyResultDataBean.getResultData().get(1));
                        }
                    });
                }
                if(appCategroyResultDataBean.getResultData().size()>2){
                    ImageLoader.getInstance().displayImage(appCategroyResultDataBean.getResultData().get(2).getApp_icon(), iv3, AppConfig.DEFAULT_IMG_PRODUCT_BG);
                    iv3.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onAppCategoryItemClick(appCategroyResultDataBean.getResultData().get(2));
                        }
                    });
                }
                setVisibility(View.VISIBLE);
            }else{
                setVisibility(View.GONE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
