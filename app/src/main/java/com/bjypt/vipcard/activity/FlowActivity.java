package com.bjypt.vipcard.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.LifeMoneyOptionBean;
import com.bjypt.vipcard.model.PhoneCallerlocBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/8/5
 * Use by  流量充值
 */
public class FlowActivity extends BaseActivity implements VolleyCallBack {

    private RelativeLayout mBack;
    private ImageView mLinkManFlow;
    private EditText mGetPhoneFlow;
    private LinearLayout mOneFlow, mTwoFlow, mThreeFlow, mFourFlow;
    private TextView mPay;
    private TextView mPhoneAddr;
    private TextView one_money_txt, one_flow_txt, two_money_txt, two_flow_txt, three_money_txt, three_flow_txt, four_money_txt, four_flow_txt;
    private String[] str = {};

    private int FLAG_MOBILE = 1;//=2的时候是电信，=3的时候是移动，=4的时候是联通
    private int FLAG_CLICK = 1;//判断哪个被选中
    private List<LifeMoneyOptionBean> list;
    public static int FLAG_LIFE_FLOW = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    one_flow_txt.setText(list.get(0).getSellingPrice());
                    one_money_txt.setText(list.get(0).getBody() + "元");
                    two_flow_txt.setText(list.get(1).getSellingPrice());
                    two_money_txt.setText(list.get(1).getBody() + "元");
                    three_flow_txt.setText(list.get(2).getSellingPrice());
                    three_money_txt.setText(list.get(2).getBody() + "元");
                    four_flow_txt.setText(list.get(3).getSellingPrice());
                    four_money_txt.setText(list.get(3).getBody() + "元");
                    break;

            }
        }
    };

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_flow);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        FLAG_LIFE_FLOW = 1;
        mBack = (RelativeLayout) findViewById(R.id.rl_printer_back_flow);
        mLinkManFlow = (ImageView) findViewById(R.id.iv_Linkman_flow);
        mGetPhoneFlow = (EditText) findViewById(R.id.et_getPhone_flow);

        mOneFlow = (LinearLayout) findViewById(R.id.one_flow);
        mTwoFlow = (LinearLayout) findViewById(R.id.two_flow);
        mThreeFlow = (LinearLayout) findViewById(R.id.three_flow);
        mFourFlow = (LinearLayout) findViewById(R.id.four_flow);

        mPay = (TextView) findViewById(R.id.pay_flow);//立即充值

        one_money_txt = (TextView) findViewById(R.id.one_money_txt);
        one_flow_txt = (TextView) findViewById(R.id.one_flow_txt);
        two_money_txt = (TextView) findViewById(R.id.two_money_txt);
        two_flow_txt = (TextView) findViewById(R.id.two_flow_txt);
        three_money_txt = (TextView) findViewById(R.id.three_money_txt);
        three_flow_txt = (TextView) findViewById(R.id.three_flow_txt);
        four_money_txt = (TextView) findViewById(R.id.four_money_txt);
        four_flow_txt = (TextView) findViewById(R.id.four_flow_txt);

        mPhoneAddr = (TextView) findViewById(R.id.tv_Callerloc_flow);


    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        mGetPhoneFlow.addTextChangedListener(textWatcher);
        mBack.setOnClickListener(this);
        mLinkManFlow.setOnClickListener(this);
        mOneFlow.setOnClickListener(this);
        mTwoFlow.setOnClickListener(this);
        mThreeFlow.setOnClickListener(this);
        mFourFlow.setOnClickListener(this);
        mPay.setOnClickListener(this);
    }

    private void getFlowMoney() {
        if (list != null) {
            list.clear();
        }
        if (mPhoneAddr.getText().toString().contains("电信")) {
            //此时充值手机号为电信
            FLAG_MOBILE = 2;
            list = Wethod.initMoneyOption(2);
            mHandler.sendEmptyMessage(1);
        } else if (mPhoneAddr.getText().toString().contains("移动")) {
            //此时充值手机号为移动
            FLAG_MOBILE = 3;
            list = Wethod.initMoneyOption(3);
            mHandler.sendEmptyMessage(1);
        } else if (mPhoneAddr.getText().toString().contains("联通")) {
            //此时充值手机号为联通
            FLAG_MOBILE = 4;
            list = Wethod.initMoneyOption(4);
            mHandler.sendEmptyMessage(1);
        }
    }

    /**
     * 获取联系人电话
     *
     * @param cursor
     * @return 选择的手机号码
     * @author syj
     */
    private String getContactPhone(Cursor cursor) {
        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String phoneResult = "";
        //System.out.print(phoneNum);
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人的电话号码的cursor;
            Cursor phones = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null, null);
            //int phoneCount = phones.getCount();
            //allPhoneNum = new ArrayList<String>(phoneCount);
            if (phones.moveToFirst()) {
                // 遍历所有的电话号码
                for (; !phones.isAfterLast(); phones.moveToNext()) {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phones.getInt(typeindex);
                    String phoneNumber = phones.getString(index);
                    switch (phone_type) {
                        case 2:
                            phoneResult = phoneNumber;
                            break;
                    }
                }
                if (!phones.isClosed()) {
                    phones.close();
                }
            }
        }
        return phoneResult;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Uri contactData = data.getData();
                        Cursor c = managedQuery(contactData, null, null, null, null);
                        c.moveToFirst();
                        String phonenumber = getContactPhone(c).replaceAll(" ", "");
                        if (phonenumber != null) {
                            mGetPhoneFlow.setText(phonenumber);
                            if (phonenumber.length() == 11) {
                                Map<String, String> params = new HashMap<>();
                                params.put("mobile", phonenumber);
                                Wethod.httpPost(FlowActivity.this, 1, Config.web.query_phone_home, params, FlowActivity.this);
                            }
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "无效的手机号", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rl_printer_back_flow:
                finish();
                break;
            case R.id.iv_Linkman_flow:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
            case R.id.pay_flow:
                //立即充值
                Intent intentFlow = new Intent(this, QqSurePayActivity.class);
                intentFlow.putExtra("mobilePhone", mGetPhoneFlow.getText().toString());
                intentFlow.putExtra("FLAG", 4);
                intentFlow.putExtra("qqNum", mGetPhoneFlow.getText().toString());//等同于cardType			查询还是充值
                intentFlow.putExtra("qqType", "手机流量充值");
                if (FLAG_CLICK == 1) {
                    if (FLAG_MOBILE == 2) {
                        //电信
                        intentFlow.putExtra("cardid", "30");
                        intentFlow.putExtra("totalMoney", 5);
                    } else if (FLAG_MOBILE == 3) {
                        //移动
                        intentFlow.putExtra("cardid", "30");
                        intentFlow.putExtra("totalMoney", 5);
                    } else if (FLAG_MOBILE == 4) {
                        //联通
                        intentFlow.putExtra("cardid", "50");
                        intentFlow.putExtra("totalMoney", 6);
                    }
                } else if (FLAG_CLICK == 2) {
                    if (FLAG_MOBILE == 2) {
                        //电信
                        intentFlow.putExtra("cardid", "100");
                        intentFlow.putExtra("totalMoney", 10);
                    } else if (FLAG_MOBILE == 3) {
                        //移动
                        intentFlow.putExtra("cardid", "500");
                        intentFlow.putExtra("totalMoney", 30);
                    } else if (FLAG_MOBILE == 4) {
                        //联通
                        intentFlow.putExtra("cardid", "100");
                        intentFlow.putExtra("totalMoney", 10);
                    }
                } else if (FLAG_CLICK == 3) {
                    if (FLAG_MOBILE == 2) {
                        //电信
                        intentFlow.putExtra("cardid", "500");
                        intentFlow.putExtra("totalMoney", 30);
                    } else if (FLAG_MOBILE == 3) {
                        //移动
                        intentFlow.putExtra("cardid", "700");
                        intentFlow.putExtra("totalMoney", 40);
                    } else if (FLAG_MOBILE == 4) {
                        //联通
                        intentFlow.putExtra("cardid", "200");
                        intentFlow.putExtra("totalMoney", 15);
                    }
                } else if (FLAG_CLICK == 4) {
                    if (FLAG_MOBILE == 2) {
                        //电信
                        intentFlow.putExtra("cardid", "1");
                        intentFlow.putExtra("totalMoney", 50);
                    } else if (FLAG_MOBILE == 3) {
                        //移动
                        intentFlow.putExtra("cardid", "1");
                        intentFlow.putExtra("totalMoney", 50);
                    } else if (FLAG_MOBILE == 4) {
                        //联通
                        intentFlow.putExtra("cardid", "200");
                        intentFlow.putExtra("totalMoney", 30);
                    }

                }
                intentFlow.putExtra("consumeChildType", "5");
                startActivity(intentFlow);
                break;
            case R.id.one_flow:
                mOneFlow.setBackgroundResource(R.mipmap.qq_back_noclick);
                mTwoFlow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mThreeFlow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mFourFlow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                FLAG_CLICK = 1;
                break;
            case R.id.two_flow:
                FLAG_CLICK = 2;
                mTwoFlow.setBackgroundResource(R.mipmap.qq_back_noclick);
                mOneFlow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mThreeFlow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mFourFlow.setBackgroundColor(Color.parseColor("#FFFFFF"));

                break;
            case R.id.three_flow:
                FLAG_CLICK = 3;
                mThreeFlow.setBackgroundResource(R.mipmap.qq_back_noclick);
                mOneFlow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mTwoFlow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mFourFlow.setBackgroundColor(Color.parseColor("#FFFFFF"));

                break;
            case R.id.four_flow:
                FLAG_CLICK = 4;
                mFourFlow.setBackgroundResource(R.mipmap.qq_back_noclick);
                mOneFlow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mTwoFlow.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mThreeFlow.setBackgroundColor(Color.parseColor("#FFFFFF"));

                break;
        }

    }

    public TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            Log.d("TAG", "afterTextChanged--------------->");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            Log.d("TAG", "beforeTextChanged--------------->");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            if (s.length() > 10) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                if (isOpen) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); //强制隐藏键盘
                }

                Map<String, String> params = new HashMap<>();
                params.put("mobile", mGetPhoneFlow.getText().toString());
                Wethod.httpPost(FlowActivity.this, 1, Config.web.query_phone_home, params, FlowActivity.this);

            } else {
                mPhoneAddr.setText("");
            }
        }
    };

    @Override
    public void onSuccess(int reqcode, Object result) {
        Log.i("aaa", "" + result.toString());
        if (reqcode == 1) {
            try {
                PhoneCallerlocBean phoneCallerlocBean = getConfiguration().readValue(result.toString(), PhoneCallerlocBean.class);
                //判断没有中文
                if (phoneCallerlocBean.getResultData().contains("1")) {
                    str = phoneCallerlocBean.getResultData().split("\\|");
                    mPhoneAddr.setText(str[1] + "\t" + str[2]);
                } else {
                    if (str.length > 0) {
                        str[0] = "";
                        Log.i("aaa", "str.length===>" + str[0]);
                    }
                    Toast.makeText(this, "无效的手机号", Toast.LENGTH_SHORT).show();
                }
                getFlowMoney();

            } catch (IOException e) {
                Log.i("aaa", "Error===>" + e.toString());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if (reqcode == 1) {
            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FLAG_LIFE_FLOW == 2) {
            finish();
            FLAG_LIFE_FLOW = 1;
        }
    }
}
