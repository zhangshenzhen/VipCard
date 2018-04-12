package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class LifeHuiData {
    private String resultStatus;
    private String msg;
    private List<HomeTypeBean> resultData;

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

    public List<HomeTypeBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<HomeTypeBean> resultData) {
        this.resultData = resultData;
    }
}
