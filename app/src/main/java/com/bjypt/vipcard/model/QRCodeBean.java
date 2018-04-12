package com.bjypt.vipcard.model;

/**
 * Created by User on 2016/8/10.
 */
public class QRCodeBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : http://123.57.232.188:8080/hyb/ws/post/toRegistPage?userId=f359b57b096e44fbae6913c6f4ade0fb
     */

    private int resultStatus;
    private String msg;
    private String resultData;

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

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }
}
