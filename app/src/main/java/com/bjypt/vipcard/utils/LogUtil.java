package com.bjypt.vipcard.utils;


import android.util.Log;

import com.bjypt.vipcard.BuildConfig;
import com.sinia.orderlang.utils.StringUtil;

/**
 * Created by wanglei on 2017/11/28.
 */

public class LogUtil {
    public static void debugPrint(String message){
        if(BuildConfig.DEBUG && StringUtil.isNotEmpty(message)){
            Log.i("hyb", message);
        }
    }
}
