package com.bjypt.vipcard.h5.h5override;

import android.net.Uri;

/**
 * Created by admin on 2017/8/15.
 */

public class H5OverrideUrlFactory {
    public static H5OverrideUrlListener getInstance(String uri){
        Uri mUri = Uri.parse(uri);
        String method = mUri.getPath();
        H5OverrideUrlListener listener = null;
        if(method.equals("/native")){
              listener = new H5OverrideUrlNewNativeActivity(mUri);
        }else if(method.equals("/h5page")){
              listener = new H5OverrideUrlNewH5PageListener(mUri);
        }else if(method.equals("/tip")){
            listener = new H5OverrideUriToast(mUri);
        }
        return listener;
    }
}
