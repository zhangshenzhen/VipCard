package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by wanglei on 2017/12/26.
 */

public class AppCategoryLifeTypeBean {

    private Integer app_id;
    private String app_name;
    private String app_icon;
    private List<AppCategoryBean> subLife;

    public Integer getApp_id() {
        return app_id;
    }

    public void setApp_id(Integer app_id) {
        this.app_id = app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_icon() {
        return app_icon;
    }

    public void setApp_icon(String app_icon) {
        this.app_icon = app_icon;
    }

    public List<AppCategoryBean> getSubLife() {
        return subLife;
    }

    public void setSubLife(List<AppCategoryBean> subLife) {
        this.subLife = subLife;
    }

    @Override
    public String toString() {
        return "AppCategoryLifeTypeBean{" +
                "app_id=" + app_id +
                ", app_name='" + app_name + '\'' +
                ", app_icon='" + app_icon + '\'' +
                ", subLife=" + subLife +
                '}';
    }
}
