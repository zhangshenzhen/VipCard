package com.bjypt.vipcard.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/9
 * Use by
 */
public class MQTTService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        JPushInterface.resumePush(getApplicationContext());
        SharedPreferenceUtils.saveToSharedPreference(this,"isstart","Y");
        String clientId = SharedPreferenceUtils.getFromSharedPreference(getApplicationContext(), Config.userConfig.phoneno);
        Log.e("liyunte", "clientId" + clientId);
        JPushInterface.init(getApplicationContext()); // 初始化 JPush
        JPushInterface.setDebugMode(true);
        boolean is = JPushInterface.isPushStopped(getApplicationContext());
        JPushInterface.setSilenceTime(getApplicationContext(),0,0,23,59);
        Log.e("liyunte", "isssssssssssssssss   " + is);
        Set<String> tagset=new HashSet<String>();
        tagset.add(clientId);

        JPushInterface.setAlias(getApplicationContext(), clientId, new TagAliasCallback() {
            @Override
            public void gotResult(int responseCode, String alias, Set<String> tags) {
                Log.d("liyunte", "responseCode" + responseCode + "alias" + alias + "tags" + tags);
            }
        });
        JPushInterface.setTags(getApplicationContext(), tagset, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.d("liyunte", "responseCode" + i + "alias" + s + "tags" + set);
            }
        });
        super.onStart(intent, startId);
    }


}
