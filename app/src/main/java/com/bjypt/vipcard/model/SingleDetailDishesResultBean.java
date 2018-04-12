package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/27.
 */
public class SingleDetailDishesResultBean {
    private String vipprice;
    private String remark;
    private String pkproduct;

    public String getPkproduct() {
        return pkproduct;
    }

    public void setPkproduct(String pkproduct) {
        this.pkproduct = pkproduct;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVipprice() {
        return vipprice;
    }

    public void setVipprice(String vipprice) {
        this.vipprice = vipprice;
    }
}
