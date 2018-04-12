package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/21
 * Use by
 */
public class CommentDetailsBean {
    private String msg;
    private String resultStatus;
    private CommentDetailsData resultData;

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

    public CommentDetailsData getResultData() {
        return resultData;
    }

    public void setResultData(CommentDetailsData resultData) {
        this.resultData = resultData;
    }
}
