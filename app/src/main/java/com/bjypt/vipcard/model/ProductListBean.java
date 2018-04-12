package com.bjypt.vipcard.model;

import java.io.Serializable;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/7
 * Use by商家详情菜品分类模型
 */
public class ProductListBean implements Serializable{
    private String unit;//单位
    private String earnestMoney;//商品价格
    private String monthSelledNum;//月售
    private String productImgUrl;//菜品Url
    private String productName;//菜品名称
    private String pkproduct;//菜品主键
    private String pktypeid;
    private String productPrice;
    private String ispackage;//是否是套餐
    private String judgeAllNums;
    private String discount;
    private String isSpecGoods;//是否存在规格
    private String productdesc;//每个单品的描述
    public boolean isBoolean = false;
//    1.orginprice：商品原价
//    2. rechargeprice：商品充值价
//    3.platformprice：商品平台价
    private String orginprice;
    private String rechargeprice;
    private String platformprice;

    private int number;  //保存商品数量
    private String Money; //保存价格
    private String typename; //所属分类名称

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getProductdesc() {
        return productdesc;
    }

    public void setProductdesc(String productdesc) {
        this.productdesc = productdesc;
    }

    public String getOrginprice() {
        return orginprice;
    }

    public void setOrginprice(String orginprice) {
        this.orginprice = orginprice;
    }

    public String getRechargeprice() {
        return rechargeprice;
    }

    public void setRechargeprice(String rechargeprice) {
        this.rechargeprice = rechargeprice;
    }

    public String getPlatformprice() {
        return platformprice;
    }

    public void setPlatformprice(String platformprice) {
        this.platformprice = platformprice;
    }

    public String getIsSpecGoods() {
        return isSpecGoods;
    }

    public void setIsSpecGoods(String isSpecGoods) {
        this.isSpecGoods = isSpecGoods;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getJudgeAllNums() {
        return judgeAllNums;
    }

    public void setJudgeAllNums(String judgeAllNums) {
        this.judgeAllNums = judgeAllNums;
    }

    public String getIspackage() {
        return ispackage;
    }

    public void setIspackage(String ispackage) {
        this.ispackage = ispackage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getPktypeid() {
        return pktypeid;
    }

    public void setPktypeid(String pktypeid) {
        this.pktypeid = pktypeid;
    }

    public String getPkproduct() {
        return pkproduct;
    }

    public void setPkproduct(String pkproduct) {
        this.pkproduct = pkproduct;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEarnestMoney() {
        return earnestMoney;
    }

    public void setEarnestMoney(String earnestMoney) {
        this.earnestMoney = earnestMoney;
    }

    public String getMonthSelledNum() {
        return monthSelledNum;
    }

    public void setMonthSelledNum(String monthSelledNum) {
        this.monthSelledNum = monthSelledNum;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
