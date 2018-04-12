package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class BannerResultdataBean {
    private String resultStatus;
    private String msg;

    public List<BannerBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<BannerBean> resultData) {
        this.resultData = resultData;
    }

    private List<BannerBean> resultData;

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

}
