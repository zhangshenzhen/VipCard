package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/5
 * Use by 首页的Type
 */
public class HomeTypeBean {
     private String addressurl;
     private String logourl;
     private String mtlevel;
     private String parentpk;
     private String mtname;
     private String pkmertype;
     private String merchantCount;
     private String type;
     private String action_type;

    private String nativeurl;   //安卓原生页面跳转
    private String isentry;     //判断登录

    public String getIsentry() {
        return isentry;
    }

    public void setIsentry(String isentry) {
        this.isentry = isentry;
    }

    public String getNativeurl() {
        return nativeurl;
    }

    public void setNativeurl(String nativeurl) {
        this.nativeurl = nativeurl;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getMerchantCount() {
        return merchantCount;
    }

    public void setMerchantCount(String merchantCount) {
        this.merchantCount = merchantCount;
    }

    public String getParentpk() {
        return parentpk;
    }

    public void setParentpk(String parentpk) {
        this.parentpk = parentpk;
    }

    public void setAddressurl(String addressurl) {
        this.addressurl = addressurl;
    }

    public String getAddressurl() {
        return addressurl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setMtlevel(String mtlevel) {
        this.mtlevel = mtlevel;
    }

    public void setMtname(String mtname) {
        this.mtname = mtname;
    }

    public void setPkmertype(String pkmertype) {
        this.pkmertype = pkmertype;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMtlevel() {
        return mtlevel;
    }

    public String getMtname() {
        return mtname;
    }

    public String getPkmertype() {
        return pkmertype;
    }

    public String getType() {
        return type;
    }
}
