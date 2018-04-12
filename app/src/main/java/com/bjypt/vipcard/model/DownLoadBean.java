package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/5/12.
 */
public class DownLoadBean {
    private int resultStatus;

    private String msg;

    private DownLoadResultBean resultData;

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
    public void setResultData(DownLoadResultBean resultData){
        this.resultData = resultData;
    }
    public DownLoadResultBean getResultData(){
        return this.resultData;
    }

}
