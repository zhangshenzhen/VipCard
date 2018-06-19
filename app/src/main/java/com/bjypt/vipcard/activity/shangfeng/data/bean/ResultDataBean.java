package com.bjypt.vipcard.activity.shangfeng.data.bean;


import com.bjypt.vipcard.activity.shangfeng.common.enums.ResultCode;

/**
 * Created by Administrator on 2018/5/3.
 */
public class ResultDataBean {

    @ResultCode
    private int resultStatus;
    private String msg;
    private Object resultData;

    public ResultDataBean(int resultStatus, String msg, Object resultData) {
        this.resultStatus = resultStatus;
        this.msg = msg;
        this.resultData = resultData;
    }

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

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }
}
