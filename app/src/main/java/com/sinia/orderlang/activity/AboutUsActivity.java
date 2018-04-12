package com.sinia.orderlang.activity;

import android.os.Bundle;
import android.view.View;

import com.bjypt.vipcard.R;


/**
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutusss, "关于我们");
		getDoingView().setVisibility(View.GONE);
	}
}
