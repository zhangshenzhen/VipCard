package com.bjypt.vipcard.cycleviewpager;

import android.content.Context;
import android.os.Handler;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/12
 * Use by 为了防止oom，定义外部类，防止内部类对外部类的引用
 */
public class CycleViewPagerHandler extends Handler{
    Context context;
    public CycleViewPagerHandler(Context context){
        this.context = context;
    }
};
