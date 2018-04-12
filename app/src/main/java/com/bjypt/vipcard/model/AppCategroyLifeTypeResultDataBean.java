package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by wanglei on 2017/12/26.
 */

public class AppCategroyLifeTypeResultDataBean {
    private Integer resultStatus;
    private String msg;
    private List<AppCategoryLifeTypeBean> resultData;

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

    public List<AppCategoryLifeTypeBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<AppCategoryLifeTypeBean> resultData) {
        this.resultData = resultData;
    }

    @Override
    public String toString() {
        return "AppCategroyLifeTypeResultDataBean{" +
                "resultStatus=" + resultStatus +
                ", msg='" + msg + '\'' +
                ", resultData=" + resultData +
                '}';
    }
}
