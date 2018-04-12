package com.bjypt.vipcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.CityActivity;
import com.bjypt.vipcard.model.SortModel;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;

import java.util.List;

/****
 * 
 * 城市界面左侧listview的adapter
 * 
 * @author Administrator
 *
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer{
	private List<SortModel> list = null;
	private Context mContext;
	private BroadCastReceiverUtils utils;
	public SortAdapter(Context mContext, List<SortModel> list) {
		utils = new BroadCastReceiverUtils();
		this.mContext = mContext;
		this.list = list;
		
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<SortModel> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
//		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		//根据position获取分类的首字母的Char ascii值
		viewHolder.tvTitle.setText(list.get(position).getName());
		String currentStr = list.get(position).getSortLetters();
		String previewStr = (position - 1) >= 0 ? list.get(position - 1)
				.getSortLetters() : " ";
//		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (!previewStr.equals(currentStr)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(currentStr);
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		
		view.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//((CityActivity)mContext).setResult(((CityActivity)mContext).CITYCODE,new Intent().putExtra("city", list.get(position).getName()));
				utils.sendBroadCastReceiver(mContext, "change_city", "updata", list.get(position).getName());
				((CityActivity)mContext).finish();
			}
		});
		
//		if(position == getPositionForSection(section)){
//			viewHolder.tvLetter.setVisibility(View.VISIBLE);
//			viewHolder.tvLetter.setText(mContent.getSortLetters());
//		}else{
//			viewHolder.tvLetter.setVisibility(View.GONE);
//		}
	
//		viewHolder.tvTitle.setText(this.list.get(position).getName());
		
		return view;

	}
	


	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();

			char firstChar = sortStr.toUpperCase().charAt(0);

			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}