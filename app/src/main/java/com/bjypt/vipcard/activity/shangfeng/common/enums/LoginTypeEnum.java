package com.bjypt.vipcard.activity.shangfeng.common.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.bjypt.vipcard.activity.shangfeng.common.enums.LoginTypeEnum.All;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.LoginTypeEnum.Login;


/**
 * Created by wanglei on 2018/5/18.
 */

@IntDef({All,Login})
@Retention(RetentionPolicy.SOURCE)
public @interface LoginTypeEnum {
//All(0, "不需要"), Login(1, "需要");
    int All = 0;
    int Login =1;
}
