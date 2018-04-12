package com.sinia.orderlang.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.bean.MessageListBean;
import com.sinia.orderlang.utils.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 订单详情
 */
public class OrderInfoAdapter extends BaseAdapter {
	private Context context;
	public List<MessageListBean> data = new ArrayList<MessageListBean>();

	public OrderInfoAdapter(Context context) {
		this.context = context;
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
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_orderinformation, null);
		}
		TextView tv_ordertitle = ViewHolder.get(convertView, R.id.tv_ordertitle);
		TextView tv_ordername = ViewHolder.get(convertView, R.id.tv_ordername);
		TextView tv_ordertime = ViewHolder.get(convertView, R.id.tv_ordertime);
		MessageListBean bean = data.get(position);
		tv_ordertitle.setText(bean.getContent());
		tv_ordertime.setText(bean.getCreateTime());
		return convertView;
	}

}
