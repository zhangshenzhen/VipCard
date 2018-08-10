package com.bjypt.vipcard.view.categoryview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.bjypt.vipcard.activity.LifeTypeActivity;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.TrackCommon;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.model.AppCategoryBean;
import com.bjypt.vipcard.model.AppCategroyLifeTypeResultDataBean;
import com.bjypt.vipcard.model.AppCategroyResultDataBean;
import com.bjypt.vipcard.model.GridViewTestData;
import com.bjypt.vipcard.model.LifeTypeBean;
import com.bjypt.vipcard.model.ListTestData;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.PhoneCpuId;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.InnerGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglei on 2017/12/28.
 * 生活服务--更多
 */

public class AppCategoryLifeTypeMenuView extends AppCategoryContextView {

    private ListView listview;
    private AppCategroyLifeTypeResultDataBean appCategroyLifeTypeResultDataBean;
    private  List<AppCategoryBean> appCategoryBeans;

    public AppCategoryLifeTypeMenuView(Context context) {
        super(context);
    }

    public AppCategoryLifeTypeMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AppCategoryLifeTypeMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AppCategoryLifeTypeMenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {
        Wethod.configImageLoader(context);
        LayoutInflater.from(context).inflate(R.layout.view_app_category_lifetype_menu_view, this);
        listview = (ListView) findViewById(R.id.life_type_list);
    }

    @Override
    public AppCategoryBean getItemBean(int postion) {
        LogUtil.debugPrint("getItemBean:" + postion);
        return null;
    }

    @Override
    public void httpGetData() {
        Map<String, String> params = new HashMap<>();
        params.put("city_code", SharedPreferenceUtils.getFromSharedPreference(getContext(), Config.userConfig.citycode, "1558"));
        params.put("mtlevel", "1");
        startLoading(Config.web.application_common_menu, params);
    }

    @Override
    public void initCategoryView(String result) {
        LogUtil.debugPrint("app lifetype:" + result);
        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
        if (StringUtil.isNotEmpty(result)) {
            try {
                appCategroyLifeTypeResultDataBean = objectMapper.readValue(result.toString(), AppCategroyLifeTypeResultDataBean.class);
                if (appCategroyLifeTypeResultDataBean != null && appCategroyLifeTypeResultDataBean.getResultData() != null) {
                    listview.setAdapter(new ListViewAdapter());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public class ListViewAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return appCategroyLifeTypeResultDataBean.getResultData().size();
        }

        @Override
        public Object getItem(int position) {
            return appCategroyLifeTypeResultDataBean.getResultData().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ListViewAdapter.ListHolder holder = null;
            if (holder == null) {
                holder = new ListViewAdapter.ListHolder();

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lifetype_list, null);
                holder.name = (TextView) convertView.findViewById(R.id.textView1);
                holder.gridview = (InnerGridView) convertView.findViewById(R.id.gridview);
                holder.head_item = (RelativeLayout) convertView.findViewById(R.id.head_item);

                convertView.setTag(holder);
            } else {
                holder = (ListViewAdapter.ListHolder) convertView.getTag();
            }

            holder.name.setText(appCategroyLifeTypeResultDataBean.getResultData().get(position).getApp_name());
            List<AppCategoryBean> appCategoryBeans = appCategroyLifeTypeResultDataBean.getResultData().get(position).getSubLife();
            if (appCategoryBeans.size() % 2 != 0) {
                AppCategoryBean gridViewTestData = new AppCategoryBean();
                gridViewTestData.setApp_name("");
                gridViewTestData.setApp_id(0);
                gridViewTestData.setApp_icon("");
                appCategoryBeans.add(gridViewTestData);
            }
            holder.gridview.setAdapter(new ListViewAdapter.GridViewAdapter(appCategoryBeans));


            return convertView;
        }

        public class GridViewAdapter extends BaseAdapter {
            private List<AppCategoryBean> list;

            public GridViewAdapter(List<AppCategoryBean> data) {
                // TODO Auto-generated constructor stub
                list = data;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                // TODO Auto-generated method stub
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub

                ListViewAdapter.GridHolder holder = null;

                if (null == convertView) {
                    holder = new ListViewAdapter.GridHolder();
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lifetype_grid, null);

                    holder.name = (TextView) convertView.findViewById(R.id.textView1);
                    holder.child_item = (RelativeLayout) convertView.findViewById(R.id.child_item);
                    holder.img = (ImageView) convertView.findViewById(R.id.imageView1);

                    convertView.setTag(holder);
                } else {
                    holder = (ListViewAdapter.GridHolder) convertView.getTag();
                }

                holder.name.setText(list.get(position).getApp_name());
                if(list.get(position).getApp_id() ==0){
                    holder.img.setVisibility(View.INVISIBLE);
                }else{
                    holder.img.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(list.get(position).getApp_icon(), holder.img, AppConfig.DEFAULT_IMG_LIFE_TYPE);
                }

                convertView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(list.get(position).getApp_id() != 0){
                            AppCategoryBean appCategoryBean = list.get(position);
                            if(appCategoryBean != null){
                                postTracker(TrackCommon.ViewTrackLifeService, appCategoryBean.getApp_name());
                            }
                            onAppCategoryItemClick(appCategoryBean);
                        }
                    }
                });

                return convertView;
            }

        }


        public class ListHolder {
            TextView name;
            InnerGridView gridview;
            RelativeLayout head_item;
        }

        public class GridHolder {
            TextView name;
            RelativeLayout child_item;
            ImageView img;
        }
    }


}
