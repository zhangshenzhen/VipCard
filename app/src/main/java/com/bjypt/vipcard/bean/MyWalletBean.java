package com.bjypt.vipcard.bean;

/**
 * 我的钱包
 */
public class MyWalletBean {

    private String app_id;
    // 名称
    private String app_name;
    // 图片
    private String app_icon;

    // 是否需要登录  0:不需要 1:需要
    private int isentry;

    // H5地址
    private String link_url;
    // H5地址是否可以点击
    private int link_type;

    // 本地跳转地址
    private String android_native_url;

    // 城市 code
    private String city_code;

    private String native_params;

    public MyWalletBean() {

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

    public int getLink_type() {
        return link_type;
    }

    public void setLink_type(int link_type) {
        this.link_type = link_type;
    }

    public int getIsentry() {
        return isentry;
    }

    public void setIsentry(int isentry) {
        this.isentry = isentry;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getNative_params() {
        return native_params;
    }

    public void setNative_params(String native_params) {
        this.native_params = native_params;
    }

    public String getAndroid_native_url() {
        return android_native_url;
    }

    public void setAndroid_native_url(String android_native_url) {
        this.android_native_url = android_native_url;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }
}
