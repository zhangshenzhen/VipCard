package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/8/11.
 */

public class CitizenCardMerchantBean {
    private String resultStatus;
    private String msg;
    private List<CitizenCardMerchantData> resultData;

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

    public List<CitizenCardMerchantData> getResultData() {
        return resultData;
    }

    public void setResultData(List<CitizenCardMerchantData> resultData) {
        this.resultData = resultData;
    }
}
