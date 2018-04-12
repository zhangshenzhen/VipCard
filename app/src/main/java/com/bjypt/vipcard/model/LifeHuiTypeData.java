package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class LifeHuiTypeData {
    private String msg;
    private String resultStatus;
    private List<LifeHuiTypeBean> resultData;

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

    public List<LifeHuiTypeBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<LifeHuiTypeBean> resultData) {
        this.resultData = resultData;
    }
}
