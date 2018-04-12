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
import com.sinia.orderlang.bean.ArrayXnGoodTypeItemsBean;
import com.sinia.orderlang.utils.ViewHolder;

public class ClassifyAdapter extends BaseAdapter {
	private Context context;
	public int check;
	public List<ArrayXnGoodTypeItemsBean> data = new ArrayList<ArrayXnGoodTypeItemsBean>();
	public ClassifyAdapter(Context mContext){
		context = mContext;
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(null==convertView){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_classify, null);
		}
		TextView tv_good_name = ViewHolder.get(convertView, R.id.tv_good_name);
		ImageView iv_check = ViewHolder.get(convertView, R.id.iv_check);
		ArrayXnGoodTypeItemsBean bean = data.get(position);
		if(position == check){
			iv_check.setVisibility(View.VISIBLE);
		}else{
			iv_check.setVisibility(View.INVISIBLE);
		}
		tv_good_name.setText(bean.getTypeName());
		return convertView;
	}

}
