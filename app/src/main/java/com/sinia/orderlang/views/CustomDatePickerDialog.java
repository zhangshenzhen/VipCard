package com.sinia.orderlang.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.views.CustomDatePicker.ChangingListener;

public class CustomDatePickerDialog extends Dialog {

	private TextView tv_sure;
	private TextView tv_cancel;
	private onDateListener listener;
	private Calendar c = Calendar.getInstance();
	private CustomDatePicker cdp;
	private Context context;

	public CustomDatePickerDialog(Context context, Calendar c) {
		super(context, R.style.dialog);
		this.c = c;
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_datepicker_dialog);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		setCanceledOnTouchOutside(true);
		Window window = getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (display.getWidth() - 100); // 设置宽度
		getWindow().setAttributes(lp);
		tv_sure = (TextView) findViewById(R.id.sure_layout);
		tv_cancel = (TextView) findViewById(R.id.cancel_layout);

		cdp = (CustomDatePicker) findViewById(R.id.cdp);

		cdp.setDate(c);

		SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy年MM月dd日 E");
		String string = sdfFrom.format(c.getTime());

		cdp.addChangingListener(new ChangingListener() {

			@Override
			public void onChange(Calendar c1) {
				c = c1;
			}
		});
		View.OnClickListener clickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == tv_sure) {
					if (listener != null) {
						listener.dateFinish(c);
					}

				}
				dismiss();
			}
		};

		tv_sure.setOnClickListener(clickListener);
		tv_cancel.setOnClickListener(clickListener);

	}

	/**
	 * 设置限制日期，设置后，不能选择设置的开始日期以前的日期
	 * 
	 * @param c
	 */
	public void setNowData(Calendar c) {
		cdp.setNowData(c);

	}

	/**
	 * 设置点击确认的事件
	 * 
	 * @param listener
	 */
	public void addDateListener(onDateListener listener) {
		this.listener = listener;
	}

	public interface onDateListener {
		void dateFinish(Calendar c);
	}

}
