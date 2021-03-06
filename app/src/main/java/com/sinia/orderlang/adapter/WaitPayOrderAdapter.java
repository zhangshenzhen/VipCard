package com.sinia.orderlang.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.activity.TejiaOrderPayActivity;
import com.sinia.orderlang.bean.AddOrderBean;
import com.sinia.orderlang.bean.OrderManagerBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.DelXnOrderByIdRequest;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.ViewHolder;

public class WaitPayOrderAdapter extends BaseAdapter {

	private Context mContext;
	
	public List<OrderManagerBean> data = new ArrayList<OrderManagerBean>();

	public WaitPayOrderAdapter(Context context) {
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
					R.layout.item_waitpayorder, null);
		}
		final OrderManagerBean bean = data.get(position);
		LinearLayout item = ViewHolder.get(convertView, R.id.item);
		TextView btn2 = ViewHolder.get(convertView, R.id.btn2);
		TextView btn1 = ViewHolder.get(convertView, R.id.btn1);
		TextView tv_shop_name = ViewHolder.get(convertView, R.id.tv_shop_name);
		TextView tvOrderStatus = ViewHolder.get(convertView,
				R.id.tv_orderstatus);
		TextView tvGoodName = ViewHolder.get(convertView, R.id.tv_goodname);
		TextView tvNumber = ViewHolder.get(convertView, R.id.tv_number);
		TextView tvPrice = ViewHolder.get(convertView, R.id.tv_price);
		ImageView imgGood = ViewHolder.get(convertView, R.id.img_good);
		
		tvOrderStatus.setText("待支付");
		tv_shop_name.setText(bean.getShopName());
		BitmapUtilsHelp.getImage(mContext, R.drawable.default_img).display(imgGood, bean.getImageUrl());
		tvGoodName.setText(bean.getGoodName());
		tvNumber.setText("数量：x"+bean.getGoodNum());
		tvPrice.setText("¥ "+bean.getOrderPrice());
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//调用取消订单接口
				delOrder(bean.getOrderId(),position);
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//去支付
				Intent it = new Intent(mContext,TejiaOrderPayActivity.class);
				it.putExtra("showWhat", 1);
				AddOrderBean addOrderBean = new AddOrderBean();
				addOrderBean.setBuyNum(bean.getGoodNum());
				addOrderBean.setCreateTime(bean.getCreateTime());
				addOrderBean.setOrderId(bean.getOrderId());
				addOrderBean.setOrderNum(bean.getOrderNum());
				addOrderBean.setPayType("1");
				addOrderBean.setTureCost(bean.getOrderPrice());
				it.putExtra("AddOrderBean", addOrderBean);
				mContext.startActivity(it);
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
					Toast.makeText(mContext, "取消成功", Toast.LENGTH_SHORT).show();
					data.remove(pos);
					notifyDataSetChanged();
				}else{
					Toast.makeText(mContext, "取消失败", Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onRequestFailed() {
				Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
