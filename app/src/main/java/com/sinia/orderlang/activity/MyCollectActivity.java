package com.sinia.orderlang.activity;

import java.util.Iterator;
import java.util.List;

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
import android.widget.ListView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.adapter.CollectAdapter;
import com.sinia.orderlang.adapter.CollectAdapter.CollectionCallBack;
import com.sinia.orderlang.bean.CollectionListList;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.request.CollectionListRequest;
import com.sinia.orderlang.request.DelCollectionRequest;
import com.sinia.orderlang.utils.Constants;

/**
 * 我的收藏
 */
public class MyCollectActivity extends BaseActivity {
	private TextView doing, tv_delete;
	private ListView listView;
	private CollectAdapter adapter;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				collectionList();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect, "我的收藏");
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		collectionList();
	}

	private void initView() {
		doing = (TextView) getDoingView();
		tv_delete = (TextView) findViewById(R.id.tv_delete);
		doing.setText("编辑");
		listView = (ListView) findViewById(R.id.listView);
		adapter = new CollectAdapter(this, null);
		adapter.setEditStatus(false);
		listView.setAdapter(adapter);
		adapter.setCallback(new CollectionCallBackImp());
		tv_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (adapter.map.size() > 0) {
					Iterator<String> it = adapter.map.keySet().iterator();
					StringBuffer sb = new StringBuffer();
					while (it.hasNext()) {
						String id = it.next();
						sb.append(id).append(",");
					}
					String goodsId = sb.toString().substring(0,
							sb.toString().length());
//					 delXnCollection(goodsId);
//					createCancelOrDeleteDialog("确定删除吗？", goodsId);
					delCollection(goodsId);
				}
			}
		});
	}

	@Override
	public void doing() {
		if (adapter.isEditStatus()) {
			adapter.setEditStatus(false);
			doing.setText("编辑");
			tv_delete.setVisibility(View.GONE);
		} else {
			adapter.setEditStatus(true);
			doing.setText("完成");
			tv_delete.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
	}

	class CollectionCallBackImp implements CollectionCallBack {

		@Override
		public void itemClick(String id) {
			// Intent intent = new Intent();
			// intent.putExtra("goodId", id);
			// startActivityForIntent(GoodsDetailActivity.class, intent);
		}

		@Override
		public void removeItem(List<Integer> listPos) {

		}
	}

	// 我的收藏
	private void collectionList() {
		showLoad("加载中...");
		CollectionListRequest request = new CollectionListRequest();
		request.setMethod("collectionList");
		request.setUserId(Constants.userId);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.listen = this;
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.COLLECTIONLIST,
				request.toString());
	}

	@Override
	public void getCollectionListSuccess(CollectionListList list) {
		// TODO Auto-generated method stub
		super.getCollectionListSuccess(list);
		dismiss();
		adapter.list.clear();
		adapter.list.addAll(list.getItems());
		adapter.notifyDataSetChanged();
	}

	@Override
	public void requestFailed() {
		// TODO Auto-generated method stub
		super.requestFailed();
		dismiss();
		showToast("请求失败");
	}

	private void delCollection(String collectionId) {
		showLoad("");
		DelCollectionRequest request = new DelCollectionRequest();
		request.setMethod("delCollection");
		request.setCollectionId(collectionId);
		logUrl(request.toString());
		CoreHttpClient.get(this, Constants.REQUEST_TYPE.DELCOLLECTION,
				request.toString());
	}

	@Override
	public void delCollectionSuccess() {
		// TODO Auto-generated method stub
		super.delCollectionSuccess();
		dismiss();
		showToast("删除成功");
		handler.sendEmptyMessage(100);
	}

	@Override
	public void delCollectionFailed() {
		// TODO Auto-generated method stub
		super.delCollectionFailed();
		dismiss();
		showToast("删除失败");
	}
	
	/**
	 * 
	 * */
	private Dialog createCancelOrDeleteDialog(final String content,final String goodsId) {
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
				delCollection(goodsId);
			}
		});
		return dialog;
	}
}
