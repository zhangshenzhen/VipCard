package com.sinia.orderlang.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.sinia.orderlang.adapter.AllOrderAdapter;
import com.sinia.orderlang.adapter.WaitCommentOrderAdapter;
import com.sinia.orderlang.adapter.WaitPayOrderAdapter;
import com.sinia.orderlang.adapter.WaitReceiveOrderAdapter;
import com.sinia.orderlang.bean.OrderManagerList;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.OrderManagerRequest;
import com.sinia.orderlang.utils.Constants;

/**
 * 全部订单
 */
public class AllOrderActivity extends BaseActivity implements OnClickListener {

	private TextView all_order, wait_pay, waite_delivery, waite_comment;
	private ImageView cursor1, cursor2, cursor3, cursor4;
	private ViewPager pager;

	private List<View> view;
	private LinearLayout ll_order, ll_pay, ll_delivery, ll_comment;
	private View v1, v2, v3, v4;
	private AllOrderAdapter allOrderAdapter;
	private WaitPayOrderAdapter waitPayOrderAdapter;
	private WaitReceiveOrderAdapter waitReceiveOrderAdapter;
	private WaitCommentOrderAdapter waitCommentOrderAdapter;
	private String status = "1";
	private int flag;
	private int currentIndex;
	private boolean canRefresh;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				orderManager((pager.getCurrentItem() + 1) + "");
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allorder, "全部订单");
		getDoingView().setVisibility(View.GONE);
		flag = getIntent().getIntExtra("flag", 0);
		if (flag == 0) {
			canRefresh = true;
		}
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (canRefresh) {
			orderManager((pager.getCurrentItem() + 1) + "");
		}
		canRefresh = true;
	}

	public void initView() {
		cursor1 = (ImageView) findViewById(R.id.cursor1);
		cursor2 = (ImageView) findViewById(R.id.cursor2);
		cursor3 = (ImageView) findViewById(R.id.cursor3);
		cursor4 = (ImageView) findViewById(R.id.cursor4);
		ll_order = (LinearLayout) findViewById(R.id.ll_order);
		ll_pay = (LinearLayout) findViewById(R.id.ll_pay);
		ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
		ll_delivery = (LinearLayout) findViewById(R.id.ll_delivery);
		ll_order.setOnClickListener(this);
		ll_pay.setOnClickListener(this);
		ll_comment.setOnClickListener(this);
		ll_delivery.setOnClickListener(this);
		all_order = (TextView) findViewById(R.id.all_order);
		wait_pay = (TextView) findViewById(R.id.wait_pay);
		waite_delivery = (TextView) findViewById(R.id.waite_delivery);
		waite_comment = (TextView) findViewById(R.id.waite_comment);
		all_order.setTextColor(Color.parseColor("#D13C47"));
		pager = (ViewPager) findViewById(R.id.vp_pager);
		view = new ArrayList<View>();
		v1 = LayoutInflater.from(this).inflate(R.layout.activity_allorderlist,
				null);
		v2 = LayoutInflater.from(this).inflate(R.layout.activity_allorderlist,
				null);
		v3 = LayoutInflater.from(this).inflate(R.layout.activity_allorderlist,
				null);
		v4 = LayoutInflater.from(this).inflate(R.layout.activity_allorderlist,
				null);
		view.add(v1);
		view.add(v2);
		view.add(v3);
		view.add(v4);

		pager.setAdapter(new MyPagerAdapter(view));
		pager.setOnPageChangeListener(new MyOnPageChangeListener());

		if (flag == 1) {
			pager.setCurrentItem(1);
		} else if (flag == 2) {
			pager.setCurrentItem(2);
		} else if (flag == 3) {
			pager.setCurrentItem(3);
		} else {
			pager.setCurrentItem(0);
		}

		allOrder();
		waitPay();
		waitReceive();
		waitComment();
	}

	public void allOrder() {
		ListView listView1 = (ListView) v1.findViewById(R.id.lv);
		allOrderAdapter = new AllOrderAdapter(this, handler);
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(AllOrderActivity.this,
						OrderDetailActivity.class);
				it.putExtra("orderId", allOrderAdapter.data.get(position)
						.getOrderId());
				it.putExtra("OrderManagerBean",
						allOrderAdapter.data.get(position));
				startActivity(it);
			}
		});
		listView1.setAdapter(allOrderAdapter);
	}

	public void waitPay() {
		ListView listView2 = (ListView) v2.findViewById(R.id.lv);
		listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(AllOrderActivity.this,
						OrderDetailActivity.class);
				it.putExtra("orderId", waitPayOrderAdapter.data.get(position)
						.getOrderId());
				it.putExtra("OrderManagerBean",
						waitPayOrderAdapter.data.get(position));
				startActivity(it);
			}
		});
		waitPayOrderAdapter = new WaitPayOrderAdapter(this);
		listView2.setAdapter(waitPayOrderAdapter);
	}

	public void waitReceive() {
		ListView listView3 = (ListView) v3.findViewById(R.id.lv);
		listView3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(AllOrderActivity.this,
						OrderDetailActivity.class);
				it.putExtra("orderId",
						waitReceiveOrderAdapter.data.get(position).getOrderId());
				it.putExtra("OrderManagerBean",
						waitReceiveOrderAdapter.data.get(position));
				startActivity(it);
			}
		});
		waitReceiveOrderAdapter = new WaitReceiveOrderAdapter(this);
		listView3.setAdapter(waitReceiveOrderAdapter);
	}

	public void waitComment() {
		ListView listView4 = (ListView) v4.findViewById(R.id.lv);
		listView4.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(AllOrderActivity.this,
						OrderDetailActivity.class);
				it.putExtra("orderId",
						waitCommentOrderAdapter.data.get(position).getOrderId());
				it.putExtra("OrderManagerBean",
						waitCommentOrderAdapter.data.get(position));
				startActivity(it);
			}
		});
		waitCommentOrderAdapter = new WaitCommentOrderAdapter(this);
		listView4.setAdapter(waitCommentOrderAdapter);
	}

	private void orderManager(String type) {
		showLoad("");
		OrderManagerRequest request = new OrderManagerRequest();
		request.setMethod("orderManager");
		request.setUserId(getFromSharePreference(Config.userConfig.pkregister));
		request.setType(type);
		logUrl(request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ORDERMANAGER,
				request.toString());
	}

	@Override
	public void getOrderManagerListSuccess(OrderManagerList list) {
		// TODO Auto-generated method stub
		super.getOrderManagerListSuccess(list);
		dismiss();
		switch (pager.getCurrentItem()) {
		case 0:
			allOrderAdapter.data.clear();
			allOrderAdapter.data.addAll(list.getItems());
			allOrderAdapter.notifyDataSetChanged();
			break;
		case 1:
			waitPayOrderAdapter.data.clear();
			waitPayOrderAdapter.data.addAll(list.getItems());
			waitPayOrderAdapter.notifyDataSetChanged();
			break;
		case 2:
			waitReceiveOrderAdapter.data.clear();
			waitReceiveOrderAdapter.data.addAll(list.getItems());
			waitReceiveOrderAdapter.notifyDataSetChanged();
			break;
		case 3:
			waitCommentOrderAdapter.data.clear();
			waitCommentOrderAdapter.data.addAll(list.getItems());
			waitCommentOrderAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				status = "1";
				all_order.setTextColor(getResources().getColor(
						R.color.text_selected));
				wait_pay.setTextColor(getResources().getColor(
						R.color.text_coupon_normal));
				waite_delivery.setTextColor(getResources().getColor(
						R.color.text_coupon_normal));
				waite_comment.setTextColor(getResources().getColor(
						R.color.text_coupon_normal));
				cursor1.setVisibility(View.VISIBLE);
				cursor2.setVisibility(View.INVISIBLE);
				cursor3.setVisibility(View.INVISIBLE);
				cursor4.setVisibility(View.INVISIBLE);
				// manageXnOrder("1", "1", "100");
				orderManager("1");
				break;
			case 1:
				status = "2";
				all_order.setTextColor(getResources().getColor(
						R.color.text_coupon_normal));
				wait_pay.setTextColor(getResources().getColor(
						R.color.text_selected));
				waite_delivery.setTextColor(getResources().getColor(
						R.color.text_coupon_normal));
				waite_comment.setTextColor(getResources().getColor(
						R.color.text_coupon_normal));
				cursor2.setVisibility(View.VISIBLE);
				cursor1.setVisibility(View.INVISIBLE);
				cursor3.setVisibility(View.INVISIBLE);
				cursor4.setVisibility(View.INVISIBLE);
				// manageXnOrder("2", "1", "100");
				orderManager("2");
				break;
			case 2:
				status = "3";
				all_order.setTextColor(getResources().getColor(
						R.color.text_coupon_normal));
				wait_pay.setTextColor(getResources().getColor(
						R.color.text_coupon_normal));
				waite_delivery.setTextColor(getResources().getColor(
						R.color.text_selected));
				waite_comment.setTextColor(getResources().getColor(
						R.color.text_coupon_normal));
				cursor3.setVisibility(View.VISIBLE);
				cursor2.setVisibility(View.INVISIBLE);
				cursor1.setVisibility(View.INVISIBLE);
				cursor4.setVisibility(View.INVISIBLE);
				// manageXnOrder("3", "1", "100");
				orderManager("3");
				break;
			case 3:
				status = "4";
				all_order.setTextColor(getResources().getColor(
						R.color.text_coupon_normal));
				wait_pay.setTextColor(getResources().getColor(
						R.color.text_coupon_normal));
				waite_comment.setTextColor(getResources().getColor(
						R.color.text_selected));
				waite_delivery.setTextColor(getResources().getColor(
						R.color.text_coupon_normal));
				cursor4.setVisibility(View.VISIBLE);
				cursor2.setVisibility(View.INVISIBLE);
				cursor3.setVisibility(View.INVISIBLE);
				cursor1.setVisibility(View.INVISIBLE);
				// manageXnOrder("4", "1", "100");
				orderManager("4");
				break;
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	private class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(ViewGroup container) {

		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			super.registerDataSetObserver(observer);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
			super.restoreState(state, loader);
		}

		@Override
		public Parcelable saveState() {
			return super.saveState();
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			super.setPrimaryItem(container, position, object);
		}

		@Override
		public void startUpdate(ViewGroup container) {
			super.startUpdate(container);
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			super.unregisterDataSetObserver(observer);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_order:
			pager.setCurrentItem(0);
			break;
		case R.id.ll_pay:
			pager.setCurrentItem(1);
			break;
		case R.id.ll_delivery:
			pager.setCurrentItem(2);
			break;
		case R.id.ll_comment:
			pager.setCurrentItem(3);
			break;
		}
	}

	// @Override
	// public void confirmOrderCallBack(String id) {
	// ConfirmOrderRequest request = new ConfirmOrderRequest();
	// request.setMethod("comfireorder");
	// request.setOrderId(id);
	// CoreHttpClient.listen = this;
	// CoreHttpClient.get(this, Constants.REQUEST_TYPE.CONFIRM_ORDER,
	// request.toString());
	// }
	//
	// @Override
	// public void getRegisterSuccess() {
	// super.getRegisterSuccess();
	// showToast("确认收货成功");
	// if (status.equals("1")) {
	// manageXnOrder("1", "1", "100");
	// } else {
	// manageXnOrder("3", "1", "100");
	// }
	// }
	//
	// @Override
	// public void deleteOrderCallBack(String id) {
	// ConfirmOrderRequest request = new ConfirmOrderRequest();
	// request.setMethod("delXnOrderById");
	// request.setOrderId(id);
	// CoreHttpClient.listen = this;
	// CoreHttpClient.get(this, Constants.REQUEST_TYPE.DELETE_ORDER,
	// request.toString());
	// }
	//
	// @Override
	// public void delXnCollectionSuccess() {
	// super.delXnCollectionSuccess();
	// showToast("删除订单成功");
	// if (status.equals("1")) {
	// manageXnOrder("1", "1", "100");
	// } else if (status.equals("2")) {
	// manageXnOrder("2", "1", "100");
	// } else {
	// manageXnOrder("4", "1", "100");
	// }
	// }
}
