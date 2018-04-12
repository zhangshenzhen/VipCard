package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/14
 * Use by 获取订单流水号
 */
public class OrderInfoBean {
    private String resultStatus;
    private String msg;
    private OrderResult resultData;

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

    public OrderResult getResultData() {
        return resultData;
    }

    public void setResultData(OrderResult resultData) {
        this.resultData = resultData;
    }
}
