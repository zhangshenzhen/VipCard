package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/5/17
 * Use by 积分记录总积分和消费积分
 */
public class PointRecordInfo {
    private String resultStatus;
    private String msg;
    private PointRecordDetails resultData;

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

    public PointRecordDetails getResultData() {
        return resultData;
    }

    public void setResultData(PointRecordDetails resultData) {
        this.resultData = resultData;
    }
}
