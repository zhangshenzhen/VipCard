package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/19.
 */
public class PresentRecordRootBean {
   private String resultStatus;
   private String msg;
   private List<PrecentRecordBean> resultData;

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

    public List<PrecentRecordBean> getResultData() {
        return resultData;
    }

    public void setResultData(List<PrecentRecordBean> resultData) {
        this.resultData = resultData;
    }
}
