package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/7/1
 * Use by
 */
public class WxpayBean {

//    "appid":"wx958a56f9cacfa0b4",
//     "partnerid":"1361194602",
//      "prepayid":"wx20160701100347fb002a38fb0310591884",
//      "package_app":"Sign=WXPay",
//       "noncestr":"1003455218"
//        "timestamp":"1467338627",
//     "sign":"D5F939D7F842C74CF9E65FC45267C866"
    private String appid;
    private String partnerid;
    private String prepayid;
    private String package_app;
    private String noncestr;
    private String timestamp;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackage_app() {
        return package_app;
    }

    public void setPackage_app(String package_app) {
        this.package_app = package_app;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
