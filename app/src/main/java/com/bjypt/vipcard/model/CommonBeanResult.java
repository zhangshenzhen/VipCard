package com.bjypt.vipcard.model;

/**
 * Created by 崔龙 on 2017/9/30.
 */

public class CommonBeanResult<T> {
    private String resultStatus;
    private String msg;
    private T resultData;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResultData() {
        return resultData;
    }

    public void setResultData(T resultData) {
        this.resultData = resultData;
    }
}
