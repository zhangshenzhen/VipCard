package com.bjypt.vipcard.activity.shangfeng.common.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.bjypt.vipcard.activity.shangfeng.common.enums.EventBusWhat.H5pay;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.EventBusWhat.NewOrder;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.EventBusWhat.WxPayResult;


/**
 * Created by wanglei on 2018/5/24.
 */
@IntDef({H5pay,NewOrder,WxPayResult})
@Retention(RetentionPolicy.SOURCE)
public @interface EventBusWhat {
    //H5支付结果处理
    int H5pay = 0;//
    int NewOrder =1;
    //微信支付结果通知
    int WxPayResult =2;
}
