package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/20
 * Use by
 */
public class OrderContentData {
    //    productId:单品或套餐的主键
//    detailName：单品或套餐名称
//    detailType：(1:单品 ； 2:套餐)
//    orderCount：预订数量
    private String productId;
    private String detailName;
    private int detailType;
    private int orderCount;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public int getDetailType() {
        return detailType;
    }

    public void setDetailType(int detailType) {
        this.detailType = detailType;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
