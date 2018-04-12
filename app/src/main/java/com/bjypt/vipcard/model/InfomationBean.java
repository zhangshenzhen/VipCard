package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/28
 * Use by
 */
public class InfomationBean {
    private String msg;
    private String resultStatus;
    private List<InfomationData> resultData;

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

    public List<InfomationData> getResultData() {
        return resultData;
    }

    public void setResultData(List<InfomationData> resultData) {
        this.resultData = resultData;
    }
}
