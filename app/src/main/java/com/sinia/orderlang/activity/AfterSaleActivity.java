package com.sinia.orderlang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.adapter.AfterSaleAdapter;
import com.sinia.orderlang.bean.OrderManagerList;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.OrderManagerRequest;
import com.sinia.orderlang.utils.Constants;

/**
 * 售后
 */
public class AfterSaleActivity extends BaseActivity {

	private ListView listview;
	private AfterSaleAdapter afterSaleAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aftersale, "售后");
		getDoingView().setVisibility(View.GONE);
		initView();
		getList();
		orderManager();
	}

	public void initView() {
		listview = (ListView) findViewById(R.id.listview);
	}

	public void getList() {
		afterSaleAdapter = new AfterSaleAdapter(this);
		listview.setAdapter(afterSaleAdapter);
	}

	private void orderManager() {
		showLoad("");
		OrderManagerRequest request = new OrderManagerRequest();
		request.setMethod("orderManager");
		request.setUserId(Constants.userId);
		request.setType("5");
		logUrl(request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ORDERMANAGER,
				request.toString());
	}

	@Override
	public void getOrderManagerListSuccess(OrderManagerList list) {
		dismiss();
		afterSaleAdapter.data.clear();
		afterSaleAdapter.data.addAll(list.getItems());
		afterSaleAdapter.notifyDataSetChanged();
	}
}
