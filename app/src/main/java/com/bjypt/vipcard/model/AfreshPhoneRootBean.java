package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/22.
 */
public class AfreshPhoneRootBean {
    private int resultStatus;

    private String msg;

    private AfreshPhoneBean resultData;

    public void setResultStatus(int resultStatus){
        this.resultStatus = resultStatus;
    }
    public int getResultStatus(){
        return this.resultStatus;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setResultData(AfreshPhoneBean resultData){
        this.resultData = resultData;
    }
    public AfreshPhoneBean getResultData(){
        return this.resultData;
    }
}
