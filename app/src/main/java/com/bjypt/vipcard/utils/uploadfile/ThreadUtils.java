package com.bjypt.vipcard.utils.uploadfile;

import android.os.Looper;
import android.util.Log;

/**
 * Created by 崔龙 on 2017/11/30.
 * <p>
 * 查看线程工具类
 */

public class ThreadUtils {

    public static final String TAG = "ThreadUtils";

    public static boolean isInMainThread() {
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        Log.i(TAG, "isInMainThread myLooper=" + myLooper + ";mainLooper=" + mainLooper);
        return myLooper == mainLooper;
    }
}
