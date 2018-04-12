package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/6/16
 * Use by
 */
public class ZbarBalanceData {

//    balance_sys 用户在平台余额
//    balance_mer 用户在商家余额

    private String balance_sys;
    private String balance_mer;
    private String muname;
    private String pksystem;

    public String getMuname() {
        return muname;
    }

    public void setMuname(String muname) {
        this.muname = muname;
    }

    public String getBalance_sys() {
        return balance_sys;
    }

    public void setBalance_sys(String balance_sys) {
        this.balance_sys = balance_sys;
    }

    public String getBalance_mer() {
        return balance_mer;
    }

    public void setBalance_mer(String balance_mer) {
        this.balance_mer = balance_mer;
    }

    public String getPksystem() {
        return pksystem;
    }

    public void setPksystem(String pksystem) {
        this.pksystem = pksystem;
    }
}
