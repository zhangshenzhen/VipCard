package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class NewPayData {
    private String msg;
    private String resultStatus;
    private NewPayBean resultData;

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

    public NewPayBean getResultData() {
        return resultData;
    }

    public void setResultData(NewPayBean resultData) {
        this.resultData = resultData;
    }
}
