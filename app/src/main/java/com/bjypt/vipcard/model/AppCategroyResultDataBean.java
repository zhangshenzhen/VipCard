package com.bjypt.vipcard.model;

import android.content.Intent;

import java.util.List;

/**
 * Created by wanglei on 2017/12/26.
 */

public class AppCategroyResultDataBean {
    private Integer resultStatus;
    private String msg;
    private List<AppCategoryBean> resultData;

    public Integer getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(Integer resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<AppCategoryBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<AppCategoryBean> resultData) {
        this.resultData = resultData;
    }
}
