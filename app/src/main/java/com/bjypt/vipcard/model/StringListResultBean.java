package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by wanglei on 2017/10/27.
 */

public class StringListResultBean {

    /**
     * resultStatus : 0
     * msg : SUCCESS
     * resultData : ["平台余额每日首单支付0.00元以上可享受9.9折优惠"]
     */

    private int resultStatus;
    private String msg;
    private List<String> resultData;

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

    public List<String> getResultData() {
        return resultData;
    }

    public void setResultData(List<String> resultData) {
        this.resultData = resultData;
    }
}
