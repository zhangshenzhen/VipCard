package com.bjypt.vipcard.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 */
public class DetailOrderBean implements Serializable{
    private String pkpropre;

    private String pkmuser;

    private String muname;

    private String logo;

    private String productTolprice;

    private String discount;

    private String status;

    private String address;

    private String phone;

    private String longitude;

    private String latitude;

    private List<DetailOrderListBean> hybProductList ;

    private String orderNo;

    private String orderCount;

    private String productTolprices;

    private String discountNum;
    private String orderTime;
    private String refundreason;
    private String desc_payment_return;

    public String getDesc_payment_return() {
        return desc_payment_return;
    }

    public void setDesc_payment_return(String desc_payment_return) {
        this.desc_payment_return = desc_payment_return;
    }

    public String getRefundreason() {
        return refundreason;
    }

    public void setRefundreason(String refundreason) {
        this.refundreason = refundreason;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getPkpropre() {
        return pkpropre;
    }

    public void setPkpropre(String pkpropre) {
        this.pkpropre = pkpropre;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getProductTolprice() {
        return productTolprice;
    }

    public void setProductTolprice(String productTolprice) {
        this.productTolprice = productTolprice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public List<DetailOrderListBean> getHybProductList() {
        return hybProductList;
    }

    public void setHybProductList(List<DetailOrderListBean> hybProductList) {
        this.hybProductList = hybProductList;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getProductTolprices() {
        return productTolprices;
    }

    public void setProductTolprices(String productTolprices) {
        this.productTolprices = productTolprices;
    }

    public String getDiscountNum() {
        return discountNum;
    }

    public void setDiscountNum(String discountNum) {
        this.discountNum = discountNum;
    }
}
