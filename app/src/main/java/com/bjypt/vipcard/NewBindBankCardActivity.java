package com.bjypt.vipcard;

import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.adapter.GridCodePopWindowAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.dialog.NormalDialog;
import com.bjypt.vipcard.model.BankCardBean;
import com.bjypt.vipcard.model.BankCtityBean;
import com.bjypt.vipcard.model.BindCardBean;
import com.bjypt.vipcard.model.CommentDetailsClBean;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.model.UnBindCardBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.BankCitySelectUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.widgets.ListViewAdaptWidth;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewBindBankCardActivity extends BaseActivity implements VolleyCallBack<String> {

    private LinearLayout mBack;               //返回
    private LinearLayout ll_at_concrete;
    private LinearLayout ll_at_city;
    private EditText mRealName;              //姓名
    private EditText mCardNum;               //身份证号码
    private EditText mPhone;                 //手机号码
    private EditText mBankNum;               //银行卡号码
    private String bankNum;                  //银行卡号码
    private EditText mGuiShuHang;           //归属行
    private String mGuiShuHangString;           //归属行
    private Button mBindBank;             //确认
    private Button mModify;               //修改
    private EditText mYanZhengMa;          //验证码
    private TextView mGetYanZhengMa;      //获取验证码
    private TextView tv_at_city;          //银行卡所在地
    private TextView tv_at_concrete;      //银行具体支行
    private LinearLayout yanZhengMa;       //验证码LinearLayout
    private View line;
    private BankCardBean bankCardBean;
    private int FLAG = 0;                  //确认按钮标签
    private BindCardBean bindCardBean;
    private MyTimer timer;
    private SystemInfomationBean systemInfomationBean;
    private BankCitySelectUtil bankCitySelectUtil;
    private PopupWindow mPopupWindow;
    private List<BankCtityBean.ResultDataBean> BankName = new ArrayList<>();
    private static int REQUEST_BANK_KEY = 201712291;
    private static int REQUEST_UNBIND_BUTTON = 1111;
    private static int REQUEST_BAND_INFO = 1101;
    private String mBankName;
    private String mBankCode;
    private String bankprovince;
    private String bankcity;
    private Button unbind_card;               //解绑


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_new_bind_bank_card);
    }

    @Override
    public void beforeInitView() {
        requestBindCard();

        Map<String, String> params1 = new HashMap<>();
        params1.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(NewBindBankCardActivity.this, 1112, Config.web.system_infomatiob, params1, this);
    }

    //请求绑定的银行卡信息
    public void requestBindCard() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(NewBindBankCardActivity.this, 111, Config.web.is_bind_card, params, this);

    }

    @Override
    public void initView() {
        timer = new MyTimer(60000, 1000);
        mBack = (LinearLayout) findViewById(R.id.new_bindbankcard_title_back);
        ll_at_concrete = (LinearLayout) findViewById(R.id.ll_at_concrete);
        ll_at_city = (LinearLayout) findViewById(R.id.ll_at_city);
        mRealName = (EditText) findViewById(R.id.real_name_et);     //姓名
        mCardNum = (EditText) findViewById(R.id.id_card_num_et);    //身份证号码
        mPhone = (EditText) findViewById(R.id.id_card_phone_et);    //手机号码
        mBankNum = (EditText) findViewById(R.id.id_bank_num_et);    //银行卡号码
        mGuiShuHang = (EditText) findViewById(R.id.guishuhang_tv);  //归属行
        mBindBank = (Button) findViewById(R.id.bind_bank);         //确认
        mModify = (Button) findViewById(R.id.bind_bank_modify);   //修改
        unbind_card = (Button) findViewById(R.id.unbind_card);    //解绑
        mYanZhengMa = (EditText) findViewById(R.id.et_yanzhengma);  //验证码
        mGetYanZhengMa = (TextView) findViewById(R.id.tv_get_yanzheng);   // 获取验证码
        yanZhengMa = (LinearLayout) findViewById(R.id.linear_yanzhengma); //验证码linear
        line = findViewById(R.id.view_bindbank);
        tv_at_city = (TextView) findViewById(R.id.tv_at_city);
        tv_at_concrete = (TextView) findViewById(R.id.tv_at_concrete);
        mBankNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {       //获得焦点
                    //在这里可以对获得焦点进行处理
                } else {             //失去焦点
                    //在这里可以对输入的文本内容进行有效的验证
                    bankNum = mBankNum.getText().toString();
                    getBankDetail();
                }
            }
        });
        ll_at_city.setEnabled(false);
        tv_at_city.setEnabled(false);
        ll_at_concrete.setEnabled(false);
        tv_at_concrete.setEnabled(false);
    }

    //获取银行卡信息
    public void getBankDetail() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        params.put("bankcardno", bankNum);
        Wethod.httpPost(NewBindBankCardActivity.this, 110, Config.web.get_bank_detail, params, this);
    }

    @Override
    public void afterInitView() {
    }

    @Override
    public void bindListener() {
        mBack.setOnClickListener(this);
        mBindBank.setOnClickListener(this);
        mModify.setOnClickListener(this);
        unbind_card.setOnClickListener(this);
        ll_at_city.setOnClickListener(this);
        ll_at_concrete.setOnClickListener(this);
        tv_at_city.setOnClickListener(this);
        tv_at_concrete.setOnClickListener(this);
        mGetYanZhengMa.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.unbind_card:
                final NormalDialog mNormalDialog = NormalDialog.getInstance(NewBindBankCardActivity.this);
                mNormalDialog.setTitle("温馨提示");
                mNormalDialog.setDesc("是否解绑");
                mNormalDialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNormalDialog.dismiss();
                        //解绑
                        requestBindInfo();
                        finish();
                    }
                });
                mNormalDialog.setOnNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNormalDialog.dismiss();
                    }
                });
                break;
            case R.id.new_bindbankcard_title_back:
                finish();
                break;
            case R.id.ll_at_city:
            case R.id.tv_at_city:
