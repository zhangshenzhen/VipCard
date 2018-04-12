package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/6/16
 * Use by
 */
public class ZbarBalanceBean {
    private String msg;
    private String resultStatus;
    private ZbarBalanceData resultData;

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

    public ZbarBalanceData getResultData() {
        return resultData;
    }

    public void setResultData(ZbarBalanceData resultData) {
        this.resultData = resultData;
    }
}
