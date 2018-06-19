package com.bjypt.vipcard.activity.shangfeng.data.bean;

import java.math.BigDecimal;

/**
 * Created by wanglei on 2018/5/16.
 */

public class ScanQcodeDiscountBean {


    private String pkregister;
    private String pkmuser;
    private String muname;
    private Integer pay_type;
    private Integer order_type;
    private BigDecimal amount;
    private BigDecimal real_pay_amount;
    private BigDecimal offline_pay_amount;
    private String coupons;
    private String order_title;
    private BigDecimal discount_amount;
    private String discount_rate;

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }

    public String getPkregister() {
        return pkregister;
    }

    public void setPkregister(String pkregister) {
        this.pkregister = pkregister;
    }

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    public Object getPay_type() {
        return pay_type;
    }

    public void setPay_type(Integer pay_type) {
        this.pay_type = pay_type;
    }

    public Integer getOrder_type() {
        return order_type;
    }

    public void setOrder_type(Integer order_type) {
        this.order_type = order_type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getReal_pay_amount() {
        return real_pay_amount;
    }

    public void setReal_pay_amount(BigDecimal real_pay_amount) {
        this.real_pay_amount = real_pay_amount;
    }

    public BigDecimal getOffline_pay_amount() {
        return offline_pay_amount;
    }

    public void setOffline_pay_amount(BigDecimal offline_pay_amount) {
        this.offline_pay_amount = offline_pay_amount;
    }

    public String getCoupons() {
        return coupons;
    }

    public void setCoupons(String coupons) {
        this.coupons = coupons;
    }

    public String getOrder_title() {
        return order_title;
    }

    public void setOrder_title(String order_title) {
        this.order_title = order_title;
    }

    public BigDecimal getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(BigDecimal discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getMuname() {
        return muname;
    }

    public void setMuname(String muname) {
        this.muname = muname;
    }
}
