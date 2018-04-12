package com.bjypt.vipcard.model;

/**
 * Created by admin on 2017/4/26.
 */

public class PayAwayBean {
    private String resultStatus;
    private String msg;
    private PayAwayData resultData;

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

    public PayAwayData getResultData() {
        return resultData;
    }

    public void setResultData(PayAwayData resultData) {
        this.resultData = resultData;
    }
}
