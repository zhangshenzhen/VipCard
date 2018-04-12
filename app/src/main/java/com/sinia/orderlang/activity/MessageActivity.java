package com.sinia.orderlang.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.MyMessageRequest;
import com.sinia.orderlang.utils.Constants;

/**
 * 消息
 */
public class MessageActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rlOrder;
	private TextView tv_order, img_order;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message, "消息");
		getDoingView().setVisibility(View.GONE);
		initView();
	}

	
	
	private void getMessage() {
		// img_order.setVisibility(View.VISIBLE);
		// tv_order.setText("");
		//
		// img_order.setVisibility(View.GONE);
		// tv_order.setText("暂无新消息");
		showLoad("");
		MyMessageRequest request = new MyMessageRequest();
		request.setMethod("myMessage");
		request.setUserId(Constants.userId);
		logUrl(request.toString());
		CoreHttpClient.get(this, request.toString(), new HttpCallBackListener() {
			
			@Override
			public void onSuccess(JSONObject json) {
				dismiss();
				Log.d("result", json.toString());
				if(json.optInt("isSuccessful")==0){
					String content = json.optString("content");
					tv_order.setText(content);
					if(json.optInt("messStatus")==2){
						img_order.setVisibility(View.VISIBLE);
					}else{
						img_order.setVisibility(View.INVISIBLE);
					}
				}
			}
			
			@Override
			public void onRequestFailed() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		getMessage();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void initView() {
		rlOrder = (RelativeLayout) findViewById(R.id.rl_order);
		tv_order = (TextView) findViewById(R.id.tv_order);
		img_order = (TextView) findViewById(R.id.img_order);
		rlOrder.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_order:
			startActivityForNoIntent(OrderInfoActivity.class);
			break;
		}

	}

}
