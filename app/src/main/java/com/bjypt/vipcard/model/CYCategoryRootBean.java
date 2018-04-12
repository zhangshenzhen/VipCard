package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/12/14.
 */

public class CYCategoryRootBean {
private String resultStatus;
private String msg;
private CYCategoryBean resultData;

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

    public CYCategoryBean getResultData() {
        return resultData;
    }

    public void setResultData(CYCategoryBean resultData) {
        this.resultData = resultData;
    }
}
