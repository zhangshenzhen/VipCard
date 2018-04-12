package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/14
 * Use by 获取订单流水号
 */
public class OrderResult {
    private String orderId;
    private String primaryk;
    private String createtime;
    private String preorderId;

    public String getPreorderId() {
        return preorderId;
    }

    public void setPreorderId(String preorderId) {
        this.preorderId = preorderId;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrimaryk() {
        return primaryk;
    }

    public void setPrimaryk(String primaryk) {
        this.primaryk = primaryk;
    }
}
