package com.bjypt.vipcard.h5.h5override;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.ArraySet;

import com.bjypt.vipcard.view.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 根据H5 处理跳转原生的界面
 * Created by admin on 2017/8/15.
 */

public class H5OverrideUrlNewNativeActivity extends BaseH5OverrideUriListener {

    public H5OverrideUrlNewNativeActivity(Uri uri) {
        super(uri);
    }

    @Override
    public void handler(Activity context) {
        Set<String> parameterNames = mUri.getQueryParameterNames();
        List<String> arrayList = new ArrayList<>();
        for (String key : parameterNames) {
            if (!excludeParamKey(key)) {
                arrayList.add(key);
            }
        }
        String className = mUri.getQueryParameter(nativeClassParamKey);
        Class aClass  = null;
        try {
            aClass = Class.forName(className);
            Intent intent = new Intent();
            intent.setClass(context, aClass);
            if (context.getPackageManager().resolveActivity(intent, 0) == null) {
                // 说明系统中不存在这个activity
                ToastUtil.showToast(context, "暂未开通");
            } else {
                for (String key : arrayList){
                    intent.putExtra(key, mUri.getQueryParameter(key));
                }
                context.startActivity(intent);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
