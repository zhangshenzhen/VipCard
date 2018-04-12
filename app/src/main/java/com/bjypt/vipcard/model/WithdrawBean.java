package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/26
 * Use by
 */
public class WithdrawBean {
    private String msg;
    private String resultStatus;
    private WithdrawData resultData;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public WithdrawData getResultData() {
        return resultData;
    }

    public void setResultData(WithdrawData resultData) {
        this.resultData = resultData;
    }
}
