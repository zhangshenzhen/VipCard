package com.sinia.orderlang.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.city.CityPicker;

public class DialogUtils {
	public static Dialog dialog;
	private static DialogUtils instance = null;
	private TextView mEnsure, mCancel;
	private ListView mListview;

	public DialogUtils() {
	}

	public static DialogUtils getInstance() {
		if (instance == null) {
			instance = new DialogUtils();
		}
		return instance;
	}

	private static Dialog initDialog(Context context, int resId, int theme) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(resId, null);
		Dialog dialog = new Dialog(context, theme);
		dialog.setContentView(v, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		return dialog;
	}

	public static Dialog createAddressDialog(Context context,
			final TextView province) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.activity_pickview1, null);
		dialog = new Dialog(context, R.style.dialog);
		dialog.show();
		dialog.setContentView(v, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (display.getWidth()); // 设置宽度
		dialog.getWindow().setAttributes(lp);
		final CityPicker cityPicker = (CityPicker) v
				.findViewById(R.id.citypicker);
		TextView cancelTextView = (TextView) v.findViewById(R.id.cancel_layout);
		TextView sureTextView = (TextView) v.findViewById(R.id.sure_layout);
		cancelTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		sureTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (cityPicker.getCity().equals("县")
						|| cityPicker.getCity().equals("辖区")) {
					if (cityPicker.getCouny().equals("县")
							|| cityPicker.getCouny().equals("辖区")) {
						province.setText(cityPicker.getProvince());
					} else {
						province.setText(cityPicker.getProvince()+" "
								+ cityPicker.getCity() +" "+cityPicker.getCouny());
					}
				} else {
					province.setText(cityPicker.getProvince()+" "
							+ cityPicker.getCity() +" "+ cityPicker.getCouny());
				}
				dialog.dismiss();
			}
		});
		return dialog;
	}

	public static Dialog createChoosePayDialog(Context context,
			final Handler handler, int type) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_choose_pay, null);
		dialog = new Dialog(context, R.style.dialog);
		dialog.show();
		dialog.setContentView(v, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (display.getWidth()); // 设置宽度
		dialog.getWindow().setAttributes(lp);
		final LinearLayout ll_ali = (LinearLayout) v.findViewById(R.id.ll_ali);
		final LinearLayout ll_wechat = (LinearLayout) v
				.findViewById(R.id.ll_wechat);
		final LinearLayout ll_bank = (LinearLayout) v
				.findViewById(R.id.ll_bank);
		TextView tv_cancel = (TextView) v.findViewById(R.id.tv_cancel);
		switch (type) {
		case 1:
			ll_ali.setBackgroundResource(R.drawable.bg_seleted);
			ll_wechat.setBackgroundResource(R.drawable.bg_unselect);
			ll_bank.setBackgroundResource(R.drawable.bg_unselect);
			break;
		case 2:
			ll_wechat.setBackgroundResource(R.drawable.bg_seleted);
			ll_ali.setBackgroundResource(R.drawable.bg_unselect);
			ll_bank.setBackgroundResource(R.drawable.bg_unselect);
			break;
		case 3:
			ll_bank.setBackgroundResource(R.drawable.bg_seleted);
			ll_ali.setBackgroundResource(R.drawable.bg_unselect);
			ll_wechat.setBackgroundResource(R.drawable.bg_unselect);
			break;
		default:
			break;
		}
		ll_ali.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(100);
				ll_ali.setBackgroundResource(R.drawable.bg_seleted);
				ll_wechat.setBackgroundResource(R.drawable.bg_unselect);
				ll_bank.setBackgroundResource(R.drawable.bg_unselect);
				dialog.dismiss();
			}
		});
		ll_wechat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(101);
				ll_wechat.setBackgroundResource(R.drawable.bg_seleted);
				ll_ali.setBackgroundResource(R.drawable.bg_unselect);
				ll_bank.setBackgroundResource(R.drawable.bg_unselect);
				dialog.dismiss();
			}
		});
		ll_bank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(102);
				ll_bank.setBackgroundResource(R.drawable.bg_seleted);
				ll_ali.setBackgroundResource(R.drawable.bg_unselect);
				ll_wechat.setBackgroundResource(R.drawable.bg_unselect);
				dialog.dismiss();
			}
		});
		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		return dialog;
	}

}
