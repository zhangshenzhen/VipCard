package com.bjypt.vipcard.activity.shangfeng.common.enums;

/**
 * Created by wanglei on 2018/5/15.
 */

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.bjypt.vipcard.activity.shangfeng.common.enums.PayTypeEnum.Alipay;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.PayTypeEnum.WechatPay;


@IntDef({Alipay, WechatPay})
@Retention(RetentionPolicy.SOURCE)
public @interface PayTypeEnum {
    int Alipay = 1;
    int WechatPay = 6;
}
