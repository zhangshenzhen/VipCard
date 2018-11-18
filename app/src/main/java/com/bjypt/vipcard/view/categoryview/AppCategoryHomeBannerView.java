package com.bjypt.vipcard.view.categoryview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.model.AppCategoryBean;
import com.bjypt.vipcard.model.AppCategroyResultDataBean;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.widget.FlyBanner;

import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglei on 2017/12/26.
 */

public class AppCategoryHomeBannerView extends AppCategoryContextView{

    private ArrayList<String> adv_list = new ArrayList<>();
    private FlyBanner viewpager_new_home;

   public AppCategroyResultDataBean appCategroyResultDataBean;

    private int bannerType;

    public AppCategoryHomeBannerView(Context context) {
        super(context);
    }

    public AppCategoryHomeBannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AppCategoryHomeBannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_app_category_banner, this);
        if (attrs == null) {
            bannerType = 3;
        } else {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppCategoryHomeBannerView);
            bannerType = a.getInteger(R.styleable.AppCategoryHomeBannerView_bannerType, 1);
        }
        viewpager_new_home  = (FlyBanner) findViewById(R.id.viewpager_new_home);
        // 设置指示点位置
        viewpager_new_home.setPoinstPosition(2);

//        httpGetData();
    }

    @Override
    public void httpGetData() {
        Map<String, String> params = new HashMap<>();
        params.put("city_code", Config.web.cityCode);//SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.userConfig.citycode, "1558")
        params.put("app_type", bannerType+"");
//        com.orhanobut.logger.Logger.e("city_code :"+SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.userConfig.citycode, "1558"));
//        com.orhanobut.logger.Logger.e("app_type :"+bannerType);
        startLoading(Config.web.application_ad, params);
    }

    @Override
    public void initCategoryView(String result) {
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            if (adv_list.size() > 0) {
                adv_list.clear();
            }
            appCategroyResultDataBean = objectMapper.readValue(result.toString(), AppCategroyResultDataBean.class);
            for (int i = 0; i < appCategroyResultDataBean.getResultData().size(); i++) {
                adv_list.add(appCategroyResultDataBean.getResultData().get(i).getApp_icon());
            }
            if (adv_list.size() > 0) {
                viewpager_new_home.setImagesUrl(adv_list);
            } else {
                List<Integer> images = new ArrayList<>();
                images.add(R.mipmap.ad_bg);
                images.add(R.mipmap.ad_bg);
                viewpager_new_home.setImages(images);
            }

            viewpager_new_home.setOnItemClickListener(new FlyBanner.OnItemClickListener() {

                @Override
                public void onItemClick(int position) {
                    if (adv_list.size() > 0) {
                        onAppCategoryItemClick(getItemBean(position));
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AppCategoryBean getItemBean(int postion) {
        if(appCategroyResultDataBean != null && appCategroyResultDataBean.getResultData().size() > postion){
            return appCategroyResultDataBean.getResultData().get(postion);
        }
        return null;
    }
}
