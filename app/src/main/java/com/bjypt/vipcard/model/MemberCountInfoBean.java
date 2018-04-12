package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/11/25.
 */

public class MemberCountInfoBean {
    private int resultStatus;
    private String msg;
    private MemberCountInfoData resultData;

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

    public MemberCountInfoData getResultData() {
        return resultData;
    }

    public void setResultData(MemberCountInfoData resultData) {
        this.resultData = resultData;
    }
}
