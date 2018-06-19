package com.bjypt.vipcard.activity.shangfeng.data.bean;

import java.io.Serializable;

/**
 * Created by wanglei on 2018/5/18.
 */

public class CommonWebData implements Serializable{


    public CommonWebData(){}
    public CommonWebData(String url, String title) {
        this.url = url;
        this.title = title;
    }

    private String url;
    private String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
