package com.bjypt.vipcard.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/12
 * Use by
 */
public class PayOrderBean {

    private String userId;
    private String pkmuser;
    private String totalPrice;
    private String totalEarnet;
    private String orderPhone;
    private String userRemark;
    List<SingleGoods> singleGoods = new ArrayList<>();
    List<PackageGoods> packageGoods = new ArrayList<>();

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalEarnet() {
        return totalEarnet;
    }

    public void setTotalEarnet(String totalEarnet) {
        this.totalEarnet = totalEarnet;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public List<SingleGoods> getSingleGoods() {
        return singleGoods;
    }

    public void setSingleGoods(List<SingleGoods> singleGoods) {
        this.singleGoods = singleGoods;
    }

    public List<PackageGoods> getPackageGoods() {
        return packageGoods;
    }

    public void setPackageGoods(List<PackageGoods> packageGoods) {
        this.packageGoods = packageGoods;
    }

}
