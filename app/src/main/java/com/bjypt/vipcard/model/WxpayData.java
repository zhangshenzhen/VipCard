package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/7/1
 * Use by
 */
public class WxpayData {
    private String resultStatus;
    private String msg;
    private WxpayBean resultData;

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

    public WxpayBean getResultData() {
        return resultData;
    }

    public void setResultData(WxpayBean resultData) {
        this.resultData = resultData;
    }
}
