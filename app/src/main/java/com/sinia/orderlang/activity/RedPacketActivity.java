package com.sinia.orderlang.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
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
import com.bjypt.vipcard.activity.MainActivity;
import com.bjypt.vipcard.common.Config;
import com.sinia.orderlang.adapter.RedPacketAdapter;
import com.sinia.orderlang.adapter.RedPacketUsedAdapter;
import com.sinia.orderlang.adapter.RedPacketWaitUseAdapter;
import com.sinia.orderlang.bean.RedBaoManagerList;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.RedBaoManagerRequest;
import com.sinia.orderlang.utils.Constants;

/**
 * 红包
 */
public class RedPacketActivity extends BaseActivity implements OnClickListener {
	private TextView all_red_packet, wait_use, tv_used;
	private ImageView cursor1, cursor2, cursor3;
	private ViewPager vp_pager;
	private View v1, v2, v3;
	private RedPacketAdapter redPacketAdapter;
	private RedPacketWaitUseAdapter redPacketWaitUseAdapter;
	private RedPacketUsedAdapter redPacketUsedAdapter;
	private LinearLayout ll_all, ll_wait_use, ll_use;
	private List<View> viewList = new ArrayList<View>();
	private String type1 = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_red_packet, "红包管理");
		getDoingView().setVisibility(View.GONE);
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(MainActivity.SECKILL_FLAG_LIST == 2&&MainActivity.SECKILL_FLAG ==2){
			finish();
			MainActivity.SECKILL_FLAG_LIST = 1;
			MainActivity.SECKILL_FLAG = 1;
		}
		redBaoManager();
	}

	private void redBaoManager() {
		showLoad("");
		RedBaoManagerRequest request = new RedBaoManagerRequest();
		request.setMethod("redBaoManager");
		request.setUserId(getFromSharePreference(Config.userConfig.pkregister));
		request.setType(type1);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.REDBAOMANAGER,
				request.toString());
	}

	@Override
	public void getRedBaoManagerListSuccess(RedBaoManagerList list) {
		// TODO Auto-generated method stub
		super.getRedBaoManagerListSuccess(list);
		dismiss();
		switch (type1) {
		case "1":
			redPacketAdapter.data.clear();
			redPacketAdapter.data.addAll(list.getItems());
			redPacketAdapter.notifyDataSetChanged();
			break;
		case "2":
			redPacketWaitUseAdapter.data.clear();
			redPacketWaitUseAdapter.data.addAll(list.getItems());
			redPacketWaitUseAdapter.notifyDataSetChanged();
			break;
		case "3":
			redPacketUsedAdapter.data.clear();
			redPacketUsedAdapter.data.addAll(list.getItems());
			redPacketUsedAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		super.requestFailed();
		dismiss();
		showToast("获取列表失败");
	}

	private void initView() {
		all_red_packet = (TextView) findViewById(R.id.all_red_packet);
		wait_use = (TextView) findViewById(R.id.wait_use);
		tv_used = (TextView) findViewById(R.id.tv_used);
		all_red_packet.setOnClickListener(this);
		wait_use.setOnClickListener(this);
		tv_used.setOnClickListener(this);
		vp_pager = (ViewPager) findViewById(R.id.vp_pager);
		cursor1 = (ImageView) findViewById(R.id.cursor1);
		cursor2 = (ImageView) findViewById(R.id.cursor2);
		cursor3 = (ImageView) findViewById(R.id.cursor3);
		v1 = LayoutInflater.from(this).inflate(R.layout.activity_redpacketlist,
				null);
		v2 = LayoutInflater.from(this).inflate(R.layout.activity_redpacketlist,
				null);
		v3 = LayoutInflater.from(this).inflate(R.layout.activity_redpacketlist,
				null);
		redPacketAll();
		redPacketWaitUse();
		redPacketUsed();
		viewList.add(v1);
		viewList.add(v2);
		viewList.add(v3);
		vp_pager.setAdapter(new MyPagerAdapter(viewList));
		vp_pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					all_red_packet.setTextColor(getResources().getColor(
							R.color.text_selected));
					wait_use.setTextColor(getResources().getColor(
							R.color.text_coupon_normal));
					tv_used.setTextColor(getResources().getColor(
							R.color.text_coupon_normal));
					cursor1.setVisibility(View.VISIBLE);
					cursor2.setVisibility(View.INVISIBLE);
					cursor3.setVisibility(View.INVISIBLE);
					type1 = "1";
					redBaoManager();
					break;
				case 1:
					wait_use.setTextColor(getResources().getColor(
							R.color.text_selected));
					all_red_packet.setTextColor(getResources().getColor(
							R.color.text_coupon_normal));
					tv_used.setTextColor(getResources().getColor(
							R.color.text_coupon_normal));
					cursor2.setVisibility(View.VISIBLE);
					cursor1.setVisibility(View.INVISIBLE);
					cursor3.setVisibility(View.INVISIBLE);
					type1 = "2";
					redBaoManager();
					break;
				case 2:
					tv_used.setTextColor(getResources().getColor(
							R.color.text_selected));
					all_red_packet.setTextColor(getResources().getColor(
							R.color.text_coupon_normal));
					wait_use.setTextColor(getResources().getColor(
							R.color.text_coupon_normal));
					cursor3.setVisibility(View.VISIBLE);
					cursor1.setVisibility(View.INVISIBLE);
					cursor2.setVisibility(View.INVISIBLE);
					type1 = "3";
					redBaoManager();
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void redPacketAll() {
		ListView listview = (ListView) v1.findViewById(R.id.lv);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(RedPacketActivity.this,
						RedPacketDetailActivity.class);
				it.putExtra("orderId", redPacketAdapter.data.get(position)
						.getOrderId());
				it.putExtra("status", redPacketAdapter.data.get(position)
						.getRedbaoStatus());
				startActivity(it);
			}
		});
		redPacketAdapter = new RedPacketAdapter(this);
		listview.setAdapter(redPacketAdapter);
	}

	private void redPacketWaitUse() {
		ListView listview = (ListView) v2.findViewById(R.id.lv);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(RedPacketActivity.this,
						RedPacketDetailActivity.class);
				it.putExtra("orderId",
						redPacketWaitUseAdapter.data.get(position).getOrderId());
				it.putExtra("status", redPacketWaitUseAdapter.data.get(position)
						.getRedbaoStatus());
				startActivity(it);
			}
		});
		redPacketWaitUseAdapter = new RedPacketWaitUseAdapter(this);
		listview.setAdapter(redPacketWaitUseAdapter);
	}

	private void redPacketUsed() {
		ListView listview = (ListView) v3.findViewById(R.id.lv);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(RedPacketActivity.this,
						RedPacketDetailActivity.class);
				it.putExtra("orderId", redPacketUsedAdapter.data.get(position)
						.getOrderId());
				it.putExtra("status", redPacketAdapter.data.get(position)
						.getRedbaoStatus());
				startActivity(it);
			}
		});
		redPacketUsedAdapter = new RedPacketUsedAdapter(this);
		listview.setAdapter(redPacketUsedAdapter);
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
		case R.id.all_red_packet:
			vp_pager.setCurrentItem(0);
			break;
		case R.id.wait_use:
			vp_pager.setCurrentItem(1);
			break;
		case R.id.tv_used:
			vp_pager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}
}
