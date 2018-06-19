package com.bjypt.vipcard.activity.shangfeng.common.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.bjypt.vipcard.activity.shangfeng.common.enums.ActionTypeEnum.H5;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.ActionTypeEnum.Native;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.ActionTypeEnum.NoAction;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.ActionTypeEnum.SystemBrowse;


/**
 * Created by wanglei on 2018/5/18.
 */

@IntDef({NoAction, H5, Native,SystemBrowse})
@Retention(RetentionPolicy.SOURCE)
public @interface ActionTypeEnum {
    int NoAction = 0;//, "不可点击"), H5(1, "H5地址"), Native(2, "原生");
    int H5 = 1;
    int Native = 2;
    int SystemBrowse = 3; //系统浏览
}
