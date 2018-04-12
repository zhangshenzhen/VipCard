package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class RightAddOrderData {
    private String resultStatus;
    private String msg;
    private RightAddOrderBean resultData;

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

    public RightAddOrderBean getResultData() {
        return resultData;
    }

    public void setResultData(RightAddOrderBean resultData) {
        this.resultData = resultData;
    }
}
