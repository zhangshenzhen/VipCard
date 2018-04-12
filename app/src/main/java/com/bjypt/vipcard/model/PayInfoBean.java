package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/22
 * Use by 支付前信息
 */
public class PayInfoBean {
//    1) pkregister用户主键
//    2) pkmuser 商家Id
//    ) muname商家名称
//    ) pkpayid 支付主键
//    ) payEntrance 支付入口
//    ) amount 订单总额
//    ) earnestMoney 定金
//    ) waitMoney 待付金额【待付定金、待付消费金额】
//            // 定金和消费时
//            ) grade 会员等级
//    ) discount 会员折扣
//// 消费时
//    ) pkWeal 优惠券主键
//    ) couponPay 优惠券金额
//    ) redPacket 红包金额
//    ) point 积分
//    ) pointMoney 积分金额
//    ) saveMoney 以节省金额
//    ) sysBalance 平台余额
//    ) merBalance 商家余额
    private String pkregister;
    private String pkmuser;
    private String muname;
    private String pkpayid;
    private String payEntrance;
    private String amount;
    private String earnestMoney;
    private String waitMoney;
    private String grade;
    private String discount;
    private String pkWeal;
    private String couponPay;
    private String redPacket;
    private String point;
    private String pointMoney;
    private String saveMoney;
    private String sysBalance;
    private String merBalance;
    private String virtualMoney;
    private List<OrderPayDetailsBean> orderPayDetailList;
    private String pkWeal_merchant; // 优惠券主键:商家余额
    private String couponPay_merchant; // 优惠券金额:商家余额
    private String redPacket_merchant; // 红包支付:商家余额
    private String point_merchant; // 积分:商家余额
    private String pointMoney_merchant; // 积分金额:商家余额
    private String virtualMoney_merchant; // 使用惠员币(即用户在平台虚拟余额)立减金额:商家余额
    private String saveMoney_merchant; // 已节省金额:商家余额
    private String waitMoney_merchant; // 待付金额:商家余额
    private String favourable_merchant; // 商家充值价与原价相比,优惠金额

    private String pkWeal_platform; // 优惠券主键:系统余额
    private String couponPay_platform; // 优惠券金额:系统余额
    private String redPacket_platform; // 红包支付:系统余额
    private String point_platform; // 积分:系统余额
    private String pointMoney_platform; // 积分金额:系统余额
    private String virtualMoney_platform; // 使用惠员币(即用户在平台虚拟余额)立减金额:系统余额
    private String saveMoney_platform; // 已节省金额:系统余额
    private String waitMoney_platform; // 待付金额:系统余额
    private String favourable_platform; // 平台价与原价相比，优惠金额
    private String pksystem;//系统主键

    public String getPksystem() {
        return pksystem;
    }

    public void setPksystem(String pksystem) {
        this.pksystem = pksystem;
    }

    public String getPkWeal_merchant() {
        return pkWeal_merchant;
    }

    public void setPkWeal_merchant(String pkWeal_merchant) {
        this.pkWeal_merchant = pkWeal_merchant;
    }

    public String getCouponPay_merchant() {
        return couponPay_merchant;
    }

    public void setCouponPay_merchant(String couponPay_merchant) {
        this.couponPay_merchant = couponPay_merchant;
    }

    public String getRedPacket_merchant() {
        return redPacket_merchant;
    }

    public void setRedPacket_merchant(String redPacket_merchant) {
        this.redPacket_merchant = redPacket_merchant;
    }

    public String getPoint_merchant() {
        return point_merchant;
    }

    public void setPoint_merchant(String point_merchant) {
        this.point_merchant = point_merchant;
    }

    public String getPointMoney_merchant() {
        return pointMoney_merchant;
    }

    public void setPointMoney_merchant(String pointMoney_merchant) {
        this.pointMoney_merchant = pointMoney_merchant;
    }

    public String getVirtualMoney_merchant() {
        return virtualMoney_merchant;
    }

    public void setVirtualMoney_merchant(String virtualMoney_merchant) {
        this.virtualMoney_merchant = virtualMoney_merchant;
    }

    public String getSaveMoney_merchant() {
        return saveMoney_merchant;
    }

