package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/11
 * Use by
 */
public class IsVipBean {
    private String resultStatus;
    private String msg;
    private IsVipData resultData;

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

    public IsVipData getResultData() {
        return resultData;
    }

    public void setResultData(IsVipData resultData) {
        this.resultData = resultData;
    }


}
