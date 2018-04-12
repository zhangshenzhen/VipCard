package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class AllOrderRootBean {
    private int resultStatus;

    private String msg;

    private List<AllOrderBean> resultData ;

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
    public void setResultData(List<AllOrderBean> resultData){
        this.resultData = resultData;
    }
    public List<AllOrderBean> getResultData(){
        return this.resultData;
    }

}