    public void setSaveMoney_merchant(String saveMoney_merchant) {
        this.saveMoney_merchant = saveMoney_merchant;
    }

    public String getWaitMoney_merchant() {
        return waitMoney_merchant;
    }

    public void setWaitMoney_merchant(String waitMoney_merchant) {
        this.waitMoney_merchant = waitMoney_merchant;
    }

    public String getFavourable_merchant() {
        return favourable_merchant;
    }

    public void setFavourable_merchant(String favourable_merchant) {
        this.favourable_merchant = favourable_merchant;
    }

    public String getPkWeal_platform() {
        return pkWeal_platform;
    }

    public void setPkWeal_platform(String pkWeal_platform) {
        this.pkWeal_platform = pkWeal_platform;
    }

    public String getCouponPay_platform() {
        return couponPay_platform;
    }

    public void setCouponPay_platform(String couponPay_platform) {
        this.couponPay_platform = couponPay_platform;
    }

    public String getRedPacket_platform() {
        return redPacket_platform;
    }

    public void setRedPacket_platform(String redPacket_platform) {
        this.redPacket_platform = redPacket_platform;
    }

    public String getPoint_platform() {
        return point_platform;
    }

    public void setPoint_platform(String point_platform) {
        this.point_platform = point_platform;
    }

    public String getPointMoney_platform() {
        return pointMoney_platform;
    }

    public void setPointMoney_platform(String pointMoney_platform) {
        this.pointMoney_platform = pointMoney_platform;
    }

    public String getVirtualMoney_platform() {
        return virtualMoney_platform;
    }

    public void setVirtualMoney_platform(String virtualMoney_platform) {
        this.virtualMoney_platform = virtualMoney_platform;
    }

    public String getSaveMoney_platform() {
        return saveMoney_platform;
    }

    public void setSaveMoney_platform(String saveMoney_platform) {
        this.saveMoney_platform = saveMoney_platform;
    }

    public String getWaitMoney_platform() {
        return waitMoney_platform;
    }

    public void setWaitMoney_platform(String waitMoney_platform) {
        this.waitMoney_platform = waitMoney_platform;
    }

    public String getFavourable_platform() {
        return favourable_platform;
    }

    public void setFavourable_platform(String favourable_platform) {
        this.favourable_platform = favourable_platform;
    }

    public String getVirtualMoney() {
        return virtualMoney;
    }

    public void setVirtualMoney(String virtualMoney) {
        this.virtualMoney = virtualMoney;
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

    public String getMuname() {
        return muname;
    }

    public void setMuname(String muname) {
        this.muname = muname;
    }

    public String getPkpayid() {
        return pkpayid;
    }

    public void setPkpayid(String pkpayid) {
        this.pkpayid = pkpayid;
    }

    public String getPayEntrance() {
        return payEntrance;
    }

    public void setPayEntrance(String payEntrance) {
        this.payEntrance = payEntrance;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEarnestMoney() {
        return earnestMoney;
    }

    public void setEarnestMoney(String earnestMoney) {
        this.earnestMoney = earnestMoney;
    }

    public String getWaitMoney() {
        return waitMoney;
    }

    public void setWaitMoney(String waitMoney) {
        this.waitMoney = waitMoney;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
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

    public String getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(String redPacket) {
        this.redPacket = redPacket;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPointMoney() {
        return pointMoney;
    }

    public void setPointMoney(String pointMoney) {
        this.pointMoney = pointMoney;
    }

    public String getSaveMoney() {
        return saveMoney;
    }

    public void setSaveMoney(String saveMoney) {
        this.saveMoney = saveMoney;
    }

    public String getSysBalance() {
        return sysBalance;
    }

    public void setSysBalance(String sysBalance) {
        this.sysBalance = sysBalance;
    }

    public String getMerBalance() {
        return merBalance;
    }

    public void setMerBalance(String merBalance) {
        this.merBalance = merBalance;
    }

    public List<OrderPayDetailsBean> getOrderPayDetailList() {
        return orderPayDetailList;
    }

    public void setOrderPayDetailList(List<OrderPayDetailsBean> orderPayDetailList) {
        this.orderPayDetailList = orderPayDetailList;
    }
}
