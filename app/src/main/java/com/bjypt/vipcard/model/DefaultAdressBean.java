package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/21
 * Use by 默认地址
 */
public class DefaultAdressBean {
    private String msg;
    private String resultStatus;
    private List<AdressBean> resultData;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public List<AdressBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<AdressBean> resultData) {
        this.resultData = resultData;
    }
}
