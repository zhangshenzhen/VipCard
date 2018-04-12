package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/8.
 */
public class MerchantCategoryOneBeam {
    private String addressurl;

    public String getAddressurl() {
        return addressurl;
    }

    public void setAddressurl(String addressurl) {
        this.addressurl = addressurl;
    }

    private String pkmertype;

    private String mtname;

    private int mtlevel;

    private String parentpk;

    private String type;
    private String logourl;

    private int merchantCount;

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

    public String getLogourl() {
        return this.logourl;
    }

    public void setPkmertype(String pkmertype) {
        this.pkmertype = pkmertype;
    }

    public String getPkmertype() {
        return this.pkmertype;
    }

    public void setMtname(String mtname) {
        this.mtname = mtname;
    }

    public String getMtname() {
        return this.mtname;
    }

    public void setMtlevel(int mtlevel) {
        this.mtlevel = mtlevel;
    }

    public int getMtlevel() {
        return this.mtlevel;
    }

    public void setParentpk(String parentpk) {
        this.parentpk = parentpk;
    }

    public String getParentpk() {
        return this.parentpk;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setMerchantCount(int merchantCount) {
        this.merchantCount = merchantCount;
    }

    public int getMerchantCount() {
        return this.merchantCount;
    }


}
