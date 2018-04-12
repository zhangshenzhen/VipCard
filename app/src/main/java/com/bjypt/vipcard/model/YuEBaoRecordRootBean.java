package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by liyunte on 2017/1/9.
 */

public class YuEBaoRecordRootBean {
private String resultStatus;
    private String msg;
    private List<YuEBaoRecordBean> resultData;

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

    public List<YuEBaoRecordBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<YuEBaoRecordBean> resultData) {
        this.resultData = resultData;
    }
}
