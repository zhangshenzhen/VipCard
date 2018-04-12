package com.bjypt.vipcard.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/28.
 */
public class PayOneBean implements Serializable{
    private String name;
    private String fen;
    private String price;

    public PayOneBean(String name, String fen, String price) {
        this.name = name;
        this.fen = fen;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
