package com.bjypt.vipcard.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.TosiftLeftListAdapter;
import com.bjypt.vipcard.adapter.TosiftRightListAdapter;
import com.bjypt.vipcard.model.MerchantCategoryOneBeam;

import java.util.List;

/****
 * 分类 弹出的popupwindow
 *
 */
public class CategoryPopup {
	TosiftRightListAdapter rightListAdapter;
	TosiftLeftListAdapter leftListAdapter;
	PopupWindow popupWindow;
	ListView right;
	ListView left;
	LinearLayout other;
	LinearLayout other1;
	LinearLayout layout_quanbu_two;
	List<MerchantCategoryOneBeam> list;
	public CategoryPopup() {
	}

	/**
	 * 分类|位置筛选
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param parnet
	 *            显示在谁的下面
	 * @param context
	 *            上下文
	 * @param list
	 *            数据源
	 * @param handler
	 *            通知UI做数据改变的
	 *
	 *
	 */

	public void setPopu_toSift(int width, int height, View parnet,
							   final Context context, final List<MerchantCategoryOneBeam> list,final List<List<MerchantCategoryOneBeam>> listall ,
							   final Handler handler,int listwidth) {
		if (null != popupWindow && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
		CategoryPopup.this.list = list;
		final View view = LayoutInflater.from(context).inflate(
				R.layout.category_quanbu_one, null);
		popupWindow = new PopupWindow(view);
		other1 = (LinearLayout)view.findViewById(R.id.other1);
		left = (ListView) view.findViewById(R.id.list_quanbu_one);//左边的listview
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) left.getLayoutParams();
		linearParams.width = listwidth;
		layout_quanbu_two = (LinearLayout)view.findViewById(R.id.lay_list_quanbu_two);
		left.setLayoutParams(linearParams);
		right = (ListView) view.findViewById(R.id.list_quanbu_two);//右边的listview
		other = (LinearLayout) view
				.findViewById(R.id.other);//通过点击这个空白的布局实现取消弹出框

		leftListAdapter = new TosiftLeftListAdapter(
				context, list,listall,rightListAdapter,other1,right,layout_quanbu_two,handler);
		left.setAdapter(leftListAdapter);
		leftListAdapter.notifyDataSetInvalidated();
		Log.v("TAGGG",""+list.size());
		rightListAdapter=new TosiftRightListAdapter(context,null,handler);
		right.setAdapter(rightListAdapter);



		view.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					popupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
		other.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popupWindow.dismiss();
				return true;
			}
		});

		popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		popupWindow.setWidth(width);

		popupWindow.setHeight(height);
		popupWindow.setFocusable(false);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.update();
		popupWindow.showAsDropDown(parnet);
	}


	/**
	 * 智能筛选
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param parnet
	 *            显示在谁的下面
	 * @param context
	 *            上下文
	 *
	 *            数据源
	 * @param handler
	 *            通知UI做数据改变的
	 */
	public void setPopu_toSift_znsx(int width, int height, View parnet,
									final Context context,
									final Handler handler) {
		final String[] str = new String[] { "评价最好", "最近加入", "销量优先","人气最高"};
		if (null != popupWindow && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
		final View view = LayoutInflater.from(context).inflate(
				R.layout.layout_tosift_onlyonelist_zn, null);
		popupWindow = new PopupWindow(view);
		ListView left = (ListView) view.findViewById(R.id.listview_frist);
		LinearLayout other = (LinearLayout) view
				.findViewById(R.id.lin_popu_other);
		left.setAdapter(new BaseAdapter() {
			@Override
			public View getView(final int position, View convertView,
								ViewGroup parent) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.layout_tosift_onlyonetext_item, null);
				TextView tv = (TextView) convertView
						.findViewById(R.id.tv_tosift_onetext);
				tv.setText(str[position]);
				convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.v("TAG", "setOnClickListener");
						Message msg = handler.obtainMessage(3, str[position]);
						handler.sendMessage(msg);
						dismiss();
					}
				});
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return str.length;
			}
		});
		other.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popupWindow.dismiss();
				return true;
			}
		});
		view.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					popupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
		popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		popupWindow.setWidth(width);
		popupWindow.setHeight(height);
//		popupWindow.setContentView(view);
		popupWindow.setFocusable(false);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.update();
		popupWindow.showAsDropDown(parnet);
	}

	/***
	 *
	 *
	 * @param width
	 * @param height
	 * @param parnet
	 * @param context
	 * @param handler
	 */
	public void setJuli(int width, int height, View parnet,
						final Context context,
						final Handler handler){
		final String[] strings = new String[] { "a","1km","2km", "3km",
				"4km","5km","6km","7km"};
		if (null != popupWindow && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
		final View view = LayoutInflater.from(context).inflate(
				R.layout.layout_tosift_onlyonelist, null);
		popupWindow = new PopupWindow(view);
		ListView left = (ListView) view.findViewById(R.id.listview_frist);
		LinearLayout other = (LinearLayout) view
				.findViewById(R.id.lin_popu_other);
		int pos = 0;
		left.setAdapter(new BaseAdapter() {
			int pos = 0;

			@Override
			public View getView(final int position, View convertView,
								ViewGroup parent) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.layout_juli_item, null);
				final ImageView iv = (ImageView) convertView.findViewById(R.id.iv_juli_item);
				final TextView tv = (TextView) convertView
						.findViewById(R.id.tv_juli_item);
				if ("a".equals(strings[position])){
					tv.setText("全部");
				}else {
					tv.setText(strings[position]);
				}
				convertView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						Message msg = handler.obtainMessage(2, strings[position]);
						handler.sendMessage(msg);
						dismiss();
					}
				});
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return strings[position];
			}

			@Override
			public int getCount() {
				return strings.length;
			}
		});

		other.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popupWindow.dismiss();
				return true;
			}
		});
		view.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					popupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
		popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		popupWindow.setWidth(width);
		popupWindow.setHeight(height);
		popupWindow.setFocusable(false);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.update();
		popupWindow.showAsDropDown(parnet);

	}

	/**
	 * 消失并滞空
	 */
	public void dismiss() {
		if (null != popupWindow && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	public boolean isShowing() {
		if (null != popupWindow) {
			if (popupWindow.isShowing()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean isNull(){
		if(null == popupWindow){
			return false;
		}else
			return true;
	}
}
