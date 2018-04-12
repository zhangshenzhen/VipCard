package com.bjypt.vipcard.model;

/**
 * Created by wanglei on 2017/12/26.
 */

public class AppCategoryBean {

    /**
     * app_id : 1
     * app_name : 提现
     * app_icon : http://huiyuanbao.oss-cn-hangzhou.aliyuncs.com/userposition_27b24e90c80f46978400eff7d0628fb6.jpg
     * mtlevel : 1
     * city_code : 025
     * isentry : 1
     * link_type : 2
     * android_native_url : com.hyb.withdraw
     * ios_native_url : com.hyb.withdraw
     * native_params : {'pkmuser':'skjfskjfwek'}
     */

    public enum  ActionTypeEnum {
        NoAction(0, "不可点击"), H5(1, "H5地址"), Native(2, "原生");
        private int value;
        private String name;

        private ActionTypeEnum(int value, String name){
            this.name = name;
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum  LoginTypeEnum {
        All(0, "不需要"), Login(1, "需要");
        private int value;
        private String name;

        private LoginTypeEnum(int value, String name){
            this.name = name;
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    private int app_id;
    private String app_name;
    private String app_icon;
    private int mtlevel;
    private String city_code;
    private int isentry;//是否需要登录 0:不需要 1:需要
    private int link_type;//0:不可点击 1:H5地址 2:原生
    private String android_native_url;
    private String ios_native_url;
    private String native_params;
    private String link_url;

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

    public String getIos_native_url() {
        return ios_native_url;
    }

    public void setIos_native_url(String ios_native_url) {
        this.ios_native_url = ios_native_url;
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
}
