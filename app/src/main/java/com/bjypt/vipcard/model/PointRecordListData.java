package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/5/17
 * Use by
 */
public class PointRecordListData {
    private String resultStatus;
    private String msg;
    private List<PointRecordListBean> resultData;

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

    public List<PointRecordListBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<PointRecordListBean> resultData) {
        this.resultData = resultData;
    }
}
