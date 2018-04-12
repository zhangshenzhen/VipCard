package com.bjypt.vipcard.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/20.
 */

public class MenuModel implements Serializable{
    private String typename;
    private List<ProductListBean> menuProductions;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public List<ProductListBean> getMenuProductions() {
        return menuProductions;
    }

    public void setMenuProductions(List<ProductListBean> menuProductions) {
        this.menuProductions = menuProductions;
    }

    @Override
    public String toString() {
        return "MenuModel{" +
                "typename='" + typename + '\'' +
                ", menuProductions=" + menuProductions +
                '}';
    }
}
