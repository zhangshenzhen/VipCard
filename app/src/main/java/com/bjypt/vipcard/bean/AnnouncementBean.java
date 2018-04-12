package com.bjypt.vipcard.bean;

/**
 * Created by Dell on 2018/4/3.
 */

public class AnnouncementBean {

    private String app_id;
    private String app_name;
    private String app_icon;
    private String mtlevel;
    private String parent_app_id;
    private String city_code;
    private int isentry;
    private int link_type;
    private String link_url;

    public AnnouncementBean() {
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
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

    public String getMtlevel() {
        return mtlevel;
    }

    public void setMtlevel(String mtlevel) {
        this.mtlevel = mtlevel;
    }

    public String getParent_app_id() {
        return parent_app_id;
    }

    public void setParent_app_id(String parent_app_id) {
        this.parent_app_id = parent_app_id;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public int getIsentry() {
        return isentry;
    }

    public void setIsentry(int isentry) {
        this.isentry = isentry;
    }

    public int getLink_type() {
        return link_type;
    }

    public void setLink_type(int link_type) {
        this.link_type = link_type;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }
}
