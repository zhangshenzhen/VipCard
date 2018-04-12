package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/3/10.
 */

public class ObjectModelBean<T> {
    private String resultStatus;
    private String msg;
    public T resultData;

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

    public T getResultData() {
        return resultData;
    }

    public void setResultData(T resultData) {
        this.resultData = resultData;
    }
}
