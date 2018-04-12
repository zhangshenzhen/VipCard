package com.bjypt.vipcard.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.SortAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.dialog.NormalDialog;
import com.bjypt.vipcard.model.LocationDingweiBean;
import com.bjypt.vipcard.model.SortModel;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.GaoDeMapLocation;
import com.bjypt.vipcard.utils.PinyinComparator;
import com.bjypt.vipcard.utils.PinyinUtils;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bjypt.vipcard.view.SideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjypt.vipcard.activity.FirstActivity.REQUEST_ACCESS_FINE_LOCATION;

public class CityActivity extends BaseActivity {
    private RelativeLayout city_back;
    private ListView sortListView;//城市
    private SideBar sideBar;//导航栏
    private TextView dialog;
    private TextView city_title;
    private SortAdapter adapter;
    private HotCityAdapter hotcityAdapter;
    private EditText city_search;
    private GridView gridview;
    private List<SortModel> list;
    public static final int CITYCODE = 99;
    private String[] hotcity = new String[]{"北京", "成都", "重庆", "广州", "杭州",
            "南京", "上海", "深圳", "天津", "武汉", "西安", "长春", "大同", "哈尔滨", "南昌", "海口", "三亚", "阜阳"};

    private TextView city_tv;


    private BroadCastReceiverUtils utils;
    private JSONObject mJsonObj;

    private TextView cityNow;//当前城市名
    private TextView refrush_ing;//当前正在刷新还是未定位到城市选择
    private TextView city_change_refrush;//重新定位
    private ImageView city_change_refrushing;//正在刷新图标

    private GaoDeMapLocation gaoDeMapLocation;
    private RelativeLayout dingwei_ing_rela;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.shengfen);
        cityNow = (TextView) findViewById(R.id.city_now);
        dingwei_ing_rela = (RelativeLayout) findViewById(R.id.dingwei_ing_rela);
        refrush_ing = (TextView) findViewById(R.id.refrush_ing);
        if (getFromSharePreference(Config.userConfig.citycode) == null || getFromSharePreference(Config.userConfig.citycode).equals("")) {
            cityNow.setText("定位失败");
            dingwei_ing_rela.setVisibility(View.VISIBLE);
            refrush_ing.setVisibility(View.VISIBLE);
        } else {
            Log.e("tyz", "城市名:" + getFromSharePreference(Config.userConfig.cname));
            cityNow.setText(getFromSharePreference(Config.userConfig.cname) + "");
//            dingwei_ing_rela.setVisibility(View.GONE);
            refrush_ing.setVisibility(View.GONE);
        }

    }

    @Override
    public void beforeInitView() {
        utils = new BroadCastReceiverUtils();
        list = new ArrayList<>();
    }

    @Override
    public void initView() {
        city_tv = (TextView) findViewById(R.id.city_tv);
        city_search = (EditText) findViewById(R.id.city_search);
        // characterParser=CharacterParser.getInstance();
        // pinyinComparator = new PinyinComparator();
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        city_title = (TextView) findViewById(R.id.tv_city_title);
        city_back = (RelativeLayout) findViewById(R.id.city_back);
        sideBar.setTextView(dialog);
        gridview = (GridView) findViewById(R.id.hot_city);

        city_change_refrush = (TextView) findViewById(R.id.city_change_refrush);
        city_change_refrushing = (ImageView) findViewById(R.id.city_change_refrushing);

    }

    @Override
    public void afterInitView() {
        initJsonData();
        list = initDatas();

    }

    @Override
    public void bindListener() {
        city_back.setOnClickListener(this);
        city_change_refrush.setOnClickListener(this);

        adapter = new SortAdapter(this, list);
        sortListView.setAdapter(adapter);
        /**
         * 设置右侧触摸监听
         *
         * */
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });


        /**
         *   热门城市
         *
         * */
        hotcityAdapter = new HotCityAdapter(this, hotcity);
        gridview.setAdapter(hotcityAdapter);


        /**
         *   搜索
         *
         * */
        city_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                //				mCityNames.clear();s

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                gridview.setVisibility(View.GONE);
                city_tv.setVisibility(View.GONE);
                List<SortModel> listc = new ArrayList<SortModel>();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getName().indexOf(s.toString()) != -1 || list.get(i).getSortLetters().indexOf(s.toString()) != -1) {
                        SortModel model = new SortModel();
                        model.setName(list.get(i).getName());
                        model.setSortLetters(list.get(i).getSortLetters());
                        listc.add(model);
                        adapter.updateListView(listc);
                    }


                }


                //				mCityNames.clear();
            }
        });

//        cityNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(StringUtil.isEmpty(cityNow.getText().toString()) || cityNow.getText().equals("定位失败")){
//                    return ;
//                }
//                utils.sendBroadCastReceiver(CityActivity.this, "change_city", "updata", cityNow.getText().toString());
//            }
//        });

    }

    private void onSureChangeCityCode(final LocationDingweiBean ld) {
        final NormalDialog normalDialog = NormalDialog.getInstance(this);
        normalDialog.setDesc("确定要更改城市到" + ld.getAdress() + "吗？");
        normalDialog.setTitle("城市切换提示");
        normalDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceUtils.saveToSharedPreference(CityActivity.this, Config.userConfig.lngu, ld.getmLng() + "");
                SharedPreferenceUtils.saveToSharedPreference(CityActivity.this, Config.userConfig.latu, ld.getmLat() + "");
                SharedPreferenceUtils.saveToSharedPreference(CityActivity.this, Config.userConfig.citycode, ld.getCityCode() + "");
                SharedPreferenceUtils.saveToSharedPreference(CityActivity.this, Config.userConfig.adress, ld.getAdress() + "");
                SharedPreferenceUtils.saveToSharedPreference(CityActivity.this, Config.userConfig.cityAdress, ld.getCityAdress() + "");
                SharedPreferenceUtils.saveToSharedPreference(CityActivity.this, Config.userConfig.cname,  ld.getAdress());
                utils.sendBroadCastReceiver(CityActivity.this, "change_city", "updata", getFromSharePreference(Config.userConfig.cname));
                normalDialog.dismiss();
                CityActivity.this.finish();
            }
        });
        normalDialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalDialog.dismiss();
                CityActivity.this.finish();
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.city_back:
                utils.sendBroadCastReceiver(CityActivity.this, "change_city", "updata", getFromSharePreference(Config.userConfig.cname));
