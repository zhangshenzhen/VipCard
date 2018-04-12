package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/8/17.
 */

public class CardBalanceBean {
    private String resultStatus;
    private String msg;
    private CitizenCardListData resultData;

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

    public CitizenCardListData getResultData() {
        return resultData;
    }

    public void setResultData(CitizenCardListData resultData) {
        this.resultData = resultData;
    }
}
