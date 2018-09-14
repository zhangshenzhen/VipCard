package com.bjypt.vipcard.model.cf;

import com.bjypt.vipcard.activity.shangfeng.data.bean.BannerBean;

import java.util.List;

public class BannerBeanResultData {
    private int resultStatus;

    private String msg;

    List<BannerBean> resultData;

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

    public List<BannerBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<BannerBean> resultData) {
        this.resultData = resultData;
    }
}
