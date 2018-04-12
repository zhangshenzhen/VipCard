package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/23.
 */
public class ListDetailBean {
    private String resultStatus;
    private String msg;

    public ListDetail getResultData() {
        return resultData;
    }

    public void setResultData(ListDetail resultData) {
        this.resultData = resultData;
    }

    private ListDetail resultData;

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
