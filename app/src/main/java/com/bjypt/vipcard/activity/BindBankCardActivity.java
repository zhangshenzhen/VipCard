package com.bjypt.vipcard.activity;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.bjypt.vipcard.model.BankNameBean;
import com.bjypt.vipcard.model.BindCardBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.CityPopupUtil;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/5
 * Use by 绑定银行卡
 */
public class BindBankCardActivity extends BaseActivity implements VolleyCallBack {

    private RelativeLayout mBack;
    private EditText mRealName, mCardNum, mPhone, mBankNum, et_yanzhengma_bind_bank_card;
    private TextView mSpinnerProvice, mSpinnerCity;
    private Spinner mSinnerCard;
    private TextView mTrue, mSinnerCardName, tv_get_yanzheng_bind_bank_card;
    private String pkregister;
    private BindCardBean bindCardBean;
    private String[] bankNameArray;
    private String bank;
    private RelativeLayout mAfreshBind;
    private int FLAG = 0;
    private ArrayAdapter<String> mProvinceAdapter;//省份数据适配器
    private ArrayAdapter<String> mCityAdapter;//城市数据适配器
    private ArrayAdapter<String> mAreaAdapter;//省份数据适配器

    private String mCurrentProviceName = "北京";//当前省的名称
    private String mCurrentCityName = "北京";//当前城市名称


