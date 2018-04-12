package com.bjypt.vipcard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.TextView;

public class RegisterReceiverUtils {
	private static RegisterReceiverUtils ut;
	private TextView tv;
	public static RegisterReceiverUtils getInstance(){
		if(ut==null){
			ut=new RegisterReceiverUtils();
		}
		return ut;
	}
	public RegisterReceiverUtils(TextView tv){
		this.tv=tv;

	}

	public RegisterReceiverUtils(){
	}
	/**
	 * 
	 * @param con
	 * @param mReceiver 注册
	 */
	public void registerBoradcastReceiver(Context con,BroadcastReceiver mReceiver){
		IntentFilter mfilter = new IntentFilter();
		mfilter.addAction("img_hand");
		mfilter.addAction("scanning");//扫一扫
		mfilter.addAction("homelode");//首页加载
		mfilter.addAction("main");
		
		con.registerReceiver(mReceiver, mfilter);
		Log.e("---注册广播---", "-----");
	}


	/**
	 * 
	 * @param context
	 * @param action  广播标记
	 *        str 数据
	 */
	public void sendBroadcast(Context context,String action,String str){

		Intent mIntent = new Intent();
		mIntent.setAction(action);
		mIntent.putExtra("scanning", str);
		context.sendBroadcast(mIntent);
		//mIntent.putExtra("bitmap",bitmapByte);
		Log.e("-----发送广播----", "-----"+str);
	}
	/**
	 * 
	 * @param con
	 * @param mReceiver  取消
	 */
	public void unregisterReceiver(Context con,BroadcastReceiver mReceiver){
		con.unregisterReceiver(mReceiver);
		Log.e("--------取消广播--------", "------------");
	}

}
