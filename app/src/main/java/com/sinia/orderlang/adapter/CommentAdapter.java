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
import com.sinia.orderlang.bean.CommentItemsBean;
import com.sinia.orderlang.utils.ViewHolder;

public class CommentAdapter extends BaseAdapter {
	private Context mContext;
	public List<CommentItemsBean> data = new ArrayList<CommentItemsBean>();

	public CommentAdapter(Context context) {
		this.mContext = context;
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
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_comment, null);
		}
		TextView tv_phone = ViewHolder.get(convertView, R.id.tv_phone);
		ImageView iv1 = ViewHolder.get(convertView, R.id.iv1);
		ImageView iv2 = ViewHolder.get(convertView, R.id.iv2);
		ImageView iv3 = ViewHolder.get(convertView, R.id.iv3);
		ImageView iv4 = ViewHolder.get(convertView, R.id.iv4);
		ImageView iv5 = ViewHolder.get(convertView, R.id.iv5);
		TextView tv_count = ViewHolder.get(convertView, R.id.tv_count);
		TextView tv_comment = ViewHolder.get(convertView, R.id.tv_comment);
		TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
		CommentItemsBean bean = data.get(position);

		tv_phone.setText(bean.getTelephone().replace(
				bean.getTelephone().substring(3, 7), "****"));
		tv_count.setText(bean.getStar() + "分");
		tv_comment.setText(bean.getContent());
		tv_time.setText(bean.getCreateTime());

		switch (bean.getStar()) {
		case "0":
			setInvisible(iv1, iv2, iv3, iv4, iv5);
			break;
		case "1":
			setInvisible(iv1, iv2, iv3, iv4);
			setVisible(iv5);
			break;
		case "2":
			setInvisible(iv1, iv2, iv3);
			setVisible(iv5, iv4);
			break;
		case "3":
			setInvisible(iv1, iv2);
			setVisible(iv5, iv4, iv3);
			break;
		case "4":
			setInvisible(iv1);
			setVisible(iv5, iv4, iv3, iv2);
			break;
		case "5":
			setVisible(iv5, iv4, iv3, iv2, iv1);
			break;
		default:
			break;
		}
		return convertView;
	}

	private void setVisible(ImageView... imageViews) {
		for (ImageView view : imageViews) {
			view.setVisibility(View.VISIBLE);
		}
	}

	private void setInvisible(ImageView... imageViews) {
		for (ImageView view : imageViews) {
			view.setVisibility(View.INVISIBLE);
		}
	}
}
