package com.bjypt.vipcard.model;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/27
 * Use by
 */
public class SystemInfomationData {

    private String balance;
    private String isBindingBank;
    private String pkmuser;
    /**
     * historytotalinterest：余额累计收益
     interest：昨日收益
     grade*/
    private String historytotalinterest;
    private String interest;

    public String getWholebalance() {
        return wholebalance;
    }

    public void setWholebalance(String wholebalance) {
        this.wholebalance = wholebalance;
    }

    private String wholebalance;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    private String grade;

    public String getHistorytotalinterest() {
        return historytotalinterest;
    }

    public void setHistorytotalinterest(String historytotalinterest) {
        this.historytotalinterest = historytotalinterest;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getIsBindingBank() {
        return isBindingBank;
    }

    public void setIsBindingBank(String isBindingBank) {
        this.isBindingBank = isBindingBank;
    }

    public String getPkmuser() {
        return pkmuser;
    }

    public void setPkmuser(String pkmuser) {
        this.pkmuser = pkmuser;
    }
}
