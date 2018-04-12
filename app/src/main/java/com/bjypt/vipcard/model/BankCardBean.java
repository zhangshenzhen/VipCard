package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 */

public class BankCardBean {
    private int resultStatus;
    private String msg;
    private BankCardDetailBean resultData ;

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

    public BankCardDetailBean getResultData() {
        return resultData;
    }

    public void setResultData(BankCardDetailBean resultData) {
        this.resultData = resultData;
    }
}
