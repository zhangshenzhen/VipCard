package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/27.
 */
public class BannerBean {

    /*//    "resultStatus": 0,
//            "msg": "SUCCESS",
//            "resultData": [
//    {
//        "slogan": "系统广告",
//            "adurl": "SysAd1454461506932.jpg"
//    },
//    {
//        "slogan": "系统广告",
//            "adurl": "SysAd1454461486790.jpg"
//    },
//    {
//        "slogan": "系统广告",
//            "adurl": "SysAd1454461455743.jpg"
//    }
//    ]*/
//    private String slogan;
    private String adurl;

    private String linkurl;
    private String addesc;

    private String nativeurl;

    public String getNativeurl() {
        return nativeurl;
    }

    public void setNativeurl(String nativeurl) {
        this.nativeurl = nativeurl;
    }

    public String getAddesc() {
        return addesc;
    }

    public void setAddesc(String addesc) {
        this.addesc = addesc;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

//    public String getSlogan() {
//        return slogan;
//    }
//
//    public void setSlogan(String slogan) {
//        this.slogan = slogan;
//    }

    public String getAdurl() {
        return adurl;
    }

    public void setAdurl(String adurl) {
        this.adurl = adurl;
    }
}
