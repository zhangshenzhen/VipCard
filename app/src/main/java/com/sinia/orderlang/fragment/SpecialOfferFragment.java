package com.sinia.orderlang.fragment;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.google.gson.Gson;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sinia.orderlang.activity.GoodsDetailActivity;
import com.sinia.orderlang.activity.LangMainActivity;
import com.sinia.orderlang.activity.Search1Activity;
import com.sinia.orderlang.activity.ShowDialogActivity;
import com.sinia.orderlang.adapter.SpecialOfferAdapter;
import com.sinia.orderlang.bean.TeJiaListList;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.TeJiaListRequest;
import com.sinia.orderlang.utils.Constants;

public class SpecialOfferFragment extends BaseFragment implements
		OnClickListener {
	private View rootView;
	private TextView tv1, tv_classify, tv_area;
	private ListView listview;
	private ImageView back, search;
	private RelativeLayout rl_classify, rl_area, rl_sifting;
	private SpecialOfferAdapter adapter;
	private PopupWindow popWindow;
	/**
	 * conditional 同城送
	 */
	public static String conditional = "-1", goodType = "-1";
	public static String city = SeckillFragment.city,
			areaName = SeckillFragment.areaName, goodName = "-1";
	public static TeJiaListList teJiaListList;
	private boolean isRefresh;
	private LangMainActivity activity;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// case 300:
			// listview.onRefreshComplete();
			// break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("lamp", "activity.showWaht="+activity.showWaht);
		if (!activity.showWaht) {
			if (city.equals("-1")) {
				tv_area.setText("区域");
			} else {
				tv_area.setText(city);
			}
			if (goodType.equals("-1")) {
				tv_classify.setText("分类");
			} else {
				int pos = Integer.parseInt(goodType);
				tv_classify.setText(teJiaListList.getArrayXnGoodTypeItems()
						.get(pos - 1).getTypeName());
			}
			teJiaList();
		}
		Log.d("lamp", "SpecialOfferFragment---onResume");
	}

	private void teJiaList() {
		showLoad("");
		TeJiaListRequest request = new TeJiaListRequest();
		request.setMethod("teJiaList");
		request.setConditional(conditional);
		request.setGoodType(goodType);
		request.setCity(city);
		request.setAreaName(areaName);
		request.setGoodName(goodName);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.get(getActivity(), request.toString(),
				new HttpCallBackListener() {

					@Override
					public void onSuccess(JSONObject json) {
						Log.d("result", json.toString());
						dismiss();
						if (json.optInt("isSuccessful") == 0) {
							Gson gson = new Gson();
							TeJiaListList list = gson.fromJson(json.toString(),
									TeJiaListList.class);
							teJiaListList = list;
							adapter.data.clear();
							adapter.data.addAll(list.getGoodItems());
							adapter.notifyDataSetChanged();
							// if (isRefresh) {
							// listview.onRefreshComplete();
							// }
						}
					}

					@Override
					public void onRequestFailed() {

					}
				});
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

	private void initView() {
		rootView = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_special_offer, null);
		activity = (LangMainActivity) getActivity();
		listview = (ListView) rootView.findViewById(R.id.listview);
		tv1 = (TextView) rootView.findViewById(R.id.tv1);
		tv_classify = (TextView) rootView.findViewById(R.id.tv_classify);
		tv_area = (TextView) rootView.findViewById(R.id.tv_area);
		back = (ImageView) rootView.findViewById(R.id.back);
		search = (ImageView) rootView.findViewById(R.id.search);
		rl_classify = (RelativeLayout) rootView.findViewById(R.id.rl_classify);
		rl_sifting = (RelativeLayout) rootView.findViewById(R.id.rl_sifting);
		rl_area = (RelativeLayout) rootView.findViewById(R.id.rl_area);
		adapter = new SpecialOfferAdapter(getActivity());
		listview.setAdapter(adapter);
		rl_sifting.setOnClickListener(this);
		rl_classify.setOnClickListener(this);
		rl_area.setOnClickListener(this);
		search.setOnClickListener(this);
		// listview.setOnRefreshListener(new OnRefreshListener<ListView>() {
		//
		// @Override
		// public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// isRefresh = true;
		// teJiaList();
		// }
		// });
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 跳转到特价商品详情页
				Intent it = new Intent(getActivity(), GoodsDetailActivity.class);
				it.putExtra("goodId", adapter.data.get(position - 1)
						.getGoodId());
				startActivity(it);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_sifting:
			showPopWindow();
			break;
		case R.id.rl_classify:
			Intent it = new Intent(getActivity(), ShowDialogActivity.class);
			it.putExtra("showWhat", false);
			it.putExtra("secOrSpec", 2);
			startActivity(it);
			break;
		case R.id.rl_area:
			Intent it2 = new Intent(getActivity(), ShowDialogActivity.class);
			it2.putExtra("showWhat", true);
			it2.putExtra("secOrSpec", 2);
			startActivity(it2);
			break;
		case R.id.search:
			Intent it3 = new Intent(getActivity(), Search1Activity.class);
			it3.putExtra("secOrSpec", 2);
			startActivity(it3);
			break;
		default:
			break;
		}
	}

	private void showPopWindow() {
		LayoutInflater layoutInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.layout_sifting_popwindow,
				null);
		popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		popWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation2);
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		popWindow.setBackgroundDrawable(dw);
		popWindow.showAsDropDown(rl_sifting, 0, 0);
		WindowManager.LayoutParams lp = getActivity().getWindow()
				.getAttributes();
		// lp.alpha = 0.7f;
		lp.alpha = 1f;
		getActivity().getWindow().setAttributes(lp);
		popWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
			}
		});

		TextView tv_comprehensive = (TextView) view
				.findViewById(R.id.tv_comprehensive);
		TextView tv_same_city = (TextView) view.findViewById(R.id.tv_same_city);
		TextView tv_techan = (TextView) view.findViewById(R.id.tv_techan);
		tv_comprehensive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tv1.setText("综合");
				popWindow.dismiss();
				conditional = "-1";
				teJiaList();
			}
		});
		tv_same_city.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tv1.setText("同城送");
				popWindow.dismiss();
				conditional = "1";
				teJiaList();
			}
		});
		tv_techan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tv1.setText("特产区");
				popWindow.dismiss();
				conditional = "2";
				teJiaList();
			}
		});
	}

}
