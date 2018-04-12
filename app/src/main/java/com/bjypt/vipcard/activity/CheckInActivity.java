package com.bjypt.vipcard.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.receiver.Logger;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.PsdDialogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/1
 * Use by 我想入住
 */
public class CheckInActivity extends BaseActivity implements VolleyCallBack {

    private ImageButton back_iv;
    private EditText mPhoneEt, mShopNameEt, mDetailsAdressEt, mNameEt;
    private Spinner mPro, mCity, mCounty;
    private TextView mEntrue;

    private JSONObject mJsonObj;//把全国的省市区的信息以json的格式保存，解析完成后赋值为null
    private String[] mProvinceDatas;//所有省
    private String mAddress;//原有的地址
    private String[] mAddressList;//原有地址，用空格进行切分地址
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();//key - 省 value - 市s
    private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();//key - 市 values - 区s
    private Boolean isFirstLord = true;//判断是不是最近进入对话框
    private Boolean ifSetFirstAddress = true;//判断是否已经设置了，初始的详细地址

    private ArrayAdapter<String> mProvinceAdapter;//省份数据适配器
    private ArrayAdapter<String> mCityAdapter;//城市数据适配器
    private ArrayAdapter<String> mAreaAdapter;//省份数据适配器

    private String mCurrentProviceName;//当前省的名称
    private String mCurrentAreaName = "";//当前区的名称
    private String mCityCurrentName;//当前城市的名称


