package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/22
 * Use by
 */
public class PayOneDetailsBean {
    private String msg;
    private String resultStatus;
    private PayInfoBean resultData;

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

    public PayInfoBean getResultData() {
        return resultData;
    }

    public void setResultData(PayInfoBean resultData) {
        this.resultData = resultData;
    }
}
