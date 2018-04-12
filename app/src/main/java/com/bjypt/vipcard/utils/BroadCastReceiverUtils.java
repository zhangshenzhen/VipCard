package com.bjypt.vipcard.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Administrator on 2016/4/28.
 */
public class BroadCastReceiverUtils {


    /**
     * 发送广播
     * @param context
     * @param action
     */
    public  void sendBroadCastReceiver(Context context,String action){
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);
    }

    /**
     * 发送广播 带参数
     * @param context  上下文对象
     * @param action   动作
     * @param name   参数名
     * @param value  参数值
     */
    public  void sendBroadCastReceiver(Context context,String action,String name,String value){
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(name, value);
        context.sendBroadcast(intent);
    }

    /**
     * 解除广播
     * @param context
     * @param receiver
     */
    public  void UnRegisterBroadCastReceiver(Context context,BroadcastReceiver receiver){
      context.unregisterReceiver(receiver);
    }

    /**
     * 注册广播
     * @param context  上下文对象
     * @param action   动作
     * @param receiver  接收器
     */
    public  void registerBroadCastReceiver(Context context,String action,BroadcastReceiver receiver){
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        context.registerReceiver(receiver,filter);
    }
}
