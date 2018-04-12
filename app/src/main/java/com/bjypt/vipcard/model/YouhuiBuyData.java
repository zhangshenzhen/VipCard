package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class YouhuiBuyData {
    private String pkcoupon;
    private String payamount;
    private String maxpayamount;
    private String valueamount;
    private String startdate;
    private String enddate;
    private String balance;
    private String paytype;
    private String pkmerchantcoupon;
    private String actualPayment;
    private String policystatus;
    private String policycontent;

    public String getPkmerchantcoupon() {
        return pkmerchantcoupon;
    }

    public void setPkmerchantcoupon(String pkmerchantcoupon) {
        this.pkmerchantcoupon = pkmerchantcoupon;
    }

    public String getActualPayment() {
        return actualPayment;
    }

    public void setActualPayment(String actualPayment) {
        this.actualPayment = actualPayment;
    }

    public String getPolicystatus() {
        return policystatus;
    }

    public void setPolicystatus(String policystatus) {
        this.policystatus = policystatus;
    }

    public String getPolicycontent() {
        return policycontent;
    }

    public void setPolicycontent(String policycontent) {
        this.policycontent = policycontent;
    }

    public String getPkcoupon() {
        return pkcoupon;
    }

    public void setPkcoupon(String pkcoupon) {
        this.pkcoupon = pkcoupon;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }

    public String getMaxpayamount() {
        return maxpayamount;
    }

    public void setMaxpayamount(String maxpayamount) {
        this.maxpayamount = maxpayamount;
    }

    public String getValueamount() {
        return valueamount;
    }

    public void setValueamount(String valueamount) {
        this.valueamount = valueamount;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }
}
