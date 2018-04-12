package com.sinia.orderlang.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import com.bjypt.vipcard.R;
import com.sinia.orderlang.fragment.ClassifyFragment;
import com.sinia.orderlang.fragment.SeckillFragment;
import com.sinia.orderlang.fragment.SelectAddressFragment;
import com.sinia.orderlang.fragment.SpecialOfferFragment;

public class ShowDialogActivity extends FragmentActivity {
	private ClassifyFragment classifyFragment;
	private FragmentManager mFragmentManager;
	private SelectAddressFragment selectAddressFragment;
	/** 显示选择类别还是选择地址 */
	private boolean showWhat;
	/**
	 * 从秒杀还是特价跳转过来的 1秒杀 2特价
	 * */
	public int secOrSpec;
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				Log.e("tyz","finish");
				if(secOrSpec == 2&&showWhat){
					SpecialPriceMainActivity.flag_area_special = "1";
				}
				finish();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_dialog);
		showWhat = getIntent().getBooleanExtra("showWhat", false);
		secOrSpec = getIntent().getIntExtra("secOrSpec", 0);
		initDisplay();
		initFragment();
	}

	private void initDisplay() {
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams lp = getWindow().getAttributes(); // 获取对话框当前的参数值
		lp.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的
		lp.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的
		lp.alpha = 1.0f; // 设置本身透明度
		lp.dimAmount = 0.5f; // 设置黑暗度
		getWindow().setAttributes(lp); // 设置生效
		getWindow().setGravity(Gravity.BOTTOM | Gravity.RIGHT);// 设置右下角
	}

	private void initFragment() {
		mFragmentManager = getSupportFragmentManager();
		if (showWhat) {
			selectAddressFragment = new SelectAddressFragment();
			mFragmentManager.beginTransaction()
					.add(R.id.root, selectAddressFragment)
					.show(selectAddressFragment).commit();
		} else {
			classifyFragment = new ClassifyFragment();
			mFragmentManager.beginTransaction()
					.add(R.id.root, classifyFragment).show(classifyFragment)
					.commit();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d("lamp", "onPause");
		// 处理
		if (showWhat) {
			if (secOrSpec == 1) {
				if (!selectAddressFragment.choose) {
					SeckillFragment.city = "-1";
					SeckillFragment.areaName = "-1";
				}
			} else if (secOrSpec == 2) {
				if (!selectAddressFragment.choose) {
					SpecialOfferFragment.city = "-1";
					SpecialOfferFragment.areaName = "-1";
					SpecialPriceMainActivity.flag_area_special = "1";
					Log.e("tyz","choose");
				}
			}
		} else {

		}
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		Log.d("lamp", "ondestroy");

		super.onDestroy();
	}
}
