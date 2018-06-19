package com.bjypt.vipcard.activity.shangfeng.common.enums;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.bjypt.vipcard.activity.shangfeng.common.enums.UserInformationFields.HEAD_PORTRAIT;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.UserInformationFields.IS_LOGIN;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.UserInformationFields.NICK_NAME;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.UserInformationFields.PHONE_NUMBER;
import static com.bjypt.vipcard.activity.shangfeng.common.enums.UserInformationFields.USER_KEY;


/**
 * 用户相关数据 存储 key
 */
@StringDef({IS_LOGIN,NICK_NAME,USER_KEY,PHONE_NUMBER,HEAD_PORTRAIT})
@Retention(RetentionPolicy.SOURCE)
public @interface UserInformationFields {
    /**
     * 登录状态
     */
    String IS_LOGIN = "login";

    /**
     * 用户昵称
     */
    String NICK_NAME = "nickname";

    /**
     * 用户主键
     */
    String USER_KEY = "pkregister";

    /**
     * 用户手机号
     */
    String PHONE_NUMBER = "phoneno";

    /**
     * 用户头像url
     */
    String HEAD_PORTRAIT = "position";

    /**
     * 支付密码
     */
    String PAY_PASSWORD = "paypassword";

    /**
     * 登录密码
     */
    String LOGIN_PASSWORD = "loginpassword";

}
