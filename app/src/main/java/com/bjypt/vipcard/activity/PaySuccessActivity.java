package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.common.Config;

public class PaySuccessActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1;
    private Button btn2;
    private String cardnum = "";
    private String stringType="";
    private TextView tv_content;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_pay_success);
        setContentView(R.layout.dialog_recharge_state);
        //隐藏状态栏
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);

        beforeInitView();
        initView();
        afterInitView();
        bindListener();
    }


    public void beforeInitView() {
        cardnum = getIntent().getStringExtra("cardnum");
        stringType = getIntent().getStringExtra("stringType");

    }

    public void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        tv_content = (TextView) findViewById(R.id.tv_content);
    }

    public void afterInitView() {
        if (!stringType.equals("")){
            tv_content.setText(stringType);
        }
    }

    public void bindListener() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn2:
                Intent intent = new Intent(PaySuccessActivity.this, LifeServireH5Activity.class);
                intent.putExtra("life_url", Config.web.citizen_card_expense_calendar + "?type_main=7" + "&cardnum=" + cardnum + "&pkregister=");
                intent.putExtra("isLogin", "Y");
                intent.putExtra("serviceName", "消费记录");
                startActivity(intent);
                finish();
                break;
            case R.id.btn1:
                finish();
                break;
        }
    }


}
