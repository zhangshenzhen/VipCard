package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public class NewYinLianPayData {
    private String msg;
    private String resultStatus;
    private NewYinLianPayBean resultData;

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

    public NewYinLianPayBean getResultData() {
        return resultData;
    }

    public void setResultData(NewYinLianPayBean resultData) {
        this.resultData = resultData;
    }
}
