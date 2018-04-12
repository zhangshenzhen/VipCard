package com.bjypt.vipcard.h5.h5override;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.bjypt.vipcard.view.ToastUtil;
import com.sinia.orderlang.utils.StringUtil;

/**
 * Created by admin on 2017/8/15.
 */

public class H5OverrideUriToast extends BaseH5OverrideUriListener {

    public H5OverrideUriToast(Uri uri) {
        super(uri);
    }

    @Override
    public void handler(Activity context) {
        String msg = mUri.getQueryParameter("msg");
        if(StringUtil.isNotEmpty(msg)){
            ToastUtil.showToast(context, msg);
        }
    }
}
