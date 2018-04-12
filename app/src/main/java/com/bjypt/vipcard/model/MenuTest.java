package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/4/18.
 */

public class MenuTest {

    private String resultStatus;
    private String msg;
    private MerchantDetailsBean resultData;

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

    public MerchantDetailsBean getResultData() {
        return resultData;
    }

    public void setResultData(MerchantDetailsBean resultData) {
        this.resultData = resultData;
    }
}
