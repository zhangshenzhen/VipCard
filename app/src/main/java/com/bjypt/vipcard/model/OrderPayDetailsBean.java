package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/22
 * Use by
 */
public class OrderPayDetailsBean {
//    detailName 详细名称
//    ) orderCount 数量
//    ) orderDetailTotalprice 详细总价
    private String detailName;
    private String orderCount;
    private String orderDetailTotalprice;

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

    public String getOrderDetailTotalprice() {
        return orderDetailTotalprice;
    }

    public void setOrderDetailTotalprice(String orderDetailTotalprice) {
        this.orderDetailTotalprice = orderDetailTotalprice;
    }
}
