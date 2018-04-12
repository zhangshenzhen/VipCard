package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/27.
 */
public class SingleDetailDishesRootBean {
    private int resultStatus;

    private String msg;

    private SingleDetailDishesResultBean resultData ;

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

    public SingleDetailDishesResultBean getResultData() {
        return resultData;
    }

    public void setResultData(SingleDetailDishesResultBean resultData) {
        this.resultData = resultData;
    }
}
