package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/2/15 0015.
 */

public class RightAwayBeforeBean {
    private String discountType;
    private String discountMsg;
    private String paytype;
    private String couponsMsg;
    private String couponsType;
    private String areaCardBalance;

    public String getAreaCardBalance() {
        return areaCardBalance;
    }

    public void setAreaCardBalance(String areaCardBalance) {
        this.areaCardBalance = areaCardBalance;
    }

    public String getCouponsMsg() {
        return couponsMsg;
    }

    public void setCouponsMsg(String couponsMsg) {
        this.couponsMsg = couponsMsg;
    }

    public String getCouponsType() {
        return couponsType;
    }

    public void setCouponsType(String couponsType) {
        this.couponsType = couponsType;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountMsg() {
        return discountMsg;
    }

    public void setDiscountMsg(String discountMsg) {
        this.discountMsg = discountMsg;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }
}
