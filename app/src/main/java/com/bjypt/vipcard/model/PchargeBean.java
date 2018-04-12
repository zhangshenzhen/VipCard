package com.bjypt.vipcard.model;

/**
 * Created by User on 2016/6/7.
 */
public class PchargeBean {

    private int price;
    private Double allprice;

    public PchargeBean(int price, Double allprice){
        this.allprice=allprice;
        this.price=price;
    }
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Double getAllprice() {
        return allprice;
    }

    public void setAllprice(Double allprice) {
        this.allprice = allprice;
    }
}
