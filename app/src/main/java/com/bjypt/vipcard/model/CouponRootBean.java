package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public class CouponRootBean {
    private int resultStatus;
    private String msg;
    private List<CouponBean> resultData ;

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
    public void setResultData(List<CouponBean> resultData){
        this.resultData = resultData;
    }
    public List<CouponBean> getResultData(){
        return this.resultData;
    }
}
