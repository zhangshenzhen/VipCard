package com.sinia.orderlang.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DialogUtil {
	public static Dialog dialog;
	private static TextView mEnsure;
	private static TextView mCancel;
	private static ListView mListview;
	private static int selectedIdSave = -1;
	private int selectIdGoodType = -1;

	private static Dialog initDialog(Context context, int resId, int theme) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(resId, null);
		Dialog dialog = new Dialog(context, theme);
		dialog.setContentView(v, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		return dialog;
	}
	
	public interface OnOkListener {
		public void onClick();
	}

	public interface OnCancelListener {
		public void onClick();
	}
}
