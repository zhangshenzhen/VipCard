package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/25.
 */
public class ListDetailTypeTwoBean {

    /* "resultStatus": 0,
    "msg": "SUCCESS",
    */
    private String resultStatus;
    private String msg;

    public ListDetailTypeTwoListBean getResultData() {
        return resultData;
    }

    public void setResultData(ListDetailTypeTwoListBean resultData) {
        this.resultData = resultData;
    }

    private ListDetailTypeTwoListBean resultData;



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