//                if (StringUtil.isNotEmpty(mGuiShuHangString)) {
                bankCitySelectUtil = new BankCitySelectUtil(this, true, "");
                bankCitySelectUtil.setOnItemClickListner(new BankCitySelectUtil.OnItemClickListener() {
                    @Override
                    public void click(String value, String n) {
                        tv_at_city.setText(value + "  " + n);
                        bankprovince = value;
                        bankcity = n;
                        if (value.equals("北京") || value.equals("天津") || value.equals("重庆") || value.equals("海南") || value.equals("上海")) {
                            requestBankKey(value);
                        } else {
                            requestBankKey(n);
                        }
                    }
                });
                bankCitySelectUtil.show(mBack);
//                } else {
//                    Toast.makeText(NewBindBankCardActivity.this, "请先输入您的银行卡号", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.ll_at_concrete:
            case R.id.tv_at_concrete:
                if (tv_at_city.getText().equals("")) {
                    Toast.makeText(NewBindBankCardActivity.this, "请先选择银行卡所在地", Toast.LENGTH_SHORT).show();
                } else {
                    showPopWindow(tv_at_concrete);
                }
                break;
            case R.id.tv_get_yanzheng:   //获取验证码
                if (mCardNum.getText().toString().equals("") || mBankNum.getText().toString().equals("") || mGuiShuHang.getText().toString().equals("") || tv_at_city.getText().equals("") || tv_at_concrete.getText().equals("") || mPhone.getText().toString().equals("")) {
                    Toast.makeText(NewBindBankCardActivity.this, "信息填写完整后，才能获取验证码", Toast.LENGTH_SHORT).show();
                } else if (mPhone.getText().toString().length() != 11) {
                    Toast.makeText(NewBindBankCardActivity.this, "银行卡绑定手机号码不正确", Toast.LENGTH_SHORT).show();
                } else {
                    timer.start();
                    Map<String, String> params = new HashMap<>();
                    params.put("phoneno", mPhone.getText().toString());
                    Wethod.httpPost(NewBindBankCardActivity.this, 122, Config.web.get_bind_bank_code, params, this);
                }
