package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/3/31.
 * 此类为未支付的订单Model
 */
public class UnPayOrderBean {

    private String userRemark;
    private String goodsName;
    private String goodsImg;
    private int goodsNum;
    private double goodsPrice;
    private String preorderId;
    private int status;
    private String pkmuser;
    private String orderNo;
    private String time;
    private String createtime;
    private String pksubscbptn;
    private int earnestmoney;

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    public int getEarnestmoney() {
        return earnestmoney;
    }

    public void setEarnestmoney(int earnestmoney) {
        this.earnestmoney = earnestmoney;
    }

    public String getPksubscbptn() {
        return pksubscbptn;
    }

    public void setPksubscbptn(String pksubscbptn) {
        this.pksubscbptn = pksubscbptn;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPreorderId() {
        return preorderId;
    }

    public void setPreorderId(String preorderId) {
        this.preorderId = preorderId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }
}
