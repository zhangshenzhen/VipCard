package com.sinia.orderlang.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.AddAdviceRequest;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.MyApplication;
import com.sinia.orderlang.utils.StringUtil;

/**
 * 意见反馈
 */
public class AdviceFeedbackActivity extends BaseActivity {

	private EditText et_content;
	private TextView tv_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advicefeedback, "意见反馈");
		getDoingView().setVisibility(View.GONE);
		initView();
	}

	private void initView() {
		tv_submit = (TextView) findViewById(R.id.tv_submit);
		et_content = (EditText) findViewById(R.id.et_content);
		tv_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String content = et_content.getEditableText().toString().trim();
				if (StringUtil.isEmpty(content)) {
					showToast("请输入意见内容");
				} else {
					addAdvice(content);
				}
			}
		});
	}

	private void addAdvice(String content) {
		showLoad("");
		AddAdviceRequest request = new AddAdviceRequest();
		request.setMethod("addAdvice");
		request.setUserId(Constants.userId);
		request.setContent(content);
		CoreHttpClient.listen = this;
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ADDADVICE,
				request.toString());
	}

	@Override
	public void requestSuccess() {
		// TODO Auto-generated method stub
		super.requestSuccess();
		dismiss();
		showToast("反馈成功");
	}
	
	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		super.requestFailed();
		dismiss();
		showToast("反馈失败");
	}
}