//                if (mPhone.getText().toString().equals("")) {
//                    Toast.makeText(NewBindBankCardActivity.this, "银行卡绑定手机号码不能为空", Toast.LENGTH_SHORT).show();
//                } else if (mPhone.getText().toString().length() != 11) {
//                    Toast.makeText(NewBindBankCardActivity.this, "银行卡绑定手机号码不正确", Toast.LENGTH_SHORT).show();
//                } else {
//                    timer.start();
//                    Map<String, String> params = new HashMap<>();
//                    params.put("phoneno", mPhone.getText().toString());
//                    Wethod.httpPost(NewBindBankCardActivity.this, 122, Config.web.get_bind_bank_code, params, this);
//                }
                break;
            case R.id.bind_bank:
                mGuiShuHangString = mGuiShuHang.getText().toString();
                if (mRealName.getText().toString().equals("")) {
                    Toast.makeText(NewBindBankCardActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                } else if (mCardNum.getText().toString().equals("")) {
                    Toast.makeText(NewBindBankCardActivity.this, "身份证号不能为空", Toast.LENGTH_SHORT).show();
                } else if (mBankNum.getText().toString().equals("")) {
                    Toast.makeText(NewBindBankCardActivity.this, "银行卡号不能为空", Toast.LENGTH_SHORT).show();
                } else if (mPhone.getText().toString().equals("")) {
                    Toast.makeText(NewBindBankCardActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
//                } else if (mYanZhengMa.getText().toString().equals("")) {
//                    Toast.makeText(NewBindBankCardActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else if (mGuiShuHang.getText().toString().equals("")) {
                    Toast.makeText(NewBindBankCardActivity.this, "银行归属行不能为空", Toast.LENGTH_SHORT).show();
                } else if (tv_at_concrete.getText().equals("")) {
                    Toast.makeText(NewBindBankCardActivity.this, "银行具体支行不能为空", Toast.LENGTH_SHORT).show();
                } else if (tv_at_city.getText().equals("")) {
                    Toast.makeText(NewBindBankCardActivity.this, "银行卡所在地不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //确认按钮
                    if (FLAG == 1) {
                        //此时按钮是灰的，不给点击
                    } else if (FLAG == 2) {
                        //确认按钮
                        Map<String, String> params = new HashMap<>();
                        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                        params.put("bankusername", mRealName.getText().toString());
                        params.put("bankcardno", mBankNum.getText().toString());
                        params.put("bankname", mGuiShuHangString);
                        params.put("bankuserphone", mPhone.getText().toString());
                        params.put("idcardno", mCardNum.getText().toString());
//                        params.put("code", mYanZhengMa.getText().toString());
                        params.put("phoneno", getFromSharePreference(Config.userConfig.phoneno));
                        params.put("bankCode", mBankCode);
                        params.put("lName", mBankName);
                        params.put("bankprovince", bankprovince);
                        params.put("bankcity", bankcity);
                        Wethod.httpPost(NewBindBankCardActivity.this, 112, Config.web.bind_bank_card, params, this);
                    } else if (FLAG == 3) {
                        Map<String, String> params = new HashMap<>();
                        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));   // pkregister：用户主键
                        params.put("bankcardno", mBankNum.getText().toString());                          // bankcardno：银行卡号
                        params.put("bankname", mGuiShuHangString);                                       // bankname：银行名称
                        params.put("bankuserphone", mPhone.getText().toString());                         // bankuserphone：预留手机
                        params.put("phoneno", getFromSharePreference(Config.userConfig.phoneno));
//                        params.put("code", mYanZhengMa.getText().toString());
                        params.put("bankCode", mBankCode);
                        params.put("lName", mBankName);
                        params.put("bankusername", mRealName.getText().toString());                       //持卡人姓名
                        params.put("idcardno", mCardNum.getText().toString());                             //持卡人身份证号
                        params.put("bankprovince", bankprovince);
                        params.put("bankcity", bankcity);
                        Wethod.httpPost(NewBindBankCardActivity.this, 113, Config.web.update_bank_card, params, this);
                    }
                }
                break;
            case R.id.bind_bank_modify:
                FLAG = 3;
                mRealName.setEnabled(false);
                mPhone.setEnabled(true);
                mBankNum.setEnabled(true);
                mBindBank.setEnabled(true);
                mGuiShuHang.setEnabled(true);
                mCardNum.setEnabled(true);
//                if (bindCardBean.getResultData().getIdcardno() == null) {
//                    mCardNum.setEnabled(true);
//                } else {
//                    mCardNum.setEnabled(false);
//                }
                mBindBank.setBackgroundResource(R.drawable.background_bindbank_confirm);
                mBankNum.setText("");
                mGuiShuHang.setText("");
                mPhone.setText("");
                tv_at_city.setText("");
                mCardNum.setText("");
                tv_at_concrete.setText("");
                ll_at_city.setEnabled(true);
                tv_at_city.setEnabled(true);
                ll_at_concrete.setEnabled(true);
                tv_at_concrete.setEnabled(true);
                mBindBank.setVisibility(View.VISIBLE);
                mModify.setVisibility(View.GONE);
//                yanZhengMa.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                unbind_card.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == REQUEST_BAND_INFO) {
//            requestBindCard();
        }
        if (reqcode == REQUEST_UNBIND_BUTTON) {
            try {
                UnBindCardBean unBindCardBean = getConfiguration().readValue(result.toString(), UnBindCardBean.class);
                if (unBindCardBean.getResultData().getFlag() == 0) {
                    unbind_card.setVisibility(View.GONE);
                } else if (unBindCardBean.getResultData().getFlag() == 1) {
                    unbind_card.setVisibility(View.VISIBLE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 1112) {
            try {
                systemInfomationBean = getConfiguration().readValue(result.toString(), SystemInfomationBean.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 110) {
            Log.e("yang", "bindCardBean:" + result.toString());
            try {
                bankCardBean = getConfiguration().readValue(result.toString(), BankCardBean.class);
                mGuiShuHang.setText(bankCardBean.getResultData().getBankName());
                mGuiShuHangString = bankCardBean.getResultData().getBankName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 111) {
            try {
                bindCardBean = getConfiguration().readValue(result.toString(), BindCardBean.class);
                Log.e("TYZ", "bindCardBean:" + result.toString());
                FLAG = 1;
                //不为空时，说明当前用户已经绑定了银行卡，所以用户姓名和身份证号码不可更改

                mRealName.setEnabled(false);
                mCardNum.setEnabled(false);
                mPhone.setEnabled(false);
                mGuiShuHang.setEnabled(false);
                mBankNum.setEnabled(false);
                mGuiShuHang.setText(bindCardBean.getResultData().getBankname());
                mRealName.setText(bindCardBean.getResultData().getBankusername());
                mCardNum.setText(bindCardBean.getResultData().getIdcardno());
                mPhone.setText(bindCardBean.getResultData().getPhoneno());
                mBankNum.setText(bindCardBean.getResultData().getBankcardno());
                tv_at_city.setText(bindCardBean.getResultData().getBankprovince() + "  " + bindCardBean.getResultData().getBankcity());
                tv_at_concrete.setText(bindCardBean.getResultData().getlName());
                Log.e("clclcl========", bindCardBean.getResultData().getBankprovince() + "  " + bindCardBean.getResultData().getBankcity() + "...." + bindCardBean.getResultData().getlName());
                mModify.setVisibility(View.VISIBLE);
                mBindBank.setVisibility(View.GONE);
//                yanZhengMa.setVisibility(View.GONE);
                line.setVisibility(View.GONE);

                //判断解绑按钮是否显示
                requestUnbindButton();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (reqcode == 112) {
            Toast.makeText(this, "绑定银行卡成功", Toast.LENGTH_LONG).show();
            mBindBank.setVisibility(View.GONE);
            mModify.setVisibility(View.VISIBLE);
//            yanZhengMa.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            finish();
        } else if (reqcode == 113) {
            mBindBank.setVisibility(View.GONE);
            mModify.setVisibility(View.VISIBLE);
//            yanZhengMa.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            Toast.makeText(this, "修改银行卡成功", Toast.LENGTH_LONG).show();
            finish();
//        } else if (reqcode == 122) {
//            Log.e("liyunte", "验证码获取成功");
        } else if (reqcode == REQUEST_BANK_KEY) {
            Log.e("bankResult", "" + result);
            BankName.clear();
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                BankCtityBean bankCtityBean = objectMapper.readValue(result.toString(), BankCtityBean.class);
                BankName.addAll(bankCtityBean.getResultData());
                initPopupWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, String result) {
        if (reqcode == 111) {
            Wethod.ToFailMsg(this, result);
            FLAG = 2;
            ll_at_city.setEnabled(true);
            tv_at_city.setEnabled(true);
            ll_at_concrete.setEnabled(true);
            tv_at_concrete.setEnabled(true);
            mRealName.setEnabled(true);
            mCardNum.setEnabled(true);
            mBindBank.setEnabled(true);
            mBankNum.setEnabled(true);
            mBindBank.setBackgroundResource(R.drawable.background_bindbank_confirm);
            mModify.setVisibility(View.GONE);
            mBindBank.setVisibility(View.VISIBLE);
//            yanZhengMa.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            unbind_card.setVisibility(View.GONE);   //解绑按钮
        } else if (reqcode == 110) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 112) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 113) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == 122) {
            Wethod.ToFailMsg(this, result);
        } else if (reqcode == REQUEST_BANK_KEY) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                CommentDetailsClBean commentDetailsClBean = objectMapper.readValue(result.toString(), CommentDetailsClBean.class);
                String resultData = commentDetailsClBean.getResultData();
                ToastUtil.showToast(this, resultData);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            mGetYanZhengMa.setText("倒计时" + millisUntilFinished / 1000);
            mGetYanZhengMa.setClickable(false);
        }

        @Override
        public void onFinish() {
            mGetYanZhengMa.setText("获取验证码");
            mGetYanZhengMa.setClickable(true);
        }

    }

    /**
     * Description: 初始化PopWindow，以及给PopWindow中的ListView设置adapter,以及Item监听
     */
    private void initPopupWindow() {
        View view;
        view = LayoutInflater.from(this).inflate(R.layout.grid_code_pop_window_listview, null);
        ListViewAdaptWidth lvPopWindowList = (ListViewAdaptWidth) view.findViewById(R.id.lv_pop_window);
        lvPopWindowList.setDivider(null);                                                       //取消ListView分隔线
        lvPopWindowList.setVerticalScrollBarEnabled(false);                                     //隐藏侧滑栏
        lvPopWindowList.setAdapter(new GridCodePopWindowAdapter(BankName, this));    //给弹窗ListView设置adapter

        //设置Item监听
        lvPopWindowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //点击弹出弹框
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                mBankName = BankName.get(position).getlName();
                mBankCode = BankName.get(position).getBankCode();
                tv_at_concrete.setText(mBankName);
            }
        });
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);   //生成PopWindow
        mPopupWindow.setOutsideTouchable(true);
        /** 为其设置背景，使得其内外焦点都可以获得 */
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setFocusable(true);
        lvPopWindowList.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mPopupWindow.setWidth(lvPopWindowList.getMeasuredWidth());
    }

    //设置弹窗基于标题栏的显示位置
    public void showPopWindow(View view) {
        mPopupWindow.showAsDropDown(view);
        mPopupWindow.setAnimationStyle(R.style.popupAnimation);
    }

    /**
     * 请求银行支行名字
     *
     * @param value value
     */
    private void requestBankKey(String value) {
        if (StringUtil.isEmpty(mGuiShuHangString)) {
            Toast.makeText(this, "请输入您的银行卡号", Toast.LENGTH_LONG).show();
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("bank", mGuiShuHangString);
            params.put("city", value.trim());
            params.put("card", mBankNum.getText().toString());
            Wethod.httpPost(NewBindBankCardActivity.this, REQUEST_BANK_KEY, Config.web.request_bank_key, params, this);
        }
    }

    private void requestUnbindButton() {
        Wethod.httpPost(NewBindBankCardActivity.this, REQUEST_UNBIND_BUTTON, Config.web.un_bind_card, null, this);
    }

    private void requestBindInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(NewBindBankCardActivity.this, REQUEST_BAND_INFO, Config.web.bind_info, params, this);

    }
}
