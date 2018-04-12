package com.sinia.orderlang.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.adapter.GoodsListAdapter;
import com.sinia.orderlang.bean.CarCheckBean;
import com.sinia.orderlang.bean.GoodItemsBean;
import com.sinia.orderlang.bean.GoodsListBean;
import com.sinia.orderlang.bean.GoodsListList;

/**
 * 商品列表
 */
public class GoodsListActivity extends BaseActivity {

	private ListView lv;
	private GoodsListAdapter adapter;
	private GoodsListList goodsListList;
	private boolean fromWhere;
	private CarCheckBean carCheckBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goodslist, "商品清单");
		initView();
		getListView();
		fromWhere = getIntent().getBooleanExtra("fromWhere", false);
		if(fromWhere){
			carCheckBean = (CarCheckBean) getIntent().getSerializableExtra("CarCheckBean");
			List<GoodsListBean> list = new ArrayList<GoodsListBean>();
			List<GoodItemsBean> beans = carCheckBean.getData();
			for(GoodItemsBean bean : beans){
				GoodsListBean gbean =  new GoodsListBean();
				gbean.setGoodName(bean.getGoodName());
				gbean.setGoodNum(bean.getGoodNum());
				gbean.setPrice(bean.getGoodPrice());
				gbean.setImageUrl(bean.getImageUrl());
				list.add(gbean);
			}
			getDoingView().setText("共" + list.size() + "件");
			adapter.data.clear();
			adapter.data.addAll(list);
			adapter.notifyDataSetChanged();
		}else{
			goodsListList = (GoodsListList) getIntent().getSerializableExtra(
					"goodsListList");
			getDoingView().setText("共" + goodsListList.getItems().size() + "件");
			adapter.data.clear();
			adapter.data.addAll(goodsListList.getItems());
			adapter.notifyDataSetChanged();
		}
	}

	public void initView() {
		lv = (ListView) findViewById(R.id.lv);
	}

	public void getListView() {
		adapter = new GoodsListAdapter(this);
		lv.setAdapter(adapter);
	}
}
