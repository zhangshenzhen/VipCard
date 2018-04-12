package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class XinWenData {
    private String resultStatus;
    private String msg;
    private List<XinWenBean> resultData;

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

    public List<XinWenBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<XinWenBean> resultData) {
        this.resultData = resultData;
    }
}
