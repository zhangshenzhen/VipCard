package com.bjypt.vipcard.model;

import java.io.Serializable;
import java.util.List;

/**
 * 套餐bean类
 * name 菜名
 * fen 几份
 * price 价格
 * Created by Administrator on 2016/3/31.
 */
public class TaoCanBean implements Serializable {
    private String name;
    private String fen;
    private String price;
    private List<TaoCanBean> list;

    public List<TaoCanBean> getList() {
        return list;
    }

    public void setList(List<TaoCanBean> list) {
        this.list = list;
    }

    public TaoCanBean(String name, String fen, String price) {
        this.name = name;
        this.fen = fen;
        this.price = price;
        this.list = list;
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
