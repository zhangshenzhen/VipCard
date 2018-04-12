//package com.bjypt.vipcard.utils;
//
//import android.content.Context;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//
///**
// * 通过回调函数setLocation提供：经纬度、城市、邮编号
// */
//
//public class LocationListen {
//	private LocationClient mLocationClient;//定位SDK的核心类
//	private MyLocationListener mMyLocationListener;
//	private Context context;
//	private AddressListen Listen;
//
//	public LocationListen(Context context, AddressListen Listen) {
//		this.context = context;
//		this.Listen = Listen;
//	}
//
//	public void LocationAction() {
//		mLocationClient = new LocationClient(context.getApplicationContext());
//		mMyLocationListener = new MyLocationListener();
//		mLocationClient.registerLocationListener(mMyLocationListener);//注册监听函数：
//		setLocation();
//		mLocationClient.start();
//	}
//
//	private void setLocation() {
//		LocationClientOption option = new LocationClientOption();//该类用来设置定位SDK的定位方式。
//		// 设置定位模式
//		option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
//		// 返回的定位结果
//		option.setCoorType("gcj02");//设置坐标类型：
//		// 设置定时定位的时间间隔。单位ms
//		option.setScanSpan(1000);
//		// 返回的定位结果包含地址信息
//		option.setIsNeedAddress(true);
//		mLocationClient.setLocOption(option);
//	}
//
//	public AddressListen getListen() {
//		return Listen;
//	}
//
//	public void setListen(AddressListen listen) {
//		Listen = listen;
//
//	}
//
//	public interface AddressListen {
//
//
//		public void setLocation(double latitude, double longitude, String cityCode, String city, String address);
//
//
//	}
//
//	public class MyLocationListener implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation location) {//封装了定位SDK的定位结果，在BDLocationListener的onReceive方法中获取。通过该类用户可以获取error code，位置的坐标，精度半径等信息
//
//
//
//
//			// String province = location.getProvince();
//			String city = location.getCity();
//
//			// String district = location.getDistrict();
//			String address = location.getAddrStr() + "";//获取反地理编码
//
//			double latitude = location.getLatitude();// 获取纬度
//
//			double longitude = location.getLongitude();// 获取经度
//			String cityCode = location.getCityCode();
//			// Toast.makeText(context, address+"===", Toast.LENGTH_LONG).show();
//			//l && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(cityCode)
//			if (Listen != null) {
//				Listen.setLocation(latitude, longitude,cityCode,city,address);
//			}
//			mLocationClient.stop();
//
//		}
//	}
//
//}
