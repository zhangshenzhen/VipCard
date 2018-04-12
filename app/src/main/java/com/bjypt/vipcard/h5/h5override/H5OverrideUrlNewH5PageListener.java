package com.bjypt.vipcard.h5.h5override;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bjypt.vipcard.activity.LifeServireH5Activity;
import com.sinia.orderlang.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** 根据H5 处理打开H5页面
 * Created by admin on 2017/8/15.
 */

public class H5OverrideUrlNewH5PageListener extends  BaseH5OverrideUriListener{

    public H5OverrideUrlNewH5PageListener(Uri uri){
        super(uri);
    }

    @Override
    public void handler(Activity context) {
        String h5url = mUri.getQueryParameter(h5UrlKey);
        if(StringUtil.isNotEmpty(h5url)){
            Intent intent = new Intent();
            intent.setClass(context,  LifeServireH5Activity.class);
            Set<String> parameterNames = mUri.getQueryParameterNames();
            for (String key : parameterNames) {
                if (!excludeParamKey(key)) {
                    intent.putExtra(key, mUri.getQueryParameter(key));
                }
            }
            context.startActivity(intent);
        }
    }

}
