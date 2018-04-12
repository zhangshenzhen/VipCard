package com.bjypt.vipcard.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

/**
 * Created by Administrator on 2016/4/28.
 */
public class JpushService extends Service{


    private String TAG="aaa";
    private static final String RECEVICE_ACTION="com.bjypt.vipcard.MESSAGE_RECEIVED_ACTION";
    private final IBinder binder = new MyBinder();
    private String s="";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }

    public class MyBinder extends Binder {

        public void show(Intent intent,int i){
            onStart(intent,0);
        }

    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(final Intent intent, int startId) {
        final String  clientId= SharedPreferenceUtils.getFromSharedPreference(this, Config.userConfig.pkregister);
        Log.i("aaa", "-------推送  ID----------:"+clientId);

        if(!"".equals(clientId)){
            JPushInterface.init(this); // 初始化 JPush
            Set<String>  tagset=new HashSet<String>();
            tagset.add(clientId);
            JPushInterface.setTags(getApplicationContext(), tagset, null);
        }
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }
    public void def(){
        JPushLocalNotification ln = new JPushLocalNotification();
        ln.setBuilderId(0);
        ln.setContent("hhh");
        ln.setTitle("ln");
        ln.setNotificationId(11111111) ;
        ln.setBroadcastTime(System.currentTimeMillis() + 1000 *5);

        Map<String , Object> map = new HashMap<String, Object>() ;
        map.put("name", "jpush") ;
        map.put("test", "111") ;
        JSONObject json = new JSONObject(map) ;
        ln.setExtras(json.toString()) ;
        JPushInterface.addLocalNotification(getApplicationContext(), ln);
    }
    public void register(String str){
//        String a=JPushInterface.getRegistrationID(getApplicationContext());
//        Log.i("aaa", "获取到的id=" + a);


        s=str;
        Log.i("aaa",""+str);
//
//        //设置显示最多条数
//        JPushInterface.setLatestNotificationNumber(getApplicationContext(), 3);
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(JPushInterface.ACTION_MESSAGE_RECEIVED);
//        MyReceiver myReceiver = new MyReceiver();
//        registerReceiver(myReceiver, intentFilter);
//
//        JPushInterface.setAlias(getApplicationContext(), str, new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//                if (i == 0) {
//                    Log.i("aaa", "使用别名成功");
//                } else {
//                    Log.i("aaa", "使用别名失败");
//                }
//
//                Log.i("aaa", "获取到的Alias=" + s);
//            }
//        });

    }

}
