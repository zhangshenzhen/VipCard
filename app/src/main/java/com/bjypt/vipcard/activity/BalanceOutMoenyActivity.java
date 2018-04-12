package com.bjypt.vipcard.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.umeng.UmengCountContext;

/**
 * Created by Administrator on 2016/4/5.
 */
public class BalanceOutMoenyActivity extends BaseActivity {

    private TextView tv_bankName, tv_bankNum;
    private EditText et_outMoenyNum;
    private ImageView iv_bankLogo;
    private Button btn_sureOutMoney;
    private RelativeLayout layout_back;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_balance_outmoney);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_bankName = (TextView) findViewById(R.id.tv_bankName);
        tv_bankNum = (TextView) findViewById(R.id.tv_bankNum);
        et_outMoenyNum = (EditText) findViewById(R.id.et_outMoenyNum);
        iv_bankLogo = (ImageView) findViewById(R.id.iv_bankLogo);
        btn_sureOutMoney = (Button) findViewById(R.id.btn_sureOutMoney);
        layout_back = (RelativeLayout) findViewById(R.id.layout_back);
    }

    @Override
    public void afterInitView() {


    }

    @Override
    public void bindListener() {

        layout_back.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {

        switch (v.getId()) {

            //确认提现
            case R.id.btn_sureOutMoney:
                break;
            case R.id.layout_back:

                this.finish();
                break;
        }
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
