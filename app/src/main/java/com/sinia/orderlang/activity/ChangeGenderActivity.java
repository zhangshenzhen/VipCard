package com.sinia.orderlang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bjypt.vipcard.R;

/**
 * 修改性别
 */
public class ChangeGenderActivity extends BaseActivity implements
		OnClickListener {

	private RelativeLayout rlFemale, rlMale, rlPrivary;
	private ImageView imgFemale, imgMale, imgPrivary;
	private String gender;
	private int sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changegender, "修改性别");
		getDoingView().setVisibility(View.GONE);
		sex = getIntent().getIntExtra("sex", 3);
		initView();
	}

	public void initView() {
		rlFemale = (RelativeLayout) findViewById(R.id.rl_female);
		rlMale = (RelativeLayout) findViewById(R.id.rl_male);
		rlPrivary = (RelativeLayout) findViewById(R.id.rl_privary);
		imgFemale = (ImageView) findViewById(R.id.img_female);
		imgMale = (ImageView) findViewById(R.id.img_male);
		imgPrivary = (ImageView) findViewById(R.id.img_privary);
		if (sex == 1) {
			imgFemale.setVisibility(View.INVISIBLE);
			imgMale.setVisibility(View.VISIBLE);
			imgPrivary.setVisibility(View.INVISIBLE);
		} else if (sex == 2) {
			imgFemale.setVisibility(View.VISIBLE);
			imgMale.setVisibility(View.INVISIBLE);
			imgPrivary.setVisibility(View.INVISIBLE);
		} else {
			imgFemale.setVisibility(View.INVISIBLE);
			imgMale.setVisibility(View.INVISIBLE);
			imgPrivary.setVisibility(View.VISIBLE);
		}
		rlFemale.setOnClickListener(this);
		rlMale.setOnClickListener(this);
		rlPrivary.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_female:
			imgFemale.setVisibility(View.VISIBLE);
			imgMale.setVisibility(View.INVISIBLE);
			imgPrivary.setVisibility(View.INVISIBLE);
			sex = 2;
			gender = "女";
			break;
		case R.id.rl_male:
			imgFemale.setVisibility(View.INVISIBLE);
			imgMale.setVisibility(View.VISIBLE);
			imgPrivary.setVisibility(View.INVISIBLE);
			gender = "男";
			sex = 1;
			break;
		case R.id.rl_privary:
			imgFemale.setVisibility(View.INVISIBLE);
			imgMale.setVisibility(View.INVISIBLE);
			imgPrivary.setVisibility(View.VISIBLE);
			gender = "保密";
			sex = 3;
			break;
		default:
			break;
		}

	}
	@Override
	protected void backView() {
		// TODO Auto-generated method stub
		super.backView();
		Log.d("lamp", sex + "");
		setResult(RESULT_OK, new Intent().putExtra("gender", sex));
		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Log.d("lamp", sex + "");
		setResult(RESULT_OK, new Intent().putExtra("gender", sex));
		finish();
	}
}
