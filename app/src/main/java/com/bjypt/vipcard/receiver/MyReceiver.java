package com.bjypt.vipcard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bjypt.vipcard.activity.InformationActivity;
import com.bjypt.vipcard.service.PlayMusicService;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/4/7.
 */
public class MyReceiver extends BroadcastReceiver {

    public static String TAG = "liyunte";
    private BroadCastReceiverUtils utils;
    private String action_news = "action_news";


    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        Log.e("liyunte", "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

            Log.e("liyunte", "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));


        }else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            context.startService(new Intent(context, PlayMusicService.class));
            Log.e("liyunte", "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            try {
                Log.d("liyunte","news_action_news-----------");
                utils = new BroadCastReceiverUtils();
                utils.sendBroadCastReceiver(context,action_news);
                utils.sendBroadCastReceiver(context,"news_red_point");
                JSONObject obj = new JSONObject(extra);
                String type = obj.getString("type");
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的收到的信息: " + type);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

            Log.e("liyunte", "[MyReceiver] 用户点击打开了通知");
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject obj = new JSONObject(extra);
                String type = obj.getString("type");
                Log.e("liyunte", "[MyReceiver] 接收到推送下来的通知的收到的信息: " + type);
                Intent intent_info = new Intent(context,InformationActivity.class);
                intent_info.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (type.equals("2")){
                    intent_info.putExtra("type", "2");//商家优惠信息
                    context.startActivity(intent_info);
                }else if (type.equals("0")){
                    intent_info.putExtra("type", "0");//商家活动
                    context.startActivity(intent_info);
                }else if(type.equals("3")){
                    intent_info.putExtra("type", "3");//系统
                    context.startActivity(intent_info);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }




        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.d(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
