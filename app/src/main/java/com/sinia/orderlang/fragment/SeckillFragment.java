package com.sinia.orderlang.fragment;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.bjypt.vipcard.R;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.google.gson.Gson;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sinia.orderlang.activity.LangMainActivity;
import com.sinia.orderlang.activity.Search1Activity;
import com.sinia.orderlang.activity.SeckillDetailActivity;
import com.sinia.orderlang.activity.ShowDialogActivity;
import com.sinia.orderlang.adapter.SecKillAdapter;
import com.sinia.orderlang.base.BaseFragment;
import com.sinia.orderlang.bean.RedpacketBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.RedBaoListRequest;
import com.sinia.orderlang.utils.Constants;
//import com.tencent.map.geolocation.TencentLocation;
//import com.tencent.map.geolocation.TencentLocationListener;
//import com.tencent.map.geolocation.TencentLocationManager;
//import com.tencent.map.geolocation.TencentLocationRequest;

public class SeckillFragment extends BaseFragment implements OnClickListener
//		, TencentLocationListener
{
	private View rootView;
	private PullToRefreshListView listview;
	private ImageView back, search;
	private RelativeLayout rl_classify, rl_area;
	private SecKillAdapter adapter;
	private TextView tv_classify, tv_area;
	public static String city = "-1", areaName = "-1", goodName = "-1",
			goodType = "-1";
	private boolean flag = false;
	private boolean isRefresh;
	public static RedpacketBean redpacketBean;
	private LangMainActivity activity;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				flag = true;
				redBaoList(city, areaName, goodType, goodName);
				break;
			case 300:
				// listview.onRefreshComplete();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		// initLocat();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (activity.showWaht) {
			if (city.equals("-1")) {
				tv_area.setText("区域");
			} else {
				tv_area.setText(city);
			}
			Log.d("lamp", "goodType=" + goodType);
			if (goodType.equals("-1")) {
				tv_classify.setText("分类");
			} else {
				int pos = Integer.parseInt(goodType);
				tv_classify.setText(redpacketBean.getArrayXnGoodTypeItems()
						.get(pos - 1).getTypeName());
			}
			redBaoList(SeckillFragment.city, SeckillFragment.areaName,
					goodType, goodName);
		}
		Log.d("lamp", "SeckillFragment---onResume");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeAllViewsInLayout();
		}
		return rootView;
	}

	// 定位请求
	/*private void initLocat() {
		Log.d("lamp", "initLocat");
		TencentLocationRequest request = TencentLocationRequest.create();
		request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
		TencentLocationManager.getInstance(getActivity())
				.requestLocationUpdates(request, this);
	}*/

	private void redBaoList(String city, String areaname, String goodType,
			String goodName) {
		showLoad("");
		RedBaoListRequest request = new RedBaoListRequest();
		request.setMethod("redBaoList");
		request.setCity(city);
		request.setAreaName(areaname);
		request.setGoodType(goodType);
		request.setGoodName(goodName);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.get(getActivity(), request.toString(),
				new HttpCallBackListener() {

					@Override
					public void onSuccess(JSONObject json) {
						Log.d("result", json.toString());
						dismiss();
						if (json.optInt("isSuccessful") == 0
								&& json.optInt("state") == 0) {
							Gson gson = new Gson();
							RedpacketBean redpacketBean1 = gson.fromJson(
									json.toString(), RedpacketBean.class);
							redpacketBean = redpacketBean1;
							adapter.data.clear();
							adapter.data.addAll(redpacketBean1.getGoodItems());
							adapter.notifyDataSetChanged();
							// if (isRefresh) {
							// listview.onRefreshComplete();
							// }
						}

					}

					@Override
					public void onRequestFailed() {
						dismiss();
						showToast("网络异常");
					}
				});
	}

	private void initView() {
		rootView = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_seckill, null);
		activity = (LangMainActivity) getActivity();
		listview = (PullToRefreshListView) rootView.findViewById(R.id.listview);
		back = (ImageView) rootView.findViewById(R.id.back);
		search = (ImageView) rootView.findViewById(R.id.search);
		rl_classify = (RelativeLayout) rootView.findViewById(R.id.rl_classify);
		rl_area = (RelativeLayout) rootView.findViewById(R.id.rl_area);
		tv_classify = (TextView) rootView.findViewById(R.id.tv_classify);
		tv_area = (TextView) rootView.findViewById(R.id.tv_area);

		adapter = new SecKillAdapter(getActivity());
		listview.setAdapter(adapter);
		rl_classify.setOnClickListener(this);
		rl_area.setOnClickListener(this);
		search.setOnClickListener(this);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(getActivity(),
						SeckillDetailActivity.class);
				it.putExtra("goodId", adapter.data.get(position).getGoodId());
				startActivity(it);

			}
		});
		// listview.setOnRefreshListener(new OnRefreshListener<ListView>() {
		//
		// @Override
		// public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// isRefresh = true;
		// redBaoList(city, areaName, goodType, goodName);
		// }
		// });
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_classify:
			Intent it = new Intent(getActivity(), ShowDialogActivity.class);
			it.putExtra("showWhat", false);
			it.putExtra("secOrSpec", 1);
			startActivityForResult(it, 100);
			break;
		case R.id.rl_area:
			Intent it2 = new Intent(getActivity(), ShowDialogActivity.class);
			it2.putExtra("showWhat", true);
			it2.putExtra("secOrSpec", 1);
			startActivityForResult(it2, 101);
			break;
		case R.id.search:
			Intent it3 = new Intent(getActivity(), Search1Activity.class);
			it3.putExtra("flag", true);
			it3.putExtra("secOrSpec", 1);
			startActivity(it3);
			break;
		default:
			break;
		}
	}

	/*@Override
	public void onLocationChanged(TencentLocation tencentLocation, int code,
			String arg2) {
		switch (code) {
		case TencentLocation.ERROR_OK:
			Log.d("lamp",
					tencentLocation.getProvince() + tencentLocation.getCity()
							+ tencentLocation.getDistrict());
			String location = tencentLocation.getProvince()
					+ tencentLocation.getCity() + tencentLocation.getDistrict();
			Log.d("lamp", location);
			city = tencentLocation.getCity();
			areaName = tencentLocation.getDistrict();
			tv_area.setText(city);
			handler.sendEmptyMessage(100);
			TencentLocationManager.getInstance(getActivity()).removeUpdates(
					this);
			break;
		case TencentLocation.ERROR_NETWORK:
			// showToast("网络异常");
			break;
		case TencentLocation.ERROR_BAD_JSON:
			// showToast("定位失败，请手动搜索城市");
			break;
		case TencentLocation.ERROR_UNKNOWN:
			// showToast("定位失败，请手动搜索城市");
			break;
		default:
			break;
		}

	}*/

	/*@Override
	public void onStatusUpdate(String arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}*/

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 100:

				break;
			case 101:

				break;
			default:
				break;
			}
		}
	}
}
