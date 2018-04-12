package com.sinia.orderlang.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.activity.AllOrderActivity;
import com.sinia.orderlang.activity.ApplyAfterSaleActivity;
import com.sinia.orderlang.activity.OrderCommentActivity;
import com.sinia.orderlang.activity.OrderDetailActivity;
import com.sinia.orderlang.activity.TejiaOrderPayActivity;
import com.sinia.orderlang.bean.AddOrderBean;
import com.sinia.orderlang.bean.OrderManagerBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.ComfireorderRequest;
import com.sinia.orderlang.request.DelXnOrderByIdRequest;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.ViewHolder;

/**
 * 所有订单
 */
public class AllOrderAdapter extends BaseAdapter {

	private Context mContext;

	public List<OrderManagerBean> data = new ArrayList<OrderManagerBean>();

	private Handler handler;

	public AllOrderAdapter(Context context, Handler handler) {
		this.mContext = context;
		this.handler = handler;
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
					R.layout.item_allorder, null);
		}
		final OrderManagerBean bean = data.get(position);
		TextView btn2 = ViewHolder.get(convertView, R.id.btn2);
		TextView tv_shop_name = ViewHolder.get(convertView, R.id.tv_shop_name);
		TextView btn1 = ViewHolder.get(convertView, R.id.btn1);
		TextView tvOrderStatus = ViewHolder.get(convertView,
				R.id.tv_orderstatus);
		TextView tvGoodName = ViewHolder.get(convertView, R.id.tv_goodname);
		TextView tvNumber = ViewHolder.get(convertView, R.id.tv_number);
		TextView tvPrice = ViewHolder.get(convertView, R.id.tv_price);
		ImageView imgGood = ViewHolder.get(convertView, R.id.img_good);
		ImageView img_trash = ViewHolder.get(convertView, R.id.img_trash);
		tv_shop_name.setText(bean.getShopName());
		BitmapUtilsHelp.getImage(mContext, R.drawable.default_img).display(
				imgGood, bean.getImageUrl());
		tvGoodName.setText(bean.getGoodName());
		tvNumber.setText("x"+bean.getGoodNum());
		tvPrice.setText("¥ " + bean.getOrderPrice());
		img_trash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createCancelOrDeleteDialog("确定删除订单？", 0,bean,position);
			}
		});
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (bean.getOrderStatus()) {
				case "1":
					createCancelOrDeleteDialog("确定取消订单？", 0,bean,position);
					break;
				case "2":
					Intent it2 = new Intent(mContext, OrderDetailActivity.class);
					it2.putExtra("orderId", data.get(position).getOrderId());
					mContext.startActivity(it2);
					break;
				case "3":
					Intent it3 = new Intent(mContext, OrderDetailActivity.class);
					it3.putExtra("orderId", data.get(position).getOrderId());
					mContext.startActivity(it3);
					break;
				case "4":
					// 申请售后
					Intent intent4 = new Intent(mContext,
							ApplyAfterSaleActivity.class);
					intent4.putExtra("OrderManagerBean", bean);
					mContext.startActivity(intent4);
					break;
				case "5":
					// 申请售后
					Intent intent5 = new Intent(mContext,
							ApplyAfterSaleActivity.class);
					intent5.putExtra("OrderManagerBean", bean);
					mContext.startActivity(intent5);
					break;
				case "9":
					// 申请售后
					Intent intent9 = new Intent(mContext,
							ApplyAfterSaleActivity.class);
					intent9.putExtra("OrderManagerBean", bean);
					mContext.startActivity(intent9);
					break;
				default:
					break;
				}
			}
		});
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (bean.getOrderStatus()) {
				case "1":
					// 去支付
					Intent it = new Intent(mContext,
							TejiaOrderPayActivity.class);
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
					break;
				case "2":
					createCancelOrDeleteDialog("确定收货？", 1,bean,position);
					
					break;
				case "3":
					createCancelOrDeleteDialog("确定收货？", 1,bean,position);
					break;
				case "4":
					Intent it4 = new Intent(mContext,
							OrderCommentActivity.class);
					it4.putExtra("OrderManagerBean", bean);
					mContext.startActivity(it4);
					break;
				case "5":

					break;
				case "7":
					if (bean.getCommentStatus().equals("1")) {

					} else {
						Intent it7 = new Intent(mContext,
								OrderCommentActivity.class);
						it7.putExtra("OrderManagerBean", bean);
						mContext.startActivity(it7);
					}
					break;
				case "8":
					if (bean.getCommentStatus().equals("1")) {

					} else {
						Intent it8 = new Intent(mContext,
								OrderCommentActivity.class);
						it8.putExtra("OrderManagerBean", bean);
						mContext.startActivity(it8);
					}
					break;
				case "9":
					if (bean.getCommentStatus().equals("1")) {

					} else {
						Intent it9 = new Intent(mContext,
								OrderCommentActivity.class);
						it9.putExtra("OrderManagerBean", bean);
						mContext.startActivity(it9);
					}
					break;

				default:
					break;
				}
			}
		});
		switch (bean.getOrderStatus()) {
		case "1":
			tvOrderStatus.setText("待支付");
			btn1.setText("取消订单");
			btn2.setText("立即支付");
			break;
		case "2":
			tvOrderStatus.setText("待收货");
			btn1.setText("查看物流");
			btn2.setText("确认收货");
			break;
		case "3":
			tvOrderStatus.setText("待收货");
			btn1.setText("查看物流");
			btn2.setText("确认收货");
			break;
		case "4":
			tvOrderStatus.setText("交易完成");
			btn1.setText("申请售后");
			btn2.setBackgroundResource(R.drawable.red_btn_bg2);
			btn2.setTextColor(Color.parseColor("#D13C47"));
			btn2.setText("评价");
			break;
		case "5":
			tvOrderStatus.setText("交易完成");
			btn1.setText("申请售后");
			btn2.setBackgroundResource(R.drawable.gray_btn_bg);
			btn2.setTextColor(Color.parseColor("#C7C7C7"));
			btn2.setText("已评价");
			break;
		case "7":
			tvOrderStatus.setText("交易完成");
			btn1.setText("审核中");
			btn1.setBackgroundResource(R.drawable.gray_btn_bg);
			btn1.setTextColor(Color.parseColor("#C7C7C7"));
			btn2.setVisibility(View.INVISIBLE);
			break;
		case "8":
			tvOrderStatus.setText("交易完成");
			btn1.setText("退款成功");
			btn1.setBackgroundResource(R.drawable.gray_btn_bg);
			btn1.setTextColor(Color.parseColor("#C7C7C7"));
			btn2.setVisibility(View.INVISIBLE);
			break;
		case "9":
			tvOrderStatus.setText("交易完成");
			btn1.setText("申请失败");
			btn2.setVisibility(View.INVISIBLE);
			btn1.setBackgroundResource(R.drawable.gray_btn_bg);
			btn1.setTextColor(Color.parseColor("#C7C7C7"));
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

	private void comfireorder(String orderId, final int pos) {
		ComfireorderRequest request = new ComfireorderRequest();
		request.setMethod("comfireorder");
		request.setOrderId(orderId);
		Log.d("URL", Constants.BASE_URL + request.toString());
		CoreHttpClient.get(mContext, request.toString(),
				new HttpCallBackListener() {

					@Override
					public void onSuccess(JSONObject json) {
						Log.d("result", json.toString());
						if (json.optInt("isSuccessful") == 0) {
							handler.sendEmptyMessage(100);
							Toast.makeText(mContext, "确认收货成功", Toast.LENGTH_SHORT).show();

						} else {
							Toast.makeText(mContext, "确认收货失败", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onRequestFailed() {
						Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
					}
				});
	}

	private Dialog createCancelOrDeleteDialog(final String content,final int flag,final OrderManagerBean bean,final int pos) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.dialog_loginout, null);
		final Dialog dialog = new AlertDialog.Builder(mContext).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		WindowManager windowManager = ((Activity) mContext).getWindowManager();
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
				if(flag==0){
					delOrder(bean.getOrderId(), pos);
				}else if(flag==1){
					comfireorder(bean.getOrderId(), pos);
				}
				dialog.dismiss();
			}
		});
		return dialog;
	}
	
}
