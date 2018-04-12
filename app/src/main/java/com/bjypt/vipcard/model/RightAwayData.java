package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class RightAwayData {
    private String resultStatus;
    private String msg;
    private RightAwayBean resultData;

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

    public RightAwayBean getResultData() {
        return resultData;
    }

    public void setResultData(RightAwayBean resultData) {
        this.resultData = resultData;
    }
}
