package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/10/20.
 */

public class SignInGongGaoBean {

    private int resultStatus;
    private String msg;
    private List<SignInGonggaoData>  resultData;

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

    public List<SignInGonggaoData> getResultData() {
        return resultData;
    }

    public void setResultData(List<SignInGonggaoData> resultData) {
        this.resultData = resultData;
    }
}
