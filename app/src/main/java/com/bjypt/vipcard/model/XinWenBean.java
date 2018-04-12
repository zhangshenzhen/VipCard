package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class XinWenBean {
    private String id;
    private String lasttime;
    private String title;
    private List<XinWenPicBean> pics;
    private String picSize;

    public String getPicSize() {
        return picSize;
    }

    public void setPicSize(String picSize) {
        this.picSize = picSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<XinWenPicBean> getPics() {
        return pics;
    }

    public void setPics(List<XinWenPicBean> pics) {
        this.pics = pics;
    }
}
