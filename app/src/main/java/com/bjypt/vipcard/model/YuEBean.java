package com.bjypt.vipcard.model;

/**
 * Created by liyunte on 2017/1/9.
 */

public class YuEBean {
    private String pkproduct;

    private String  productday;

    private Productexpiredate productexpiredate;

    private String productmemo;

    private String productname;

    private String productrate;

    private String productstatus;

    private String producttotalmoney;
    private String diffdate;

    public String getDiffdate() {
        return diffdate;
    }

    public void setDiffdate(String diffdate) {
        this.diffdate = diffdate;
    }

    public String getPkproduct() {
        return pkproduct;
    }

    public void setPkproduct(String pkproduct) {
        this.pkproduct = pkproduct;
    }

    public String getProductday() {
        return productday;
    }

    public void setProductday(String productday) {
        this.productday = productday;
    }

    public Productexpiredate getProductexpiredate() {
        return productexpiredate;
    }

    public void setProductexpiredate(Productexpiredate productexpiredate) {
        this.productexpiredate = productexpiredate;
    }

    public String getProductmemo() {
        return productmemo;
    }

    public void setProductmemo(String productmemo) {
        this.productmemo = productmemo;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductrate() {
        return productrate;
    }

    public void setProductrate(String productrate) {
        this.productrate = productrate;
    }

    public String getProductstatus() {
        return productstatus;
    }

    public void setProductstatus(String productstatus) {
        this.productstatus = productstatus;
    }

    public String getProducttotalmoney() {
        return producttotalmoney;
    }

    public void setProducttotalmoney(String producttotalmoney) {
        this.producttotalmoney = producttotalmoney;
    }
}
