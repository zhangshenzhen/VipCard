package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.model.MerchantCategoryOneBeam;

import java.util.List;

public class TosiftLeftListAdapter extends BaseAdapter {

	private Context context;
	private List<MerchantCategoryOneBeam> list;
	private List<List<MerchantCategoryOneBeam>> listall;
	private TosiftRightListAdapter rightAdapter;
	private String url =  Config.web.base+"post/merchantCatetory";
	private ListView right;
	private LinearLayout other1;
	private LinearLayout layout_quanbu_two;
	private Handler handler;
	private int pos = -1;
//7a5ad72b8b29411cbf79a10f2a6204cb
	public TosiftLeftListAdapter(Context context,List<MerchantCategoryOneBeam> list,List<List<MerchantCategoryOneBeam>> listall,
								 TosiftRightListAdapter rightAdapter,
								 LinearLayout other1,ListView right,LinearLayout layout_quanbu_two,Handler handler) {
		super();
		this.context = context;
		this.list = list;
		this.listall = listall;
		this.rightAdapter = rightAdapter;
		this.right = right;
		this.other1 = other1;
		this.layout_quanbu_two = layout_quanbu_two;
		this.handler = handler;

	}

	@Override
	public int getCount() {
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		viewHolder holder = null;
		if (null == convertView) {
			holder = new viewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.category_quanbu_one_item, null);
			holder.tv = (TextView) convertView.findViewById(R.id.category_quanbu_text);
			holder.rl_layout = (RelativeLayout) convertView.findViewById(R.id.rl_layout);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
         if (pos ==position){
			 holder.rl_layout.setBackgroundResource(R.color.line_write_gray);
		 }else {
			 holder.rl_layout.setBackgroundResource(R.color.white);
		 }
		holder.tv.setText(list.get(position).getMtname());


		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                 Log.v("TAGGG",""+position);
				pos = position;
				other1.setVisibility(View.GONE);
				notifyDataSetChanged();
				layout_quanbu_two.setVisibility(View.VISIBLE);
				if ("全部".equals(list.get(position).getMtname())){
					Message message = handler.obtainMessage(1,"");
					handler.sendMessage(message);
				}else {
					for (int i = 0; i < list.size(); i++) {
						for (int j = 0; j < listall.get(i).size(); j++) {
							if ((list.get(position).getPkmertype()).equals(listall.get(i).get(j).getParentpk())) {
								if (listall.get(i) == null) {
									rightAdapter = new TosiftRightListAdapter(context, null, handler);
								} else {
									rightAdapter = new TosiftRightListAdapter(context, listall.get(i), handler);
								}
								right.setAdapter(rightAdapter);
								rightAdapter.notifyDataSetInvalidated();
							}
						}

					}

				}

			}
		});

		return convertView;
	}




	class viewHolder {

		TextView tv;
		RelativeLayout rl_layout;
	}

}
