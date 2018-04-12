package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/5.
 */
public class FundListBean {

    private String tradeName;
    private String tradeTime;
    private String tradeLogo;
    private String tradeMoney;
    private String tradeNum;

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    private String pkid;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getTradeLogo() {
        return tradeLogo;
    }

    public void setTradeLogo(String tradeLogo) {
        this.tradeLogo = tradeLogo;
    }

    public String getTradeMoney() {
        return tradeMoney;
    }

    public void setTradeMoney(String tradeMoney) {
        this.tradeMoney = tradeMoney;
    }

    public String getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
    }
}
