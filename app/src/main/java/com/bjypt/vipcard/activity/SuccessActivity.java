package com.bjypt.vipcard.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.model.HuiYuanBiBean;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import java.io.IOException;

public class SuccessActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView obtain;
    private HuiYuanBiBean huiYuanBiBean;
    private RelativeLayout rl_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pay_success);
        //隐藏状态栏
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);

        initView();
        afterInitView();
    }


    public void initView() {
        obtain = (TextView) findViewById(R.id.success_number);
        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
    }

    public void afterInitView() {
        String huiYuanBi = getIntent().getStringExtra("huiYuanBiBean");
        try {
            huiYuanBiBean = ObjectMapperFactory.createObjectMapper().readValue(huiYuanBi, HuiYuanBiBean.class);
            obtain.setText(huiYuanBiBean.getResultData().getAmount());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void bindListener() {
        rl_content.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
    }


}
