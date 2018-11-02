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
import com.bjypt.vipcard.common.TrackCommon;
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
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglei on 2017/12/27.
 * 八大汇菜单
 */

public class AppCategoryHomeMenuView extends AppCategoryContextView {

    private ViewPager viewpager_category_new_home;       //分类
    private LinearLayout ly_category_new_home_point;     //分类小圆点的布局
    private int currentitem = 0;
    private int mHeight;
    private int size = 8;
    private GridView gridView;
    List<AppCategoryGroupBean> listbeans = new ArrayList<>();


    private AppCategroyResultDataBean appCategroyResultDataBean;

    public AppCategoryHomeMenuView(Context context) {
        super(context);
    }

    public AppCategoryHomeMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AppCategoryHomeMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_app_category_menu_view, this);
        viewpager_category_new_home = (ViewPager) findViewById(R.id.viewpager_category_new_home);
        ly_category_new_home_point = (LinearLayout) findViewById(R.id.ly_category_new_home_point);

        ly_category_new_home_point.setVisibility(View.GONE);
    }

    @Override
    public AppCategoryBean getItemBean(int postion) {
        return appCategroyResultDataBean.getResultData().get(postion);
    }

    @Override
    public void httpGetData() {
        Map<String, String> params = new HashMap<>();
        params.put("city_code", SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.web.cityCode, "1558"));
//        com.orhanobut.logger.Logger.e("city_code : "+SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.userConfig.citycode, "1558"));
//        params.put("city_code", Config.userConfig.citycode);
        startLoading(Config.web.application_home_category, params);
    }

    @Override
    public void initCategoryView(String result) {
        LogUtil.debugPrint("app menu:" + result);
        com.orhanobut.logger.Logger.e("app menu:" + result);
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        try {
            listbeans.clear();
            appCategroyResultDataBean = objectMapper.readValue(result.toString(), AppCategroyResultDataBean.class);

            if (appCategroyResultDataBean.getResultData().size() <= 4) {
                mHeight = DensityUtil.dip2px(getContext(), 80);
            } else {
                mHeight = DensityUtil.dip2px(getContext(), 150);
            }

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewpager_category_new_home.getLayoutParams();
            params.height = mHeight;
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            viewpager_category_new_home.setLayoutParams(params);
            initPagerView(appCategroyResultDataBean.getResultData(), size);
            initData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initPagerView(List<AppCategoryBean> list, int size) {
        fengzhuang(list, size);
        setRoundviewColor();
    }

    private void initData() {


        viewpager_category_new_home.setAdapter(new MyPagerAdpater(listbeans, getContext()));

        viewpager_category_new_home.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentitem = position;
                setRoundviewColor();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * @param list 所有的item的集合
     * @param size 每页显示多少个item
     */
    public void fengzhuang(List<AppCategoryBean> list, int size) {
        int n = list.size() % size;
        int num;
        if (n != 0) {
            num = (int) list.size() / size + 1;
        } else {
            num = (int) list.size() / size;
        }
        for (int i = 1; i <= num; i++) {
            List<AppCategoryBean> stringList = new ArrayList();
            for (int j = 0; j < list.size(); j++) {
                if (j >= (i - 1) * size && j < i * size) {
                    stringList.add(list.get(j));
                }
            }
            AppCategoryGroupBean bean = new AppCategoryGroupBean();
            bean.setAppCategoryBeans(stringList);
            listbeans.add(bean);
        }
    }


    class AppCategoryGroupBean {
        private List<AppCategoryBean> appCategoryBeans;

        public List<AppCategoryBean> getAppCategoryBeans() {
            return appCategoryBeans;
        }

        public void setAppCategoryBeans(List<AppCategoryBean> appCategoryBeans) {
            this.appCategoryBeans = appCategoryBeans;
        }
    }


    public View roundview(int height) {
        RoundImageView view = new RoundImageView(getContext());
        view.setImageResource(R.mipmap.app_ic_launcher);
//        View view = new View(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height, height);
        params.setMargins(10, 0, 0, 0);//设置左边margin为10
        view.setLayoutParams(params);
        return view;
    }

    public void setRoundviewColor() {
        ly_category_new_home_point.removeAllViews();
        for (int i = 0; i < listbeans.size(); i++) {
            if (i == currentitem) {
                ly_category_new_home_point.addView(roundview(20));
            } else {
                ly_category_new_home_point.addView(roundview(10));
            }
        }
    }

    class MyPagerAdpater extends PagerAdapter {
        private List<AppCategoryGroupBean> listbeans;
        private Context context;


        public MyPagerAdpater(List<AppCategoryGroupBean> listbeans, Context context) {
            this.context = context;
            this.listbeans = listbeans;
        }

        @Override
        public int getCount() {
            return listbeans.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            gridView = new GridView(context);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(viewpager_category_new_home.getMeasuredWidth(), viewpager_category_new_home.getMeasuredHeight());
            gridView.setLayoutParams(layoutParams);
            gridView.setNumColumns(size / 2);
            gridView.setVerticalSpacing(DensityUtil.dip2px(context, 17));
            gridView.setAdapter(new MyGridViewAdapter(context, listbeans, position));
            container.addView(gridView);
            return gridView;
        }
    }

    private int ceshi = 0;

    class MyGridViewAdapter extends BaseAdapter {

        private Context context;
        private List<AppCategoryGroupBean> list;
        private int page;


        public MyGridViewAdapter(Context context, List<AppCategoryGroupBean> list, int flag) {
            this.context = context;
            this.list = list;
            this.page = flag;

            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
            ImageLoader.getInstance().init(configuration);
        }

        @Override
        public int getCount() {
            return list.get(page).getAppCategoryBeans().size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(page).getAppCategoryBeans().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_viewpager_gridview, parent, false);
                viewHolder.iv_menu = (ImageView) convertView.findViewById(R.id.iv_menu);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_viewpager_gridview);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(list.get(page).getAppCategoryBeans().get(position).getApp_name());
            //AppConfig.DEFAULT_IMG_MERCHANT_BG 配置iamgeloader的显示参数
            ImageLoader.getInstance().displayImage(list.get(page).getAppCategoryBeans().get(position).getApp_icon(),
                    viewHolder.iv_menu, AppConfig.DEFAULT_IMG_MERCHANT_BG);
            convertView.setTag(R.id.news_data_id ,position);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = StringUtil.getInt(v.getTag(R.id.news_data_id)+"",0);
                    AppCategoryBean appCategoryBean = list.get(page).getAppCategoryBeans().get(position);
                    if(appCategoryBean != null){
                        postTracker(TrackCommon.ViewTrackCommonMenus , appCategoryBean.getApp_name());
                    }
                    onAppCategoryItemClick(appCategoryBean);
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        ImageView iv_menu;//处理圆图
        TextView textView;//显示名字
    }


}
