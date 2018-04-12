package com.sinia.orderlang.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.sinia.orderlang.activity.CompleteOrderActivity;
import com.sinia.orderlang.activity.LangMainActivity;
import com.sinia.orderlang.adapter.ShopCarAdapter;
import com.sinia.orderlang.adapter.ShopCarAdapter.ischeck;
import com.sinia.orderlang.bean.CarCheckBean;
import com.sinia.orderlang.bean.CarListBean;
import com.sinia.orderlang.bean.GoodItemsBean;
import com.sinia.orderlang.bean.GroupBean;
import com.sinia.orderlang.bean.MerchantItemsBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.CarDeleteRequest;
import com.sinia.orderlang.request.CarListRequest;
import com.sinia.orderlang.utils.Constants;

public class ShoppingCartFragment extends BaseFragment implements
		OnClickListener, ischeck {

	private View v, emptyView;
	private Activity activity;
	private LayoutInflater inflater;
	private ImageView back_shoppcar;
	private TextView tv_edit, tv_goods_num, tv_money, tv_freight;

	/**
	 * 去结算或者删除购物车
	 */
	private LinearLayout goto_pay;
	private ExpandableListView listView;
	private ImageView iv_check;
	private boolean checkAll = true, isEdit = true;
	private ShopCarAdapter adapter;
	private CarListBean carListBean;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				// 全选按钮是否选中，总价、运费变更
				boolean checkA = true;
				for (int i = 0; i < adapter.childs.size(); i++) {
					String key = adapter.groups.get(i).getShopName();
					List<GoodItemsBean> data = adapter.childs.get(key);
					for (GoodItemsBean bean : data) {
						if (bean.isChecked()) {
						} else {
							checkA = false;
						}
					}
				}
				if (isEdit) {
					tv_goods_num.setText("去结算" + "(" + allSize() + ")");
				} else {
					tv_goods_num.setText("删除" + "(" + allSize() + ")");
				}
				tv_money.setText("¥" + allPrice());
				checkAll = checkA;
				if (checkA) {
					iv_check.setImageResource(R.drawable.car_check);
				} else {
					iv_check.setImageResource(R.drawable.car_unckeck);
				}
				break;
			case 101:
				getData();
				break;
			default:
				break;
			}
		};
	};

	// 选中的数量
	private int allSize() {
		int allSize = 0;
		for (int i = 0; i < adapter.childs.size(); i++) {
			String key = adapter.groups.get(i).getShopName();
			List<GoodItemsBean> data = adapter.childs.get(key);
			for (GoodItemsBean bean : data) {
				if (bean.isChecked()) {
					allSize = allSize + Integer.parseInt(bean.getGoodNum());
				}
			}
		}
		return allSize;
	}

	// 选中的价格
	private float allPrice() {
		float allPrice = 0f;
		for (int i = 0; i < adapter.childs.size(); i++) {
			String key = adapter.groups.get(i).getShopName();
			List<GoodItemsBean> data = adapter.childs.get(key);
			for (GoodItemsBean bean : data) {
				if (bean.isChecked()) {
					allPrice = allPrice + Float.parseFloat(bean.getGoodPrice());
				}
			}
		}

		return allPrice;
	}

	// 选中的carID
	private String transIds() {
		String ids = "";
		for (int i = 0; i < adapter.childs.size(); i++) {
			String key = adapter.groups.get(i).getShopName();
			List<GoodItemsBean> data = adapter.childs.get(key);
			boolean flag = false;
			for (GoodItemsBean bean : data) {
				if (bean.isChecked()) {
					flag = true;
					ids = ids + bean.getCarId() + "!";
				}
			}
			if (flag) {
				ids = ids + ";";
			}
		}

		return ids;
	}

	// 删除时选中的carID
	private String transIds2() {
		String ids = "";
		for (int i = 0; i < adapter.childs.size(); i++) {
			String key = adapter.groups.get(i).getShopName();
			List<GoodItemsBean> data = adapter.childs.get(key);
			boolean flag = false;
			for (GoodItemsBean bean : data) {
				if (bean.isChecked()) {
					flag = true;
					ids = ids + bean.getCarId() + ",";
				}
			}
		}

		return ids;
	}
	
	// 选中的bean
	private List<GoodItemsBean> getCheckItem() {
		List<GoodItemsBean> list = new ArrayList<GoodItemsBean>();
		for (int i = 0; i < adapter.childs.size(); i++) {
			String key = adapter.groups.get(i).getShopName();
			List<GoodItemsBean> data = adapter.childs.get(key);
			for (GoodItemsBean bean : data) {
				if (bean.isChecked()) {
					list.add(bean);
				}
			}
		}
		return list;
	}

	// 选中的数量组合成String
	private String getCheckNum() {
		String ids = "";
		for (int i = 0; i < adapter.childs.size(); i++) {
			String key = adapter.groups.get(i).getShopName();
			List<GoodItemsBean> data = adapter.childs.get(key);
			boolean flag = false;
			for (GoodItemsBean bean : data) {
				if (bean.isChecked()) {
					flag = true;
					ids = ids + bean.getGoodNum() + "!";
				}
			}
			if (flag) {
				ids = ids + ";";
			}
		}
		return ids;
	}

	// 选中的价格组合成String
	private String getCheckPrice() {
		String ids = "";
		for (int i = 0; i < adapter.childs.size(); i++) {
			String key = adapter.groups.get(i).getShopName();
			List<GoodItemsBean> data = adapter.childs.get(key);
			boolean flag = false;
			for (GoodItemsBean bean : data) {
				if (bean.isChecked()) {
					flag = true;
					ids = ids + bean.getGoodPrice() + "!";
				}
			}
			if (flag) {
				ids = ids + ";";
			}
		}
		return ids;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		getData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeAllViewsInLayout();
		}
		return v;
	}

	private void initView() {
		activity = getActivity();
		inflater = LayoutInflater.from(activity);
		v = inflater.inflate(R.layout.fragment_shop_car, null);
		emptyView = inflater.inflate(R.layout.view_no_shop_car, null);
		tv_edit = (TextView) v.findViewById(R.id.tv_edit);
		tv_goods_num = (TextView) v.findViewById(R.id.tv_goods_num);
		back_shoppcar = (ImageView) v.findViewById(R.id.back_shoppcar);
		tv_money = (TextView) v.findViewById(R.id.tv_money);
		tv_freight = (TextView) v.findViewById(R.id.tv_freight);
		goto_pay = (LinearLayout) v.findViewById(R.id.goto_pay);
		listView = (ExpandableListView) v.findViewById(R.id.listView);
		listView.setEmptyView(emptyView);
		iv_check = (ImageView) v.findViewById(R.id.iv_check);
		back_shoppcar.setOnClickListener(this);
		tv_edit.setOnClickListener(this);
		goto_pay.setOnClickListener(this);
		iv_check.setOnClickListener(this);
		adapter = new ShopCarAdapter(getActivity(), handler);
		adapter.setischek(this);
		listView.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("lamp", "ShopCarFragment ----- onresume");
//		getData();
	}

	
	
	public void getData() {
		showLoad("");
		CarListRequest request = new CarListRequest();
		request.setMethod("carList");
		request.setUserId(SharedPreferenceUtils.getFromSharedPreference(getActivity(), Config.userConfig.pkregister));
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.get(getActivity(), request.toString(),
				new HttpCallBackListener() {

					@Override
					public void onSuccess(JSONObject json) {
						Log.d("result", json.toString());
						dismiss();
						if (json.optInt("isSuccessful") == 0) {
							Gson gson = new Gson();
							adapter.groups.clear();
							adapter.childs.clear();
							carListBean = gson.fromJson(
									json.toString(), CarListBean.class);
							List<MerchantItemsBean> list = carListBean
									.getMerchantItems();
							tv_freight.setText("运费：" + carListBean.getFreight());
							for (MerchantItemsBean bean : list) {
								GroupBean groupBean = new GroupBean();
								groupBean.setShopName(bean.getMerchantName());
								adapter.groups.add(groupBean);
								List<GoodItemsBean> data = new ArrayList<GoodItemsBean>();
								data.addAll(bean.getGoodItems());
								adapter.childs.put(bean.getMerchantName(), data);
								Log.d("lamp",
										"adapter.groups.name"
												+ bean.getMerchantName()
												+ ",value size="
												+ adapter.childs.get(
														bean.getMerchantName())
														.size());
							}
							// 结算、删除按钮点击会显示不同的字样
							if (checkAll) {
								iv_check.setImageResource(R.drawable.car_check);
								tv_goods_num.setText("去结算" + "(" + allSize()
										+ ")");
								tv_money.setText("¥" + allPrice());
							} else {
								iv_check.setImageResource(R.drawable.car_unckeck);
								tv_goods_num.setText("删除" + "(" + 0 + ")");
								tv_money.setText("¥0");
							}
							setCheckAll(isEdit);
							Log.d("lamp", "isEdit=" + isEdit);
							int size = adapter.getGroupCount();
							for (int i = 0; i < size; i++) {
								listView.expandGroup(i);
							}

							// 设置运费

						}
					}

					@Override
					public void onRequestFailed() {
						showToast("请求失败");
					}
				});
	}

	private void deleteCar(String carId) {
		showLoad("");
		CarDeleteRequest request = new CarDeleteRequest();
		request.setMethod("carDelete");
		request.setCarId(carId);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.get(getActivity(), request.toString(),
				new HttpCallBackListener() {

					@Override
					public void onSuccess(JSONObject json) {
						dismiss();
						Log.d("result", json.toString());
						if (json.optInt("isSuccessful") == 0) {
							showToast("删除成功");
							handler.sendEmptyMessage(101);
						} else {
							showToast("删除失败");
						}
					}

					@Override
					public void onRequestFailed() {
						showToast("请求失败");
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_edit:
			if (isEdit) {
				isEdit = false;
				tv_edit.setText("完成");
			} else {
				isEdit = true;
				tv_edit.setText("编辑");
			}
			setCheckAll(isEdit);
			checkAll = isEdit;
			if (checkAll) {
				iv_check.setImageResource(R.drawable.car_check);
				tv_goods_num.setText("去结算" + "(" + allSize() + ")");
			} else {
				iv_check.setImageResource(R.drawable.car_unckeck);
				tv_goods_num.setText("删除" + "(" + allSize() + ")");
			}
			tv_money.setText("¥" + allPrice());
			break;
		case R.id.goto_pay:
			// 删除或者去结算

			if (allSize() > 0) {
				if (isEdit) {
					// 结算
					Intent it = new Intent(getActivity(),
							CompleteOrderActivity.class);
					CarCheckBean bean = new CarCheckBean();
					bean.setData(getCheckItem());
					it.putExtra("fromWhere", true);
					it.putExtra("CarCheckBean", bean);
					it.putExtra("transId", transIds());
					it.putExtra("numId", getCheckNum());
					it.putExtra("priceId", getCheckPrice());
					it.putExtra("allPrice", allPrice()+"");
					it.putExtra("addressid2",carListBean.getAddressid());
					it.putExtra("registername2",carListBean.getRegistername());
					it.putExtra("phoneno2",carListBean.getPhoneno());
					it.putExtra("receiptaddress2",carListBean.getReceiptaddress());
					String freight = tv_freight.getText().toString();
					it.putExtra("freight",
							freight.substring(3, freight.length()));
					Log.d("lamp",
							"transId=" + transIds() + "----numId="
									+ getCheckNum() + "----priceId="
									+ getCheckPrice() + "----freight="
									+ freight.substring(3, freight.length())
									+ "----allPrice=" + allPrice());
					startActivity(it);
					getActivity().finish();
				} else {
					// 删除
					Log.d("lamp", "id=" + transIds2());
					createCancelOrDeleteDialog("确定删除吗？");
				}
			} else {
				showToast("您还没有选择商品哟");
			}
			break;
		case R.id.iv_check:
			// 全选按钮
			if (checkAll) {
				checkAll = false;
				iv_check.setImageResource(R.drawable.car_unckeck);
			} else {
				checkAll = true;
				iv_check.setImageResource(R.drawable.car_check);
			}
			setCheckAll(checkAll);
			handler.sendEmptyMessage(100);
			// if (checkAll) {
			// iv_check.setImageResource(R.drawable.car_check);
			// tv_goods_num.setText("删除" + "(" + allSize() + ")");
			// } else {
			// iv_check.setImageResource(R.drawable.car_unckeck);
			// tv_goods_num.setText("去结算" + "(" + allSize() + ")");
			// }
			// tv_money.setText("¥" + allPrice());
			break;
			case R.id.back_shoppcar:
				activity.finish();
				break;

		default:
			break;
		}
	}

	// 设置全部选中或不选中
	private void setCheckAll(boolean flag) {
		int size = adapter.groups.size();
		for (int i = 0; i < size; i++) {
			adapter.groups.get(i).setChecked(flag);
			ischekgroup(i, flag);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void ischekgroup(int groupposition, boolean ischeck) {
		List<GoodItemsBean> list = adapter.childs.get(adapter.groups.get(
				groupposition).getShopName());
		for (GoodItemsBean bean : list) {
			bean.setChecked(ischeck);
		}
		adapter.groups.get(groupposition).setChecked(ischeck);
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * 
	 * */
	private Dialog createCancelOrDeleteDialog(final String content) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View v = inflater.inflate(R.layout.dialog_loginout, null);
		final Dialog dialog = new AlertDialog.Builder(getActivity()).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		WindowManager windowManager = ((Activity) getActivity()).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (display.getWidth() - 100); // 设置宽度
		lp.height = (display.getHeight() * 2 / 5); // 设置高度
		dialog.getWindow().setAttributes(lp);
		dialog.setContentView(v, lp);

		final TextView ok = (TextView) dialog.findViewById(R.id.tv_add);
		final TextView tv_name = (TextView) dialog.findViewById(R.id.tv_name);
		final TextView tv_cancel = (TextView) dialog
				.findViewById(R.id.tv_cancel);
		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		tv_name.setText(content);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				deleteCar(transIds2());
			}
		});
		return dialog;
	}
}
