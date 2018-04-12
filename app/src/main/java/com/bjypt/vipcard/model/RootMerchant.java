package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public class RootMerchant {
    private int resultStatus;
    private String msg;
    private List<NewMerchantListBean> resultData ;
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
    public void setResultData(List<NewMerchantListBean> resultData){
        this.resultData = resultData;
    }
    public List<NewMerchantListBean> getResultData(){
        return this.resultData;
    }

}
