package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/23.
 */
public class BillsDatailBean {
    private String resultStatus;
    private String msg;
    private List<BDResultData> resultData;

    public List<BDResultData> getResultData() {
        return resultData;
    }

    public void setResultData(List<BDResultData> resultData) {
        this.resultData = resultData;
    }

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
