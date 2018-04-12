package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/20
 * Use by 某订单下的订餐内容
 */
public class OrderContentBean {
    private String msg;
    private String resultStatus;
    private List<OrderContentData> resultData;

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

    public List<OrderContentData> getResultData() {
        return resultData;
    }

    public void setResultData(List<OrderContentData> resultData) {
        this.resultData = resultData;
    }
}
