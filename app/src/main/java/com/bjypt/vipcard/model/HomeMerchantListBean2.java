package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/9/19
 * Use by
 */
public class HomeMerchantListBean2 {

    private String resultStatus;
    private String msg;
    private List<MerchantListBean> resultData;

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

    public List<MerchantListBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<MerchantListBean> resultData) {
        this.resultData = resultData;
    }

}
