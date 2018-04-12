package com.bjypt.vipcard.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/10.
 */

public class CitizenCardListData {
    private int cardid;
    private String cardnum;
    private int categoryid;
    private String category_name;
    private String card_pic;
    private int status;

    public int getRecharge_power() {
        return recharge_power;
    }

    public void setRecharge_power(int recharge_power) {
        this.recharge_power = recharge_power;
    }

    private int recharge_power;

    public int getWithdraw_power() {
        return withdraw_power;
    }

    public void setWithdraw_power(int withdraw_power) {
        this.withdraw_power = withdraw_power;
    }

    private int withdraw_power;
    private String amount;
    private String virtual_amount;
    private String bind_time;
    private String showCardNum;
    private String card_gift;
    private ArrayList<CardManageBanner> adlist;
    private String pkmuser;
    private String muname;

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }

    public String getMuname() {
        return muname;
    }

    public void setMuname(String muname) {
        this.muname = muname;
    }

    public ArrayList<CardManageBanner> getAdlist() {
        return adlist;
    }

    public void setAdlist(ArrayList<CardManageBanner> adlist) {
        this.adlist = adlist;
    }

    public String getCard_gift() {
        return card_gift;
    }

    public void setCard_gift(String card_gift) {
        this.card_gift = card_gift;
    }

    public String getShowCardNum() {
        return showCardNum;
    }

    public void setShowCardNum(String showCardNum) {
        this.showCardNum = showCardNum;
    }

    public String getBind_time() {
        return bind_time;
    }

    public void setBind_time(String bind_time) {
        this.bind_time = bind_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVirtual_amount() {
        return virtual_amount;
    }

    public void setVirtual_amount(String virtual_amount) {
        this.virtual_amount = virtual_amount;
    }

    public int getCardid() {
        return cardid;
    }

    public void setCardid(int cardid) {
        this.cardid = cardid;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCard_pic() {
        return card_pic;
    }

    public void setCard_pic(String card_pic) {
        this.card_pic = card_pic;
    }
}
