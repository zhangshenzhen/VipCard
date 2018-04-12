package com.bjypt.vipcard.activity;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.CityPopupUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/31
 * Use by 添加收货人地址
 */
public class AddAdressActivity extends BaseActivity implements VolleyCallBack{

    public static final int SELECT_CITY=1002;

    private RelativeLayout mSettingDefault;
    private ImageView mCheckAdress;
    private RelativeLayout mBack;
    private TextView mSpinnerProvice,mSpinnerCity;
    private int CHECK_FLAG = 0;
    private EditText mName;
    private EditText mPhone;
    private TextView mTrue;
    private String adress;
    private EditText mDetailsAddress;
    private String mCurrentProviceName="北京";
    private String mCurrentCityName="北京";
    private CityPopupUtil cityPopupUtil_provice;
    private CityPopupUtil cityPopupUtil_city;
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_add_adress);
    }

    @Override
    public void beforeInitView() {

    }

    /*
         * 获取当前的手机号
         */
    public String getLocalNumber() {
        TelephonyManager tManager = (TelephonyManager) this
                .getSystemService(TELEPHONY_SERVICE);
        String number = tManager.getLine1Number();
          return number;
    }

    @Override
    public void initView() {

        mSpinnerProvice = (TextView) findViewById(R.id.spinner_province);
        mTrue = (TextView) findViewById(R.id.adress_entrue);
        mName = (EditText) findViewById(R.id.add_adress_name);
        mPhone = (EditText) findViewById(R.id.add_adress_phone);
        mSpinnerCity = (TextView) findViewById(R.id.spinner_city);
        mSettingDefault = (RelativeLayout) findViewById(R.id.setting_default);
        mCheckAdress = (ImageView) findViewById(R.id.check_adress);
        mDetailsAddress = (EditText) findViewById(R.id.adress_et_details);
        mBack = (RelativeLayout) findViewById(R.id.adress_back);


    }


    @Override
    public void afterInitView() {
        cityPopupUtil_provice = new CityPopupUtil(AddAdressActivity.this,true,"");
        cityPopupUtil_provice.setOnItemClickListner(new CityPopupUtil.OnItemColickListener() {
            @Override
            public void click(String value) {
                mCurrentProviceName = value;
                mSpinnerProvice.setText(value);
            }
        });
    }

    @Override
    public void bindListener() {
        mSpinnerProvice.setOnClickListener(this);
        mSpinnerCity.setOnClickListener(this);
        mSettingDefault.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mTrue.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(final View v) {
        int id = v.getId();
        switch (id){
            case R.id.spinner_province:
                cityPopupUtil_provice.show(mBack);
                break;
            case R.id.spinner_city:
                cityPopupUtil_city = new CityPopupUtil(AddAdressActivity.this,false,mCurrentProviceName);
                cityPopupUtil_city.setOnItemClickListner(new CityPopupUtil.OnItemColickListener() {
                    @Override
                    public void click(String value) {
                        mCurrentCityName = value;
                        mSpinnerCity.setText(value);
                    }
                });
                cityPopupUtil_city.show(mBack);
                break;
            case R.id.setting_default:
                if(CHECK_FLAG == 0){
                    CHECK_FLAG = 1;
                    mCheckAdress.setImageResource(R.mipmap.adress_click);
                }else{
                    CHECK_FLAG = 0;
                    mCheckAdress.setImageResource(R.mipmap.adress_noclick);
                }
                break;
            case R.id.adress_back:
                finish();
                break;
            case R.id.adress_entrue:
                adress = mCurrentProviceName+mCurrentCityName;
                Log.i("aaa",""+adress);
                if(mName.getText().toString().equals("")||mName.getText().toString()==null){
                    Toast.makeText(this,"姓名不能为空",Toast.LENGTH_LONG).show();
                }else if(mPhone.getText().toString().equals("")||mPhone.getText().toString()==null){
                    Toast.makeText(this,"手机号码不能为空",Toast.LENGTH_LONG).show();
                }else if(mPhone.getText().toString().length()!=11){
                    Toast.makeText(this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                }else if(adress==null||adress.equals("")){
                    Toast.makeText(this,"地址不能为空",Toast.LENGTH_LONG).show();
                }else{
                    Map<String,String> params = new HashMap<>();
//                    pkregister：用户主键
//                    registername：用户名称
//                    phoneno：电话
//                    receiptaddress：收获地址
//                    defaultaddress：是否是默认地址（0：否，1：是）
                    params.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
                    params.put("registername",mName.getText().toString());
                    params.put("phoneno",mPhone.getText().toString());
                    params.put("receiptaddress",adress+mDetailsAddress.getText().toString());
                    Log.e("cnm","receiptaddress:"+(adress+mDetailsAddress.getText().toString()));
                    if(CHECK_FLAG == 1){
                        params.put("defaultaddress","1");
                    }else{
                        params.put("defaultaddress","0");
                    }

                    Wethod.httpPost(AddAdressActivity.this,11, Config.web.add_shopping_adress,params,this);
                }
                break;
        }
    }


    @Override
    public void onSuccess(int reqcode, Object result) {
        if(reqcode == 11){
            Toast.makeText(this,"添加收货地址成功",Toast.LENGTH_LONG).show();
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
