package com.sinia.orderlang.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.activity.GoodsDetailActivity;
import com.sinia.orderlang.activity.RedPacketDetailActivity;
import com.sinia.orderlang.activity.SeckillDetailActivity;
import com.sinia.orderlang.bean.CollectionListBean;
import com.sinia.orderlang.utils.BitmapUtilsHelp;
import com.sinia.orderlang.utils.ViewHolder;

/**
 * 收藏
 */
public class CollectAdapter extends BaseAdapter {

	private Context context;
	private boolean editStatus = false;
	private Handler handler;
	private List<Integer> listPos;
	private CollectionCallBack callback;

	public List<CollectionListBean> list = new ArrayList<CollectionListBean>();

	public HashMap<String, CollectionListBean> map = new HashMap<String, CollectionListBean>();

	public CollectAdapter(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		listPos = new ArrayList<Integer>();
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
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View v, ViewGroup arg2) {
		if (v == null) {
			v = LayoutInflater.from(context).inflate(R.layout.item_collect,
					null);
		}
		final CollectionListBean bean = list.get(position);
		final ImageView choose = ViewHolder.get(v, R.id.choose);
		ImageView img_goods = ViewHolder.get(v, R.id.img_goods);
		TextView tv_goods_name = ViewHolder.get(v, R.id.tv_goods_name);
		TextView tv_second_title = ViewHolder.get(v, R.id.tv_second_title);
		TextView tv_price = ViewHolder.get(v, R.id.tv_price);
		TextView tv_price_old = ViewHolder.get(v, R.id.tv_price_old);
		TextView tv_end_time = ViewHolder.get(v, R.id.tv_end_time);
		TextView tv_people_num = ViewHolder.get(v, R.id.tv_people_num);
		View view = ViewHolder.get(v, R.id.view);
		LinearLayout ll_buy = ViewHolder.get(v, R.id.ll_buy);
		LinearLayout layout = ViewHolder.get(v, R.id.layout);

		tv_goods_name.setText(bean.getGoodName());
		tv_price.setText("¥ " + bean.getNowPrice());
		if (bean.getType().equals("1")) {
			ll_buy.setVisibility(View.VISIBLE);
			view.setVisibility(View.INVISIBLE);
			tv_price_old.setText("剩余数量："+bean.getHasNum());
		} else {
			ll_buy.setVisibility(View.INVISIBLE);
			view.setVisibility(View.VISIBLE);
			tv_price_old.setText("¥ " + bean.getBeforePrice());
		}
		tv_second_title.setText(bean.getTitle());
		BitmapUtilsHelp.getImage(context, R.drawable.default_img).display(
				img_goods, bean.getImageUrl());
		if(bean.getSaleNum().equals("")){
			tv_people_num.setText("已有0人抢购");
		}else{
			tv_people_num.setText("已有" + bean.getSaleNum() + "人抢购");
		}
		tv_end_time.setText("截止日期：" + bean.getEndTime());
		if (editStatus) {
			choose.setSelected(false);
			choose.setVisibility(View.VISIBLE);
			listPos.clear();
		} else {
			choose.setVisibility(View.GONE);
		}
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (editStatus) {
					if (map.containsKey(list.get(position))) {
						map.remove(list.get(position));
						choose.setSelected(false);
					} else {
						map.put(list.get(position).getCollectionId(),
								list.get(position));
						choose.setSelected(true);
					}

				} else {
					// callback.itemClick(list.get(position).getCollectionId());
					if (bean.getType().equals("1")) {
						// 跳转到红包详情页
						Intent it = new Intent(context,SeckillDetailActivity.class);
						it.putExtra("goodId", bean.getGoodId());
						context.startActivity(it);
					} else {
						// 跳转到特价详情页
						Intent it = new Intent(context,GoodsDetailActivity.class);
						it.putExtra("goodId", bean.getGoodId());
						context.startActivity(it);
					}
				}

			}
		});
		return v;
	}

	public interface CollectionCallBack {
		public void itemClick(String id);

		public void removeItem(List<Integer> listPos);
	}

	public void setCallback(CollectionCallBack callback) {
		this.callback = callback;
	}

	public boolean isEditStatus() {
		return editStatus;
	}

	public void setEditStatus(boolean editStatus) {
		this.editStatus = editStatus;
	}

}
