package com.bjypt.vipcard.model;

import java.util.List;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/12
 * Use by
 */
public class SaveMoneyBean {

    private String pkmuser;
    private String pkregister;
    private List<ShoppingDetailList> shoppingDetailList;

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    public String getPkregister() {
        return pkregister;
    }

    public void setPkregister(String pkregister) {
        this.pkregister = pkregister;
    }

    public List<ShoppingDetailList> getShoppingDetailList() {
        return shoppingDetailList;
    }

    public void setShoppingDetailList(List<ShoppingDetailList> shoppingDetailList) {
        this.shoppingDetailList = shoppingDetailList;
    }
}
