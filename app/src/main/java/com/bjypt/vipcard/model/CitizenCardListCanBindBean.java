package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/8/25.
 */

public class CitizenCardListCanBindBean {
    private String resultStatus;
    private String msg;
    private CitizenCardListCanBindData resultData;

    public CitizenCardListCanBindData getResultData() {
        return resultData;
    }

    public void setResultData(CitizenCardListCanBindData resultData) {
        this.resultData = resultData;
    }

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


}
