package com.bjypt.vipcard.activity.cityconnect;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ToastUtils;
import com.sinia.orderlang.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class YouHuiquanBinderActivity extends BaseActivity implements VolleyCallBack {

    private LinearLayout liner_back;
    private EditText et_code;
    private EditText et_sure_code;
    private Button btn_sure_bind;
    private int request_bind_code = 17;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_binder);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        liner_back = findViewById(R.id.liner_back);
        et_code = findViewById(R.id.et_code);
        et_sure_code = findViewById(R.id.et_sure_code);
        btn_sure_bind = findViewById(R.id.btn_sure_binder);

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        liner_back.setOnClickListener(this);
        btn_sure_bind.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
         switch (v.getId()){
             case R.id.liner_back:
                 finish();
                 break;
             case R.id.btn_sure_binder:
                 bindYouHuiQuan();
                 break;
         }
    }

     /*绑定优惠券
     * */
    private void bindYouHuiQuan() {
        String etCode = et_code.getText().toString().trim();
        String etSureCode = et_sure_code.getText().toString().trim();
        if(StringUtil.isNotEmpty(etCode)&&StringUtil.isNotEmpty(etSureCode)){
            if(etCode.equals(etSureCode)){
                receiveYouHuiquan(etCode);
            }else {
                Toast.makeText(this, "优惠券编码和确认优惠券编码不相同",Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this,"优惠券编码不能为空",Toast.LENGTH_SHORT).show();
        }

    }
     /*
     * 提交数据绑定领取*/
    private void receiveYouHuiquan(String etCode) {
        Map<String, String> params = new HashMap<>();
        params.put("pkregister", getPkregister());
        params.put("pkcoupon", etCode);//
        Wethod.httpPost(this, request_bind_code, Config.web.city_connectin_bind_quan, params, this, View.GONE);

    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        LogUtil.debugPrint("优惠券绑定 onSuccess : " + result);
        //绑定成功返回到优惠券列表
        if(request_bind_code == reqcode){
            startActivity(new Intent(YouHuiquanBinderActivity.this,YouHuiQuanListActivity.class));
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        LogUtil.debugPrint("优惠券绑定 onFailed : " + result);
    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
