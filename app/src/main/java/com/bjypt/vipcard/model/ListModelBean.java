package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */

public class ListModelBean<T> {
    private String resultStatus;
    private String msg;
    public List<T> resultData;

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

    public List<T> getResultData() {
        return resultData;
    }

    public void setResultData(List<T> resultData) {
        this.resultData = resultData;
    }
}
