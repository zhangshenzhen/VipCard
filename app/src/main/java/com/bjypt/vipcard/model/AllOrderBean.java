package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/18.
 */
public class AllOrderBean {

    private String userRemark;
    private String preorderId;

    private String pkmuser;

    private double totalPrice;

    private double product_tolprice;

    private int earnestmoney;

    private int status;

    private String orderPhone;

    private String orderNo;

    private int payStatus;

    private int orderCount;

    private int isdefault;

    private double discount;

    private String muname;

    private String logo;
    private String disableTime;
    private String createtime;
    private String pksubscbptn;

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

    public String getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(String disableTime) {
        this.disableTime = disableTime;
    }

    public void setPreorderId(String preorderId){
        this.preorderId = preorderId;
    }
    public String getPreorderId(){
        return this.preorderId;
    }
    public void setPkmuser(String pkmuser){
        this.pkmuser = pkmuser;
    }
    public String getPkmuser(){
        return this.pkmuser;
    }
    public void setTotalPrice(double totalPrice){
        this.totalPrice = totalPrice;
    }
    public double getTotalPrice(){
        return this.totalPrice;
    }
    public void setProduct_tolprice(double product_tolprice){
        this.product_tolprice = product_tolprice;
    }
    public double getProduct_tolprice(){
        return this.product_tolprice;
    }

    public int getEarnestmoney() {
        return earnestmoney;
    }

    public void setEarnestmoney(int earnestmoney) {
        this.earnestmoney = earnestmoney;
    }

    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setOrderPhone(String orderPhone){
        this.orderPhone = orderPhone;
    }
    public String getOrderPhone(){
        return this.orderPhone;
    }
    public void setOrderNo(String orderNo){
        this.orderNo = orderNo;
    }
    public String getOrderNo(){
        return this.orderNo;
    }
    public void setPayStatus(int payStatus){
        this.payStatus = payStatus;
    }
    public int getPayStatus(){
        return this.payStatus;
    }
    public void setOrderCount(int orderCount){
        this.orderCount = orderCount;
    }
    public int getOrderCount(){
        return this.orderCount;
    }
    public void setIsdefault(int isdefault){
        this.isdefault = isdefault;
    }
    public int getIsdefault(){
        return this.isdefault;
    }
    public void setDiscount(double discount){
        this.discount = discount;
    }
    public double getDiscount(){
        return this.discount;
    }
    public void setMuname(String muname){
        this.muname = muname;
    }
    public String getMuname(){
        return this.muname;
    }
    public void setLogo(String logo){
        this.logo = logo;
    }
    public String getLogo(){
        return this.logo;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }
}
