package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */

public class NewCategoryRootBean {
    private String resultStatus;
    private String msg;
    private List<NewCategoryBean> resultData;

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

    public List<NewCategoryBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<NewCategoryBean> resultData) {
        this.resultData = resultData;
    }
}
