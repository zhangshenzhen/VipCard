package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class RightAwayBean {
//    pkregister用户主键
//    pkmuser 商家Id
//    amount 总金额
//    balance_sys 用户在平台余额
//    balance_mer 用户在商家余额
//    pkWeal 优惠券主键
//    couponPay 优惠券金额
//    desc_coupon 优惠券描述
//    redPacket 红包金额
//    virtualMoney  积分金额
//    saveMoney 已节省金额
//    waitMoney 待付金额

    private String pkregister;
    private String pkmuser;
    private String amount;
    private String balance_sys;
    private String balance_mer;
    private String pkWeal;
    private String couponPay;
    private String desc_coupon;
    private String redPacket;
    private String virtualMoney;
    private String saveMoney;
    private String waitMoney ;

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    private String cardnum;
    private String pksystem;
    private String paytype;
    private String onlineDiscount;     //在线支付优惠信息
    private String balanceDiscount;    //	平台余额支付 优惠信息
    private String merchantDiscount;   //商家余额支付 优惠信息
    private String isSystemRecharge;   //有无系统充值送
    private String cardDiscount;       //卡余额支付优惠

    public String getCardDiscount() {
        return cardDiscount;
    }

    public void setCardDiscount(String cardDiscount) {
        this.cardDiscount = cardDiscount;
    }

    public String getAreaCardBalance() {
        return areaCardBalance;
    }

    public void setAreaCardBalance(String areaCardBalance) {
        this.areaCardBalance = areaCardBalance;
    }

    private String areaCardBalance;   //有无系统充值送

    public String getIsSystemRecharge() {
        return isSystemRecharge;
    }

    public void setIsSystemRecharge(String isSystemRecharge) {
        this.isSystemRecharge = isSystemRecharge;
    }

    public String getMerchantDiscount() {
        return merchantDiscount;
    }

    public void setMerchantDiscount(String merchantDiscount) {
        this.merchantDiscount = merchantDiscount;
    }

    public String getOnlineDiscount() {
        return onlineDiscount;
    }

    public void setOnlineDiscount(String onlineDiscount) {
        this.onlineDiscount = onlineDiscount;
    }

    public String getBalanceDiscount() {
        return balanceDiscount;
    }

    public void setBalanceDiscount(String balanceDiscount) {
        this.balanceDiscount = balanceDiscount;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getPksystem() {
        return pksystem;
    }

    public void setPksystem(String pksystem) {
        this.pksystem = pksystem;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance_sys() {
        return balance_sys;
    }

    public void setBalance_sys(String balance_sys) {
        this.balance_sys = balance_sys;
    }

    public String getBalance_mer() {
        return balance_mer;
    }

    public void setBalance_mer(String balance_mer) {
        this.balance_mer = balance_mer;
    }

    public String getPkWeal() {
        return pkWeal;
    }

    public void setPkWeal(String pkWeal) {
        this.pkWeal = pkWeal;
    }

    public String getCouponPay() {
        return couponPay;
    }

    public void setCouponPay(String couponPay) {
        this.couponPay = couponPay;
    }

    public String getDesc_coupon() {
        return desc_coupon;
    }

    public void setDesc_coupon(String desc_coupon) {
        this.desc_coupon = desc_coupon;
    }

    public String getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(String redPacket) {
        this.redPacket = redPacket;
    }

    public String getVirtualMoney() {
        return virtualMoney;
    }

    public void setVirtualMoney(String virtualMoney) {
        this.virtualMoney = virtualMoney;
    }

    public String getSaveMoney() {
        return saveMoney;
    }

    public void setSaveMoney(String saveMoney) {
        this.saveMoney = saveMoney;
    }

    public String getWaitMoney() {
        return waitMoney;
    }

    public void setWaitMoney(String waitMoney) {
        this.waitMoney = waitMoney;
    }
}
