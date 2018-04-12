package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class NewsRootBean {
    private int resultStatus;

    private String msg;

    private List<NewsBean> resultData ;

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
    public void setResultData(List<NewsBean> resultData){
        this.resultData = resultData;
    }
    public List<NewsBean> getResultData(){
        return this.resultData;
    }
}
