package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.model.MerchantCategoryOneBeam;

import java.util.List;


public class TosiftRightListAdapter extends BaseAdapter {

	private Context context;
	private List<MerchantCategoryOneBeam> list;
	private Handler handler;
	PopupWindow popupWindow;
	public TosiftRightListAdapter(Context context,
								  List<MerchantCategoryOneBeam> list ,Handler handler) {
		super();
		this.context = context;
		this.list = list;
		this.handler = handler;
		popupWindow =new PopupWindow();
	}

	@Override
	public int getCount() {
		if (null == list) {
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void updateView() {
		if(this.list!=null){
			this.list=null;
		}
		notifyDataSetChanged();
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		viewHolder holder = null;
		if (null == convertView) {
			holder = new viewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.category_quanbu_two_item, null);
			holder.tv = (TextView) convertView.findViewById(R.id.category_quanbu_two_name);
			holder.tv_number = (TextView) convertView.findViewById(R.id.category_quanbu_two_sum);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		holder.tv.setText(list.get(position).getMtname());
		holder.tv_number.setText(null == (list.get(position).getMerchantCount() + "") ? "" : list.get(position).getMerchantCount() + "");
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = handler.obtainMessage(1,list.get(position));
				handler.sendMessage(msg);
				popupWindow.dismiss();
			}
		});
		return convertView;
	}


	class viewHolder {
		TextView tv, tv_number;
	}



}
