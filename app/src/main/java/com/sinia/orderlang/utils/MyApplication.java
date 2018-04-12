package com.sinia.orderlang.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyApplication extends Application {

	private static MyApplication instance;

	public static Context context;

	public static boolean isLogin = false;
	public static boolean isChanged = false;
	public static String orderId;
	public static String getOrderId() {
		return orderId;
	}

	public static void setOrderId(String orderId) {
		MyApplication.orderId = orderId;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		context = this.getApplicationContext();
		// 创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);
		ImageLoader.getInstance().init(configuration);
		instance = this;
	}

	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	public static boolean isChanged() {
		return isChanged;
	}

	public static void setChanged(boolean isChanged) {
		MyApplication.isChanged = isChanged;
	}

	public void setBooleanValue(String in_settingName, boolean in_val) {
		SharedPreferences sp = context.getSharedPreferences("is_login",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = sp.edit();
		ed.putBoolean(in_settingName, in_val);
		ed.commit();
		ed = null;
		sp = null;
	}

	public Boolean getBoolValue(String in_settingName) {
		SharedPreferences sp = context.getSharedPreferences("is_login",
				Context.MODE_PRIVATE);
		boolean ret = sp.getBoolean(in_settingName, false);
		sp = null;
		return ret;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		MyApplication.isLogin = isLogin;
	}

}
