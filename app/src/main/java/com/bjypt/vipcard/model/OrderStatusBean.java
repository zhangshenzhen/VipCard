package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/5/15.
 */

public class OrderStatusBean {

    private  int resultStatus;
    private  String msg;
    private  String resultData;

    public int getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(int resultStatus) {
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
