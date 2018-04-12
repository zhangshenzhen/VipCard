package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/20
 * Use by
 */
public class BankNameBean {
    private String resultStatus;
    private String msg;
    private List<BankNameData> resultData;

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

    public List<BankNameData> getResultData() {
        return resultData;
    }

    public void setResultData(List<BankNameData> resultData) {
        this.resultData = resultData;
    }
}
