package com.sinia.orderlang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.MyApplication;

/**
 * 修改昵称
 */
public class ChangeNicknameActivity extends BaseActivity {

	private EditText etNickname;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changenickname, "修改昵称");
		// getDoingView().setVisibility(View.GONE);
		getDoingView().setText("确定");
		getDoingView().setTextColor(Color.parseColor("#000000"));
		name = getIntent().getStringExtra("name");
		initView();
	}

	public void initView() {
		etNickname = (EditText) findViewById(R.id.et_nickname);
		if (!name.equals("")) {
			etNickname.setText(name);
		}
	}

	/*
	 * private void updateNickName(String nickName) { showLoad("");
	 * UpdateUserInformationRequest request = new
	 * UpdateUserInformationRequest();
	 * request.setMethod("updateUserInformation");
	 * request.setUserId(MyApplication.getInstance().getUserbean(this).getId());
	 * request.setUserName(nickName); CoreHttpClient.get(this,
	 * Constants.REQUEST_TYPE.UPDATE_USER_INFORMATION, request.toString());
	 * Log.d("tag", Constants.BASE_URL + request.toString()); }
	 * 
	 * @Override public void getUpdateUserInformationFailed() {
	 * showToast("昵称修改失败"); }
	 * 
	 * @Override public void getUpdateUserInformationSuccess(
	 * UpdateUserInformationBean updateUserInformationBean) {
	 * showToast("昵称修改成功"); LoginBean bean =
	 * MyApplication.getInstance().getUserbean(this);
	 * bean.setUserName(etNickname.getEditableText().toString().trim());
	 * MyApplication.getInstance().setUserbean(this, bean);
	 * 
	 * }
	 */
	@Override
	public void doing() {
		super.doing();
		Log.e("lamp", "nickname="
				+ etNickname.getEditableText().toString().trim());
		setResult(
				RESULT_OK,
				new Intent().putExtra("nickname", etNickname.getText()
						.toString().trim()));
		finish();
	}

}