    /*pkregister:用户主键
    phoneno：电话号码
    registername：用户姓名
    address：商家地址*/
    private String url = Config.web.checkin;
    private String pkregister;
    private String phoneno;
    private String registername;
    private String address;
    private Map<String, String> map;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_check_in);
    }

    @Override
    public void beforeInitView() {
        Wethod.httpPost(this, 1111, "http://123.57.232.188:8080/hyb-lifeservice/news/newsList?pkregister=8fec54d4258f4808baf335d7715e717b", null, this);
    }

    @Override
    public void initView() {
        initJsonData();
        initDatas();
        back_iv = (ImageButton) findViewById(R.id.back_iv);
        mNameEt = (EditText) findViewById(R.id.name_et);
        mPhoneEt = (EditText) findViewById(R.id.phone_et);
        mShopNameEt = (EditText) findViewById(R.id.shop_name_et);
        mDetailsAdressEt = (EditText) findViewById(R.id.details_adress_et);
        mPro = (Spinner) findViewById(R.id.pro_sp);
        mCity = (Spinner) findViewById(R.id.city_sp);
        mCounty = (Spinner) findViewById(R.id.county_sp);
        mEntrue = (TextView) findViewById(R.id.check_in_entrue);


        int selectPro = 11;//有传输数据时
        mProvinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mCityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        mAreaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        for (int i = 0; i < mProvinceDatas.length; i++) {
//            当有传入值时，进行判断选中的条目，设置默认值
            if (mAddress != null && mAddress.equals("") && mAddressList.length > 0 && mAddressList[0].equals(mProvinceDatas[i])) {
                selectPro = i;
            }
            mProvinceAdapter.add(mProvinceDatas[i]);
        }
        mProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPro.setAdapter(mProvinceAdapter);
        mPro.setSelection(selectPro);

        mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCity.setAdapter(mCityAdapter);
//        setCity("安徽");

        mAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCounty.setAdapter(mAreaAdapter);
//        setAreas("阜阳");

        /**
         * 设置，省，市，县，的适配器，进行动态设置其中的值  end
         */
        setupViewsListener();
    }


    /**
     * 设置控件点击选择监听
     */
    private void setupViewsListener() {
        mPro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                mCurrentProviceName = arg0.getSelectedItem() + "";
                if (isFirstLord) {
                    if (mAddress != null && !mAddress.equals("") && mAddressList.length > 1 && mAddressList.length < 3) {
                        updateCitiesAndAreas(mCurrentProviceName, mAddressList[1], null);
                    } else if (mAddress != null && !mAddress.equals("") && mAddressList.length >= 3) {
                        updateCitiesAndAreas(mCurrentProviceName, mAddressList[1], mAddressList[2]);
                    } else {
                        updateCitiesAndAreas(mCurrentProviceName, null, null);
                    }
                } else {
                    updateCitiesAndAreas(mCurrentProviceName, null, null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        mCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                mCityCurrentName = arg0.getSelectedItem() + "";
                if (!isFirstLord) {
                    updateAreas(arg0.getSelectedItem(), null);
                } else {
                    if (mAddress != null && !mAddress.equals("") && mAddressList.length == 4) {
//                        mEtDetailAddre.setText(mAddressList[3]);
                    }
                }
                isFirstLord = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        mCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                mCurrentAreaName = arg0.getSelectedItem() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }


    /**
     * 根据当前的省，更新市和地区信息
     */
    private void updateCitiesAndAreas(Object object, Object city, Object myArea) {
        int selectPosition = 10;//当有数据时，进行匹配城市，默认选中
        String[] cities = mCitisDatasMap.get(object);
        mCityAdapter.clear();
        for (int i = 0; i < cities.length; i++) {
            com.orhanobut.logger.Logger.d(cities[i]);
            if (city != null && city.toString().equals(cities[i])) {
                selectPosition = i;
            }
            mCityAdapter.add(cities[i]);
        }
        mCityAdapter.notifyDataSetChanged();
        if (city == null) {
            updateAreas(cities[0], null);
        } else {
            mCity.setSelection(selectPosition);
            updateAreas(city, myArea);
        }
    }

    private void setCity(String object) {
        String[] cities = mCitisDatasMap.get(object);
        mCityAdapter.clear();
        for (int i = 0; i < cities.length; i++) {
            mCityAdapter.add(cities[i]);
        }
        mCityAdapter.notifyDataSetChanged();
        mCity.setSelection(10);

    }

    /**
     * 根据当前的市，更新地区信息
     */
    private void updateAreas(Object object, Object myArea) {
        boolean isAreas = false;//判断第三个地址是地区还是详细地址
        int selectPosition = 0;//当有数据时，进行匹配地区，默认选中
        String[] area = mAreaDatasMap.get(object);
        mAreaAdapter.clear();
        if (area != null) {
            for (int i = 0; i < area.length; i++) {
                if (myArea != null && myArea.toString().equals(area[i])) {
                    selectPosition = i;
                    isAreas = true;
                }
                mAreaAdapter.add(area[i]);
            }
            mAreaAdapter.notifyDataSetChanged();
            mCounty.setSelection(selectPosition);
        }
        //第三个地址是详细地址，并且是第一次设置edtext值，正好，地址的长度为3的时候，设置详细地址
        /*if(!isAreas&&ifSetFirstAddress&&mAddress!=null&&!mAddress.equals("")&&mAddressList.length==3){
            mEtDetailAddre.setText(mAddressList[2]);
            ifSetFirstAddress=false;
        }*/
    }

    private void setAreas(String city){
        String[] area = mAreaDatasMap.get(city);
        com.orhanobut.logger.Logger.d(area);
        mAreaAdapter.clear();
        if (area != null) {
            for (int i = 0; i < area.length; i++) {
                mAreaAdapter.add(area[i]);
            }
            mAreaAdapter.notifyDataSetChanged();
            mCounty.setSelection(0);
        }
    }


    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        back_iv.setOnClickListener(this);
        mEntrue.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_iv:

                finish();
                break;
            case R.id.check_in_entrue:
                address = mCurrentProviceName + mCityCurrentName + mCurrentAreaName + mDetailsAdressEt.getText().toString();
                if (!"".equals(mPhoneEt.getText().toString()) && !"".equals(mNameEt.getText().toString()) && !"".equals(address)) {
                    map = new HashMap<>();
                    map.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                    map.put("phoneno", mPhoneEt.getText().toString());
                    map.put("registername", mNameEt.getText().toString());
                    map.put("address", address);
                    Wethod.httpPost(CheckInActivity.this, 61, url, map, CheckInActivity.this);

                } else {
                    Toast.makeText(CheckInActivity.this, "姓名、电话、门店名称、地址不能为空", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    private void initDatas() {
        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvinceDatas = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
                String province = jsonP.getString("p");// 省名字

                mProvinceDatas[i] = province;

                JSONArray jsonCs = null;
                try {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1) {
                    continue;
                }
                String[] mCitiesDatas = new String[jsonCs.length()];
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("n");// 市名字
                    mCitiesDatas[j] = city;
                    JSONArray jsonAreas = null;
                    try {
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e) {
                        continue;
                    }

                    String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
                    for (int k = 0; k < jsonAreas.length(); k++) {
                        String area = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
                        mAreasDatas[k] = area;
                    }
                    mAreaDatasMap.put(city, mAreasDatas);
                }

                mCitisDatasMap.put(province, mCitiesDatas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }


    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[is.available()];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "gb2312"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 61) {
            Toast.makeText(CheckInActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }
}
