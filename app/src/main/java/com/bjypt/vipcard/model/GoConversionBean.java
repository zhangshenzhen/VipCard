package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/7/26.
 */

public class GoConversionBean {
    private String resultStatus;
    private String msg;
    private InformationDatas resultData;

    public InformationDatas getResultData() {
        return resultData;
    }

    public void setResultData(InformationDatas resultData) {
        this.resultData = resultData;
    }

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

}
