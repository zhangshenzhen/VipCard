package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public class VipCenterInMarchantBean {

    private String resultStatus;
    private String msg;

    public List<VipCenterInMarchantResultData> getResultData() {
        return resultData;
    }

    public void setResultData(List<VipCenterInMarchantResultData> resultData) {
        this.resultData = resultData;
    }

    private List<VipCenterInMarchantResultData> resultData;

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
}
