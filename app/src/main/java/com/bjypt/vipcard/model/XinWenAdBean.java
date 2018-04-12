package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class XinWenAdBean {
    private String id;
    private String title;
    private String lastTime;
    private List<XinWenPicBean> pics;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public List<XinWenPicBean> getPics() {
        return pics;
    }

    public void setPics(List<XinWenPicBean> pics) {
        this.pics = pics;
    }
}
