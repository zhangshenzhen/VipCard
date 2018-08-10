package com.bjypt.vipcard.common;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 接口请求返回码
 */
@StringDef({TrackCommon.ViewTrackCategroy,
        TrackCommon.ViewTrackPagesMainTab,
        TrackCommon.ViewTrackPagesMerchatTab,
        TrackCommon.ViewTrackPagesNewsTab,
        TrackCommon.ViewTrackPagesMineTab,
        TrackCommon.ViewTrackRegister,
        TrackCommon.ViewTrackCommonMenus,
        TrackCommon.ViewTrackMerchantDetail,
        TrackCommon.ViewTrackAwayPay,
        TrackCommon.ViewTrackRecharge,
        TrackCommon.ViewTrackWithdraw,
        TrackCommon.ViewTrackLogin})
@Retention(RetentionPolicy.SOURCE)
public @interface TrackCommon {

    String ViewTrackCategroy = "查看页面";
    String ViewTrackPagesMainTab = "首页";
    String ViewTrackPagesMerchatTab = "铺子";
    String ViewTrackPagesNewsTab = "有料";
    String ViewTrackPagesMineTab = "我的";

    String ViewTrackLogin = "登录";
    String ViewTrackRegister = "注册";

    String ViewTrackCommonMenus = "八大汇";

    String ViewTrackLifeService = "服务";

    String ViewTrackMerchantDetail = "商家详情";

    String ViewTrackAwayPay = "立即买单";

    String ViewTrackRecharge = "我要充值";

    String ViewTrackWithdraw = "我要提现";


}
