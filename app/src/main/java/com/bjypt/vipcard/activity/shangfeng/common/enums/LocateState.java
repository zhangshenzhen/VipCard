package com.bjypt.vipcard.activity.shangfeng.common.enums;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.bjypt.vipcard.activity.shangfeng.common.enums.LocateState.LOCATING;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.LocateState.FAILED;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.LocateState.SUCCESS;


/**
 * 定位状态返回码
 */
@IntDef({LOCATING,FAILED,SUCCESS})
@Retention(RetentionPolicy.SOURCE)
public @interface LocateState {

    int LOCATING    = 111;
    int FAILED      = 666;
    int SUCCESS     = 888;

}
