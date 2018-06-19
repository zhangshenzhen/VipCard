package com.bjypt.vipcard.activity.shangfeng.data;


import com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode;

/**
 * Created by Dell on 2018/4/23.
 */

public interface RequestCallBack<T> {

    void success(@ResultCode int resultCode, T data);

    void onError(@ResultCode int resultCode, String errorMsg);


}
