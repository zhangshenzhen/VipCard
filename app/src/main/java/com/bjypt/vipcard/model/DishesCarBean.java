package com.bjypt.vipcard.model;

import java.io.Serializable;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/9
 * Use by 菜品购物车模型类
 */
public class DishesCarBean  implements Serializable{
    private String pkId;//菜品主键
    private String dishesName;//名字
    private double dishesPrice;//单价
    private double dishesPayMent;//定金
    private String type;//规格
    private String speciType;//规格
    private String productSpeciId;//规格Id
    private int dishesNum;//数量
    private String isPackage;//是否是套餐
    public boolean isClick = false;

    /**
     *
     * @param pkId
     * @param dishesName
     * @param dishesPrice
     * @param dishesPayMent
     * @param type
     * @param speciType
     * @param productSpeciId
     * @param dishesNum
     * @param isPackage
     */
    public DishesCarBean(String pkId, String dishesName, double dishesPrice, double dishesPayMent, String type, String speciType, String productSpeciId, int dishesNum, String isPackage) {
        this.pkId = pkId;
        this.dishesName = dishesName;
        this.dishesPrice = dishesPrice;
        this.dishesPayMent = dishesPayMent;
        this.type = type;
        this.speciType = speciType;
        this.productSpeciId = productSpeciId;
        this.dishesNum = dishesNum;
        this.isPackage = isPackage;
    }

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public String getDishesName() {
        return dishesName;
    }

    public void setDishesName(String dishesName) {
        this.dishesName = dishesName;
    }

    public double getDishesPrice() {
        return dishesPrice;
    }

    public void setDishesPrice(double dishesPrice) {
        this.dishesPrice = dishesPrice;
    }

    public double getDishesPayMent() {
        return dishesPayMent;
    }

    public void setDishesPayMent(double dishesPayMent) {
        this.dishesPayMent = dishesPayMent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpeciType() {
        return speciType;
    }

    public void setSpeciType(String speciType) {
        this.speciType = speciType;
    }

    public String getProductSpeciId() {
        return productSpeciId;
    }

    public void setProductSpeciId(String productSpeciId) {
        this.productSpeciId = productSpeciId;
    }

    public int getDishesNum() {
        return dishesNum;
    }

    public void setDishesNum(int dishesNum) {
        this.dishesNum = dishesNum;
    }

    public String getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(String isPackage) {
        this.isPackage = isPackage;
    }


}
