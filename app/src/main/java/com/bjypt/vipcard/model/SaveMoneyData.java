package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/12
 * Use by
 */
public class SaveMoneyData {
    private String msg;

    public saveMoneyListBean getResultData() {
        return resultData;
    }

    public void setResultData(saveMoneyListBean resultData) {
        this.resultData = resultData;
    }

    private saveMoneyListBean resultData;
    private String resultStatus;

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
}
