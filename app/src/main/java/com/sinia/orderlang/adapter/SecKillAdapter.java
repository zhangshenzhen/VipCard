package com.sinia.orderlang.adapter;

import java.util.ArrayList;
import java.util.List;

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
import com.bjypt.vipcard.model.TransactionOrRechangeBean;
import com.bumptech.glide.Glide;
import com.sinia.orderlang.activity.GoodsDetailActivity;
import com.sinia.orderlang.activity.SeckillDetailActivity;
import com.sinia.orderlang.activity.SeckillOrderSubmitActivity;
import com.sinia.orderlang.bean.RedpacketItems;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.ViewHolder;

/**
 * 秒杀
 */
public class SecKillAdapter extends BaseAdapter {
	private Context context;
	public List<RedpacketItems> data = new ArrayList<RedpacketItems>();

	public SecKillAdapter(Context mContext) {
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
					R.layout.item_seckill, null);
		}
		ImageView iv_good = ViewHolder.get(convertView, R.id.iv_good);
		LinearLayout ll_buy = ViewHolder.get(convertView, R.id.ll_buy);
		TextView tv_people_num = ViewHolder
				.get(convertView, R.id.tv_people_num);
		TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
		TextView tv_title2 = ViewHolder.get(convertView, R.id.tv_title2);
		TextView tv_money = ViewHolder.get(convertView, R.id.tv_money);
		TextView tv_surplus = ViewHolder.get(convertView, R.id.tv_surplus);
		TextView tv_shop_name = ViewHolder.get(convertView, R.id.tv_shop_name);
		TextView tv_end_time = ViewHolder.get(convertView, R.id.tv_end_time);
		TextView tv_address = ViewHolder.get(convertView, R.id.tv_address);

		final RedpacketItems item = data.get(position);
//		BitmapUtilsHelp.getImage(context, R.drawable.default_img).display(
//				iv_good, item.getLogo());
		Glide.with(context).load(item.getLogo()).centerCrop().placeholder(R.drawable.default_img).crossFade().into(iv_good);

		tv_title.setText(item.getGoodName() + " " + item.getFuGoodName());
		tv_title2.setText(item.getTitle() + " " + item.getFuTitle());
		/*if(item.getBuyNum().equals("")){
			tv_people_num.setText("已有0人抢购");
		}else{
			tv_people_num.setText("已有" + item.getBuyNum() + "人抢购");
		}*/
		tv_money.setText("秒杀价 ¥ " + Float.parseFloat(item.getNowPrice()));
		tv_surplus.setText("剩余数量：" + item.getHasNum());
		tv_end_time.setText("截止日期：" + item.getEndTime());
		tv_shop_name.setText(item.getMuname());
		tv_address.setText(item.getAddress_kill());
		if(1 == item.getGoodStatus()){
			ll_buy.setClickable(false);
			ll_buy.setBackgroundResource(R.drawable.mine_shape_gray);
		}else{
			ll_buy.setClickable(true);
			ll_buy.setBackgroundResource(R.drawable.mine_shape_white);
			ll_buy.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent it = new Intent(context,
							SeckillOrderSubmitActivity.class);
					it.putExtra("goodname", item.getGoodName());
					it.putExtra("info",
							item.getTitle());
					it.putExtra("price", Float.parseFloat(item.getNowPrice()));
					it.putExtra("goodId", item.getGoodId());
					context.startActivity(it);
				}
			});
		}
		return convertView;
	}

	public void add(List<RedpacketItems> l){
		if (data==null){
			data = l;
		}else {
			data.addAll(l);
		}
	}

}
