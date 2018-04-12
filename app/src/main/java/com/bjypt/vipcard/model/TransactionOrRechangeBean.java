package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/5/19.
 */
public class TransactionOrRechangeBean {
    private String logo_url;

    private String trade_desc;

    private String trade_time;

    private String trade_money;
    private String status_desc;

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getTrade_desc() {
        return trade_desc;
    }

    public void setTrade_desc(String trade_desc) {
        this.trade_desc = trade_desc;
    }

    public String getTrade_time() {
        return trade_time;
    }

    public void setTrade_time(String trade_time) {
        this.trade_time = trade_time;
    }

    public String getTrade_money() {
        return trade_money;
    }

    public void setTrade_money(String trade_money) {
        this.trade_money = trade_money;
    }

    public String getStatus_desc() {
        return status_desc;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }
}
