package com.sinia.orderlang.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.bean.OrderManagerBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.DelXnOrderByIdRequest;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.ViewHolder;

/**
 * 售后
 */
public class AfterSaleAdapter extends BaseAdapter {

	private Context mContext;

	public List<OrderManagerBean> data = new ArrayList<OrderManagerBean>();

	public AfterSaleAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_aftersale, null);
		}
		TextView tv_shop_name = ViewHolder.get(convertView, R.id.tv_shop_name);
		TextView tv_good_name = ViewHolder.get(convertView, R.id.tv_good_name);
		TextView tv_count = ViewHolder.get(convertView, R.id.tv_count);
		TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
		TextView btn = ViewHolder.get(convertView, R.id.btn);
		ImageView img_trash = ViewHolder.get(convertView, R.id.img_trash);
		ImageView img_good = ViewHolder.get(convertView, R.id.img_good);

		final OrderManagerBean bean = data.get(position);
		tv_shop_name.setText(bean.getShopName());
		tv_good_name.setText(bean.getGoodName());
		tv_count.setText("数量：x"+bean.getGoodNum());
		tv_price.setText("¥"+bean.getOrderPrice());
		BitmapUtilsHelp.getImage(mContext, R.drawable.default_img).display(img_good, bean.getImageUrl());
		img_trash.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				delOrder(bean.getOrderId(), position);
			}
		});
		
		switch (bean.getOrderStatus()) {
		case "7":
			btn.setText("审核中");
			btn.setBackgroundResource(R.drawable.gray_btn_bg);
			btn.setTextColor(Color.parseColor("#C7C7C7"));
			break;
		case "8":
			btn.setText("完成");
			btn.setBackgroundResource(R.drawable.gray_btn_bg);
			btn.setTextColor(Color.parseColor("#C7C7C7"));
			break;
		case "9":
			btn.setText("申请失败");
			btn.setBackgroundResource(R.drawable.gray_btn_bg);
			btn.setTextColor(Color.parseColor("#C7C7C7"));
			break;
		default:
			break;
		}

		return convertView;
	}

	private void delOrder(String orderId, final int pos) {
		DelXnOrderByIdRequest request = new DelXnOrderByIdRequest();
		request.setMethod("delXnOrderById");
		request.setOrderId(orderId);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.get(mContext, request.toString(),
				new HttpCallBackListener() {

					@Override
					public void onSuccess(JSONObject json) {
						Log.d("result", json.toString());
						if (json.optInt("isSuccessful") == 0) {
							Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
							data.remove(pos);
							notifyDataSetChanged();
						} else {
							Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onRequestFailed() {
						Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
					}
				});
	}
}
