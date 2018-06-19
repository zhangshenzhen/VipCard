package com.bjypt.vipcard.activity.shangfeng.common.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode.HTTPSIGNERROR;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode.SERVE_UPDATE;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode.SUCCESS;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode.USER_INEXISTENCE;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode.USER_PASSWORD_ERROR;

/**
 * 接口请求返回码
 */
@IntDef({SUCCESS,USER_INEXISTENCE,USER_PASSWORD_ERROR,SERVE_UPDATE,HTTPSIGNERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface ResultCode {

    //************通用接口状态说明************
    //成功
    int SUCCESS = 0;//操作成功



    //************登录失败接口状态说明****
    int USER_INEXISTENCE = -1;//用户不存在 、 验证码已过期
    int USER_PASSWORD_ERROR = 1011;//登录密码错误
    int NO_BIND_PHONE = 2001;//未绑定手机号


    //************服务端升级************
    int SERVE_UPDATE = 1001;//服务端升级

    int HTTPSIGNERROR = -9000;


}
