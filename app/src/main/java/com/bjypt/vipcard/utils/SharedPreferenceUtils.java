package com.bjypt.vipcard.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/9
 * Use by SharedPreference工具类
 */
public class SharedPreferenceUtils {
    /**
     * 使用SharedPreference存储数据
     */
    public static void saveToSharedPreference(Context mContext,String key,String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("hyb",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * 使用SharedPreference得到数据
     */
    public static String getFromSharedPreference(Context mContext,String key){
        String string;
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("hyb",Context.MODE_PRIVATE);
            string = sharedPreferences.getString(key,"");
        }catch (Exception e){
            return "";
        }
        return string;
    }

    /**
     * 使用SharedPreference得到数据
     */
    public static String getFromSharedPreference(Context mContext,String key, String defaultStr){
        String string;
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("hyb",Context.MODE_PRIVATE);
            string = sharedPreferences.getString(key,defaultStr);
        }catch (Exception e){
            return "";
        }
        return string;
    }
}
