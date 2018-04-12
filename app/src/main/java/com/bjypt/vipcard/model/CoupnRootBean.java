package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by liyunte on 2017/2/10.
 */

public class CoupnRootBean {
    private int resultStatus;
    private String msg;
    private List<CoupnBean> resultData ;

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

    public List<CoupnBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<CoupnBean> resultData) {
        this.resultData = resultData;
    }
}
