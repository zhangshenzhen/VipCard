package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class OrderIdData {
    private String msg;
    private String resultStatus;
    private OrderIdBean resultData;

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

    public OrderIdBean getResultData() {
        return resultData;
    }

    public void setResultData(OrderIdBean resultData) {
        this.resultData = resultData;
    }
}
