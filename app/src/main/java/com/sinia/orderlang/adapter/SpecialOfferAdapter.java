package com.sinia.orderlang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.LoginActivity;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;
import com.bumptech.glide.Glide;
import com.sinia.orderlang.activity.GoodsDetailActivity;
import com.sinia.orderlang.bean.TeJiaListBean;
import com.sinia.orderlang.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SpecialOfferAdapter extends BaseAdapter {
	private Context context;
	public List<TeJiaListBean> data = new ArrayList<TeJiaListBean>();
	public SpecialOfferAdapter(Context mContext) {
		context = mContext;
	}

	@Override
	public int getCount() {
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_special_offer, null);
		}
		final TeJiaListBean bean = data.get(position);
		ImageView iv_good = ViewHolder.get(convertView, R.id.iv_good);
		TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
		TextView tv_people_num = ViewHolder
				.get(convertView, R.id.tv_people_num);
		TextView tv_money = ViewHolder.get(convertView, R.id.tv_money);
		TextView tv_before_money = ViewHolder.get(convertView,
				R.id.tv_before_money);
		LinearLayout ll_buy = ViewHolder.get(convertView, R.id.ll_buy);
		TextView tv_shop_name = ViewHolder.get(convertView, R.id.tv_shop_name);
		TextView tv_address = ViewHolder.get(convertView, R.id.tv_address);
		ll_buy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {


				if(SharedPreferenceUtils.getFromSharedPreference(context, Config.userConfig.is_Login).equals("Y")){
					//跳转到特价商品详情页
					Intent it = new Intent(context,GoodsDetailActivity.class);
					it.putExtra("goodId", bean.getGoodId());
					context.startActivity(it);
				}else{
					context.startActivity(new Intent(context, LoginActivity.class));
				}
			}
		});
//		BitmapUtilsHelp.getImage(context, R.drawable.default_img).display(iv_good, bean.getLogo());
		Glide.with(context).load(bean.getLogo()).centerCrop().placeholder(R.drawable.default_img).crossFade().into(iv_good);
		/*if(bean.getSaleNum().equals("")){
			tv_people_num.setText("已有0人抢购");
		}else{
			tv_people_num.setText("已有"+bean.getSaleNum()+"人抢购");
		}*/
		tv_title.setText(bean.getGoodName());
		tv_money.setText("平台价 ¥ "+bean.getNowPrice());
		tv_before_money.setText("商超价 ¥ "+bean.getBeforePrice());
		tv_shop_name.setText(bean.getMuname());
		tv_address.setText(bean.getAddress_tj());
		
		
		return convertView;
	}

	public void add(List<TeJiaListBean> l){
		if (data==null){
			data = l;
		}else {
			data.addAll(l);
		}
	}

}
