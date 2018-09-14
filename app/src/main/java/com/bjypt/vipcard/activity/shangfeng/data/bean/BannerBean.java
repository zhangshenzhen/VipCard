package com.bjypt.vipcard.activity.shangfeng.data.bean;

import java.io.Serializable;

/**
 * banner Bean
 */
public class BannerBean implements Serializable {

    private int app_id;
    private String app_name;
    /**
     * 图片url
     */
    private String app_icon;

    private int mtlevel;

    private String city_code;
    /**
     * 是否需要登录
     * 0：不需要
     * 1：需要
     */
    private int isentry;
    /**
     * 0:不可点击
     * 1:H5地址
     * 2:原生
     */
    private int link_type;
    /**
     * android原生地址
     */
    private String android_native_url;

    private String native_params;

    private String link_url;

    public BannerBean(){}

    public BannerBean(int app_id, String app_name, String app_icon, int mtlevel, String city_code, int isentry, int link_type, String android_native_url, String native_params, String link_url) {
        this.app_id = app_id;
        this.app_name = app_name;
        this.app_icon = app_icon;
        this.mtlevel = mtlevel;
        this.city_code = city_code;
        this.isentry = isentry;
        this.link_type = link_type;
        this.android_native_url = android_native_url;
        this.native_params = native_params;
        this.link_url = link_url;
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
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

    public int getMtlevel() {
        return mtlevel;
    }

    public void setMtlevel(int mtlevel) {
        this.mtlevel = mtlevel;
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

    public String getAndroid_native_url() {
        return android_native_url;
    }

    public void setAndroid_native_url(String android_native_url) {
        this.android_native_url = android_native_url;
    }

    public String getNative_params() {
        return native_params;
    }

    public void setNative_params(String native_params) {
        this.native_params = native_params;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    @Override
    public String toString() {
        return "BannerBean{" +
                "app_id=" + app_id +
                ", app_name='" + app_name + '\'' +
                ", app_icon='" + app_icon + '\'' +
                ", mtlevel=" + mtlevel +
                ", city_code='" + city_code + '\'' +
                ", isentry=" + isentry +
                ", link_type=" + link_type +
                ", android_native_url='" + android_native_url + '\'' +
                ", native_params='" + native_params + '\'' +
                ", link_url='" + link_url + '\'' +
                '}';
    }
}
