package com.sinia.orderlang.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class AppInfoUtil {

	/**
	 * 
	 * TODO:获取屏幕高度
	 * 
	 * @param context
	 * @return 屏幕高度
	 */
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 
	 * TODO:获取屏幕宽度
	 * 
	 * @param context
	 * @return 屏幕宽度
	 */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 
	 * TODO:获取IMEI
	 * 
	 * @param context
	 * @return IMEI
	 */
	public static String getIMEI(Context context) {
		return ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	/**
	 * 
	 * TODO:获取IMSI
	 * 
	 * @param context
	 * @return IMSI
	 */
	public static String getIMSI(Context context) {
		return ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
	}

	/**
	 * 
	 * TODO:获取设备型号
	 * 
	 * @return 设备型号
	 */
	public static String getModel() {
		return Build.MODEL;
	}

	/**
	 * 
	 * TODO:获取设备制�?�?
	 * 
	 * @return 设备制�?�?
	 */
	public static String getManufacturer() {
		return Build.MANUFACTURER;
	}

	/**
	 * 
	 * TODO:获取系统版本�?
	 * 
	 * @return 系统版本�?
	 */
	public static String getSDKReleaseVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 
	 * TODO:获取系统SDK版本
	 * 
	 * @return 系统SDK版本
	 */
	public static int getOsSdkVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 
	 * TODO:获取程序包名
	 * 
	 * @param context
	 * @return 程序包名
	 */
	public static String getPackageName(Context context) {
		return context.getPackageName();
	}

	public static int getVersionCode(Context context) {
		int versionCode = 0;
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		// 当前应用的版本名称
		return versionCode;

	}

	public static String getVersionName(Context context) {
		String versionName = "";
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		// 当前应用的版本名称
		return versionName;

	}

	public static int getSDKversion() {
		return Build.VERSION.SDK_INT;
	}

	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		if (info.getMacAddress() != null) {
			return info.getMacAddress();
		}
		return "";
	}

	public static String getProductModel() {
		return Build.MODEL;
	}

	public static String getProductVERSION() {
		return Build.VERSION.RELEASE;
	}

	public static String getProduct() {
		return Build.MANUFACTURER;
	}

}