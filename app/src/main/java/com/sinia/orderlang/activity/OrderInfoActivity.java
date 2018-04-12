package com.sinia.orderlang.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.bjypt.vipcard.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sinia.orderlang.adapter.OrderInfoAdapter;
import com.sinia.orderlang.bean.MessageListList;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.DelMessageRequest;
import com.sinia.orderlang.request.MessageListRequest;
import com.sinia.orderlang.utils.Constants;

/**
 * 订单信息
 */
public class OrderInfoActivity extends BaseActivity {
	private ListView listview;
	private OrderInfoAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_info, "订单信息");
		getDoingView().setText("清空");
		getDoingView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 调用清空消息接口
				delMessage();
			}
		});
		initView();
		messageList();
	}
	
	private void initView(){
		listview = (ListView) findViewById(R.id.listview);
		adapter = new OrderInfoAdapter(this);
		listview.setAdapter(adapter);
	}
	
	private void messageList(){
		showLoad("");
		MessageListRequest request = new MessageListRequest();
		request.setMethod("messageList");
		request.setUserId(Constants.userId);
		logUrl(request.toString());
		CoreHttpClient.get(this, request.toString(), new HttpCallBackListener() {
			
			@Override
			public void onSuccess(JSONObject json) {
				dismiss();
				if(json.optInt("isSuccessful")==0){
					Gson gson = new Gson();
					MessageListList list = gson.fromJson(json.toString(), MessageListList.class);
					adapter.data.clear();
					adapter.data.addAll(list.getItems());
					adapter.notifyDataSetChanged();
				}
				
			}
			@Override
			public void onRequestFailed() {
				dismiss();
				showToast("请求失败");
			}

		});
	}
	
	private void delMessage(){
		showLoad("");
		DelMessageRequest request = new DelMessageRequest();
		request.setMethod("delMessage");
		request.setUserId(Constants.userId);
		logUrl(request.toString());
		CoreHttpClient.get(this, request.toString(), new HttpCallBackListener() {
			
			@Override
			public void onSuccess(JSONObject json) {
				dismiss();
				if(json.optInt("isSuccessful")==0){
					showToast("清空成功");
					adapter.data.clear();
					adapter.notifyDataSetChanged();
				}else{
					showToast("清空失败");
				}
			}
			
			@Override
			public void onRequestFailed() {
				dismiss();
				showToast("请求失败");
			}
		});
	}
}
