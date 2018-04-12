package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/13.
 */
public class DetailDeshesResultBean {
    private String pkproduct;
    private String pkspecid;

    private String specCode;

    private String specName;

    private int price;

    private int vipprice;

    private String remark;

    public String getPkproduct() {
        return pkproduct;
    }

    public void setPkproduct(String pkproduct) {
        this.pkproduct = pkproduct;
    }

    public String getPkspecid() {
        return pkspecid;
    }

    public void setPkspecid(String pkspecid) {
        this.pkspecid = pkspecid;
    }

    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getVipprice() {
        return vipprice;
    }

    public void setVipprice(int vipprice) {
        this.vipprice = vipprice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
