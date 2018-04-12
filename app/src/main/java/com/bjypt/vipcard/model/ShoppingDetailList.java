package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/12
 * Use by
 */
public class ShoppingDetailList {

    private String pkproduct;
    private String productPrice;
    private String productCount;
    private String earnestMoney;

    public String getEarnestMoney() {
        return earnestMoney;
    }

    public void setEarnestMoney(String earnestMoney) {
        this.earnestMoney = earnestMoney;
    }

    public String getPkproduct() {
        return pkproduct;
    }

    public void setPkproduct(String pkproduct) {
        this.pkproduct = pkproduct;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

}
