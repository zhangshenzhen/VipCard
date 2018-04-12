package com.sinia.orderlang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.utils.MyApplication;

/**
 * 评论设置
 */
public class CommonSettingActivity extends BaseActivity implements
		OnClickListener {

	private RelativeLayout rlFeedback, rlPsw, rlAboutus;
	private TextView tv_logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commonsetting, "通用设置");
		getDoingView().setVisibility(View.GONE);
		initView();
	}

	public void initView() {
		rlFeedback = (RelativeLayout) findViewById(R.id.rl_feedback);
		rlPsw = (RelativeLayout) findViewById(R.id.rl_psw);
		rlAboutus = (RelativeLayout) findViewById(R.id.rl_aboutus);
		tv_logout = (TextView) findViewById(R.id.tv_logout);

		rlFeedback.setOnClickListener(this);
		rlPsw.setOnClickListener(this);
		rlAboutus.setOnClickListener(this);
		tv_logout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_feedback:
//			if (MyApplication.getInstance().getBoolValue("is_login")) {
				startActivity(new Intent(CommonSettingActivity.this,
						AdviceFeedbackActivity.class));
//			} else {
//				showToast("请先登录");
//			}
			break;
		case R.id.rl_psw:
//			if (MyApplication.getInstance().getBoolValue("is_login")) {
				startActivity(new Intent(CommonSettingActivity.this,
						ModifyPswActivity.class));
//			} else {
//				showToast("请先登录");
//			}
			break;
		case R.id.rl_aboutus:
			startActivity(new Intent(CommonSettingActivity.this,
					AboutUsActivity.class));
			break;
		case R.id.tv_logout:
			MyApplication.getInstance().setBooleanValue("is_login", false);
			finish();
			break;
		}

	}
}
