package com.bjypt.vipcard.utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

/**
 * Created by 涂有泽 .
 * Date by 2016/6/18
 * Use by
 */
public class PhoneCpuId {

    public static String getDeviceId(Context context){
       if(context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
         TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
           return tm.getDeviceId();
       }else{
           return "";
       }
    }

}
