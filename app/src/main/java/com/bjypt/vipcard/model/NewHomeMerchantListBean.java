package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */

public class NewHomeMerchantListBean {
    private String resultStatus;
    private String msg;
    private List<NewMerchantListBean> resultData;

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

    public List<NewMerchantListBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<NewMerchantListBean> resultData) {
        this.resultData = resultData;
    }
}
