package com.sinia.orderlang.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.utils.ViewHolder;

/**
 * 历史搜索
 */
public class HistorySearchAdapter extends BaseAdapter {

	private Context mContext;
	public List<String> list;

	public HistorySearchAdapter(Context context, List<String> list) {
		this.mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_history_search, null);
		}
		TextView tv = ViewHolder.get(convertView, R.id.tv_name);
		tv.setText(list.get(position));
		return convertView;
	}

}
