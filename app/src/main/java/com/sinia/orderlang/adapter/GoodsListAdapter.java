package com.sinia.orderlang.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.bean.GoodsListBean;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.ViewHolder;

/**
 * 商品列表
 */
public class GoodsListAdapter extends BaseAdapter {
	private Context mContext;
	public List<GoodsListBean> data = new ArrayList<GoodsListBean>();

	public GoodsListAdapter(Context context) {
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
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_goodslist, null);
		}
		ImageView img_good = ViewHolder.get(convertView, R.id.img_good);
		TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
		TextView tv_num = ViewHolder.get(convertView, R.id.tv_num);
		TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
		GoodsListBean goodsListBean = data.get(position);
		
		BitmapUtilsHelp.getImage(mContext, R.drawable.default_img).display(
				img_good, goodsListBean.getImageUrl());
		tv_name.setText(goodsListBean.getGoodName());
		tv_num.setText("x" + goodsListBean.getGoodNum());
		tv_price.setText("¥" + goodsListBean.getPrice());
		return convertView;
	}

}
