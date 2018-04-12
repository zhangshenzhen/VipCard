package com.bjypt.vipcard.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.umeng.UmengCountContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/31
 * Use by 修改昵称
 */
public class ChangeNameActivity extends BaseActivity implements VolleyCallBack {
    private Button mChangeNameTv;
    private EditText mOldName;
    private ImageButton mBack;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_change_name);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        mChangeNameTv = (Button) findViewById(R.id.change_name_entrue);
        mOldName = (EditText) findViewById(R.id.old_name);
//        mNewName = (EditText) findViewById(R.id.new_name);
        mBack = (ImageButton) findViewById(R.id.change_name_back);
        mOldName.setText(getFromSharePreference(Config.userConfig.nickname));

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        mChangeNameTv.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.change_name_back:
                finish();
                break;
            case R.id.change_name_entrue:

             /*   if (getFromSharePreference(Config.userConfig.nickname) == null || getFromSharePreference(Config.userConfig.nickname).equals("")) {
                    mOldName.setText(getFromSharePreference(Config.userConfig.phoneno));
                } else {*/
                Map<String, String> params = new HashMap<>();
                if (mOldName.getText().toString().equals("") || mOldName.getText().toString() == null) {
                    Toast.makeText(this, "请输入您的新名称", Toast.LENGTH_LONG).show();
                } else if (mOldName.getText().toString().equals(getFromSharePreference(Config.userConfig.nickname))) {
                    Toast.makeText(this, "新旧昵称不能一样哦", Toast.LENGTH_LONG).show();
                } else {
                    params.put("pkregister", getFromSharePreference(Config.userConfig.pkregister));
                    params.put("phoneno", getFromSharePreference(Config.userConfig.phoneno));

                    params.put("nickname", mOldName.getText().toString());
                    Log.e("kkkk", "phoneno:" + getFromSharePreference(Config.userConfig.phoneno) + "newName:" + mOldName.getText().toString());
                    Wethod.httpPost(ChangeNameActivity.this, 1, Config.web.change_name, params, this);
                    //  }
                }
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == 1) {
            saveDataToSharePreference(Config.userConfig.nickname, mOldName.getText().toString());
            Wethod.ToFailMsg(this, result);
            finish();
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
        UmengCountContext.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengCountContext.onPause(this);
    }
}
