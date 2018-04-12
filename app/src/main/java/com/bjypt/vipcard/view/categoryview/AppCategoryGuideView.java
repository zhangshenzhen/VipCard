package com.bjypt.vipcard.view.categoryview;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.FirstActivity;
import com.bjypt.vipcard.activity.MainActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.AppCategoryBean;
import com.bjypt.vipcard.model.AppCategroyResultDataBean;
import com.bjypt.vipcard.utils.ImageUtils;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.recker.flybanner.FlyBanner;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglei on 2018/1/11.
 */

public class AppCategoryGuideView extends AppCategoryContextView{


    private ViewPager viewPage;
    // 图片
    //    private int[] imageView = { R.drawable.welcome, R.drawable.welcome,
    //            R.drawable.welcome, R.drawable.welcome };
//    private List<String> imageView = new ArrayList<>();

    private List<View> list = new ArrayList<>();
    // 底部小点的图片
    private LinearLayout llPoint;
    //立即进入按钮
    private TextView textView;
    //跳过引导页
    private ImageLoader imageLoader;
    AppCategroyResultDataBean appCategroyResultDataBean;
    private int currentItem;
    private Runnable runnable;
    private boolean isLooper = true;
    OnChangePageLastListener onChangePageLastListener;

    public AppCategoryGuideView(Context context) {
        super(context);
    }

    public AppCategoryGuideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AppCategoryGuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AppCategoryGuideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.view_app_category_guide, this);
        viewPage = (ViewPager) findViewById(R.id.viewpage);
        llPoint = (LinearLayout) findViewById(R.id.llPoint);
        imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            ImageUtils.initImageLoader(context);
        }
    }

    @Override
    public AppCategoryBean getItemBean(int postion) {
        return null;
    }

    @Override
    public void httpGetData() {
        Map<String, String> params = new HashMap<>();
        params.put("city_code", SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.userConfig.citycode, "1558"));
        params.put("app_type", "4");
        startLoading(Config.web.application_ad, params);
    }

    @Override
    public void initCategoryView(String result) {
        LogUtil.debugPrint("app guideView:" + result);
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            appCategroyResultDataBean = objectMapper.readValue(result.toString(), AppCategroyResultDataBean.class);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            for (int i = 0; i < appCategroyResultDataBean.getResultData().size(); i++) {
                ImageView iv = new ImageView(getContext());
                iv.setLayoutParams(params);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                ImageLoader.getInstance().displayImage(appCategroyResultDataBean.getResultData().get(i).getApp_icon(), iv, AppConfig.DEFAULT_IMG_OPTIONS_WELCOME);
                list.add(iv);
            }
            // 加入适配器
            viewPage.setAdapter(new GuideViewAdapter(list));
            viewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentItem = position;
                    monitorPoint(position);

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


            addPoint();

            runnable = new Runnable() {
                @Override
                public void run() {

                    if (appCategroyResultDataBean.getResultData().size() == 0 || currentItem == appCategroyResultDataBean.getResultData().size() - 1) {
                        viewPage.setCurrentItem(currentItem);
                        if (SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.userConfig.citycode) != null && isLooper) {
                            if(onChangePageLastListener != null){
                                onChangePageLastListener.onLastEvent();
                            }

                        }
                        return;
                    }
                    currentItem++;
                    viewPage.setCurrentItem(currentItem);
                    viewPage.postDelayed(runnable, 3000);
                }
            };
            viewPage.postDelayed(runnable, 3000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnChangePageLastListener(OnChangePageLastListener onChangePageLastListener) {
        this.onChangePageLastListener = onChangePageLastListener;
    }

    public  interface OnChangePageLastListener{
        public void onLastEvent();
    }

    public class GuideViewAdapter extends PagerAdapter {

        private List<View> list;

        public GuideViewAdapter(List<View> list) {
            this.list = list;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(list.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }
    }

    /**
     * 判断小圆点
     * @param position
     */
    private void monitorPoint(int position) {
        if (appCategroyResultDataBean.getResultData().size() > 0) {
            for (int i = 0; i < appCategroyResultDataBean.getResultData().size(); i++) {
                if (i == position) {
                    llPoint.getChildAt(position).setBackgroundResource(
                            R.mipmap.first_click);
                } else {
                    llPoint.getChildAt(i).setBackgroundResource(
                            R.mipmap.first_noclick);
                }
            }
        }
    }

    /**
     * 添加小圆点
     */
    private void addPoint() {
        // 1.根据图片多少，添加多少小圆点
        for (int i = 0; i < appCategroyResultDataBean.getResultData().size(); i++) {
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (i < 1) {
                pointParams.setMargins(0, 0, 0, 0);
            } else {
                pointParams.setMargins(10, 0, 0, 0);
            }
            ImageView iv = new ImageView(getContext());
            iv.setLayoutParams(pointParams);
            iv.setBackgroundResource(R.mipmap.first_noclick);
            llPoint.addView(iv);
        }
//        llPoint.getChildAt(0).setBackgroundResource(R.mipmap.first_click);

        if(llPoint.getChildCount() >0 ){
            llPoint.getChildAt(0).setBackgroundResource(R.mipmap.first_click);
        }
    }
}
