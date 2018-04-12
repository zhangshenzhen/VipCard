package com.bjypt.vipcard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.bjypt.vipcard.utils.BroadCastReceiverUtils;

/**
 * Created by Administrator on 2016/11/16.
 */

public class NetWorkReceiver extends BroadcastReceiver {
    private boolean isconnect = true;
    private BroadCastReceiverUtils utils;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (utils==null){
            utils = new BroadCastReceiverUtils();
        }
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
           //网络不可用
            Log.e("liyunte","------------ 网络没有连接");

        }else {
            Log.e("liyunte","------------ 网络已经连接");
                   Log.e("liyunte","准备发送消息-------------");
                   utils.sendBroadCastReceiver(context,"re_dingwei");

        }

    }
}
