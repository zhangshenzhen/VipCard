package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/25
 * Use by
 */
public class UpdataHeadBean {
    private String resultStatus;
    private String msg;
    private UpdateHeadData resultData;

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

    public UpdateHeadData getResultData() {
        return resultData;
    }

    public void setResultData(UpdateHeadData resultData) {
        this.resultData = resultData;
    }
}
