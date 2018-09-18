package com.bjypt.vipcard.activity.crowdfunding;

import android.view.View;
import android.widget.ImageButton;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.BaseFraActivity;
import com.bjypt.vipcard.fragment.crowdfunding.CrowdfundingFragment;

public class CrowdfundingMainActivity extends BaseFraActivity {

    private View crowdfunding_fragment;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_crowdfunding_main);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        crowdfunding_fragment = findViewById(R.id.crowdfunding_fragment);
        ImageButton imageButton = crowdfunding_fragment.findViewById(R.id.ibtn_back);
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }
}
