package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.AddOrderBean;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.AES;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/***
 * 充值金额
 */
 public class TopupAmountActivity extends BaseActivity implements VolleyCallBack {
private TextView tv_login_top_up_amount;//立即充值按钮
    private LinearLayout back_layout;//返回
    private EditText et_top_up_amount;//金额输入框
    private int FLAG;//此时判断进入充值的页面之前是哪个页面  FLAG =1时是从商家详情进入的，当FLAG =2 时是从我的资产进入的
    //商家充值时有平台余额充值这个入口，平台充值的时候不存在此入口
    private String pkmuser;
    private String primaryk;
    private TextView recharge_money_tv;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_topup_amount);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        FLAG = intent.getIntExtra("FLAG",0);
        pkmuser = intent.getStringExtra("pkmuser");
    }

    @Override
    public void initView() {
        tv_login_top_up_amount = (TextView) findViewById(R.id.tv_login_top_up_amount);
        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        et_top_up_amount = (EditText) findViewById(R.id.et_top_up_amount);
        recharge_money_tv = (TextView) findViewById(R.id.recharge_money_tv);

        et_top_up_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                recharge_money_tv.setText(s.toString());
            }
        });

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        tv_login_top_up_amount.setOnClickListener(this);
        back_layout.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
switch (v.getId()){
    /**
     * 立即支付
     */
    case R.id.tv_login_top_up_amount:
            //此时生成充值订单
        if(et_top_up_amount.getText().toString().equals("")||et_top_up_amount.getText().toString()==null){
            Toast.makeText(this,"请输入充值金额",Toast.LENGTH_LONG).show();
        }else if(Integer.parseInt(et_top_up_amount.getText().toString())*1==0){
            Toast.makeText(this,"请输入充值金额大于0",Toast.LENGTH_LONG).show();
        }else{
            if(et_top_up_amount.getText().toString().substring(0,1).equals("0")){
                Toast.makeText(this,"首位数字不能为0",Toast.LENGTH_LONG).show();
            }else{
                Map<String,String> params = new HashMap<>();
                params.put("pkregister",getFromSharePreference(Config.userConfig.pkregister));
                params.put("pkmuser", pkmuser);
                params.put("waitMoney", AES.encrypt(et_top_up_amount.getText().toString(),AES.key));
                params.put("payEntrance", "1");
                params.put("pkpropre", "");
                Wethod.httpPost(TopupAmountActivity.this,1, Config.web.create_new_order, params, this);
            }

        }

        break;
/**
 * 返回
 */
    case R.id.back_layout:
        finish();
        break;

}
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if(reqcode == 1){
            try {
                AddOrderBean addOrderBean = getConfiguration().readValue(result.toString(), AddOrderBean.class);
                primaryk = addOrderBean.getResultData().getPkpayid();
                Intent intent = new Intent(this,TopupWayActivity.class);
                intent.putExtra("payMoney",et_top_up_amount.getText().toString());
//                intent.putExtra("payMoney", "0.1");
                intent.putExtra("FLAG",FLAG);
                intent.putExtra("pkmuser",pkmuser);
                intent.putExtra("outorderid", addOrderBean.getResultData().getOutorderid());
                intent.putExtra("primaryk",primaryk);
                startActivity(intent);
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if(reqcode == 1){
            Wethod.ToFailMsg(this,result);
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
