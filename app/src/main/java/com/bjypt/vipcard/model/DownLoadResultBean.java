package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/5/12.
 */
public class DownLoadResultBean {
    private String msg;

    private String flag;

    private String url;

    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setFlag(String flag){
        this.flag = flag;
    }
    public String getFlag(){
        return this.flag;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
}
