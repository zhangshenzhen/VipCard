package com.bjypt.vipcard.h5.h5override;

import android.app.Activity;
import android.content.Context;

/**
 * Created by admin on 2017/8/15.
 */

public interface H5OverrideUrlListener {
    /**
     * 处理整个流程控制
     * @param context
     */
    public void process(Activity context);

    /**
     * 处理各个监听不同的处理
     * @param context
     */
    public void handler(Activity context);
    public String getQuery();
}
