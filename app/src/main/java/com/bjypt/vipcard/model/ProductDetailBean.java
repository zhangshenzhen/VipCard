package com.bjypt.vipcard.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/15.
 */
public class ProductDetailBean {

    private String resultStatus;
    private String msg;
    private ArrayList<ResultData> resultData;

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

    public ArrayList<ResultData> getResultData() {
        return resultData;
    }

    public void setResultData(ArrayList<ResultData> resultData) {
        this.resultData = resultData;
    }
}
