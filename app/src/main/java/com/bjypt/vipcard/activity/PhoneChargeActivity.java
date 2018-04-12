package com.bjypt.vipcard.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.PChargeAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.PchargeBean;
import com.bjypt.vipcard.model.PhoneCallerlocBean;
import com.bjypt.vipcard.model.SystemInfomationBean;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.Imeobserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 2016/6/7.
 */
public class PhoneChargeActivity extends BaseActivity implements VolleyCallBack {

    private RelativeLayout mBack;
    private EditText mPhoneNum;
    private ImageView mPhoneLinkman;//联系人
    private TextView mPhoneCallerloc;//归属地
    private GridView mGridView;
    private TextView mChargetogo;
    private PChargeAdapter mAdapter;

    private String[] str = {};
    private int ChargeMoneyNum = 50;
    private Double Mybalance;
    private SystemInfomationBean systemInfomationBean;
    private String systemPkmuser;

    private int IsQualified;//手机号码是否合格
    private int QUALIFIED = 1;
    private int UN_QUALIFIED = 0;
    private BroadCastReceiverUtils utils;
    private BroadcastReceiver receiver;

    @Override
    public void setContentLayout() {

        setContentView(R.layout.activity_phonecharge);
    }

    @Override
    public void beforeInitView() {

        //点击EditView显示键盘 其他则隐藏
        receiver = new MyBroadCaseReceiver();
        utils = new BroadCastReceiverUtils();
        utils.registerBroadCastReceiver(this,"close",receiver);
        Imeobserver.observer(this);
    }
    public class MyBroadCaseReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("close")){
                finish();
            }
        }
    }

    @Override
    public void initView() {

        mBack = (RelativeLayout) findViewById(R.id.rl_printer_back);
        mPhoneNum = (EditText) findViewById(R.id.et_getPhone);
        mPhoneLinkman = (ImageView) findViewById(R.id.iv_Linkman);
        mPhoneCallerloc = (TextView) findViewById(R.id.tv_Callerloc);
        mGridView = (GridView) findViewById(R.id.gv_pcharge);
        mChargetogo = (TextView) findViewById(R.id.tv_pcharge_togo);
    }

    @Override
    public void afterInitView() {

//        Intent intent=getIntent();
//        Mybalance=Double.parseDouble(intent.getStringExtra("mybalance"));
        //添加数据
        List<PchargeBean> pchargeBeanList = new ArrayList<>();
//        pchargeBeanList.add(new PchargeBean(10, 0.30));
        pchargeBeanList.add(new PchargeBean(50, 1.50));
        pchargeBeanList.add(new PchargeBean(100, 3.00));
        pchargeBeanList.add(new PchargeBean(200, 6.00));
        pchargeBeanList.add(new PchargeBean(300, 9.00));

        mAdapter = new PChargeAdapter(this, pchargeBeanList);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void bindListener() {

        mPhoneNum.addTextChangedListener(textWatcher);
        mBack.setOnClickListener(this);
        mPhoneLinkman.setOnClickListener(this);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("aaa", ">>>你选择的是=" + mAdapter.selectChargePrice(position));
                ChargeMoneyNum = mAdapter.selectChargePrice(position);
            }
        });
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.rl_printer_back:
                finish();
                break;
            case R.id.tv_pcharge_togo:

                if (mPhoneNum.getText().toString().length() == 0 || mPhoneNum.getText().toString().equals("")) {
                    Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                } else if (mPhoneNum.getText().toString().length() < 11 || str[0]==null||str[0].toString().equals("")) {
                    Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                } else {
                    if (getFromSharePreference(Config.userConfig.is_Login).equals("Y")) {

                        if (systemInfomationBean.getResultData().getBalance() != null&&!mPhoneCallerloc.getText().equals("")) {
                            Intent in = new Intent(this, SureChargeActivity.class);
                            in.putExtra("PhoneCallerloc", str[1]);
                            in.putExtra("PhoneCallerlocLocation", str[2]);
                            in.putExtra("ChargeMoneyNum", ChargeMoneyNum);
                            in.putExtra("PhoneNum", mPhoneNum.getText().toString());
                            in.putExtra("MyBalance", Mybalance);
                            in.putExtra("systemPkmuser", systemPkmuser);
                            startActivity(in);
                        } else {
                            Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                    }

                }
                break;
            case R.id.iv_Linkman:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
        }
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
                            mPhoneNum.setText(phonenumber);
                            if (phonenumber.length() == 11) {
                                Map<String, String> params = new HashMap<>();
                                params.put("mobile", phonenumber);
                                Wethod.httpPost(PhoneChargeActivity.this,1, Config.web.query_phone_home, params, PhoneChargeActivity.this);
                            }
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "无效的手机号", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
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
    public void onSuccess(int reqcode, Object result) {

        Log.i("aaa", "" + result.toString());
        if (reqcode == 1) {
            try {
                PhoneCallerlocBean phoneCallerlocBean = getConfiguration().readValue(result.toString(), PhoneCallerlocBean.class);
                //判断没有中文
                if (phoneCallerlocBean.getResultData().contains("1")) {
                    str = phoneCallerlocBean.getResultData().split("\\|");
                    mPhoneCallerloc.setText(str[1] + "\t" + str[2]);

                    mChargetogo.setOnClickListener(this);
                } else {
                    if (str.length > 0) {
                        str[0] = "";
                        Log.i("aaa", "str.length===>" + str[0]);
                    }
                    Toast.makeText(this, "无效的手机号", Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                Log.i("aaa", "Error===>" + e.toString());
                e.printStackTrace();
            }
        } else if (reqcode == 1234) {
            try {
                systemInfomationBean = getConfiguration().readValue(result.toString(), SystemInfomationBean.class);
                Mybalance = Double.parseDouble(systemInfomationBean.getResultData().getBalance());
                systemPkmuser = systemInfomationBean.getResultData().getPkmuser();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNumTen=false;
    @Override
    public void onFailed(int reqcode, Object result) {
        Log.i("aaa", "OnFailed" + result.toString());
    }

    @Override
    public void onError(VolleyError volleyError) {

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
                boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
                if(isOpen){
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); //强制隐藏键盘
                }

                Map<String, String> params = new HashMap<>();
                params.put("mobile", mPhoneNum.getText().toString());
                Wethod.httpPost(PhoneChargeActivity.this,1, Config.web.query_phone_home, params, PhoneChargeActivity.this);
            } else {
                mPhoneCallerloc.setText("");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
        Wethod.httpPost(PhoneChargeActivity.this,1234, Config.web.system_infomatiob, params, this);
    }
}
