package com.sinia.orderlang.activity;

import java.util.HashMap;
import java.util.Map.Entry;

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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.sinia.orderlang.adapter.MyAddressAdapter;
import com.sinia.orderlang.bean.AddressListBean;
import com.sinia.orderlang.bean.AddressListList;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.AddressListRequest;
import com.sinia.orderlang.request.AddressSetMoRenRequest;
import com.sinia.orderlang.request.DelAddressRequest;
import com.sinia.orderlang.utils.Constants;


/**
 * 我的地址管理
 */
public class AddressManageActivity extends BaseActivity {

	private ListView listView;
	private TextView tv_add;
	private MyAddressAdapter adapter;
	public static HashMap<Integer, String> toDeleteAddressId = new HashMap<Integer, String>();
	// private List<QueryXnAddressListItemsBean> adapterList = new
	// ArrayList<>();
	public static String fromWhere = "111";
	// private List<Integer> deletePosition = new ArrayList<>();
	public int index;
	private boolean canSelect;
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				index = msg.arg1;
				setDefaltAddress(adapter.data.get(index).getAddressId());
				break;
			case 101:
				getAddressList();
				break;
			case 102:
				int pos = msg.arg1;
				Intent it = getIntent();
				AddressListBean aBean = adapter.data.get(pos);
				it.putExtra("AddressListBean", aBean);
				setResult(RESULT_OK, it);
				finish();
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_addressss, "地址管理");
		getDoingView().setVisibility(View.VISIBLE);
		getDoingView().setText("编辑");
		fromWhere = getIntent().getStringExtra("fromWhere");
		canSelect = getIntent().getBooleanExtra("canSelect", false);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getAddressList();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.listView);
		tv_add = (TextView) findViewById(R.id.tv_add);
		adapter = new MyAddressAdapter(this,handler,canSelect);
		listView.setAdapter(adapter);
		tv_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (adapter.isEditStatus()) {
					if (!toDeleteAddressId.isEmpty()) {
						// delXnAddress(getOtherIds(toDeleteAddressId));
						createCancelOrDeleteDialog("确定删除吗？");
					} else {
						showToast("请选择删除内容");
					}
				} else {
					startActivityForNoIntent(AddAddressActivity.class);
				}
			}
		});
	}

	public String getOtherIds(HashMap<Integer, String> map) {
		String strIds = "";
		for (Entry<Integer, String> entry : map.entrySet()) {
			if (strIds.isEmpty()) {
				strIds = strIds + entry.getValue();
			} else {
				strIds = strIds + "," + entry.getValue();
			}
		}
		return strIds;
	}

	private void getAddressList() {
		AddressListRequest request = new AddressListRequest();
		request.setMethod("addressList");
		request.setUserId(getFromSharePreference(Config.userConfig.pkregister));
		logUrl(request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ADDRESS_LIST,
				request.toString());
	}

	@Override
	public void getAddressListSuccess(AddressListList list) {
		// TODO Auto-generated method stub
		super.getAddressListSuccess(list);
		adapter.data.clear();
		adapter.data.addAll(list.getItems());
		adapter.notifyDataSetChanged();
	}

	private void setDefaltAddress(String addressId) {
		showLoad("");
		AddressSetMoRenRequest request = new AddressSetMoRenRequest();
		request.setMethod("addressSetMoRen");
		request.setAddressId(addressId);
		logUrl(request.toString());
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.ADDRESSSETMOREN,
				request.toString());
	}

	@Override
	public void setDefaltAddressSuccess() {
		// TODO Auto-generated method stub
		super.setDefaltAddressSuccess();
		dismiss();
		adapter.defaltIndex = index;
		adapter.notifyDataSetChanged();
	}

	@Override
	public void setDefaltAddressFailed() {
		// TODO Auto-generated method stub
		super.setDefaltAddressFailed();
		dismiss();
		showToast("设置默认地址失败");
	}

	private void delAddress(String addressId){
		showLoad("");
		DelAddressRequest request = new DelAddressRequest();
		request.setMethod("delAddress");
		request.setAddressId(addressId);
		logUrl(request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.DELADDRESS, request.toString());
	}
	
	@Override
	public void delAddressSuccess() {
		// TODO Auto-generated method stub
		super.delAddressSuccess();
		dismiss();
		showToast("删除成功");
		handler.sendEmptyMessage(101);
	}
	
	@Override
	public void delAddressFailed() {
		// TODO Auto-generated method stub
		super.delAddressFailed();
		dismiss();
		showToast("删除失败");
	}
	
	// @Override
	// public void queryXnAddressListSuccess(QueryXnAddressListBean bean) {
	// adapter.setData(bean.getItems());
	// adapter.notifyDataSetChanged();
	// adapterList.clear();
	// adapterList.addAll(bean.getItems());
	// }
	//
	// // 删除地址接口
	// public void delXnAddress(String addressIds) {
	// DelXnAddressRequest request = new DelXnAddressRequest();
	// request.setMethod("delXnAddress");
	// request.setAddressId(addressIds);
	// CoreHttpClient.listen = this;
	// CoreHttpClient.get(this, Constants.REQUEST_TYPE.DEL_XN_ADDRESS,
	// request.toString());
	// Log.e("tag", "删除地址接口..." + Constants.BASE_URL + request.toString());
	// }
	//
	// @Override
	// public void delXnAddressSuccess() {
	// Log.i("tag", "地址删除成功");
	// showToast("地址删除成功");
	// queryXnAddressList(MyApplication.getInstance().getUserbean(this)
	// .getId());
	// for (Entry<Integer, String> entry : toDeleteAddressId.entrySet()) {
	// deletePosition.add(entry.getKey());
	// }
	// adapter.removeItem(deletePosition);
	// toDeleteAddressId.clear();
	// deletePosition.clear();
	// }
	//
	// @Override
	// public void delXnAddressFailed() {
	// Log.i("tag", "地址删除失败");
	// }

	@Override
	public void doing() {
		super.doing();
		if (adapter.isEditStatus()) {
			adapter.setEditStatus(false);
			tv_add.setText("添加地址");
			getDoingView().setText("编辑");
			// isEdit = true;
		} else {
			adapter.setEditStatus(true);
			tv_add.setText("删除");
			getDoingView().setText("完成");
			// isEdit = false;
		}
		adapter.notifyDataSetChanged();
	}
	private Dialog createCancelOrDeleteDialog(final String content) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.dialog_loginout, null);
		final Dialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		WindowManager windowManager = ((Activity) this).getWindowManager();
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
				delAddress(getOtherIds(toDeleteAddressId));
				dismiss();
			}
		});
		return dialog;
	}
}
