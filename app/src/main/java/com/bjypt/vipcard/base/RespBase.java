package com.bjypt.vipcard.base;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/19
 * Use by
 */
public class RespBase {

    String resultStatus;
    String msg;
    Object resultData = "";

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public boolean isSuccess() {
        if (resultStatus != null && resultStatus.equals("0")) {
            return true;
        } else {
            return false;
        }
    }
}
