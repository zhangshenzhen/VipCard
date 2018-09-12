package com.bjypt.vipcard.model.cf;

public class CfAccountDetailData {

    private int resultStatus;

    private String msg;
    private CfAccountData resultData;

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

    public CfAccountData getResultData() {
        return resultData;
    }

    public void setResultData(CfAccountData resultData) {
        this.resultData = resultData;
    }
}