//                 saveDataToSharePreference(Config.userConfig.cityAdress,hotcity[position]);
                finish();
                break;
            case R.id.city_change_refrush:
                city_change_refrush.setVisibility(View.GONE);
                city_change_refrushing.setVisibility(View.VISIBLE);
                refrush_ing.setText("正在定位");
                Log.e("dingwei", "-----" + Build.VERSION.SDK_INT);

                gaoDeMapLocation = new GaoDeMapLocation(this);
                gaoDeMapLocation.startLocation();
                gaoDeMapLocation.setOnCityLocationChangeListener(new GaoDeMapLocation.OnCityLocationChangeListener() {
                    @Override
                    public void onChange(LocationDingweiBean ld) {
                        cityNow.setText(ld.getAdress() + "");
                        city_change_refrush.setVisibility(View.VISIBLE);
                        city_change_refrushing.setVisibility(View.GONE);
//                        onSureChangeCityCode(ld);
                        refrush_ing.setText("未定位到城市,请选择");
                    }
                });
                gaoDeMapLocation.setDingWeiSuccessListener(new GaoDeMapLocation.DingWeiSuccessListener() {
                    @Override
                    public void onDingWeiSuccessListener() {
                        //定位成功的监听 todo
                        city_change_refrush.setVisibility(View.VISIBLE);
                        city_change_refrushing.setVisibility(View.GONE);
                        refrush_ing.setText("未定位到城市,请选择");
                    }
                });
                break;
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void mayRequestLocation() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("获取位置信息");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("修改或删除您的SD卡中的内容");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("读取您的SD卡中的内容");
        if (!addPermission(permissionsList, Manifest.permission.BLUETOOTH_ADMIN)) {

        }
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE)) {
            permissionsNeeded.add("读取手机信息");
        }
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_ACCESS_FINE_LOCATION);
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION: {
//                startLocation();
                if (gaoDeMapLocation == null) {
                    gaoDeMapLocation = new GaoDeMapLocation(this);
                    Log.e("dingwei", "++++++++" + Build.VERSION.SDK_INT);
                }
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.BLUETOOTH_ADMIN, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_SETTINGS, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED
                        ) {
                } else {
                }
                gaoDeMapLocation.startLocation();
                Log.e("dingwei", "*******" + Build.VERSION.SDK_INT);
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /**
     * 根据  城市名称  或  城市首字母
     */

    private ArrayList<SortModel> getSelectCityNames(String con) {
        ArrayList<SortModel> names = new ArrayList<SortModel>();
        //判断查询的内容是不是汉字

        return names;
    }

    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = getAssets().open("mycity.json");
            int len = -1;
            byte[] buf = new byte[is.available()];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<SortModel> initDatas() {


        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("CITYS");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
                JSONArray jsonCs = null;
                try {
                    jsonCs = jsonP.getJSONArray("citys");
                } catch (Exception e1) {
                }
                for (int j = 0; j < jsonCs.length(); j++) {
                    String city = jsonCs.getJSONObject(j).getString("city");
                    //   if (!"区".equals(city.substring(city.length()-1,city.length()))&&!"县".equals(city.substring(city.length()-1,city.length()))){

                    String first = PinyinUtils.getSpells(city).substring(0, 1);
                    SortModel sortModel = new SortModel();
                    sortModel.setName(jsonCs.getJSONObject(j).getString("city"));
                    sortModel.setSortLetters(first);
                    list.add(sortModel);
                    Collections.sort(list, new PinyinComparator());

                    //  }


                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
        return list;
    }


    class HotCityAdapter extends BaseAdapter {

        private Context context;
        private String[] hotcity;

        public HotCityAdapter(Context context, String[] city) {
            super();
            this.context = context;
            this.hotcity = city;
        }

        @Override
        public int getCount() {

            return hotcity.length;
        }

        @Override
        public Object getItem(int position) {

            return hotcity[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {

            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.hotcity, null);
                holder.Tv = (TextView) convertView.findViewById(R.id.city);

                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();

            }
            holder.Tv.setText(hotcity[position]);

            holder.Tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    //  setResult(CITYCODE, new Intent().putExtra("city", hotcity[position]));
                    utils.sendBroadCastReceiver(CityActivity.this, "change_city", "updata", hotcity[position]);
                    saveDataToSharePreference(Config.userConfig.cityAdress, hotcity[position]);
                    CityActivity.this.finish();
                }
            });

            return convertView;
        }

        class ViewHolder {

            TextView Tv;
        }

    }


    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }


    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //do something...
            utils.sendBroadCastReceiver(CityActivity.this, "change_city", "updata", getFromSharePreference(Config.userConfig.cname));
            this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
