package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.bean.CircleBgPic;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChangeSignNameActivity extends BaseActivity implements VolleyCallBack<String> {

    private RelativeLayout signname_back;
    private EditText sign_name;
    private TextView sign_name_entrue;
    private String myUid;
    private static final int CHANGE_SIGN_NAME = 0001;
    private static final int REQUEST_PIC = 0011;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_change_sign_name);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        myUid = intent.getStringExtra("uid");
        requestBgPic();
    }

    @Override
    public void initView() {
        sign_name_entrue = (TextView) findViewById(R.id.sign_name_entrue);
        signname_back = (RelativeLayout) findViewById(R.id.signname_back);
        sign_name = (EditText) findViewById(R.id.sign_name);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        signname_back.setOnClickListener(this);
        sign_name_entrue.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.signname_back:
                finish();
                break;
            case R.id.sign_name_entrue:
                Map<String, String> params = new HashMap<>();
                if (sign_name.getText().toString().equals("") || sign_name.getText().toString() == null) {
                    Toast.makeText(this, "请输入您的新签名", Toast.LENGTH_LONG).show();
                } else if (sign_name.getText().toString().equals(getFromSharePreference(Config.userConfig.sign_name))) {
                    Toast.makeText(this, "新旧签名不能一样哦", Toast.LENGTH_LONG).show();
                } else {
                    params.put("signs", sign_name.getText().toString());
                    params.put("uid", myUid);
                    Wethod.httpPost(ChangeSignNameActivity.this, CHANGE_SIGN_NAME, Config.web.request_update_pic, params, this);
                }
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, String result) {
        if (reqcode == CHANGE_SIGN_NAME) {
            saveDataToSharePreference(Config.userConfig.sign_name, sign_name.getText().toString());
            Wethod.ToFailMsg(this, result);
            finish();
        }else if (reqcode == REQUEST_PIC) {
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                CircleBgPic circleBgPic = objectMapper.readValue(result.toString(), CircleBgPic.class);
                sign_name.setText(circleBgPic.getResultData().getSigns());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void requestBgPic() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", myUid);
        Wethod.httpPost(this, REQUEST_PIC, Config.web.request_bg_pic, params, this);
    }

    @Override
    public void onFailed(int reqcode, String result) {
        if (reqcode == CHANGE_SIGN_NAME) {
            Wethod.ToFailMsg(this, result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

}
