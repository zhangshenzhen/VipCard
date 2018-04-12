package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class YouhuiBuyBean {
    private String msg;
    private String resultStatus;
    private YouhuiBuyData resultData;

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

    public YouhuiBuyData getResultData() {
        return resultData;
    }

    public void setResultData(YouhuiBuyData resultData) {
        this.resultData = resultData;
    }
}
