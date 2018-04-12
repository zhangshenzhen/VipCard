package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ListDetailTypeOneListBean {
    /* "payment": 9,
        "ordertotalprice": 20,
        "discount": 10,
        "earnestmoney": 1,
        "couponpay": 0,
        "wealpay": 0,
        "waitmoney": 19,
        "subsidies_system": 0,
        "payment_sub": "9",
        "goodsDetailList": [
            {
                "orderprice": 20,
                "orderCount": 1,
                "detailName": "牛肉水饺",
                "detailType": 1
            }
        ]*/
    private String payment;
    private String ordertotalprice;
    private String discount;
    private String earnestmoney;
    private String couponpay;
    private String wealpay;
    private String waitmoney;
    private String subsidies_system;
    private String payment_sub;
    private String favourable_merchant;

    public String getFavourable_merchant() {
        return favourable_merchant;
    }

    public void setFavourable_merchant(String favourable_merchant) {
        this.favourable_merchant = favourable_merchant;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getEarnestmoney() {
        return earnestmoney;
    }

    public void setEarnestmoney(String earnestmoney) {
        this.earnestmoney = earnestmoney;
    }

    public String getCouponpay() {
        return couponpay;
    }

    public void setCouponpay(String couponpay) {
        this.couponpay = couponpay;
    }

    public String getWealpay() {
        return wealpay;
    }

    public void setWealpay(String wealpay) {
        this.wealpay = wealpay;
    }

    public String getWaitmoney() {
        return waitmoney;
    }

    public void setWaitmoney(String waitmoney) {
        this.waitmoney = waitmoney;
    }

    public String getSubsidies_system() {
        return subsidies_system;
    }

    public void setSubsidies_system(String subsidies_system) {
        this.subsidies_system = subsidies_system;
    }

    public String getPayment_sub() {
        return payment_sub;
    }

    public void setPayment_sub(String payment_sub) {
        this.payment_sub = payment_sub;
    }

    public String getOrdertotalprice() {
        return ordertotalprice;
    }

    public void setOrdertotalprice(String ordertotalprice) {
        this.ordertotalprice = ordertotalprice;
    }
    private List<ListDetailTypeOneSingleBean> goodsDetailList;

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public List<ListDetailTypeOneSingleBean> getGoodsDetailList() {
        return goodsDetailList;
    }

    public void setGoodsDetailList(List<ListDetailTypeOneSingleBean> goodsDetailList) {
        this.goodsDetailList = goodsDetailList;
    }
}
