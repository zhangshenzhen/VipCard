package com.bjypt.vipcard.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/19.
 */
public class DetailOrderRootBean implements Serializable{
    private String resultStatus;
    private String msg;
    private DetailOrderBean resultData;

    public void setResultStatus(String  resultStatus){
        this.resultStatus = resultStatus;
    }
    public String   getResultStatus(){
        return this.resultStatus;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setResultData(DetailOrderBean resultData){
        this.resultData = resultData;
    }
    public DetailOrderBean getResultData(){
        return this.resultData;
    }

}
