package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/6/14
 * Use by
 */
public class LIfeServiceData {
    private String msg;
    private String resultStatus;
    private List<LifeServiceBean> resultData;

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

    public List<LifeServiceBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<LifeServiceBean> resultData) {
        this.resultData = resultData;
    }
}
