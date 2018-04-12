package com.bjypt.vipcard.model;

/**
 * Created by Administrator on 2016/4/20.
 */
public class VipCenterInMarchantResultData {

    /* "pkmuser": "61ffdb6c29fd46d8ad1572bbc42ef937",
            "muname": "女人当家",
            "logo": "merchantLogo1453947032365.jpg",
            "balance": 218.21,
            "totalinterest": 0.92,
            "interest": 0.06,
            "grade": "普通会员",
            "discount": 0.9*/
    private String pkmuser;
    private String muname;
    private String logo;
    private String balance;
    private String totalinterest;
    private String interest;
    private String grade;
    private String discount;
    private String distance;
    private String historytotalinterest;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }


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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTotalinterest() {
        return totalinterest;
    }

    public void setTotalinterest(String totalinterest) {
        this.totalinterest = totalinterest;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getHistorytotalinterest() {
        return historytotalinterest;
    }

    public void setHistorytotalinterest(String historytotalinterest) {
        this.historytotalinterest = historytotalinterest;
    }
}