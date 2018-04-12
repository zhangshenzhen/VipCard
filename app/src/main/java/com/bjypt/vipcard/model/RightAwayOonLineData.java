package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/11/19 0019.
 */

public class RightAwayOonLineData {
    private String msg;
    private String resultStatus;
    private RightAwayOnLineBean resultData;

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

    public RightAwayOnLineBean getResultData() {
        return resultData;
    }

    public void setResultData(RightAwayOnLineBean resultData) {
        this.resultData = resultData;
    }
}
