package com.bjypt.vipcard.activity;

import android.view.View;
import android.widget.Button;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.config.Constant;

/**
 * Created by 涂有泽 .
 * Date by 2016/5/23
 * Use by 注册协议
 */
public class RegisterProtocolActivity extends BaseActivity{

    private Button bt_accept,bt_reject;
    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_register_protocol);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        bt_accept = (Button) findViewById(R.id.bt_accept);
        bt_reject = (Button) findViewById(R.id.bt_reject);

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {
        bt_accept.setOnClickListener(this);
        bt_reject.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_accept://接受
//                Toast.makeText(this,"接受",Toast.LENGTH_LONG).show();
                setResult(Constant.Accept);
                this.finish();
                break;
            case R.id.bt_reject://拒绝
//                Toast.makeText(this,"拒绝",Toast.LENGTH_LONG).show();
                setResult(Constant.Reject);
                this.finish();
                break;
        }
    }
}
