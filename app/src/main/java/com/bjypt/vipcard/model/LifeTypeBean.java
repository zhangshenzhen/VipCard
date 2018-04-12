package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13 0013.
 */

public class LifeTypeBean {
    private String msg;
    private String resultStatus;
    private List<LifeTypeData> resultData;

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

    public List<LifeTypeData> getResultData() {
        return resultData;
    }

    public void setResultData(List<LifeTypeData> resultData) {
        this.resultData = resultData;
    }
}
