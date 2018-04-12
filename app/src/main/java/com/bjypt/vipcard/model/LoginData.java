package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/18
 * Use by 登录模型
 */
public class LoginData {
    private String resultStatus;
    private String msg;
    private LoginBean resultData;

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

    public LoginBean getResultData() {
        return resultData;
    }

    public void setResultData(LoginBean resultData) {
        this.resultData = resultData;
    }
}
