package com.sinia.orderlang.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.bean.GoodItemsBean;
import com.sinia.orderlang.bean.GroupBean;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.ViewHolder;

public class ShopCarAdapter extends BaseExpandableListAdapter {
	public List<GroupBean> groups = new ArrayList<GroupBean>();
	public HashMap<String, List<GoodItemsBean>> childs = new HashMap<String, List<GoodItemsBean>>();
	private Context context;
	private Handler handler;
	ischeck ischeck;
	public void setischek(ischeck ischeck) {
		this.ischeck = ischeck;
	}
	public ShopCarAdapter(Context mContext,Handler handler){
		this.context = mContext;
		this.handler = handler;
	}
	
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childs.get(groups.get(groupPosition).getShopName()).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childs.get(groups.get(groupPosition).getShopName()).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(null == convertView){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_car_group, null);
		}
		GroupBean gBean = groups.get(groupPosition);
		CheckBox ck1 = ViewHolder.get(convertView, R.id.ck1);
		ck1.setText(gBean.getShopName());
		if (gBean.isChecked()) {
			ck1.setChecked(true);
		} else {
			ck1.setChecked(false);
		}
		ck1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					groups.get(groupPosition).setChecked(true);
					Log.d("lamp", ""+(groupPosition + ",ischeck=" + true));
					ischeck.ischekgroup(groupPosition, true);
				} else {
					ischeck.ischekgroup(groupPosition, false);
				}
				handler.sendEmptyMessage(100);
			}
		});
		
		
		
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(null == convertView){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_car, null);
		}
		final GoodItemsBean goodItemsBean = childs.get(groups.get(groupPosition).getShopName()).get(childPosition);
		final float singlePrice = Float.parseFloat(goodItemsBean.getGoodPrice())/Integer.parseInt(goodItemsBean.getGoodNum());
		CheckBox ck2 = ViewHolder.get(convertView, R.id.ck2);
		ImageView iv_good_img = ViewHolder.get(convertView, R.id.iv_good_img);
		TextView tv_good_name = ViewHolder.get(convertView, R.id.tv_good_name);
		final TextView tv_count = ViewHolder.get(convertView, R.id.tv_count);
		final TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
		final TextView tv_num = ViewHolder.get(convertView, R.id.tv_num);
		RelativeLayout rl_minus = ViewHolder.get(convertView, R.id.rl_minus);
		RelativeLayout rl_plus = ViewHolder.get(convertView, R.id.rl_plus);
		if(goodItemsBean.isChecked()){
			ck2.setChecked(true);
		}else{
			ck2.setChecked(false);
		}
		BitmapUtilsHelp.getImage(context, R.drawable.default_img).display(iv_good_img, goodItemsBean.getImageUrl());
		tv_good_name.setText(goodItemsBean.getGoodName());
		tv_count.setText("x"+goodItemsBean.getGoodNum());
		tv_price.setText("¥ "+goodItemsBean.getGoodPrice());
		tv_num.setText(goodItemsBean.getGoodNum());
		rl_minus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int num1 = Integer.parseInt(tv_num.getText().toString());
				if(num1>1){
					int num2 = num1 - 1;
					tv_count.setText("x"+num2);
					tv_num.setText(num2+"");
					goodItemsBean.setGoodNum(num2+"");
					goodItemsBean.setGoodPrice(singlePrice*num2 + "");
					tv_price.setText("¥ "+goodItemsBean.getGoodPrice());
					handler.sendEmptyMessage(100);
				}
			}
		});
		rl_plus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int num1 = Integer.parseInt(tv_num.getText().toString());
				int num2 = num1 + 1;
				tv_count.setText("x"+num2);
				tv_num.setText(num2+"");
				goodItemsBean.setGoodNum(num2+"");
				goodItemsBean.setGoodPrice(singlePrice*num2 + "");
				tv_price.setText("¥ "+goodItemsBean.getGoodPrice());
				goodItemsBean.setChecked(true);
				oneClassSetCheck(groupPosition);
				handler.sendEmptyMessage(100);
			}
		});
		ck2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					goodItemsBean.setChecked(true);
					//二级分类全选，那么一级分类也得是选择
					oneClassSetCheck(groupPosition);
					
				} else {
					goodItemsBean.setChecked(false);
					//二级分类全部不选，那么一级分类也得是不选
					oneClassSetUnCheck(groupPosition);
				}
				handler.sendEmptyMessage(100);
			}
		});
		return convertView;
	}

	private void oneClassSetCheck(int groupPosition){
		boolean isCheckAll = false;
		int j = 0;
		for(int i = 0;i < childs.get(groups.get(groupPosition).getShopName()).size();i++){
			GoodItemsBean gb = childs.get(groups.get(groupPosition).getShopName()).get(i);
			Log.d("lamp", "gb.isChecked()="+gb.isChecked());
			if(gb.isChecked()){
				j++;
			}
			if(j==childs.get(groups.get(groupPosition).getShopName()).size()){
				isCheckAll = true;
			}
		}
		if(isCheckAll){
			groups.get(groupPosition).setChecked(true);
			notifyDataSetChanged();
		}
	}
	
	private void oneClassSetUnCheck(int groupPosition){
		boolean isCheckAll = true;
		for(int i = 0;i < childs.get(groups.get(groupPosition).getShopName()).size();i++){
			GoodItemsBean gb = childs.get(groups.get(groupPosition).getShopName()).get(i);
			Log.d("lamp", "gb.isChecked()="+gb.isChecked());
			if(gb.isChecked()){
				isCheckAll = false;
			}
		}
		if(isCheckAll){
			Log.d("lamp", "isCheckAll="+isCheckAll);
			groups.get(groupPosition).setChecked(false);
			notifyDataSetChanged();
		}
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public interface ischeck {
		public void ischekgroup(int groupposition, boolean ischeck);
	}
}
