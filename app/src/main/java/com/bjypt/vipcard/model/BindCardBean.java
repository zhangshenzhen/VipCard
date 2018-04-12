package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/20
 * Use by 绑定了银行卡返回的信息
 */
public class BindCardBean {
    private String resultStatus;
    private String msg;
    private BindCardData resultData;

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

    public BindCardData getResultData() {
        return resultData;
    }

    public void setResultData(BindCardData resultData) {
        this.resultData = resultData;
    }
}