    private RelativeLayout mCityRela, mProviceRela;
    private View mCityView, mProviceView;
    private MyTimer timer;
    private CityPopupUtil cityPopupUtil_provice;
    private CityPopupUtil cityPopupUtil_city;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_bind_bank_card);
    }

    @Override
    public void beforeInitView() {

        Wethod.httpPost(BindBankCardActivity.this, 2, Config.web.get_Card_name, null, this);

    }

    @Override
    public void initView() {
        timer = new MyTimer(60000, 1000);
        mBack = (RelativeLayout) findViewById(R.id.bind_bank_card_back);
        mRealName = (EditText) findViewById(R.id.real_name_et);//姓名
        mCardNum = (EditText) findViewById(R.id.id_card_num_et);//身份证号码
        mPhone = (EditText) findViewById(R.id.id_card_phone_et);//手机号码
        mBankNum = (EditText) findViewById(R.id.id_bank_num_et);//银行卡号码
        mSinnerCard = (Spinner) findViewById(R.id.spinner_card);//银行名称选择
        mTrue = (TextView) findViewById(R.id.bind_card_issue);//确认提现
        mSinnerCardName = (TextView) findViewById(R.id.spinner_card_tv);//当已经绑定了银行卡后显示绑定的银行卡信息，此处显示，Spinner隐藏
        tv_get_yanzheng_bind_bank_card = (TextView) findViewById(R.id.tv_get_yanzheng_bind_bank_card);//获取验证码
        mAfreshBind = (RelativeLayout) findViewById(R.id.afresh_bind);//重新绑定
        et_yanzhengma_bind_bank_card = (EditText) findViewById(R.id.et_yanzhengma_bind_bank_card);//验证码输入框
        mSpinnerProvice = (TextView) findViewById(R.id.spinner_provice);//选择银行省份
        mSpinnerCity = (TextView) findViewById(R.id.spinner_city);//选择银行城市
        mProviceView = findViewById(R.id.provice_view);
        mProviceRela = (RelativeLayout) findViewById(R.id.provice_rela);
        mCityView = findViewById(R.id.city_view);
        mCityRela = (RelativeLayout) findViewById(R.id.city_rela);
        int selectPro = 0;//有传输数据时
    }

    @Override
    public void afterInitView() {
        cityPopupUtil_provice = new CityPopupUtil(this, true, "");
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
        mBack.setOnClickListener(this);
        mTrue.setOnClickListener(this);
        tv_get_yanzheng_bind_bank_card.setOnClickListener(this);
        mAfreshBind.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.spinner_provice:               //银行省份
                cityPopupUtil_provice.show(mBack);
                break;
            case R.id.spinner_city:                   //银行城市
                cityPopupUtil_city = new CityPopupUtil(BindBankCardActivity.this, false, mCurrentProviceName);
                cityPopupUtil_city.setOnItemClickListner(new CityPopupUtil.OnItemColickListener() {
                    @Override
                    public void click(String value) {
                        mCurrentCityName = value;
                        mSpinnerCity.setText(value);
                    }
                });
                cityPopupUtil_city.show(mBack);
                break;
            case R.id.bind_bank_card_back:                //返回
                finish();
                break;
            case R.id.tv_get_yanzheng_bind_bank_card:   //获取验证码
                if (mPhone.getText().toString().equals("")) {
                    Toast.makeText(BindBankCardActivity.this, "银行卡绑定手机号码不能为空", Toast.LENGTH_SHORT).show();
                } else if (mPhone.getText().toString().length() != 11) {
                    Toast.makeText(BindBankCardActivity.this, "银行卡绑定手机号码不正确", Toast.LENGTH_SHORT).show();
                } else {
                    timer.start();
                    Map<String, String> params = new HashMap<>();
                    params.put("phoneno", mPhone.getText().toString());
                    Wethod.httpPost(BindBankCardActivity.this, 22, Config.web.get_bind_bank_code, params, this);
                }
                break;
            case R.id.bind_card_issue:
                Log.e("TYZ", "FLAG:" + FLAG);
                if (mRealName.getText().toString().equals("")) {
                    Toast.makeText(BindBankCardActivity.this, "真实姓名不能为空", Toast.LENGTH_SHORT).show();
                } else if (mBankNum.getText().toString().equals("")) {
                    Toast.makeText(BindBankCardActivity.this, "银行卡号不能为空", Toast.LENGTH_SHORT).show();
                } else if (et_yanzhengma_bind_bank_card.getText().toString().equals("")) {
                    Toast.makeText(BindBankCardActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //确认按钮
                    if (FLAG == 1) {
                        //此时按钮是灰的，不给点击
                    } else if (FLAG == 2) {
                        Map<String, String> params = new HashMap<>();
                        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                        params.put("bankusername", mRealName.getText().toString());
                        params.put("bankcardno", mBankNum.getText().toString());
                        params.put("bankname", bank);
                        params.put("code", et_yanzhengma_bind_bank_card.getText().toString());
                        params.put("bankuserphone", mPhone.getText().toString());
                        params.put("idcardno", mCardNum.getText().toString());
                        params.put("phoneno", getFromSharePreference(Config.userConfig.phoneno));
                        Wethod.httpPost(BindBankCardActivity.this, 3, Config.web.bind_bank_card, params, this);
                    } else if (FLAG == 3) {
                        Map<String, String> params = new HashMap<>();
//                    pkregister：用户主键
//                    bankcardno：银行卡号
//                    bankname：银行名称
//                    bankuserphone：预留手机
                        params.put("code", et_yanzhengma_bind_bank_card.getText().toString());
                        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                        params.put("bankcardno", mBankNum.getText().toString());
                        params.put("code", et_yanzhengma_bind_bank_card.getText().toString());
                        params.put("bankname", mCurrentProviceName + mCurrentCityName + bank);
                        params.put("bankuserphone", mPhone.getText().toString());
                        params.put("phoneno", getFromSharePreference(Config.userConfig.phoneno));
                        Wethod.httpPost(BindBankCardActivity.this, 4, Config.web.update_bank_card, params, this);
                    }
                }

                break;
            case R.id.afresh_bind:             //重新绑定
                FLAG = 3;
                mRealName.setEnabled(false);
                mCardNum.setEnabled(false);
                mPhone.setEnabled(true);
                mBankNum.setEnabled(true);

                mCityRela.setVisibility(View.VISIBLE);
                mCityView.setVisibility(View.VISIBLE);
                mProviceRela.setVisibility(View.VISIBLE);
                mProviceView.setVisibility(View.VISIBLE);

                mTrue.setEnabled(true);
                mSinnerCard.setVisibility(View.VISIBLE);
                mSinnerCardName.setVisibility(View.GONE);
                mTrue.setBackgroundResource(R.mipmap.affirm_outmoney_unpress);
                break;
        }
    }

    /***
     * 填充数据到布局中
     ***/
    /*public void setData(){
        mRealName.setText(bindCardBean.getResultData().getBankusername());
        mCardNum.setText(bindCardBean.getResultData().getIdcardno());
        mPhone.setText(bindCardBean.getResultData().getBankuserphone());
        mBankNum.setText(bindCardBean.getResultData().getBankcardno());
        mSinnerCardName.setText(bindCardBean.getResultData().getBankname());
    }*/

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            mSinnerCard.setSelection(arg2);
            bank = (String) mSinnerCard.getSelectedItem();
        }


        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }


    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
            try {
                bindCardBean = getConfiguration().readValue(result.toString(), BindCardBean.class);
                Log.e("TYZ", "bindCardBean:" + bindCardBean.toString());
                Log.e("TYZ", "1");
                FLAG = 1;
                //不为空时，说明当前用户已经绑定了银行卡，所以用户姓名和身份证号码不可更改

                mCityRela.setVisibility(View.GONE);
                mCityView.setVisibility(View.GONE);
                mProviceRela.setVisibility(View.GONE);
                mProviceView.setVisibility(View.GONE);
                mRealName.setEnabled(false);
                mCardNum.setEnabled(false);
                mPhone.setEnabled(false);
                mBankNum.setEnabled(false);
                mRealName.setText(bindCardBean.getResultData().getBankusername());
                mCardNum.setText(bindCardBean.getResultData().getIdcardno());
                mPhone.setText(bindCardBean.getResultData().getPhoneno());
                mBankNum.setText(bindCardBean.getResultData().getBankcardno());
                mSinnerCard.setVisibility(View.GONE);
                mSinnerCardName.setVisibility(View.VISIBLE);
                mSinnerCardName.setText(bindCardBean.getResultData().getBankname());
                mTrue.setEnabled(false);
                mTrue.setBackgroundResource(R.mipmap.affirm_outmoney_press);
               /* mSinnerCardName.setVisibility(View.VISIBLE);
                mSinnerCard.setVisibility(View.GONE);
                mRealName.setEnabled(false);
                mCardNum.setEnabled(false);
                mPhone.setEnabled(false);
                mBankNum.setEnabled(false);
                mTrue.setClickable(true);*/
            } catch (IOException e) {
                Log.e("TYZ", "eeee:" + e.toString());
                e.printStackTrace();
            }

        } else if (reqcode == 2) {

            try {
                BankNameBean bankNameBean = getConfiguration().readValue(result.toString(), BankNameBean.class);
                bankNameArray = new String[bankNameBean.getResultData().size()];
                for (int i = 0; i < bankNameBean.getResultData().size(); i++) {
                    bankNameArray[i] = bankNameBean.getResultData().get(i).getBankname();
                }
                //将可选内容与ArrayAdapter连接起来
                ArrayAdapter<String> bankNameadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bankNameArray);
                //设置下拉列表的风格
                bankNameadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //将adapter 添加到spinner中
                mSinnerCard.setAdapter(bankNameadapter);
                //添加事件Spinner事件监听
                mSinnerCard.setOnItemSelectedListener(new SpinnerSelectedListener());

                //设置默认值
                mSinnerCard.setVisibility(View.VISIBLE);
                pkregister = getFromSharePreference(Config.userConfig.pkregister);//用户主键
                Map<String, String> params = new HashMap<>();
                params.put("pkregister", pkregister);

                Wethod.httpPost(BindBankCardActivity.this, 1, Config.web.is_bind_card, params, this);

            } catch (IOException e) {
                Log.e("CXY", "返回:" + e.toString());
                e.printStackTrace();
            }

        } else if (reqcode == 3) {
            Toast.makeText(this, "绑定银行卡成功", Toast.LENGTH_LONG).show();
            finish();
        } else if (reqcode == 4) {
            Toast.makeText(this, "修改银行卡成功", Toast.LENGTH_LONG).show();
            finish();
        } else if (reqcode == 22) {
            Log.e("liyunte", "验证码获取成功");

        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result);
            Log.e("TYZ", "2");
            FLAG = 2;
            mRealName.setEnabled(true);
            mCardNum.setEnabled(true);
            mTrue.setEnabled(true);
            mTrue.setBackgroundResource(R.mipmap.affirm_outmoney_unpress);
            mSinnerCard.setVisibility(View.VISIBLE);
            mSinnerCardName.setVisibility(View.GONE);
            /*mTrue.setBackgroundResource(R.mipmap.dray_bg);
            mSinnerCardName.setVisibility(View.GONE);
            mSinnerCard.setVisibility(View.VISIBLE);
            mRealName.setEnabled(true);
            mCardNum.setEnabled(true);
            mPhone.setEnabled(true);
            mBankNum.setEnabled(true);
            mTrue.setClickable(false);*/
        } else if (reqcode == 2) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 3) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 22) {
            Wethod.ToFailMsg(this, result);
        }
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


    /******************************************************************
     * 城市选择
     ******************************************************************/


    class MyTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_get_yanzheng_bind_bank_card.setText("倒计时" + millisUntilFinished / 1000);
            tv_get_yanzheng_bind_bank_card.setClickable(false);
        }

        @Override
        public void onFinish() {
            tv_get_yanzheng_bind_bank_card.setText("获取验证码");
            tv_get_yanzheng_bind_bank_card.setClickable(true);
        }
    }
}
