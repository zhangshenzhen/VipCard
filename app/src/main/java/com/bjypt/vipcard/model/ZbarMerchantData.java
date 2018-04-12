package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/7/2
 * Use by
 */
public class ZbarMerchantData {
    private String resultStatus;
    private String msg;
    private String resultData;

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

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }
}
