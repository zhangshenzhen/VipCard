package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */

public class MyHomePageBean {
    private int resultStatus;
    private String msg;
    private List<MyHomePageData> resultData;


    public int getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(int resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<MyHomePageData> getResultData() {
        return resultData;
    }

    public void setResultData(List<MyHomePageData> resultData) {
        this.resultData = resultData;
    }
}
