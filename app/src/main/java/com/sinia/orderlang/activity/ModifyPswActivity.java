package com.sinia.orderlang.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.UpdatePasswordRequest;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.MyApplication;
import com.sinia.orderlang.utils.StringUtil;

/**
 * 修改密码
 */
public class ModifyPswActivity extends BaseActivity implements OnClickListener {

	private EditText etOldPsw, etNewPsw, etReNewPsw;
	private TextView tvSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifypassword, "修改密码");
		getDoingView().setVisibility(View.GONE);
		initView();
	}

	public void initView() {
		etOldPsw = (EditText) findViewById(R.id.et_oldpsw);
		etNewPsw = (EditText) findViewById(R.id.et_newpsw);
		etReNewPsw = (EditText) findViewById(R.id.et_renewpsw);
		tvSave = (TextView) findViewById(R.id.tv_save);

		tvSave.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_save:
			if (!StringUtil.isEmpty(etOldPsw.getText().toString().trim())) {
				if (!StringUtil.isEmpty(etNewPsw.getText().toString().trim())) {
					if (!StringUtil.isEmpty(etReNewPsw.getText().toString()
							.trim())) {
						if (etNewPsw.getText().toString().trim()
								.equals(etReNewPsw.getText().toString().trim())) {
							updatePassword("1", etOldPsw.getText().toString()
									.trim(), etNewPsw.getText().toString()
									.trim());
						} else {
							showToast("两次输入的新密码不一致");
						}
					} else {
						showToast("请再次输入新密码");
					}
				} else {
					showToast("请输入新密码");
				}
			} else {
				showToast("请输入旧密码");
			}
			break;

		default:
			break;
		}

	}

	// 修改密码接口
	public void updatePassword(String userId, String oldPwd, String newPwd) {
		showLoad("");
		UpdatePasswordRequest request = new UpdatePasswordRequest();
		request.setMethod("updatePassword");
		request.setUserId(userId);
		request.setOldPassword(oldPwd);
		request.setNewPassword(newPwd);
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.UPDATE_PASSWORD,
				request.toString());
	}

	@Override
	public void requestSuccess() {
		// TODO Auto-generated method stub
		super.requestSuccess();
		dismiss();
		showToast("修改成功");
		finish();
	}

	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		super.requestFailed();
		dismiss();
		showToast("修改失败");
	}

	/*
	 * @Override public void updatePasswordSuccess() { finish(); }
	 * 
	 * @Override public void updatePasswordException() {
	 * showToast("旧密码错误，请重新输入");
	 * 
	 * }
	 * 
	 * @Override public void updatePasswordFailed() { showToast("修改失败"); }
	 */
}
