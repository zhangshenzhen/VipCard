package com.sinia.orderlang.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import com.sinia.orderlang.bean.RedBaoManagerBean;
import com.sinia.orderlang.http.CoreHttpClient;
import com.sinia.orderlang.http.HttpCallBackListener;
import com.sinia.orderlang.request.DelRedBaoRequest;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.Constants;
import com.sinia.orderlang.utils.ViewHolder;

public class RedPacketAdapter extends BaseAdapter {
	private Context context;
	public List<RedBaoManagerBean> data = new ArrayList<RedBaoManagerBean>();
	public RedPacketAdapter(Context mContext) {
		context = mContext;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_red_packet, null);
		}
		ImageView img_trash = ViewHolder.get(convertView, R.id.img_trash);
		ImageView img_good = ViewHolder.get(convertView, R.id.img_good);
		TextView tv_orderstatus = ViewHolder.get(convertView,
				R.id.tv_orderstatus);
		TextView tv_shop_name = ViewHolder.get(convertView, R.id.tv_shop_name);
		TextView tv_goodname = ViewHolder.get(convertView, R.id.tv_goodname);
		TextView tv_info = ViewHolder.get(convertView, R.id.tv_info);
		TextView tv_intro = ViewHolder.get(convertView, R.id.tv_intro);
		TextView tv_number = ViewHolder.get(convertView, R.id.tv_number);
		
		final RedBaoManagerBean bean = data.get(position);
		tv_shop_name.setText(bean.getShopName());
		if(bean.getRedbaoStatus().equals("1")){
			tv_orderstatus.setText("未支付");
			tv_orderstatus.setTextColor(Color.parseColor("#60ABF9"));
		}else if(bean.getRedbaoStatus().equals("2")){
			tv_orderstatus.setText("待使用");
			tv_orderstatus.setTextColor(Color.parseColor("#60ABF9"));
		}else{
			tv_orderstatus.setText("已使用");
			tv_orderstatus.setTextColor(Color.parseColor("#C7C7C7"));
		}
		
		BitmapUtilsHelp.getImage(context, R.drawable.default_img).display(img_good, bean.getImageUrl());
		tv_goodname.setText(bean.getGoodName()+bean.getFuGoodName());
		tv_info.setText(bean.getTitle()+"（"+bean.getFuTitle()+"）");
		tv_number.setText("数量："+bean.getGoodNum());
		tv_intro.setText("说明：使用时间"+bean.getBgtime()+"至"+bean.getEdtime());
		img_trash.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//删除订单
//				delRedBao(bean.getOrderId(), position);
				createCancelOrDeleteDialog("确定删除红包？", bean, position);
			}
		});
		return convertView;
	}

	private void delRedBao(String orderId,final int pos){
		DelRedBaoRequest request = new DelRedBaoRequest();
		request.setMethod("delRedBao");
		request.setOrderId(orderId);
		Log.d("URL", Constants.BASE_URL+request.toString());
		CoreHttpClient.get(context, request.toString(), new HttpCallBackListener() {
			
			@Override
			public void onSuccess(JSONObject json) {
				if(json.optInt("isSuccessful")==0){
					data.remove(pos);
					notifyDataSetChanged();
					Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onRequestFailed() {
				
			}
		});
		
	}
	
	private Dialog createCancelOrDeleteDialog(final String content,final RedBaoManagerBean bean,final int pos) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_loginout, null);
		final Dialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		WindowManager windowManager = ((Activity) context).getWindowManager();
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
				delRedBao(bean.getOrderId(), pos);
				dialog.dismiss();
			}
		});
		return dialog;
	}
}
