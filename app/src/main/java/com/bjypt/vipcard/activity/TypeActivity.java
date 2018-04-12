package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.bjypt.vipcard.model.SmallTypeData;
import com.bjypt.vipcard.model.TypeBean;
import com.bjypt.vipcard.utils.PhoneCpuId;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.StringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/13 0013.
 * 首页点击后小分类
 */

public class TypeActivity extends BaseActivity implements VolleyCallBack<String> {
    private RelativeLayout mBack;
    private GridView mTypeGrid, mBigTypeGrid;
    private String parentpk;//分类主键
    private TypeBean typeBean;
    private String name;//父分类名称
    private TextView mParentName;


   /* private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what){
                case 1:
                    mParentName.setText(typeBean.getResultData().getPkparentName());
                    break;
            }
        }
    };*/

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_type);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        parentpk = intent.getStringExtra("parentpk");
        name = intent.getStringExtra("name");
        Map<String, String> params = new HashMap<>();
        params.put("parentpk", parentpk);
        params.put("citycode", getFromSharePreference(Config.userConfig.citycode));
        Wethod.httpPost(this, 1234, Config.web.small_type, params, this);
    }

    @Override
    public void initView() {
        mBack = (RelativeLayout) findViewById(R.id.type_back);
        mTypeGrid = (GridView) findViewById(R.id.type_grid);
        mParentName = (TextView) findViewById(R.id.parent_type_name);
        mParentName.setText(name);
        mBigTypeGrid = (GridView) findViewById(R.id.type_big_grid);
        Wethod.configImageLoader(this);
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
        switch (id) {
            case R.id.type_back:
                finish();
                break;
        }
    }

    public void statistics(String category_code, String pksubcategory) {
//        category_code 分类编码
//        pksubcategory 分类主键
//        cpuid 机器编码
        Map<String, String> params = new HashMap<>();
        params.put("category_code", category_code);
        params.put("pksubcategory", pksubcategory);
        params.put("cpuid", PhoneCpuId.getDeviceId(this));
        Wethod.httpPost(this, 1991, Config.web.statistics_type, params, this);
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == 1234) {
            try {
                typeBean = getConfiguration().readValue(result.toString(), TypeBean.class);

                if (typeBean.getResultData().getHomeApplicationSubCategoryList().size() % 2 != 0) {
                    SmallTypeData smallTypeData = new SmallTypeData();
                    smallTypeData.setCategory_code("");
                    smallTypeData.setCategory_icon("");
                    smallTypeData.setCategory_name("");
                    smallTypeData.setPkhomeapplicationsubcategory("");
                    smallTypeData.setUse_url("");
                    smallTypeData.setIsentry("");
                    typeBean.getResultData().getHomeApplicationSubCategoryList().add(smallTypeData);
                }

                mTypeGrid.setAdapter(new GridViewAdapter(typeBean.getResultData().getHomeApplicationSubCategoryList()));
                mBigTypeGrid.setAdapter(new GridViewTypeAdapter(typeBean.getResultData().getHomeApplicationRecommendList()));
            } catch (IOException e) {
                Log.e("tutu", "eeee:" + e.toString());
                e.printStackTrace();
            }
        } else if (reqcode == 1991) {

        } else if (reqcode == 2234) {
            /*try {
                typeBean = getConfiguration().readValue(result.toString(),TypeBean.class);

                if(typeBean.getResultData().getHomeApplicationSubCategoryList().size()%2 !=0){
                    SmallTypeData smallTypeData = new SmallTypeData();
                    smallTypeData.setCategory_code("");
                    smallTypeData.setCategory_icon("");
                    smallTypeData.setCategory_name("");
                    smallTypeData.setPkhomeapplicationsubcategory("");
                    smallTypeData.setUse_url("");
                    smallTypeData.setIsentry("");
                    typeBean.getResultData().getHomeApplicationSubCategoryList().add(smallTypeData);
                }

                Log.e("tuyouze","typeBean.getResultData().getHomeApplicationSubCategoryList():"+typeBean.getResultData().getHomeApplicationSubCategoryList().size());
                mTypeGrid.setAdapter(new GridViewAdapter(typeBean.getResultData().getHomeApplicationSubCategoryList()));

                mBigTypeGrid.setAdapter(new GridViewTypeAdapter(typeBean.getResultData().getHomeApplicationCategoryList()));
            } catch (IOException e) {
                Log.e("tutu","eeee:"+e.toString());
                e.printStackTrace();
            }*/
        }

    }

    @Override
    public void onFailed(int reqcode, String result) {
        if (reqcode == 1234) {

        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    public class GridViewTypeAdapter extends BaseAdapter {
        private List<SmallTypeData> lists;

        public GridViewTypeAdapter(List<SmallTypeData> data) {
            lists = data;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            GridViewBigHolder holder = null;
            if (convertView == null) {
                holder = new GridViewBigHolder();
                convertView = LayoutInflater.from(TypeActivity.this).inflate(R.layout.item_type_grid, null);
                holder.mBigImg = (ImageView) convertView.findViewById(R.id.gird_big_item);
                convertView.setTag(holder);
            } else {
                holder = (GridViewBigHolder) convertView.getTag();
            }

            holder.mBigImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 此时是跳转到相应的H5页面中
                    /*Map<String,String> maps = new HashMap<String, String>();
                    maps.put("parentpk",lists.get(position).getPkhomeapplicationcategory());
                    Wethod.httpPost(TypeActivity.this,2234, Config.web.small_type,maps,TypeActivity.this);*/
                    statistics(lists.get(position).getCategory_code(), lists.get(position).getPkhomeapplicationsubcategory());


                    if (lists.get(position).getUse_url() == null || lists.get(position).getUse_url().equals("")) {
                        Toast.makeText(TypeActivity.this, "近期开通", Toast.LENGTH_LONG).show();
                    } else {
                        String usertype = lists.get(position).getUse_type();
                        if (lists.get(position).getIsentry().equals("N")) {
                            Intent intent = new Intent(TypeActivity.this, LifeServireH5Activity.class);
                            intent.putExtra("life_url", lists.get(position).getUse_url());
                            intent.putExtra("isLogin", lists.get(position).getIsentry());
                            intent.putExtra("isallurl", ((StringUtil.isNotEmpty(usertype) && usertype.equals("2")) ? "Y" : ""));
                            intent.putExtra("serviceName", lists.get(position).getCategory_name());
                            startActivity(intent);
                        } else {
                            if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                                Intent intent = new Intent(TypeActivity.this, LifeServireH5Activity.class);
                                intent.putExtra("life_url", lists.get(position).getUse_url());
                                intent.putExtra("isLogin", lists.get(position).getIsentry());
                                intent.putExtra("serviceName", lists.get(position).getCategory_name());
                                intent.putExtra("isallurl", ((StringUtil.isNotEmpty(usertype) && usertype.equals("2")) ? "Y" : ""));
                                startActivity(intent);
                            } else {
                                startLogin();
                            }
                        }
                    }

                }
            });

            ImageLoader.getInstance().displayImage(Config.web.picUrl + lists.get(position).getCategory_icon(), holder.mBigImg, AppConfig.DEFAULT_IMG_LIFE_TYPE);
            return convertView;
        }

        public class GridViewBigHolder {
            ImageView mBigImg;
        }
    }

    public class GridViewAdapter extends BaseAdapter {

        private List<SmallTypeData> list;

        public GridViewAdapter(List<SmallTypeData> datas) {
            list = datas;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            GridHolder holder = null;

            if (null == convertView) {
                holder = new GridHolder();
                convertView = LayoutInflater.from(TypeActivity.this).inflate(R.layout.item_lifetype_grid, null);

                holder.name = (TextView) convertView.findViewById(R.id.textView1);
                holder.child_item = (RelativeLayout) convertView.findViewById(R.id.child_item);
                holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);

                convertView.setTag(holder);
            } else {
                holder = (GridHolder) convertView.getTag();
            }

            holder.name.setText(list.get(position).getCategory_name());

            if (list.get(position).getPkhomeapplicationsubcategory().equals("") || list.get(position).getPkhomeapplicationsubcategory() == null) {

            } else {
                ImageLoader.getInstance().displayImage(Config.web.picUrl + list.get(position).getCategory_icon(), holder.imageView1, AppConfig.DEFAULT_IMG_LIFE_TYPE);
            }


            holder.child_item.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                    Toast.makeText(TypeActivity.this, "" + list.get(position).getCategory_name(), Toast.LENGTH_LONG).show();

                    if (list.get(position).getPkhomeapplicationsubcategory().equals("") || list.get(position).getPkhomeapplicationsubcategory() == null) {

                    } else {
                        statistics(list.get(position).getCategory_code(), list.get(position).getPkhomeapplicationsubcategory());


                        if (list.get(position).getUse_url() == null || list.get(position).getUse_url().equals("")) {
                            Toast.makeText(TypeActivity.this, "近期开通", Toast.LENGTH_LONG).show();
                        } else {
                            if (list.get(position).getIsentry().equals("N")) {
                                Intent intent = new Intent(TypeActivity.this, LifeServireH5Activity.class);
                                intent.putExtra("life_url", list.get(position).getUse_url());
                                intent.putExtra("isLogin", list.get(position).getIsentry());
                                intent.putExtra("serviceName", list.get(position).getCategory_name());
                                startActivity(intent);
                            } else {
                                if ("Y".equals(getFromSharePreference(Config.userConfig.is_Login))) {
                                    Intent intent = new Intent(TypeActivity.this, LifeServireH5Activity.class);
                                    intent.putExtra("life_url", list.get(position).getUse_url());
                                    intent.putExtra("isLogin", list.get(position).getIsentry());
                                    intent.putExtra("serviceName", list.get(position).getCategory_name());
                                    startActivity(intent);
                                } else {
                                    startLogin();
                                }
                            }
                        }


//                        finish();
                    }

                }
            });

            return convertView;
        }

        public class GridHolder {
            TextView name;
            RelativeLayout child_item;
            ImageView imageView1;
        }

    }

    public void startLogin() {
        startActivity(new Intent(TypeActivity.this, LoginActivity.class));
    }
}
