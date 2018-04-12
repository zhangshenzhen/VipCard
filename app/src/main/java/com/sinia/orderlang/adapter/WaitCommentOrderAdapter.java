package com.sinia.orderlang.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
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
import com.sinia.orderlang.activity.ApplyAfterSaleActivity;
import com.sinia.orderlang.activity.OrderCommentActivity;
import com.sinia.orderlang.bean.OrderManagerBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.DelXnOrderByIdRequest;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.ViewHolder;

public class WaitCommentOrderAdapter extends BaseAdapter {

	private Context mContext;

	public List<OrderManagerBean> data = new ArrayList<OrderManagerBean>();

	public WaitCommentOrderAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_waitcommentorder, null);
		}
		final OrderManagerBean bean = data.get(position);
		TextView btn2 = ViewHolder.get(convertView, R.id.btn2);
		TextView btn1 = ViewHolder.get(convertView, R.id.btn1);
		TextView tv_shop_name = ViewHolder.get(convertView, R.id.tv_shop_name);
		TextView tvGoodName = ViewHolder.get(convertView, R.id.tv_goodname);
		TextView tvNumber = ViewHolder.get(convertView, R.id.tv_number);
		TextView tvPrice = ViewHolder.get(convertView, R.id.tv_price);
		ImageView imgTrash = ViewHolder.get(convertView, R.id.img_trash);
		ImageView imgGood = ViewHolder.get(convertView, R.id.img_good);
		tv_shop_name.setText(bean.getShopName());
		BitmapUtilsHelp.getImage(mContext, R.drawable.default_img).display(
				imgGood, bean.getImageUrl());
		tvGoodName.setText(bean.getGoodName());
		tvNumber.setText(bean.getGoodNum());
		tvPrice.setText("¥ " + bean.getOrderPrice());
		imgTrash.setVisibility(View.VISIBLE);
		
		
		if (bean.getOrderStatus().equals("5")) {
			btn2.setBackgroundResource(R.drawable.gray_btn_bg);
			btn2.setTextColor(Color.parseColor("#C7C7C7"));
			btn2.setText("已评价");
		} else {
			btn2.setBackgroundResource(R.drawable.red_btn_bg2);
			btn2.setTextColor(Color.parseColor("#D13C47"));
			btn2.setText("评价");
		}
		
		imgTrash.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				delOrder(bean.getOrderId(), position);
			}
		});
		btn1.setText("申请售后");
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext,
						ApplyAfterSaleActivity.class);
				intent.putExtra("OrderManagerBean", bean);
				mContext.startActivity(intent);
			}
		});
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!bean.getOrderStatus().equals("5")) {
					// 评价
					Intent it = new Intent(mContext,OrderCommentActivity.class);
					it.putExtra("OrderManagerBean", bean);
					mContext.startActivity(it);
				}
			}
		});

		return convertView;
	}
	private void delOrder(String orderId,final int pos){
		DelXnOrderByIdRequest request = new DelXnOrderByIdRequest();
		request.setMethod("delXnOrderById");
		request.setOrderId(orderId);
		Log.d("URL", Constants.BASE_URL+request.toString());
		CoreHttpClient.get(mContext, request.toString(), new HttpCallBackListener() {
			
			@Override
			public void onSuccess(JSONObject json) {
				Log.d("result", json.toString());
				if(json.optInt("isSuccessful")==0){
					Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
					data.remove(pos);
					notifyDataSetChanged();
				}else{
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
