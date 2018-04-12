package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2017/2/10 0010.
 */

public class CouponMap {
    private String payamount;
    private String pkcoupon;
    private String remark;
    private String valueamount;
    private String pkmerchantcoupon;

    public String getPkmerchantcoupon() {
        return pkmerchantcoupon;
    }

    public void setPkmerchantcoupon(String pkmerchantcoupon) {
        this.pkmerchantcoupon = pkmerchantcoupon;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }

    public String getPkcoupon() {
        return pkcoupon;
    }

    public void setPkcoupon(String pkcoupon) {
        this.pkcoupon = pkcoupon;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getValueamount() {
        return valueamount;
    }

    public void setValueamount(String valueamount) {
        this.valueamount = valueamount;
    }
}
