package com.sinia.orderlang.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.activity.AddressManageActivity;
import com.sinia.orderlang.bean.AddressListBean;

public class MyAddressAdapter extends BaseAdapter {
	private Context context;

	private LayoutInflater in;

	HashMap<String, Boolean> states = new HashMap<String, Boolean>();// 用于记录每个RadioButton的状态，并保证只可选一个

	// public static List<QueryXnAddressListItemsBean> list;

	public List<AddressListBean> data = new ArrayList<AddressListBean>();

	private boolean editStatus = false;

	public int defaltIndex;

	private Handler handler;

	private boolean flag;

	public MyAddressAdapter(Context context, Handler handler, boolean flag) {
		this.context = context;
		in = LayoutInflater.from(context);
		this.handler = handler;
		this.flag = flag;
	}

	// public void setData(List<QueryXnAddressListItemsBean> data){
	// this.list=data;
	// }

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View v, ViewGroup arg2) {
		final ViewHolder h;
		if (v == null) {
			h = new ViewHolder();
			v = in.inflate(R.layout.item_address, null);
			h.name = (TextView) v.findViewById(R.id.name);
			h.phone = (TextView) v.findViewById(R.id.phone);
			h.text = (TextView) v.findViewById(R.id.text);
			h.address = (TextView) v.findViewById(R.id.address);
			h.radio = (RadioButton) v.findViewById(R.id.radio);
			h.img_default = (ImageView) v.findViewById(R.id.img_default);
			h.item = (RelativeLayout) v.findViewById(R.id.item);
			v.setTag(h);
		} else {
			h = (ViewHolder) v.getTag();
		}
		AddressListBean addressListBean = data.get(position);
		h.name.setText(addressListBean.getName());
		h.phone.setText(addressListBean.getTelephone());
		h.address.setText(addressListBean.getArea() + " "
				+ addressListBean.getAddress());
		if (position == defaltIndex) {
			h.text.setVisibility(View.VISIBLE);
		} else {
			h.text.setVisibility(View.INVISIBLE);
		}
		if (editStatus) {
			h.img_default.setSelected(false);
			h.img_default.setImageResource(R.drawable.chose_unclick);
			h.img_default.setVisibility(View.VISIBLE);
			// list.clear();
		} else {
			h.img_default.setVisibility(View.GONE);
		}

		h.item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editStatus) {
					if (h.img_default.isSelected()) {
						Log.i("tag", "取消");
						AddressManageActivity.toDeleteAddressId
								.remove(position);
						h.img_default
								.setImageResource(R.drawable.chose_unclick);
						h.img_default.setSelected(false);
					} else {
						Log.i("tag", "点击");
						AddressManageActivity.toDeleteAddressId.put(position,
								data.get(position).getAddressId());
						h.img_default.setImageResource(R.drawable.choose_click);
						h.img_default.setSelected(true);
					}
				} else {
					if (flag) {
						// 选择一个地址，再返回
						Message msg = new Message();
						msg.arg1 = position;
						msg.what = 102;
						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.arg1 = position;
						msg.what = 100;
						handler.sendMessage(msg);
					}
				}
			}
		});
		// String isDefault = list.get(p).getIsDefault();
		// if ("2".equals(isDefault))
		// {
		// h.img_default.setVisibility(View.VISIBLE);
		// h.radio.setVisibility(View.VISIBLE);
		// }
		// else
		// {
		// h.img_default.setVisibility(View.INVISIBLE);
		// h.radio.setVisibility(View.INVISIBLE);
		// }

		// h.radio.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View arg0)
		// {
		// // 重置，确保最多只有一项被选中
		// for (String key : states.keySet())
		// {
		// states.put(key, false);
		// }
		// states.put(list.get(p).getId(), h.radio.isChecked());
		// notifyDataSetChanged();
		// }
		// });
		// boolean res = false;
		// if (states.get(list.get(p).getId()) == null
		// || states.get(list.get(p).getId()) == false)
		// {
		// res = false;
		// states.put(list.get(p).getId(), false);
		// }
		// else
		// res = true;
		// h.radio.setChecked(res);
		return v;
	}

	class ViewHolder {
		TextView name, phone, address, text;

		ImageView img_default;

		RadioButton radio;

		RelativeLayout item;
	}

	public boolean isEditStatus() {
		return editStatus;
	}

	public void setEditStatus(boolean editStatus) {
		this.editStatus = editStatus;
	}

	public void removeItem(List<Integer> deletePosition) {
		for (int i = 0; i < deletePosition.size(); i++) {
			// list.remove(i);
			Log.i("tag", "remove item..." + i);
		}
		notifyDataSetChanged();
	}
}
