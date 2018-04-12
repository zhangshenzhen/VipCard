package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ListDetailTypeOneSingleBean {

    /*"orderprice": 20,
                "orderCount": 1,
                "detailName": "牛肉水饺",
                "detailType": 1*/
    private String detailName;
    private String orderCount;
    private String detailType;//1 单独商品  2套餐
    private String orderprice;

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }


    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public String getOrderprice() {
        return orderprice;
    }

    public void setOrderprice(String orderprice) {
        this.orderprice = orderprice;
    }
}