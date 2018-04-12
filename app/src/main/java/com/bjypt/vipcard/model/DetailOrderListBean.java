package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/29.
 */
public class DetailOrderListBean {
    private String productPrice;

    private String productName;

    private String unit;
    private String pktypeid;

    private String ispackage;

    public String getPktypeid() {
        return pktypeid;
    }

    public void setPktypeid(String pktypeid) {
        this.pktypeid = pktypeid;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIspackage() {
        return ispackage;
    }

    public void setIspackage(String ispackage) {
        this.ispackage = ispackage;
    }
}
