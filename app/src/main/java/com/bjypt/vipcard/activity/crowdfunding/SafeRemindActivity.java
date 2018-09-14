package com.bjypt.vipcard.activity.crowdfunding;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;

public class SafeRemindActivity extends BaseActivity  {

    private Button safe_btn;
    private String mbarcode;
    private ImageView iv_back;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_safe_remind);
    }

    @Override
    public void beforeInitView() {
        Intent intent = getIntent();
        mbarcode = intent.getStringExtra("barcode");

    }

    @Override
    public void initView() {
        iv_back = findViewById(R.id.iv_code_back);
        safe_btn = findViewById(R.id.safe_btn);
        safe_btn.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {

    }
    private static final int SUCCESS_CODE = 456258;
    @Override
    public void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.iv_code_back:
                finish();
                break;
            case R.id.safe_btn:
         Intent intent = new Intent(this, CrowdfundingBigOneCodeActivity.class);
         intent.putExtra("barcode", mbarcode);
         intent.putExtra("pkmuser", 0);
         intent.putExtra("muname", 0);
         intent.putExtra("cardnum", 0);
         startActivityForResult(intent, SUCCESS_CODE);
               break;


        }


    }
     /*
     * 直接返回上一级*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
          finish();
    }
}
